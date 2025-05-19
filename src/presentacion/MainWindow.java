package presentacion;

import dominio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
            
            // 5. Mostrar el diálogo de lanzamiento de moneda
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

            // Crear barra de menú
            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("Archivo");
            
            // Opción Guardar Partida
            JMenuItem saveItem = new JMenuItem("Guardar Partida");
            saveItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardarPartida();
                }
            });
            
            // Opción Cargar Partida
            JMenuItem loadItem = new JMenuItem("Cargar Partida");
            loadItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cargarPartida();
                }
            });
            
            // Opción Salir
            JMenuItem exitItem = new JMenuItem("Salir");
            exitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            
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
        // fileChooser.setCurrentDirectory(new File("ruta/a/tu/directorio"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Asegurarse de que tenga la extensión correcta
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
                // 1. Cargar el modelo
                Battle nuevaBatalla = Battle.cargarPartida(fileToLoad.getAbsolutePath());
                
                // 2. Actualizar referencia
                this.battle = nuevaBatalla;
                this.battle.setListener(this);
                
                // 3. Reconstruir completamente la UI
                rebuildUIAfterLoad();
                
                // 4. Mostrar mensaje de éxito
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
        // 1. Remover componentes antiguos
        getContentPane().removeAll();
        
        // 2. Recrear paneles con los nuevos datos
        this.battlePanel = new BattlePanel(battle.getEntrenador1(), battle.getEntrenador2());
        this.actionPanel = new ActionPanel(this, battle.getTurnoActual());
        
        // 3. Reconfigurar listeners
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
        
        // 4. Reagregar componentes
        add(turnIndicator, BorderLayout.NORTH);
        add(battlePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        
        // 5. Actualizar UI
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
    public void onPokemonChanged(Trainer trainer, String message) {
        // Actualizar la interfaz con el nuevo Pokémon
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
        // Actualizar el panel de acción con el resultado
        actionPanel.addBattleText(result);
        updateUI();
    }

    @Override
    public void onItemUsed(Trainer trainer, String result) {
        // Actualizar el panel de acción con el resultado
        actionPanel.addBattleText(result);
        updateUI();
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
        try {
            String message = battle.movimientoSeleccionado(moveIndex);
            actionPanel.addBattleText(message);
            updateUI();

            actionPanel.disableButtons();

            // Temporizador para mostrar el mensaje antes de continuar
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

    public Battle getBattle() {
        return battle;
    }
    public void setBattle(Battle battle) {
        this.battle = battle;
    }
}