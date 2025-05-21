package dominio;

/**
 * Clase que representa un entrenador de IA especializado en cambios de Pokemon
 * Implementa un entrenador con estrategia basada en cambios de Pokemon
 * Hereda de AITrainer y personaliza el comportamiento de combate
 * 
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
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
    public String decidirAccion(BattlePvM batalla, Trainer oponente) {
        // 1. Si hay un Pokémon con mejor efectividad contra el rival, cámbialo
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

        // Si encontró un Pokémon con mejor efectividad, lo cambia
        if (mejorIndice != -1) {
            return cambiarPokemon(mejorIndice);
        }

        // 2. Si no hay mejor cambio, usa el primer ataque utilizable
        for (int i = 0; i < pokemonActivo.getMovimientos().size(); i++) {
            Movimiento m = pokemonActivo.getMovimientos().get(i);
            if (m.esUtilizable()) {
                return onAttackSelected(i, oponente);
            }
        }

        // 3. Si no puede atacar, intenta usar ítem de curación
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof Potion || item instanceof SuperPotion || item instanceof HyperPotion) {
                return onItemSelected(i);
            }
        }

        // 4. Si no puede hacer nada, pasa turno
        return "";
    }
}
