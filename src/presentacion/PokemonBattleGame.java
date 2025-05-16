package presentacion;

import dominio.*;
import java.util.*;
import javax.swing.SwingUtilities;


/**
 * Clase principal que gestiona el inicio de las batallas Pokemon
 * Contiene metodos para configurar los entrenadores sus Pokemon sus movimientos
 * sus estadisticas y los items que poseen Inicia la interfaz grafica del combate
 *
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class PokemonBattleGame {
    
    /**
     * Inicia una batalla Pokemon entre dos entrenadores humanos
     * Configura los equipos de Pokemon con sus movimientos y estadisticas
     * agrega los items a los entrenadores e inicia la interfaz grafica del combate
     *
     * @param equipo1 Lista de Pokemon para el primer entrenador
     * @param equipo2 Lista de Pokemon para el segundo entrenador
     * @param items1  Mapa de nombres de items y cantidades para el primer entrenador
     * @param items2  Mapa de nombres de items y cantidades para el segundo entrenador
     */
    public static void iniciarBatalla(
        List<String> nombresEquipo1,
        List<String> nombresEquipo2,
        Map<String, Integer> items1,
        Map<String, Integer> items2
    ) {
    Battle battle = Battle.setupBattle(nombresEquipo1, nombresEquipo2, items1, items2);
    SwingUtilities.invokeLater(() -> {
        MainWindow mainWindow = new MainWindow((HumanTrainer) battle.getEntrenador1(), (HumanTrainer) battle.getEntrenador2());
        mainWindow.setVisible(true);
        mainWindow.startBattle();
    });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PvPNormalSetUp setup = new PvPNormalSetUp();
            setup.setVisible(true);
        });
    }
}