package dominio;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Clase que representa una SuperPotion un item que restaura PS a un Pokemon
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class SuperPotion extends Item {
    /**
     * Cantidad de PS que la SuperPotion restaura al Pokemon
     */
    private static final int CURA_PS = 50;

    /**
     * Constructor de SuperPotion
     * Asigna el nombre del item
     */
    public SuperPotion() {
        super("SuperPotion");
    }

    /**
     * Usa la SuperPotion sobre un Pokemon objetivo
     * Restaura 50 PS si el Pokemon no esta debilitado
     *
     * @param objetivo Pokemon al que se le aplicara la SuperPotion
     */
    @Override
    public String usarEn(Pokemon objetivo) {
        Logger logger = Logger.getLogger(Potion.class.getName());
        try {
            if (!objetivo.estaDebilitado()) {
                objetivo.restaurarPS(CURA_PS);
                return "Se restauraron 50 PS a " + objetivo.getNombre();
            } else {
                return "No puedes usar SuperPotion en un Pokemon debilitado";
            }
        } catch (POOBkemonException e) {
            logger.log(Level.WARNING, "Error al usar Potion: " + e.getMessage(), e);
            return "Error al usar Potion: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al usar Potion: " + e.getMessage(), e);
            return "Error inesperado al usar Potion.";
        }
    }

    
}