package presentacion;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Ventana de configuracion para batallas Player vs Machine (PvM)
 * Permite al usuario seleccionar su equipo de Pokemon y los items que llevara a la batalla,
 * mientras que la maquina genera su equipo e items de forma aleatoria.
 * Permite elegir el tipo de IA de la maquina.
 * Muestra visualmente los equipos y los items seleccionados para ambos participantes.
 * Al confirmar la seleccion, inicia la batalla llamando a PokemonBattleGame.iniciarBatallaPvM.
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class PvMSetUp extends JFrame {

    // Panel para el jugador humano
    private JPanel panelJugador;
    // Panel para la maquina
    private JPanel panelMaquina;

    // Modelo y lista para mostrar el equipo seleccionado por el jugador
    private DefaultListModel<String> equipoJugadorModel = new DefaultListModel<>();
    private JList<String> equipoListJugador = new JList<>(equipoJugadorModel);

    // Mapas para imagenes de pokemones e items
    private HashMap<String, ImageIcon> imagenesPokemones = new HashMap<>();
    private JLabel imagenPokemonJugador = new JLabel();
    private HashMap<String, ImageIcon> imagenesItems = new HashMap<>();

    // Lista de nombres de pokemones disponibles
    private ArrayList<String> listaPokemones = new ArrayList<>(Arrays.asList(
        "charizard", "blastoise", "venusaur", "gengar", "dragonite", "snorlax", "raichu",
        "togetic", "tyranitar", "gardevoir", "metagross", "donphan", "machamp", "delibird",
        "scizor", "mewtwo", "torkoal", "milotic", "sceptile", "manectric", "glalie",
        "kabutops", "whiscash", "masquerain", "banette", "altaria", "claydol", "hariyama",
        "swellow", "aggron", "weezing", "nidoking", "zangoose", "clefable", "absol", "chimecho"
    ));

    // Listas para los nombres de los equipos seleccionados
    private ArrayList<String> nombresEquipoJugador = new ArrayList<>();
    private ArrayList<String> nombresEquipoMaquina = new ArrayList<>();

    // Mapas para los items seleccionados por jugador y maquina
    private HashMap<String, Integer> itemsJugador = new HashMap<>();
    private HashMap<String, Integer> itemsMaquina = new HashMap<>();
    // Lista de items disponibles
    private final String[] itemsDisponibles = {"Potion", "SuperPotion", "HyperPotion", "Revive"};

    // Tipo de IA seleccionada para la maquina
    private String trainerType;

    /**
     * Constructor de la ventana de configuracion PvM
     * Inicializa la interfaz, paneles y equipos para jugador y maquina
     * 
     * @param tipoMaquina Tipo de IA para la maquina (por ejemplo: attackingTrainer, defensiveTrainer, etc)
     */
    public PvMSetUp(String tipoMaquina) {
        setTitle("Player vs Machine - " + tipoMaquina);
        setSize(1280, 720);
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(20, 20, 20));

        // Cargar fuente Pokemon GB para la interfaz
        try {
            Font pokemonFont = Font.createFont(Font.TRUETYPE_FONT,
                getClass().getResourceAsStream("/graficos/PokemonGB.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (Exception e) {
            System.err.println("Error al cargar la fuente Pokemon GB: " + e.getMessage());
        }

        cargarImagenes(); // Cargar imagenes de pokemones e items
        crearPanelJugador(); // Crear panel para el jugador humano
        crearPanelMaquina(tipoMaquina); // Crear panel para la maquina
        trainerType = tipoMaquina; // Guardar el tipo de IA seleccionado
        add(panelJugador);
        add(panelMaquina);
    }

    /**
     * Crea el panel de seleccion de equipo y items para el jugador humano
     * Permite seleccionar hasta 6 pokemones y hasta 7 items en total
     * Al confirmar, valida la seleccion y llama a PokemonBattleGame.iniciarBatallaPvM
     */
    private void crearPanelJugador() {
        panelJugador = new JPanel();
        panelJugador.setBackground(new Color(200, 200, 255));
        panelJugador.setLayout(new BorderLayout(0, 10));
        panelJugador.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));

        // Titulo del panel
        JLabel titulo = new JLabel("Jugador", SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(new Color(100, 100, 255));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 5, 5, 5)));
        titulo.setFont(new Font("Pokemon GB", Font.BOLD, 24));
        titulo.setPreferredSize(new Dimension(0, 40));
        panelJugador.add(titulo, BorderLayout.NORTH);

        // Panel para seleccionar pokemones y mostrar imagen
        JPanel seleccionPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        seleccionPanel.setBackground(new Color(200, 200, 255));
        seleccionPanel.setPreferredSize(new Dimension(0, 200));

        JComboBox<String> pokeBox = new JComboBox<>(listaPokemones.toArray(new String[0]));
        pokeBox.setBackground(Color.WHITE);
        pokeBox.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
        pokeBox.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(2, 2, 2, 2)));
        pokeBox.setPreferredSize(new Dimension(0, 30));

        imagenPokemonJugador.setHorizontalAlignment(SwingConstants.CENTER);
        imagenPokemonJugador.setIcon(imagenesPokemones.get(listaPokemones.get(0)));
        imagenPokemonJugador.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));

        pokeBox.addActionListener(e -> imagenPokemonJugador.setIcon(imagenesPokemones.get((String) pokeBox.getSelectedItem())));

        JButton agregar = new JButton("Agregar al equipo");
        agregar.setBackground(new Color(100, 100, 255));
        agregar.setForeground(Color.WHITE);
        agregar.setFocusPainted(false);
        agregar.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        agregar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(2, 5, 2, 5)));
        agregar.setPreferredSize(new Dimension(0, 30));
        agregar.addActionListener(e -> {
            String seleccionado = (String) pokeBox.getSelectedItem();
            if (equipoJugadorModel.getSize() < 6) equipoJugadorModel.addElement(seleccionado);
        });

        seleccionPanel.add(pokeBox);
        seleccionPanel.add(imagenPokemonJugador);
        seleccionPanel.add(agregar);

        JPanel centralPanel = new JPanel(new BorderLayout(0, 10));
        centralPanel.setBackground(new Color(200, 200, 255));
        centralPanel.add(seleccionPanel, BorderLayout.NORTH);

        // Panel para seleccionar items y su cantidad
        JPanel itemPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        itemPanel.setBackground(new Color(200, 200, 255));
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10)));

        HashMap<String, JSpinner> spinners = new HashMap<>();
        for (String item : itemsDisponibles) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 2));
            row.setBackground(new Color(200, 200, 255));
            JLabel iconLabel = new JLabel(imagenesItems.get(item));
            iconLabel.setPreferredSize(new Dimension(32, 32));
            JLabel itemLabel = new JLabel(item);
            itemLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
            itemLabel.setPreferredSize(new Dimension(200, 25));
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, item.equals("Revive") ? 1 : 2, 1));
            spinner.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
            spinner.setBorder(new LineBorder(Color.BLACK, 1));
            spinner.setPreferredSize(new Dimension(50, 25));
            row.add(iconLabel);
            row.add(itemLabel);
            row.add(spinner);
            itemPanel.add(row);
            spinners.put(item, spinner);
        }

        centralPanel.add(itemPanel, BorderLayout.CENTER);
        panelJugador.add(centralPanel, BorderLayout.CENTER);

        // Panel para mostrar el equipo seleccionado y confirmar
        JPanel equipoPanel = new JPanel(new BorderLayout(0, 5));
        equipoPanel.setBackground(new Color(200, 200, 255));
        equipoPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
        equipoPanel.setPreferredSize(new Dimension(0, 150));

        JLabel equipoLabel = new JLabel("Equipo seleccionado:");
        equipoLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        equipoPanel.add(equipoLabel, BorderLayout.NORTH);

        equipoListJugador.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
        equipoListJugador.setBackground(Color.WHITE);
        equipoListJugador.setBorder(new LineBorder(Color.BLACK, 1));
        equipoPanel.add(new JScrollPane(equipoListJugador), BorderLayout.CENTER);

        JButton confirmar = new JButton("Confirmar equipo");
        confirmar.setBackground(new Color(100, 100, 255));
        confirmar.setForeground(Color.WHITE);
        confirmar.setFocusPainted(false);
        confirmar.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        confirmar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 10, 5, 10)));
        confirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nombresEquipoJugador.clear();
                itemsJugador.clear();
                int totalItems = 0;

                // Validar cantidad de pokemones seleccionados
                if (equipoJugadorModel.size() < 1 || equipoJugadorModel.size() > 6) {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar entre 1 y 6 Pokemon.");
                    return;
                }

                // Validar cantidad total de items
                for (String item : itemsDisponibles) {
                    int cantidad = (int) spinners.get(item).getValue();
                    totalItems += cantidad;
                }
                if (totalItems > 7) {
                    JOptionPane.showMessageDialog(null, "No puedes seleccionar mas de 7 items.");
                    return;
                }

                // Guardar items seleccionados
                for (String item : itemsDisponibles) {
                    int cantidad = (int) spinners.get(item).getValue();
                    if (cantidad > 0) {
                        itemsJugador.put(item, cantidad);
                    }
                }

                // Guardar pokemones seleccionados
                for (int i = 0; i < equipoJugadorModel.size(); i++) {
                    nombresEquipoJugador.add(equipoJugadorModel.get(i));
                }

                JOptionPane.showMessageDialog(null, "Equipo confirmado. Comienza la batalla!");

                // Iniciar la batalla PvM
                PokemonBattleGame.iniciarBatallaPvM(
                     new ArrayList<>(nombresEquipoJugador),
                     new ArrayList<>(nombresEquipoMaquina),
                     new HashMap<>(itemsJugador),
                     new HashMap<>(itemsMaquina),
                     trainerType
                );
                dispose();
            }
        });
        equipoPanel.add(confirmar, BorderLayout.SOUTH);

        panelJugador.add(equipoPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel de equipo e items para la maquina
     * Selecciona aleatoriamente 6 pokemones y asigna items por defecto
     * 
     * @param tipoMaquina Tipo de IA de la maquina
     */
    private void crearPanelMaquina(String tipoMaquina) {
        panelMaquina = new JPanel();
        panelMaquina.setBackground(new Color(255, 120, 120)); // Panel rojo para la maquina
        panelMaquina.setLayout(new BorderLayout(0, 10));
        panelMaquina.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));

        JLabel titulo = new JLabel("Maquina (" + tipoMaquina + ")", SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(new Color(180, 50, 50));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 5, 5, 5)));
        titulo.setFont(new Font("Pokemon GB", Font.BOLD, 24));
        titulo.setPreferredSize(new Dimension(0, 40));
        panelMaquina.add(titulo, BorderLayout.NORTH);

        // Seleccion aleatoria de 6 pokemones para la maquina
        nombresEquipoMaquina.clear();
        ArrayList<String> copiaPokemones = new ArrayList<>(listaPokemones);
        Collections.shuffle(copiaPokemones);
        for (int i = 0; i < 6; i++) {
            nombresEquipoMaquina.add(copiaPokemones.get(i));
        }

        JPanel equipoPanel = new JPanel(new BorderLayout(0, 5));
        equipoPanel.setBackground(new Color(255, 180, 180));
        equipoPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
        equipoPanel.setPreferredSize(new Dimension(0, 220));

        JLabel equipoLabel = new JLabel("Equipo de la maquina:");
        equipoLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        equipoPanel.add(equipoLabel, BorderLayout.NORTH);

        JPanel pokemonesPanel = new JPanel(new GridLayout(6, 1, 0, 2));
        pokemonesPanel.setBackground(new Color(255, 180, 180));
        for (String poke : nombresEquipoMaquina) {
            JPanel pokeRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2)); 
            pokeRow.setBackground(new Color(255, 180, 180));
            JLabel iconLabel = new JLabel(imagenesPokemones.get(poke));
            iconLabel.setPreferredSize(new Dimension(48, 48));
            JLabel nameLabel = new JLabel(poke);
            nameLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
            nameLabel.setForeground(Color.BLACK);
            pokeRow.add(iconLabel);
            pokeRow.add(nameLabel);
            pokemonesPanel.add(pokeRow);
        }
        equipoPanel.add(pokemonesPanel, BorderLayout.CENTER);

        // Items de la maquina: 2 de cada uno, excepto Revive (1)
        itemsMaquina.clear();
        for (String item : itemsDisponibles) {
            itemsMaquina.put(item, item.equals("Revive") ? 1 : 2);
        }

        JPanel itemPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        itemPanel.setBackground(new Color(255, 180, 180));
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10)));
        for (String item : itemsDisponibles) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 2));
            row.setBackground(new Color(255, 180, 180));
            JLabel iconLabel = new JLabel(imagenesItems.get(item));
            iconLabel.setPreferredSize(new Dimension(32, 32));
            JLabel itemLabel = new JLabel(item + ": " + itemsMaquina.get(item));
            itemLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
            itemLabel.setPreferredSize(new Dimension(200, 25));
            row.add(iconLabel);
            row.add(itemLabel);
            itemPanel.add(row);
        }

        equipoPanel.add(itemPanel, BorderLayout.SOUTH);
        panelMaquina.add(equipoPanel, BorderLayout.CENTER);
    }

    /**
     * Carga las imagenes de los pokemones y los items para mostrar en la interfaz
     * Escala las imagenes para que se ajusten a los paneles
     */
    private void cargarImagenes() {
        for (String nombre : listaPokemones) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/graficos/pokemones/" + nombre.toLowerCase() + ".png"));
                Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                imagenesPokemones.put(nombre, new ImageIcon(img));
            } catch (Exception e) {
                System.err.println("Error al cargar imagen de " + nombre + ": " + e.getMessage());
            }
        }
        for (String item : itemsDisponibles) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/graficos/items/" + item.toLowerCase() + ".png"));
                Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                imagenesItems.put(item, new ImageIcon(img));
            } catch (Exception e) {
                System.err.println("Error al cargar imagen de item " + item + ": " + e.getMessage());
            }
        }
    }
}