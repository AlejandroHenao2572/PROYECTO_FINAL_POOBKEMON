package dominio;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Clase que implementa el item HyperPotion
 * Item que restaura 200 PS a un Pokemon
 * No funciona en Pokemon debilitados
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class HyperPotion extends Item {
    private static final int CURA_PS = 200;

    /**
     * Crea una nueva HyperPotion
     * Inicializa con nombre "HyperPotion"
     */
    public HyperPotion() {
        super("HyperPotion");
    }

    /**
     * Aplica el efecto de curacion al Pokemon
     * Restaura 200 PS si el Pokemon no esta debilitado
     * 
     * @param objetivo Pokemon a curar
     */

    @Override
    public String usarEn(Pokemon objetivo) {
        Logger logger = Logger.getLogger(HyperPotion.class.getName());
        try {
            if (!objetivo.estaDebilitado()) {
                objetivo.restaurarPS(CURA_PS);
                return "Se restauraron 200 PS a " + objetivo.getNombre();
            } else {
                return "No puedes usar HyperPotion en un Pokemon debilitado.";
            }
        } catch (POOBkemonException e) {
            logger.log(Level.WARNING, "Error al usar HyperPotion: " + e.getMessage(), e);
            return "Error al usar HyperPotion: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al usar HyperPotion: " + e.getMessage(), e);
            return "Error inesperado al usar HyperPotion.";
        }
    }
}