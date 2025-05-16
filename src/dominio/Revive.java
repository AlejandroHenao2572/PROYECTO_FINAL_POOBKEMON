package dominio;

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
    public void usarEn(Pokemon objetivo) {
        if (objetivo.estaDebilitado()) {
            int psRevive = objetivo.getPs() / 2;
            objetivo.restaurarPS(psRevive);
            System.out.println("Se revivio a " + objetivo.getNombre() + " con " + psRevive + " PS");
        } else {
            System.out.println("Solo puedes usar Revive en un Pokemon debilitado");
        }
    }
}