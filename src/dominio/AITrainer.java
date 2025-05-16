package dominio;

/**
 * Esta clase abstracta representa un entrenador controlado por IA en el sistema
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public abstract class AITrainer extends Trainer {

    /**
     * Constructor de la clase AITrainer
     * Inicializa un nuevo entrenador de IA con nombre y color especificos
     * 
     * @param nombre El nombre del entrenador de IA
     * @param color El color asociado al entrenador de IA
     */
    public AITrainer(String nombre, String color) {
        super(nombre, color);
    }
}