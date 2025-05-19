package presentacion;

import javax.sound.sampled.*;

public class MusicManager {
    private static Clip currentClip = null;

    // Reproduce una pista de musica (ruta relativa a resources)
    public static void playMusic(String resourcePath) {
        stopMusic();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                MusicManager.class.getClassLoader().getResource(resourcePath)
            );
            currentClip = AudioSystem.getClip();
            currentClip.open(audioInputStream);
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            currentClip.start();
        } catch (Exception e) {
            System.err.println("No se pudo reproducir la musica: " + e.getMessage());
        }
    }

    // Detiene la musica actual
    public static void stopMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }
}