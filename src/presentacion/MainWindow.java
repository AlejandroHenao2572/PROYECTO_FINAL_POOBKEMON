package presentacion;

import dominio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Ventana principal de la aplicacion de batalla Pokemon
 * Gestiona la interfaz grafica, paneles de batalla y acciones del usuario
 * Implementa BattleGUIListener para recibir eventos de la batalla
 * Permite guardar, cargar partidas y mostrar mensajes al usuario
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class MainWindow extends JFrame implements BattleGUIListener {
    // Panel grafico principal de la batalla
    private BattlePanel battlePanel;
    // Panel de acciones del jugador
    private ActionPanel actionPanel;
    // Modelo de la batalla
    private Battle battle;
    // Dialogo para mostrar mensajes
    private JDialog messageDialog;
    // Indicador de turno en la interfaz
    private JLabel turnIndicator;
    // Indica si la batalla esta pausada
    private boolean isBattlePaused = false;

    /**
     * Constructor de la ventana principal
     * Inicializa la interfaz, paneles y la batalla
     * 
     * @param player1 Primer entrenador humano
     * @param player2 Segundo entrenador humano
     */
    public MainWindow(HumanTrainer player1, HumanTrainer player2) {
        setTitle("Pokemon Esmeralda - Combate");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLayout(new BorderLayout());
        
        // Cargar fuente personalizada para la interfaz
        try {
            Font pokemonFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getClassLoader().getResourceAsStream("graficos/PokemonGB.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (Exception e) {
            System.err.println("Error cargando fuente Pokemon: " + e.getMessage());
        }

        // 1. Inicializar la batalla
        this.battle = new Battle(player1, player2);
        
        // 2. Inicializar el panel de batalla
        this.battlePanel = new BattlePanel(player1, player2);
        
        // 3. Configurar listener para pausa
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

        // 4. Configurar listener de la batalla
        this.battle.setListener(this);
        
        // 5. Mostrar dialogo de lanzamiento de moneda
        if(battle.getTurnoActual() == player1){
            CoinFlipDialog coinFlip = new CoinFlipDialog(this, true);
            coinFlip.setVisible(true); 
        }
        else {
            CoinFlipDialog coinFlip = new CoinFlipDialog(this, false);
            coinFlip.setVisible(true); 
        }
        
        // 6. Inicializar el panel de acciones
        this.actionPanel = new ActionPanel(this, player1);
        
        // 7. Añadir componentes al JFrame
        add(battlePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Configurar dialogo de mensajes
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

        // Crear barra de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");
        
        // Opcion Guardar Partida
        JMenuItem saveItem = new JMenuItem("Guardar Partida");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPartida();
            }
        });
        
        // Opcion Cargar Partida
        JMenuItem loadItem = new JMenuItem("Cargar Partida");
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarPartida();
            }
        });
        
        // Opcion Salir
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

    /**
     * Muestra el dialogo para guardar la partida y guarda el estado actual
     */
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
                JOptionPane.showMessageDialog(this, "Partida guardada con exito",
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
        
    /**
     * Muestra el dialogo para cargar una partida y actualiza la interfaz
     */
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
                
                // 4. Mostrar mensaje de exito
                JOptionPane.showMessageDialog(this, "Partida cargada con exito", 
                    "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Reconstruye la interfaz grafica despues de cargar una partida
     */
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

    /**
     * Centra la ventana en la pantalla
     */
    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, 
                   (screenSize.height - getHeight()) / 2);
    }

    /**
     * Inicia la batalla
     */
    public void startBattle() {
        battle.iniciar();
    }

    /**
     * Actualiza la interfaz grafica segun el estado de la batalla
     */
    public void updateUI() {
        if (!isBattlePaused) {
            battlePanel.updatePokemonStats();
            actionPanel.setCurrentPlayer(battle.getTurnoActual());
            updateTurnIndicator(battle.getTurnoActual());
            repaint();
        }
    }

    /**
     * Evento cuando inicia la batalla
     */
    @Override
    public void onBattleStarted() {
        updateUI();
    }

    /**
     * Evento cuando inicia el turno de un entrenador
     * @param trainer Entrenador que inicia su turno
     */
    @Override
    public void onTurnStarted(Trainer trainer) {
        // Actualizar el indicador de turno
        updateTurnIndicator(trainer);
        
        actionPanel.setCurrentPlayer(trainer);
        updateUI();
        actionPanel.addBattleText("¿Que deberia hacer " + trainer.getPokemonActivo().getNombre() + "?");
    }

    /**
     * Actualiza el indicador de turno en la interfaz
     * @param currentTrainer Entrenador actual
     */
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

    /**
     * Evento cuando termina el turno de un entrenador
     */
    @Override
    public void onTurnEnded(Trainer trainer) {
    }

    /**
     * Evento cuando un Pokemon es debilitado
     * @param trainer Entrenador cuyo Pokemon fue debilitado
     */
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

    /**
     * Evento cuando un entrenador cambia de Pokemon
     * @param trainer Entrenador que realiza el cambio
     * @param message Mensaje descriptivo del cambio
     */
    @Override
    public void onPokemonChanged(Trainer trainer, String message) {
        // Actualizar la interfaz con el nuevo Pokemon
        battlePanel.updatePokemonStats();
        actionPanel.addBattleText(message);
        updateUI();
    }

    /**
     * Evento cuando termina la batalla
     * @param winner Entrenador que gano la batalla
     */
    @Override
    public void onBattleEnded(Trainer winner) {
        showMessage(winner.getNombre() + " ha ganado la batalla!");
        
        Object[] options = {"Nueva Batalla", "Salir"};
        int choice = JOptionPane.showOptionDialog(this,
            "¿Que deseas hacer?",
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

    /**
     * Evento cuando un entrenador usa un movimiento
     * @param trainer Entrenador que usa el movimiento
     * @param result Mensaje descriptivo del resultado
     */
    @Override
    public void onMoveUsed(Trainer trainer, String result) {
        // Actualizar el panel de accion con el resultado
        actionPanel.addBattleText(result);
        updateUI();
    }

    /**
     * Evento cuando un entrenador usa un item
     * @param trainer Entrenador que usa el item
     * @param result Mensaje descriptivo del resultado
     */
    @Override
    public void onItemUsed(Trainer trainer, String result) {
        // Actualizar el panel de accion con el resultado
        actionPanel.addBattleText(result);
        updateUI();
    }

    /**
     * Evento cuando un Pokemon es revivido con un item
     * @param trainer Entrenador que revive al Pokemon
     * @param pokemon Pokemon que fue revivido
     */
    @Override
    public void onPokemonRevivido(Trainer trainer, Pokemon pokemon) {
        // Actualizar la interfaz
        battlePanel.updatePokemonStats();
        actionPanel.addBattleText(trainer.getNombre() + " ha revivido a " + pokemon.getNombre());
        updateUI();
    }

    /**
     * Llama al metodo para usar un item de revivir
     * @param itemIndex Indice del item en la mochila
     * @param pokemonIndex Indice del Pokemon a revivir
     */
    public void useReviveItem(int itemIndex, int pokemonIndex) {
        battle.usarItemRevivir(itemIndex, pokemonIndex);
    }

    /**
     * Llama al metodo para seleccionar un ataque
     * @param moveIndex Indice del movimiento a usar
     */
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

    /**
     * Llama al metodo para cambiar de Pokemon
     * @param pokemonIndex Indice del Pokemon a cambiar
     */
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

    /**
     * Llama al metodo para usar un item
     * @param itemIndex Indice del item a usar
     */
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

    /**
     * Permite huir del combate y cerrar la aplicacion
     */
    public void runAway() {
        actionPanel.addBattleText("¡Has huido del combate!");
        showMessage("Escapaste con exito");
        System.exit(0);
    }

    /**
     * Muestra un mensaje emergente en pantalla
     * @param message Mensaje a mostrar
     */
    private void showMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(this, "");
        
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        dialog.setVisible(true);
    }

    /**
     * Devuelve la batalla actual
     * @return battle objeto Battle actual
     */
    public Battle getBattle() {
        return battle;
    }

    /**
     * Establece la batalla actual
     * @param battle Nueva batalla a establecer
     */
    public void setBattle(Battle battle) {
        this.battle = battle;
    }
}