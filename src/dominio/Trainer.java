package dominio;

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
public abstract class Trainer {
    protected String nombre;
    protected String color;
    protected ArrayList<Pokemon> equipo;
    protected ArrayList<Item> items;
    protected Pokemon pokemonActivo;

    /**
     * Constructor del entrenador
     *
     * @param nombre Nombre del entrenador
     * @param color Color que representa al entrenador
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
     * @param item Item a agregar
     */
    public void agregarItem(Item item) {
        items.add(item);
    }

    /**
     * Cambia el Pokemon activo por otro en el equipo si no esta debilitado
     *
     * @param indice Indice del Pokemon en el equipo
     */
    public void cambiarPokemon(int indice) {
        if (indice >= 0 && indice < equipo.size()) {
            if (!equipo.get(indice).estaDebilitado()) {
                pokemonActivo = equipo.get(indice);
            } else {
                System.out.println("No puedes cambiar a un Pokemon debilitado");
            }
        }
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
     * @return Nombre del entrenador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el color del entrenador
     *
     * @return Color del entrenador
     */
    public String getColor() {
        return color;
    }

    /**
     * Obtiene la lista de Pokemon del equipo
     *
     * @return Lista del equipo de Pokemon
     */
    public ArrayList<Pokemon> getEquipo() {
        return equipo;
    }
    
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
     * Obtiene la lista de items del entrenador
     *
     * @return Lista de items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Metodo abstracto para cambiar el Pokemon de forma manual
     * Debe ser implementado por las clases hijas
     */
    public abstract void cambiarPokemonManual();

    /**
     * Metodo abstracto que define la jugada del entrenador durante el turno
     * Debe ser implementado por las clases hijas
     *
     * @param oponente Entrenador oponente
     */
    public abstract void realizarTurno(Trainer oponente);
}