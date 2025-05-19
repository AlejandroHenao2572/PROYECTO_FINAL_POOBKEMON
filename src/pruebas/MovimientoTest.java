package pruebas;

import dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * Pruebas unitarias para verificar el correcto funcionamiento de los movimientos.
 */
public class MovimientoTest {

    private Pokemon atacante;
    private Pokemon objetivo;

    @BeforeEach
    public void setUp() {
        // Inicializar Pokemon de prueba
        atacante = new Pokemon("Charmander", "Fuego", null, 10, 50, 30, 20, 15, 10, null);
        objetivo = new Pokemon("Bulbasaur", "Planta", null, 10, 50, 20, 30, 10, 15, null);
        
    }

    @Test
    public void testMovimientoFisicoHaceDanoYReducePP() {
        Movimiento tackle = new MovimientoFisico("Tackle", "Normal", 40, 100, 10);
        int hpInicial = objetivo.getPs();
        int ppInicial = tackle.getPP();

        tackle.ejecutar(atacante, objetivo);
        assertTrue(objetivo.getPsActual() < hpInicial, "El objetivo debe recibir daño");
        assertEquals(ppInicial - 1, tackle.getPP(), "El movimiento debe consumir 1 PP");
    }

    @Test
    public void testMovimientoEspecialHaceDano() {
        Movimiento ascuas = new MovimientoEspecial("Ascuas", "Fuego", 40, 100, 10);
        int hpInicial = objetivo.getPs();

        ascuas.ejecutar(atacante, objetivo);

        assertTrue(objetivo.getPsActual() < hpInicial, "El objetivo debe recibir daño especial");
    }

    @Test
    public void testPrecisionFallaCuandoEsBaja() {
        Movimiento fallo = new MovimientoFisico("FalloSeguro", "Normal", 50, 0, 5);
        int hpInicial = objetivo.getPs();
        fallo.ejecutar(atacante, objetivo);

        assertEquals(hpInicial, objetivo.getPsActual(), "No debe causar daño con 0% de precision");
    }

    @Test
    public void testPPSeAgota() {
        Movimiento ataque = new MovimientoFisico("Golpe", "Normal", 40, 100, 1);
        assertTrue(ataque.esUtilizable());
        ataque.ejecutar(atacante, objetivo);
        assertFalse(ataque.esUtilizable(), "Debe dejar de ser utilizable al agotarse los PP");
    }

    @Test
    public void testForcejeoHaceDanoYAutodaño() {
        Movimiento forcejeo = new Forcejeo();
        int hpInicialAtacante = atacante.getPs();
        int hpInicialObjetivo = objetivo.getPs();

        forcejeo.ejecutar(atacante, objetivo);

        assertTrue(objetivo.getPsActual() < hpInicialObjetivo, "El objetivo debe recibir daño de Forcejeo");
        assertTrue(atacante.getPsActual() < hpInicialAtacante, "El atacante debe recibir autodaño");
        assertEquals(Integer.MAX_VALUE, forcejeo.getPP(), "Forcejeo debe tener PP infinitos");
    }

    @Test
    public void testActivacionDeForcejeoCuandoNoHayPP() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        Movimiento m1 = new MovimientoFisico("Agotado1", "Normal", 10, 100, 0);
        Movimiento m2 = new MovimientoEspecial("Agotado2", "Fuego", 10, 100, 0);
        movimientos.add(m1);
        movimientos.add(m2);

        assertDoesNotThrow(() -> atacante.setMovimientos(movimientos));

        assertTrue(atacante.sinPP(), "El Pokemon debe activar Forcejeo si no tiene PP en ningun movimiento");

        Movimiento forcejeo = new Forcejeo();
        assertDoesNotThrow(() -> forcejeo.ejecutar(atacante, objetivo));

        assertTrue(objetivo.getPsActual() < 100);
    }
}