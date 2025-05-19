package dominio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa movimientos de estado sin daño directo
 * Representa movimientos que alteran estados en lugar de causar daño
 * Pueden afectar estadisticas, condiciones o propiedades del objetivo
 * 
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public class MovimientoEstado extends Movimiento {
    
    private String efectoSecundario;
    /**
     * Crea un nuevo movimiento de estado
     * 
     * @param nombre Nombre identificativo del movimiento
     * @param tipo Tipo elemental del movimiento
     * @param precision Probabilidad de exito (0-100)
     * @param pp Puntos de poder iniciales
     * @param efectoSecundario Descripcion del efecto aplicado
     */
    public MovimientoEstado(String nombre, String tipo, int precision, int pp, String efectoSecundario) {
        super(nombre, tipo, 0, precision, pp);
        this.efectoSecundario = efectoSecundario;
    }

    /**
     * Ejecuta el efecto de estado sobre el objetivo
     * No causa daño directo pero aplica efectos secundarios
     * 
     * @param atacante Pokemon que usa el movimiento
     * @param objetivo Pokemon afectado por el movimiento
     */
    @Override
    public String ejecutar(Pokemon atacante, Pokemon objetivo) {
        Logger logger = Logger.getLogger(MovimientoEstado.class.getName());
        String estadisticaAfectada = "";
        int aumento = 0;
        String message;
        try {
            if (!esUtilizable()) return null;

            switch (efectoSecundario) {
                case "Aumenta puntos de salud":
                    aumento = atacante.aumentarEstadisticas("ps", 10);
                    estadisticaAfectada = "PS";
                    break;
                case "Aumenta ataque":
                    aumento = atacante.aumentarEstadisticas("ataque", 10);
                    estadisticaAfectada = "ataque";
                    break;
                case "Aumenta defensa":
                    aumento = atacante.aumentarEstadisticas("defensa", 10);
                    estadisticaAfectada = "defensa";
                    break;
                case "Aumenta velocidad":
                    aumento = atacante.aumentarEstadisticas("velocidad", 10);
                    estadisticaAfectada = "velocidad";
                    break;
                case "Aumenta ataque Especial":
                    aumento = atacante.aumentarEstadisticas("ataqueEspecial", 10);
                    estadisticaAfectada = "ataque especial";
                    break;
                case "Aumenta defensa Especial":
                    aumento = atacante.aumentarEstadisticas("defensaEspecial", 10);
                    estadisticaAfectada = "defensa especial";
                    break;
                default:
                    break;
            }
            message = String.format("%s usó %s! %s aumentó en %d puntos.",
                                    atacante.getNombre(),
                                    getNombre(),
                                    estadisticaAfectada,
                                    aumento);

            usar();
        } catch (POOBkemonException e) {
            logger.log(Level.WARNING, "Error al ejecutar MovimientoEstado: " + e.getMessage(), e);
            message = "Error al ejecutar el movimiento de estado: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al ejecutar MovimientoEstado: " + e.getMessage(), e);
            message = "Error inesperado al ejecutar el movimiento de estado.";
        }
        return message;
    }

    public String getEfectoSecundario(){
        return efectoSecundario;
    }
}