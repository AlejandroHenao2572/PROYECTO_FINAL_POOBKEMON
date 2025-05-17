package presentacion;

import javax.swing.*;
import java.awt.*;

public class CoinFlipDialog extends JDialog {
    private boolean resultado; // true = jugador1, false = jugador2

    public CoinFlipDialog(JFrame parent, boolean resultado) {
        super(parent, "Lanzamiento de Moneda", true);
        this.resultado = resultado;

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setUndecorated(true);

        // Panel principal con diseño personalizado
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

        JLabel mensaje = new JLabel((resultado ? "¡Jugador 1" : "¡Jugador 2") + " comienza!");
        mensaje.setFont(new Font("Pokemon GB", Font.BOLD, 22));
        mensaje.setForeground(Color.WHITE);
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton confirmar = new JButton("CONFIRMAR");
        confirmar.setFont(new Font("Pokemon GB", Font.BOLD, 16));
        confirmar.setBackground(new Color(50, 50, 50));
        confirmar.setForeground(Color.WHITE);
        confirmar.setFocusPainted(false);
        confirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        confirmar.setPreferredSize(new Dimension(200, 40));
        confirmar.addActionListener(e -> dispose());

        // Espaciado y disposición
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(imagen);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(mensaje);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(confirmar);
        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
    }

    public boolean getResultado() {
        return resultado;
    }
}
