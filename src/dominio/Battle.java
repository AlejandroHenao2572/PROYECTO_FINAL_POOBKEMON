package dominio;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Esta clase maneja la logica de una batalla entre entrenadores humanos
 * Autores David Patacon y Daniel Hueso
 * Version 2.0
 */
public class Battle {
    private HumanTrainer entrenador1;
    private HumanTrainer entrenador2;
    private HumanTrainer turnoActual;
    private BattleGUIListener listener;
    private Timer turnTimer;
    private boolean esperandoAccion;
    private static final int TIEMPO_TURNO = 20; // 20 segundos por turno
    private static final Movimiento FORCEJEO = new Forcejeo();
    private boolean cambioForzado = false;

    /**
     * Constructor que inicializa una batalla con dos entrenadores humanos
     * 
     * @param entrenador1 Primer participante de la batalla
     * @param entrenador2 Segundo participante de la batalla
     */
    public Battle(HumanTrainer entrenador1, HumanTrainer entrenador2) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;
        this.turnoActual = lanzarMoneda();
        this.esperandoAccion = false;
        
        // Configurar listeners de los entrenadores
        this.entrenador1.setListener(new TrainerListener() {
            @Override
            public void onActionPerformed() {
                finalizarTurno();
            }
        });
        
        this.entrenador2.setListener(new TrainerListener() {
            @Override
            public void onActionPerformed() {
                finalizarTurno();
            }
        });
    }

    public static Battle setupBattle(
        List<String> nombresEquipo1,
        List<String> nombresEquipo2,
        Map<String, Integer> items1,
        Map<String, Integer> items2
    ) {
        HumanTrainer jugador1 = new HumanTrainer("Jugador 1", "Rojo");
        HumanTrainer jugador2 = new HumanTrainer("Jugador 2", "Azul");

           for (String nombre : nombresEquipo1) {
            Pokemon p = BattleFactory.crearPokemon(nombre, jugador1);
            jugador1.agregarPokemon(p);
        }

        for (String nombre : nombresEquipo2) {
            Pokemon p = BattleFactory.crearPokemon(nombre, jugador2);
            jugador2.agregarPokemon(p);
        }

        BattleFactory.agregarItems(jugador1, items1);
        BattleFactory.agregarItems(jugador2, items2);

        return new Battle(jugador1, jugador2);
    }

    /**
     * Establece el listener para eventos de la interfaz grafica
     * 
     * @param listener Objeto que recibira los eventos de la batalla
     */
    public void setListener(BattleGUIListener listener) {
        this.listener = listener;
    }

    /**
     * Determina aleatoriamente que entrenador comienza la batalla
     * 
     * @return El entrenador que inicia el combate
     */
    private HumanTrainer lanzarMoneda() {
        Random random = new Random();
        return random.nextBoolean() ? entrenador1 : entrenador2;
    }

    /**
     * Inicia la batalla y notifica al listener
     */
    public void iniciar() {
        if (listener != null) {
            listener.onBattleStarted();
        }
        iniciarTurno();
    }

    /**
     * Inicia un nuevo turno de batalla
     */
    private void iniciarTurno() {
        if (entrenador1.estaDerrotado() || entrenador2.estaDerrotado()) {
            finalizarBatalla();
            return;
        }

        if (turnoActual.getPokemonActivo().estaDebilitado()) {
            if (listener != null) {
                listener.onPokemonDebilitado(turnoActual);
            }
            return;
        }

        // Verificar si el Pokémon se quedó sin PP
        if (turnoActual.getPokemonActivo().sinPP()) {
            turnoActual.getPokemonActivo().getMovimientos().add(FORCEJEO);
        }

        esperandoAccion = true;
        iniciarTemporizadorTurno();

        if (listener != null) {
            listener.onTurnStarted(turnoActual);
        }
    }

    /**
     * Inicia el temporizador para el turno actual
     */
    private void iniciarTemporizadorTurno() {
        cancelarTemporizador();
        
        turnTimer = new Timer();
        turnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                tiempoAgotado();
            }
        }, TIEMPO_TURNO * 1000);
    }

    /**
     * Cancela el temporizador del turno actual
     */
    private void cancelarTemporizador() {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer = null;
        }
    }

    /**
     * Maneja la situación cuando se agota el tiempo para un turno
     */
    private void tiempoAgotado() {
        if (esperandoAccion) {
            // Penalización por tiempo agotado: pierde 1 PP en cada movimiento
            for (Movimiento m : turnoActual.getPokemonActivo().getMovimientos()) {
                if (m.getPP() > 0) {
                    m.usar(); // Consume 1 PP
                }
            }
            
            if (listener != null) {
                listener.onTurnEnded(turnoActual);
            }
            
            finalizarTurno();
        }
    }

    /**
     * Finaliza el turno actual y pasa al siguiente
     */
    public void finalizarTurno() {
        esperandoAccion = false;
        cancelarTemporizador();
        cambiarTurno();
    }

    /**
     * Cambia el turno al otro entrenador
     */
    public void cambiarTurno() {
        turnoActual = (turnoActual == entrenador1) ? entrenador2 : entrenador1;
        iniciarTurno();
    }

    /**
     * Finaliza la batalla y determina al ganador
     */
    private void finalizarBatalla() {
        cancelarTemporizador();
        HumanTrainer ganador = entrenador1.estaDerrotado() ? entrenador2 : entrenador1;
        if (listener != null) {
            listener.onBattleEnded(ganador);
        }
    }

    /**
     * Procesa la selección de un movimiento por parte del jugador
     * 
     * @param indiceMovimiento Índice del movimiento seleccionado
     */
    public void movimientoSeleccionado(int indiceMovimiento) {
        if (!esperandoAccion) return;
        
        HumanTrainer oponente = (turnoActual == entrenador1) ? entrenador2 : entrenador1;
        turnoActual.onAttackSelected(indiceMovimiento, oponente);
    }

    /**
     * Procesa la selección de cambio de Pokémon por parte del jugador
     * 
     * @param indicePokemon Índice del Pokémon seleccionado
     */
    public void cambioPokemonSeleccionado(int indicePokemon) {
        if (!esperandoAccion) return;
        
        turnoActual.onSwitchSelected(indicePokemon);
    }

    /**
     * Procesa la selección de uso de ítem por parte del jugador
     * 
     * @param indiceItem Índice del ítem seleccionado
     */
    public void itemSeleccionado(int indiceItem) {
        if (!esperandoAccion) return;
        
        turnoActual.onItemSelected(indiceItem);
    }

    // Métodos getter
    public HumanTrainer getEntrenador1() {
        return entrenador1;
    }

    public HumanTrainer getEntrenador2() {
        return entrenador2;
    }

    public HumanTrainer getTurnoActual() {
        return turnoActual;
    }
    
}

/**
 * Interfaz para notificar a Battle cuando un entrenador completa su acción
 */
interface TrainerListener {
    void onActionPerformed();
}