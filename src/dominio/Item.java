package dominio;

/**
 * Clase abstracta base para todos los items del juego
 * Representa un item generico que puede usarse sobre pokemones
 * Las clases concretas deben implementar el efecto especifico
 * 
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public abstract class Item {
    protected String nombre;

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
    public abstract void usarEn(Pokemon objetivo);
}