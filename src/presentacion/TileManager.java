package presentacion;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

/**
 * Clase TileManager
 *
 * Esta clase gestiona la carga y el dibujado de los tiles del mapa del juego.
 * Lee la informacion del mapa desde un archivo de texto y almacena los tiles
 * en una matriz para su posterior representacion en la pantalla.
 *
 * Autores: David Patacon y Daniel Hueso
 * Version: 1.0
 */
public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];

    /**
     * Constructor de la clase TileManager.
     * Inicializa el TileManager con una instancia del GamePanel.
     * Carga las imagenes de los tiles y el mapa desde el archivo.
     * @param gp La instancia del GamePanel asociada.
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap("/graficos/player/map.txt");
    }

    /**
     * Carga las imagenes de los diferentes tipos de tiles.
     * Actualmente carga la imagen para el pasto y la pared.
     */
    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/graficos/player/grass01.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/graficos/player/wall.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la estructura del mapa desde un archivo de texto.
     * El archivo especifica el numero de tile para cada posicion en el mapa.
     * @param filePath La ruta del archivo de texto que contiene la informacion del mapa.
     */
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                String line = br.readLine();

                while(col < gp.maxScreenCol) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col==gp.maxScreenCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {

        }

    }

    /**
     * Dibuja los tiles del mapa en la pantalla.
     * Recorre la matriz del mapa y dibuja la imagen del tile correspondiente
     * en cada posicion.
     * @param g2 El objeto Graphics2D utilizado para dibujar.
     */
    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;

        while(row < gp.maxScreenRow) {
            while(col < gp.maxScreenCol) {

                int tileNum = mapTileNum[col][row];

                g2.drawImage(tile[tileNum].image, col * gp.titleSize, row * gp.titleSize, gp.titleSize, gp.titleSize, null);
                col++;
            }
            col = 0;
            row++;
        }
    }
}