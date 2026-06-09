package com.snakegame.service;

import com.snakegame.model.*;
import java.io.*;
import java.util.*;

/**
 * Service for game data persistence.
 * Handles saving and loading game state, user profiles, and settings.
 */
public class GameService {
    private static final String SAVE_DIR = "saves";
    private static final String PROFILE_DIR = "profiles";
    private static final String SETTINGS_FILE = "settings.dat";
    private static final int MAX_SAVE_SLOTS = 5;

    public GameService() {
        initializeDirectories();
    }

    private void initializeDirectories() {
        new File(SAVE_DIR).mkdirs();
        new File(PROFILE_DIR).mkdirs();
    }

    public boolean saveGame(GameState state, int slot) {
        if (slot < 0 || slot >= MAX_SAVE_SLOTS) return false;
        
        String filename = SAVE_DIR + File.separator + "save_" + slot + ".dat";
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(state);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            return false;
        }
    }

    public GameState loadGame(int slot) {
        if (slot < 0 || slot >= MAX_SAVE_SLOTS) return null;
        
        String filename = SAVE_DIR + File.separator + "save_" + slot + ".dat";
        File file = new File(filename);
        
        if (!file.exists()) return null;
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    public List<String> getSaveSlots() {
        List<String> slots = new ArrayList<>();
        
        for (int i = 0; i < MAX_SAVE_SLOTS; i++) {
            String filename = SAVE_DIR + File.separator + "save_" + i + ".dat";
            File file = new File(filename);
            
            if (file.exists()) {
                slots.add("Slot " + i + " - " + new Date(file.lastModified()));
            } else {
                slots.add("Slot " + i + " - Empty");
            }
        }
        
        return slots;
    }

    public boolean deleteSave(int slot) {
        if (slot < 0 || slot >= MAX_SAVE_SLOTS) return false;
        
        String filename = SAVE_DIR + File.separator + "save_" + slot + ".dat";
        return new File(filename).delete();
    }

    public boolean saveProfile(UserProfile profile) {
        String filename = PROFILE_DIR + File.separator + profile.getUsername() + ".dat";
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(profile);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save profile: " + e.getMessage());
            return false;
        }
    }

    public UserProfile loadProfile(String username) {
        String filename = PROFILE_DIR + File.separator + username + ".dat";
        File file = new File(filename);
        
        if (!file.exists()) return null;
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (UserProfile) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load profile: " + e.getMessage());
            return null;
        }
    }

    public List<String> getProfiles() {
        List<String> profiles = new ArrayList<>();
        File dir = new File(PROFILE_DIR);
        
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".dat")) {
                    String name = file.getName().replace(".dat", "");
                    profiles.add(name);
                }
            }
        }
        
        return profiles;
    }

    public boolean deleteProfile(String username) {
        String filename = PROFILE_DIR + File.separator + username + ".dat";
        return new File(filename).delete();
    }

    public boolean saveSettings(UserProfile.UserSettings settings) {
        String filename = SETTINGS_FILE;
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(settings);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save settings: " + e.getMessage());
            return false;
        }
    }

    public UserProfile.UserSettings loadSettings() {
        String filename = SETTINGS_FILE;
        File file = new File(filename);
        
        if (!file.exists()) return new UserProfile.UserSettings();
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (UserProfile.UserSettings) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            return new UserProfile.UserSettings();
        }
    }

    /**
     * Save a game score to the database.
     */
    public void saveScore(String playerName, GameState gameState) {
        com.snakegame.model.PlayerScore score = new com.snakegame.model.PlayerScore(
            playerName,
            gameState.getScore(),
            gameState.getGameMode(),
            gameState.getFoodEaten(),
            gameState.getElapsedTime(),
            java.time.LocalDateTime.now()
        );
        com.snakegame.util.DatabaseManager.saveScore(score);
    }

    /**
     * Get top scores from the database.
     */
    public List<com.snakegame.model.PlayerScore> getTopScores(int limit) {
        return com.snakegame.util.DatabaseManager.getTopScores(limit);
    }

    /**
     * Get high score for a specific game mode.
     */
    public int getHighScoreByMode(GameState.GameMode mode) {
        return com.snakegame.util.DatabaseManager.getHighScoreByMode(mode);
    }

    /**
     * Get all scores for a specific game mode.
     */
    public List<com.snakegame.model.PlayerScore> getScoresByMode(GameState.GameMode mode) {
        return com.snakegame.util.DatabaseManager.getScoresByMode(mode);
    }
}