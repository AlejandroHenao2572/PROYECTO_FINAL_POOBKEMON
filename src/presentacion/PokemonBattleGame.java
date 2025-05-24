package presentacion;

import dominio.*;
import java.util.*;
import javax.swing.SwingUtilities;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Clase principal que gestiona el inicio de las batallas Pokemon
 * Contiene metodos para configurar los entrenadores, sus Pokemon, sus movimientos,
 * sus estadisticas y los items que poseen. Inicia la interfaz grafica del combate.
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class PokemonBattleGame {
    // Logger para registrar eventos y errores
    private static final Logger LOGGER = Logger.getLogger(PokemonBattleGame.class.getName());

    /**
     * Inicia una batalla Pokemon entre dos entrenadores humanos
     * Configura los equipos de Pokemon, movimientos, estadisticas y items
     * Lanza la interfaz grafica del combate
     *
     * @param nombresEquipo1 Lista de nombres de Pokemon para el primer entrenador
     * @param nombresEquipo2 Lista de nombres de Pokemon para el segundo entrenador
     * @param items1  Mapa de nombres de items y cantidades para el primer entrenador
     * @param items2  Mapa de nombres de items y cantidades para el segundo entrenador
     */
    public static void iniciarBatallaPvP(
        List<String> nombresEquipo1,
        List<String> nombresEquipo2,
        Map<String, Integer> items1,
        Map<String, Integer> items2
    ) {
        try {
            Battle battle = Battle.getInstance();
            battle.setUpBattlePvP(nombresEquipo1, nombresEquipo2, items1, items2);
            SwingUtilities.invokeLater(() -> {
                MainWindow mainWindow = new MainWindow(battle.getEntrenador1(), battle.getEntrenador2(), battle);
                mainWindow.setVisible(true);
                mainWindow.startBattle();
            });
        } catch (POOBkemonException e) {
            // Manejo de errores especificos del juego
            LOGGER.log(Level.WARNING, "Error al iniciar la batalla: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error al iniciar batalla", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Manejo de errores inesperados
            LOGGER.log(Level.SEVERE, "Error inesperado al iniciar la batalla: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Error inesperado al iniciar la batalla.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inicia una batalla Jugador vs Maquina (PvM)
     * Configura los equipos y items para el jugador humano y la IA
     * Lanza la interfaz grafica del combate PvM
     *
     * @param nombresEquipoJugador Lista de nombres de Pokemon del jugador humano
     * @param nombresEquipoMaquina Lista de nombres de Pokemon de la maquina
     * @param itemsJugador Mapa de items del jugador humano
     * @param itemsMaquina Mapa de items de la maquina
     * @param nombreEntrenadorMaquina Nombre del entrenador IA
     */
    public static void iniciarBatallaPvM(
        List<String> nombresEquipoJugador,
        List<String> nombresEquipoMaquina,
        Map<String, Integer> itemsJugador,
        Map<String, Integer> itemsMaquina,
        String nombreEntrenadorMaquina
    ) {
        try {
            Battle battle = Battle.getInstance();
            battle.setUpBattlePvM(nombresEquipoJugador, nombresEquipoMaquina, itemsJugador, itemsMaquina, nombreEntrenadorMaquina);
            SwingUtilities.invokeLater(() -> {
                MainWindow mainWindow = new MainWindow(battle.getEntrenador1(), battle.getEntrenador2(), battle);
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

    /**
     * Inicia una batalla Maquina vs Maquina (MvM)
     * Configura los equipos, items y tipo de IA para cada maquina
     * Lanza la interfaz grafica del combate MvM
     *
     * @param nombresEquipoMaquina1 Lista de nombres de Pokemon para la maquina 1
     * @param nombresEquipoMaquina2 Lista de nombres de Pokemon para la maquina 2
     * @param itemsMaquina1 Mapa de items para la maquina 1
     * @param itemsMaquina2 Mapa de items para la maquina 2
     * @param tipoMaquina1 Tipo de IA para la maquina 1
     * @param tipoMaquina2 Tipo de IA para la maquina 2
     */
    public static void iniciarBatallaMvM(
        List<String> nombresEquipoMaquina1,
        List<String> nombresEquipoMaquina2,
        Map<String, Integer> itemsMaquina1,
        Map<String, Integer> itemsMaquina2,
        String tipoMaquina1,
        String tipoMaquina2
    ) {
        try {
            Battle battle = Battle.getInstance();
            battle.setUpBattleMvM(nombresEquipoMaquina1, nombresEquipoMaquina2, itemsMaquina1, itemsMaquina2, tipoMaquina1, tipoMaquina2);
            SwingUtilities.invokeLater(() -> {
                MainWindow mainWindow = new MainWindow(battle.getEntrenador1(), battle.getEntrenador2(),battle);
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
}