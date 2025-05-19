package pruebas;

import dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Clase de pruebas unitarias para la clase Battle.
 * Aqui se prueban los principales flujos de la batalla, el manejo de turnos,
 * la seleccion de movimientos, el cambio de pokemon, el uso de items y el manejo de excepciones.
 * Cada test verifica un comportamiento esperado o una situacion de error controlada.
 */
public class BattleTest {

    private HumanTrainer entrenador1;
    private HumanTrainer entrenador2;
    private Battle batalla;

    private Pokemon p1;
    private Pokemon p2;

    /**
     * Prepara el escenario antes de cada prueba.
     * Se crean dos entrenadores y un pokemon para cada uno con un movimiento de alto dano.
     */
    @BeforeEach
    void setUp() {
        entrenador1 = new HumanTrainer("Ash", "Rojo");
        entrenador2 = new HumanTrainer("Gary", "Azul");

        Movimiento ataque = new MovimientoFisico("Ataque Rapido", "Normal", 999, 100, 99); // movimiento de alto dano

        p1 = new Pokemon("Pikachu", "Electrico", null, 100, 50, 40, 40, 40, 90, new ArrayList<>());
        p1.getMovimientos().add(ataque);

        p2 = new Pokemon("Squirtle", "Agua", null, 100, 50, 40, 40, 40, 43, new ArrayList<>());
        p2.getMovimientos().add(ataque);

        entrenador1.agregarPokemon(p1);
        entrenador2.agregarPokemon(p2);

        batalla = new Battle(entrenador1, entrenador2);
    }

    /**
     * Verifica que la batalla se inicializa correctamente y se asigna un turno inicial.
     */
    @Test
    void shouldStartBattleProperly() {
        batalla.iniciar();
        assertNotNull(batalla.getTurnoActual(), "La batalla debe haber asignado un turno inicial");
    }

    /**
     * Prueba que la batalla termina cuando uno de los entrenadores es derrotado.
     * Se debilita el pokemon de un entrenador y se verifica el estado de derrota.
     */
    @Test
    void shouldEndBattleWhenOneTrainerIsDefeated() {
        assertDoesNotThrow(() -> p2.recibirDaño(999)); // dejar fuera de combate
        assertTrue(p2.estaDebilitado(), "El Pokemon deberia estar debilitado");

        batalla.iniciar();
        assertTrue(entrenador2.estaDerrotado(), "El entrenador 2 deberia estar derrotado");
        assertFalse(entrenador1.estaDerrotado(), "El entrenador 1 no deberia estar derrotado");
    }

    /**
     * Verifica que el turno cambia correctamente entre los entrenadores.
     */
    @Test
    void shouldChangeTurn() {
        Trainer turnoInicial = batalla.getTurnoActual();

        batalla.cambiarTurno();
        Trainer nuevoTurno = batalla.getTurnoActual();

        assertNotEquals(turnoInicial, nuevoTurno, "El turno deberia haber cambiado de entrenador");
    }

    /**
     * Prueba que un pokemon debilitado no puede actuar y permanece debilitado tras finalizar el turno.
     */
    @Test
    void shouldNotActIfPokemonIsFainted() {
        batalla.iniciar();
        Trainer actual = batalla.getTurnoActual();
        Pokemon activo = actual.getPokemonActivo();
        assertDoesNotThrow(() -> activo.recibirDaño(999));

        assertTrue(activo.estaDebilitado(), "El Pokemon deberia estar debilitado");

        batalla.finalizarTurno();
        assertTrue(actual.getPokemonActivo().estaDebilitado(), "El Pokemon aun esta debilitado y no debe actuar");
    }

    /**
     * Verifica que el ganador se declara correctamente cuando un entrenador pierde todos sus pokemon.
     */
    @Test
    void shouldDeclareCorrectWinner() {
        assertDoesNotThrow(() -> p2.recibirDaño(999));
        batalla.iniciar();

        assertTrue(entrenador2.estaDerrotado(), "Entrenador 2 deberia haber perdido");
        assertFalse(entrenador1.estaDerrotado(), "Entrenador 1 deberia haber ganado");
    }

    /**
     * Prueba que los turnos alternan correctamente entre los entrenadores.
     */
    @Test
    void shouldAlternateTurnsProperly() {
        batalla.iniciar();
        Trainer turno1 = batalla.getTurnoActual();

        batalla.cambiarTurno();
        Trainer turno2 = batalla.getTurnoActual();

        batalla.cambiarTurno();
        Trainer turno3 = batalla.getTurnoActual();

        assertEquals(turno1, turno3, "El turno deberia alternar correctamente entre los entrenadores");
        assertNotEquals(turno1, turno2, "El turno deberia cambiar tras una llamada a cambiarTurno");
    }

    /**
     * Prueba que se lanza una excepcion si se intenta seleccionar un movimiento fuera de turno.
     */
    @Test
    void testMovimientoSeleccionadoFueraDeTurnoLanzaExcepcion() {
        // No se ha iniciado la batalla, esperandoAccion es false
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            batalla.movimientoSeleccionado(0);
        });
        assertEquals(POOBkemonException.ERROR_MOVIMIENTO_TURNO, ex.getMessage());
    }

    /**
     * Prueba que se lanza una excepcion si se selecciona un indice de movimiento invalido.
     */
    @Test
    void testMovimientoSeleccionadoIndiceInvalidoLanzaExcepcion() {
        batalla.iniciar();
        // Turno valido, pero indice fuera de rango
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            batalla.movimientoSeleccionado(99);
        });
        assertEquals(POOBkemonException.ERROR_MOVIMIENTO_INDICE, ex.getMessage());
    }

    /**
     * Prueba que se lanza una excepcion si se intenta cambiar de pokemon fuera de turno.
     */
    @Test
    void testCambioPokemonSeleccionadoFueraDeTurnoLanzaExcepcion() {
        // No se ha iniciado la batalla, esperandoAccion es false
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            batalla.cambioPokemonSeleccionado(0);
        });
        assertEquals(POOBkemonException.ERROR_CAMBIO_POKEMON_TURNO, ex.getMessage());
    }

    /**
     * Prueba que se lanza una excepcion si se selecciona un indice de pokemon invalido al cambiar.
     */
    @Test
    void testCambioPokemonSeleccionadoIndiceInvalidoLanzaExcepcion() {
        batalla.iniciar();
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            batalla.cambioPokemonSeleccionado(99);
        });
        assertEquals(POOBkemonException.ERROR_CAMBIO_POKEMON_INDICE, ex.getMessage());
    }

    /**
     * Prueba que se lanza una excepcion si se intenta cambiar a un pokemon debilitado.
     */
    @Test
    void testCambioPokemonSeleccionadoDebilitadoLanzaExcepcion() {
        batalla.iniciar();
        // Debilitar el Pokemon que no es el activo
        Pokemon otro = new Pokemon("Bulbasaur", "Planta", null, 100, 50, 40, 40, 40, 45, new ArrayList<>());
        entrenador1.agregarPokemon(otro);
        assertDoesNotThrow(() -> otro.recibirDaño(999));
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            batalla.cambioPokemonSeleccionado(1); // Cambiar al debilitado
        });
        assertTrue(ex.getMessage().contains("debilitado"));
    }

    /**
     * Prueba que se lanza una excepcion si se intenta usar un item fuera de turno.
     */
    @Test
    void testItemSeleccionadoFueraDeTurnoLanzaExcepcion() {
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            batalla.itemSeleccionado(0);
        });
        assertEquals(POOBkemonException.ERROR_ITEM_TURNO, ex.getMessage());
    }

    /**
     * Prueba que se lanza una excepcion si se selecciona un indice de item invalido.
     */
    @Test
    void testItemSeleccionadoIndiceInvalidoLanzaExcepcion() {
        batalla.iniciar();
        POOBkemonException ex = assertThrows(POOBkemonException.class, () -> {
            batalla.itemSeleccionado(99);
        });
        assertEquals(POOBkemonException.ERROR_ITEM_INDICE, ex.getMessage());
    }

    /**
     * Prueba que la partida se puede guardar y cargar correctamente sin lanzar excepcion.
     */
    @Test
    void testGuardarYcargarPartida() throws Exception {
        batalla.iniciar();
        String path = "test_save.pokemon";
        // Guardar
        assertDoesNotThrow(() -> batalla.guardarPartida(path));
        // Cargar
        Battle cargada = assertDoesNotThrow(() -> Battle.cargarPartida(path));
        assertNotNull(cargada);
        assertEquals(batalla.getEntrenador1().getNombre(), cargada.getEntrenador1().getNombre());
        // Limpieza
        new java.io.File(path).delete();
    }
}