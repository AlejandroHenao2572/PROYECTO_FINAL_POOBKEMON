package dominio;

import java.util.ArrayList;
import java.util.Map;

public class BattleFactory {

    public static Pokemon crearPokemon(String nombre, Trainer entrenador) {
        Pokemon p = new Pokemon(nombre, "Normal", null, 100, 100, 100, 100, 100, 100, new ArrayList<>());
        configurarEstadisticas(p);
        p.setMovimientos(obtenerMovimientosParaPokemon(nombre));
        return p;
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
                
            case "blastoise":
                movimientos.add(new MovimientoEspecial("Hidrobomba", "Agua", 110, 80, 5));
                movimientos.add(new MovimientoFisico("Cabezazo", "Normal", 70, 100, 15));
                movimientos.add(new MovimientoFisico("Giro Rápido", "Normal", 50, 100, 40));
                movimientos.add(new MovimientoEstado("Defensa Férrea", "Acero", 0, 0, "Aumenta defensa")); 
                break;
                
            case "venusaur":
                movimientos.add(new MovimientoEspecial("Rayo Solar", "Planta", 120, 100, 10));
                movimientos.add(new MovimientoFisico("Latigazo", "Planta", 45, 100, 25));
                movimientos.add(new MovimientoFisico("Derribo", "Normal", 90, 85, 20));
                movimientos.add(new MovimientoEstado("Síntesis", "Planta", 0, 0, "Cura PS")); 
                break;
                
            case "gengar":
                movimientos.add(new MovimientoEspecial("Bola Sombra", "Fantasma", 80, 100, 15));
                movimientos.add(new MovimientoFisico("Sombra Vil", "Fantasma", 40, 100, 30));
                movimientos.add(new MovimientoFisico("Puño Sombra", "Fantasma", 60, 100, 20));
                movimientos.add(new MovimientoEstado("Hipnosis", "Psíquico", 0, 60, "Aumenta defensa")); 
                break;
                
            case "dragonite":
                movimientos.add(new MovimientoEspecial("Pulso Dragón", "Dragón", 85, 100, 10));
                movimientos.add(new MovimientoFisico("Garra Dragón", "Dragón", 80, 100, 15));
                movimientos.add(new MovimientoFisico("Ataque Ala", "Volador", 75, 90, 20));
                movimientos.add(new MovimientoEstado("Danza Dragón", "Dragón", 0, 100, "Cura PS")); 
                break;
                
            case "togetic":
                movimientos.add(new MovimientoEspecial("Viento Feérico", "Hada", 80, 100, 10));
                movimientos.add(new MovimientoFisico("Ataque Rápido", "Normal", 40, 100, 30));
                movimientos.add(new MovimientoFisico("Golpe Cabeza", "Normal", 70, 100, 15));
                movimientos.add(new MovimientoEstado("Deseo", "Hada", 0, 0, "Cura PS")); 
                break;
                
            case "tyranitar":
                movimientos.add(new MovimientoEspecial("Tierra Viva", "Tierra", 90, 100, 10));
                movimientos.add(new MovimientoFisico("Avalancha", "Roca", 75, 90, 10));
                movimientos.add(new MovimientoFisico("Golpe Bajo", "Siniestro", 70, 100, 15));
                movimientos.add(new MovimientoEstado("Bufido", "Normal", 0, 100, "Aumenta defensa")); 
                break;
                
            case "gardevoir":
                movimientos.add(new MovimientoEspecial("Psíquico", "Psíquico", 90, 100, 10));
                movimientos.add(new MovimientoFisico("Cabezazo Zen", "Psíquico", 80, 90, 15));
                movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
                movimientos.add(new MovimientoEstado("Calmado", "Psíquico", 0, 0, "Cura PS")); 
                break;
                
            case "snorlax":
                movimientos.add(new MovimientoEspecial("Rayo Hielo", "Hielo", 90, 100, 10));
                movimientos.add(new MovimientoFisico("Golpe Cuerpo", "Normal", 85, 100, 15));
                movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
                movimientos.add(new MovimientoEstado("Descanso", "Psíquico", 0, 0, "Aumenta ataque")); 
                break;
                
            case "metagross":
                movimientos.add(new MovimientoEspecial("Psíquico", "Psíquico", 90, 100, 10));
                movimientos.add(new MovimientoFisico("Golpe Metal", "Acero", 70, 90, 20));
                movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
                movimientos.add(new MovimientoEstado("Amnesia", "Psíquico", 0, 0, "Cura PS")); // Aumenta defensa 
                break;
                
            case "donphan":
                movimientos.add(new MovimientoEspecial("Tierra Viva", "Tierra", 90, 100, 10));
                movimientos.add(new MovimientoFisico("Terremoto", "Tierra", 100, 100, 10));
                movimientos.add(new MovimientoFisico("Derribo", "Normal", 90, 85, 20));
                movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 0, "Aumenta ataque")); // Aumenta ataque
                break;
                
            case "machamp":
                movimientos.add(new MovimientoEspecial("Foco Resplandor", "Lucha", 80, 100, 10));
                movimientos.add(new MovimientoFisico("Golpe Karate", "Lucha", 90, 100, 10));
                movimientos.add(new MovimientoFisico("Sumisión", "Lucha", 80, 80, 20));
                movimientos.add(new MovimientoEstado("Agilidad", "Psíquico", 0, 0, "Aumenta velocidad")); 
                break;
                
            case "delibird":
                movimientos.add(new MovimientoEspecial("Viento Hielo", "Hielo", 55, 95, 15));
                movimientos.add(new MovimientoFisico("Golpe Aéreo", "Volador", 75, 95, 20));
                movimientos.add(new MovimientoFisico("Presente", "Normal", 0, 90, 15)); 
                movimientos.add(new MovimientoEstado("Danza Pluma", "Volador", 0, 0, "Aumenta ataque")); 
                break;
                
            case "raichu":
                movimientos.add(new MovimientoEspecial("Trueno", "Eléctrico", 110, 70, 10));
                movimientos.add(new MovimientoFisico("Voltio Cruel", "Eléctrico", 90, 100, 15));
                movimientos.add(new MovimientoFisico("Cola Férrea", "Acero", 100, 75, 15));
                movimientos.add(new MovimientoEstado("Danza Espada", "Normal", 0, 0, "Aumenta velocidad")); // Aumenta ataque
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
            case "blastoise":
                pokemon.setPs(362); 
                pokemon.setAtaque(291); 
                pokemon.setDefensa(328);
                pokemon.setVelocidad(280);
                pokemon.setAtaqueEspecial(295); 
                pokemon.setDefensaEspecial(339); 
                pokemon.setTipoPrincipal("Agua");
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
            case "gengar":
                pokemon.setPs(324); 
                pokemon.setAtaque(251); 
                pokemon.setDefensa(240);
                pokemon.setVelocidad(350);
                pokemon.setAtaqueEspecial(394); 
                pokemon.setDefensaEspecial(273); 
                pokemon.setTipoPrincipal("Fantasma");
                pokemon.setTipoSecundario("Veneno");
                break;
            case "dragonite":
                pokemon.setPs(386); 
                pokemon.setAtaque(403); 
                pokemon.setDefensa(317);
                pokemon.setVelocidad(284);
                pokemon.setAtaqueEspecial(328); 
                pokemon.setDefensaEspecial(328); 
                pokemon.setTipoPrincipal("Dragon");
                pokemon.setTipoSecundario("Volador");
                break;
            case "togetic":
                pokemon.setPs(314); 
                pokemon.setAtaque(196); 
                pokemon.setDefensa(295);
                pokemon.setVelocidad(196);
                pokemon.setAtaqueEspecial(284); 
                pokemon.setDefensaEspecial(339); 
                pokemon.setTipoPrincipal("Hada");
                pokemon.setTipoSecundario("Volador");
                break;
            case "tyranitar":
                pokemon.setPs(404); 
                pokemon.setAtaque(403); 
                pokemon.setDefensa(350);
                pokemon.setVelocidad(243);
                pokemon.setAtaqueEspecial(317); 
                pokemon.setDefensaEspecial(328); 
                pokemon.setTipoPrincipal("Roca");
                pokemon.setTipoSecundario("Siniestro");
                break;
            case "gardevoir":
                pokemon.setPs(340); 
                pokemon.setAtaque(251); 
                pokemon.setDefensa(251);
                pokemon.setVelocidad(284);
                pokemon.setAtaqueEspecial(383); 
                pokemon.setDefensaEspecial(361); 
                pokemon.setTipoPrincipal("Psiquico");
                pokemon.setTipoSecundario("Hada");
                break;
            case "snorlax":
                pokemon.setPs(524); 
                pokemon.setAtaque(350); 
                pokemon.setDefensa(251);
                pokemon.setVelocidad(174);
                pokemon.setAtaqueEspecial(251); 
                pokemon.setDefensaEspecial(350); 
                pokemon.setTipoPrincipal("Normal");
                break;
            case "metagross":
                pokemon.setPs(364); 
                pokemon.setAtaque(405); 
                pokemon.setDefensa(394);
                pokemon.setVelocidad(262);
                pokemon.setAtaqueEspecial(317); 
                pokemon.setDefensaEspecial(306); 
                pokemon.setTipoPrincipal("Acero");
                pokemon.setTipoSecundario("Psiquico");
                break;
            case "donphan":
                pokemon.setPs(384); 
                pokemon.setAtaque(372); 
                pokemon.setDefensa(372);
                pokemon.setVelocidad(218);
                pokemon.setAtaqueEspecial(240); 
                pokemon.setDefensaEspecial(240); 
                pokemon.setTipoPrincipal("Tierra");
                break;
            case "machamp":
                pokemon.setPs(384); 
                pokemon.setAtaque(394); 
                pokemon.setDefensa(284);
                pokemon.setVelocidad(229);
                pokemon.setAtaqueEspecial(251); 
                pokemon.setDefensaEspecial(295); 
                pokemon.setTipoPrincipal("Lucha");
                break;
            case "delibird":
                pokemon.setPs(294); 
                pokemon.setAtaque(229); 
                pokemon.setDefensa(207);
                pokemon.setVelocidad(273);
                pokemon.setAtaqueEspecial(251); 
                pokemon.setDefensaEspecial(207); 
                pokemon.setTipoPrincipal("Hielo");
                pokemon.setTipoSecundario("Volador");
                break;
            case "raichu":
                pokemon.setPs(324); 
                pokemon.setAtaque(306); 
                pokemon.setDefensa(229);
                pokemon.setVelocidad(350);
                pokemon.setAtaqueEspecial(306); 
                pokemon.setDefensaEspecial(284); 
                pokemon.setTipoPrincipal("Eléctrico");
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
