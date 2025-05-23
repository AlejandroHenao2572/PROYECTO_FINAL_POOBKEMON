package dominio;

/**
 * Esta clase abstracta representa un entrenador controlado por IA en el sistema
 * Un AITrainer puede tomar decisiones automaticas durante la batalla
 * Hereda de Trainer y define metodos para ataques uso de items y cambio de pokemon
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public abstract class AITrainer extends Trainer {
    private TrainerListener listener;

    /**
     * Constructor de la clase AITrainer
     * Inicializa un nuevo entrenador de IA con nombre y color especificos
     * 
     * @param nombre El nombre del entrenador de IA
     * @param color El color asociado al entrenador de IA
     */
    public AITrainer(String nombre, String color) {
        super(nombre, color);
    }

    /**
     * Ejecuta un ataque seleccionado por la IA contra el oponente
     * 
     * @param moveIndex indice del movimiento a usar
     * @param oponente entrenador rival
     * @return mensaje del resultado del ataque
     */
    @Override
    public String onAttackSelected(int moveIndex, Trainer oponente) {
        if (moveIndex < 0 || moveIndex >= pokemonActivo.getMovimientos().size()) {
            return "";
        }
        Movimiento movimiento = pokemonActivo.getMovimientos().get(moveIndex);
        if (pokemonActivo.sinPP()) {
            Movimiento forcejeo = new Forcejeo();
            return forcejeo.ejecutar(pokemonActivo, oponente.getPokemonActivo());
        } else if (movimiento.esUtilizable()) {
            return movimiento.ejecutar(pokemonActivo, oponente.getPokemonActivo());
        }
        return "";
    }

    /**
     * Usa un item seleccionado por la IA en el pokemon activo
     * 
     * @param itemIndex indice del item a usar
     * @return mensaje del resultado del uso del item
     */
    @Override
    public String onItemSelected(int itemIndex) {
        if (itemIndex < 0 || itemIndex >= items.size()) return "";
        Item item = items.get(itemIndex);
        String msg = item.usarEn(pokemonActivo); 
        items.remove(itemIndex);
        return msg;
    }

    /**
     * Cambia el pokemon activo por otro del equipo si es valido
     * 
     * @param indice indice del pokemon en el equipo
     * @return mensaje del resultado del cambio
     */
    @Override
    public String cambiarPokemon(int indice) {
        if (indice < 0 || indice >= equipo.size()) return "";
        Pokemon nuevo = equipo.get(indice);
        if (nuevo == pokemonActivo || nuevo.estaDebilitado()) return "";
        Pokemon anterior = pokemonActivo;
        pokemonActivo = nuevo;
        String msg = nombre + " cambio a " + nuevo.getNombre();
        return msg;
    }

    /**
     * Metodo abstracto para que la IA decida su accion en una batalla PvM
     * 
     * @param batalla referencia a la batalla PvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    public abstract String decidirAccion(BattlePvM batalla, Trainer oponente);

    /**
     * Metodo abstracto para que la IA decida su accion en una batalla MvM
     * 
     * @param batalla referencia a la batalla MvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    public abstract String decidirAccion(BattleMvM batalla, Trainer oponente);


    /**
     * Establece el listener para notificar acciones completadas
     * 
     * @param listener Objeto listener
     */
    public void setListener(TrainerListener listener) {
        this.listener = listener;
    }

}
