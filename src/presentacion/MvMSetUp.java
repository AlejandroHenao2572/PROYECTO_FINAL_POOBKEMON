package presentacion;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * Ventana de configuracion para batallas Maquina vs Maquina (MvM)
 * Permite seleccionar equipos, items y tipo de IA para cada maquina antes de iniciar la batalla
 * Muestra visualmente los equipos y los items de cada maquina
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class MvMSetUp extends JFrame {
    // Paneles para cada maquina
    private JPanel panelMaquina1, panelMaquina2;
    // Lista de nombres de pokemones disponibles
    private ArrayList<String> listaPokemones = new ArrayList<>(Arrays.asList(
        "charizard", "blastoise", "venusaur", "gengar", "dragonite", "snorlax", "raichu",
        "togetic", "tyranitar", "gardevoir", "metagross", "donphan", "machamp", "delibird",
        "scizor", "mewtwo", "torkoal", "milotic", "sceptile", "manectric", "glalie",
        "kabutops", "whiscash", "masquerain", "banette", "altaria", "claydol", "hariyama",
        "swellow", "aggron", "weezing", "nidoking", "zangoose", "clefable", "absol", "chimecho"
    ));
    // Mapas para imagenes de pokemones e items
    private HashMap<String, ImageIcon> imagenesPokemones = new HashMap<>();
    private HashMap<String, ImageIcon> imagenesItems = new HashMap<>();
    // Items disponibles para seleccionar
    private final String[] itemsDisponibles = {"Potion", "SuperPotion", "HyperPotion", "Revive"};

    // Equipos e items de cada maquina
    private ArrayList<String> nombresEquipoMaquina1 = new ArrayList<>();
    private ArrayList<String> nombresEquipoMaquina2 = new ArrayList<>();
    private HashMap<String, Integer> itemsMaquina1 = new HashMap<>();
    private HashMap<String, Integer> itemsMaquina2 = new HashMap<>();

    // Desplegables para seleccionar tipo de IA
    private JComboBox<String> tipoMaquinaBox1;
    private JComboBox<String> tipoMaquinaBox2;

    /**
     * Constructor de la ventana de configuracion MvM
     * Inicializa la interfaz y los paneles de cada maquina
     */
    public MvMSetUp() {
        setTitle("Machine vs Machine - Preparar Batalla");
        setSize(1280, 720);
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(20, 20, 20));

        cargarImagenes();
        crearPanelMaquina1();
        crearPanelMaquina2();

        add(panelMaquina1);
        add(panelMaquina2);
    }

    /**
     * Crea el panel de configuracion para la maquina 1
     * Incluye seleccion de IA, equipo y items
     */
    private void crearPanelMaquina1() {
        panelMaquina1 = new JPanel();
        panelMaquina1.setBackground(new Color(255, 180, 180));
        panelMaquina1.setLayout(new BorderLayout(0, 10));
        panelMaquina1.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));

        // Titulo del panel
        JLabel titulo = new JLabel("Maquina 1", SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(new Color(180, 50, 50));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 5, 5, 5)));
        titulo.setFont(new Font("Pokemon GB", Font.BOLD, 24));
        titulo.setPreferredSize(new Dimension(0, 40));
        panelMaquina1.add(titulo, BorderLayout.NORTH);

        // Desplegable para tipo de IA
        String[] tipos = {"defensiveTrainer", "attackingTrainer", "chaningTrainer", "expertTrainer"};
        tipoMaquinaBox1 = new JComboBox<>(tipos);
        tipoMaquinaBox1.setFont(new Font("Pokemon GB", Font.PLAIN, 16));
        tipoMaquinaBox1.setSelectedIndex(0);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 180, 180));
        topPanel.add(tipoMaquinaBox1, BorderLayout.CENTER);
        panelMaquina1.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Seleccion aleatoria de 6 pokemones para la maquina 1
        nombresEquipoMaquina1.clear();
        ArrayList<String> copiaPokemones = new ArrayList<>(listaPokemones);
        Collections.shuffle(copiaPokemones);
        for (int i = 0; i < 6; i++) {
            nombresEquipoMaquina1.add(copiaPokemones.get(i));
        }

        // Panel para mostrar el equipo
        JPanel equipoPanel = new JPanel(new BorderLayout(0, 5));
        equipoPanel.setBackground(new Color(255, 180, 180));
        equipoPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
        equipoPanel.setPreferredSize(new Dimension(0, 220));

        JLabel equipoLabel = new JLabel("Equipo de la maquina 1:");
        equipoLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        equipoPanel.add(equipoLabel, BorderLayout.NORTH);

        JPanel pokemonesPanel = new JPanel(new GridLayout(6, 1, 0, 2));
        pokemonesPanel.setBackground(new Color(255, 180, 180));
        for (String poke : nombresEquipoMaquina1) {
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

        // Items de la maquina 1:
        itemsMaquina1.clear();
        for (String item : itemsDisponibles) {
            itemsMaquina1.put(item, item.equals("Revive") ? 1 : 2);
        }

        // Panel para mostrar los items
        JPanel itemPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        itemPanel.setBackground(new Color(255, 180, 180));
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10)));
        for (String item : itemsDisponibles) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 2));
            row.setBackground(new Color(255, 180, 180));
            JLabel iconLabel = new JLabel(imagenesItems.get(item));
            iconLabel.setPreferredSize(new Dimension(32, 32));
            JLabel itemLabel = new JLabel(item + ": " + itemsMaquina1.get(item));
            itemLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
            itemLabel.setPreferredSize(new Dimension(200, 25));
            row.add(iconLabel);
            row.add(itemLabel);
            itemPanel.add(row);
        }
        equipoPanel.add(itemPanel, BorderLayout.SOUTH);
        panelMaquina1.add(equipoPanel, BorderLayout.CENTER);
    }

    /**
     * Crea el panel de configuracion para la maquina 2
     * Incluye seleccion de IA, equipo y items
     */
    private void crearPanelMaquina2() {
        panelMaquina2 = new JPanel();
        panelMaquina2.setBackground(new Color(180, 255, 180));
        panelMaquina2.setLayout(new BorderLayout(0, 10));
        panelMaquina2.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));

        // Titulo del panel
        JLabel titulo = new JLabel("Maquina 2", SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(new Color(50, 180, 50));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 5, 5, 5)));
        titulo.setFont(new Font("Pokemon GB", Font.BOLD, 24));
        titulo.setPreferredSize(new Dimension(0, 40));
        panelMaquina2.add(titulo, BorderLayout.NORTH);

        // Desplegable para tipo de IA
        String[] tipos = {"defensiveTrainer", "attackingTrainer", "chaningTrainer", "expertTrainer"};
        tipoMaquinaBox2 = new JComboBox<>(tipos);
        tipoMaquinaBox2.setFont(new Font("Pokemon GB", Font.PLAIN, 16));
        tipoMaquinaBox2.setSelectedIndex(0);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(180, 255, 180));
        topPanel.add(tipoMaquinaBox2, BorderLayout.CENTER);
        panelMaquina2.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Seleccion aleatoria de 6 pokemones para la maquina 2
        nombresEquipoMaquina2.clear();
        ArrayList<String> copiaPokemones = new ArrayList<>(listaPokemones);
        Collections.shuffle(copiaPokemones);
        for (int i = 0; i < 6; i++) {
            nombresEquipoMaquina2.add(copiaPokemones.get(i));
        }

        // Panel para mostrar el equipo
        JPanel equipoPanel = new JPanel(new BorderLayout(0, 5));
        equipoPanel.setBackground(new Color(180, 255, 180));
        equipoPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
        equipoPanel.setPreferredSize(new Dimension(0, 220));

        JLabel equipoLabel = new JLabel("Equipo de la maquina 2:");
        equipoLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        equipoPanel.add(equipoLabel, BorderLayout.NORTH);

        JPanel pokemonesPanel = new JPanel(new GridLayout(6, 1, 0, 2));
        pokemonesPanel.setBackground(new Color(180, 255, 180));
        for (String poke : nombresEquipoMaquina2) {
            JPanel pokeRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
            pokeRow.setBackground(new Color(180, 255, 180));
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

        // Items de la maquina 2
        itemsMaquina2.clear();
        for (String item : itemsDisponibles) {
            itemsMaquina2.put(item, item.equals("Revive") ? 1 : 2);
        }

        // Panel para mostrar los items
        JPanel itemPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        itemPanel.setBackground(new Color(180, 255, 180));
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10)));
        for (String item : itemsDisponibles) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 2));
            row.setBackground(new Color(180, 255, 180));
            JLabel iconLabel = new JLabel(imagenesItems.get(item));
            iconLabel.setPreferredSize(new Dimension(32, 32));
            JLabel itemLabel = new JLabel(item + ": " + itemsMaquina2.get(item));
            itemLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
            itemLabel.setPreferredSize(new Dimension(200, 25));
            row.add(iconLabel);
            row.add(itemLabel);
            itemPanel.add(row);
        }
        equipoPanel.add(itemPanel, BorderLayout.SOUTH);
        panelMaquina2.add(equipoPanel, BorderLayout.CENTER);

        // Boton para iniciar la batalla MvM
        JButton iniciar = new JButton("Iniciar Batalla MvM");
        iniciar.setBackground(new Color(50, 180, 50));
        iniciar.setForeground(Color.WHITE);
        iniciar.setFocusPainted(false);
        iniciar.setFont(new Font("Pokemon GB", Font.BOLD, 16));
        iniciar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 10, 5, 10)));
        iniciar.addActionListener((ActionEvent e) -> {
            // Llama al metodo para iniciar la batalla MvM
            PokemonBattleGame.iniciarBatallaMvM(
                new ArrayList<>(nombresEquipoMaquina1),
                new ArrayList<>(nombresEquipoMaquina2),
                new HashMap<>(itemsMaquina1),
                new HashMap<>(itemsMaquina2),
                (String) tipoMaquinaBox1.getSelectedItem(),
                (String) tipoMaquinaBox2.getSelectedItem()
            );
            dispose();
        });
        panelMaquina2.add(iniciar, BorderLayout.SOUTH);
    }

    /**
     * Carga las imagenes de los pokemones y los items para mostrar en la interfaz
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