package dominio;

/**
 * Clase de excepcion personalizada para el sistema POOBkemon
 * Permite manejar errores especificos del juego usando mensajes constantes
 * Incluye mensajes de error comunes para operaciones de batalla y gestion de objetos
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class POOBkemonException extends Exception {
    // Mensajes de error comunes como constantes
    public static final String ERROR_CAMBIO_POKEMON_TURNO = "No puedes cambiar Pokemon ahora";
    public static final String ERROR_CAMBIO_POKEMON_INDICE = "Seleccion de Pokemon no valida";
    public static final String ERROR_POKEMON_DEBILITADO = "%s esta debilitado";
    public static final String ERROR_POKEMON_ACTIVO = "%s ya esta en combate";
    public static final String ERROR_MOVIMIENTO_TURNO = "No es posible atacar en este momento";
    public static final String ERROR_MOVIMIENTO_INDICE = "Movimiento seleccionado no valido";
    public static final String ERROR_ITEM_TURNO = "No es posible usar un item en este momento";
    public static final String ERROR_ITEM_INDICE = "Item seleccionado no valido";
    public static final String ERROR_GUARDAR = "No se pudo guardar la partida";
    public static final String ERROR_CARGAR = "No se pudo cargar la partida";
    public static final String ERROR_ITEM_REVIVIR = "No se puede usar el item de revivir en este contexto";
    public static final String ERROR_CANTIDAD_NO_VALIDA = "La cantidad debe ser positiva";
    public static final String ERROR_NIVEL_NO_VALIDO = "El nivel debe ser mayor que cero";
    public static final String ERROR_MOVIMIENTOS_NO_VALIDOS = "La lista de movimientos no puede ser nula o vacia";
    public static final String ERROR_ESTADISTICA_NO_EXISTE = "La estadistica %s no existe";
    public static final String ERROR_CREAR_BATALLA = "No se pudo crear la batalla";

    /**
     * Crea una nueva excepcion POOBkemon con un mensaje especifico
     * 
     * @param message Mensaje descriptivo del error
     */
    public POOBkemonException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepcion POOBkemon con un mensaje y una causa
     * 
     * @param message Mensaje descriptivo del error
     * @param cause Causa original de la excepcion
     */
    public POOBkemonException(String message, Throwable cause) {
        super(message, cause);
    }
}