package presentacion;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase PvPNormalSetUp
 *
 * Permite a dos jugadores humanos configurar sus equipos Pokemon e items
 * para una batalla en modo normal (PvP).
 * Cada jugador puede seleccionar hasta 6 Pokemon y hasta 7 items (con maximo 1 Revive).
 * Muestra visualmente los equipos y los items seleccionados para ambos jugadores.
 * Al confirmar ambos equipos, inicia la batalla llamando a PokemonBattleGame.iniciarBatalla.
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class PvPNormalSetUp extends JFrame {
    // Paneles para cada jugador
    private JPanel panelJugador1, panelJugador2;
    // Modelos y listas para mostrar los equipos seleccionados
    private DefaultListModel<String> equipo1Model = new DefaultListModel<>();
    private DefaultListModel<String> equipo2Model = new DefaultListModel<>();
    private JList<String> equipoList1 = new JList<>(equipo1Model);
    private JList<String> equipoList2 = new JList<>(equipo2Model);

    // Mapas para imagenes de pokemones e items
    private HashMap<String, ImageIcon> imagenesPokemones = new HashMap<>();
    private JLabel imagenPokemon1 = new JLabel();
    private JLabel imagenPokemon2 = new JLabel();
    private HashMap<String, ImageIcon> imagenesItems = new HashMap<>();

    // Lista de nombres de pokemones disponibles
    private ArrayList<String> listaPokemones = new ArrayList<>() {{
        add("charizard"); add("blastoise"); add("venusaur");
        add("gengar"); add("dragonite"); add("snorlax"); add("raichu");
        add("togetic"); add("tyranitar"); add("gardevoir");
        add("metagross"); add("donphan"); add("machamp"); add("delibird");
        add("scizor"); add("mewtwo");add("torkoal"); add("milotic"); add("sceptile");add("manectric");
        add("glalie"); add("kabutops"); add("whiscash"); add("masquerain");
        add("banette"); add("altaria"); add("claydol"); add("hariyama");
        add("swellow"); add("aggron"); add("weezing"); add("nidoking");
        add("zangoose"); add("clefable"); add("absol"); add("chimecho");
    }};

    // Listas para los nombres de los equipos seleccionados por cada jugador
    private ArrayList<String> nombresEquipo1 = new ArrayList<>();
    private ArrayList<String> nombresEquipo2 = new ArrayList<>();

    // Mapas para los items seleccionados por cada jugador
    private HashMap<String, Integer> itemsJugador1 = new HashMap<>();
    private HashMap<String, Integer> itemsJugador2 = new HashMap<>();
    // Lista de items disponibles
    private final String[] itemsDisponibles = {"Potion", "SuperPotion", "HyperPotion", "Revive"};

    /**
     * Constructor de la ventana de configuracion PvP Normal
     * Inicializa la interfaz, paneles y equipos para ambos jugadores
     */
    public PvPNormalSetUp() {
        setTitle("PvP MODO NORMAL");
        setSize(1280, 720); // Tamaño ligeramente mayor
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(20, 20, 20)); // Fondo oscuro retro
        
        // Cargar fuente Pokemon GB
        try {
            Font pokemonFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getResourceAsStream("/graficos/PokemonGB.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (Exception e) {
            System.err.println("Error al cargar la fuente Pokemon GB: " + e.getMessage());
        }

        cargarImagenes(); // Cargar imagenes de pokemones e items
        crearPanelJugador(panelJugador1 = new JPanel(), 1); // Panel para jugador 1
        crearPanelJugador(panelJugador2 = new JPanel(), 2); // Panel para jugador 2

        add(panelJugador1);
        add(panelJugador2);
    }

    /**
     * Crea el panel de seleccion de equipo y items para un jugador
     * Permite seleccionar hasta 6 pokemones y hasta 7 items en total
     * Al confirmar, valida la seleccion y si ambos equipos estan listos, inicia la batalla
     * 
     * @param panel Panel a configurar
     * @param jugador Numero de jugador (1 o 2)
     */
    private void crearPanelJugador(JPanel panel, int jugador) {
        Color colorFondo = jugador == 1 ? new Color(255, 100, 100) : new Color(100, 100, 255);
        Color colorSecundario = jugador == 1 ? new Color(255, 200, 200) : new Color(200, 200, 255);

        panel.setBackground(colorSecundario);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));

        // Titulo del panel
        JLabel titulo = new JLabel("Jugador " + jugador, SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(colorFondo);
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 5, 5, 5)));
        titulo.setFont(new Font("Pokemon GB", Font.BOLD, 24));
        titulo.setPreferredSize(new Dimension(0, 40));
        panel.add(titulo, BorderLayout.NORTH);

        // Panel para seleccionar pokemones y mostrar imagen
        JPanel seleccionPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        seleccionPanel.setBackground(colorSecundario);
        seleccionPanel.setPreferredSize(new Dimension(0, 200));

        JComboBox<String> pokeBox = new JComboBox<>(listaPokemones.toArray(new String[0]));
        pokeBox.setBackground(Color.WHITE);
        pokeBox.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
        pokeBox.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(2, 2, 2, 2)));
        pokeBox.setPreferredSize(new Dimension(0, 30));

        JLabel imagenLabel = jugador == 1 ? imagenPokemon1 : imagenPokemon2;
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenLabel.setIcon(imagenesPokemones.get(listaPokemones.get(0)));
        imagenLabel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));

        pokeBox.addActionListener(e -> imagenLabel.setIcon(imagenesPokemones.get((String) pokeBox.getSelectedItem())));

        JButton agregar = new JButton("Agregar al equipo");
        agregar.setBackground(colorFondo);
        agregar.setForeground(Color.WHITE);
        agregar.setFocusPainted(false);
        agregar.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        agregar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(2, 5, 2, 5)));
        agregar.setPreferredSize(new Dimension(0, 30));
        agregar.addActionListener(e -> {
            String seleccionado = (String) pokeBox.getSelectedItem();
            DefaultListModel<String> model = jugador == 1 ? equipo1Model : equipo2Model;
            if (model.getSize() < 6) model.addElement(seleccionado);
        });

        seleccionPanel.add(pokeBox);
        seleccionPanel.add(imagenLabel);
        seleccionPanel.add(agregar);

        JPanel centralPanel = new JPanel(new BorderLayout(0, 10));
        centralPanel.setBackground(colorSecundario);
        centralPanel.add(seleccionPanel, BorderLayout.NORTH);

        // Panel para seleccionar items y su cantidad
        JPanel itemPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        itemPanel.setBackground(colorSecundario);
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10)));

        HashMap<String, JSpinner> spinners = new HashMap<>();
        for (String item : itemsDisponibles) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 2));
            row.setBackground(colorSecundario);
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
        panel.add(centralPanel, BorderLayout.CENTER);

        // Panel para mostrar el equipo seleccionado y confirmar
        JPanel equipoPanel = new JPanel(new BorderLayout(0, 5));
        equipoPanel.setBackground(colorSecundario);
        equipoPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
        equipoPanel.setPreferredSize(new Dimension(0, 150));

        JLabel equipoLabel = new JLabel("Equipo seleccionado:");
        equipoLabel.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        equipoPanel.add(equipoLabel, BorderLayout.NORTH);

        JList<String> lista = jugador == 1 ? equipoList1 : equipoList2;
        lista.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
        lista.setBackground(Color.WHITE);
        lista.setBorder(new LineBorder(Color.BLACK, 1));
        equipoPanel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton confirmar = new JButton("Confirmar equipo");
        confirmar.setBackground(colorFondo);
        confirmar.setForeground(Color.WHITE);
        confirmar.setFocusPainted(false);
        confirmar.setFont(new Font("Pokemon GB", Font.BOLD, 14));
        confirmar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 10, 5, 10)));
        confirmar.addActionListener(new ConfirmarEquipoListener(jugador, spinners));
        equipoPanel.add(confirmar, BorderLayout.SOUTH);

        panel.add(equipoPanel, BorderLayout.SOUTH);
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

    /**
     * Listener para el boton de confirmar equipo de cada jugador
     * Valida la seleccion de pokemones y items, y si ambos equipos estan listos, inicia la batalla
     */
    private class ConfirmarEquipoListener implements ActionListener {
        int jugador;
        HashMap<String, JSpinner> spinners;

        public ConfirmarEquipoListener(int jugador, HashMap<String, JSpinner> spinners) {
            this.jugador = jugador;
            this.spinners = spinners;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultListModel<String> model = jugador == 1 ? equipo1Model : equipo2Model;
            ArrayList<String> equipo = jugador == 1 ? nombresEquipo1 : nombresEquipo2;
            HashMap<String, Integer> items = jugador == 1 ? itemsJugador1 : itemsJugador2;

            equipo.clear();
            items.clear();
            int totalItems = 0;

            // Validar cantidad de pokemones seleccionados (entre 1 y 6)
            if (model.size() < 1 || model.size() > 6) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar al menos 1 y como maximo 6 Pokemon.");
                return;
            }

            // Validar cantidad total de items (maximo 7)
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
                    items.put(item, cantidad);
                }
            }

            // Guardar pokemones seleccionados
            for (int i = 0; i < model.size(); i++) {
                equipo.add(model.get(i));
            }

            JOptionPane.showMessageDialog(null, "Equipo del Jugador " + jugador + " confirmado");

            // Verificar que ambos equipos esten confirmados (minimo 1 Pokemon por jugador)
            if (nombresEquipo1.size() >= 1 && nombresEquipo2.size() >= 1) {
                PokemonBattleGame.iniciarBatallaPvP(
                    new ArrayList<>(nombresEquipo1),
                    new ArrayList<>(nombresEquipo2),
                    new HashMap<>(itemsJugador1),
                    new HashMap<>(itemsJugador2)
                );
                dispose();
            }
        }
    }
}