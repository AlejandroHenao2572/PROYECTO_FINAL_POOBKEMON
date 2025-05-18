package dominio;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class Battle implements Serializable {
    private static final long serialVersionUID = 1L;
    private HumanTrainer entrenador1;
    private HumanTrainer entrenador2;
    private HumanTrainer turnoActual;
    private transient BattleGUIListener listener;
    private transient Timer turnTimer;
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
        this.entrenador1.setListener(new TrainerActionListener(this));
        this.entrenador2.setListener(new TrainerActionListener(this));
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
    public void iniciarTurno() {
        if (entrenador1.estaDerrotado() || entrenador2.estaDerrotado()) {
            finalizarBatalla();
            return;
        }
    
        // Verificar si el Pokémon activo está debilitado
        if (turnoActual.getPokemonActivo().estaDebilitado()) {
            manejarPokemonDebilitado();
            return;
        }
    
        // Resto de la lógica normal del turno
        if (turnoActual.getPokemonActivo().sinPP()) {
            turnoActual.getPokemonActivo().getMovimientos().add(FORCEJEO);
        }
    
        esperandoAccion = true;
        iniciarTemporizadorTurno();
    
        if (listener != null) {
            listener.onTurnStarted(turnoActual);
        }
    }
    
    public void manejarPokemonDebilitado() {
        cambioForzado = true;
        cancelarTemporizador();
        
        if (listener != null) {
            listener.onPokemonDebilitado(turnoActual);
        }
        
        if (turnoActual.estaDerrotado()) {
            finalizarBatalla();
        }
    }

    /**
     * Inicia el temporizador para el turno actual
     */
    public void iniciarTemporizadorTurno() {
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
    public void cancelarTemporizador() {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer = null;
        }
    }

    /**
     * Maneja la situación cuando se agota el tiempo para un turno
     */
    public void tiempoAgotado() {
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
    public void finalizarBatalla() {
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
    public String movimientoSeleccionado(int indiceMovimiento) {
        if (!esperandoAccion || isPaused()) return null;
        
        HumanTrainer oponente = (turnoActual == entrenador1) ? entrenador2 : entrenador1;
        String message = turnoActual.onAttackSelected(indiceMovimiento, oponente);
        if (listener != null) {
            listener.onMoveUsed(turnoActual, message);
        }
        return message;
    }

    /**
     * Procesa la selección de cambio de Pokémon por parte del jugador
     * 
     * @param indicePokemon Índice del Pokémon seleccionado
     */
    public String cambioPokemonSeleccionado(int indicePokemon) {
        if ((!esperandoAccion && !cambioForzado) || isPaused()) {
            return "No puedes cambiar Pokémon ahora";
        }
        
        try {
            // Validar selección
            if (indicePokemon < 0 || indicePokemon >= turnoActual.getEquipo().size()) {
                return "Selección de Pokémon no válida";
            }
            
            Pokemon seleccionado = turnoActual.getEquipo().get(indicePokemon);
            
            // Verificar condiciones de cambio
            if (seleccionado.estaDebilitado()) {
                return "¡" + seleccionado.getNombre() + " está debilitado!";
            }
            
            if (seleccionado == turnoActual.getPokemonActivo()) {
                return "¡" + seleccionado.getNombre() + " ya está en combate!";
            }
            
            // Realizar el cambio y obtener mensaje
            String message = turnoActual.cambiarPokemon(indicePokemon);
            
            // Notificar cambio
            if (listener != null) {
                listener.onPokemonChanged(turnoActual, message);
            }
            
            // Manejar flujo del juego
            if (cambioForzado) {
                cambioForzado = false;
                cambiarTurno(); // Pasar al oponente después de cambio forzado
            } else {
                finalizarTurno();
            }
            
            return message;
        } catch (Exception e) {
            System.err.println("Error al cambiar Pokémon: " + e.getMessage());
            return "Error al cambiar Pokémon";
        }
    }

    /**
     * Procesa la selección de uso de ítem por parte del jugador
     * 
     * @param indiceItem Índice del ítem seleccionado
     */
    public String itemSeleccionado(int indiceItem) {
        if (!esperandoAccion || isPaused()) return null;
        
        String message = turnoActual.onItemSelected(indiceItem);;
        if (listener != null) {
            listener.onMoveUsed(turnoActual, message);
        }
        return message;
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
    
    public boolean isCambioForzado() {
        return cambioForzado;
    }

    /**
     * Procesa el uso de un item de revivir
     * @param itemIndex Índice del item en la mochila
     * @param pokemonIndex Índice del Pokémon a revivir
     */
    public void usarItemRevivir(int itemIndex, int pokemonIndex) {
        if (!esperandoAccion) return;
        
        try {
            // Validar índices
            if (itemIndex < 0 || itemIndex >= turnoActual.getItems().size() ||
                pokemonIndex < 0 || pokemonIndex >= turnoActual.getEquipo().size()) {
                return;
            }
            
            Item item = turnoActual.getItems().get(itemIndex);
            Pokemon pokemon = turnoActual.getEquipo().get(pokemonIndex);
            
            // Verificar que el item sea de revivir y el Pokémon esté debilitado
            if (item instanceof Revive && pokemon.estaDebilitado()) {
                // Usar el item
                item.usarEn(pokemon);
                
                // Eliminar el item de la mochila
                turnoActual.getItems().remove(itemIndex);
                
                // Notificar a la interfaz
                if (listener != null) {
                    listener.onPokemonRevivido(turnoActual, pokemon);
                }
                
                // Si revivimos al Pokémon activo, continuar el turno
                if (pokemon == turnoActual.getPokemonActivo()) {
                    esperandoAccion = true;
                    iniciarTemporizadorTurno();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al usar item de revivir: " + e.getMessage());
        }
    }

    public boolean isPaused() {
        return  false;
    }

    /**
     * Guarda el estado actual de la batalla en un archivo
     * @param filePath Ruta del archivo donde guardar
     * @throws IOException Si ocurre un error de E/S
     */
    public void guardarPartida(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(filePath)))) {
            oos.writeObject(this);
        }
    }
    /**
     * Carga una batalla desde un archivo
     * @param filePath Ruta del archivo a cargar
     * @return La batalla cargada
     * @throws IOException Si ocurre un error de E/S
     * @throws ClassNotFoundException Si la clase no se encuentra
     */
    public static Battle cargarPartida(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Battle batalla = (Battle) ois.readObject();
            // Reconstruir listeners después de cargar
            batalla.entrenador1.setListener(new TrainerActionListener(batalla));
            batalla.entrenador2.setListener(new TrainerActionListener(batalla));
            return batalla;
        }
    }
    
    private static class TrainerActionListener implements TrainerListener, Serializable {
    private static final long serialVersionUID = 1L;
    private final Battle battle;
    
    public TrainerActionListener(Battle battle) {
        this.battle = battle;
    }
    
    @Override
    public void onActionPerformed() {
        battle.finalizarTurno();
    }
}
}

/**
 * Interfaz para notificar a Battle cuando un entrenador completa su acción
 */
interface TrainerListener extends Serializable {
    void onActionPerformed();
}

