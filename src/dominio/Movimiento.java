package dominio;

import java.io.Serializable;

/**
 * Clase abstracta base para todos los movimientos de pokemon
 * Representa un movimiento generico que puede usar un pokemon
 * Define atributos y comportamientos comunes para todos los movimientos
 * 
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
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

    // Metodos de acceso
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getPotencia() { return potencia; }
    public int getPrecision() { return precision; }
    public int getPP() { return pp; }
    public int getPPMaximos() { return ppMaximos; }
    public int getPrioridad() { return prioridad; }

    /**
     * Ejecuta el efecto del movimiento
     * 
     * @param atacante Pokemon que realiza els movimiento
     * @param objetivo Pokemon que recibe el movimiento
     */
    public abstract String ejecutar(Pokemon atacante, Pokemon objetivo);

    public double calcularEfectividad(Pokemon rival) {
        // Ejemplo simple: solo considera algunos tipos comunes
        String tipoRival = rival.getTipo(); // Asume que el Pokémon tiene un método getTipo()
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
        // Por defecto, efectividad normal
        return 1.0;
    }
}