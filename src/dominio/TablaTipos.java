package dominio;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que maneja las efectividades de tipos Pokemon
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class TablaTipos {
    private static final Map<String, Map<String, Double>> efectividades = new HashMap<>();
    private static boolean modoTest = false;

    static {
        // Inicializar tabla de tipos
        inicializarEfectividades();
    }

    private static void inicializarEfectividades() {
        // Efectividades de tipos
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

    private static void agregarEfectividad(String atacante, String defensor, double multiplicador) {
        efectividades.computeIfAbsent(atacante, k -> new HashMap<>()).put(defensor, multiplicador);
    }

    /**
     * Obtiene el multiplicador de efectividad entre tipos
     * @param tipoAtacante Tipo del movimiento atacante
     * @param tipoDefensor Tipo del Pokemon defensor
     * @return Multiplicador de dano (0.0 0.5 1.0 o 2.0)
     */
    public static double getMultiplicador(String tipoAtacante, String tipoDefensor) {
        if (modoTest) return 1.0;
        return efectividades.getOrDefault(tipoAtacante, new HashMap<>())
                            .getOrDefault(tipoDefensor, 1.0);
    }

    public static void setModoTest(boolean test) {
        modoTest = test;
    }
}