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


}