package dominio;

/**
 * Esta clase representa un entrenador de IA con estrategia de ataque
 * El entrenador atacante prioriza movimientos ofensivos y busca debilitar rapidamente al rival
 * Si su pokemon esta muy danado intentara curarse o cambiar de pokemon
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class AttackingTrainer extends AITrainer {

    /**
     * Constructor de la clase AttackingTrainer
     * Crea un nuevo entrenador con comportamiento ofensivo
     * 
     * @param nombre El nombre del entrenador atacante
     * @param color El color que identifica al entrenador
     */
    public AttackingTrainer(String nombre, String color) {
        super(nombre, color);
    }
    
    /**
     * Decide la accion de la IA en una batalla PvM
     * Prioriza curarse o cambiar si el pokemon esta muy dañado
     * Luego busca movimientos ofensivos y si no hay usa el primer ataque disponible
     * Si no puede atacar intenta cambiar de pokemon
     * 
     * @param batalla referencia a la batalla PvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    @Override
    public String decidirAccion(BattlePvM batalla, Trainer oponente) {
        // 1. Si el Pokemon activo esta muy danado intenta curarse o cambiar
        if (pokemonActivo.getPsActual() < pokemonActivo.getPs() * 0.3) {
            // Intenta usar objeto de curacion
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item instanceof Potion || item instanceof SuperPotion || item instanceof HyperPotion) {
                    String msg = onItemSelected(i);
                    batalla.getListener().onMoveUsed(this, msg);
                    return msg;
                }
            }
            // Si hay otro Pokemon sano cambia
            for (int i = 0; i < equipo.size(); i++) {
                Pokemon p = equipo.get(i);
                if (!p.estaDebilitado() && p != pokemonActivo) {
                    String msg = cambiarPokemon(i);
                    batalla.getListener().onPokemonChanged(this, msg);
                    return msg;
                }
            }
        }

        // 2. Busca movimientos ofensivos (fisicos o especiales)
        int mejorMovimiento = -1;
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable() && (m instanceof MovimientoEspecial || m instanceof MovimientoFisico)) {
                mejorMovimiento = i;
                break;
            }
        }
        // Si encuentra uno lo usa
        if (mejorMovimiento != -1) { 
            return onAttackSelected(mejorMovimiento, oponente);
        }

        // 3. Si no hay movimientos ofensivos usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable()) {
                return onAttackSelected(i, oponente);
            }
        }

        // 4. Si no puede atacar intenta cambiar de Pokemon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            if (!p.estaDebilitado() && p != pokemonActivo) {
                String msg = cambiarPokemon(i);
                batalla.getListener().onPokemonChanged(this, msg);
                return msg;
            }
        }

        // 5. Si no puede hacer nada pasa turno
        return "";
    }

    /**
     * Decide la accion de la IA en una batalla MvM
     * Prioriza curarse o cambiar si el pokemon esta muy dañado
     * Luego busca movimientos ofensivos y si no hay usa el primer ataque disponible
     * Si no puede atacar intenta cambiar de pokemon
     * 
     * @param batalla referencia a la batalla MvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    @Override
    public String decidirAccion(BattleMvM batalla, Trainer oponente) {
        // 1. Si el Pokemon activo esta muy danado intenta curarse o cambiar
        if (pokemonActivo.getPsActual() < pokemonActivo.getPs() * 0.3) {
            // Intenta usar objeto de curacion
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item instanceof Potion || item instanceof SuperPotion || item instanceof HyperPotion) {
                    String msg = onItemSelected(i);
                    batalla.getListener().onMoveUsed(this, msg);
                    return msg;
                }
            }
            // Si hay otro Pokemon sano cambia
            for (int i = 0; i < equipo.size(); i++) {
                Pokemon p = equipo.get(i);
                if (!p.estaDebilitado() && p != pokemonActivo) {
                    String msg = cambiarPokemon(i);
                    batalla.getListener().onPokemonChanged(this, msg);
                    return msg;
                }
            }
        }

        // 2. Busca movimientos ofensivos (fisicos o especiales)
        int mejorMovimiento = -1;
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable() && (m instanceof MovimientoEspecial || m instanceof MovimientoFisico)) {
                mejorMovimiento = i;
                break;
            }
        }
        // Si encuentra uno lo usa
        if (mejorMovimiento != -1) { 
            return onAttackSelected(mejorMovimiento, oponente);
        }

        // 3. Si no hay movimientos ofensivos usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable()) {
                String msg = onAttackSelected(i, oponente);
                batalla.getListener().onMoveUsed(this, msg);
                return msg;
            }
        }

        // 4. Si no puede atacar intenta cambiar de Pokemon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            if (!p.estaDebilitado() && p != pokemonActivo) {
                String msg = cambiarPokemon(i);
                batalla.getListener().onPokemonChanged(this, msg);
                return msg;
            }
        }

        // 5. Si no puede hacer nada pasa turno
        return "";
    }
    
}
