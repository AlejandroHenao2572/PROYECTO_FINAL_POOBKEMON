package dominio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa el item Potion
 * Este item permite restaurar una pequena cantidad de PS a un Pokemon
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class Potion extends Item {
    /**
     * Cantidad de PS que la Potion restaura al Pokemon
     */
    private static final int CURA_PS = 20;

    /**
     * Constructor de Potion
     * Asigna el nombre del item
     */
    public Potion() {
        super("Potion");
    }

    /**
     * Usa la Potion sobre un Pokemon objetivo
     * Restaura 20 PS si el Pokemon no esta debilitado
     *
     * @param objetivo Pokemon al que se le aplicara la Potion
     */
    @Override
    public String usarEn(Pokemon objetivo) {
        Logger logger = Logger.getLogger(Potion.class.getName());
        try {
            if (!objetivo.estaDebilitado()) {
                objetivo.restaurarPS(CURA_PS);
                return "Se restauraron 20 PS a " + objetivo.getNombre();
            } else {
                return "No puedes usar Potion en un Pokemon debilitado";
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