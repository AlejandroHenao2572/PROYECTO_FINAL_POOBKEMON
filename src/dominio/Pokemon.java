package dominio;

import java.util.ArrayList;


/**
 * Clase que representa un Pokemon en el sistema de batalla
 *
 * Modela las caracteristicas y comportamiento de un Pokemon
 * Incluye estadisticas, tipos, movimientos y metodos de combate
 * 
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public class Pokemon {
    private String nombre;
    private String tipo;
    private String tipoSecundario;
    private int ps;
    private int ataque;
    private int defensa;
    private int ataqueEspecial;
    private int defensaEspecial;
    private int velocidad;
    private int psActual;
    private ArrayList<Movimiento> movimientos;
    private int nivel;

    /**
     * Constructor principal para crear un Pokemon
     * 
     * @param nombre Nombre del Pokemon
     * @param tipo Tipo principal
     * @param tipoSecundario Tipo secundario (puede ser null)
     * @param ps Puntos de salud maximos
     * @param ataque Valor de ataque fisico
     * @param defensa Valor de defensa fisica
     * @param ataqueEspecial Valor de ataque especial
     * @param defensaEspecial Valor de defensa especial
     * @param velocidad Valor de velocidad
     * @param movimientos Lista de movimientos disponibles
     */
    public Pokemon(String nombre, String tipo, String tipoSecundario, int ps, int ataque, int defensa,
                 int ataqueEspecial, int defensaEspecial, int velocidad, ArrayList<Movimiento> movimientos) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tipoSecundario = tipoSecundario;
        this.ps = ps;
        this.ataque = ataque;
        this.defensa = defensa;
        this.ataqueEspecial = ataqueEspecial;
        this.defensaEspecial = defensaEspecial;
        this.velocidad = velocidad;
        this.psActual = ps;
        this.movimientos = movimientos;
        this.nivel = 100;
    }

    // Metodos setters para configuracion
    public void setMovimientos(ArrayList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
    
    public void setTipoPrincipal(String tipo) {
        this.tipo = tipo;
    }

    public void setTipoSecundario(String tipo) {
        this.tipoSecundario = tipo;
    }
    
    public void setPs(int ps) {
        this.ps = ps;
        this.psActual = ps;
    }
    
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    public void setAtaqueEspecial(int ataqueEspecial) {
        this.ataqueEspecial = ataqueEspecial;
    }
    
    public void setDefensaEspecial(int defensaEspecial) {
        this.defensaEspecial = defensaEspecial;
    }
    
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    // Metodos getters
    public String getNombre() {
        return nombre;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public String getTipoSecundario() {
        return tipoSecundario;
    }
    
    public int getPs() {
        return ps;
    }
    
    public int getPsActual() {
        return psActual;
    }
    
    public int getAtaque() {
        return ataque;
    }
    
    public int getDefensa() {
        return defensa;
    }
    
    public int getAtaqueEspecial() {
        return ataqueEspecial;
    }
    
    public int getDefensaEspecial() {
        return defensaEspecial;
    }
    
    public int getVelocidad() {
        return velocidad;
    }
    
    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }
    
    public int getNivel() {
        return nivel;
    }
    
    /**
     * Reduce los PS actuales del Pokemon
     * 
     * @param cantidad Daño a recibir
     */
    public void recibirDaño(int cantidad) {
        psActual -= cantidad;
        if (psActual < 0) psActual = 0;
    }
    
    /**
     * Verifica si el Pokemon esta debilitado
     * 
     * @return true si PS <= 0, false en caso contrario
     */
    public boolean estaDebilitado() {
        return psActual <= 0;
    }
    
    /**
     * Restaura PS al Pokemon
     * 
     * @param cantidad Cantidad de PS a restaurar
     */
    public void restaurarPS(int cantidad) {
        psActual += cantidad;
        if (psActual > ps) psActual = ps;
    }

    /**
     * Verifica si el pokemon se quedo sin puntos de poder
     * 
     * @return booleano para indicar si se quedo sin puntos o no
     */    
    public boolean sinPP() {
        for (Movimiento m : movimientos) {
            if (m.esUtilizable()) return false;
        }
        return true;
    }
    
}