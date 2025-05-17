package dominio;

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
        if (!esUtilizable()) return null;
        
        String message = String.format("%s usó %s: %s%n", atacante.getNombre(), nombre, efectoSecundario);
        usar();
        return message;
    }

    public String getEfectoSecundario(){
        return efectoSecundario;
    }
}