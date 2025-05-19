package pruebas;

import dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Pokemon bulbasaur;
    private Potion potion;
    private SuperPotion superPotion;
    private HyperPotion hyperPotion;
    private Revive revive;

    @BeforeEach
    public void setUp() throws POOBkemonException {
        bulbasaur = new Pokemon("Bulbasaur", "Planta", null, 100, 49, 49, 65, 65, 45, new ArrayList<>());
        potion = new Potion();
        superPotion = new SuperPotion();
        hyperPotion = new HyperPotion();
        revive = new Revive();
    }

    // Potion

    @Test
    public void shouldRestore20PsWithPotionIfNotFainted() throws POOBkemonException {
        bulbasaur.recibirDaño(30); // PS actual = 70
        potion.usarEn(bulbasaur);
        assertEquals(90, bulbasaur.getPsActual());
    }

    @Test
    public void shouldNotRestorePsWithPotionIfFainted() throws POOBkemonException {
        bulbasaur.recibirDaño(100);
        potion.usarEn(bulbasaur);
        assertEquals(0, bulbasaur.getPsActual());
    }

    // SuperPotion

    @Test
    public void shouldRestore50PsWithSuperPotionIfNotFainted() throws POOBkemonException {
        bulbasaur.recibirDaño(60); // PS actual = 40
        superPotion.usarEn(bulbasaur);
        assertEquals(90, bulbasaur.getPsActual());
    }

    @Test
    public void shouldNotRestorePsWithSuperPotionIfFainted() throws POOBkemonException {
        bulbasaur.recibirDaño(200);
        superPotion.usarEn(bulbasaur);
        assertEquals(0, bulbasaur.getPsActual());
    }

    // HyperPotion

    @Test
    public void shouldRestoreUpToMaxPsWithHyperPotionIfNotFainted() throws POOBkemonException {
        bulbasaur.recibirDaño(150); // PS actual = 0
        hyperPotion.usarEn(bulbasaur);
        assertEquals(0, bulbasaur.getPsActual());

        bulbasaur.setPs(150);  // Nuevo max ps
        bulbasaur.recibirDaño(100); // PS actual = 50
        hyperPotion.usarEn(bulbasaur); // Debe curar 200 pero max es 150
        assertEquals(150, bulbasaur.getPsActual());
    }

    @Test
    public void shouldNotRestorePsWithHyperPotionIfFainted() throws POOBkemonException {
        bulbasaur.recibirDaño(100);
        hyperPotion.usarEn(bulbasaur);
        assertEquals(0, bulbasaur.getPsActual());
    }

    // Revive

    @Test
    public void shouldReviveFaintedPokemonWithHalfPs() throws POOBkemonException {
        bulbasaur.recibirDaño(100);
        assertTrue(bulbasaur.estaDebilitado());
        revive.usarEn(bulbasaur);
        assertEquals(50, bulbasaur.getPsActual());
        assertFalse(bulbasaur.estaDebilitado());
    }

    @Test
    public void shouldNotUseReviveIfPokemonIsNotFainted() throws POOBkemonException {
        bulbasaur.recibirDaño(60);
        revive.usarEn(bulbasaur);
        assertEquals(40, bulbasaur.getPsActual());
    }

    // getNombre

    @Test
    public void shouldReturnCorrectItemNames() {
        assertEquals("Potion", potion.getNombre());
        assertEquals("SuperPotion", superPotion.getNombre());
        assertEquals("HyperPotion", hyperPotion.getNombre());
        assertEquals("Revive", revive.getNombre());
    }

   
}