package dominio;

import java.io.Serializable;

/**
 * Clase abstracta base para todos los movimientos de pokemon
 * Representa un movimiento generico que puede usar un pokemon
 * Define atributos y comportamientos comunes para todos los movimientos
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public abstract class Movimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nombre;
    protected String tipo;
    protected int potencia;
    protected int precision;
    protected int pp;
    protected int ppMaximos;
    protected int prioridad;

    /**
     * Crea un nuevo movimiento con sus caracteristicas basicas
     * 
     * @param nombre Nombre identificativo del movimientoa
     * @param tipo Tipo elemental del movimiento
     * @param potencia Valor de daño base del movimiento
     * @param precision Probabilidad de acierto (0-100)
     * @param pp Puntos de poder iniciales
     */
    public Movimiento(String nombre, String tipo, int potencia, int precision, int pp) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potencia = potencia;
        this.precision = precision;
        this.pp = pp;
        this.ppMaximos = pp;
        this.prioridad = 0;
    }

    /**
     * Verifica si el movimiento puede usarse
     * 
     * @return true si tiene PP disponible, false si no
     */
    public boolean esUtilizable() {
        return pp > 0;
    }

    /**
     * Consume un punto de poder al usar el movimiento
     */
    public void usar() {
        if (pp > 0) pp--;
    }

    /**
     * Obtiene el nombre del movimiento
     * @return nombre del movimiento
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el tipo del movimiento
     * @return tipo del movimiento
     */
    public String getTipo() { return tipo; }

    /**
     * Obtiene la potencia del movimiento
     * @return potencia del movimiento
     */
    public int getPotencia() { return potencia; }

    /**
     * Obtiene la precision del movimiento
     * @return precision del movimiento
     */
    public int getPrecision() { return precision; }

    /**
     * Obtiene los PP actuales del movimiento
     * @return PP actuales
     */
    public int getPP() { return pp; }

    /**
     * Obtiene los PP maximos del movimiento
     * @return PP maximos
     */
    public int getPPMaximos() { return ppMaximos; }

    /**
     * Obtiene la prioridad del movimiento
     * @return prioridad del movimiento
     */
    public int getPrioridad() { return prioridad; }

    /**
     * Calcula la efectividad del movimiento contra un pokemon rival
     * 
     * @param rival Pokemon que recibe el movimiento
     * @return multiplicador de efectividad (2.0 super efectivo 0.5 no muy efectivo 1.0 normal)
     */
    public double calcularEfectividad(Pokemon rival) {
        String tipoRival = rival.getTipo(); 
        if (tipo.equals("Fuego")) {
            if (tipoRival.equals("Planta")) return 2.0;
            if (tipoRival.equals("Agua")) return 0.5;
            if (tipoRival.equals("Fuego")) return 0.5;
        } else if (tipo.equals("Agua")) {
            if (tipoRival.equals("Fuego")) return 2.0;
            if (tipoRival.equals("Planta")) return 0.5;
            if (tipoRival.equals("Agua")) return 0.5;
        } else if (tipo.equals("Planta")) {
            if (tipoRival.equals("Agua")) return 2.0;
            if (tipoRival.equals("Fuego")) return 0.5;
            if (tipoRival.equals("Planta")) return 0.5;
        }
        return 1.0;
    }

    /**
     * Ejecuta el efecto del movimiento
     * 
     * @param atacante Pokemon que realiza el movimiento
     * @param objetivo Pokemon que recibe el movimiento
     * @return String con el resultado de la accion
     */
    public abstract String ejecutar(Pokemon atacante, Pokemon objetivo);
}