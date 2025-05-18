package presentacion;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Clase PvPSupervivenciaSetUp
 *
 * Esta clase configura automáticamente equipos aleatorios de 6 Pokémon para cada jugador
 * en el modo Supervivencia, mostrando las imágenes de los Pokémon asignados.
 *
 * Autores: David Patacon y Daniel Hueso
 * Version: 1.1
 */
public class PvPSupervivenciaSetUp extends JFrame {
    private JPanel panelJugador1, panelJugador2;
    private DefaultListModel<String> equipo1Model = new DefaultListModel<>();
    private DefaultListModel<String> equipo2Model = new DefaultListModel<>();
    
    private HashMap<String, ImageIcon> imagenesPokemones = new HashMap<>();
    private HashMap<String, ImageIcon> imagenesPokemonesMini = new HashMap<>();
    private ArrayList<JLabel> imagenesEquipo1 = new ArrayList<>();
    private ArrayList<JLabel> imagenesEquipo2 = new ArrayList<>();

    private ArrayList<String> listaPokemones = new ArrayList<>() {{
        add("Charizard"); add("Blastoise"); add("Venusaur");
        add("Gengar"); add("Dragonite"); add("Snorlax"); add("Raichu");
        add("Togetic"); add("Tyranitar"); add("Gardevoir");
        add("Metagross"); add("Donphan"); add("Machamp"); add("Delibird");
        add("Scizor"); add("Mewtwo"); add("Torkoal"); add("Milotic"); add("Sceptile"); add("Manectric");
        add("Glalie"); add("Kabutops"); add("Whiscash"); add("Masquerain");
        add("Banette"); add("Altaria"); add("Claydol"); add("Hariyama");
        add("Swellow"); add("Aggron"); add("Weezing"); add("Nidoking");
        add("Zangoose"); add("Clefable"); add("Absol"); add("Chimecho");
    }};

    private ArrayList<String> nombresEquipo1 = new ArrayList<>();
    private ArrayList<String> nombresEquipo2 = new ArrayList<>();

    public PvPSupervivenciaSetUp() {
        setTitle("Configuración - PvP Modo Supervivencia");
        setSize(1200, 800); // Aumenté un poco el tamaño para mejor visualización
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));

        cargarImagenes();
        crearPanelJugador(panelJugador1 = new JPanel(), 1);
        crearPanelJugador(panelJugador2 = new JPanel(), 2);

        // Generar equipos aleatorios automáticamente
        generarEquipoAleatorio(1);
        generarEquipoAleatorio(2);

        // Cargar fuente Pokemon GB
        try {
            Font pokemonFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getResourceAsStream("/graficos/PokemonGB.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (Exception e) {
            System.err.println("Error al cargar la fuente Pokemon GB: " + e.getMessage());
        }

        add(panelJugador1);
        add(panelJugador2);
    }

    private void crearPanelJugador(JPanel panel, int jugador) {
        Color colorFondo = jugador == 1 ? new Color(255, 100, 100) : new Color(100, 100, 255);
        Color colorSecundario = jugador == 1 ? new Color(255, 220, 220) : new Color(220, 220, 255);

        panel.setBackground(colorSecundario);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(15, 15, 15, 15)));

        // Panel superior con título y modo de juego
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(colorFondo);
        topPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));

        JLabel titulo = new JLabel("Jugador " + jugador, SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Pokemon GB", Font.BOLD, 28));
        topPanel.add(titulo, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        // Panel central con los Pokémon asignados
        JPanel equipoPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        equipoPanel.setBackground(colorSecundario);
        equipoPanel.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(Color.BLACK, 2), "Equipo Asignado", 
                TitledBorder.CENTER, TitledBorder.TOP, 
                new Font("Pokemon GB", Font.BOLD, 16), Color.BLACK),
            new EmptyBorder(10, 10, 10, 10)));

        // Inicializar las etiquetas de imágenes para los 6 Pokémon
        ArrayList<JLabel> imagenesEquipo = jugador == 1 ? imagenesEquipo1 : imagenesEquipo2;
        for (int i = 0; i < 6; i++) {
            JLabel pokemonLabel = new JLabel("", SwingConstants.CENTER);
            pokemonLabel.setPreferredSize(new Dimension(150, 150));
            pokemonLabel.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK, 1),
                new EmptyBorder(5, 5, 5, 5)));
            pokemonLabel.setBackground(Color.WHITE);
            pokemonLabel.setOpaque(true);
            pokemonLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
            pokemonLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            pokemonLabel.setFont(new Font("Pokemon GB", Font.PLAIN, 12));
            
            imagenesEquipo.add(pokemonLabel);
            equipoPanel.add(pokemonLabel);
        }

        panel.add(equipoPanel, BorderLayout.CENTER);

        // Panel inferior con botón de confirmación
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(colorSecundario);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton confirmar = new JButton("CONFIRMAR EQUIPO");
        confirmar.setBackground(colorFondo.darker());
        confirmar.setForeground(Color.WHITE);
        confirmar.setFocusPainted(false);
        confirmar.setFont(new Font("Pokemon GB", Font.BOLD, 16));
        confirmar.setBorder(new CompoundBorder(
            new LineBorder(Color.BLACK, 2),
            new EmptyBorder(10, 20, 10, 20)));
        confirmar.addActionListener(new ConfirmarEquipoListener(jugador));
        
        bottomPanel.add(confirmar);
        panel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarImagenes() {
        for (String nombre : listaPokemones) {
            try {
                // Imagen normal (para mostrar en el equipo)
                ImageIcon icon = new ImageIcon(getClass().getResource("/graficos/pokemones/" + nombre.toLowerCase() + ".png"));
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imagenesPokemones.put(nombre, new ImageIcon(img));
                
                // Imagen mini (para posibles usos futuros)
                Image imgMini = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                imagenesPokemonesMini.put(nombre, new ImageIcon(imgMini));
            } catch (Exception e) {
                System.err.println("Error al cargar imagen de " + nombre + ": " + e.getMessage());
            }
        }
    }

    private void generarEquipoAleatorio(int jugador) {
        DefaultListModel<String> model = jugador == 1 ? equipo1Model : equipo2Model;
        ArrayList<JLabel> imagenesEquipo = jugador == 1 ? imagenesEquipo1 : imagenesEquipo2;
        model.clear();
        
        // Hacer una copia de la lista de pokémones para no modificar la original
        ArrayList<String> pokemonesDisponibles = new ArrayList<>(listaPokemones);
        Collections.shuffle(pokemonesDisponibles);
        
        // Seleccionar los primeros 6 pokémones de la lista mezclada
        for (int i = 0; i < 6 && i < pokemonesDisponibles.size(); i++) {
            String pokemon = pokemonesDisponibles.get(i);
            model.addElement(pokemon);
            
            // Actualizar la imagen correspondiente en el equipo
            if (i < imagenesEquipo.size()) {
                JLabel label = imagenesEquipo.get(i);
                label.setIcon(imagenesPokemones.get(pokemon));
                label.setText(pokemon);
            }
        }
    }

    private class ConfirmarEquipoListener implements ActionListener {
        int jugador;

        public ConfirmarEquipoListener(int jugador) {
            this.jugador = jugador;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultListModel<String> model = jugador == 1 ? equipo1Model : equipo2Model;
            ArrayList<String> equipo = jugador == 1 ? nombresEquipo1 : nombresEquipo2;

            equipo.clear();
            
            for (int i = 0; i < model.size(); i++) {
                equipo.add(model.get(i));
            }

            JOptionPane.showMessageDialog(null, 
                "Equipo del Jugador " + jugador + " confirmado!\n" +
                "Listo para la batalla de supervivencia.",
                "Equipo Confirmado", 
                JOptionPane.INFORMATION_MESSAGE);

            // Verificar que ambos equipos estén confirmados
            if (nombresEquipo1.size() == 6 && nombresEquipo2.size() == 6) {
                PokemonBattleGame.iniciarBatalla(
                    new ArrayList<>(nombresEquipo1),
                    new ArrayList<>(nombresEquipo2),
                    new HashMap<>(),  // Sin items para el jugador 1
                    new HashMap<>()   // Sin items para el jugador 2
                );
                dispose();
            }
        }
    }
}
