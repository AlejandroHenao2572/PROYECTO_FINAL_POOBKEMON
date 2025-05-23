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

/**
 * Clase BattlePvM
 *
 * Gestiona una batalla entre un jugador humano y una maquina (IA).
 * Permite configurar los equipos, items y el tipo de IA de la maquina.
 * Controla el flujo de la batalla, los turnos, el uso de movimientos e items,
 * el cambio de Pokemon y la finalizacion de la batalla.
 * Permite guardar y cargar el estado de la batalla.
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class BattlePvM {
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

    /**
     * Constructor de la batalla PvM
     * @param jugador Entrenador humano
     * @param maquina Entrenador IA
     */
    public BattlePvM(HumanTrainer jugador, AITrainer maquina) {
        this.jugador = jugador;
        this.maquina = maquina;
        // Decide aleatoriamente quien inicia
        this.turnoActual = (Math.random() < 0.5) ? jugador : maquina;
        // Asigna el listener a ambos entrenadores
        this.jugador.setListener(new TrainerActionListener(this));
        this.maquina.setListener(new TrainerActionListener(this));
    }

    /**
     * Configura una nueva batalla PvM con los equipos, items y tipo de IA dados
     * @param nombresEquipoJugador Nombres de los Pokemon del jugador
     * @param nombresEquipoMaquina Nombres de los Pokemon de la maquina
     * @param itemsJugador Items del jugador
     * @param itemsMaquina Items de la maquina
     * @param nombreEntrenadorMaquina Tipo de IA de la maquina
     * @return Instancia de BattlePvM lista para iniciar
     * @throws POOBkemonException Si ocurre un error al crear la batalla
     */
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

    /**
     * Devuelve el entrenador humano
     * @return jugador
     */
    public HumanTrainer getEntrenador1() {
        return jugador;
    }

    /**
     * Devuelve el entrenador IA
     * @return maquina
     */
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

    /**
     * Devuelve el entrenador cuyo turno esta activo
     * @return turnoActual
     */
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
     * Maneja la situacion cuando se agota el tiempo para un turno
     */
    public void tiempoAgotado() {
        if (esperandoAccion) {
            // Penalizacion por tiempo agotado: pierde 1 PP en cada movimiento
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
    
        // Verificar si el Pokemon activo esta debilitado
        if (turnoActual.getPokemonActivo().estaDebilitado()) {
            manejarPokemonDebilitado();
            return;
        }
    
        // Si el Pokemon activo no tiene PP, agregar Forcejeo
        if (turnoActual.getPokemonActivo().sinPP()) {
            turnoActual.getPokemonActivo().getMovimientos().add(FORCEJEO);
        }
    
        esperandoAccion = true;
        iniciarTemporizadorTurno();
    
        if (listener != null) {
            listener.onTurnStarted(turnoActual);
        }
    
        // Si es el turno de la maquina, realiza la accion automaticamente
        if (turnoActual == maquina) {
            realizarTurnoMaquina();
        }
    }

    /**
     * Hace que la maquina (IA) realice su turno automaticamente.
     */
    private void realizarTurnoMaquina() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!esperandoAccion) return;
                // IA decide y ejecuta su accion
                maquina.decidirAccion(BattlePvM.this, jugador);
                finalizarTurno();
            }
        }, 2000); // Espera 2 segundos para simular "pensar"
    }

    /**
     * Maneja el caso en que el Pokemon activo esta debilitado
     */
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
                    cambioForzado = false;
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
     * @param listener Objeto que recibira los eventos de la batalla
     */
    public void setListener(BattleGUIListener listener) {
        this.listener = listener;
    }

    /**
     * Guarda el estado actual de la batalla en un archivo
     * @param filePath Ruta del archivo donde guardar
     * @throws POOBkemonException Si ocurre un error al guardar
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

    /**
     * Carga una batalla desde un archivo
     * @param filePath Ruta del archivo a cargar
     * @return Instancia de BattlePvM cargada
     * @throws POOBkemonException Si ocurre un error al cargar
     */
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
     * Procesa el uso de un item de revivir
     * @param itemIndex Indice del item en la mochila
     * @param pokemonIndex Indice del Pokemon a revivir
     */
    public void usarItemRevivir(int itemIndex, int pokemonIndex) {
        if (!esperandoAccion) return;
        
        try {
            // Validar indices
            if (itemIndex < 0 || itemIndex >= turnoActual.getItems().size() ||
                pokemonIndex < 0 || pokemonIndex >= turnoActual.getEquipo().size()) {
                return;
            }
            
            Item item = turnoActual.getItems().get(itemIndex);
            Pokemon pokemon = turnoActual.getEquipo().get(pokemonIndex);
            
            // Verificar que el item sea de revivir y el Pokemon este debilitado
            if (item instanceof Revive && pokemon.estaDebilitado()) {
                // Usar el item
                item.usarEn(pokemon);
                
                // Eliminar el item de la mochila
                turnoActual.getItems().remove(itemIndex);
                
                // Notificar a la interfaz
                if (listener != null) {
                    listener.onPokemonRevivido(turnoActual, pokemon);
                }
                
                // Si revivimos al Pokemon activo, continuar el turno
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
     * Procesa la seleccion de un movimiento por parte del jugador
     * @param indiceMovimiento Indice del movimiento seleccionado
     * @return Mensaje descriptivo de la accion
     * @throws POOBkemonException Si ocurre un error en la seleccion
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
            // Si el turno fue del jugador humano, forzamos el avance de turno aqui
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
     * Procesa la seleccion de cambio de Pokemon por parte del jugador
     * @param indicePokemon Indice del Pokemon seleccionado
     * @return Mensaje descriptivo del cambio
     * @throws POOBkemonException Si ocurre un error en la seleccion
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
            LOGGER.log(Level.WARNING, "Error al cambiar Pokemon: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cambiar Pokemon", e);
            throw new POOBkemonException("Error inesperado al cambiar Pokemon", e);
        }
    }

    /**
     * Procesa la seleccion de uso de item por parte del jugador
     * @param indiceItem Indice del item seleccionado
     * @return Mensaje descriptivo del uso
     * @throws POOBkemonException Si ocurre un error en la seleccion
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
            LOGGER.log(Level.SEVERE, "Error inesperado al usar item", e);
            throw new POOBkemonException("Error inesperado al usar item", e);
        }
    }

    /**
     * Indica si la batalla esta pausada (siempre falso en PvM)
     * @return false
     */
    public boolean isPaused() {
        return  false;
    }

    /**
     * Listener interno para acciones de los entrenadores
     */
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

    /**
     * Indica si el cambio de Pokemon es forzado
     * @return true si el cambio es forzado
     */
    public boolean isCambioForzado() {
        return cambioForzado;
    }

    /**
     * Devuelve el listener de la batalla
     * @return listener
     */
    public BattleGUIListener getListener() {
        return listener;
    }
}

/**
 * Interfaz para escuchar acciones de los entrenadores
 */
interface TrainerListener extends Serializable {
    void onActionPerformed();
}