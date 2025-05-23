package presentacion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Clase principal del panel de juego que implementa la logica del juego y la interfaz de usuario
 * Gestiona los estados del juego menu principal seleccion de modo juego
 * dibuja los elementos en pantalla y actualiza el estado del juego
 * Implementa la interfaz Runnable para ejecutarse en un hilo separado
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class GamePanel extends JPanel implements Runnable {

    // Configuracion de pantalla
    final int originalTitleSize = 16;
    final int scale = 3;
    public final int titleSize = originalTitleSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = titleSize * maxScreenCol;
    public final int screenHeight = titleSize * maxScreenRow;
    private Font pokemonFont;

    // Control del juego
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);
    TileManager tileM = new TileManager(this);

    // Estados del juego
    enum GameState {
        MAIN_MENU,
        PVP_MODE_SELECTION,
        PVM_MODE_SELECTION,
        MVM_MODE_SELECTION,
        IN_GAME,

    }
    private GameState currentState = GameState.MAIN_MENU;

    // Areas de botones del menu principal
    Rectangle pvpZone;
    Rectangle pvmZone;
    Rectangle mvmZone;

    // Areas de botones de seleccion de modo PvP
    Rectangle normalModeZone;
    Rectangle survivalModeZone;

    // Nueva area para el boton de volver
    Rectangle backZone;

    // Nuevas areas para los botones de selección de tipo de maquina
    Rectangle defensiveTrainerZone;
    Rectangle attackingTrainerZone;
    Rectangle chaningTrainerZone;
    Rectangle expertTrainerZone;
    Rectangle backZonePVM;
    Rectangle backZoneMVM;

    // Fuentes
    Font buttonFont = new Font("Arial", Font.BOLD, 16);
    Font titleFont = new Font("Arial", Font.BOLD, 24);
    Font subtitleFont = new Font("Arial", Font.BOLD, 20);
    FontMetrics fontMetrics;

    // Imagenes
    private BufferedImage titleImage;
    private int titleWidth;
    private int titleHeight;

    // Configuracion de FPS
    int FPS = 60;

    // Referencia al JFrame contenedor
    private JFrame parentFrame;

    /**
     * Constructor de la clase GamePanel
     * Inicializa la configuracion de la pantalla, el KeyHandler, el TileManager,
     * y carga los recursos del juego. Tambien inicializa las metricas de la fuente y calcula
     * el tamaño de los botones del menu. Agrega listeners de mouse para seleccionar modos con click.
     *
     * @param parentFrame El JFrame contenedor de este panel
     */
    public GamePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        loadPokemonFont();
        buttonFont = pokemonFont.deriveFont(14f);
        titleFont = pokemonFont.deriveFont(24f);
        subtitleFont = pokemonFont.deriveFont(16f);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Crear barra de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");

        JMenuItem openItem = new JMenuItem("Abrir Partida");
        openItem.addActionListener(e -> cargarPartida());

        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> {
            if (parentFrame != null) parentFrame.dispose();
        });

        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        if (parentFrame instanceof JFrame) {
            ((JFrame) parentFrame).setJMenuBar(menuBar);
        }
        loadResources();

        fontMetrics = this.getFontMetrics(buttonFont);
        calculateButtonSizes();
        MusicManager.playMusic("musica/Normal_music.wav");
        this.removeKeyListener(keyH);
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleMenuClick(e.getPoint());
            }
        });
    }

    /**
     * Carga los recursos graficos del juego como la imagen del titulo
     * Si ocurre un error durante la carga se imprime un mensaje de error
     */
    private void loadResources() {
        try {
            // Cargar imagen del titulo
            titleImage = ImageIO.read(getClass().getResourceAsStream("/graficos/POOBkemon_logo.png"));
            double scaleFactor = (screenWidth * 0.7) / titleImage.getWidth();
            titleWidth = (int)(titleImage.getWidth() * scaleFactor);
            titleHeight = (int)(titleImage.getHeight() * scaleFactor);
        } catch (IOException e) {
            System.err.println("Error al cargar recursos graficos: " + e.getMessage());
            titleImage = null;
        }
    }


    private void cargarPartida() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir partida");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToLoad = fileChooser.getSelectedFile();
            try {
                dominio.Battle battle = dominio.Battle.cargarPartida(fileToLoad.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Partida cargada con éxito", 
                    "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
                // Cerrar el JFrame actual
                if (parentFrame != null) parentFrame.dispose();
                // Abrir la ventana de batalla con la partida cargada
                MainWindow mainWindow = new MainWindow(battle.getEntrenador1(), battle.getEntrenador2());
                mainWindow.setVisible(true);
                mainWindow.startBattle();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Inicia el hilo del juego
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Metodo principal del hilo del juego
     * Implementa el bucle del juego actualizando y repintando el panel a la velocidad de FPS
     */
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                repaint();
                delta--;
            }
        }
    }

    /**
     * Maneja la seleccion de elementos del menu principal y de seleccion de modo PvP
     * usando la posicion del click del mouse sobre los botones
     */
    private void handleMenuClick(Point clickPoint) {
        switch(currentState) {
            case MAIN_MENU:
                if (pvpZone.contains(clickPoint)) {
                    currentState = GameState.PVP_MODE_SELECTION;
                } else if (pvmZone.contains(clickPoint)) {
                    currentState = GameState.PVM_MODE_SELECTION;
                } else if (mvmZone.contains(clickPoint)) {
                    currentState = GameState.IN_GAME;
                    MvMSetUp();
                }
                break;

            case PVP_MODE_SELECTION:
                if (normalModeZone.contains(clickPoint)) {
                    startPvPNormalMode();
                } else if (survivalModeZone.contains(clickPoint)) {
                    startPvPSurvivalMode();
                } else if (backZone.contains(clickPoint)) {
                    currentState = GameState.MAIN_MENU;
                }
                break;

            case PVM_MODE_SELECTION:
                if (defensiveTrainerZone.contains(clickPoint)) {
                    PvMSetUp("defensiveTrainer");
                } else if (attackingTrainerZone.contains(clickPoint)) {
                    PvMSetUp("attackingTrainer");
                } else if (chaningTrainerZone.contains(clickPoint)) {
                    PvMSetUp("chaningTrainer");
                } else if (expertTrainerZone.contains(clickPoint)) {
                    PvMSetUp("expertTrainer");
                } else if (backZonePVM.contains(clickPoint)) {
                    currentState = GameState.MAIN_MENU;
                }
                break;

            case MVM_MODE_SELECTION:
                MvMSetUp();
                break;

            case IN_GAME:
                break;
        }
    }

    /**
     * Inicia el modo de juego PvP Normal
     * Cierra la ventana actual y crea una nueva instancia de PvPNormalSetUp
     */
    private void startPvPNormalMode() {
        // Cerrar la ventana actual de forma segura
        SwingUtilities.invokeLater(() -> {
            if (parentFrame != null) {
                parentFrame.dispose();
            }

            // Crear nueva instancia de PvPNormalSetUp
            PvPNormalSetUp pvpSetup = new PvPNormalSetUp();
            pvpSetup.setVisible(true);
        });
    }

    /**
     * Inicia el modo de juego PvP Supervivencia 
     * Cierra la ventana actual y crea una nueva instancia de PvPSurvivalSetUp
     */
    private void startPvPSurvivalMode() {
        // Cerrar la ventana actual de forma segura
        SwingUtilities.invokeLater(() -> {
            if (parentFrame != null) {
                parentFrame.dispose();
            }

            // Crear nueva instancia de PvPSurpervivenciaSetUp
            PvPSupervivenciaSetUp pvpSetup = new PvPSupervivenciaSetUp();
            pvpSetup.setVisible(true);
        });
    }

    /**
     * Inicia el modo de juego PvM
     * Cierra la ventana actual y crea una nueva instancia de PvMSetUp
     *
     * @param trainerType El tipo de entrenador a usar en el modo PvM
     */
    private void PvMSetUp(String trainerType) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (parentFrame != null) {
                    parentFrame.dispose();
                }

                // Crear nueva instancia de PvMSetUp
                PvMSetUp pvmSetup = new PvMSetUp(trainerType);
                pvmSetup.setVisible(true);
            }
        });
    }

    /**
     * Inicia el modo de juego MvM
     * Cierra la ventana actual y crea una nueva instancia de MvMSetUp
     */
    private void MvMSetUp() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (parentFrame != null) {
                    parentFrame.dispose();
                }

                // Crear nueva instancia de MvMSetUp
                MvMSetUp mvmSetup = new MvMSetUp();
                mvmSetup.setVisible(true);
            }
        });
    }


    /**
     * Dibuja un boton en la pantalla
     *
     * @param g2    El objeto Graphics2D para dibujar
     * @param rect  El rectangulo que define la posicion y el tamaño del boton
     * @param text  El texto a mostrar en el boton
     * @param color El color de fondo del boton
     */
    private void drawButton(Graphics2D g2, Rectangle rect, String text, Color color) {
        if (rect == null) return;
        // Fondo del boton
        g2.setColor(color);
        g2.fillRect(rect.x, rect.y, rect.width, rect.height);
        // Borde pixelado 
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        g2.setColor(new Color(60, 60, 60));
        g2.drawLine(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height); // abajo
        g2.drawLine(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height); // derecha
        g2.setFont(buttonFont);
        g2.setColor(Color.WHITE);
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + (rect.height / 2) + g2.getFontMetrics().getAscent() / 2 - 5;
        g2.drawString(text, textX, textY);
    }

    /**
     * Calcula el tamaño y la posicion de los botones del menu principal y de seleccion de modo PvP
     * basandose en las metricas de la fuente
     */
    private void calculateButtonSizes() {
        if (fontMetrics == null) return;
        int mainButtonWidth = fontMetrics.stringWidth("Player vs Player") + 40;
        int mainButtonHeight = 30;
        int mainButtonSpacing = 20;
        int totalMainHeight = mainButtonHeight * 3 + mainButtonSpacing * 2;
        int startMainY = (screenHeight - totalMainHeight) / 2 + 60;
        int mainButtonX = (screenWidth - mainButtonWidth) / 2;
        pvpZone = new Rectangle(mainButtonX, startMainY, mainButtonWidth, mainButtonHeight);
        pvmZone = new Rectangle(mainButtonX, startMainY + mainButtonHeight + mainButtonSpacing, mainButtonWidth, mainButtonHeight);
        mvmZone = new Rectangle(mainButtonX, startMainY + 2 * (mainButtonHeight + mainButtonSpacing), mainButtonWidth, mainButtonHeight);

        // Botones de modo PvP en columna
        int modeButtonWidth = Math.max(
            fontMetrics.stringWidth("Modo Normal") + 25,
            fontMetrics.stringWidth("Supervivencia") + 25
        );
        int modeButtonHeight = 35;
        int modeButtonSpacing = 20;
        int totalModeHeight = modeButtonHeight * 2 + modeButtonSpacing + 40;
        int startModeY = (screenHeight - totalModeHeight) / 2 + 40;
        int modeButtonX = (screenWidth - modeButtonWidth) / 2;

        normalModeZone = new Rectangle(modeButtonX, startModeY, modeButtonWidth, modeButtonHeight);
        survivalModeZone = new Rectangle(modeButtonX, startModeY + modeButtonHeight + modeButtonSpacing, modeButtonWidth, modeButtonHeight);

        // Boton de volver debajo de los modos
        int backButtonWidth = fontMetrics.stringWidth("Volver") + 30;
        int backButtonHeight = 28;
        int backButtonX = (screenWidth - backButtonWidth) / 2;
        int backButtonY = startModeY + 2 * (modeButtonHeight + modeButtonSpacing) + 10;
        backZone = new Rectangle(backButtonX, backButtonY, backButtonWidth, backButtonHeight);

        // Botones para selección de tipo de máquina (PvM y MvM)
        int machineButtonWidth = fontMetrics.stringWidth("expertTrainer") + 40;
        int machineButtonHeight = 30;
        int machineButtonSpacing = 15;
        int totalMachineHeight = machineButtonHeight * 4 + machineButtonSpacing * 3 + 40;
        int startMachineY = (screenHeight - totalMachineHeight) / 2 + 40;
        int machineButtonX = (screenWidth - machineButtonWidth) / 2;

        defensiveTrainerZone = new Rectangle(machineButtonX, startMachineY, machineButtonWidth, machineButtonHeight);
        attackingTrainerZone = new Rectangle(machineButtonX, startMachineY + (machineButtonHeight + machineButtonSpacing), machineButtonWidth, machineButtonHeight);
        chaningTrainerZone = new Rectangle(machineButtonX, startMachineY + 2 * (machineButtonHeight + machineButtonSpacing), machineButtonWidth, machineButtonHeight);
        expertTrainerZone = new Rectangle(machineButtonX, startMachineY + 3 * (machineButtonHeight + machineButtonSpacing), machineButtonWidth, machineButtonHeight);

        // Botón de volver para PvM y MvM
        int backMachineY = startMachineY + 4 * (machineButtonHeight + machineButtonSpacing) + 10;
        backZonePVM = new Rectangle(machineButtonX, backMachineY, backButtonWidth, backButtonHeight);
        backZoneMVM = new Rectangle(machineButtonX, backMachineY, backButtonWidth, backButtonHeight);
    }

    /**
     * Sobreescribe el metodo paintComponent para dibujar el contenido del panel
     * Dibuja el fondo el titulo y la interfaz de usuario especifica del estado actual del juego
     * Tambien dibuja al jugador
     *
     * @param g El objeto Graphics para dibujar
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo
        tileM.draw(g2);

        // Dibujar titulo
        drawTitle(g2);

        // Dibujar interfaz segun estado
        switch(currentState) {
            case MAIN_MENU:
                drawMainMenu(g2);
                break;

            case PVP_MODE_SELECTION:
                drawPvpModeSelection(g2);
                break;

            case PVM_MODE_SELECTION:
                drawPvmModeSelection(g2);
                break;

            case IN_GAME:
                drawGame(g2);
                break;
        }

        g2.dispose();
    }

    /**
     * Dibuja una imagen en la pantalla en una posicion y tamaño especificos
     * 
     * @param g2 El objeto Graphics2D para dibujar
     * @param img La imagen a dibujar
     * @param x La coordenada x de la posicion superior izquierda
     * @param y La coordenada y de la posicion superior izquierda
     * @param width El ancho al que se dibujara la imagen
     * @param height La altura a la que se dibujara la imagen
     */
    private void drawImageAt(Graphics2D g2, Image img, int x, int y, int width, int height) {
        if (img != null) {
            g2.drawImage(img, x, y, width, height, null);
        }
    }

    /**
     * Dibuja el titulo del juego en la pantalla
     * Si la imagen del titulo esta cargada la dibuja centrada
     * Si no dibuja un titulo de texto de respaldo
     *
     * @param g2 El objeto Graphics2D para dibujar
     */
    private void drawTitle(Graphics2D g2) {
        if (titleImage != null) {
            // Especifica aquí la posición exacta:
            int posX = (screenWidth - titleWidth) / 2; // centrado horizontal
            int posY = -150; // por ejemplo, 20 píxeles desde arriba
            drawImageAt(g2, titleImage, posX, posY, titleWidth, titleHeight);
        } else {
            // Fallback si no hay imagen
            g2.setColor(Color.WHITE);
            g2.setFont(titleFont);
            String title = "POOBkemon";
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (screenWidth - titleWidth)/2, 70);
        }
    }

    /**
     * Dibuja la interfaz del menu principal
     * Muestra un subtitulo y los botones para seleccionar los diferentes modos de juego
     *
     * @param g2 El objeto Graphics2D para dibujar
     */
    private void drawMainMenu(Graphics2D g2) {
        // Subtitulo
        g2.setColor(Color.WHITE);
        g2.setFont(subtitleFont);
        String subtitle = "Selecciona un modo de juego";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.drawString(subtitle, (screenWidth - subtitleWidth)/2, pvpZone.y - 30);

        // Botones principales
        drawButton(g2, pvpZone, "Player vs Player",
            player.getBounds().intersects(pvpZone) ?
            new Color(255, 150, 150) : new Color(255, 100, 100));

        drawButton(g2, pvmZone, "Player vs Machine",
            player.getBounds().intersects(pvmZone) ?
            new Color(150, 200, 255) : new Color(100, 150, 255));

        drawButton(g2, mvmZone, "Machine vs Machine",
            player.getBounds().intersects(mvmZone) ?
            new Color(150, 255, 150) : new Color(100, 255, 100));
    }

    /**
     * Dibuja la interfaz de seleccion de modo PvP
     * Muestra un titulo un subtitulo y los botones para seleccionar los diferentes tipos de juego PvP
     *
     * @param g2 El objeto Graphics2D para dibujar
     */
    private void drawPvpModeSelection(Graphics2D g2) {
        // Titulo
        g2.setColor(Color.WHITE);
        g2.setFont(titleFont);
        String title = "Player vs Player";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (screenWidth - titleWidth)/2, pvpZone.y - 100);

        // Subtitulo
        g2.setFont(subtitleFont);
        String subtitle = "Elige el tipo de juego";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.drawString(subtitle, (screenWidth - subtitleWidth)/2, pvpZone.y - 60);

        // Botones de modo
        drawButton(g2, normalModeZone, "Modo Normal",
            player.getBounds().intersects(normalModeZone) ?
            new Color(200, 200, 255) : new Color(150, 150, 255));

        drawButton(g2, survivalModeZone, "Supervivencia",
            player.getBounds().intersects(survivalModeZone) ?
            new Color(255, 200, 200) : new Color(255, 150, 150));

        // Boton de volver
        drawButton(g2, backZone, "Volver",
            player.getBounds().intersects(backZone) ?
            new Color(255, 255, 180) : new Color(220, 220, 120));
    }

    // Dibuja la seleccion de tipo de maquina para PvM
    private void drawPvmModeSelection(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(titleFont);
        String title = "Player vs Machine";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (screenWidth - titleWidth)/2, defensiveTrainerZone.y - 60);

        g2.setFont(subtitleFont);
        String subtitle = "Elige el tipo de máquina";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.drawString(subtitle, (screenWidth - subtitleWidth)/2, defensiveTrainerZone.y - 30);

        // Colores mas oscuros para mejor contraste
        drawButton(g2, defensiveTrainerZone, "defensiveTrainer", new Color(60, 60, 120));
        drawButton(g2, attackingTrainerZone, "attackingTrainer", new Color(120, 60, 60));
        drawButton(g2, chaningTrainerZone, "chaningTrainer", new Color(120, 120, 60));
        drawButton(g2, expertTrainerZone, "expertTrainer", new Color(60, 120, 60));
        drawButton(g2, backZonePVM, "Volver", new Color(100, 100, 40));
    }

    // Dibuja la selección de tipo de maquina para MvM
    private void drawMvmModeSelection(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(titleFont);
        String title = "Machine vs Machine";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (screenWidth - titleWidth)/2, defensiveTrainerZone.y - 60);

        g2.setFont(subtitleFont);
        String subtitle = "Elige el tipo de máquina";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.drawString(subtitle, (screenWidth - subtitleWidth)/2, defensiveTrainerZone.y - 30);

        // Colores mas oscuros para mejor contraste
        drawButton(g2, defensiveTrainerZone, "defensiveTrainer", new Color(60, 60, 120));
        drawButton(g2, attackingTrainerZone, "attackingTrainer", new Color(120, 60, 60));
        drawButton(g2, chaningTrainerZone, "chaningTrainer", new Color(120, 120, 60));
        drawButton(g2, expertTrainerZone, "expertTrainer", new Color(60, 120, 60));
        drawButton(g2, backZoneMVM, "Volver", new Color(100, 100, 40));
    }

    /**
     * Dibuja la interfaz durante el juego
     * Actualmente solo muestra un mensaje indicando que el juego esta en progreso
     * Aqui se dibujarian los elementos del juego en si
     *
     * @param g2 El objeto Graphics2D para dibujar
     */
    private void drawGame(Graphics2D g2) {
        // Aqui iria el dibujo del juego en progreso
        g2.setColor(Color.WHITE);
        g2.setFont(titleFont);
        String message = "Juego en progreso";
        int msgWidth = g2.getFontMetrics().stringWidth(message);
        g2.drawString(message, (screenWidth - msgWidth)/2, screenHeight/2);
    }

    private void loadPokemonFont() {
        try {
            pokemonFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getClassLoader().getResourceAsStream("graficos/PokemonGB.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la fuente PokemonGB: " + e.getMessage());
            pokemonFont = new Font("Monospaced", Font.BOLD, 24); // Fallback
        }
    }
}