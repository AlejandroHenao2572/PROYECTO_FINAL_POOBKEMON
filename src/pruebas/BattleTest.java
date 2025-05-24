package pruebas;

import dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Pruebas unitarias para la clase Battle.
 * Se verifica el correcto funcionamiento de los metodos principales de la batalla,
 * incluyendo turnos, seleccion de movimientos, cambios de pokemon, uso de items y guardado/carga.
 */
public class BattleTest {

    private HumanTrainer entrenador1;
    private HumanTrainer entrenador2;
    private Battle battle;
    private Pokemon p1;
    private Pokemon p2;

    /**
     * Configura el entorno de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        entrenador1 = new HumanTrainer("Ash", "Rojo");
        entrenador2 = new HumanTrainer("Gary", "Azul");

        Movimiento ataque = new MovimientoFisico("Ataque Rapido", "Normal", 999, 100, 99);

        p1 = new Pokemon("Pikachu", "Electrico", null, 100, 50, 40, 40, 40, 90, new ArrayList<>());
        p1.getMovimientos().add(ataque);

        p2 = new Pokemon("Squirtle", "Agua", null, 100, 50, 40, 40, 40, 43, new ArrayList<>());
        p2.getMovimientos().add(ataque);

        entrenador1.agregarPokemon(p1);
        entrenador2.agregarPokemon(p2);

        battle = Battle.getInstance();
        battle.setEntrenador1(entrenador1);
        battle.setEntrenador2(entrenador2);
        battle.setTurnoActual(entrenador1);
        battle.setListener(null);
    }

    /**
     * Verifica que la batalla inicia y asigna un turno inicial.
     */
    @Test
    void deberiaIniciarBatallaYAsignarTurnoInicial() {
        battle.iniciar();
        assertNotNull(battle.getTurnoActual(), "La batalla debe asignar un turno inicial");
    }

    /**
     * Verifica que el turno cambia entre entrenadores.
     */
    @Test
    void deberiaCambiarTurnoEntreEntrenadores() {
        battle.iniciar();
        Trainer turnoInicial = battle.getTurnoActual();
        battle.finalizarTurno();
        Trainer nuevoTurno = battle.getTurnoActual();
        assertNotEquals(turnoInicial, nuevoTurno, "El turno debe cambiar de entrenador");
    }

    /**
     * Verifica que la batalla termina cuando un entrenador es derrotado.
     */
    @Test
    void deberiaTerminarBatallaCuandoEntrenadorEsDerrotado() {
        try {
            p2.recibirDaño(999);
        } catch (POOBkemonException e) {
            fail("No se esperaba una excepcion al debilitar al Pokemon: " + e.getMessage());
        }
        assertTrue(p2.estaDebilitado(), "El Pokemon debe estar debilitado");
        battle.iniciar();
        assertTrue(entrenador2.estaDerrotado(), "El entrenador 2 debe estar derrotado");
    }

    /**
     * Verifica que se puede seleccionar un movimiento en el turno correcto.
     */
    @Test
    void deberiaPermitirSeleccionarMovimientoEnTurno() throws POOBkemonException {
        battle.iniciar();
        String result = battle.movimientoSeleccionado(0);
        assertNotNull(result, "Debe poder seleccionar un movimiento en su turno");
    }

    /**
     * Verifica que no se permite seleccionar un movimiento con un indice invalido.
     */
    @Test
    void noDeberiaPermitirSeleccionarMovimientoConIndiceInvalido() {
        battle.iniciar();
        assertThrows(POOBkemonException.class, () -> {
            battle.movimientoSeleccionado(99);
        });
    }

    /**
     * Verifica que se puede cambiar de pokemon en el turno correcto.
     */
    @Test
    void deberiaPermitirCambioDePokemonEnTurno() throws POOBkemonException {
        Pokemon otro = new Pokemon("Bulbasaur", "Planta", null, 100, 50, 40, 40, 40, 45, new ArrayList<>());
        entrenador1.agregarPokemon(otro);
        battle.iniciar();
        String msg = battle.cambioPokemonSeleccionado(1);
        assertNotNull(msg, "Debe poder cambiar de Pokemon en su turno");
    }

    /**
     * Verifica que no se puede cambiar a un pokemon debilitado.
     */
    @Test
    void noDeberiaPermitirCambioAPokemonDebilitado() {
        Pokemon otro = new Pokemon("Bulbasaur", "Planta", null, 100, 50, 40, 40, 40, 45, new ArrayList<>());
        entrenador1.agregarPokemon(otro);
        try {
            otro.recibirDaño(999);
        } catch (POOBkemonException e) {
            fail("No se esperaba una excepcion al debilitar al Pokemon: " + e.getMessage());
        }
        battle.iniciar();
        assertThrows(POOBkemonException.class, () -> {
            battle.cambioPokemonSeleccionado(1);
        });
    }

    /**
     * Verifica que no se puede cambiar de pokemon fuera de turno.
     */
    @Test
    void noDeberiaPermitirCambioDePokemonFueraDeTurno() {
        assertThrows(POOBkemonException.class, () -> {
            battle.cambioPokemonSeleccionado(0);
        });
    }

    /**
     * Verifica que no se puede cambiar de pokemon con un indice invalido.
     */
    @Test
    void noDeberiaPermitirCambioDePokemonConIndiceInvalido() {
        battle.iniciar();
        assertThrows(POOBkemonException.class, () -> {
            battle.cambioPokemonSeleccionado(99);
        });
    }

    /**
     * Verifica que se puede usar un item en el turno correcto.
     */
    @Test
    void deberiaPermitirUsoDeItemEnTurno() throws POOBkemonException {
        Item pocion = new Potion();
        entrenador1.getItems().add(pocion);
        battle.iniciar();
        String msg = battle.itemSeleccionado(0);
        assertNotNull(msg, "Debe poder usar un item en su turno");
    }

    /**
     * Verifica que no se puede usar un item fuera de turno.
     */
    @Test
    void noDeberiaPermitirUsoDeItemFueraDeTurno() {
        assertThrows(POOBkemonException.class, () -> {
            battle.itemSeleccionado(0);
        });
    }

    /**
     * Verifica que no se puede usar un item con un indice invalido.
     */
    @Test
    void noDeberiaPermitirUsoDeItemConIndiceInvalido() {
        battle.iniciar();
        assertThrows(POOBkemonException.class, () -> {
            battle.itemSeleccionado(99);
        });
    }

    /**
     * Verifica que se puede guardar y cargar la batalla sin excepcion.
     */
    @Test
    void deberiaGuardarYCargarBatallaSinExcepcion() throws Exception {
        battle.iniciar();
        String path = "test_save.pokemon";
        assertDoesNotThrow(() -> battle.guardarPartida(path));
        Battle cargada = assertDoesNotThrow(() -> Battle.cargarPartida(path));
        assertNotNull(cargada);
        assertEquals(battle.getEntrenador1().getNombre(), cargada.getEntrenador1().getNombre());
        new java.io.File(path).delete();
    }
}