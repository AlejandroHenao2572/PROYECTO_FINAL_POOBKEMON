
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


}