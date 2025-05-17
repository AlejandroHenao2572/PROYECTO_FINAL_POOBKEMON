package presentacion;

import dominio.*;
import javax.swing.*;
import java.awt.*;
//prueba de commit
public class MainWindow extends JFrame implements BattleGUIListener {
    private BattlePanel battlePanel;
    private ActionPanel actionPanel;
    private Battle battle;
    private JDialog messageDialog;
    private JLabel turnIndicator;
    private boolean isBattlePaused = false;

        public MainWindow(HumanTrainer player1, HumanTrainer player2) {
        setTitle("Pokémon Esmeralda - Combate");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLayout(new BorderLayout());
        
        // Cargar fuente Pokémon
        try {
            Font pokemonFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getClassLoader().getResourceAsStream("graficos/PokemonGB.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (Exception e) {
            System.err.println("Error cargando fuente Pokémon: " + e.getMessage());
        }

        // 1. Primero inicializamos la batalla
        this.battle = new Battle(player1, player2);
        
        // 2. Luego inicializamos el battlePanel
        this.battlePanel = new BattlePanel(player1, player2);
        
        // 3. Configuramos el listener para pausa (ahora battle ya está inicializado)
        this.battlePanel.addPropertyChangeListener("pauseState", evt -> {
            isBattlePaused = (boolean) evt.getNewValue();
            if (isBattlePaused) {
                battle.cancelarTemporizador();
                if (turnIndicator != null) {
                    turnIndicator.setText("JUEGO EN PAUSA");
                    turnIndicator.setForeground(Color.YELLOW);
                    turnIndicator.setBackground(Color.BLACK);
                }
            } else {
                updateTurnIndicator(battle.getTurnoActual());
                if (battle.getTurnoActual() instanceof HumanTrainer) {
                    battle.iniciarTemporizadorTurno();
                }
            }
        });

        // 4. Configuramos el listener de la batalla
        this.battle.setListener(this);
        
        // 5. Mostrar el diálogo de lanzamiento de moneda (ahora battle está inicializado)
        if(battle.getTurnoActual() == player1){
            CoinFlipDialog coinFlip = new CoinFlipDialog(this, true);
            coinFlip.setVisible(true); 
        }
        else {
            CoinFlipDialog coinFlip = new CoinFlipDialog(this, false);
            coinFlip.setVisible(true); 
        }
        
        // 6. Inicializamos el actionPanel
        this.actionPanel = new ActionPanel(this, player1);
        
        // 7. Añadimos los componentes al JFrame
        add(battlePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Configurar diálogo de mensajes
        messageDialog = new JDialog(this, "", false);
        messageDialog.setUndecorated(true);
        messageDialog.setSize(300, 100);
        messageDialog.setLocationRelativeTo(this);

        // Crear e inicializar el indicador de turno
        turnIndicator = new JLabel("", SwingConstants.CENTER);
        turnIndicator.setFont(new Font("Pokemon GB", Font.BOLD, 16));
        turnIndicator.setOpaque(true);
        turnIndicator.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Añadir el indicador en la parte superior
        add(turnIndicator, BorderLayout.NORTH);
        
        centerWindow();
        setVisible(true);
    }
    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, 
                   (screenSize.height - getHeight()) / 2);
    }

    public void startBattle() {
        battle.iniciar();
    }

    public void updateUI() {
        if (!isBattlePaused) {
            battlePanel.updatePokemonStats();
            actionPanel.setCurrentPlayer(battle.getTurnoActual());
            updateTurnIndicator(battle.getTurnoActual());
            repaint();
        }
    }

    @Override
    public void onBattleStarted() {
        updateUI();
    }

     @Override
    public void onTurnStarted(HumanTrainer trainer) {
        // Actualizar el indicador de turno
        updateTurnIndicator(trainer);
        
        actionPanel.setCurrentPlayer(trainer);
        updateUI();
        actionPanel.addBattleText("¿Qué debería hacer " + trainer.getPokemonActivo().getNombre() + "?");
    }

    private void updateTurnIndicator(Trainer currentTrainer) {
        if (currentTrainer == battle.getEntrenador1()) {
            turnIndicator.setText("Turno de " + currentTrainer.getNombre());
            turnIndicator.setForeground(Color.WHITE);
            turnIndicator.setBackground(Color.RED);
        } else {
            turnIndicator.setText("Turno de " + currentTrainer.getNombre());
            turnIndicator.setForeground(Color.WHITE);
            turnIndicator.setBackground(Color.BLUE);
        }
    }

    @Override
    public void onTurnEnded(HumanTrainer trainer) {
    }

    @Override
    public void onPokemonDebilitado(Trainer trainer) {
        // Mostrar mensaje en la interfaz
        actionPanel.addBattleText(trainer.getPokemonActivo().getNombre() + " se ha debilitado!");
        
        // Mostrar opciones de cambio solo si es el turno del jugador afectado
        if (trainer == battle.getTurnoActual()) {
            SwingUtilities.invokeLater(() -> {
                actionPanel.showSwitchOptions();
            });
        }
        
        updateUI();
    }

    @Override
    public void onPokemonChanged(Trainer trainer) {
        // Actualizar la interfaz con el nuevo Pokémon
        battlePanel.updatePokemonStats();
        actionPanel.addBattleText(trainer.getNombre() + " ha cambiado a " + 
                            trainer.getPokemonActivo().getNombre());
        updateUI();
    }

    @Override
    public void onBattleEnded(Trainer winner) {
        showMessage(winner.getNombre() + " ha ganado la batalla!");
        
        Object[] options = {"Nueva Batalla", "Salir"};
        int choice = JOptionPane.showOptionDialog(this,
            "¿Qué deseas hacer?",
            "Batalla terminada",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            dispose();
            //new PokemonBattleGame().startNewBattle();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onPokemonRevivido(Trainer trainer, Pokemon pokemon) {
        // Actualizar la interfaz
        battlePanel.updatePokemonStats();
        actionPanel.addBattleText(trainer.getNombre() + " ha revivido a " + pokemon.getNombre());
        updateUI();
    }

    public void useReviveItem(int itemIndex, int pokemonIndex) {
        battle.usarItemRevivir(itemIndex, pokemonIndex);
    }

    public void attackSelected(int moveIndex) {
        battle.movimientoSeleccionado(moveIndex);
    }

    public void switchPokemonSelected(int pokemonIndex) {
        battle.cambioPokemonSeleccionado(pokemonIndex);
    }

    public void useItemSelected(int itemIndex) {
        battle.itemSeleccionado(itemIndex);
    }

    public void runAway() {
        actionPanel.addBattleText("¡Has huido del combate!");
        showMessage("Escapaste con éxito");
        System.exit(0);
    }

    private void showMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(this, "");
        
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        dialog.setVisible(true);
    }

    public Battle getBattle() {
        return battle;
    }
}