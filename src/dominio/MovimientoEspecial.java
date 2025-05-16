package dominio;

import java.util.Random;

/**
 * Clase que implementa movimientos especiales de daño
 * Representa movimientos que usan ataque/defensa especial
 * para calcular el daño infligido
 * 
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public class MovimientoEspecial extends Movimiento {
    
    /**
     * Crea un nuevo movimiento especial
     * 
     * @param nombre Nombre del movimiento
     * @param tipo Tipo elemental del movimiento
     * @param potencia Valor base de daño
     * @param precision Probabilidad de acierto (0-100)
     * @param pp Puntos de poder iniciales
     */
    public MovimientoEspecial(String nombre, String tipo, int potencia, int precision, int pp) {
        super(nombre, tipo, potencia, precision, pp);
    }

    /**
     * Ejecuta el movimiento especial contra un objetivo
     * Calcula daño considerando tipos y estadisticas especiales
     * 
     * @param atacante Pokemon que usa el movimiento
     * @param objetivo Pokemon que recibe el movimiento
     */
    @Override
    public void ejecutar(Pokemon atacante, Pokemon objetivo) {
        if (!esUtilizable()) return;

        Random rand = new Random();
        if (rand.nextInt(100) >= precision) {
            System.out.println(atacante.getNombre() + " falló el ataque!");
            usar();
            return;
        }

        double multiplicador = TablaTipos.getMultiplicador(tipo, objetivo.getTipo());
        if (objetivo.getTipoSecundario() != null) {
            multiplicador *= TablaTipos.getMultiplicador(tipo, objetivo.getTipoSecundario());
        }

        if (atacante.getTipo().equals(tipo)) {
            multiplicador *= 1.5;
        }

        int nivel = atacante.getNivel();
        int dano = (int) ((((2 * nivel / 5 + 2) * potencia * 
                atacante.getAtaqueEspecial() / objetivo.getDefensaEspecial()) / 50 + 2) * multiplicador);

        objetivo.recibirDaño(dano);
        usar();

        System.out.printf("%s usó %s y causó %d de daño (x%.1f)%n", 
            atacante.getNombre(), nombre, dano, multiplicador);
    }
}