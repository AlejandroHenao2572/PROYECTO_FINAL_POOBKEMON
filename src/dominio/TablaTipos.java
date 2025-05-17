package dominio;

import java.util.HashMap;
import java.util.Map;

public interface TablaTipos {
    static final Map<String, Map<String, Double>> efectividades = new HashMap<>();
    static boolean modoTest = false;

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

    static void agregarEfectividad(String atacante, String defensor, double multiplicador) {
        efectividades.computeIfAbsent(atacante, k -> new HashMap<>()).put(defensor, multiplicador);
    }

    /**
     * Devuelve el multiplicador de efectividad entre tipos
     */
    default double getMultiplicador(String tipoAtacante, String tipoDefensor) {
        if (modoTest) return 1.0;
        return efectividades.getOrDefault(tipoAtacante, new HashMap<>())
                            .getOrDefault(tipoDefensor, 1.0);
    }

    static void setModoTest(boolean test) {
        // No se puede modificar variable static en interfaz directamente
        // En un caso real se movería a una clase util o singleton
        throw new UnsupportedOperationException("No se puede cambiar el modoTest desde interfaz.");
    }
}
