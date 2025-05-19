package pruebas;

import dominio.Pokemon;
import dominio.Movimiento;
import dominio.POOBkemonException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Pokemon.
 * Se verifica la correcta inicializacion, el manejo de PS, el funcionamiento de los setters,
 * y el lanzamiento de excepciones en casos de error o parametros invalidos.
 */
public class PokemonTest {

    private Pokemon pikachu;
    private Pokemon bulbasaur;

    /**
     * Prepara el escenario antes de cada prueba.
     * Se crean dos Pokemon de prueba con estadisticas y movimientos basicos.
     */
    @BeforeEach
    public void setUp() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        bulbasaur = new Pokemon("Bulbasaur", "Planta", null, 100, 49, 49, 65, 65, 45, new ArrayList<>());
        pikachu = new Pokemon("Pikachu", "Electrico", null, 100, 55, 40, 50, 50, 90, movimientos);
    }

    // CONSTRUCTOR

    /**
     * Prueba que el constructor inicializa correctamente todos los atributos del Pokemon.
     */
    @Test
    public void shouldInitializePokemonWithCorrectValues() {
        assertEquals("Pikachu", pikachu.getNombre());
        assertEquals("Electrico", pikachu.getTipo());
        assertNull(pikachu.getTipoSecundario());
        assertEquals(100, pikachu.getPs());
        assertEquals(100, pikachu.getPsActual());
        assertEquals(55, pikachu.getAtaque());
        assertEquals(40, pikachu.getDefensa());
        assertEquals(50, pikachu.getAtaqueEspecial());
        assertEquals(50, pikachu.getDefensaEspecial());
        assertEquals(90, pikachu.getVelocidad());
        assertEquals(100, pikachu.getNivel());
        assertNotNull(pikachu.getMovimientos());
    }

    // recibirDaño

    /**
     * Prueba que al recibir dano, los PS actuales se reducen correctamente.
     */
    @Test
    public void shouldReducePsActualWhenReceivingDamage() throws POOBkemonException {
        pikachu.recibirDaño(25);
        assertEquals(75, pikachu.getPsActual());
    }

    /**
     * Prueba que los PS actuales no pueden ser negativos aunque el dano recibido sea mayor que los PS.
     */
    @Test
    public void shouldNotHaveNegativePsActualAfterReceivingExcessiveDamage() throws POOBkemonException {
        pikachu.recibirDaño(999);
        assertEquals(0, pikachu.getPsActual());
    }

    // estaDebilitado 

    /**
     * Prueba que un Pokemon no esta debilitado si sus PS actuales son mayores a cero.
     */
    @Test
    public void shouldNotBeDebilitatedIfPsActualGreaterThanZero() throws POOBkemonException {
        pikachu.recibirDaño(20);
        assertFalse(pikachu.estaDebilitado());
    }

    /**
     * Prueba que un Pokemon esta debilitado si sus PS actuales son cero.
     */
    @Test
    public void shouldBeDebilitatedIfPsActualIsZero() throws POOBkemonException {
        pikachu.recibirDaño(100);
        assertTrue(pikachu.estaDebilitado());
    }

    // restaurarPS

    /**
     * Prueba que al restaurar PS, los PS actuales aumentan correctamente.
     */
    @Test
    public void shouldRestorePsActualWhenHealing() throws POOBkemonException {
        pikachu.recibirDaño(40);
        pikachu.restaurarPS(30);
        assertEquals(90, pikachu.getPsActual());
    }

    /**
     * Prueba que al restaurar PS, los PS actuales no pueden exceder el maximo de PS.
     */
    @Test
    public void shouldNotExceedMaxPsWhenRestoring() throws POOBkemonException {
        pikachu.recibirDaño(10);
        pikachu.restaurarPS(100);
        assertEquals(100, pikachu.getPsActual());
    }

    // SETTERS 

    /**
     * Prueba que los setters actualizan correctamente las estadisticas y tipos del Pokemon.
     */
    @Test
    public void shouldUpdatePokemonStatsWithSetters() {
        pikachu.setAtaque(70);
        pikachu.setDefensa(60);
        pikachu.setTipoPrincipal("Fuego");
        pikachu.setTipoSecundario("Volador");

        assertEquals(70, pikachu.getAtaque());
        assertEquals(60, pikachu.getDefensa());
        assertEquals("Fuego", pikachu.getTipo());
        assertEquals("Volador", pikachu.getTipoSecundario());
    }

    /**
     * Prueba que al cambiar el maximo de PS, los PS actuales tambien se actualizan.
     */
    @Test
    public void shouldResetPsAndPsActualWhenSettingNewPs() {
        pikachu.setPs(150);
        assertEquals(150, pikachu.getPs());
        assertEquals(150, pikachu.getPsActual());
    }

    // EXCEPCIONES

    /**
     * Prueba que recibir dano con una cantidad no valida lanza la excepcion correspondiente.
     */
    @Test
    public void recibirDanioCantidadNoValidaLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.recibirDaño(0);
        });
        assertEquals(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA, ex.getMessage());
    }

    /**
     * Prueba que restaurar PS con una cantidad no valida lanza la excepcion correspondiente.
     */
    @Test
    public void restaurarPsCantidadNoValidaLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.restaurarPS(-10);
        });
        assertEquals(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA, ex.getMessage());
    }

    /**
     * Prueba que asignar movimientos nulos o vacios lanza la excepcion correspondiente.
     */
    @Test
    public void setMovimientosNuloOLimpioLanzaExcepcion() {
        POOBkemonException ex1 = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.setMovimientos(null);
        });
        assertEquals(POOBkemonException.ERROR_MOVIMIENTOS_NO_VALIDOS, ex1.getMessage());

        POOBkemonException ex2 = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.setMovimientos(new ArrayList<>());
        });
        assertEquals(POOBkemonException.ERROR_MOVIMIENTOS_NO_VALIDOS, ex2.getMessage());
    }

    /**
     * Prueba que asignar un nivel no valido lanza la excepcion correspondiente.
     */
    @Test
    public void setNivelNoValidoLanzaExcepcion() {
        POOBkemonException ex1 = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.setNivel(0);
        });
        assertEquals(POOBkemonException.ERROR_NIVEL_NO_VALIDO, ex1.getMessage());

        POOBkemonException ex2 = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.setNivel(-5);
        });
        assertEquals(POOBkemonException.ERROR_NIVEL_NO_VALIDO, ex2.getMessage());
    }

    /**
     * Prueba que aumentar estadisticas con cantidad no valida lanza la excepcion correspondiente.
     */
    @Test
    public void aumentarEstadisticasCantidadNoValidaLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.aumentarEstadisticas("ataque", 0);
        });
        assertEquals(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA, ex.getMessage());
    }

    /**
     * Prueba que intentar aumentar una estadistica inexistente lanza la excepcion correspondiente.
     */
    @Test
    public void aumentarEstadisticasNoExistenteLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.aumentarEstadisticas("fuerza", 10);
        });
        assertTrue(ex.getMessage().contains("no existe"));
    }

    /**
     * Prueba que aumentar una estadistica valida incrementa correctamente el valor.
     */
    @Test
    public void aumentarEstadisticasCorrecto() throws POOBkemonException {
        int oldAtaque = bulbasaur.getAtaque();
        int aumento = bulbasaur.aumentarEstadisticas("ataque", 10);
        assertEquals(10, aumento);
        assertEquals(oldAtaque + 10, bulbasaur.getAtaque());
    }
}