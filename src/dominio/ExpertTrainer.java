package dominio;

/**
 * Clase que implementa un entrenador de IA con inteligencia avanzada
 * Entrenador experto que utiliza estrategias complejas de combate
 * Hereda de AITrainer e implementa toma de decisiones avanzada
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class ExpertTrainer extends AITrainer {

    /**
     * Constructor del entrenador experto
     * 
     * @param nombre Nombre identificativo del entrenador
     * @param color Color representativo del equipo
     */
    public ExpertTrainer(String nombre, String color) {
        super(nombre, color);
    }

    /**
     * Ejecuta un ataque seleccionado contra el oponente
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
     * Decide la accion de la IA en una batalla PvM
     * Si el pokemon activo esta muy danado intenta curarse o cambiar
     * Luego prioriza movimientos ofensivos o que suban ataque o bajen defensa del rival
     * Si no hay movimientos ofensivos usa el primer ataque utilizable
     * Si no puede atacar intenta cambiar de pokemon
     * Si no puede hacer nada pasa turno
     * 
     * @param batalla referencia a la batalla PvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    @Override
    public String decidirAccion(BattlePvM batalla, Trainer oponente) {
        // 1. Si el pokemon activo esta muy danado intenta curarse o cambiar
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
            // Si hay otro pokemon sano cambia
            for (int i = 0; i < equipo.size(); i++) {
                Pokemon p = equipo.get(i);
                if (!p.estaDebilitado() && p != pokemonActivo) {
                    String msg = cambiarPokemon(i);
                    batalla.getListener().onPokemonChanged(this, msg);
                    return msg;
                }
            }
        }

        // 2. Busca movimientos ofensivos (que suban ataque o bajen defensa del rival)
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

        // Si no hay movimientos ofensivos usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable()) {
                return onAttackSelected(i, oponente);
            }
        }

        // Si no puede atacar intenta cambiar de pokemon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            if (!p.estaDebilitado() && p != pokemonActivo) {
                String msg = cambiarPokemon(i);
                batalla.getListener().onPokemonChanged(this, msg);
                return msg;
            }
        }

        // Si no puede hacer nada pasa turno
        return "";
    }

    /**
     * Decide la accion de la IA en una batalla MvM
     * Si el pokemon activo esta muy danado intenta curarse o cambiar
     * Luego prioriza movimientos ofensivos o que suban ataque o bajen defensa del rival
     * Si no hay movimientos ofensivos usa el primer ataque utilizable
     * Si no puede atacar intenta cambiar de pokemon
     * Si no puede hacer nada pasa turno
     * 
     * @param batalla referencia a la batalla MvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    @Override
    public String decidirAccion(BattleMvM batalla, Trainer oponente) {
        // 1. Si el pokemon activo esta muy danado intenta curarse o cambiar
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
            // Si hay otro pokemon sano cambia
            for (int i = 0; i < equipo.size(); i++) {
                Pokemon p = equipo.get(i);
                if (!p.estaDebilitado() && p != pokemonActivo) {
                    String msg = cambiarPokemon(i);
                    batalla.getListener().onPokemonChanged(this, msg);
                    return msg;
                }
            }
        }

        // 2. Busca movimientos ofensivos (que suban ataque o bajen defensa del rival)
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

        // Si no hay movimientos ofensivos usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable()) {
                return onAttackSelected(i, oponente);
            }
        }

        // Si no puede atacar intenta cambiar de pokemon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            if (!p.estaDebilitado() && p != pokemonActivo) {
                String msg = cambiarPokemon(i);
                batalla.getListener().onPokemonChanged(this, msg);
                return msg;
            }
        }

        // Si no puede hacer nada pasa turno
        return "";
    }
}