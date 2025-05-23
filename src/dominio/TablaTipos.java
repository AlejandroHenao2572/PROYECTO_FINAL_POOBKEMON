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
        // Normal
        agregarEfectividad("Normal", "Roca", 0.5);
        agregarEfectividad("Normal", "Fantasma", 0.0);
        agregarEfectividad("Normal", "Acero", 0.5);

        // Fuego
        agregarEfectividad("Fuego", "Fuego", 0.5);
        agregarEfectividad("Fuego", "Agua", 0.5);
        agregarEfectividad("Fuego", "Planta", 2.0);
        agregarEfectividad("Fuego", "Hielo", 2.0);
        agregarEfectividad("Fuego", "Bicho", 2.0);
        agregarEfectividad("Fuego", "Roca", 0.5);
        agregarEfectividad("Fuego", "Dragon", 0.5);
        agregarEfectividad("Fuego", "Acero", 2.0);

        // Agua
        agregarEfectividad("Agua", "Fuego", 2.0);
        agregarEfectividad("Agua", "Agua", 0.5);
        agregarEfectividad("Agua", "Planta", 0.5);
        agregarEfectividad("Agua", "Tierra", 2.0);
        agregarEfectividad("Agua", "Roca", 2.0);
        agregarEfectividad("Agua", "Dragon", 0.5);

        // Planta
        agregarEfectividad("Planta", "Fuego", 0.5);
        agregarEfectividad("Planta", "Agua", 2.0);
        agregarEfectividad("Planta", "Planta", 0.5);
        agregarEfectividad("Planta", "Volador", 0.5);
        agregarEfectividad("Planta", "Bicho", 0.5);
        agregarEfectividad("Planta", "Veneno", 0.5);
        agregarEfectividad("Planta", "Roca", 2.0);
        agregarEfectividad("Planta", "Tierra", 2.0);
        agregarEfectividad("Planta", "Dragon", 0.5);

        // Electrico
        agregarEfectividad("Electrico", "Agua", 2.0);
        agregarEfectividad("Electrico", "Electrico", 0.5);
        agregarEfectividad("Electrico", "Planta", 0.5);
        agregarEfectividad("Electrico", "Tierra", 0.0);
        agregarEfectividad("Electrico", "Volador", 2.0);
        agregarEfectividad("Electrico", "Dragon", 0.5);

        // Tierra
        agregarEfectividad("Tierra", "Fuego", 2.0);
        agregarEfectividad("Tierra", "Planta", 0.5);
        agregarEfectividad("Tierra", "Electrico", 2.0);
        agregarEfectividad("Tierra", "Volador", 0.0);
        agregarEfectividad("Tierra", "Bicho", 0.5);
        agregarEfectividad("Tierra", "Roca", 2.0);
        agregarEfectividad("Tierra", "Veneno", 2.0);
        agregarEfectividad("Tierra", "Acero", 2.0);

        // Volador
        agregarEfectividad("Volador", "Planta", 2.0);
        agregarEfectividad("Volador", "Electrico", 0.5);
        agregarEfectividad("Volador", "Roca", 0.5);
        agregarEfectividad("Volador", "Lucha", 2.0);
        agregarEfectividad("Volador", "Bicho", 2.0);

        // Lucha
        agregarEfectividad("Lucha", "Normal", 2.0);
        agregarEfectividad("Lucha", "Roca", 2.0);
        agregarEfectividad("Lucha", "Acero", 2.0);
        agregarEfectividad("Lucha", "Hielo", 2.0);
        agregarEfectividad("Lucha", "Siniestro", 2.0);
        agregarEfectividad("Lucha", "Volador", 0.5);
        agregarEfectividad("Lucha", "Veneno", 0.5);
        agregarEfectividad("Lucha", "Bicho", 0.5);
        agregarEfectividad("Lucha", "Fantasma", 0.0);
        agregarEfectividad("Lucha", "Hada", 0.5);

        // Veneno
        agregarEfectividad("Veneno", "Planta", 2.0);
        agregarEfectividad("Veneno", "Hada", 2.0);
        agregarEfectividad("Veneno", "Veneno", 0.5);
        agregarEfectividad("Veneno", "Tierra", 0.5);
        agregarEfectividad("Veneno", "Roca", 0.5);
        agregarEfectividad("Veneno", "Fantasma", 0.5);
        agregarEfectividad("Veneno", "Acero", 0.0);

        // Hielo
        agregarEfectividad("Hielo", "Planta", 2.0);
        agregarEfectividad("Hielo", "Tierra", 2.0);
        agregarEfectividad("Hielo", "Volador", 2.0);
        agregarEfectividad("Hielo", "Dragon", 2.0);
        agregarEfectividad("Hielo", "Fuego", 0.5);
        agregarEfectividad("Hielo", "Agua", 0.5);
        agregarEfectividad("Hielo", "Hielo", 0.5);
        agregarEfectividad("Hielo", "Acero", 0.5);

        // Bicho
        agregarEfectividad("Bicho", "Planta", 2.0);
        agregarEfectividad("Bicho", "Psiquico", 2.0);
        agregarEfectividad("Bicho", "Siniestro", 2.0);
        agregarEfectividad("Bicho", "Fuego", 0.5);
        agregarEfectividad("Bicho", "Lucha", 0.5);
        agregarEfectividad("Bicho", "Veneno", 0.5);
        agregarEfectividad("Bicho", "Volador", 0.5);
        agregarEfectividad("Bicho", "Fantasma", 0.5);
        agregarEfectividad("Bicho", "Acero", 0.5);
        agregarEfectividad("Bicho", "Hada", 0.5);

        // Roca
        agregarEfectividad("Roca", "Fuego", 2.0);
        agregarEfectividad("Roca", "Hielo", 2.0);
        agregarEfectividad("Roca", "Volador", 2.0);
        agregarEfectividad("Roca", "Bicho", 2.0);
        agregarEfectividad("Roca", "Lucha", 0.5);
        agregarEfectividad("Roca", "Tierra", 0.5);
        agregarEfectividad("Roca", "Acero", 0.5);

        // Fantasma
        agregarEfectividad("Fantasma", "Fantasma", 2.0);
        agregarEfectividad("Fantasma", "Psiquico", 2.0);
        agregarEfectividad("Fantasma", "Normal", 0.0);
        agregarEfectividad("Fantasma", "Siniestro", 0.5);

        // Dragón
        agregarEfectividad("Dragon", "Dragon", 2.0);
        agregarEfectividad("Dragon", "Acero", 0.5);
        agregarEfectividad("Dragon", "Hada", 0.0);

        // Siniestro
        agregarEfectividad("Siniestro", "Fantasma", 2.0);
        agregarEfectividad("Siniestro", "Psiquico", 2.0);
        agregarEfectividad("Siniestro", "Lucha", 0.5);
        agregarEfectividad("Siniestro", "Siniestro", 0.5);
        agregarEfectividad("Siniestro", "Hada", 0.5);

        // Acero
        agregarEfectividad("Acero", "Hada", 2.0);
        agregarEfectividad("Acero", "Hielo", 2.0);
        agregarEfectividad("Acero", "Roca", 2.0);
        agregarEfectividad("Acero", "Fuego", 0.5);
        agregarEfectividad("Acero", "Agua", 0.5);
        agregarEfectividad("Acero", "Electrico", 0.5);
        agregarEfectividad("Acero", "Acero", 0.5);

        // Hada
        agregarEfectividad("Hada", "Lucha", 2.0);
        agregarEfectividad("Hada", "Dragón", 2.0);
        agregarEfectividad("Hada", "Siniestro", 2.0);
        agregarEfectividad("Hada", "Fuego", 0.5);
        agregarEfectividad("Hada", "Veneno", 0.5);
        agregarEfectividad("Hada", "Acero", 0.5);

        // Psiquico
        agregarEfectividad("Psiquico", "Lucha", 2.0);
        agregarEfectividad("Psiquico", "Veneno", 2.0);
        agregarEfectividad("Psiquico", "Psiquico", 0.5);
        agregarEfectividad("Psiquico", "Acero", 0.5);
        agregarEfectividad("Psiquico", "Siniestro", 0.0);
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