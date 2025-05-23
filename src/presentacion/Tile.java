package presentacion;

import java.awt.image.BufferedImage;


/**
 * Clase Tile
 *
 * Esta clase representa un bloque individual dentro del mapa del juego.
 * Cada Tile puede tener una imagen asociada y una propiedad de colision.
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class Tile {
    public BufferedImage image;
    public boolean collision = false;
}