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
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * Esta clase maneja la logica de una batalla entre entrenadores humanos
 * Permite gestionar turnos ataques cambios de pokemon uso de items y eventos de la interfaz
 * Incluye metodos para guardar y cargar el estado de la batalla
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class Battle implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Battle instance;
    private Trainer entrenador1;
    private Trainer entrenador2;
    private Trainer turnoActual;
    private transient Timer turnTimer;
    private boolean esperandoAccion;
    private static final int TIEMPO_TURNO = 20; 
    private boolean cambioForzado = false;
    private static final Logger LOGGER = Logger.getLogger(Battle.class.getName());
    static {
        try {
            java.util.logging.FileHandler fileHandler = new java.util.logging.FileHandler("battle_exceptions.log", true);
            fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("No se pudo inicializar el log de Battle: " + e.getMessage());
        }
    }
    private transient BattleGUIListener listener;

    /**
     * Constructor que inicializa una batalla con dos entrenadores humanos
     * 
     * @param entrenador1 Primer participante de la batalla
     * @param entrenador2 Segundo participante de la batalla
     */
    private Battle() {
    }

    /**
     * Devuelve la instancia de Battle (singleton)
     * 
     * @return Instancia de Battle
     */
    public static Battle getInstance() {
        if (instance == null) {
            instance = new Battle();
        }
        return instance;
    }

    /**
     * Crea el equipo de un entrenador agregando los Pokemon especificados por nombre.
     *
     * @param entrenador El entrenador al que se le agregara el equipo.
     * @param nombresEquipo Lista de nombres de los Pokemon a agregar al equipo.
     * @throws POOBkemonException Si ocurre un error al crear algún Pokemon.
     */
    private void crearEquipo(Trainer entrenador, List<String> nombresEquipo) throws POOBkemonException {
        for (String nombre : nombresEquipo) {
            Pokemon p = BattleFactory.crearPokemon(nombre, entrenador);
            entrenador.agregarPokemon(p);
        }
    }

    /**
     * Agrega los items especificados al entrenador.
     *
     * @param entrenador El entrenador al que se le agregaran los items.
     * @param items Mapa con los nombres de los items y la cantidad de cada uno.
     * @throws POOBkemonException Si ocurre un error al agregar los items.
     */
    private void agregarItems(Trainer entrenador, Map<String, Integer> items)   {
        BattleFactory.agregarItems(entrenador, items);
    }

    /**
     * Crea una instancia de un entrenador controlado por IA segun el tipo especificado.
     *
     * @param tipo Tipo de IA ("defensiveTrainer", "attackingTrainer", "chaningTrainer", "expertTrainer").
     * @param nombre Nombre del entrenador IA.
     * @param color Color asociado al entrenador IA.
     * @return Una instancia de AITrainer del tipo solicitado.
     */
    private AITrainer crearAITrainer(String tipo, String nombre, String color) {
        switch (tipo) {
            case "defensiveTrainer": return new DefensiveTrainer(nombre, color);
            case "attackingTrainer": return new AttackingTrainer(nombre, color);
            case "chaningTrainer":   return new ChangingTrainer(nombre, color);
            case "expertTrainer":    return new ExpertTrainer(nombre, color);
            default:                 return new AttackingTrainer(nombre, color);
        }
    }

    /**
     * Inicializa la batalla con los dos entrenadores dados.
     * Asigna los entrenadores, determina aleatoriamente quien inicia,
     * reinicia el estado de espera de accion y asigna los listeners para eventos de accion.
     *
     * @param t1 Primer entrenador participante de la batalla.
     * @param t2 Segundo entrenador participante de la batalla.
     */
    private void inicializarBatalla(Trainer t1, Trainer t2) {
        this.entrenador1 = t1;
        this.entrenador2 = t2;
        this.turnoActual = lanzarMoneda();
        this.esperandoAccion = false;
        this.entrenador1.setListener(new TrainerActionListener(this));
        this.entrenador2.setListener(new TrainerActionListener(this));
    }

    /**
     * Metodo para crear una batalla con los equipos e items dados
     * 
     * @param nombresEquipo1 Lista de nombres de los pokemon del primer entrenador
     * @param nombresEquipo2 Lista de nombres de los pokemon del segundo entrenador
     * @param items1 Mapa de items para el primer entrenador
     * @param items2 Mapa de items para el segundo entrenador
     * @return Una nueva instancia de Battle
     * @throws POOBkemonException si ocurre un error al crear la batalla
     */
    public void setUpBattlePvP(
            List<String> nombresEquipo1,
            List<String> nombresEquipo2,
            Map<String, Integer> items1,
            Map<String, Integer> items2
    ) throws POOBkemonException {
        HumanTrainer jugador1 = new HumanTrainer("Jugador 1", "Rojo");
        HumanTrainer jugador2 = new HumanTrainer("Jugador 2", "Azul");
        try {
            crearEquipo(jugador1, nombresEquipo1);
            crearEquipo(jugador2, nombresEquipo2);
            agregarItems(jugador1, items1);
            agregarItems(jugador2, items2);
            inicializarBatalla(jugador1, jugador2);
        } catch (POOBkemonException e) {
            LOGGER.log(Level.WARNING, "Error al crear el equipo: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al crear la batalla: " + e.getMessage(), e);
            throw new POOBkemonException(POOBkemonException.ERROR_CREAR_BATALLA, e);
        }
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
    public void setUpBattlePvM(
        List<String> nombresEquipoJugador,
        List<String> nombresEquipoMaquina,
        Map<String, Integer> itemsJugador,
        Map<String, Integer> itemsMaquina,
        String nombreEntrenadorMaquina
    ) throws POOBkemonException {
        HumanTrainer jugador = new HumanTrainer("Jugador", "Azul");
        AITrainer maquina = crearAITrainer(nombreEntrenadorMaquina, "Maquina", "Rojo");
        try {
            crearEquipo(jugador, nombresEquipoJugador);
            crearEquipo(maquina, nombresEquipoMaquina);
            agregarItems(jugador, itemsJugador);
            agregarItems(maquina, itemsMaquina);
            inicializarBatalla(jugador, maquina);
        } catch (POOBkemonException e) {
            throw e;
        } catch (Exception e) {
            throw new POOBkemonException(POOBkemonException.ERROR_CREAR_BATALLA, e);
        }
    }

    /**
     * Configura una nueva batalla MvM con los equipos, items y tipos de IA dados
     * @param nombresEquipoMaquina1 Nombres de los Pokemon de la maquina 1
     * @param nombresEquipoMaquina2 Nombres de los Pokemon de la maquina 2
     * @param itemsMaquina1 Items de la maquina 1
     * @param itemsMaquina2 Items de la maquina 2
     * @param tipoMaquina1 Tipo de IA de la maquina 1
     * @param tipoMaquina2 Tipo de IA de la maquina 2
     * @return Instancia de BattleMvM lista para iniciar
     * @throws POOBkemonException Si ocurre un error al crear la batalla
     */
    public void setUpBattleMvM(
        List<String> nombresEquipoMaquina1,
        List<String> nombresEquipoMaquina2,
        Map<String, Integer> itemsMaquina1,
        Map<String, Integer> itemsMaquina2,
        String tipoMaquina1,
        String tipoMaquina2
    ) throws POOBkemonException {
        AITrainer maquina1 = crearAITrainer(tipoMaquina1, "Maquina", "Rojo");
        AITrainer maquina2 = crearAITrainer(tipoMaquina2, "Maquina", "Rojo");
        try {
            crearEquipo(maquina1, nombresEquipoMaquina1);
            crearEquipo(maquina2, nombresEquipoMaquina2);
            agregarItems(maquina1, itemsMaquina1);
            agregarItems(maquina2, itemsMaquina2);
            inicializarBatalla(maquina1, maquina2);
        } catch (POOBkemonException e) {
            throw e;
        } catch (Exception e) {
            throw new POOBkemonException(POOBkemonException.ERROR_CREAR_BATALLA, e);
        }
    }
    
    /**
     * Determina aleatoriamente que entrenador comienza la batalla
     * 
     * @return El entrenador que inicia el combate
     */
    private Trainer lanzarMoneda() {
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
        // Verificar si el Pokemon activo esta debilitado
        if (turnoActual.getPokemonActivo().estaDebilitado()) {
            manejarPokemonDebilitado();
            return;
        }
        // Resto de la logica normal del turno
        if (turnoActual.getPokemonActivo().sinPP()) {
            turnoActual.getPokemonActivo().getMovimientos().add(new Forcejeo());
        }
        esperandoAccion = true;
        iniciarTemporizadorTurno();
        if (listener != null) {
            listener.onTurnStarted(turnoActual);
        }
        // Si es el turno de la maquina, realiza la accion automaticamente
        if(entrenador2 instanceof AITrainer || entrenador1 instanceof AITrainer) {
            if (turnoActual instanceof AITrainer) {
                realizarTurnoMaquina();
            }
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
                if (turnoActual instanceof AITrainer){
                    Trainer oponente = (turnoActual == entrenador1) ? entrenador2 : entrenador1;
                    ((AITrainer)turnoActual).decidirAccion(Battle.this, oponente);
                    finalizarTurno();
                }
            }
        }, 1500); 
    }
    
    /**
     * Maneja la situacion cuando el pokemon activo esta debilitado
     * Notifica al listener y verifica si el entrenador esta derrotado
     */
    public void manejarPokemonDebilitado() {
        cambioForzado = true;
        cancelarTemporizador();
        
        if (listener != null) {
            listener.onPokemonDebilitado(turnoActual);
        }
        
        if (turnoActual.estaDerrotado()) {
            finalizarBatalla();
        }

        if(entrenador2 instanceof AITrainer || entrenador1 instanceof AITrainer) {
            if (turnoActual instanceof AITrainer) {
                manejarPokemonDebilitadoMaquina();
            }
        }

    }

    /**
     * Hace que la maquina (IA) maneje el pokemon debilitado automaticamente.
     */
    private void manejarPokemonDebilitadoMaquina() {
            for (int i = 0; i < turnoActual.getEquipo().size(); i++) {
                Pokemon p = turnoActual.getEquipo().get(i);
                if (!p.estaDebilitado() && p != turnoActual.getPokemonActivo()) {
                    String msg = turnoActual.cambiarPokemon(i);
                    if (listener != null) {
                        listener.onPokemonChanged(turnoActual, msg);
                    }
                    cambioForzado = false;
                    iniciarTurno();
                    return;
                }
            }
    }
    
    /**
     * Inicia el temporizador para el turno actual
     * Si el tiempo se agota se llama a tiempoAgotado
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
     * Cancela el temporizador del turno actual si existe
     */
    public void cancelarTemporizador() {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer = null;
        }
    }

    /**
     * Maneja la situacion cuando se agota el tiempo para un turno
     * Penaliza al jugador perdiendo 1 PP en cada movimiento
     * Notifica al listener y finaliza el turno
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
     * Cancela el temporizador y cambia el turno
     */
    public void finalizarTurno() {
        esperandoAccion = false;
        cancelarTemporizador();
        cambiarTurno();
    }

    /**
     * Cambia el turno al otro entrenador y llama a iniciarTurno
     */
    public void cambiarTurno() {
        turnoActual = (turnoActual == entrenador1) ? entrenador2 : entrenador1;
        iniciarTurno();
    }

    /**
     * Finaliza la batalla y determina al ganador
     * Notifica al listener el resultado
     */
    public void finalizarBatalla() {
        cancelarTemporizador();
        Trainer ganador = entrenador1.estaDerrotado() ? entrenador2 : entrenador1;
        if (listener != null) {
            listener.onBattleEnded(ganador);
        }
    }

    /**
     * Procesa la seleccion de un movimiento por parte del jugador
     * 
     * @param indiceMovimiento Indice del movimiento seleccionado
     * @return Mensaje del resultado del ataque
     * @throws POOBkemonException si ocurre un error en la seleccion
     */
    public String movimientoSeleccionado(int indiceMovimiento) throws POOBkemonException {
        if (!esperandoAccion || isPaused()) {
            throw new POOBkemonException(POOBkemonException.ERROR_MOVIMIENTO_TURNO);
        }
        if (indiceMovimiento < 0 || indiceMovimiento >= turnoActual.getPokemonActivo().getMovimientos().size()) {
            throw new POOBkemonException(POOBkemonException.ERROR_MOVIMIENTO_INDICE);
        }
        try {
            Trainer oponente = (turnoActual == entrenador1) ? entrenador2 : entrenador1;
            String message = turnoActual.onAttackSelected(indiceMovimiento, oponente);
            if (listener != null) {
                listener.onMoveUsed(turnoActual, message);
            }
            return message;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al usar movimiento", e);
            throw new POOBkemonException("Error inesperado al usar movimiento", e);
        }
    }

    /**
     * Procesa la seleccion de cambio de Pokemon por parte del jugador
     * 
     * @param indicePokemon Indice del Pokemon seleccionado
     * @return Mensaje del resultado del cambio
     * @throws POOBkemonException si ocurre un error en la seleccion
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
                cambiarTurno();
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
     * Procesa la seleccion de uso de item por parte del jugador
     * 
     * @param indiceItem Indice del item seleccionado
     * @return Mensaje del resultado del uso del item
     * @throws POOBkemonException si ocurre un error en la seleccion
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
            finalizarTurno();
            return message;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al usar ítem", e);
            throw new POOBkemonException("Error inesperado al usar ítem", e);
        }
    }


    /**
     * Procesa el uso de un item de revivir
     * 
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
                
                // Si revivimos al Pokemon activo continuar el turno
                if (pokemon == turnoActual.getPokemonActivo()) {
                    esperandoAccion = true;
                    iniciarTemporizadorTurno();
                }
                finalizarTurno();
            }
        } catch (Exception e) {
            System.err.println("Error al usar item de revivir: " + e.getMessage());
        }
    }

    /**
     * Sacrifica el Pokemon activo del entrenador y transfiere su salud a otro Pokemon del mismo equipo.
     * @param entrenador Entrenador que sacrifica
     * @param indiceReceptor Indice del Pokemon que recibira la salud
     * @return Mensaje descriptivo de la accion
     * @throws POOBkemonException Si no es sacrificable o el receptor es invalido
     */
    public String sacrificarPokemon(Trainer entrenador, int indiceReceptor) throws POOBkemonException {
        Pokemon sacrificado = entrenador.getPokemonActivo();
        if (!sacrificado.esSacrificable()) {
            throw new POOBkemonException("El Pokemon no es sacrificable.");
        }
        if (indiceReceptor < 0 || indiceReceptor >= entrenador.getEquipo().size()) {
            throw new POOBkemonException("Indice de receptor invalido.");
        }
        Pokemon receptor = entrenador.getEquipo().get(indiceReceptor);
        if (receptor == sacrificado || receptor.estaDebilitado()) {
            throw new POOBkemonException("No se puede transferir a ese Pokemon.");
        }
        int porcentaje = sacrificado.getPorcentajeSalud();
        receptor.aumentarSaludPorcentaje(porcentaje);
        sacrificado.setPsActual(0); // El sacrificado queda fuera de combate

        // Cambiar el Pokemon activo al receptor
        entrenador.cambiarPokemon(indiceReceptor);

        // Cambiar turno automaticamente
        finalizarTurno();

        return sacrificado.getNombre() + " se sacrifico y transfirio " + porcentaje + "% de salud a " + receptor.getNombre() + ".";
    }


    /**
     * Devuelve el primer entrenador humano de la batalla
     * 
     * @return entrenador1 primer entrenador humano
     */
    public Trainer getEntrenador1() {
        return entrenador1;
    }

    /**
     * Devuelve el segundo entrenador humano de la batalla
     * 
     * @return entrenador2 segundo entrenador humano
     */
    public Trainer getEntrenador2() {
        return entrenador2;
    }

    /**
     * Devuelve el entrenador cuyo turno esta activo actualmente
     * 
     * @return turnoActual entrenador que tiene el turno
     */
    public Trainer getTurnoActual() {
        return turnoActual;
    }
    
    /**
     * Indica si el cambio de pokemon es forzado por debilitamiento
     * 
     * @return true si el cambio es forzado false en caso contrario
     */
    public boolean isCambioForzado() {
        return cambioForzado;
    }


  
    /**
     * Indica si la batalla esta pausada
     * 
     * @return false si la batalla no esta pausada
     */
    public boolean isPaused() {
        return  false;
    }

    /**
     * Guarda el estado actual de la batalla en un archivo
     * 
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
     * 
     * @param filePath Ruta del archivo a cargar
     * @return La batalla cargada
     * @throws POOBkemonException Si ocurre un error al cargar
     */
    public static Battle cargarPartida(String filePath) throws POOBkemonException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Battle batalla = (Battle) ois.readObject();
            batalla.entrenador1.setListener(new TrainerActionListener(batalla));
            batalla.entrenador2.setListener(new TrainerActionListener(batalla));
            return batalla;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la partida", e);
            throw new POOBkemonException(POOBkemonException.ERROR_CARGAR, e);
        }
    }
    
    /**
     * Establece el primer entrenador de la batalla
     * 
     * @param entrenador1 Entrenador a establecer
     */
    public void setEntrenador1(HumanTrainer entrenador1) {
        this.entrenador1 = entrenador1;
    }

    /**
     * Establece el segundo entrenador de la batalla
     * 
     * @param entrenador2 Entrenador a establecer
     */
    public void setEntrenador2(HumanTrainer entrenador2) {
        this.entrenador2 = entrenador2;
    }
    /**
     * Establece el entrenador cuyo turno esta activo
     * 
     * @param turnoActual Entrenador a establecer
     */
    public void setTurnoActual(Trainer turnoActual) {
        this.turnoActual = turnoActual;
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
     * Devuelve el listener de la batalla
     * 
     * @return listener objeto que recibe los eventos de la batalla
     */
    public BattleGUIListener getListener() {
        return listener;
    }

    /**
     * Clase interna para escuchar acciones de los entrenadores y notificar a la batalla
     */
    private static class TrainerActionListener implements TrainerListener {
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
 * Interfaz para notificar a Battle cuando un entrenador completa su accion
 */
interface TrainerListener extends Serializable {
    void onActionPerformed();
}

