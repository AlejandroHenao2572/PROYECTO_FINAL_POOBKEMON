package dominio;

/**
 * Interfaz que define los eventos de la interfaz grafica de batalla
 * Permite notificar a la interfaz sobre cambios importantes durante la batalla
 * Incluye eventos para inicio y fin de batalla, turnos, cambios de pokemon, uso de movimientos e items
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public interface BattleGUIListener {
    
    /**
     * Se ejecuta cuando comienza una batalla
     * Notifica a la interfaz que la batalla ha iniciado
     */
    void onBattleStarted();

    /**
     * Se ejecuta cuando comienza el turno de un entrenador humano
     * 
     * @param trainer Entrenador humano que inicia su turno
     */
    void onTurnStarted(Trainer trainer);

    /**
     * Se ejecuta cuando termina el turno de un entrenador humano
     * 
     * @param trainer Entrenador humano que finaliza su turno
     */
    void onTurnEnded(Trainer trainer);

    /**
     * Se ejecuta cuando un pokemon es debilitado
     * 
     * @param trainer Entrenador cuyo pokemon fue debilitado
     */
    void onPokemonDebilitado(Trainer trainer);

    /**
     * Se ejecuta cuando termina la batalla
     * 
     * @param winner Entrenador que gano la batalla
     */
    void onBattleEnded(Trainer winner);

    /**
     * Se ejecuta cuando un entrenador cambia de pokemon
     * 
     * @param trainer Entrenador que realiza el cambio
     * @param message Mensaje descriptivo del cambio
     */
    void onPokemonChanged(Trainer trainer, String message);

    /**
     * Se ejecuta cuando un pokemon es revivido con un item
     * 
     * @param trainer Entrenador que revive al pokemon
     * @param pokemon Pokemon que fue revivido
     */
    void onPokemonRevivido(Trainer trainer, Pokemon pokemon);

    /**
     * Se ejecuta cuando un entrenador usa un movimiento
     * 
     * @param trainer Entrenador que usa el movimiento
     * @param result Mensaje descriptivo del resultado del movimiento
     */
    void onMoveUsed(Trainer trainer, String result);

    /**
     * Se ejecuta cuando un entrenador usa un item
     * 
     * @param trainer Entrenador que usa el item
     * @param result Mensaje descriptivo del resultado del item
     */
    void onItemUsed(Trainer trainer, String result);
}