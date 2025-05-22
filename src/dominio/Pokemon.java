package dominio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que representa un Pokemon en el sistema de batalla
 *
 * Modela las caracteristicas y comportamiento de un Pokemon
 * Incluye estadisticas tipos movimientos y metodos de combate
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class Pokemon implements Serializable {
    private static final long serialVersionUID = 1L;
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

    /**
     * Establece los movimientos del Pokemon
     * 
     * @param movimientos Lista de movimientos
     * @throws POOBkemonException Si la lista es nula o vacia
     */
    public void setMovimientos(ArrayList<Movimiento> movimientos) throws POOBkemonException {
        if (movimientos == null || movimientos.isEmpty()) {
            throw new POOBkemonException(POOBkemonException.ERROR_MOVIMIENTOS_NO_VALIDOS);
        }
        this.movimientos = movimientos;
    }

    /**
     * Establece el tipo principal del Pokemon
     * 
     * @param tipo Tipo principal
     */
    public void setTipoPrincipal(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Establece el tipo secundario del Pokemon
     * 
     * @param tipo Tipo secundario
     */
    public void setTipoSecundario(String tipo) {
        this.tipoSecundario = tipo;
    }
    
    /**
     * Establece los puntos de salud maximos del pokemon y actualiza los puntos actuales
     * @param ps puntos de salud maximos
     */
    public void setPs(int ps) {
        this.ps = ps;
        this.psActual = ps;
    }

    /**
     * Establece el valor de ataque del pokemon
     * @param ataque nuevo valor de ataque
     */
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    /**
     * Establece el valor de defensa del pokemon
     * @param defensa nuevo valor de defensa
     */
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    /**
     * Establece el valor de ataque especial del pokemon
     * @param ataqueEspecial nuevo valor de ataque especial
     */
    public void setAtaqueEspecial(int ataqueEspecial) {
        this.ataqueEspecial = ataqueEspecial;
    }

    /**
     * Establece el valor de defensa especial del pokemon
     * @param defensaEspecial nuevo valor de defensa especial
     */
    public void setDefensaEspecial(int defensaEspecial) {
        this.defensaEspecial = defensaEspecial;
    }

    /**
     * Establece el valor de velocidad del pokemon
     * @param velocidad nuevo valor de velocidad
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Establece el nivel del pokemon
     * @param nivel nuevo nivel
     * @throws POOBkemonException si el nivel no es valido
     */
    public void setNivel(int nivel) throws POOBkemonException {
        if (nivel <= 0 || nivel > 100) {
            throw new POOBkemonException(POOBkemonException.ERROR_NIVEL_NO_VALIDO);
        }
        this.nivel = nivel;
    }

    /**
     * Reduce los PS actuales del Pokemon
     * 
     * @param cantidad Dano a recibir
     * @throws POOBkemonException Si la cantidad es invalida
     */
    /**
     * Devuelve el nombre del Pokemon
     * 
     * @return nombre del Pokemon
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Devuelve el tipo principal del Pokemon
     * 
     * @return tipo principal
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * Devuelve el tipo secundario del Pokemon
     * 
     * @return tipo secundario o null si no tiene
     */
    public String getTipoSecundario() {
        return tipoSecundario;
    }
    
    /**
     * Devuelve los puntos de salud maximos del Pokemon
     * 
     * @return puntos de salud maximos
     */
    public int getPs() {
        return ps;
    }
    
    /**
     * Devuelve los puntos de salud actuales del Pokemon
     * 
     * @return puntos de salud actuales
     */
    public int getPsActual() {
        return psActual;
    }
    
    /**
     * Devuelve el valor de ataque del Pokemon
     * 
     * @return ataque
     */
    public int getAtaque() {
        return ataque;
    }
    
    /**
     * Devuelve el valor de defensa del Pokemon
     * 
     * @return defensa
     */
    public int getDefensa() {
        return defensa;
    }
    
    /**
     * Devuelve el valor de ataque especial del Pokemon
     * 
     * @return ataque especial
     */
    public int getAtaqueEspecial() {
        return ataqueEspecial;
    }
    
    /**
     * Devuelve el valor de defensa especial del Pokemon
     * 
     * @return defensa especial
     */
    public int getDefensaEspecial() {
        return defensaEspecial;
    }
    
    /**
     * Devuelve el valor de velocidad del Pokemon
     * 
     * @return velocidad
     */
    public int getVelocidad() {
        return velocidad;
    }
    
    /**
     * Devuelve la lista de movimientos del Pokemon
     * 
     * @return lista de movimientos
     */
    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }
    
    /**
     * Devuelve el nivel del Pokemon
     * 
     * @return nivel
     */
    public int getNivel() {
        return nivel;
    }
    
    /**
     * Reduce los PS actuales del Pokemon
     * 
     * @param cantidad Daño a recibir
     */
    public void recibirDaño(int cantidad) throws POOBkemonException {
        if (cantidad <= 0) {
            throw new POOBkemonException(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA);
        }
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
    public void restaurarPS(int cantidad) throws POOBkemonException {
        if (cantidad <= 0) {
            throw new POOBkemonException(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA);
        }
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

    /**
     * Aumenta una estadistica especifica del Pokemon
     * 
     * @param estadistica Nombre de la estadistica a aumentar
     * @param cantidad Valor a aumentar
     * @return La cantidad que se aumento
     * @throws POOBkemonException Si la cantidad es invalida o la estadistica no existe
     */
    public int aumentarEstadisticas(String estadistica, int cantidad) throws POOBkemonException {
        if (cantidad <= 0) {
            throw new POOBkemonException(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA);
        }
        switch (estadistica.toLowerCase()) {
            case "ps":
                this.ps += cantidad;
                this.psActual += cantidad;
                return cantidad;
            case "ataque":
                this.ataque += cantidad;
                return cantidad;
            case "defensa":
                this.defensa += cantidad;
                return cantidad;
            case "ataqueespecial":
                this.ataqueEspecial += cantidad;
                return cantidad;
            case "defensaespecial":
                this.defensaEspecial += cantidad;
                return cantidad;
            case "velocidad":
                this.velocidad += cantidad;
                return cantidad;
            default:
                throw new POOBkemonException(String.format(POOBkemonException.ERROR_ESTADISTICA_NO_EXISTE, estadistica));
        }
    }
    
}