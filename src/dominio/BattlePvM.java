package dominio;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;

public class BattlePvM  {
    private static final long serialVersionUID = 1L;
    private HumanTrainer jugador;
    private AITrainer maquina;
    private Trainer turnoActual;
    private transient Timer turnTimer;
    private boolean esperandoAccion;
    private static final int TIEMPO_TURNO = 20;
    private transient BattleGUIListener listener;
    private static final Movimiento FORCEJEO = new Forcejeo();
    private boolean cambioForzado = false;
    private static final Logger LOGGER = Logger.getLogger(Battle.class.getName());

    public BattlePvM(HumanTrainer jugador, AITrainer maquina) {
        this.jugador = jugador;
        this.maquina = maquina;
        // Decide aleatoriamente quién inicia
        this.turnoActual = (Math.random() < 0.5) ? jugador : maquina;
        // ASIGNA EL LISTENER A AMBOS ENTRENADORES
        this.jugador.setListener(new TrainerActionListener(this));
        this.maquina.setListener(new TrainerActionListener(this));
    }

    public static BattlePvM setupBattle(    
        List<String> nombresEquipoJugador,
        List<String> nombresEquipoMaquina,
        Map<String, Integer> itemsJugador,
        Map<String, Integer> itemsMaquina,
        String nombreEntrenadorMaquina
    ) throws POOBkemonException {
        HumanTrainer jugador = new HumanTrainer("Jugador", "Azul");
        AITrainer maquina;
        System.out.println("Nombre Entrenador Maquina: " + nombreEntrenadorMaquina);
        switch (nombreEntrenadorMaquina) {
            case "defensiveTrainer":
            maquina = new DefensiveTrainer("Maquina", "Rojo");
                break;
        case "attackingTrainer":
            maquina = new AttackingTrainer("Maquina", "Rojo");
                break;
        case "chaningTrainer":
            maquina = new ChangingTrainer("Maquina", "Rojo");
                break;
        case "expertTrainer":
            maquina = new ExpertTrainer("Maquina", "Rojo");
                break;
        default:
            maquina = new AttackingTrainer("Maquina", "Rojo");
                break;
        }

        try {
            for (String nombre : nombresEquipoJugador) {
                Pokemon p = BattleFactory.crearPokemon(nombre, jugador);
                jugador.agregarPokemon(p);
            }
            for (String nombre : nombresEquipoMaquina) {
                Pokemon p = BattleFactory.crearPokemon(nombre, maquina);
                maquina.agregarPokemon(p);
            }
            BattleFactory.agregarItems(jugador, itemsJugador);
            BattleFactory.agregarItems(maquina, itemsMaquina);

            return new BattlePvM(jugador, maquina);
        } catch (POOBkemonException e) {
            throw e;
        } catch (Exception e) {
            throw new POOBkemonException(POOBkemonException.ERROR_CREAR_BATALLA, e);
        }
    }

    public HumanTrainer getEntrenador1() {
        return jugador;
    }

    public AITrainer getEntrenador2() {
        return maquina;
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

    public Trainer getTurnoActual() {
        return turnoActual;
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
        turnoActual = (turnoActual == jugador) ? maquina : jugador;
        iniciarTurno();
    }

        /**
     * Inicia un nuevo turno de batalla
     */
    public void iniciarTurno() {
        if (jugador.estaDerrotado() || maquina.estaDerrotado()) {
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
    
        // Si es el turno de la máquina, realiza la acción automáticamente
        if (turnoActual == maquina) {
            realizarTurnoMaquina();
        }
    }

    /**
     * Hace que la máquina (IA) realice su turno automáticamente.
     */
    private void realizarTurnoMaquina() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!esperandoAccion) return;
                // IA decide y ejecuta su acción
                maquina.decidirAccion(BattlePvM.this, jugador);
                finalizarTurno();
            }
        }, 2000); // Espera 2 segundos para simular "pensar"
    }

    public void manejarPokemonDebilitado() {
        cambioForzado = true;
        cancelarTemporizador();

        if (listener != null) {
                listener.onPokemonDebilitado(turnoActual);
        }

        if (turnoActual.estaDerrotado()) {
            finalizarBatalla();
            return;
        }

        if (turnoActual == maquina) {
            for (int i = 0; i < maquina.getEquipo().size(); i++) {
                Pokemon p = maquina.getEquipo().get(i);
                if (!p.estaDebilitado() && p != maquina.getPokemonActivo()) {
                    String msg = maquina.cambiarPokemon(i);
                    if (listener != null) {
                        listener.onPokemonChanged(maquina, msg);
                    }
                    cambioForzado = false; // Solución previa
                    iniciarTurno();
                    return;
                }
            }
        }
    }

    /**
     * Finaliza la batalla y determina al ganador
     */
    public void finalizarBatalla() {
        cancelarTemporizador();
        Trainer ganador = jugador.estaDerrotado() ? maquina : jugador;
        if (listener != null) {
            listener.onBattleEnded(ganador);
        }
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
     * Guarda el estado actual de la batalla en un archivo
     * @param filePath Ruta del archivo donde guardar
     * @throws IOException Si ocurre un error de E/S
     */
    public void guardarPartida(String filePath) throws POOBkemonException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(filePath)))) {
            oos.writeObject(this);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar la partida", e);
            throw new POOBkemonException(POOBkemonException.ERROR_GUARDAR, e);
        }
    }

    public static BattlePvM cargarPartida(String filePath) throws POOBkemonException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            BattlePvM batalla = (BattlePvM) ois.readObject();
            batalla.jugador.setListener(new TrainerActionListener(batalla));
            batalla.maquina.setListener(new TrainerActionListener(batalla));
            return batalla;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la partida", e);
            throw new POOBkemonException(POOBkemonException.ERROR_CARGAR, e);
        }
    }

    public void iniciar() {
        if (listener != null) {
            listener.onBattleStarted();
        }
        iniciarTurno();
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

        /**
     * Procesa la selección de un movimiento por parte del jugador
     * 
     * @param indiceMovimiento Índice del movimiento seleccionado
     */
    public String movimientoSeleccionado(int indiceMovimiento) throws POOBkemonException {
        if (!esperandoAccion || isPaused()) {
            throw new POOBkemonException(POOBkemonException.ERROR_MOVIMIENTO_TURNO);
        }
        if (indiceMovimiento < 0 || indiceMovimiento >= turnoActual.getPokemonActivo().getMovimientos().size()) {
            throw new POOBkemonException(POOBkemonException.ERROR_MOVIMIENTO_INDICE);
        }
        try {
            Trainer oponente = (turnoActual == jugador) ? maquina : jugador;
            String message = turnoActual.onAttackSelected(indiceMovimiento, oponente);
            if (listener != null) {
                listener.onMoveUsed(turnoActual, message);
            }
            // Si el turno fue del jugador humano, forzamos el avance de turno aquí
            if (turnoActual == jugador) {
                finalizarTurno();
            }
            return message;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al usar movimiento", e);
            throw new POOBkemonException("Error inesperado al usar movimiento", e);
        }
    }

        /**
     * Procesa la selección de cambio de Pokémon por parte del jugador
     * 
     * @param indicePokemon Índice del Pokémon seleccionado
     */
    
    public String cambioPokemonSeleccionado(int indicePokemon) throws POOBkemonException {
        if ((!esperandoAccion && !cambioForzado) || isPaused()) {
            throw new POOBkemonException(POOBkemonException.ERROR_CAMBIO_POKEMON_TURNO);
        }
        try {
            if (indicePokemon < 0 || indicePokemon >= turnoActual.getEquipo().size()) {
                throw new POOBkemonException(POOBkemonException.ERROR_CAMBIO_POKEMON_INDICE);
            }
            Pokemon seleccionado = turnoActual.getEquipo().get(indicePokemon);
            if (seleccionado.estaDebilitado()) {
                throw new POOBkemonException(String.format(POOBkemonException.ERROR_POKEMON_DEBILITADO, seleccionado.getNombre()));
            }
            if (seleccionado == turnoActual.getPokemonActivo()) {
                throw new POOBkemonException(String.format(POOBkemonException.ERROR_POKEMON_ACTIVO, seleccionado.getNombre()));
            }
            String message = turnoActual.cambiarPokemon(indicePokemon);
            if (listener != null) {
                listener.onPokemonChanged(turnoActual, message);
            }
            if (cambioForzado) {
                cambioForzado = false;
            } else if (turnoActual == jugador) {
                finalizarTurno();
            }
            return message;
        } catch (POOBkemonException e) {
            LOGGER.log(Level.WARNING, "Error al cambiar Pokémon: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cambiar Pokémon", e);
            throw new POOBkemonException("Error inesperado al cambiar Pokémon", e);
        }
    }


    /**
     * Procesa la selección de uso de ítem por parte del jugador
     * 
     * @param indiceItem Índice del ítem seleccionado
     */
    public String itemSeleccionado(int indiceItem) throws POOBkemonException {
        if (!esperandoAccion || isPaused()) {
            throw new POOBkemonException(POOBkemonException.ERROR_ITEM_TURNO);
        }
        if (indiceItem < 0 || indiceItem >= turnoActual.getItems().size()) {
            throw new POOBkemonException(POOBkemonException.ERROR_ITEM_INDICE);
        }
        try {
            String message = turnoActual.onItemSelected(indiceItem);
            if (listener != null) {
                listener.onMoveUsed(turnoActual, message);
            }
            if (turnoActual == jugador) {
                finalizarTurno();
            }
            return message;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al usar ítem", e);
            throw new POOBkemonException("Error inesperado al usar ítem", e);
        }
    }

    public boolean isPaused() {
        return  false;
    }

    private static class TrainerActionListener implements TrainerListener {
        private static final long serialVersionUID = 1L;
        private final BattlePvM battle;
        public TrainerActionListener(BattlePvM battle) {
            this.battle = battle;
        }
        @Override
        public void onActionPerformed() {
            battle.finalizarTurno();
        }
    }

    public boolean isCambioForzado() {
        return cambioForzado;
    }

    public BattleGUIListener getListener() {
        return listener;
    }

 
}
interface TrainerListener extends Serializable {
    void onActionPerformed();
}