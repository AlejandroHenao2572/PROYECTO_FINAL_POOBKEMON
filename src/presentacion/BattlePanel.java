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
        private Point enemyPokemonPosition = new Point(480, 100);
        private Point playerPokemonPosition = new Point(100, 305);
        private Point enemyInfoPosition = new Point(50, 180);
        private Point playerInfoPosition = new Point(450, 380);
        private JPanel playerPokeballPanel;
        private JPanel enemyPokeballPanel;
        private ImageIcon fullBallIcon;
        private ImageIcon emptyBallIcon;

        public BattlePanel(Trainer player1, Trainer player2) {
            this.player = player1;
            this.enemy = player2;
            setLayout(null);
            setBackground(new Color(120, 200, 80));

            setupPlayerNames();
            loadBackgroundImage();
            setupPokemonDisplays();
            setupInfoPanels();
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
            loadPokemonImage(enemy.getPokemonActivo().getNombre(), enemyPokemonImage, false);
            enemyPokemonImage.setBounds(enemyPokemonPosition.x, enemyPokemonPosition.y, 200, 200);
            add(enemyPokemonImage);

            playerPokemonImage = new JLabel();
            loadPokemonImage(player.getPokemonActivo().getNombre(), playerPokemonImage, true);
            playerPokemonImage.setBounds(playerPokemonPosition.x, playerPokemonPosition.y, 200, 200);
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
                                pokemonName.toLowerCase() + ".png";
                URL imageUrl = getClass().getClassLoader().getResource(imagePath);

                if (imageUrl != null) {
                    ImageIcon originalIcon = new ImageIcon(imageUrl);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setIcon(createPlaceholderIcon());
                    imageLabel.setText(pokemonName);
                }
            } catch (Exception e) {
                imageLabel.setIcon(createPlaceholderIcon());
                imageLabel.setText(pokemonName);
            }
        }

        private ImageIcon createPlaceholderIcon() {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("graficos/pokemones/placeholder.png"));
            if (icon != null) {
                return new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
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
            // Panel para nombres de jugadores
            JPanel namesPanel = new JPanel(new GridLayout(1, 2));
            namesPanel.setOpaque(false);
            namesPanel.setBounds(0, 0, getWidth(), 30);
            
            // Nombre del jugador 1 (rojo)
            playerNameLabel = new JLabel(player.getNombre(), SwingConstants.CENTER);
            playerNameLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
            playerNameLabel.setForeground(Color.WHITE);
            playerNameLabel.setBackground(new Color(200, 0, 0, 150));
            playerNameLabel.setOpaque(true);
            
            // Nombre del jugador 2 (azul)
            enemyNameLabel = new JLabel(enemy.getNombre(), SwingConstants.CENTER);
            enemyNameLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
            enemyNameLabel.setForeground(Color.WHITE);
            enemyNameLabel.setBackground(new Color(0, 0, 200, 150));
            enemyNameLabel.setOpaque(true);
            
            namesPanel.add(playerNameLabel);
            namesPanel.add(enemyNameLabel);
            add(namesPanel);
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

        private void updatePokeballPanel(JPanel panel,ArrayList<Pokemon> equipo) {
            panel.removeAll();
                for (Pokemon p : equipo) {
                    boolean vivo = p.getPsActual() > 0;
                    JLabel icon = new JLabel(vivo ? fullBallIcon : emptyBallIcon);
                    panel.add(icon);
                }
                panel.revalidate();
                panel.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }