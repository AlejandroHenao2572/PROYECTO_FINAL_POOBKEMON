package presentacion;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase que implementa la interfaz KeyListener para manejar los eventos del teclado
 * Detecta cuando las teclas son presionadas y liberadas y actualiza las variables booleanas
 * correspondientes a las teclas de movimiento y la tecla Enter
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class KeyHandler implements KeyListener {
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean enterPressed;

    /**
     * Metodo invocado cuando se escribe un caracter
     * No se utiliza en esta implementacion
     *
     * @param e El evento de teclado
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // No necesario
    }

    /**
     * Metodo invocado cuando se presiona una tecla
     * Actualiza las variables booleanas segun la tecla presionada
     *
     * @param e El evento de teclado
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_ENTER -> enterPressed = true;
        }
    }

    /**
     * Metodo invocado cuando se libera una tecla
     * Restablece las variables booleanas segun la tecla liberada
     *
     * @param e El evento de teclado
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_ENTER -> enterPressed = false;
        }
    }
}