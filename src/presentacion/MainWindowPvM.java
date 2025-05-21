package presentacion;

import dominio.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MainWindowPvM extends JFrame implements BattleGUIListener {
    private BattlePanel battlePanel;
    private ActionPanelPvM actionPanel;
    private BattlePvM battle;
    private JDialog messageDialog;
    private JLabel turnIndicator;
    private boolean isBattlePaused = false;

    public MainWindowPvM(HumanTrainer player, AITrainer machine) {
        // Configuración de la ventana principal
        setTitle("Pokémon Esmeralda - Jugador vs Máquina");
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

        // Inicializar la batalla
        this.battle = new BattlePvM(player, machine);
        this.battlePanel = new BattlePanel(player, machine);

        // ActionPanel solo para el jugador humano
        this.actionPanel = new ActionPanelPvM(this, player);

        // Configuramos el listener de la batalla
        this.battle.setListener(this);

        add(battlePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // Diálogo de mensajes
        messageDialog = new JDialog(this, "", false);
        messageDialog.setUndecorated(true);
        messageDialog.setSize(300, 100);
        messageDialog.setLocationRelativeTo(this);

        // Indicador de turno
        turnIndicator = new JLabel("", SwingConstants.CENTER);
        turnIndicator.setFont(new Font("Pokemon GB", Font.BOLD, 16));
        turnIndicator.setOpaque(true);
        turnIndicator.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(turnIndicator, BorderLayout.NORTH);

        // Barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");

        JMenuItem saveItem = new JMenuItem("Guardar Partida");
        saveItem.addActionListener(e -> guardarPartida());

        JMenuItem loadItem = new JMenuItem("Cargar Partida");
        loadItem.addActionListener(e -> cargarPartida());

        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        centerWindow();
        setVisible(true);
        MusicManager.playMusic("musica/Battle_music.wav");
    }

    private void guardarPartida() {
        JFileChooser fileChooser = new JFileChooser() {
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new NotSerializableException("JFileChooser no debe ser serializado");
            }
        };
        fileChooser.setDialogTitle("Guardar partida");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".pokemon")) {
                fileToSave = new File(fileToSave.getPath() + ".pokemon");
            }

            try {
                battle.guardarPartida(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Partida guardada con éxito",
                        "Guardado", JOptionPane.INFORMATION_MESSAGE);
            } catch (POOBkemonException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error inesperado al guardar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarPartida() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar partida");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                BattlePvM nuevaBatalla = BattlePvM.cargarPartida(fileToLoad.getAbsolutePath());
                this.battle = nuevaBatalla;
                this.battle.setListener(this);
                rebuildUIAfterLoad();
                JOptionPane.showMessageDialog(this, "Partida cargada con éxito",
                        "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void rebuildUIAfterLoad() {
        getContentPane().removeAll();
        this.battlePanel = new BattlePanel(battle.getEntrenador1(), battle.getEntrenador2());
        this.actionPanel = new ActionPanelPvM(this, battle.getTurnoActual());
        battlePanel.addPropertyChangeListener("pauseState", evt -> {
            isBattlePaused = (boolean) evt.getNewValue();
            if (isBattlePaused) {
                battle.cancelarTemporizador();
                turnIndicator.setText("JUEGO EN PAUSA");
                turnIndicator.setForeground(Color.YELLOW);
                turnIndicator.setBackground(Color.BLACK);
            } else {
                updateTurnIndicator(battle.getTurnoActual());
                if (battle.getTurnoActual() instanceof HumanTrainer) {
                    battle.iniciarTemporizadorTurno();
                }
            }
        });
        add(turnIndicator, BorderLayout.NORTH);
        add(battlePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        updateTurnIndicator(battle.getTurnoActual());
        updateUI();
        revalidate();
        repaint();
    }

    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2,
                (screenSize.height - getHeight()) / 2);
    }

    public void startBattle() {
        battle.iniciar();
    }

    // Método para actualizar el panel de acciones según el turno
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
    public void onTurnStarted(Trainer trainer) {
        updateTurnIndicator(trainer);
        actionPanel.setCurrentPlayer(trainer);
        updateUI();
        if (trainer instanceof HumanTrainer) {
            actionPanel.addBattleText("¿Qué debería hacer " + trainer.getPokemonActivo().getNombre() + "?");
        }
    }

    private void updateTurnIndicator(Trainer currentTrainer) {
        if (currentTrainer instanceof HumanTrainer) {
            turnIndicator.setText("Turno de " + currentTrainer.getNombre() + " (Jugador)");
            turnIndicator.setForeground(Color.WHITE);
            turnIndicator.setBackground(Color.BLUE);
        } else {
            turnIndicator.setText("Turno de " + currentTrainer.getNombre() + " (Máquina)");
            turnIndicator.setForeground(Color.WHITE);
            turnIndicator.setBackground(Color.RED);
        }
    }

    @Override
    public void onTurnEnded(Trainer trainer) {}

    @Override
    public void onPokemonDebilitado(Trainer trainer) {
        actionPanel.addBattleText(trainer.getPokemonActivo().getNombre() + " se ha debilitado!");
        if (trainer instanceof HumanTrainer) {
            actionPanel.setCurrentPlayer((HumanTrainer) battle.getEntrenador1());
            SwingUtilities.invokeLater(() -> {
                actionPanel.showSwitchOptions();
            });
        }
        updateUI();
    }

    @Override
    public void onPokemonChanged(Trainer trainer, String message) {
        battlePanel.updatePokemonStats();
        actionPanel.addBattleText(message);
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
    public void onMoveUsed(Trainer trainer, String result) {
        actionPanel.addBattleText(result);
        if (trainer instanceof AITrainer) {
            actionPanel.disableButtons();
            Timer timer = new Timer(1500, e -> actionPanel.enableButtons());
            timer.setRepeats(false);
            timer.start();
        }
        
        updateUI();
    }

    @Override
    public void onItemUsed(Trainer trainer, String result) {
        actionPanel.addBattleText(result);
        updateUI();
    }

    @Override
    public void onPokemonRevivido(Trainer trainer, Pokemon pokemon) {
        battlePanel.updatePokemonStats();
        actionPanel.addBattleText(trainer.getNombre() + " ha revivido a " + pokemon.getNombre());
        updateUI();
    }

    public void useReviveItem(int itemIndex, int pokemonIndex) {
        battle.usarItemRevivir(itemIndex, pokemonIndex);
    }

    public void attackSelected(int moveIndex) {
        try {
            String message = battle.movimientoSeleccionado(moveIndex);
            actionPanel.addBattleText(message);
            updateUI();

            actionPanel.disableButtons();

            // El turno se finalizará automáticamente por el listener en BattlePvM
            Timer messageTimer = new Timer(1500, e -> {
                actionPanel.enableButtons();
            });
            messageTimer.setRepeats(false);
            messageTimer.start();
        } catch (POOBkemonException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            actionPanel.enableButtons();
        }
    }

    public void switchPokemonSelected(int pokemonIndex) {
        try {
            String message = battle.cambioPokemonSeleccionado(pokemonIndex);
            actionPanel.addBattleText(message);
            updateUI();

            actionPanel.disableButtons();

            Timer messageTimer = new Timer(1500, e -> {
                battle.finalizarTurno();
                actionPanel.enableButtons();
            });
            messageTimer.setRepeats(false);
            messageTimer.start();
        } catch (POOBkemonException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            actionPanel.enableButtons();
        }
    }

    public void useItemSelected(int itemIndex) {
        try {
            String message = battle.itemSeleccionado(itemIndex);
            actionPanel.addBattleText(message);
            updateUI();

            actionPanel.disableButtons();

            Timer messageTimer = new Timer(1500, e -> {
                battle.finalizarTurno();
                actionPanel.enableButtons();
            });
            messageTimer.setRepeats(false);
            messageTimer.start();
        } catch (POOBkemonException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            actionPanel.enableButtons();
        }
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

    public BattlePvM getBattle() {
        return battle;
    }
    public void setBattle(BattlePvM battle) {
        this.battle = battle;
    }
}