package com.snakegame.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads demo-friendly defaults from config and persists user preferences.
 */
public final class AppConfig {
    private static final String CONFIG_FILE = "game-config.properties";
    private static final String PREFS_FILE = "user-preferences.properties";

    private static final Properties config = new Properties();
    private static final Properties prefs = new Properties();

    static {
        load();
    }

    private AppConfig() {
    }

    public static void load() {
        loadFile(CONFIG_FILE, config);
        applyDefaultConfig();
        loadFile(PREFS_FILE, prefs);
    }

    private static void loadFile(String file, Properties properties) {
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch (IOException ignored) {
        }
    }

    private static void applyDefaultConfig() {
        setDefault(config, "grid.width", "30");
        setDefault(config, "grid.height", "20");
        setDefault(config, "speed.classic", "150");
        setDefault(config, "speed.survival", "120");
        setDefault(config, "speed.timeattack", "100");
        setDefault(config, "theme.dark", "true");
        setDefault(config, "sound.enabled", "true");
        setDefault(config, "colorblind.enabled", "false");
        setDefault(config, "difficulty", "2");
    }

    private static void setDefault(Properties properties, String key, String value) {
        if (properties.getProperty(key) == null) {
            properties.setProperty(key, value);
        }
    }

    public static int gridWidth() {
        return getInt("grid.width", 30);
    }

    public static int gridHeight() {
        return getInt("grid.height", 20);
    }

    public static int speedClassic() {
        return getInt("speed.classic", 150);
    }

    public static int speedSurvival() {
        return getInt("speed.survival", 120);
    }

    public static int speedTimeAttack() {
        return getInt("speed.timeattack", 100);
    }

    public static boolean isDarkTheme() {
        return getBoolean("theme.dark", true);
    }

    public static void setDarkTheme(boolean darkTheme) {
        prefs.setProperty("theme.dark", String.valueOf(darkTheme));
        savePrefs();
    }

    public static boolean isSoundEnabled() {
        return getBoolean("sound.enabled", true);
    }

    public static void setSoundEnabled(boolean enabled) {
        prefs.setProperty("sound.enabled", String.valueOf(enabled));
        savePrefs();
    }

    public static boolean isColorblindEnabled() {
        return getBoolean("colorblind.enabled", false);
    }

    public static void setColorblindEnabled(boolean enabled) {
        prefs.setProperty("colorblind.enabled", String.valueOf(enabled));
        savePrefs();
    }

    public static int getDifficulty() {
        return getInt("difficulty", 2);
    }

    public static void setDifficulty(int difficulty) {
        prefs.setProperty("difficulty", String.valueOf(Math.max(1, Math.min(3, difficulty))));
        savePrefs();
    }

    private static int getInt(String key, int defaultValue) {
        String value = prefs.getProperty(key, config.getProperty(key));
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        String value = prefs.getProperty(key, config.getProperty(key));
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    private static void savePrefs() {
        try (FileOutputStream out = new FileOutputStream(PREFS_FILE)) {
            prefs.store(out, "Snake user preferences");
        } catch (IOException ignored) {
        }
    }
}
