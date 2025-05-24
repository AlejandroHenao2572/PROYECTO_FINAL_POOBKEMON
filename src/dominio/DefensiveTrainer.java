package dominio;

/**
 * Clase que implementa un entrenador de IA con estrategia defensiva
 * El entrenador defensivo prioriza movimientos que aumentan defensa o defensa especial
 * Tambien busca protegerse o debilitar el ataque del rival
 * Si su pokemon esta muy danado intentara curarse o cambiar de pokemon
 * Hereda de AITrainer y personaliza el comportamiento defensivo
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class DefensiveTrainer extends AITrainer {

    /**
     * Crea un nuevo entrenador defensivo
     * 
     * @param nombre Identificador del entrenador
     * @param color Color representativo del entrenador
     */
    public DefensiveTrainer(String nombre, String color) {
        super(nombre, color);
    }

    /**
     * Decide la accion de la IA en una batalla PvM
     * Prioriza movimientos defensivos como subir defensa o defensa especial
     * Si no hay movimientos defensivos intenta curarse o cambiar de pokemon si esta muy danado
     * Si no puede hacer ninguna de estas acciones usa el primer ataque utilizable
     * Si no puede atacar intenta cambiar de pokemon
     * Si no puede hacer nada pasa turno
     * 
     * @param batalla referencia a la batalla PvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    @Override
    public String decidirAccion(Battle batalla, Trainer oponente) {
        // 1. Busca movimientos defensivos (suben defensa o defensa especial propia, protegen, o bajan ataque o ataque especial rival)
        int movimientoDefensivo = -1;
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable() && (m instanceof MovimientoEstado)) {
                movimientoDefensivo = i;
                break;
            }
        }
        // Si encuentra un movimiento defensivo lo usa
        if (movimientoDefensivo != -1) {
            return onAttackSelected(movimientoDefensivo, oponente);
        }

        // 2. Si el pokemon activo esta muy dañado intenta curarse o cambiar
        if (pokemonActivo.getPsActual() < pokemonActivo.getPs() * 0.3) {
            // Intenta usar objeto de curacion
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item instanceof Potion || item instanceof SuperPotion || item instanceof HyperPotion) {
                    return onItemSelected(i);
                }
            }
        }

        // 3. Si no hay movimientos defensivos usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable() && (m instanceof MovimientoFisico)) {
                return onAttackSelected(i, oponente);
            }
        }

        // 4. Si no puede atacar intenta cambiar de pokemon
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            if (!p.estaDebilitado() && p != pokemonActivo) {
                return cambiarPokemon(i);
            }
        }

        // 5. Si no puede hacer nada pasa turno
        return "";
    }
}