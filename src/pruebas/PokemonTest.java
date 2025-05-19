package pruebas;

import dominio.Pokemon;
import dominio.Movimiento;
import dominio.POOBkemonException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {

    private Pokemon pikachu;
    private Pokemon bulbasaur;

    @BeforeEach
    public void setUp() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        bulbasaur = new Pokemon("Bulbasaur", "Planta", null, 100, 49, 49, 65, 65, 45, new ArrayList<>());
        pikachu = new Pokemon("Pikachu", "Electrico", null, 100, 55, 40, 50, 50, 90, movimientos);
    }

    // CONSTRUCTOR

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

// recibirDaño

    @Test
    public void shouldReducePsActualWhenReceivingDamage() throws POOBkemonException {
        pikachu.recibirDaño(25);
        assertEquals(75, pikachu.getPsActual());
    }

    @Test
    public void shouldNotHaveNegativePsActualAfterReceivingExcessiveDamage() throws POOBkemonException {
        pikachu.recibirDaño(999);
        assertEquals(0, pikachu.getPsActual());
    }

    // estaDebilitado 

    @Test
    public void shouldNotBeDebilitatedIfPsActualGreaterThanZero() throws POOBkemonException {
        pikachu.recibirDaño(20);
        assertFalse(pikachu.estaDebilitado());
    }

    @Test
    public void shouldBeDebilitatedIfPsActualIsZero() throws POOBkemonException {
        pikachu.recibirDaño(100);
        assertTrue(pikachu.estaDebilitado());
    }

    // restaurarPS

    @Test
    public void shouldRestorePsActualWhenHealing() throws POOBkemonException {
        pikachu.recibirDaño(40);
        pikachu.restaurarPS(30);
        assertEquals(90, pikachu.getPsActual());
    }

    @Test
    public void shouldNotExceedMaxPsWhenRestoring() throws POOBkemonException {
        pikachu.recibirDaño(10);
        pikachu.restaurarPS(100);
        assertEquals(100, pikachu.getPsActual());
    }

    // SETTERS 

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

    @Test
    public void shouldResetPsAndPsActualWhenSettingNewPs() {
        pikachu.setPs(150);
        assertEquals(150, pikachu.getPs());
        assertEquals(150, pikachu.getPsActual());
    }

     @Test
    public void recibirDanioCantidadNoValidaLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.recibirDaño(0);
        });
        assertEquals(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA, ex.getMessage());
    }

    @Test
    public void restaurarPsCantidadNoValidaLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.restaurarPS(-10);
        });
        assertEquals(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA, ex.getMessage());
    }

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

    @Test
    public void aumentarEstadisticasCantidadNoValidaLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.aumentarEstadisticas("ataque", 0);
        });
        assertEquals(POOBkemonException.ERROR_CANTIDAD_NO_VALIDA, ex.getMessage());
    }

    @Test
    public void aumentarEstadisticasNoExistenteLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            bulbasaur.aumentarEstadisticas("fuerza", 10);
        });
        assertTrue(ex.getMessage().contains("no existe"));
    }

    @Test
    public void aumentarEstadisticasCorrecto() throws POOBkemonException {
        int oldAtaque = bulbasaur.getAtaque();
        int aumento = bulbasaur.aumentarEstadisticas("ataque", 10);
        assertEquals(10, aumento);
        assertEquals(oldAtaque + 10, bulbasaur.getAtaque());
    }
}
