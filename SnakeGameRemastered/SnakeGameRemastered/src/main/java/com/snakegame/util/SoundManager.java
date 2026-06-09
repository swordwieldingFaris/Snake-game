package com.snakegame.util;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

/**
 * Manages sound effects and background music for the game.
 */
public class SoundManager {
    private static boolean soundEnabled = true;
    private static float volume = 0.5f; // 0.0f to 1.0f
    
    // Sound effect types
    public enum SoundEffect {
        FOOD_EATEN("food_eaten.wav"),
        POWER_UP("power_up.wav"),
        COLLISION("collision.wav"),
        GAME_OVER("game_over.wav"),
        MENU_SELECT("menu_select.wav"),
        BACKGROUND("background.wav");
        
        public final String filename;
        
        SoundEffect(String filename) {
            this.filename = filename;
        }
    }

    /**
     * Play a sound effect.
     */
    public static void playSound(SoundEffect sound) {
        if (!soundEnabled) return;
        
        try {
            // For now, we'll use system beep. In a production version,
            // you would load actual WAV files from resources.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(generateTone(400, 100)));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            // Set volume
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(20f * (float) Math.log10(volume));
            
            clip.start();
        } catch (Exception e) {
            // Silently ignore audio errors
        }
    }

    /**
     * Generate a simple tone for testing.
     */
    private static byte[] generateTone(int frequency, int duration) {
        int sampleRate = 44100;
        byte[] result = new byte[(sampleRate * duration) / 1000];
        double angle = 0;
        
        for (int i = 0; i < result.length; i++) {
            double sample = Math.sin(angle) * 32767;
            result[i] = (byte) sample;
            angle += (2.0 * Math.PI * frequency) / sampleRate;
        }
        return result;
    }

    /**
     * Set whether sound is enabled.
     */
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    /**
     * Check if sound is enabled.
     */
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Set the volume level (0.0f to 1.0f).
     */
    public static void setVolume(float vol) {
        volume = Math.max(0.0f, Math.min(1.0f, vol));
    }

    /**
     * Get the current volume level.
     */
    public static float getVolume() {
        return volume;
    }
}
