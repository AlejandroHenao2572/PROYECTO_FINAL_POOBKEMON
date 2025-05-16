package dominio;

/**
 * Interfaz que define los eventos de la interfaz grafica de batalla
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public interface BattleGUIListener {
    
    /**
     * Se ejecuta cuando comienza una batalla
     */
    void onBattleStarted();

    /**
     * Se ejecuta cuando comienza el turno de un entrenador humano
     * 
     * @param trainer Entrenador humano que inicia su turno
     */
    void onTurnStarted(HumanTrainer trainer);

    /**
     * Se ejecuta cuando termina el turno de un entrenador humano
     * 
     * @param trainer Entrenador humano que finaliza su turno
     */
    void onTurnEnded(HumanTrainer trainer);

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

    void onPokemonChanged(Trainer trainer);

    void onPokemonRevivido(Trainer trainer, Pokemon pokemon);
}