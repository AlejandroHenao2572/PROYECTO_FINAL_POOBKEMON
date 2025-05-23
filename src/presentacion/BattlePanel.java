package presentacion;

import dominio.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Clase que representa el panel grafico principal de la batalla
 * Muestra la informacion de los entrenadores y sus Pokemon
 * Incluye imagenes de los Pokemon, barras de vida, nombres, niveles y pokeballs
 * Permite pausar y reanudar la batalla
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class BattlePanel extends JPanel {
    // Entrenador jugador
    private Trainer player;
    // Entrenador enemigo
    private Trainer enemy;
    // Imagen del Pokemon del jugador
    private JLabel playerPokemonImage;
    // Imagen del Pokemon enemigo
    private JLabel enemyPokemonImage;
    // Barra de vida del jugador
    private JProgressBar playerHpBar;
    // Barra de vida del enemigo
    private JProgressBar enemyHpBar;
    // Nombre del Pokemon del jugador
    private JLabel playerPokemonName;
    // Nombre del Pokemon enemigo
    private JLabel enemyPokemonName;
    // Nivel del Pokemon del jugador
    private JLabel playerPokemonLevel;
    // Nivel del Pokemon enemigo
    private JLabel enemyPokemonLevel;
    // Nombre del entrenador jugador
    private JLabel playerNameLabel;
    // Nombre del entrenador enemigo
    private JLabel enemyNameLabel;
    // Imagen de fondo de la batalla
    private transient Image backgroundImage;
    // Tamaño de las imagenes de los Pokemon
    private Dimension pokemonSize = new Dimension(200, 200);
    // Posicion de la imagen del Pokemon enemigo
    private Point enemyPokemonPosition = new Point(490, 80);
    // Posicion de la imagen del Pokemon jugador
    private Point playerPokemonPosition = new Point(80, 240);
    // Posicion del panel de info del enemigo
    private Point enemyInfoPosition = new Point(140, 120);
    // Posicion del panel de info del jugador
    private Point playerInfoPosition = new Point(350, 350);
    // Tamaño de los paneles de info
    private Dimension infoPanelSize = new Dimension(270, 90);
    // Panel de pokeballs del jugador
    private JPanel playerPokeballPanel;
    // Panel de pokeballs del enemigo
    private JPanel enemyPokeballPanel;
    // Icono de pokeball llena
    private transient ImageIcon fullBallIcon;
    // Icono de pokeball vacia
    private transient ImageIcon emptyBallIcon;
    // Boton de pausa
    private JButton pauseButton;
    // Estado de pausa
    private boolean isPaused = false;

    /**
     * Constructor del panel de batalla
     * Inicializa los componentes graficos y la informacion de los entrenadores
     * @param player1 Primer entrenador
     * @param player2 Segundo entrenador
     */
    public BattlePanel(Trainer player1, Trainer player2) {
        this.player = player1;
        this.enemy = player2;
        setLayout(null);
        setBackground(new Color(120, 200, 80));

        setupPlayerNames();
        loadBackgroundImage();
        setupPokemonDisplays();
        setupInfoPanels();
        setupPauseButton();
        fullBallIcon = new ImageIcon(getClass().getClassLoader().getResource("graficos/items/pokeball_full.png"));
        emptyBallIcon = new ImageIcon(getClass().getClassLoader().getResource("graficos/items/pokeball_empty.png"));
    }

    /**
     * Carga la imagen de fondo de la batalla
     * Si no se encuentra la imagen usa un color de fondo por defecto
     */
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("graficos/fondo2.png"));
            backgroundImage = backgroundImage.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        } catch (IOException | NullPointerException e) {
            backgroundImage = null;
            System.out.println("No se cargo la imagen de fondo");
            setBackground(new Color(120, 200, 80));
        }
    }

    /**
     * Configura y agrega las imagenes de los Pokemon al panel
     */
    private void setupPokemonDisplays() {
        enemyPokemonImage = new JLabel();
        enemyPokemonImage.setOpaque(false);
        enemyPokemonImage.setBounds(
            enemyPokemonPosition.x, 
            enemyPokemonPosition.y, 
            pokemonSize.width, 
            pokemonSize.height
        );
        loadPokemonImage(enemy.getPokemonActivo().getNombre(), enemyPokemonImage, false);
        add(enemyPokemonImage);

        playerPokemonImage = new JLabel();
        playerPokemonImage.setOpaque(false);
        playerPokemonImage.setBounds(
            playerPokemonPosition.x, 
            playerPokemonPosition.y, 
            pokemonSize.width, 
            pokemonSize.height
        );
        loadPokemonImage(player.getPokemonActivo().getNombre(), playerPokemonImage, true);
        add(playerPokemonImage);
    }

    /**
     * Configura y agrega los paneles de informacion de los Pokemon
     */
    private void setupInfoPanels() {
        JPanel enemyInfo = createInfoPanel(false);
        enemyInfo.setBounds(enemyInfoPosition.x, enemyInfoPosition.y, 
                        infoPanelSize.width, infoPanelSize.height);
        add(enemyInfo);

        JPanel playerInfo = createInfoPanel(true);
        playerInfo.setBounds(playerInfoPosition.x, playerInfoPosition.y, 
                        infoPanelSize.width, infoPanelSize.height);
        add(playerInfo);
    }

    /**
     * Crea un panel de informacion para un Pokemon
     * @param isPlayer true si es el panel del jugador, false si es del enemigo
     * @return JPanel con la informacion del Pokemon
     */
    private JPanel createInfoPanel(boolean isPlayer) {
        Pokemon pokemon = isPlayer ? player.getPokemonActivo() : enemy.getPokemonActivo();

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g.setColor(Color.WHITE);
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        panel.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 5, 15));

        JLabel nameLabel = new JLabel(pokemon.getNombre());
        int fontSize = pokemon.getNombre().length() > 12 ? 14 : 16;
        nameLabel.setFont(new Font("Pokemon GB", Font.BOLD, fontSize));
        nameLabel.setForeground(Color.WHITE);

        JLabel levelLabel = new JLabel("Lv" + pokemon.getNivel());
        levelLabel.setFont(new Font("Pokemon GB", Font.BOLD, 16));
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(levelLabel, BorderLayout.EAST);

        JProgressBar hpBar = new JProgressBar(0, pokemon.getPs());
        hpBar.setValue(pokemon.getPsActual());
        hpBar.setForeground(Color.GREEN);
        hpBar.setStringPainted(true);
        hpBar.setString("HP: " + pokemon.getPsActual() + "/" + pokemon.getPs());
        hpBar.setFont(new Font("Pokemon GB", Font.PLAIN, 10));
        hpBar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        hpBar.setOpaque(false);

        updateHpBarColor(hpBar);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(hpBar, BorderLayout.CENTER);

        if (isPlayer) {
            playerPokemonName = nameLabel;
            playerPokemonLevel = levelLabel;
            playerHpBar = hpBar;
        } else {
            enemyPokemonName = nameLabel;
            enemyPokemonLevel = levelLabel;
            enemyHpBar = hpBar;
        }

        JPanel ballPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        ballPanel.setOpaque(false);

        ArrayList<Pokemon> equipo = isPlayer ? player.getEquipo() : enemy.getEquipo();
        for (Pokemon p : equipo) {
            boolean vivo = p.getPsActual() > 0;
            JLabel ballLabel = new JLabel(vivo ? fullBallIcon : emptyBallIcon);
            ballPanel.add(ballLabel);
        }

        panel.add(ballPanel, BorderLayout.SOUTH);

        if (isPlayer) {
            playerPokeballPanel = ballPanel;
        } else {
            enemyPokeballPanel = ballPanel;
        }

        return panel;
    }

    /**
     * Carga la imagen del Pokemon y la asigna al JLabel correspondiente
     * Si no encuentra la imagen usa un placeholder
     * @param pokemonName Nombre del Pokemon
     * @param imageLabel JLabel donde se muestra la imagen
     * @param isPlayer true si es el Pokemon del jugador, false si es del enemigo
     */
    private void loadPokemonImage(String pokemonName, JLabel imageLabel, boolean isPlayer) {
        try {
            String imagePath = "graficos/pokemones/" +
                            (isPlayer ? "back_" : "front_") +
                            pokemonName.toLowerCase() + ".gif";
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);

            if (imageUrl != null) {
                // Cargar el GIF original y escalarlo
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                    pokemonSize.width, 
                    pokemonSize.height, 
                    Image.SCALE_DEFAULT);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                JLabel gifLabel = new JLabel(scaledIcon) {
                    @Override
                    public void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
                                        RenderingHints.VALUE_RENDER_QUALITY);
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                        super.paintComponent(g);
                    }
                };
                gifLabel.setOpaque(false);
                gifLabel.setBounds(
                    isPlayer ? playerPokemonPosition.x : enemyPokemonPosition.x,
                    isPlayer ? playerPokemonPosition.y : enemyPokemonPosition.y,
                    pokemonSize.width,
                    pokemonSize.height
                );
                remove(imageLabel);
                if (isPlayer) {
                    playerPokemonImage = gifLabel;
                    add(gifLabel);
                } else {
                    enemyPokemonImage = gifLabel;
                    add(gifLabel);
                }
            } else {
                imageLabel.setIcon(createPlaceholderIcon());
                imageLabel.setText(pokemonName);
                imageLabel.setOpaque(false);
                imageLabel.setBounds(
                    isPlayer ? playerPokemonPosition.x : enemyPokemonPosition.x,
                    isPlayer ? playerPokemonPosition.y : enemyPokemonPosition.y,
                    pokemonSize.width,
                    pokemonSize.height
                );
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen de Pokemon: " + e.getMessage());
            imageLabel.setIcon(createPlaceholderIcon());
            imageLabel.setText(pokemonName);
            imageLabel.setOpaque(false);
            imageLabel.setBounds(
                isPlayer ? playerPokemonPosition.x : enemyPokemonPosition.x,
                isPlayer ? playerPokemonPosition.y : enemyPokemonPosition.y,
                pokemonSize.width,
                pokemonSize.height
            );
        }
    }

    /**
     * Crea un icono de imagen por defecto si no se encuentra la imagen del Pokemon
     * @return ImageIcon placeholder
     */
    private ImageIcon createPlaceholderIcon() {
        try {
            URL placeholderUrl = getClass().getClassLoader().getResource("graficos/pokemones/placeholder.gif");
            if (placeholderUrl != null) {
                ImageIcon icon = new ImageIcon(placeholderUrl);
                Image scaledImage = icon.getImage().getScaledInstance(
                    pokemonSize.width, 
                    pokemonSize.height, 
                    Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Error cargando placeholder: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza la informacion y las imagenes de los Pokemon en el panel
     */
    public void updatePokemonStats() {
        // Actualizar stats del jugador
        Pokemon playerPokemon = player.getPokemonActivo();
        playerPokemonName.setText(playerPokemon.getNombre());
        playerPokemonLevel.setText("Lv" + playerPokemon.getNivel());
        playerHpBar.setMaximum(playerPokemon.getPs());
        playerHpBar.setValue(playerPokemon.getPsActual());
        playerHpBar.setString("HP: " + playerPokemon.getPsActual() + "/" + playerPokemon.getPs());
        updateHpBarColor(playerHpBar);
        loadPokemonImage(playerPokemon.getNombre(), playerPokemonImage, true);

        // Actualizar stats del enemigo
        Pokemon enemyPokemon = enemy.getPokemonActivo();
        enemyPokemonName.setText(enemyPokemon.getNombre());
        enemyPokemonLevel.setText("Lv" + enemyPokemon.getNivel());
        enemyHpBar.setMaximum(enemyPokemon.getPs());
        enemyHpBar.setValue(enemyPokemon.getPsActual());
        enemyHpBar.setString("HP: " + enemyPokemon.getPsActual() + "/" + enemyPokemon.getPs());
        updateHpBarColor(enemyHpBar);
        loadPokemonImage(enemyPokemon.getNombre(), enemyPokemonImage, false);
        updatePokeballPanel(playerPokeballPanel, player.getEquipo());
        updatePokeballPanel(enemyPokeballPanel, enemy.getEquipo());
        repaint();
        revalidate();
    }

    /**
     * Configura y agrega los nombres de los entrenadores al panel
     */
    private void setupPlayerNames() {
        JPanel namesPanel = new JPanel(new GridLayout(1, 2));
        namesPanel.setOpaque(false);
        namesPanel.setBounds(0, 0, getWidth(), 30);

        playerNameLabel = new JLabel(player.getNombre(), SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setBackground(new Color(200, 0, 0, 150));
        playerNameLabel.setOpaque(true);

        enemyNameLabel = new JLabel(enemy.getNombre(), SwingConstants.CENTER);
        enemyNameLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        enemyNameLabel.setForeground(Color.WHITE);
        enemyNameLabel.setBackground(new Color(0, 0, 200, 150));
        enemyNameLabel.setOpaque(true);

        namesPanel.add(playerNameLabel);
        namesPanel.add(enemyNameLabel);
        add(namesPanel);
    }

    /**
     * Configura el boton de pausa y su comportamiento
     */
    private void setupPauseButton() {
        pauseButton = new JButton("Pausa");
        pauseButton.setFont(new Font("Pokemon GB", Font.BOLD, 12));
        pauseButton.setBounds(310, 10, 150, 30);
        pauseButton.setFocusPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        pauseButton.addActionListener(e -> {
            isPaused = !isPaused;
            if (isPaused) {
                pauseButton.setText("Reanudar");
                pauseButton.setForeground(Color.YELLOW);
            } else {
                pauseButton.setText("Pausa");
                pauseButton.setForeground(Color.WHITE);
            }
            firePropertyChange("pauseState", !isPaused, isPaused);
        });

        add(pauseButton);
    }

    /**
     * Actualiza el color de la barra de vida segun el porcentaje de HP
     * @param hpBar Barra de vida a actualizar
     */
    private void updateHpBarColor(JProgressBar hpBar) {
        double percentage = (double) hpBar.getValue() / hpBar.getMaximum();
        if (percentage < 0.2) {
            hpBar.setForeground(new Color(253, 35, 35));
        } else if (percentage < 0.5) {
            hpBar.setForeground(new Color(253, 159, 35));
        } else {
            hpBar.setForeground(new Color(14, 174, 147));
        }
    }

    /**
     * Actualiza el panel de pokeballs segun el estado de los Pokemon del equipo
     * @param panel Panel de pokeballs
     * @param equipo Lista de Pokemon del entrenador
     */
    private void updatePokeballPanel(JPanel panel, ArrayList<Pokemon> equipo) {
        panel.removeAll();
        for (Pokemon p : equipo) {
            boolean vivo = p.getPsActual() > 0;
            JLabel icon = new JLabel(vivo ? fullBallIcon : emptyBallIcon);
            panel.add(icon);
        }
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Indica si la batalla esta en pausa
     * @return true si esta pausada, false en caso contrario
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Dibuja el fondo de la batalla si existe una imagen cargada
     * @param g Objeto Graphics para dibujar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                               RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Actualiza los jugadores en el panel de batalla
     * @param player1 Primer jugador
     * @param player2 Segundo jugador
     */
    public void updatePlayers(Trainer player1, Trainer player2) {
        this.player = player1;
        this.enemy = player2;

        // Actualizar nombres de los jugadores
        playerNameLabel.setText(player1.getNombre());
        enemyNameLabel.setText(player2.getNombre());

        // Actualizar imagenes y stats de los Pokemon activos
        loadPokemonImage(player1.getPokemonActivo().getNombre(), playerPokemonImage, true);
        loadPokemonImage(player2.getPokemonActivo().getNombre(), enemyPokemonImage, false);

        // Actualizar paneles de informacion
        updatePokemonStats();

        // Actualizar paneles de pokeballs
        updatePokeballPanel(playerPokeballPanel, player1.getEquipo());
        updatePokeballPanel(enemyPokeballPanel, player2.getEquipo());

        // Forzar redibujado del panel
        revalidate();
        repaint();
    }
}