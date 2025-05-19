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
    public static final String ERROR_CANTIDAD_NO_VALIDA = "La cantidad debe ser positiva.";
    public static final String ERROR_NIVEL_NO_VALIDO = "El nivel debe ser mayor que cero.";
    public static final String ERROR_MOVIMIENTOS_NO_VALIDOS = "La lista de movimientos no puede ser nula o vacía.";
    public static final String ERROR_ESTADISTICA_NO_EXISTE = "La estadística '%s' no existe.";
    public static final String ERROR_CREAR_BATALLA = "No se pudo crear la batalla";

    public POOBkemonException(String message) {
        super(message);
    }
    public POOBkemonException(String message, Throwable cause) {
        super(message, cause);
    }
}