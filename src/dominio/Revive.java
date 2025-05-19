package dominio;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa el item Revive
 * Este item permite revivir a un Pokemon debilitado con la mitad de sus PS maximos
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class Revive extends Item {

    /**
     * Constructor de Revive
     * Asigna el nombre del item
     */
    public Revive() {
        super("Revive");
    }

    /**
     * Usa el item Revive sobre un Pokemon objetivo
     * Si el Pokemon esta debilitado lo revive con la mitad de sus PS maximos
     *
     * @param objetivo Pokemon al que se le aplicara Revive
     */
    @Override
    public String usarEn(Pokemon objetivo) {
        Logger logger = Logger.getLogger(Potion.class.getName());
        try {
            if (objetivo.estaDebilitado()) {
                int psRevive = objetivo.getPs() / 2;
                 objetivo.restaurarPS(psRevive);
            return "Se revivio a " + objetivo.getNombre() + " con " + psRevive + " PS";
        } else {
            return "Solo puedes usar Revive en un Pokemon debilitado";
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