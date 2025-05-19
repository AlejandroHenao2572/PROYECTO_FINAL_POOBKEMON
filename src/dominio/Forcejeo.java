package dominio;

/**
 * Clase que implementa el movimiento fisico Forcejeo
 * Movimiento que causa daño al oponente y autodaño al usuario
 * Tipo Normal con potencia fija y precision perfecta
 *
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
import java.util.logging.Level;
import java.util.logging.Logger;

public class Forcejeo extends MovimientoFisico {
    
    /**
     * Constructor del movimiento Forcejeo
     * Inicializa con parametros predeterminados:
     * - Nombre: Forcejeo
     * - Tipo: Normal
     * - Potencia: 50
     * - Precision: 100%
     * - PP: Ilimitado (MAX_VALUE)
     */
    public Forcejeo() {
        super("Forcejeo", "Normal", 50, 100, Integer.MAX_VALUE);
    }

    /**
     * Ejecuta la logica del movimiento Forcejeo
     * Calcula daño basado en ataque/defensa y aplica autodaño
     * 
     * @param atacante Pokemon que ejecuta el movimiento
     * @param objetivo Pokemon que recibe el ataque
     */
@Override
    public String ejecutar(Pokemon atacante, Pokemon objetivo) {
        Logger logger = Logger.getLogger(Forcejeo.class.getName());
        String message;
        try {
            // Calculo de daño normal
            int dano = (int)((getPotencia() * (double)atacante.getAtaque() / objetivo.getDefensa()) / 2);
            objetivo.recibirDaño(dano);

            // El atacante recibe la mitad del daño infligido
            int autoDano = dano / 2;
            atacante.recibirDaño(autoDano);

            message = "Forcejeo causó " + dano + " de daño a " + objetivo.getNombre() +
                    " y " + autoDano + " de autodano a " + atacante.getNombre();
        } catch (POOBkemonException e) {
            logger.log(Level.WARNING, "Error al ejecutar Forcejeo: " + e.getMessage(), e);
            message = "Error al ejecutar Forcejeo: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al ejecutar Forcejeo: " + e.getMessage(), e);
            message = "Error inesperado al ejecutar Forcejeo.";
        }
        return message;
}
}