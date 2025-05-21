package dominio;

/**
 * Esta clase abstracta representa un entrenador controlado por IA en el sistema
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
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
     * Establece el listener para notificar acciones completadas
     * 
     * @param listener Objeto listener
     */
    public void setListener(TrainerListener listener) {
        this.listener = listener;
    }
    

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

    @Override
    public String onItemSelected(int itemIndex) {
        if (itemIndex < 0 || itemIndex >= items.size()) return "";
        Item item = items.get(itemIndex);
        String msg = item.usarEn(pokemonActivo); // O el método correcto para usar el ítem
        items.remove(itemIndex); // Si el ítem se consume

        return msg;
    }

    @Override
    public String cambiarPokemon(int indice) {
        if (indice < 0 || indice >= equipo.size()) return "";
        Pokemon nuevo = equipo.get(indice);
        if (nuevo == pokemonActivo || nuevo.estaDebilitado()) return "";
        Pokemon anterior = pokemonActivo;
        pokemonActivo = nuevo;
        String msg = nombre + " cambió a " + nuevo.getNombre();
        
        return msg;
    }

    public abstract String decidirAccion(BattlePvM batalla, Trainer oponente);
}
