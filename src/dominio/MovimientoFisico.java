package dominio;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa movimientos fisicos de daño
 * Representa movimientos que usan ataque/defensa fisica
 * para calcular el daño infligido al oponente
 * 
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public class MovimientoFisico extends Movimiento implements TablaTipos {
    
    /**
     * Crea un nuevo movimiento fisico
     * 
     * @param nombre Identificador del movimiento
     * @param tipo Tipo elemental del movimiento  
     * @param potencia Valor base de daño
     * @param precision Probabilidad de acierto (0-100)
     * @param pp Puntos de poder iniciales
     */
    public MovimientoFisico(String nombre, String tipo, int potencia, int precision, int pp) {
        super(nombre, tipo, potencia, precision, pp);
    }

    /**
     * Ejecuta el movimiento fisico contra un objetivo
     * Calcula daño considerando tipos y estadisticas fisicas
     * 
     * @param atacante Pokemon que ejecuta el movimiento
     * @param objetivo Pokemon que recibe el daño
     */
    @Override
    public String ejecutar(Pokemon atacante, Pokemon objetivo) {
        Logger logger = Logger.getLogger(MovimientoFisico.class.getName());
        String message;
        try {
            if (!esUtilizable()) return null;

            Random rand = new Random();
            if (rand.nextInt(100) >= precision) {
                message = atacante.getNombre() + " falló el ataque!";
                usar(); // igual consume PP
                return message;
            }

            double multiplicador = getMultiplicador(tipo, objetivo.getTipo());
            if (objetivo.getTipoSecundario() != null) {
                multiplicador *= getMultiplicador(tipo, objetivo.getTipoSecundario());
            }

            // STAB
            if (atacante.getTipo().equals(tipo)) {
                multiplicador *= 1.5;
            }

            int nivel = atacante.getNivel();
            int dano = (int) ((((2 * nivel / 5 + 2) * potencia * 
                    atacante.getAtaque() / objetivo.getDefensa()) / 50 + 2) * multiplicador);

            objetivo.recibirDaño(dano);
            usar();
            // System.out.println(multiplicador); // Elimina o comenta este print para producción
            message = String.format("%s usó %s y causó %d de daño (x%.1f)%n", atacante.getNombre(), nombre, dano, multiplicador);
        } catch (POOBkemonException e) {
            logger.log(Level.WARNING, "Error al ejecutar MovimientoFisico: " + e.getMessage(), e);
            message = "Error al ejecutar el movimiento físico: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al ejecutar MovimientoFisico: " + e.getMessage(), e);
            message = "Error inesperado al ejecutar el movimiento físico.";
        }
        return message;
    }
}