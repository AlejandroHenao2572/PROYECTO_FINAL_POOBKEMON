package presentacion;

import javax.swing.*;

import dominio.TablaTipos;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Clase StartScreen
 *
 * Esta clase representa la pantalla de inicio del juego POOBkemon.
 * Muestra una imagen de titulo y un boton para comenzar el juego.
 * Al hacer clic en el boton START se cierra esta pantalla y se inicia el GamePanel.
 *
 * Autores: David Patacon y Daniel Hueso
 * Version: 1.0
 */
public class StartScreen extends JFrame {
    /**
     * Constructor de la clase StartScreen.
     * Inicializa la ventana de inicio con el titulo la imagen de fondo
     * y el boton para comenzar el juego.
     */
    public StartScreen() {
        setTitle("POOBkemon - Inicio");
        setSize(1500, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Imagen de fondo
        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("/graficos/Titulo_juego.png")));
        fondo.setLayout(new BorderLayout());

        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Arial", Font.BOLD, 32));
        startButton.setBackground(Color.YELLOW);
        startButton.setFocusPainted(false);
        startButton.addActionListener((ActionEvent e) -> {
            dispose(); // Cierra esta pantalla
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("POOBkemon");

            GamePanel gamePanel = new GamePanel(window);
            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            gamePanel.startGameThread();
        });

        fondo.add(startButton, BorderLayout.SOUTH);
        add(fondo);
        setVisible(true);
    }

    /**
     * Metodo principal para ejecutar la pantalla de inicio.
     * @param args Argumentos de la linea de comandos .
     */
    public static void main(String[] args) {
        TablaTipos.inicializarEfectividades();
        new StartScreen();
    }
}