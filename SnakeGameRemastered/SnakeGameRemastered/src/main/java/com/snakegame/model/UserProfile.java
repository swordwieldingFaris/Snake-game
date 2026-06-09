package com.snakegame.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents user profile with customizable settings and statistics.
 */
public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private int totalGamesPlayed;
    private int totalGamesWon;
    private int totalScore;
    private int highScore;
    private long totalPlayTime;
    private int longestCombo;
    private Map<String, Integer> achievements;
    private UserSettings settings;
    private SnakeAppearance snakeAppearance;
    private Map<String, GameStats> modeStats;

    public UserProfile(String username) {
        this.username = username;
        this.totalGamesPlayed = 0;
        this.totalGamesWon = 0;
        this.totalScore = 0;
        this.highScore = 0;
        this.totalPlayTime = 0;
        this.longestCombo = 0;
        this.achievements = new HashMap<>();
        this.settings = new UserSettings();
        this.snakeAppearance = new SnakeAppearance();
        this.modeStats = new HashMap<>();
        initModeStats();
    }

    private void initModeStats() {
        for (GameState.GameMode mode : GameState.GameMode.values()) {
            modeStats.put(mode.name(), new GameStats());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void incrementGamesPlayed() {
        this.totalGamesPlayed++;
    }

    public int getTotalGamesWon() {
        return totalGamesWon;
    }

    public void incrementGamesWon() {
        this.totalGamesWon++;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addScore(int score) {
        this.totalScore += score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void updateHighScore(int score) {
        if (score > highScore) {
            this.highScore = score;
        }
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void addPlayTime(long playTime) {
        this.totalPlayTime += playTime;
    }

    public int getLongestCombo() {
        return longestCombo;
    }

    public void updateLongestCombo(int combo) {
        if (combo > longestCombo) {
            this.longestCombo = combo;
        }
    }

    public Map<String, Integer> getAchievements() {
        return new HashMap<>(achievements);
    }

    public void unlockAchievement(String achievement) {
        achievements.put(achievement, 1);
    }

    public boolean hasAchievement(String achievement) {
        return achievements.containsKey(achievement);
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    public SnakeAppearance getSnakeAppearance() {
        return snakeAppearance;
    }

    public void setSnakeAppearance(SnakeAppearance snakeAppearance) {
        this.snakeAppearance = snakeAppearance;
    }

    public Map<String, GameStats> getModeStats() {
        return new HashMap<>(modeStats);
    }

    public GameStats getStatsForMode(GameState.GameMode mode) {
        return modeStats.get(mode.name());
    }

    public void recordGameResult(int score, long playTime, int combo, GameState.GameMode mode) {
        incrementGamesPlayed();
        addScore(score);
        updateHighScore(score);
        addPlayTime(playTime);
        updateLongestCombo(combo);
        
        if (mode != null) {
            GameStats stats = modeStats.get(mode.name());
            if (stats != null) {
                stats.recordGame(score, playTime);
            }
        }
        
        if (checkNewAchievements()) {
        }
    }

    private boolean checkNewAchievements() {
        if (totalGamesPlayed == 1 && !hasAchievement("FIRST_GAME")) {
            unlockAchievement("FIRST_GAME");
            return true;
        }
        if (totalScore >= 1000 && !hasAchievement("SCORE_1000")) {
            unlockAchievement("SCORE_1000");
            return true;
        }
        if (highScore >= 500 && !hasAchievement("HIGH_SCORE")) {
            unlockAchievement("HIGH_SCORE");
            return true;
        }
        if (longestCombo >= 10 && !hasAchievement("COMBO_10")) {
            unlockAchievement("COMBO_10");
            return true;
        }
        return false;
    }

    public static class UserSettings implements Serializable {
        private static final long serialVersionUID = 1L;
        private int difficulty;
        private int speed;
        private boolean colorblindMode;
        private boolean soundEnabled;
        private boolean musicEnabled;
        private boolean showTooltips;
        private boolean autoSaveEnabled;
        private int saveSlotCount;

        public UserSettings() {
            this.difficulty = 2;
            this.speed = 5;
            this.colorblindMode = false;
            this.soundEnabled = true;
            this.musicEnabled = true;
            this.showTooltips = true;
            this.autoSaveEnabled = true;
            this.saveSlotCount = 3;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = Math.max(1, Math.min(5, difficulty));
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = Math.max(1, Math.min(10, speed));
        }

        public boolean isColorblindMode() {
            return colorblindMode;
        }

        public void setColorblindMode(boolean colorblindMode) {
            this.colorblindMode = colorblindMode;
        }

        public boolean isSoundEnabled() {
            return soundEnabled;
        }

        public void setSoundEnabled(boolean soundEnabled) {
            this.soundEnabled = soundEnabled;
        }

        public boolean isMusicEnabled() {
            return musicEnabled;
        }

        public void setMusicEnabled(boolean musicEnabled) {
            this.musicEnabled = musicEnabled;
        }

        public boolean isShowTooltips() {
            return showTooltips;
        }

        public void setShowTooltips(boolean showTooltips) {
            this.showTooltips = showTooltips;
        }

        public boolean isAutoSaveEnabled() {
            return autoSaveEnabled;
        }

        public void setAutoSaveEnabled(boolean autoSaveEnabled) {
            this.autoSaveEnabled = autoSaveEnabled;
        }

        public int getSaveSlotCount() {
            return saveSlotCount;
        }

        public void setSaveSlotCount(int saveSlotCount) {
            this.saveSlotCount = Math.max(1, Math.min(5, saveSlotCount));
        }
    }

    public static class SnakeAppearance implements Serializable {
        private static final long serialVersionUID = 1L;
        private String headColor;
        private String bodyColor;
        private String patternType;
        private boolean glowEnabled;

        public SnakeAppearance() {
            this.headColor = "#00FF00";
            this.bodyColor = "#00CC00";
            this.patternType = "SOLID";
            this.glowEnabled = false;
        }

        public String getHeadColor() {
            return headColor;
        }

        public void setHeadColor(String headColor) {
            this.headColor = headColor;
        }

        public String getBodyColor() {
            return bodyColor;
        }

        public void setBodyColor(String bodyColor) {
            this.bodyColor = bodyColor;
        }

        public String getPatternType() {
            return patternType;
        }

        public void setPatternType(String patternType) {
            this.patternType = patternType;
        }

        public boolean isGlowEnabled() {
            return glowEnabled;
        }

        public void setGlowEnabled(boolean glowEnabled) {
            this.glowEnabled = glowEnabled;
        }
    }

    public static class GameStats implements Serializable {
        private static final long serialVersionUID = 1L;
        private int gamesPlayed;
        private int gamesWon;
        private int highScore;
        private long totalPlayTime;
        private int averageScore;

        public GameStats() {
            this.gamesPlayed = 0;
            this.gamesWon = 0;
            this.highScore = 0;
            this.totalPlayTime = 0;
            this.averageScore = 0;
        }

        public void recordGame(int score, long playTime) {
            gamesPlayed++;
            if (score >= highScore) {
                highScore = score;
            }
            totalPlayTime += playTime;
            averageScore = ((averageScore * (gamesPlayed - 1)) + score) / gamesPlayed;
        }

        public int getGamesPlayed() {
            return gamesPlayed;
        }

        public int getGamesWon() {
            return gamesWon;
        }

        public void incrementGamesWon() {
            this.gamesWon++;
        }

        public int getHighScore() {
            return highScore;
        }

        public long getTotalPlayTime() {
            return totalPlayTime;
        }

        public int getAverageScore() {
            return averageScore;
        }
    }
}