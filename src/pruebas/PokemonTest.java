package pruebas;

import dominio.Pokemon;
import dominio.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {

    private Pokemon pikachu;

    @BeforeEach
    public void setUp() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
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

    @Test
    public void shouldReducePsActualWhenReceivingDamage() {
        pikachu.recibirDaño(25);
        assertEquals(75, pikachu.getPsActual());
    }

    @Test
    public void shouldNotHaveNegativePsActualAfterReceivingExcessiveDamage() {
        pikachu.recibirDaño(999);
        assertEquals(0, pikachu.getPsActual());
    }

    // estaDebilitado 

    @Test
    public void shouldNotBeDebilitatedIfPsActualGreaterThanZero() {
        pikachu.recibirDaño(20);
        assertFalse(pikachu.estaDebilitado());
    }

    @Test
    public void shouldBeDebilitatedIfPsActualIsZero() {
        pikachu.recibirDaño(100);
        assertTrue(pikachu.estaDebilitado());
    }

    // restaurarPS

    @Test
    public void shouldRestorePsActualWhenHealing() {
        pikachu.recibirDaño(40);
        pikachu.restaurarPS(30);
        assertEquals(90, pikachu.getPsActual());
    }

    @Test
    public void shouldNotExceedMaxPsWhenRestoring() {
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
}
