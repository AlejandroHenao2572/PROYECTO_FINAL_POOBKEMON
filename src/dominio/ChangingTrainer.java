package dominio;

/**
 * Clase que representa un entrenador de IA especializado en cambios de Pokemon
 * Implementa un entrenador con estrategia basada en cambios de Pokemon
 * Hereda de AITrainer y personaliza el comportamiento de combate
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class ChangingTrainer extends AITrainer {

    /**
     * Constructor del ChangingTrainer
     * 
     * @param nombre Nombre del entrenador
     * @param color Color identificativo del entrenador
     */
    public ChangingTrainer(String nombre, String color) {
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
     * Si hay un pokemon con mejor efectividad contra el rival lo cambia
     * Si no hay mejor cambio usa el primer ataque utilizable
     * Si no puede atacar intenta usar item de curacion
     * Si no puede hacer nada pasa turno
     * 
     * @param batalla referencia a la batalla PvM
     * @param oponente entrenador rival
     * @return mensaje de la accion realizada
     */
    @Override
    public String decidirAccion(Battle batalla, Trainer oponente) {
        // 1. Busca un pokemon con mejor efectividad contra el rival y lo cambia
        int mejorIndice = -1;
        double mejorEfectividad = -1.0;
        Pokemon rival = oponente.getPokemonActivo();

        for (int i = 0; i < equipo.size(); i++) {
            Pokemon candidato = equipo.get(i);
            if (!candidato.estaDebilitado() && candidato != pokemonActivo) {
                double efectividad = 0.0;
                // Suma la efectividad de todos los movimientos del candidato contra el rival
                for (Movimiento mov : candidato.getMovimientos()) {
                    if (mov.esUtilizable()) {
                        efectividad += mov.calcularEfectividad(rival);
                    }
                }
                if (efectividad > mejorEfectividad) {
                    mejorEfectividad = efectividad;
                    mejorIndice = i;
                }
            }
        }

        // Si encontro un pokemon con mejor efectividad lo cambia
        if (mejorIndice != -1) {
            return cambiarPokemon(mejorIndice);
        }

        // 2. Si no hay mejor cambio usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable() && (m instanceof MovimientoFisico)) {
                return onAttackSelected(i, oponente);
            }
        }

        // 3. Si no puede atacar intenta usar item de curacion
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof Potion || item instanceof SuperPotion || item instanceof HyperPotion) {
                return onItemSelected(i);
            }
        }

        // 4. Si no puede hacer nada pasa turno
        return "";
    }


}
