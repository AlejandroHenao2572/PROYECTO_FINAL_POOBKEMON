package presentacion;

import dominio.*;
import java.util.*;
import javax.swing.SwingUtilities;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

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
    private static final Logger LOGGER = Logger.getLogger(PokemonBattleGame.class.getName());
    /**
     * 
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
        try {
            Battle battle = Battle.setupBattle(nombresEquipo1, nombresEquipo2, items1, items2);
            SwingUtilities.invokeLater(() -> {
                MainWindow mainWindow = new MainWindow((HumanTrainer) battle.getEntrenador1(), (HumanTrainer) battle.getEntrenador2());
                mainWindow.setVisible(true);
                mainWindow.startBattle();
            });
        } catch (POOBkemonException e) {
            LOGGER.log(Level.WARNING, "Error al iniciar la batalla: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error al iniciar batalla", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al iniciar la batalla: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Error inesperado al iniciar la batalla.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TablaTipos.inicializarEfectividades();
            PvPNormalSetUp setup = new PvPNormalSetUp();
            setup.setVisible(true);
        });
    }
}