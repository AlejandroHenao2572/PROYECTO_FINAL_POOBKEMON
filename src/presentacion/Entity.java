package presentacion;

import java.awt.image.BufferedImage;

/**
 * Clase base para entidades en el juego
 * Contiene atributos comunes como posicion velocidad direccion e imagenes de animacion
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class Entity {
    public int x, y;
    public int speed;

    public BufferedImage up1;
    public String direction;
}