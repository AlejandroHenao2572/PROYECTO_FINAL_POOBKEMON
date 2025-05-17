package dominio;

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
        if (!objetivo.estaDebilitado()) {
            objetivo.restaurarPS(CURA_PS);
            return "Se restauraron 50 PS a " + objetivo.getNombre();
        } else {
            return "No puedes usar SuperPotion en un Pokemon debilitado";
        }
    }
}