package dominio;

import java.io.Serializable;

/**
 * Clase abstracta base para todos los items del juego
 * Representa un item generico que puede usarse sobre pokemones
 * Las clases concretas deben implementar el efecto especifico
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public abstract class Item implements Serializable{
    protected String nombre;
    private static final long serialVersionUID = 1L;

    /**
     * Crea un nuevo item con nombre especifico
     * 
     * @param nombre Identificador del item
     */
    public Item(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre del item
     * 
     * @return String con el nombre del item
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo abstracto para usar el item en un pokemon
     * 
     * @param objetivo Pokemon sobre el que se usa el item
     */
    public abstract String usarEn(Pokemon objetivo);
}