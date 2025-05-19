package pruebas;

import dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase HumanTrainer.
 * Se verifica el manejo del equipo de pokemon, el uso de items, el cambio de pokemon activo,
 * el ataque a oponentes, el uso de Forcejeo cuando no hay PP y el manejo de condiciones de derrota.
 */
public class TrainerTest {
    private HumanTrainer entrenador;
    private HumanTrainer oponente;
    private Pokemon pikachu;
    private Pokemon charmander;

    /**
     * Prepara el escenario antes de cada prueba.
     * Se crean dos entrenadores, cada uno con su equipo de pokemon y movimientos.
     */
    @BeforeEach
    void setUp() {
        entrenador = new HumanTrainer("Ash", "Rojo");
        oponente = new HumanTrainer("Gary", "Azul");

        Movimiento ataqueRapido = new MovimientoFisico("Ataque Rapido", "Normal", 40, 100, 10);
        Movimiento golpe = new MovimientoFisico("Golpe", "Normal", 50, 100, 10);

        pikachu = new Pokemon("Pikachu", "Electrico", null, 100, 55, 40, 50, 50, 90, new ArrayList<>());
        pikachu.getMovimientos().add(ataqueRapido);
        pikachu.getMovimientos().add(golpe);

        charmander = new Pokemon("Charmander", "Fuego", null, 80, 52, 43, 60, 50, 65, new ArrayList<>());
        charmander.getMovimientos().add(new MovimientoFisico("Embestida", "Normal", 30, 100, 10));

        entrenador.agregarPokemon(pikachu);
        entrenador.agregarPokemon(charmander);

        Pokemon squirtle = new Pokemon("Squirtle", "Agua", null, 100, 48, 65, 50, 64, 43, new ArrayList<>());
        squirtle.getMovimientos().add(new MovimientoFisico("Burbuja", "Agua", 30, 100, 10));
        oponente.agregarPokemon(squirtle);
    }

    /**
     * Prueba que al agregar pokemon, el primero se establece como activo y el equipo tiene el tamano correcto.
     */
    @Test
    void shouldAddPokemonAndSetFirstAsActive() {
        assertEquals(pikachu, entrenador.getPokemonActivo());
        assertEquals(2, entrenador.getEquipo().size());
    }

    /**
     * Prueba que al usar una pocion, el pokemon activo se cura y el item se elimina del inventario.
     */
    @Test
    void shouldHealPokemonWhenUsingPotion() throws POOBkemonException {
        Potion pocion = new Potion();
        entrenador.agregarItem(pocion);

        pikachu.recibirDaño(40);
        assertEquals(60, pikachu.getPsActual());

        entrenador.onItemSelected(0);
        assertEquals(80, pikachu.getPsActual());
        assertTrue(entrenador.getItems().isEmpty());
    }

    /**
     * Prueba que el entrenador puede cambiar manualmente el pokemon activo.
     */
    @Test
    void shouldChangeActivePokemonManually() {
        entrenador.onSwitchSelected(1); // Charmander
        assertEquals(charmander, entrenador.getPokemonActivo());
    }

    /**
     * Prueba que no se puede cambiar a un pokemon debilitado, el activo permanece igual.
     */
    @Test
    void shouldNotChangeToFaintedPokemon() throws POOBkemonException {
        charmander.recibirDaño(999); // Lo debilitamos
        entrenador.onSwitchSelected(1);
        assertEquals(pikachu, entrenador.getPokemonActivo()); // No cambia
    }

    /**
     * Prueba que el entrenador es derrotado si todos sus pokemon estan debilitados.
     */
    @Test
    void shouldBeDefeatedIfAllPokemonAreFainted() throws POOBkemonException{
        pikachu.recibirDaño(200);
        charmander.recibirDaño(200);
        assertTrue(entrenador.estaDerrotado());
    }

    /**
     * Prueba que al atacar, el pokemon enemigo pierde PS.
     */
    @Test
    void shouldReduceEnemyHPWhenAttacking() {
        Pokemon enemigo = oponente.getPokemonActivo();
        int vidaAntes = enemigo.getPsActual();

        entrenador.onAttackSelected(0, oponente); // Ataque Rapido 40
        assertTrue(enemigo.getPsActual() < vidaAntes);
    }

    /**
     * Prueba que si todos los movimientos estan sin PP, se usa Forcejeo y se produce autodaño y dano al enemigo.
     */
    @Test
    void shouldUseStruggleWhenAllMovesAreOutOfPP() throws POOBkemonException {
        // Agotar PP de todos los movimientos
        for (Movimiento m : pikachu.getMovimientos()) {
            while (m.esUtilizable()) {
                m.usar();
            }
        }

        // Confirmamos que todos los movimientos estan sin PP
        assertTrue(pikachu.sinPP(), "Pikachu deberia no tener PP en ningun movimiento");

        Pokemon enemigo = oponente.getPokemonActivo();
        enemigo.recibirDaño(20); // Opcional: para que el enemigo no este al 100%
        
        int vidaInicialPikachu = pikachu.getPsActual();
        int vidaInicialEnemigo = enemigo.getPsActual();

        entrenador.onAttackSelected(0, oponente); // Este indice es irrelevante si no hay PP

        int vidaFinalPikachu = pikachu.getPsActual();
        int vidaFinalEnemigo = enemigo.getPsActual();

        // Verificamos que hubo autodaño
        assertTrue(vidaFinalPikachu < vidaInicialPikachu, "Pikachu deberia recibir autodaño por Forcejeo");

        // Verificamos que se hizo dano al enemigo
        assertTrue(vidaFinalEnemigo < vidaInicialEnemigo, "El enemigo deberia recibir dano por Forcejeo");
    }
    
    /**
     * Prueba que si el movimiento falla (precision 0), el enemigo no recibe dano.
     */
    @Test
    void shouldNotAttackWhenMoveMisses() {
        // Usamos movimiento con precision 0 para asegurar fallo
        MovimientoFisico fallo = new MovimientoFisico("Fallo Seguro", "Normal", 50, 0, 1);
        pikachu.getMovimientos().clear();
        pikachu.getMovimientos().add(fallo);

        Pokemon enemigo = oponente.getPokemonActivo();
        int vidaAntes = enemigo.getPsActual();

        entrenador.onAttackSelected(0, oponente);
        assertEquals(vidaAntes, enemigo.getPsActual(), "El ataque fallido no debe causar dano");
    }

    /**
     * Prueba que al usar un movimiento, el PP del movimiento disminuye en 1.
     */
    @Test
    void shouldDecreasePPWhenMoveIsUsed() throws POOBkemonException{
        Movimiento movimientoTest = new MovimientoFisico("Golpe Seguro", "Normal", 50, 100, 5);
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(movimientoTest);
        pikachu.setMovimientos(movimientos);

        int ppAntes = movimientoTest.getPP();
        entrenador.onAttackSelected(0, oponente);
        int ppDespues = movimientoTest.getPP();

        assertEquals(ppAntes - 1, ppDespues);  // Se espera que el PP disminuya en 1
    }

}