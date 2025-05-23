package presentacion;

import javax.sound.sampled.*;

/**
 * Clase utilitaria para gestionar la reproduccion de musica en la aplicacion
 * Permite reproducir y detener pistas de musica de fondo
 * 
 * @author David Patacon
 * @author Daniel Hueso
 * @version 1.0
 */
public class MusicManager {
    // Clip de audio actual que se esta reproduciendo
    private static Clip currentClip = null;

    /**
     * Reproduce una pista de musica (ruta relativa a resources)
     * Si ya hay musica sonando, la detiene antes de reproducir la nueva
     * 
     * @param resourcePath Ruta del archivo de audio dentro de resources
     */
    public static void playMusic(String resourcePath) {
        stopMusic();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                MusicManager.class.getClassLoader().getResource(resourcePath)
            );
            currentClip = AudioSystem.getClip();
            currentClip.open(audioInputStream);
            currentClip.loop(Clip.LOOP_CONTINUOUSLY); // Repetir la musica indefinidamente
            currentClip.start();
        } catch (Exception e) {
            System.err.println("No se pudo reproducir la musica: " + e.getMessage());
        }
    }

    /**
     * Detiene la musica actual si hay alguna reproduciendose
     * Libera los recursos del clip
     */
    public static void stopMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }
}