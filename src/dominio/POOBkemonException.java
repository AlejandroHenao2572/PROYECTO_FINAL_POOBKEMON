package dominio;

public class POOBkemonException extends Exception {
    // Mensajes de error comunes como constantes
    public static final String ERROR_CAMBIO_POKEMON_TURNO = "No puedes cambiar Pokémon ahora";
    public static final String ERROR_CAMBIO_POKEMON_INDICE = "Selección de Pokémon no válida";
    public static final String ERROR_POKEMON_DEBILITADO = "¡%s está debilitado!";
    public static final String ERROR_POKEMON_ACTIVO = "¡%s ya está en combate!";
    public static final String ERROR_MOVIMIENTO_TURNO = "No es posible atacar en este momento.";
    public static final String ERROR_MOVIMIENTO_INDICE = "Movimiento seleccionado no válido.";
    public static final String ERROR_ITEM_TURNO = "No es posible usar un ítem en este momento.";
    public static final String ERROR_ITEM_INDICE = "Ítem seleccionado no válido.";
    public static final String ERROR_GUARDAR = "No se pudo guardar la partida";
    public static final String ERROR_CARGAR = "No se pudo cargar la partida";
    public static final String ERROR_ITEM_REVIVIR = "No se puede usar el item de revivir en este contexto.";

    public POOBkemonException(String message) {
        super(message);
    }
    public POOBkemonException(String message, Throwable cause) {
        super(message, cause);
    }
}