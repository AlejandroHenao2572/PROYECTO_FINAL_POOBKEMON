package pruebas;

import dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * Pruebas unitarias para verificar el correcto funcionamiento de los movimientos.
 * Se prueban los movimientos fisicos, especiales y el movimiento Forcejeo,
 * asi como el manejo de PP, precision y efectos de autodaño.
 */
public class MovimientoTest {

    private Pokemon atacante;
    private Pokemon objetivo;

    /**
     * Prepara el escenario antes de cada prueba.
     * Se crean dos Pokemon de prueba, uno atacante y uno objetivo, con estadisticas basicas.
     */
    @BeforeEach
    public void setUp() {
        // Inicializar Pokemon de prueba
        atacante = new Pokemon("Charmander", "Fuego", null, 10, 50, 30, 20, 15, 10, null);
        objetivo = new Pokemon("Bulbasaur", "Planta", null, 10, 50, 20, 30, 10, 15, null);
    }

    /**
     * Prueba que un movimiento fisico reduce los PS del objetivo y consume 1 PP.
     * Se espera que el objetivo reciba dano y que el movimiento pierda un PP.
     */
    @Test
    public void testMovimientoFisicoHaceDanoYReducePP() {
        Movimiento tackle = new MovimientoFisico("Tackle", "Normal", 40, 100, 10);
        int hpInicial = objetivo.getPs();
        int ppInicial = tackle.getPP();

        tackle.ejecutar(atacante, objetivo);
        assertTrue(objetivo.getPsActual() < hpInicial, "El objetivo debe recibir dano");
        assertEquals(ppInicial - 1, tackle.getPP(), "El movimiento debe consumir 1 PP");
    }

    /**
     * Prueba que un movimiento especial reduce los PS del objetivo.
     * Se espera que el objetivo reciba dano especial.
     */
    @Test
    public void testMovimientoEspecialHaceDano() {
        Movimiento ascuas = new MovimientoEspecial("Ascuas", "Fuego", 40, 100, 10);
        int hpInicial = objetivo.getPs();

        ascuas.ejecutar(atacante, objetivo);

        assertTrue(objetivo.getPsActual() < hpInicial, "El objetivo debe recibir dano especial");
    }

    /**
     * Prueba que un movimiento con precision 0 nunca acierta y no causa dano.
     * Se espera que los PS del objetivo no cambien.
     */
    @Test
    public void testPrecisionFallaCuandoEsBaja() {
        Movimiento fallo = new MovimientoFisico("FalloSeguro", "Normal", 50, 0, 5);
        int hpInicial = objetivo.getPs();
        fallo.ejecutar(atacante, objetivo);

        assertEquals(hpInicial, objetivo.getPsActual(), "No debe causar dano con 0% de precision");
    }

    /**
     * Prueba que un movimiento deja de ser utilizable cuando se agotan sus PP.
     * Se espera que tras usar el movimiento una vez, ya no sea utilizable.
     */
    @Test
    public void testPPSeAgota() {
        Movimiento ataque = new MovimientoFisico("Golpe", "Normal", 40, 100, 1);
        assertTrue(ataque.esUtilizable());
        ataque.ejecutar(atacante, objetivo);
        assertFalse(ataque.esUtilizable(), "Debe dejar de ser utilizable al agotarse los PP");
    }

    /**
     * Prueba que el movimiento Forcejeo causa dano al objetivo y autodaño al atacante,
     * y que tiene PP infinitos.
     */
    @Test
    public void testForcejeoHaceDanoYAutodaño() {
        Movimiento forcejeo = new Forcejeo();
        int hpInicialAtacante = atacante.getPs();
        int hpInicialObjetivo = objetivo.getPs();

        forcejeo.ejecutar(atacante, objetivo);

        assertTrue(objetivo.getPsActual() < hpInicialObjetivo, "El objetivo debe recibir dano de Forcejeo");
        assertTrue(atacante.getPsActual() < hpInicialAtacante, "El atacante debe recibir autodaño");
        assertEquals(Integer.MAX_VALUE, forcejeo.getPP(), "Forcejeo debe tener PP infinitos");
    }

    /**
     * Prueba que Forcejeo se activa correctamente cuando el Pokemon no tiene PP en ningun movimiento.
     * Se verifica que el movimiento puede ejecutarse y causa dano.
     */
    @Test
    public void testActivacionDeForcejeoCuandoNoHayPP() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        Movimiento m1 = new MovimientoFisico("Agotado1", "Normal", 10, 100, 0);
        Movimiento m2 = new MovimientoEspecial("Agotado2", "Fuego", 10, 100, 0);
        movimientos.add(m1);
        movimientos.add(m2);

        // Se asignan movimientos sin PP al atacante
        assertDoesNotThrow(() -> atacante.setMovimientos(movimientos));

        // Se verifica que el atacante no tiene PP en ningun movimiento
        assertTrue(atacante.sinPP(), "El Pokemon debe activar Forcejeo si no tiene PP en ningun movimiento");

        Movimiento forcejeo = new Forcejeo();
        // Se verifica que Forcejeo puede ejecutarse sin lanzar excepcion
        assertDoesNotThrow(() -> forcejeo.ejecutar(atacante, objetivo));

        // Se verifica que el objetivo recibe dano
        assertTrue(objetivo.getPsActual() < 100);
    }

    /**
     * Prueba que un movimiento especial reduce los PS del objetivo y consume 1 PP.
     */
    @Test
    public void deberiaHacerDanoYReducirPPConMovimientoEspecial() {
        MovimientoEspecial rayo = new MovimientoEspecial("Rayo", "Electrico", 90, 100, 5);
        int hpInicial = objetivo.getPs();
        int ppInicial = rayo.getPP();

        rayo.ejecutar(atacante, objetivo);

        assertTrue(objetivo.getPsActual() < hpInicial, "El objetivo debe recibir dano especial");
        assertEquals(ppInicial - 1, rayo.getPP(), "El movimiento especial debe consumir 1 PP");
    }

    /**
     * Prueba que un movimiento especial no puede usarse si no tiene PP.
     */
    @Test
    public void noDeberiaPermitirUsarMovimientoEspecialSinPP() {
        MovimientoEspecial rayo = new MovimientoEspecial("Rayo", "Electrico", 90, 100, 1);
        rayo.ejecutar(atacante, objetivo); // Usa el unico PP
        assertFalse(rayo.esUtilizable(), "El movimiento especial no debe ser utilizable sin PP");
        int hpAntes = objetivo.getPsActual();
        rayo.ejecutar(atacante, objetivo); // No deberia hacer nada
        assertEquals(hpAntes, objetivo.getPsActual(), "No debe causar dano si no tiene PP");
    }

    /**
     * Prueba que un movimiento especial con precision baja puede fallar y no causa daño.
     */
    @Test
    public void noDeberiaHacerDanoSiFallaPrecisionMovimientoEspecial() {
        MovimientoEspecial especialFallo = new MovimientoEspecial("Fallo", "Psiquico", 100, 0, 5);
        int hpInicial = objetivo.getPsActual();
        especialFallo.ejecutar(atacante, objetivo);
        assertEquals(hpInicial, objetivo.getPsActual(), "No debe causar dano si la precision es 0");
    }
}