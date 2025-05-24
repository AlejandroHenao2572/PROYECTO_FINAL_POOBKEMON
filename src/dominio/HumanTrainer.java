package dominio;

/**
 * Clase que representa un entrenador humano en el juego
 * Implementa la logica de un jugador humano
 * Maneja turnos ataques cambios de pokemon y uso de items
 * Hereda de Trainer
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class HumanTrainer extends Trainer {

    /**
     * Crea un nuevo entrenador humano
     * 
     * @param nombre nombre del entrenador
     * @param color color del equipo
     */
    public HumanTrainer(String nombre, String color) {
        super(nombre, color);
    }

    /**
     * Ejecuta un ataque contra el oponente
     * 
     * @param moveIndex indice del movimiento
     * @param oponente entrenador rival
     * @return mensaje del resultado del ataque
     */
    @Override
    public String onAttackSelected(int moveIndex, Trainer oponente) {
        if (moveIndex < 0 || moveIndex >= pokemonActivo.getMovimientos().size()) {
            return null;
        }
        Movimiento movimiento = pokemonActivo.getMovimientos().get(moveIndex);
        String message;
        if (pokemonActivo.sinPP()) {
            Movimiento forcejeo = new Forcejeo();
            message = forcejeo.ejecutar(pokemonActivo, oponente.getPokemonActivo());
            return message;
        } else if (movimiento.esUtilizable()) {
            message = movimiento.ejecutar(pokemonActivo, oponente.getPokemonActivo());
            return message;
        }
        if (listener != null) {
            listener.onActionPerformed();
        }
        return null;
    }

    /**
     * Usa un item del inventario
     * 
     * @param itemIndex indice del item
     * @return mensaje del resultado del uso del item
     */
    @Override
    public String onItemSelected(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < items.size()) {
            Item item = items.get(itemIndex);
            String message = item.usarEn(pokemonActivo);
            items.remove(itemIndex);
            if (listener != null) {
                listener.onActionPerformed();
            }
            return message;
        }
        return null;
    }



    /**
     * Cambia el pokemon activo por otro del equipo si es valido
     * 
     * @param indicePokemon indice del pokemon en el equipo
     * @return mensaje del resultado del cambio
     */
    @Override
    public String cambiarPokemon(int indicePokemon) {
        if (indicePokemon >= 0 && indicePokemon < equipo.size()) {
            Pokemon nuevoActivo = equipo.get(indicePokemon);
            if (nuevoActivo.estaDebilitado()) {
                return "El pokemon seleccionado esta debilitado";
            }
            if (nuevoActivo == pokemonActivo) {
                return "El pokemon seleccionado ya esta en combate";
            }
            String message = nombre + " ha cambiado a " + nuevoActivo.getNombre();
            pokemonActivo = nuevoActivo;
            if (listener != null) {
                listener.onActionPerformed();
            }
            return message;
        }
        return "Seleccion de pokemon no valida";
    }

}