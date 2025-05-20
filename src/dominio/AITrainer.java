package dominio;

/**
 * Esta clase abstracta representa un entrenador controlado por IA en el sistema
 * Autores David Patacon y Daniel Hueso
 * Version 1.0
 */
public abstract class AITrainer extends Trainer {
    private TrainerListener listener;
    /**
     * Constructor de la clase AITrainer
     * Inicializa un nuevo entrenador de IA con nombre y color especificos
     * 
     * @param nombre El nombre del entrenador de IA
     * @param color El color asociado al entrenador de IA
     */
    public AITrainer(String nombre, String color) {
        super(nombre, color);
    }

            /**
     * Establece el listener para notificar acciones completadas
     * 
     * @param listener Objeto listener
     */
    public void setListener(TrainerListener listener) {
        this.listener = listener;
    }
    
    public String onAttackSelected(int moveIndex, Trainer oponente){return "";};

    public String onItemSelected(int itemIndex){return "";};
        
    public abstract String decidirAccion(BattlePvM batalla, Trainer oponente);
}
