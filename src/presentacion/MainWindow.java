package presentacion;

import dominio.*;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements BattleGUIListener {
    private BattlePanel battlePanel;
    private ActionPanel actionPanel;
    private Battle battle;
    private JDialog messageDialog;

    public MainWindow(HumanTrainer player, HumanTrainer enemy) {
        setTitle("Pokémon Esmeralda - Combate");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLayout(new BorderLayout());
        
        // Cargar fuente Pokémon
        try {
            Font pokemonFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getClassLoader().getResourceAsStream("graficos/PokemonGB.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (Exception e) {
            System.err.println("Error cargando fuente Pokémon: " + e.getMessage());
        }

        this.battle = new Battle(player, enemy);
        this.battle.setListener(this);
        
        this.battlePanel = new BattlePanel(player, enemy);
        this.actionPanel = new ActionPanel(this, player);
        
        add(battlePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Configurar diálogo de mensajes
        messageDialog = new JDialog(this, "", false);
        messageDialog.setUndecorated(true);
        messageDialog.setSize(300, 100);
        messageDialog.setLocationRelativeTo(this);
        
        centerWindow();
        setVisible(true);
    }

    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, 
                   (screenSize.height - getHeight()) / 2);
    }

    public void startBattle() {
        battle.iniciar();
    }

    public void updateUI() {
        battlePanel.updatePokemonStats();
        actionPanel.setCurrentPlayer(battle.getTurnoActual());
        repaint();
    }

    @Override
    public void onBattleStarted() {
        updateUI();
    }

    @Override
    public void onTurnStarted(HumanTrainer trainer) {
        actionPanel.setCurrentPlayer(trainer);
        updateUI();
        actionPanel.addBattleText("¿Qué debería hacer " + trainer.getPokemonActivo().getNombre() + "?");
    }

    @Override
    public void onTurnEnded(HumanTrainer trainer) {
    }

    @Override
    public void onPokemonDebilitado(Trainer trainer) {
        actionPanel.addBattleText(trainer.getPokemonActivo().getNombre() + " se ha debilitado!, Selecciona otro pokemon");
    }

    @Override
    public void onBattleEnded(Trainer winner) {
        showMessage(winner.getNombre() + " ha ganado la batalla!");
        
        Object[] options = {"Nueva Batalla", "Salir"};
        int choice = JOptionPane.showOptionDialog(this,
            "¿Qué deseas hacer?",
            "Batalla terminada",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            dispose();
            //new PokemonBattleGame().startNewBattle();
        } else {
            System.exit(0);
        }
    }

    public void attackSelected(int moveIndex) {
        battle.movimientoSeleccionado(moveIndex);
    }

    public void switchPokemonSelected(int pokemonIndex) {
        battle.cambioPokemonSeleccionado(pokemonIndex);
    }

    public void useItemSelected(int itemIndex) {
        battle.itemSeleccionado(itemIndex);
    }

    public void runAway() {
        actionPanel.addBattleText("¡Has huido del combate!");
        showMessage("Escapaste con éxito");
        System.exit(0);
    }

    private void showMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(this, "");
        
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        dialog.setVisible(true);
    }

    public Battle getBattle() {
        return battle;
    }
}