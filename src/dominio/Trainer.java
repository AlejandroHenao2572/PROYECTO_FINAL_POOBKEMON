package dominio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase abstracta que representa a un entrenador en el juego
 * Cada entrenador tiene un nombre un color un equipo de Pokemon
 * una lista de items y un Pokemon activo
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public abstract class Trainer implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nombre;
    protected String color;
    protected ArrayList<Pokemon> equipo;
    protected ArrayList<Item> items;
    protected Pokemon pokemonActivo;

    /**
     * Constructor del entrenador
     *
     * @param nombre nombre del entrenador
     * @param color color que representa al entrenador
     */
    public Trainer(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.equipo = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    /**
     * Agrega un Pokemon al equipo del entrenador
     * Si no hay un Pokemon activo el nuevo se asigna como activo
     *
     * @param p Pokemon a agregar
     */
    public void agregarPokemon(Pokemon p) {
        equipo.add(p);
        if (pokemonActivo == null) {
            pokemonActivo = p;
        }
    }

    /**
     * Agrega un item a la lista de items del entrenador
     *
     * @param item item a agregar
     */
    public void agregarItem(Item item) {
        items.add(item);
    }

    /**
     * Cambia el Pokemon activo por otro en el equipo si no esta debilitado
     *
     * @param indice indice del Pokemon en el equipo
     * @return mensaje del resultado del cambio
     */
    public String cambiarPokemon(int indice) {
        if (indice >= 0 && indice < equipo.size()) {
            Pokemon nuevoPokemon = equipo.get(indice);
            if (!nuevoPokemon.estaDebilitado()) {
                if (nuevoPokemon != pokemonActivo) {
                    pokemonActivo = nuevoPokemon;
                    return nombre + " ha cambiado a " + pokemonActivo.getNombre();
                }
                return nombre + " ya tiene a " + pokemonActivo.getNombre() + " en combate";
            }
            return "No puedes cambiar a un Pokemon debilitado";
        }
        return "Indice de Pokemon no valido";
    }

    /**
     * Verifica si todos los Pokemon del equipo estan debilitados
     *
     * @return true si todos los Pokemon estan debilitados false en caso contrario
     */
    public boolean estaDerrotado() {
        for (Pokemon p : equipo) {
            if (!p.estaDebilitado()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtiene el Pokemon actualmente activo del entrenador
     *
     * @return Pokemon activo
     */
    public Pokemon getPokemonActivo() {
        return pokemonActivo;
    }

    /**
     * Obtiene el nombre del entrenador
     *
     * @return nombre del entrenador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el color del entrenador
     *
     * @return color del entrenador
     */
    public String getColor() {
        return color;
    }

    /**
     * Obtiene la lista de Pokemon del equipo
     *
     * @return lista del equipo de Pokemon
     */
    public ArrayList<Pokemon> getEquipo() {
        return equipo;
    }

    /**
     * Obtiene la lista de Pokemon debilitados del equipo
     *
     * @return lista de Pokemon debilitados
     */
    public ArrayList<Pokemon> getPokemonsDebilitados() {
        ArrayList<Pokemon> debiles = new ArrayList<>();
        for(Pokemon p : equipo){
            if(p.estaDebilitado()){
                debiles.add(p);
            }
        }
        return debiles;
    }

    /**
     * Obtiene la lista de Pokemon activos del equipo
     *
     * @return lista de Pokemon no debilitados
     */
    public ArrayList<Pokemon> getPokemonsActivos() {
        ArrayList<Pokemon> vivos = new ArrayList<>();
        for(Pokemon p : equipo){
            if(!p.estaDebilitado()){
                vivos.add(p);
            }
        }
        return vivos;
    }

    /**
     * Obtiene la lista de items del entrenador
     *
     * @return lista de items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Metodo abstracto para ejecutar un ataque seleccionado
     *
     * @param moveIndex indice del movimiento
     * @param oponente entrenador oponente
     * @return mensaje del resultado del ataque
     */
    public abstract String onAttackSelected(int moveIndex, Trainer oponente);

    /**
     * Metodo abstracto para usar un item seleccionado
     *
     * @param itemIndex indice del item
     * @return mensaje del resultado del uso del item
     */
    public abstract String onItemSelected(int itemIndex);
}