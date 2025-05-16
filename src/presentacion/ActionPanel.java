package presentacion;

import dominio.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Clase que representa el panel de acciones del juego
 * Permite al jugador elegir entre luchar cambiar de Pokemon usar un item o huir
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class ActionPanel extends JPanel {
    private MainWindow mainWindow;
    private Trainer currentPlayer;
    private JLabel lastActionLabel;
    private JPanel buttonPanel;

    /**
     * Constructor de la clase ActionPanel
     * Inicializa el panel con la ventana principal y el entrenador actual
     *
     * @param mainWindow Ventana principal de la aplicacion
     * @param player Entrenador actual
     */
    public ActionPanel(MainWindow mainWindow, Trainer player) {
        this.mainWindow = mainWindow;
        this.currentPlayer = player;
        setLayout(new BorderLayout());
        setBackground(new Color(64, 64, 64));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Etiqueta para mostrar la ultima accion
        lastActionLabel = new JLabel(" ", SwingConstants.CENTER);
        lastActionLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
        lastActionLabel.setForeground(Color.WHITE);
        lastActionLabel.setBackground(new Color(64, 64, 64));
        lastActionLabel.setOpaque(true);
        lastActionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        buttonPanel = new JPanel(new GridLayout(2, 2));
        buttonPanel.setOpaque(false);

        String[] buttonLabels = {"LUCHAR", "POKEMON", "MOCHILA", "HUIR"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Pokemon GB", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(14, 174, 147));
            button.setFocusPainted(false);

            switch(label) {
                case "LUCHAR":
                    button.addActionListener(e -> showAttackOptions());
                    break;
                case "POKEMON":
                    button.addActionListener(e -> showSwitchOptions());
                    break;
                case "MOCHILA":
                    button.addActionListener(e -> showItemOptions());
                    break;
                case "HUIR":
                    button.addActionListener(e -> mainWindow.runAway());
                    break;
            }

            buttonPanel.add(button);
        }

        add(lastActionLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Muestra las opciones de ataque del Pokemon activo
     * Remueve los botones principales y muestra los movimientos del Pokemon
     */
    private void showAttackOptions() {
        buttonPanel.removeAll();
        Pokemon activePokemon = currentPlayer.getPokemonActivo();

        for (int i = 0; i < activePokemon.getMovimientos().size(); i++) {
            Movimiento move = activePokemon.getMovimientos().get(i);
            JButton moveButton = new JButton(move.getNombre() + " PP:" + move.getPP());
            moveButton.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
            moveButton.setForeground(Color.WHITE);
            moveButton.setBackground(new Color(14, 174, 147));
            moveButton.setFocusPainted(false);

            int moveIndex = i;
            moveButton.addActionListener(e -> {
                mainWindow.attackSelected(moveIndex);
                resetButtons();
            });

            if (!move.esUtilizable()) {
                moveButton.setEnabled(false);
            }

            buttonPanel.add(moveButton);
        }

        // Boton de regreso
        JButton backButton = new JButton("VOLVER");
        backButton.setFont(new Font("Pokemon GB", Font.BOLD, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(14, 174, 147));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> resetButtons());

        buttonPanel.add(backButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    /**
     * Muestra las opciones para cambiar el Pokemon activo
     * Remueve los botones principales y muestra los Pokemon del equipo
     */
    public void showSwitchOptions() {
        // Limpiar el panel primero
        buttonPanel.removeAll();
        
        // Obtener el equipo del jugador actual
        ArrayList<Pokemon> equipo = currentPlayer.getEquipo();
        
        // Crear botones para cada Pokémon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon pokemon = equipo.get(i);
            JButton pokemonButton = new JButton(pokemon.getNombre() + 
                                             (pokemon.estaDebilitado() ? " (DEBILITADO)" : ""));
            pokemonButton.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
            pokemonButton.setForeground(Color.WHITE);
            pokemonButton.setBackground(new Color(14, 174, 147));
            pokemonButton.setFocusPainted(false);
    
            // Solo permitir cambiar a Pokémon no debilitados y que no sean el actual
            if (pokemon.estaDebilitado() || pokemon == currentPlayer.getPokemonActivo()) {
                pokemonButton.setEnabled(false);
            } else {
                int pokemonIndex = i;
                pokemonButton.addActionListener(e -> {
                    mainWindow.switchPokemonSelected(pokemonIndex);
                    resetButtons(); // Restablecer la interfaz después de la selección
                });
            }
    
            buttonPanel.add(pokemonButton);
        }
    
        // Botón de volver (solo si no es un cambio obligatorio)
        if (!mainWindow.getBattle().isCambioForzado()) {
            JButton backButton = new JButton("VOLVER");
            backButton.setFont(new Font("Pokemon GB", Font.BOLD, 12));
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(new Color(14, 174, 147));
            backButton.setFocusPainted(false);
            backButton.addActionListener(e -> resetButtons());
            buttonPanel.add(backButton);
        }
    
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

/**
 * Muestra las opciones de items disponibles para usar
 * Remueve los botones principales y muestra los items en la mochila
 */
private void showItemOptions() {
    buttonPanel.removeAll();

    if (currentPlayer.getItems().isEmpty()) {
        JLabel noItemsLabel = new JLabel("No tienes items");
        noItemsLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
        noItemsLabel.setForeground(Color.WHITE);
        buttonPanel.add(noItemsLabel);
    } else {
        for (int i = 0; i < currentPlayer.getItems().size(); i++) {
            Item item = currentPlayer.getItems().get(i);
            JButton itemButton = new JButton(item.getNombre());
            itemButton.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
            itemButton.setForeground(Color.WHITE);
            itemButton.setBackground(new Color(14, 174, 147));
            itemButton.setFocusPainted(false);

            int itemIndex = i;
            itemButton.addActionListener(e -> {
                if (item instanceof Revive) {
                    showReviveOptions(itemIndex); // Mostrar opciones para revivir
                } else {
                    mainWindow.useItemSelected(itemIndex);
                    resetButtons();
                }
            });

            buttonPanel.add(itemButton);
        }
    }

    // Boton de regreso
    JButton backButton = new JButton("VOLVER");
    backButton.setFont(new Font("Pokemon GB", Font.BOLD, 12));
    backButton.setForeground(Color.WHITE);
    backButton.setBackground(new Color(14, 174, 147));
    backButton.setFocusPainted(false);
    backButton.addActionListener(e -> resetButtons());

    buttonPanel.add(backButton);
    buttonPanel.revalidate();
    buttonPanel.repaint();
}

/**
 * Muestra las opciones para seleccionar qué Pokémon revivir
 * @param itemIndex Índice del item de revivir en la mochila
 */
private void showReviveOptions(int itemIndex) {
    buttonPanel.removeAll();
    
    // Obtener Pokémon debilitados
    ArrayList<Pokemon> debilitados = currentPlayer.getPokemonsDebilitados();
    
    if (debilitados.isEmpty()) {
        JLabel noDebilitadosLabel = new JLabel("No hay Pokémon debilitados");
        noDebilitadosLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
        noDebilitadosLabel.setForeground(Color.WHITE);
        buttonPanel.add(noDebilitadosLabel);
    } else {
        // Mostrar opciones para cada Pokémon debilitado
        for (int i = 0; i < currentPlayer.getEquipo().size(); i++) {
            Pokemon pokemon = currentPlayer.getEquipo().get(i);
            if (pokemon.estaDebilitado()) {
                JButton pokemonButton = new JButton(pokemon.getNombre());
                pokemonButton.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
                pokemonButton.setForeground(Color.WHITE);
                pokemonButton.setBackground(new Color(14, 174, 147));
                pokemonButton.setFocusPainted(false);

                int pokemonIndex = i;
                pokemonButton.addActionListener(e -> {
                    // Confirmar uso del item
                    int confirm = JOptionPane.showConfirmDialog(
                        mainWindow,
                        "¿Usar " + currentPlayer.getItems().get(itemIndex).getNombre() + 
                        " en " + pokemon.getNombre() + "?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        mainWindow.useReviveItem(itemIndex, pokemonIndex);
                    }
                    resetButtons();
                });

                buttonPanel.add(pokemonButton);
            }
        }
    }

    // Botón de volver
    JButton backButton = new JButton("VOLVER");
    backButton.setFont(new Font("Pokemon GB", Font.BOLD, 12));
    backButton.setForeground(Color.WHITE);
    backButton.setBackground(new Color(14, 174, 147));
    backButton.setFocusPainted(false);
    backButton.addActionListener(e -> showItemOptions());

    buttonPanel.add(backButton);
    buttonPanel.revalidate();
    buttonPanel.repaint();
}

    /**
     * Restablece los botones principales de accion (LUCHAR, POKEMON, MOCHILA, HUIR)
     * Limpia el panel de botones y añade los botones principales
     */
    private void resetButtons() {
        buttonPanel.removeAll();

        String[] buttonLabels = {"LUCHAR", "POKEMON", "MOCHILA", "HUIR"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Pokemon GB", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(14, 174, 147));
            button.setFocusPainted(false);

            switch(label) {
                case "LUCHAR":
                    button.addActionListener(e -> showAttackOptions());
                    break;
                case "POKEMON":
                    button.addActionListener(e -> showSwitchOptions());
                    break;
                case "MOCHILA":
                    button.addActionListener(e -> showItemOptions());
                    break;
                case "HUIR":
                    button.addActionListener(e -> mainWindow.runAway());
                    break;
            }

            buttonPanel.add(button);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    /**
     * Actualiza el texto que se muestra en la etiqueta de la ultima accion
     *
     * @param text Texto a mostrar
     */
    public void addBattleText(String text) {
        // Reemplaza el texto anterior en lugar de anadirlo
        lastActionLabel.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
    }

    /**
     * Establece el entrenador actual
     *
     * @param player El nuevo entrenador actual
     */
    public void setCurrentPlayer(Trainer player) {
        this.currentPlayer = player;
    }
}