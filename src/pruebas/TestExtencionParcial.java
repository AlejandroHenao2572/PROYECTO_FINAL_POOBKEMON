package pruebas;

import dominio.Pokemon;
import dominio.Battle;
import dominio.HumanTrainer;
import dominio.Movimiento;
import dominio.POOBkemonException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


/**
 * Pruebas unitarias para la extension de sacrificio de Pokemon.
 * Se verifica el correcto funcionamiento de los metodos esSacrificable, getPorcentajeSalud,
 * aumentarSaludPorcentaje y setPsActual de la clase Pokemon.
 * Ademas, se prueba la funcionalidad de sacrificar un Pokemon y transferir su salud a otro Pokemon.
 */
public class TestExtencionParcial {

   
    private Battle battle;
    private HumanTrainer entrenador;
    private HumanTrainer entrenador2;
    private Pokemon sacrificado;
    private Pokemon receptor;
    private Pokemon pokemon;

    /**
     * Prepara el escenario antes de cada prueba.
     * Se crea un Pokemon de prueba con 100 puntos de salud maximos.
     */
    @BeforeEach
    void setUp() {
        battle = Battle.getInstance();
        entrenador = new HumanTrainer("Ash", "Rojo");
        entrenador2 = new HumanTrainer("Pedro", "Azul");
        sacrificado = new Pokemon("Pikachu", "Electrico", null, 100, 50, 40, 40, 40, 90, new ArrayList<>());
        receptor = new Pokemon("Charizard", "Fuego", null, 100, 50, 40, 40, 40, 100, new ArrayList<>());
        entrenador.agregarPokemon(sacrificado);
        entrenador.agregarPokemon(receptor);
        battle.setEntrenador1(entrenador);
        battle.setEntrenador1(entrenador2);
        battle.setTurnoActual(entrenador);
        pokemon = new Pokemon("Testmon", "Normal", null, 100, 50, 40, 40, 40, 90, new ArrayList<Movimiento>());
    }

    /**
     * Verifica que el Pokemon es sacrificable si tiene la mitad o menos de la salud y ha recibido daño.
     */
    @Test
    void deberiaSerSacrificableSiTieneMenosDeLaMitadDeSaludYHaRecibidoDanio() {
        pokemon.setPsActual(50);
        assertTrue(pokemon.esSacrificable(), "Debe ser sacrificable con 50% de salud");
        pokemon.setPsActual(49);
        assertTrue(pokemon.esSacrificable(), "Debe ser sacrificable con menos de 50% de salud");
    }

    /**
     * Verifica que el Pokemon no es sacrificable si tiene la salud completa.
     */
    @Test
    void noDeberiaSerSacrificableSiTieneSaludCompleta() {
        pokemon.setPsActual(100);
        assertFalse(pokemon.esSacrificable(), "No debe ser sacrificable con salud completa");
    }

    /**
     * Verifica que el Pokemon no es sacrificable si tiene mas de la mitad de la salud.
     */
    @Test
    void noDeberiaSerSacrificableSiTieneMasDeLaMitadDeSalud() {
        pokemon.setPsActual(51);
        assertFalse(pokemon.esSacrificable(), "No debe ser sacrificable con mas de 50% de salud");
    }

    /**
     * Verifica que el metodo getPorcentajeSalud calcula correctamente el porcentaje de salud.
     */
    @Test
    void deberiaCalcularPorcentajeSaludCorrectamente() {
        pokemon.setPsActual(100);
        assertEquals(100, pokemon.getPorcentajeSalud(), "Debe ser 100% con salud completa");
        pokemon.setPsActual(50);
        assertEquals(50, pokemon.getPorcentajeSalud(), "Debe ser 50% con la mitad de salud");
        pokemon.setPsActual(25);
        assertEquals(25, pokemon.getPorcentajeSalud(), "Debe ser 25% con un cuarto de salud");
    }

    /**
     * Verifica que el metodo aumentarSaludPorcentaje suma correctamente la salud.
     */
    @Test
    void deberiaAumentarSaludPorPorcentajeInclusoSobreElMaximo() {
        pokemon.setPsActual(100);
        pokemon.aumentarSaludPorcentaje(50); // +50% de 100 = +50
        assertEquals(150, pokemon.getPsActual(), "Debe tener 150 de salud actual");
    }

    /**
     * Verifica que se puede establecer correctamente el valor de los puntos de salud actuales
     */
    @Test
    void deberiaPermitirSetearPsActual() {
        pokemon.setPsActual(42);
        assertEquals(42, pokemon.getPsActual(), "Debe poder establecer los PS actuales");
    }

    /**
     * Verifica que se puede sacrificar un Pokemon y transferir su salud correctamente.
     */
    @Test
    void deberiaPermitirSacrificarYTransferirSalud() throws POOBkemonException {
        sacrificado.setPsActual(30); // 30% de salud
        receptor.setPsActual(100);
        entrenador.cambiarPokemon(0);    // 100% de salud
        String msg = battle.sacrificarPokemon(entrenador, 1);
        assertEquals(0, sacrificado.getPsActual(), "El sacrificado debe quedar con 0 de salud");
        assertEquals(130, receptor.getPsActual(), "El receptor debe tener 130 de salud");
        assertTrue(msg.contains("se sacrifico y transfirio 30% de salud a Charizard"));
        assertEquals(receptor, entrenador.getPokemonActivo(), "El Pokemon activo debe ser el receptor");
    }

    /**
     * Verifica que no se puede sacrificar si el Pokemon no es sacrificable.
     */
    @Test
    void noDeberiaPermitirSacrificarSiNoEsSacrificable() {
        sacrificado.setPsActual(100); // Salud completa
        receptor.setPsActual(100);
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            battle.sacrificarPokemon(entrenador, 1);
        });
        assertEquals("El Pokemon no es sacrificable.", ex.getMessage());
    }

    /**
     * Verifica que no se puede sacrificar si el indice del receptor es invalido.
     */
    @Test
    void noDeberiaPermitirSacrificarConIndiceReceptorInvalido() {
        sacrificado.setPsActual(40); // Es sacrificable
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            battle.sacrificarPokemon(entrenador, 5);
        });
        assertEquals("Indice de receptor invalido.", ex.getMessage());
    }

    /**
     * Verifica que no se puede transferir salud al mismo Pokemon sacrificado.
     */
    @Test
    void noDeberiaPermitirTransferirASiMismo() {
        sacrificado.setPsActual(40); // Es sacrificable
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            battle.sacrificarPokemon(entrenador, 0);
        });
        assertEquals("No se puede transferir a ese Pokemon.", ex.getMessage());
    }

    /**
     * Verifica que no se puede transferir salud a un Pokemon debilitado.
     */
    @Test
    void noDeberiaPermitirTransferirAPokemonDebilitado() {
        sacrificado.setPsActual(40); // Es sacrificable
        receptor.setPsActual(0); // Receptor debilitado
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            battle.sacrificarPokemon(entrenador, 1);
        });
        assertEquals("No se puede transferir a ese Pokemon.", ex.getMessage());
    }
}