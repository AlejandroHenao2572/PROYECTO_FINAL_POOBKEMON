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
 * Esta clase permite a dos jugadores configurar sus equipos Pokemon e items
 * para una batalla en modo normal.
 *
 * Autores: David Patacon y Daniel Hueso
 * Version: 1.0
 */
public class PvPNormalSetUp extends JFrame {
    private JPanel panelJugador1, panelJugador2;
    private DefaultListModel<String> equipo1Model = new DefaultListModel<>();
    private DefaultListModel<String> equipo2Model = new DefaultListModel<>();
    private JList<String> equipoList1 = new JList<>(equipo1Model);
    private JList<String> equipoList2 = new JList<>(equipo2Model);

    private HashMap<String, ImageIcon> imagenesPokemones = new HashMap<>();
    private JLabel imagenPokemon1 = new JLabel();
    private JLabel imagenPokemon2 = new JLabel();
    private HashMap<String, ImageIcon> imagenesItems = new HashMap<>();

    private ArrayList<String> listaPokemones = new ArrayList<>() {{
        add("charizard"); add("blastoise"); add("venusaur");
        add("gengar"); add("dragonite"); add("snorlax"); add("raichu");
        add("togetic"); add("tyranitar"); add("gardevoir");
        add("metagross"); add("donphan"); add("machamp"); add("delibird");
    }};

    private ArrayList<String> nombresEquipo1 = new ArrayList<>();
    private ArrayList<String> nombresEquipo2 = new ArrayList<>();

    private HashMap<String, Integer> itemsJugador1 = new HashMap<>();
    private HashMap<String, Integer> itemsJugador2 = new HashMap<>();
    private final String[] itemsDisponibles = {"Potion", "SuperPotion", "HyperPotion", "Revive"};

    public PvPNormalSetUp() {
        setTitle("Configuracion - PvP Modo Normal");
        setSize(1200, 700);
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));

        cargarImagenes();
        crearPanelJugador(panelJugador1 = new JPanel(), 1);
        crearPanelJugador(panelJugador2 = new JPanel(), 2);

        add(panelJugador1);
        add(panelJugador2);
    }

    private void crearPanelJugador(JPanel panel, int jugador) {
        Color colorFondo = jugador == 1 ? new Color(255, 100, 100) : new Color(100, 100, 255);
        Color colorSecundario = jugador == 1 ? new Color(255, 200, 200) : new Color(200, 200, 255);

        panel.setBackground(colorSecundario);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));

        JLabel titulo = new JLabel("Jugador " + jugador, SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(colorFondo);
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 5, 5, 5)));
        titulo.setFont(new Font("Pokemon GB", Font.BOLD, 24));
        titulo.setPreferredSize(new Dimension(0, 40));
        panel.add(titulo, BorderLayout.NORTH);

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

        JPanel itemPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        itemPanel.setBackground(colorSecundario);
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10)));

        HashMap<String, JSpinner> spinners = new HashMap<>();
        for (String item : itemsDisponibles) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
            row.setBackground(colorSecundario);
            JLabel iconLabel = new JLabel(imagenesItems.get(item));
            iconLabel.setPreferredSize(new Dimension(32, 32));
            JLabel itemLabel = new JLabel(item);
            itemLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 14));
            itemLabel.setPreferredSize(new Dimension(100, 25));
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

    private void cargarImagenes() {
        for (String nombre : listaPokemones) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/graficos/pokemones/" + nombre.toLowerCase() + ".png"));
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
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

    // ✅ Nuevo: permitir entre 1 y 6 Pokémon
    if (model.size() < 1 || model.size() > 6) {
        JOptionPane.showMessageDialog(null, "Debes seleccionar al menos 1 y como máximo 6 Pokémon.");
        return;
    }

    for (String item : itemsDisponibles) {
        int cantidad = (int) spinners.get(item).getValue();
        totalItems += cantidad;
    }

    if (totalItems > 7) {
        JOptionPane.showMessageDialog(null, "No puedes seleccionar más de 7 ítems.");
        return;
    }

    for (String item : itemsDisponibles) {
        int cantidad = (int) spinners.get(item).getValue();
        if (cantidad > 0) {
            items.put(item, cantidad);
        }
    }

    for (int i = 0; i < model.size(); i++) {
        equipo.add(model.get(i));
    }

    JOptionPane.showMessageDialog(null, "Equipo del Jugador " + jugador + " confirmado");

    // ✅ Verificar que ambos equipos estén confirmados (mínimo 1 Pokémon por jugador)
    if (nombresEquipo1.size() >= 1 && nombresEquipo2.size() >= 1) {
        PokemonBattleGame.iniciarBatalla(
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
