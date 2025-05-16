package presentacion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase que representa al jugador en el juego
 * Hereda de la clase Entity y gestiona la entrada del usuario el movimiento
 * y la representacion grafica del jugador
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    /**
     * Constructor de la clase Player
     * Inicializa el panel de juego el manejador de teclas establece los valores predeterminados
     * y carga la imagen del jugador
     *
     * @param gp   El panel de juego donde se dibuja el jugador
     * @param keyH El manejador de teclas para la entrada del usuario
     */
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Establece los valores predeterminados para la posicion velocidad y direccion del jugador
     */
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    /**
     * Carga la imagen del jugador desde un archivo
     * Actualmente solo carga una imagen para todas las direcciones
     */
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/graficos/player/red.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la posicion del jugador basandose en la entrada del teclado
     * y limita al jugador dentro de los bordes de la pantalla
     */
    public void update() {
        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        }
        if (keyH.downPressed) {
            direction = "down";
            y += speed;
        }
        if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        }
        if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }

        // Limita al jugador dentro de los bordes de la pantalla
        x = Math.max(0, Math.min(x, gp.screenWidth - gp.titleSize));
        y = Math.max(0, Math.min(y, gp.screenHeight - gp.titleSize));
    }

    /**
     * Dibuja al jugador en el panel de juego
     * Actualmente dibuja la misma imagen independientemente de la direccion
     *
     * @param g2 El objeto Graphics2D para dibujar
     */
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
            case "down":
            case "left":
            case "right":
                image = up1; // Solo usamos una imagen por ahora
                break;
        }

        g2.drawImage(image, x, y, gp.titleSize, gp.titleSize, null);
    }

    /**
     * Devuelve un objeto Rectangle que representa los limites del jugador
     * Se utiliza para la deteccion de colisiones
     *
     * @return Un objeto Rectangle con la posicion y el tamaño del jugador
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, gp.titleSize, gp.titleSize);
    }
}