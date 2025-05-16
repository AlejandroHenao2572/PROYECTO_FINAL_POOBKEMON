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

    /**
     * Ejecuta la logica del turno para este entrenador ofensivo
     * La implementacion concreta se definira posteriormente
     * 
     * @param oponente El rival contra el que se ejecuta el turno
     */
    @Override
    public void realizarTurno(Trainer oponente) {
        // Logica futura
    }

    /**
     * Metodo para cambiar pokemon de forma manual
     * Pendiente de implementacion en futuras versiones
     */
    @Override
    public void cambiarPokemonManual() {
        // 
    }
}
