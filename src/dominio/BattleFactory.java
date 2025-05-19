package dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BattleFactory implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BattleFactory.class.getName());
    
    public static Pokemon crearPokemon(String nombre, Trainer entrenador) throws POOBkemonException {
        try {
            Pokemon p = new Pokemon(nombre, "Normal", null, 100, 100, 100, 100, 100, 100, new ArrayList<>());
            configurarEstadisticas(p);
            p.setMovimientos(obtenerMovimientosParaPokemon(nombre));
            return p;
        } catch (POOBkemonException e) {
            LOGGER.log(Level.WARNING, "Error al crear el Pokémon: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al crear el Pokémon: " + e.getMessage(), e);
            throw new POOBkemonException("Error inesperado al crear el Pokémon: " + e.getMessage(), e);
        }
    }

    public static void agregarItems(Trainer trainer, Map<String, Integer> items) {
        items.forEach((nombre, cantidad) -> {
            for (int i = 0; i < cantidad; i++) {
                switch(nombre) {
                    case "Potion" -> trainer.agregarItem(new Potion());
                    case "SuperPotion" -> trainer.agregarItem(new SuperPotion());
                    case "HyperPotion" -> trainer.agregarItem(new HyperPotion());
                    case "Revive" -> trainer.agregarItem(new Revive());
                }
            }
        });
    }

    /**
     * Obtiene una lista de movimientos predefinidos para un Pokemon especifico
     * basandose en su nombre
     *
     * @param nombre El nombre del Pokemon para el que se obtendran los movimientos
     * @return Una lista de objetos Movimiento
     */
    private static ArrayList<Movimiento> obtenerMovimientosParaPokemon(String nombre) {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        
        switch(nombre.toLowerCase()) {

    case "charizard":
        movimientos.add(new MovimientoEspecial("Lanzallamas", "Fuego", 90, 100, 15));
        movimientos.add(new MovimientoFisico("Garra Dragón", "Dragón", 80, 100, 15));
        movimientos.add(new MovimientoFisico("Giro Fuego", "Fuego", 60, 100, 25));
        movimientos.add(new MovimientoEstado("Danza Dragón", "Dragón", 0, 100, "Aumenta ataque"));
        break;

    case "torkoal":
        movimientos.add(new MovimientoEspecial("Llamarada", "Fuego", 120, 85, 5));
        movimientos.add(new MovimientoEspecial("Rayo Solar", "Planta", 120, 100, 10));
        movimientos.add(new MovimientoEstado("Pantalla de Humo", "Normal", 0, 100, "Aumenta defensa especial"));
        movimientos.add(new MovimientoFisico("Giro Fuego", "Fuego", 60, 100, 25));
        break;

    case "blastoise":
        movimientos.add(new MovimientoEspecial("Pistola Agua", "Agua", 40, 100, 25));
        movimientos.add(new MovimientoFisico("Cabeza de Hierro", "Acero", 80, 100, 15));
        movimientos.add(new MovimientoEspecial("Hidro Bomba", "Agua", 110, 80, 5));
        movimientos.add(new MovimientoEstado("Reflejo", "Psíquico", 0, 100, "Aumenta defensa"));
        break;

    case "milotic":
        movimientos.add(new MovimientoEspecial("Hidro Bomba", "Agua", 110, 80, 5));
        movimientos.add(new MovimientoEstado("Recuperación", "Normal", 0, 100, "Restaura HP"));
        movimientos.add(new MovimientoEspecial("Rayo Hielo", "Hielo", 90, 100, 10));
        movimientos.add(new MovimientoEstado("Danza Lluvia", "Agua", 0, 100, "Aumenta ataque especial"));
        break;

    case "venusaur":
        movimientos.add(new MovimientoEspecial("Rayo Solar", "Planta", 120, 100, 10));
        movimientos.add(new MovimientoEspecial("Bomba Lodo", "Veneno", 65, 100, 10));
        movimientos.add(new MovimientoEstado("Drenadoras", "Planta", 0, 100, "Restaura HP"));
        movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
        break;

    case "sceptile":
        movimientos.add(new MovimientoFisico("Hoja Aguda", "Planta", 70, 100, 25));
        movimientos.add(new MovimientoEspecial("Rayo Solar", "Planta", 120, 100, 10));
        movimientos.add(new MovimientoEstado("Drenadoras", "Planta", 0, 100, "Restaura HP"));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        break;

    case "raichu":
        movimientos.add(new MovimientoEspecial("Impactrueno", "Eléctrico", 40, 100, 30));
        movimientos.add(new MovimientoFisico("Cola Férrea", "Acero", 65, 95, 20));
        movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
        movimientos.add(new MovimientoEstado("Reflejo", "Psíquico", 0, 100, "Aumenta defensa"));
        break;

    case "manectric":
        movimientos.add(new MovimientoEspecial("Rayo", "Eléctrico", 90, 100, 15));
        movimientos.add(new MovimientoFisico("Mordisco", "Siniestro", 60, 100, 25));
        movimientos.add(new MovimientoEstado("Pantalla Luz", "Eléctrico", 0, 100, "Reduce daño especial"));
        movimientos.add(new MovimientoEspecial("Voltio Cruel", "Eléctrico", 70, 100, 15));
        break;

    case "delibird":
        movimientos.add(new MovimientoFisico("Pico Taladro", "Volador", 80, 100, 20));
        movimientos.add(new MovimientoEspecial("Rayo Hielo", "Hielo", 90, 100, 10));
        movimientos.add(new MovimientoFisico("Golpe Bajo", "Lucha", 70, 95, 20));
        movimientos.add(new MovimientoEstado("Entrega", "Normal", 0, 100, "Pasa objeto"));
        break;

    case "glalie":
        movimientos.add(new MovimientoEspecial("Rayo Hielo", "Hielo", 90, 100, 10));
        movimientos.add(new MovimientoFisico("Cabeza de Hierro", "Acero", 80, 100, 15));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoEstado("Pantalla de Humo", "Normal", 0, 100, "Aumenta defensa especial"));
        break;

    case "tyranitar":
        movimientos.add(new MovimientoFisico("Trituradora", "Siniestro", 80, 100, 15));
        movimientos.add(new MovimientoFisico("Roca Afilada", "Roca", 75, 95, 20));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoEstado("Danza Dragón", "Dragón", 0, 100, "Aumenta ataque"));
        break;

    case "kabutops":
        movimientos.add(new MovimientoFisico("Tajo Cruzado", "Bicho", 80, 100, 20));
        movimientos.add(new MovimientoFisico("Corte Furia", "Roca", 75, 95, 15));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "donphan":
        movimientos.add(new MovimientoFisico("Tierra Viva", "Tierra", 90, 100, 10));
        movimientos.add(new MovimientoFisico("Colmillo Rayo", "Eléctrico", 65, 95, 15));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "whiscash":
        movimientos.add(new MovimientoEspecial("Hidro Bomba", "Agua", 110, 80, 5));
        movimientos.add(new MovimientoFisico("Golpe Roca", "Roca", 75, 95, 15));
        movimientos.add(new MovimientoFisico("Colmillo Rayo", "Eléctrico", 65, 95, 15));
        movimientos.add(new MovimientoEstado("Recuperación", "Normal", 0, 100, "Restaura HP"));
        break;

    case "scizor":
        movimientos.add(new MovimientoFisico("Tijera X", "Bicho", 80, 100, 15));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoFisico("Puño Fuego", "Fuego", 75, 95, 15));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "masquerain":
        movimientos.add(new MovimientoFisico("Picotazo", "Bicho", 35, 100, 35));
        movimientos.add(new MovimientoEspecial("Onda Certera", "Agua", 60, 100, 20));
        movimientos.add(new MovimientoFisico("Aire Afilado", "Volador", 60, 100, 25));
        movimientos.add(new MovimientoEstado("Reflejo", "Psíquico", 0, 100, "Aumenta defensa"));
        break;

    case "gengar":
        movimientos.add(new MovimientoEspecial("Bola Sombra", "Fantasma", 80, 100, 15));
        movimientos.add(new MovimientoEspecial("Psíquico", "Psíquico", 90, 100, 10));
        movimientos.add(new MovimientoFisico("Puño Fuego", "Fuego", 75, 95, 15));
        movimientos.add(new MovimientoEstado("Confusión", "Psíquico", 0, 100, "Confunde al rival"));
        break;

    case "banette":
        movimientos.add(new MovimientoFisico("Golpe Sombra", "Fantasma", 70, 100, 20));
        movimientos.add(new MovimientoFisico("Desarme", "Siniestro", 30, 100, 15));
        movimientos.add(new MovimientoEstado("Truco", "Fantasma", 0, 100, "Roba objeto"));
        movimientos.add(new MovimientoEspecial("Bola Sombra", "Fantasma", 80, 100, 15));
        break;

    case "dragonite":
        movimientos.add(new MovimientoFisico("Golpe Dragón", "Dragón", 90, 100, 15));
        movimientos.add(new MovimientoFisico("Cabezazo", "Normal", 70, 100, 15));
        movimientos.add(new MovimientoEspecial("Hiperrayo", "Normal", 150, 90, 5));
        movimientos.add(new MovimientoEstado("Danza Dragón", "Dragón", 0, 100, "Aumenta ataque"));
        break;

    case "altaria":
        movimientos.add(new MovimientoFisico("Pico Taladro", "Volador", 80, 100, 20));
        movimientos.add(new MovimientoEspecial("Rayo Hielo", "Hielo", 90, 100, 10));
        movimientos.add(new MovimientoFisico("Danza Dragón", "Dragón", 0, 100, 10));
        movimientos.add(new MovimientoEstado("Reflejo", "Psíquico", 0, 100, "Aumenta defensa"));
        break;

    case "mewtwo":
        movimientos.add(new MovimientoEspecial("Psíquico", "Psíquico", 90, 100, 10));
        movimientos.add(new MovimientoEspecial("Rayo", "Eléctrico", 90, 100, 15));
        movimientos.add(new MovimientoEspecial("Hiperrayo", "Normal", 150, 90, 5));
        movimientos.add(new MovimientoEstado("Pantalla Luz", "Psíquico", 0, 100, "Reduce daño especial"));
        break;

    case "claydol":
        movimientos.add(new MovimientoEspecial("Psíquico", "Psíquico", 90, 100, 10));
        movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
        movimientos.add(new MovimientoEstado("Reflejo", "Psíquico", 0, 100, "Aumenta defensa"));
        movimientos.add(new MovimientoEspecial("Rayo", "Eléctrico", 90, 100, 15));
        break;

    case "machamp":
        movimientos.add(new MovimientoFisico("Puño Dinámico", "Lucha", 100, 100, 5));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "hariyama":
        movimientos.add(new MovimientoFisico("Puño Fuego", "Fuego", 75, 95, 15));
        movimientos.add(new MovimientoFisico("Puño Dinámico", "Lucha", 100, 100, 5));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "togetic":
        movimientos.add(new MovimientoEspecial("Deseo", "Normal", 0, 100, 15));
        movimientos.add(new MovimientoEspecial("Viento Plata", "Volador", 60, 100, 25));
        movimientos.add(new MovimientoEstado("Reflejo", "Psíquico", 0, 100, "Aumenta defensa"));
        movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
        break;

    case "swellow":
        movimientos.add(new MovimientoFisico("Pico Taladro", "Volador", 80, 100, 20));
        movimientos.add(new MovimientoFisico("Golpe Aéreo", "Volador", 85, 95, 15));
        movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "metagross":
        movimientos.add(new MovimientoFisico("Puño Meteoro", "Acero", 120, 85, 10));
        movimientos.add(new MovimientoFisico("Cabezazo Zen", "Psíquico", 80, 100, 15));
        movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "aggron":
        movimientos.add(new MovimientoFisico("Cabezazo Zen", "Acero", 80, 100, 15));
        movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "weezing":
        movimientos.add(new MovimientoEspecial("Lanzallamas", "Fuego", 90, 100, 15));
        movimientos.add(new MovimientoEspecial("Bomba Lodo", "Veneno", 65, 100, 10));
        movimientos.add(new MovimientoFisico("Explosión", "Normal", 250, 100, 5));
        movimientos.add(new MovimientoEstado("Tóxico", "Veneno", 0, 90, "Envenena al rival"));
        break;

    case "nidoking":
        movimientos.add(new MovimientoFisico("Golpe Roca", "Roca", 75, 95, 15));
        movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
        movimientos.add(new MovimientoEspecial("Bomba Lodo", "Veneno", 65, 100, 10));
        movimientos.add(new MovimientoFisico("Puño Fuego", "Fuego", 75, 95, 15));
        break;

    case "snorlax":
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoFisico("Puño Fuego", "Fuego", 75, 95, 15));
        movimientos.add(new MovimientoEstado("Descanso", "Psíquico", 0, 100, "Restaura HP"));
        movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
        break;

    case "zangoose":
        movimientos.add(new MovimientoFisico("Garra Umbría", "Siniestro", 70, 100, 25));
        movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
        movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "gardevoir":
        movimientos.add(new MovimientoEspecial("Psíquico", "Psíquico", 90, 100, 10));
        movimientos.add(new MovimientoEspecial("Rayo", "Eléctrico", 90, 100, 15));
        movimientos.add(new MovimientoEstado("Pantalla Luz", "Psíquico", 0, 100, "Reduce daño especial"));
        movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
        break;

    case "clefable":
        movimientos.add(new MovimientoEspecial("Deseo", "Normal", 0, 100, 15));
        movimientos.add(new MovimientoEspecial("Rayo", "Eléctrico", 90, 100, 15));
        movimientos.add(new MovimientoEstado("Amortiguador", "Hada", 0, 100, "Reduce daño"));
        movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
        break;

    case "absol":
        movimientos.add(new MovimientoFisico("Golpe Sombra", "Fantasma", 70, 100, 20));
        movimientos.add(new MovimientoFisico("Golpe Critico", "Siniestro", 100, 90, 10));
        movimientos.add(new MovimientoFisico("Corte Furia", "Siniestro", 75, 95, 15));
        movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 100, "Aumenta ataque"));
        break;

    case "chimecho":
        movimientos.add(new MovimientoEspecial("Psíquico", "Psíquico", 90, 100, 10));
        movimientos.add(new MovimientoEspecial("Rayo", "Eléctrico", 90, 100, 15));
        movimientos.add(new MovimientoEstado("Recuperación", "Normal", 0, 100, "Restaura HP"));
        movimientos.add(new MovimientoEstado("Pantalla Luz", "Psíquico", 0, 100, "Reduce daño especial"));
        break;

                
            default:
                movimientos.add(new MovimientoFisico("Placaje", "Normal", 40, 100, 35));
                movimientos.add(new MovimientoFisico("Golpe Rápido", "Normal", 40, 100, 30));
                movimientos.add(new MovimientoEspecial("Rayo Confuso", "Psíquico", 50, 100, 10));
                movimientos.add(new MovimientoEstado("Gruñido", "Normal", 0, 100, "Aumenta ataque")); 
        }
        
        return movimientos;
    }
    
    /**
     * Configura las estadisticas base de un Pokemon especifico basandose en su nombre
     *
     * @param pokemon El Pokemon cuyas estadisticas se van a configurar
     */
    private static void configurarEstadisticas(Pokemon pokemon) {
        // Configurar estadísticas el Pokemon
        switch(pokemon.getNombre().toLowerCase()) {


            case "charizard":
                pokemon.setPs(360);
                pokemon.setAtaque(293);
                pokemon.setDefensa(280);
                pokemon.setVelocidad(328);
                pokemon.setAtaqueEspecial(348);
                pokemon.setDefensaEspecial(295);
                pokemon.setTipoPrincipal("Fuego");
                pokemon.setTipoSecundario("Volador");
                break;

            case "torkoal":
                pokemon.setPs(320);
                pokemon.setAtaque(225);
                pokemon.setDefensa(350);
                pokemon.setVelocidad(174);
                pokemon.setAtaqueEspecial(260);
                pokemon.setDefensaEspecial(300);
                pokemon.setTipoPrincipal("Fuego");
                pokemon.setTipoSecundario(null);
                break;

            case "blastoise":
                pokemon.setPs(362);
                pokemon.setAtaque(291);
                pokemon.setDefensa(328);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(295);
                pokemon.setDefensaEspecial(339);
                pokemon.setTipoPrincipal("Agua");
                pokemon.setTipoSecundario(null);
                break;

            case "milotic":
                pokemon.setPs(394);
                pokemon.setAtaque(289);
                pokemon.setDefensa(276);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(362);
                pokemon.setDefensaEspecial(361);
                pokemon.setTipoPrincipal("Agua");
                pokemon.setTipoSecundario(null);
                break;

            case "venusaur":
                pokemon.setPs(364);
                pokemon.setAtaque(289);
                pokemon.setDefensa(291);
                pokemon.setVelocidad(284);
                pokemon.setAtaqueEspecial(328);
                pokemon.setDefensaEspecial(328);
                pokemon.setTipoPrincipal("Planta");
                pokemon.setTipoSecundario("Veneno");
                break;

            case "sceptile":
                pokemon.setPs(330);
                pokemon.setAtaque(300);
                pokemon.setDefensa(270);
                pokemon.setVelocidad(360);
                pokemon.setAtaqueEspecial(330);
                pokemon.setDefensaEspecial(270);
                pokemon.setTipoPrincipal("Planta");
                pokemon.setTipoSecundario(null);
                break;

            case "raichu":
                pokemon.setPs(330);
                pokemon.setAtaque(277);
                pokemon.setDefensa(245);
                pokemon.setVelocidad(350);
                pokemon.setAtaqueEspecial(295);
                pokemon.setDefensaEspecial(280);
                pokemon.setTipoPrincipal("Eléctrico");
                pokemon.setTipoSecundario(null);
                break;

            case "manectric":
                pokemon.setPs(310);
                pokemon.setAtaque(260);
                pokemon.setDefensa(240);
                pokemon.setVelocidad(375);
                pokemon.setAtaqueEspecial(350);
                pokemon.setDefensaEspecial(260);
                pokemon.setTipoPrincipal("Eléctrico");
                pokemon.setTipoSecundario(null);
                break;

            case "delibird":
                pokemon.setPs(250);
                pokemon.setAtaque(230);
                pokemon.setDefensa(230);
                pokemon.setVelocidad(300);
                pokemon.setAtaqueEspecial(200);
                pokemon.setDefensaEspecial(200);
                pokemon.setTipoPrincipal("Hielo");
                pokemon.setTipoSecundario("Volador");
                break;

            case "glalie":
                pokemon.setPs(320);
                pokemon.setAtaque(320);
                pokemon.setDefensa(320);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(280);
                pokemon.setDefensaEspecial(280);
                pokemon.setTipoPrincipal("Hielo");
                pokemon.setTipoSecundario(null);
                break;

            case "tyranitar":
                pokemon.setPs(400);
                pokemon.setAtaque(390);
                pokemon.setDefensa(330);
                pokemon.setVelocidad(230);
                pokemon.setAtaqueEspecial(300);
                pokemon.setDefensaEspecial(330);
                pokemon.setTipoPrincipal("Roca");
                pokemon.setTipoSecundario("Siniestro");
                break;

            case "kabutops":
                pokemon.setPs(320);
                pokemon.setAtaque(350);
                pokemon.setDefensa(320);
                pokemon.setVelocidad(350);
                pokemon.setAtaqueEspecial(250);
                pokemon.setDefensaEspecial(250);
                pokemon.setTipoPrincipal("Roca");
                pokemon.setTipoSecundario("Agua");
                break;

            case "donphan":
                pokemon.setPs(390);
                pokemon.setAtaque(390);
                pokemon.setDefensa(390);
                pokemon.setVelocidad(200);
                pokemon.setAtaqueEspecial(170);
                pokemon.setDefensaEspecial(210);
                pokemon.setTipoPrincipal("Tierra");
                pokemon.setTipoSecundario(null);
                break;

            case "whiscash":
                pokemon.setPs(390);
                pokemon.setAtaque(280);
                pokemon.setDefensa(260);
                pokemon.setVelocidad(230);
                pokemon.setAtaqueEspecial(280);
                pokemon.setDefensaEspecial(260);
                pokemon.setTipoPrincipal("Agua");
                pokemon.setTipoSecundario("Tierra");
                break;

            case "scizor":
                pokemon.setPs(330);
                pokemon.setAtaque(380);
                pokemon.setDefensa(330);
                pokemon.setVelocidad(310);
                pokemon.setAtaqueEspecial(230);
                pokemon.setDefensaEspecial(280);
                pokemon.setTipoPrincipal("Bicho");
                pokemon.setTipoSecundario("Acero");
                break;

            case "masquerain":
                pokemon.setPs(320);
                pokemon.setAtaque(230);
                pokemon.setDefensa(230);
                pokemon.setVelocidad(320);
                pokemon.setAtaqueEspecial(310);
                pokemon.setDefensaEspecial(310);
                pokemon.setTipoPrincipal("Bicho");
                pokemon.setTipoSecundario("Volador");
                break;

            case "gengar":
                pokemon.setPs(320);
                pokemon.setAtaque(230);
                pokemon.setDefensa(220);
                pokemon.setVelocidad(370);
                pokemon.setAtaqueEspecial(400);
                pokemon.setDefensaEspecial(300);
                pokemon.setTipoPrincipal("Fantasma");
                pokemon.setTipoSecundario("Veneno");
                break;

            case "banette":
                pokemon.setPs(320);
                pokemon.setAtaque(360);
                pokemon.setDefensa(220);
                pokemon.setVelocidad(330);
                pokemon.setAtaqueEspecial(300);
                pokemon.setDefensaEspecial(260);
                pokemon.setTipoPrincipal("Fantasma");
                pokemon.setTipoSecundario(null);
                break;

            case "dragonite":
                pokemon.setPs(410);
                pokemon.setAtaque(400);
                pokemon.setDefensa(350);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(320);
                pokemon.setDefensaEspecial(300);
                pokemon.setTipoPrincipal("Dragón");
                pokemon.setTipoSecundario("Volador");
                break;

            case "altaria":
                pokemon.setPs(350);
                pokemon.setAtaque(280);
                pokemon.setDefensa(300);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(330);
                pokemon.setDefensaEspecial(300);
                pokemon.setTipoPrincipal("Dragón");
                pokemon.setTipoSecundario("Volador");
                break;

            case "mewtwo":
                pokemon.setPs(416);
                pokemon.setAtaque(350);
                pokemon.setDefensa(290);
                pokemon.setVelocidad(370);
                pokemon.setAtaqueEspecial(440);
                pokemon.setDefensaEspecial(330);
                pokemon.setTipoPrincipal("Psíquico");
                pokemon.setTipoSecundario(null);
                break;

            case "claydol":
                pokemon.setPs(360);
                pokemon.setAtaque(260);
                pokemon.setDefensa(350);
                pokemon.setVelocidad(240);
                pokemon.setAtaqueEspecial(310);
                pokemon.setDefensaEspecial(350);
                pokemon.setTipoPrincipal("Tierra");
                pokemon.setTipoSecundario("Psíquico");
                break;

            case "machamp":
                pokemon.setPs(390);
                pokemon.setAtaque(400);
                pokemon.setDefensa(280);
                pokemon.setVelocidad(270);
                pokemon.setAtaqueEspecial(220);
                pokemon.setDefensaEspecial(280);
                pokemon.setTipoPrincipal("Lucha");
                pokemon.setTipoSecundario(null);
                break;

            case "hariyama":
                pokemon.setPs(420);
                pokemon.setAtaque(390);
                pokemon.setDefensa(300);
                pokemon.setVelocidad(220);
                pokemon.setAtaqueEspecial(180);
                pokemon.setDefensaEspecial(280);
                pokemon.setTipoPrincipal("Lucha");
                pokemon.setTipoSecundario(null);
                break;

            case "togetic":
                pokemon.setPs(270);
                pokemon.setAtaque(180);
                pokemon.setDefensa(250);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(310);
                pokemon.setDefensaEspecial(350);
                pokemon.setTipoPrincipal("Hada");
                pokemon.setTipoSecundario("Volador");
                break;

            case "swellow":
                pokemon.setPs(320);
                pokemon.setAtaque(350);
                pokemon.setDefensa(210);
                pokemon.setVelocidad(400);
                pokemon.setAtaqueEspecial(230);
                pokemon.setDefensaEspecial(210);
                pokemon.setTipoPrincipal("Normal");
                pokemon.setTipoSecundario("Volador");
                break;

            case "metagross":
                pokemon.setPs(380);
                pokemon.setAtaque(400);
                pokemon.setDefensa(400);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(280);
                pokemon.setDefensaEspecial(320);
                pokemon.setTipoPrincipal("Acero");
                pokemon.setTipoSecundario("Psíquico");
                break;

            case "aggron":
                pokemon.setPs(400);
                pokemon.setAtaque(390);
                pokemon.setDefensa(450);
                pokemon.setVelocidad(180);
                pokemon.setAtaqueEspecial(210);
                pokemon.setDefensaEspecial(280);
                pokemon.setTipoPrincipal("Acero");
                pokemon.setTipoSecundario("Roca");
                break;

            case "weezing":
                pokemon.setPs(370);
                pokemon.setAtaque(240);
                pokemon.setDefensa(350);
                pokemon.setVelocidad(200);
                pokemon.setAtaqueEspecial(300);
                pokemon.setDefensaEspecial(300);
                pokemon.setTipoPrincipal("Veneno");
                pokemon.setTipoSecundario(null);
                break;

            case "nidoking":
                pokemon.setPs(380);
                pokemon.setAtaque(350);
                pokemon.setDefensa(280);
                pokemon.setVelocidad(310);
                pokemon.setAtaqueEspecial(310);
                pokemon.setDefensaEspecial(260);
                pokemon.setTipoPrincipal("Tierra");
                pokemon.setTipoSecundario("Veneno");
                break;

            case "snorlax":
                pokemon.setPs(524);
                pokemon.setAtaque(350);
                pokemon.setDefensa(250);
                pokemon.setVelocidad(210);
                pokemon.setAtaqueEspecial(210);
                pokemon.setDefensaEspecial(250);
                pokemon.setTipoPrincipal("Normal");
                pokemon.setTipoSecundario(null);
                break;

            case "zangoose":
                pokemon.setPs(350);
                pokemon.setAtaque(370);
                pokemon.setDefensa(250);
                pokemon.setVelocidad(350);
                pokemon.setAtaqueEspecial(180);
                pokemon.setDefensaEspecial(220);
                pokemon.setTipoPrincipal("Normal");
                pokemon.setTipoSecundario(null);
                break;

            case "gardevoir":
                pokemon.setPs(380);
                pokemon.setAtaque(230);
                pokemon.setDefensa(250);
                pokemon.setVelocidad(320);
                pokemon.setAtaqueEspecial(350);
                pokemon.setDefensaEspecial(350);
                pokemon.setTipoPrincipal("Psíquico");
                pokemon.setTipoSecundario("Hada");
                break;

            case "clefable":
                pokemon.setPs(400);
                pokemon.setAtaque(230);
                pokemon.setDefensa(280);
                pokemon.setVelocidad(230);
                pokemon.setAtaqueEspecial(350);
                pokemon.setDefensaEspecial(350);
                pokemon.setTipoPrincipal("Hada");
                pokemon.setTipoSecundario(null);
                break;

            case "absol":
                pokemon.setPs(320);
                pokemon.setAtaque(390);
                pokemon.setDefensa(250);
                pokemon.setVelocidad(350);
                pokemon.setAtaqueEspecial(200);
                pokemon.setDefensaEspecial(250);
                pokemon.setTipoPrincipal("Siniestro");
                pokemon.setTipoSecundario(null);
                break;

            case "chimecho":
                pokemon.setPs(270);
                pokemon.setAtaque(180);
                pokemon.setDefensa(280);
                pokemon.setVelocidad(320);
                pokemon.setAtaqueEspecial(330);
                pokemon.setDefensaEspecial(350);
                pokemon.setTipoPrincipal("Psíquico");
                pokemon.setTipoSecundario(null);
                break;

            default:
                // Estadisticas por defecto
                pokemon.setPs(100); 
                pokemon.setAtaque(100); 
                pokemon.setDefensa(100);
                pokemon.setAtaqueEspecial(100); 
                pokemon.setDefensaEspecial(100); 
                pokemon.setVelocidad(100);
        }
    }
}
