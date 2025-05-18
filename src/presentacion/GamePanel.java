package presentacion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Clase principal del panel de juego que implementa la logica del juego y la interfaz de usuario
 * Gestiona los estados del juego menu principal seleccion de modo PvP en juego
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

    // Control del juego
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);
    TileManager tileM = new TileManager(this);

    // Estados del juego
    enum GameState {
        MAIN_MENU,
        PVP_MODE_SELECTION,
        IN_GAME
    }
    private GameState currentState = GameState.MAIN_MENU;

    // Areas de botones del menu principal
    Rectangle pvpZone;
    Rectangle pvmZone;
    Rectangle mvmZone;

    // Areas de botones de seleccion de modo PvP
    Rectangle normalModeZone;
    Rectangle survivalModeZone;

    // Fuentes
    Font buttonFont = new Font("Arial", Font.BOLD, 16);
    Font titleFont = new Font("Arial", Font.BOLD, 24);
    Font subtitleFont = new Font("Arial", Font.BOLD, 20);
    FontMetrics fontMetrics;

    // Imagenes
    private BufferedImage titleImage;
    private int titleWidth;
    private int titleHeight;
    private int titleYOffset = 30;

    // Configuracion de FPS
    int FPS = 60;

    // Referencia al JFrame contenedor
    private JFrame parentFrame;

    /**
     * Constructor de la clase GamePanel
     * Inicializa la configuracion de la pantalla el KeyHandler el jugador el TileManager
     * y carga los recursos del juego Tambien inicializa las metricas de la fuente y calcula
     * el tamaño de los botones del menu
     *
     * @param parentFrame El JFrame contenedor de este panel
     */
    public GamePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Cargar recursos
        loadResources();

        // Inicializar metricas de fuente
        fontMetrics = this.getFontMetrics(buttonFont);
        calculateButtonSizes();
    }

    /**
     * Carga los recursos graficos del juego como la imagen del titulo
     * Si ocurre un error durante la carga se imprime un mensaje de error
     */
    private void loadResources() {
        try {
            // Cargar imagen del titulo
            titleImage = ImageIO.read(getClass().getResourceAsStream("/graficos/pokemon_logo_new (6).png"));
            double scaleFactor = (screenWidth * 0.7) / titleImage.getWidth();
            titleWidth = (int)(titleImage.getWidth() * scaleFactor);
            titleHeight = (int)(titleImage.getHeight() * scaleFactor);
        } catch (IOException e) {
            System.err.println("Error al cargar recursos graficos: " + e.getMessage());
            titleImage = null;
        }
    }

    /**
     * Inicia el hilo del juego
     * Crea una nueva instancia de Thread con este GamePanel como Runnable y la inicia
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
                update();
                repaint();
                delta--;
            }
        }
    }

    /**
     * Actualiza el estado del juego
     * Actualmente actualiza la logica del jugador y maneja la seleccion de elementos del menu
     * cuando se presiona la tecla Enter
     */
    public void update() {
        player.update();

        if (keyH.enterPressed) {
            handleMenuSelection();
            keyH.enterPressed = false;
        }
    }

    /**
     * Maneja la seleccion de elementos del menu principal y de seleccion de modo PvP
     * basandose en la posicion del jugador y las areas de los botones
     */
    private void handleMenuSelection() {
        Rectangle playerBounds = player.getBounds();

        switch(currentState) {
            case MAIN_MENU:
                if (playerBounds.intersects(pvpZone)) {
                    currentState = GameState.PVP_MODE_SELECTION;
                } else if (playerBounds.intersects(pvmZone)) {
                    startGame("Player vs Machine");
                } else if (playerBounds.intersects(mvmZone)) {
                    startGame("Machine vs Machine");
                }
                break;

            case PVP_MODE_SELECTION:
                if (playerBounds.intersects(normalModeZone)) {
                    startPvPNormalMode();
                } else if (playerBounds.intersects(survivalModeZone)) {
                    startPvPSurvivalMode();
                }
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

            // Crear nueva instancia de PvPNormalSetUp
            PvPSupervivenciaSetUp pvpSetup = new PvPSupervivenciaSetUp();
            pvpSetup.setVisible(true);
        });
    }

    /**
     * Inicia un modo de juego especifico
     * Actualmente solo imprime un mensaje en la consola y cambia el estado del juego a IN_GAME
     * Aqui se implementaria la logica para iniciar diferentes modos de juego
     *
     * @param gameMode El nombre del modo de juego a iniciar
     */
    private void startGame(String gameMode) {
        System.out.println("Iniciando juego: " + gameMode);
        currentState = GameState.IN_GAME;
        // Aqui iria la logica para iniciar otros modos de juego
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
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Borde del boton
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Texto centrado
        g2.setFont(buttonFont);
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + (rect.height / 2) + g2.getFontMetrics().getAscent() / 2 - 5;
        g2.drawString(text, textX, textY);

        // Efecto de seleccion
        if (player.getBounds().intersects(rect)) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawRoundRect(rect.x-3, rect.y-3, rect.width+6, rect.height+6, 18, 18);
        }
    }

    /**
     * Calcula el tamaño y la posicion de los botones del menu principal y de seleccion de modo PvP
     * basandose en las metricas de la fuente
     */
    private void calculateButtonSizes() {
        if (fontMetrics == null) return;

        // Botones principales mas pequeños
        int mainButtonWidth = fontMetrics.stringWidth("Player vs Player") + 40;
        int mainButtonHeight = 45;
        int mainButtonSpacing = 25;

        int totalMainWidth = mainButtonWidth * 3 + mainButtonSpacing * 2;
        int startMainX = (screenWidth - totalMainWidth) / 2;
        int mainButtonY = titleYOffset + (titleImage != null ? titleHeight : 0) + 80;

        pvpZone = new Rectangle(startMainX, mainButtonY, mainButtonWidth, mainButtonHeight);
        pvmZone = new Rectangle(startMainX + mainButtonWidth + mainButtonSpacing, mainButtonY, mainButtonWidth, mainButtonHeight);
        mvmZone = new Rectangle(startMainX + 2*(mainButtonWidth + mainButtonSpacing), mainButtonY, mainButtonWidth, mainButtonHeight);

        // Botones de modo PvP un poco mas grandes
        int modeButtonWidth = Math.max(
            fontMetrics.stringWidth("Modo Normal") + 50,
            fontMetrics.stringWidth("Supervivencia") + 50
        );
        int modeButtonHeight = 55;
        int modeButtonSpacing = 40;
        int totalModeWidth = modeButtonWidth * 2 + modeButtonSpacing;
        int startModeX = (screenWidth - totalModeWidth) / 2;
        int modeButtonY = mainButtonY + 120;

        normalModeZone = new Rectangle(startModeX, modeButtonY - 70, modeButtonWidth, modeButtonHeight);
        survivalModeZone = new Rectangle(startModeX + modeButtonWidth + modeButtonSpacing, modeButtonY - 70, modeButtonWidth, modeButtonHeight);
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

            case IN_GAME:
                drawGame(g2);
                break;
        }

        // Dibujar jugador
        player.draw(g2);
        g2.dispose();
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
            int titleX = (screenWidth - titleWidth) / 2;
            g2.drawImage(titleImage, titleX, titleYOffset, titleWidth, titleHeight, null);
        } else {
            // Fallback si no hay imagen
            g2.setColor(Color.WHITE);
            g2.setFont(titleFont);
            String title = "POOBkemon";
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (screenWidth - titleWidth)/2, titleYOffset + 40);
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
}