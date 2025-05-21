package dominio;

/**
 * Esta clase representa un entrenador de IA con estrategia de ataque
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
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
    
    @Override
    public String decidirAccion(BattlePvM batalla, Trainer oponente) {
        // 1. Si el Pokémon activo está muy dañado, intenta curarse o cambiar
        if (pokemonActivo.getPsActual() < pokemonActivo.getPs() * 0.3) {
            // Intenta usar objeto de curación
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item instanceof Potion || item instanceof SuperPotion || item instanceof HyperPotion) {
                    String msg = onItemSelected(i);
                    batalla.getListener().onMoveUsed(this, msg);
                    return msg;
                }
            }
            // Si hay otro Pokémon sano, cambia
            for (int i = 0; i < equipo.size(); i++) {
                Pokemon p = equipo.get(i);
                if (!p.estaDebilitado() && p != pokemonActivo) {
                    String msg = cambiarPokemon(i);
                    batalla.getListener().onPokemonChanged(this, msg);
                    return msg;
                }
            }
        }

        // 2. Busca movimientos ofensivos (que suban ataque/ataque especial o bajen defensa/defensa especial del rival)
        int mejorMovimiento = -1;
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable() && (m instanceof MovimientoEspecial || m instanceof MovimientoFisico)) {
                mejorMovimiento = i;
                break;
            }
        }
        // Si encuentra uno, lo usa
        if (mejorMovimiento != -1) { 
            return onAttackSelected(mejorMovimiento, oponente);
        }

        // Si no hay movimientos ofensivos, usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable()) {
                return onAttackSelected(i, oponente);
            }
        }

        // Si no puede atacar, intenta cambiar de Pokémon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            if (!p.estaDebilitado() && p != pokemonActivo) {
                String msg = cambiarPokemon(i);
                batalla.getListener().onPokemonChanged(this, msg);
                return msg;
            }
        }

        // Si no puede hacer nada, pasa turno
        return "";
    }

    @Override
    public String decidirAccion(BattleMvM batalla, Trainer oponente) {
        // 1. Si el Pokémon activo está muy dañado, intenta curarse o cambiar
        if (pokemonActivo.getPsActual() < pokemonActivo.getPs() * 0.3) {
            // Intenta usar objeto de curación
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item instanceof Potion || item instanceof SuperPotion || item instanceof HyperPotion) {
                    String msg = onItemSelected(i);
                    batalla.getListener().onMoveUsed(this, msg);
                    return msg;
                }
            }
            // Si hay otro Pokémon sano, cambia
            for (int i = 0; i < equipo.size(); i++) {
                Pokemon p = equipo.get(i);
                if (!p.estaDebilitado() && p != pokemonActivo) {
                    String msg = cambiarPokemon(i);
                    batalla.getListener().onPokemonChanged(this, msg);
                    return msg;
                }
            }
        }

        // 2. Busca movimientos ofensivos (que suban ataque/ataque especial o bajen defensa/defensa especial del rival)
        int mejorMovimiento = -1;
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable() && (m instanceof MovimientoEspecial || m instanceof MovimientoFisico)) {
                mejorMovimiento = i;
                break;
            }
        }
        // Si encuentra uno, lo usa
        if (mejorMovimiento != -1) { 
            return onAttackSelected(mejorMovimiento, oponente);
        }

        // Si no hay movimientos ofensivos, usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable()) {
                String msg = onAttackSelected(i, oponente);
                batalla.getListener().onMoveUsed(this, msg);
                return msg;
            }
        }

        // Si no puede atacar, intenta cambiar de Pokémon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            if (!p.estaDebilitado() && p != pokemonActivo) {
                String msg = cambiarPokemon(i);
                batalla.getListener().onPokemonChanged(this, msg);
                return msg;
            }
        }

        // Si no puede hacer nada, pasa turno
        return "";
    }
    
}
