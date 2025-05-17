package presentacion;

import dominio.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BattlePanel extends JPanel {
    private Trainer player;
    private Trainer enemy;
    private JLabel playerPokemonImage;
    private JLabel enemyPokemonImage;
    private JProgressBar playerHpBar;
    private JProgressBar enemyHpBar;
    private JLabel playerPokemonName;
    private JLabel enemyPokemonName;
    private JLabel playerPokemonLevel;
    private JLabel enemyPokemonLevel;
    private JLabel playerNameLabel;
    private JLabel enemyNameLabel;
    private Image backgroundImage;
    private Dimension pokemonSize = new Dimension(200, 200); // Tamaño aumentado
    private Point enemyPokemonPosition = new Point(480, 80);  // Posición ajustada
    private Point playerPokemonPosition = new Point(80, 240); // Posición ajustada
    private Point enemyInfoPosition = new Point(30, 180);  // Ajustado para el nuevo tamaño
    private Point playerInfoPosition = new Point(400, 380); // Ajustado para el nuevo tamaño
    private JPanel playerPokeballPanel;
    private JPanel enemyPokeballPanel;
    private ImageIcon fullBallIcon;
    private ImageIcon emptyBallIcon;
    private JButton pauseButton;
    private boolean isPaused = false;

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

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("graficos/fondo2.png"));
            backgroundImage = backgroundImage.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        } catch (IOException | NullPointerException e) {
            backgroundImage = null;
            System.out.println("No se cargó la imagen de fondo");
            setBackground(new Color(120, 200, 80));
        }
    }

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

    private void setupInfoPanels() {
        JPanel enemyInfo = createInfoPanel(false);
        enemyInfo.setBounds(enemyInfoPosition.x, enemyInfoPosition.y, 250, 80);
        add(enemyInfo);

        JPanel playerInfo = createInfoPanel(true);
        playerInfo.setBounds(playerInfoPosition.x, playerInfoPosition.y, 250, 80);
        add(playerInfo);
    }

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
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));

        JLabel nameLabel = new JLabel(pokemon.getNombre());
        nameLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);

        JLabel levelLabel = new JLabel("Lv" + pokemon.getNivel());
        levelLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
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

    private void loadPokemonImage(String pokemonName, JLabel imageLabel, boolean isPlayer) {
        try {
            String imagePath = "graficos/pokemones/" +
                            (isPlayer ? "back_" : "front_") +
                            pokemonName.toLowerCase() + ".gif";
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);

            if (imageUrl != null) {
                // Cargar el GIF original
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                
                // Escalar el GIF manteniendo la relación de aspecto
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                    pokemonSize.width, 
                    pokemonSize.height, 
                    Image.SCALE_DEFAULT);
                
                // Crear un nuevo ImageIcon con la imagen escalada
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                // Configurar el JLabel para mostrar el GIF escalado
                JLabel gifLabel = new JLabel(scaledIcon) {
                    @Override
                    public void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g;
                        // Configurar renderizado de alta calidad
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
                
                // Reemplazar el JLabel existente
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
            System.err.println("Error cargando imagen de Pokémon: " + e.getMessage());
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

    private ImageIcon createPlaceholderIcon() {
        try {
            URL placeholderUrl = getClass().getClassLoader().getResource("graficos/pokemones/placeholder.gif");
            if (placeholderUrl != null) {
                ImageIcon icon = new ImageIcon(placeholderUrl);
                // Escalar el placeholder al nuevo tamaño
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

    public boolean isPaused() {
        return isPaused;
    }

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
}