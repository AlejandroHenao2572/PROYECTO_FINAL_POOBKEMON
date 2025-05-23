package dominio;

import java.util.HashMap;
import java.util.Map;

/**
 * Interfaz que define la tabla de efectividades entre tipos de Pokemon
 * Permite consultar el multiplicador de dano segun el tipo atacante y el tipo defensor
 * Incluye metodos para inicializar y agregar relaciones de efectividad
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public interface TablaTipos {
    static final Map<String, Map<String, Double>> efectividades = new HashMap<>();

     /**
     * Inicializa la tabla de efectividades con relaciones predefinidas entre tipos
     * Debe llamarse una vez antes de usar la tabla para asegurar que los datos esten cargados
     */
    static void inicializarEfectividades() {
        agregarEfectividad("Normal", "Roca", 0.5);
        agregarEfectividad("Normal", "Fantasma", 0.0);
        agregarEfectividad("Fuego", "Fuego", 0.5);
        agregarEfectividad("Fuego", "Agua", 0.5);
        agregarEfectividad("Fuego", "Planta", 2.0);
        agregarEfectividad("Fuego", "Hielo", 2.0);
        agregarEfectividad("Fuego", "Bicho", 2.0);
        agregarEfectividad("Fuego", "Roca", 0.5);
        agregarEfectividad("Fuego", "Dragon", 0.5);
        agregarEfectividad("Agua", "Fuego", 2.0);
        agregarEfectividad("Agua", "Agua", 0.5);
        agregarEfectividad("Agua", "Planta", 0.5);
        agregarEfectividad("Agua", "Tierra", 2.0);
        agregarEfectividad("Agua", "Roca", 2.0);
        agregarEfectividad("Planta", "Fuego", 0.5);
        agregarEfectividad("Planta", "Agua", 2.0);
        agregarEfectividad("Planta", "Planta", 0.5);
        agregarEfectividad("Planta", "Volador", 0.5);
        agregarEfectividad("Planta", "Bicho", 0.5);
        agregarEfectividad("Planta", "Veneno", 0.5);
        agregarEfectividad("Planta", "Roca", 2.0);
        agregarEfectividad("Planta", "Tierra", 2.0);
        agregarEfectividad("Electrico", "Agua", 2.0);
        agregarEfectividad("Electrico", "Electrico", 0.5);
        agregarEfectividad("Electrico", "Planta", 0.5);
        agregarEfectividad("Electrico", "Tierra", 0.0);
        agregarEfectividad("Electrico", "Volador", 2.0);
        agregarEfectividad("Tierra", "Fuego", 2.0);
        agregarEfectividad("Tierra", "Planta", 0.5);
        agregarEfectividad("Tierra", "Electrico", 2.0);
        agregarEfectividad("Tierra", "Volador", 0.0);
        agregarEfectividad("Tierra", "Bicho", 0.5);
        agregarEfectividad("Tierra", "Roca", 2.0);
    }

    /**
     * Agrega una relacion de efectividad entre un tipo atacante y un tipo defensor
     * 
     * @param atacante Tipo atacante
     * @param defensor Tipo defensor
     * @param multiplicador Valor de efectividad 
     */
    static void agregarEfectividad(String atacante, String defensor, double multiplicador) {
        efectividades.computeIfAbsent(atacante, k -> new HashMap<>()).put(defensor, multiplicador);
    }

    /**
     * Devuelve el multiplicador de efectividad entre tipos
     * Si no existe una relacion especifica devuelve 1.0 (efectividad normal)
     * 
     * @param tipoAtacante Tipo del atacante
     * @param tipoDefensor Tipo del defensor
     * @return Multiplicador de efectividad
     */
    default double getMultiplicador(String tipoAtacante, String tipoDefensor) {
        return efectividades.getOrDefault(tipoAtacante, new HashMap<>())
                            .getOrDefault(tipoDefensor, 1.0);
    }

}