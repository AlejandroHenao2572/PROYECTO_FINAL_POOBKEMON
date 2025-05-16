package dominio;


/**
 * Clase que representa un entrenador humano en el juego
 * Implementa la logica de un jugador humano 
 * Maneja turnos, ataques, cambios de pokemon y uso de items
 * Autores David Patacon y Daniel Hueso
 * Version 2.0
 */
public class HumanTrainer extends Trainer {
    private TrainerListener listener;

    /**
     * Crea un nuevo entrenador humano
     * 
     * @param nombre Nombre del entrenador
     * @param color Color del equipo
     */
    public HumanTrainer(String nombre, String color) {
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

    /**
     * Ejecuta un ataque contra el oponente
     * 
     * @param moveIndex Indice del movimiento
     * @param oponente Entrenador rival
     */
    public void onAttackSelected(int moveIndex, Trainer oponente) {
        if (moveIndex < 0 || moveIndex >= pokemonActivo.getMovimientos().size()) {
            return;
        }

        Movimiento movimiento = pokemonActivo.getMovimientos().get(moveIndex);
        
        if (pokemonActivo.sinPP()) {
              Movimiento forcejeo = new Forcejeo();
            forcejeo.ejecutar(pokemonActivo, oponente.getPokemonActivo());
        } else if (movimiento.esUtilizable()) {
            movimiento.ejecutar(pokemonActivo, oponente.getPokemonActivo());
        }
        
        if (listener != null) {
            listener.onActionPerformed();
        }
    }

    /**
     * Cambia el pokemon activo
     * 
     * @param pokemonIndex Indice del pokemon
     */
    public void onSwitchSelected(int pokemonIndex) {
        if (pokemonIndex >= 0 && pokemonIndex < equipo.size()) {
            if (!equipo.get(pokemonIndex).estaDebilitado()) {
                cambiarPokemon(pokemonIndex);
                if (listener != null) {
                    listener.onActionPerformed();
                }
            }
        }
    }

    /**
     * Usa un item del inventario
     * 
     * @param itemIndex Indice del item
     */
    public void onItemSelected(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < items.size()) {
            Item item = items.get(itemIndex);
            item.usarEn(pokemonActivo);
            items.remove(itemIndex);
            if (listener != null) {
                listener.onActionPerformed();
            }
        }
    }

    
    public  void cambiarPokemonManual(){};


    public  void realizarTurno(Trainer oponente){};
}