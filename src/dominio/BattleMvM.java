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

public class BattleMvM  {
    private static final long serialVersionUID = 1L;
    private AITrainer maquina1;
    private AITrainer maquina2;
    private AITrainer turnoActual;
    private transient Timer turnTimer;
    private boolean esperandoAccion;
    private static final int TIEMPO_TURNO = 20;
    private transient BattleGUIListener listener;
    private static final Movimiento FORCEJEO = new Forcejeo();
    private boolean cambioForzado = false;
    private static final Logger LOGGER = Logger.getLogger(Battle.class.getName());

    public BattleMvM(AITrainer maquina1, AITrainer maquina2) {
        this.maquina1 = maquina1;
        this.maquina2 = maquina2;
        this.turnoActual = (Math.random() < 0.5) ? maquina1 : maquina2;
        this.maquina1.setListener(new TrainerActionListener(this));
        this.maquina2.setListener(new TrainerActionListener(this));
    }

    public static BattleMvM setupBattle(
        List<String> nombresEquipoMaquina1,
        List<String> nombresEquipoMaquina2,
        Map<String, Integer> itemsMaquina1,
        Map<String, Integer> itemsMaquina2,
        String tipoMaquina1,
        String tipoMaquina2
    ) throws POOBkemonException {
        AITrainer maquina1;
        switch (tipoMaquina1) {
            case "defensiveTrainer":
            maquina1 = new DefensiveTrainer("Maquina", "Rojo");
                break;
        case "attackingTrainer":
            maquina1 = new AttackingTrainer("Maquina", "Rojo");
                break;
        case "chaningTrainer":
            maquina1 = new ChangingTrainer("Maquina", "Rojo");
                break;
        case "expertTrainer":
            maquina1 = new ExpertTrainer("Maquina", "Rojo");
                break;
        default:
            maquina1 = new AttackingTrainer("Maquina", "Rojo");
                break;
        }

        AITrainer maquina2;
        switch (tipoMaquina1) {
            case "defensiveTrainer":
            maquina2 = new DefensiveTrainer("Maquina", "Rojo");
                break;
        case "attackingTrainer":
            maquina2 = new AttackingTrainer("Maquina", "Rojo");
                break;
        case "chaningTrainer":
            maquina2 = new ChangingTrainer("Maquina", "Rojo");
                break;
        case "expertTrainer":
            maquina2 = new ExpertTrainer("Maquina", "Rojo");
                break;
        default:
            maquina2 = new AttackingTrainer("Maquina", "Rojo");
                break;
        }

        try {
            for (String nombre : nombresEquipoMaquina1) {
                Pokemon p = BattleFactory.crearPokemon(nombre, maquina1);
                maquina1.agregarPokemon(p);
            }
            for (String nombre : nombresEquipoMaquina2) {
                Pokemon p = BattleFactory.crearPokemon(nombre, maquina2);
                maquina2.agregarPokemon(p);
            }
            BattleFactory.agregarItems(maquina1, itemsMaquina1);
            BattleFactory.agregarItems(maquina2, itemsMaquina2);
            
            return new BattleMvM(maquina1, maquina2);

        } catch (POOBkemonException e) {
            throw e;
        } catch (Exception e) {
            throw new POOBkemonException(POOBkemonException.ERROR_CREAR_BATALLA, e);
        }
    }
    
    public AITrainer getEntrenador1() {
        return maquina1;
    }

    public AITrainer getEntrenador2() {
        return maquina2;
    }

    public Trainer getTurnoActual() {
        return turnoActual;
    }
    
    public void cancelarTemporizador() {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer = null;
        }
    }

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

    public void finalizarTurno() {
        esperandoAccion = false;
        cancelarTemporizador();
        // Espera 2 segundos antes de cambiar el turno
        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                cambiarTurno();
            }
        }, 1000);
    }

    /**
     * Cambia el turno al otro entrenador
     */
    public void cambiarTurno() {
        turnoActual = (turnoActual == maquina1) ? maquina2 : maquina1;
        iniciarTurno();
    }

        public void iniciarTurno() {
        if (maquina1.estaDerrotado() || maquina2.estaDerrotado()) {
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
        if (turnoActual instanceof AITrainer) {
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
                AITrainer oponente = (turnoActual == maquina1) ? maquina2 : maquina1;
                turnoActual.decidirAccion(BattleMvM.this, oponente);
                finalizarTurno();
            }
        }, 3000); 
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

        for (int i = 0; i < turnoActual.getEquipo().size(); i++) {
                Pokemon p = turnoActual.getEquipo().get(i);
                if (!p.estaDebilitado() && p != turnoActual.getPokemonActivo()) {
                    String msg = turnoActual.cambiarPokemon(i);
                    if (listener != null) {
                        listener.onPokemonChanged(turnoActual, msg);
                    }
                    cambioForzado = false; // Solución previa
                    iniciarTurno();
                    return;
                }
        }
    }

    /**
     * Finaliza la batalla y determina al ganador
     */
    public void finalizarBatalla() {
        cancelarTemporizador();
        Trainer ganador = maquina1.estaDerrotado() ? maquina2 : maquina1;
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

    public static BattleMvM cargarPartida(String filePath) throws POOBkemonException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            BattleMvM batalla = (BattleMvM) ois.readObject();
            batalla.maquina1.setListener(new TrainerActionListener(batalla));
            batalla.maquina2.setListener(new TrainerActionListener(batalla));
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
            Trainer oponente = (turnoActual == maquina1) ? maquina2 : maquina1;
            String message = turnoActual.onAttackSelected(indiceMovimiento, oponente);
            if (listener != null) {
                listener.onMoveUsed(turnoActual, message);
            }

            finalizarTurno();
  
            return message;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al usar movimiento", e);
            throw new POOBkemonException("Error inesperado al usar movimiento", e);
        }
    }

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
            } else {
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
            } {
            finalizarTurno();
            return message;
        } }catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al usar ítem", e);
            throw new POOBkemonException("Error inesperado al usar ítem", e);
        }
    }

    public boolean isPaused() {
        return  false;
    }

    private static class TrainerActionListener implements TrainerListener {
        private static final long serialVersionUID = 1L;
        private final BattleMvM battle;
        public TrainerActionListener(BattleMvM battle) {
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