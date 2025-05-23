package presentacion;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa un dialogo grafico para mostrar el resultado del lanzamiento de moneda
 * Indica visualmente que jugador comienza la batalla mostrando una moneda de color y un mensaje
 * Permite al usuario confirmar para cerrar el dialogo
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class CoinFlipDialog extends JDialog {
    // true = jugador1 comienza, false = jugador2 comienza
    private boolean resultado;

    /**
     * Constructor del dialogo de lanzamiento de moneda
     * 
     * @param parent Ventana principal sobre la que se muestra el dialogo
     * @param resultado true si comienza jugador1, false si comienza jugador2
     */
    public CoinFlipDialog(JFrame parent, boolean resultado) {
        super(parent, "Lanzamiento de Moneda", true);
        this.resultado = resultado;

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setUndecorated(true);

        // Panel principal con diseño personalizado de fondo redondeado
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(14, 174, 147));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Imagen de la moneda
        JLabel imagen = new JLabel();
        imagen.setAlignmentX(Component.CENTER_ALIGNMENT);

        String caraPath = "graficos/red_coin.png";
        String cruzPath = "graficos/blue_coin.png";

        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(resultado ? caraPath : cruzPath));
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imagen.setText("Error al cargar imagen de moneda");
            imagen.setForeground(Color.WHITE);
        }

        // Mensaje que indica que jugador comienza
        JLabel mensaje = new JLabel((resultado ? "¡Jugador 1" : "¡Jugador 2") + " comienza!");
        mensaje.setFont(new Font("Pokemon GB", Font.BOLD, 22));
        mensaje.setForeground(Color.WHITE);
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Boton para confirmar y cerrar el dialogo
        JButton confirmar = new JButton("CONFIRMAR");
        confirmar.setFont(new Font("Pokemon GB", Font.BOLD, 16));
        confirmar.setBackground(new Color(50, 50, 50));
        confirmar.setForeground(Color.WHITE);
        confirmar.setFocusPainted(false);
        confirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        confirmar.setPreferredSize(new Dimension(200, 40));
        confirmar.addActionListener(e -> dispose());

        // Espaciado y disposicion de los componentes
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(imagen);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(mensaje);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(confirmar);
        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
    }

    /**
     * Devuelve el resultado del lanzamiento de moneda
     * @return true si comienza jugador1, false si comienza jugador2
     */
    public boolean getResultado() {
        return resultado;
    }
}
