
package dominio;

/**
 * Clase que implementa un entrenador de IA con estrategia defensiva
 * Entrenador especializado en tacticas defensivas durante el combate
 * Hereda de AITrainer y personaliza el comportamiento defensivo
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
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
     * Ejecuta la estrategia defensiva durante el turno
     * Prioriza proteccion y resistencia sobre ataques directos
     * 
     * @param oponente Entrenador rival al que se enfrenta
     */
    @Override
    public void realizarTurno(Trainer oponente) {
        // Logica futura de estrategia defensiva
    }

    /**
     * Maneja el cambio manual de pokemon
     * Pendiente de implementacion especifica
     */
    @Override
    public void cambiarPokemonManual() {
        // Implementacion futura
    }
}