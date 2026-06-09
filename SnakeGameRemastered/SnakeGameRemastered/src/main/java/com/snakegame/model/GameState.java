package com.snakegame.model;

import java.io.Serializable;

/**
 * Represents the current state of the game.
 * Tracks score, game mode, timer, achievements, and game status.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int score;
    private int highScore;
    private GameMode gameMode;
    private GameStatus status;
    private long startTime;
    private long elapsedTime;
    private int foodEaten;
    private int comboCount;
    private boolean comboActive;
    private int activePowerUpCount;
    private ScoreMultiplier multiplier;
    private ShieldActive shieldActive;
    private boolean slowMotionActive;
    private boolean paused;
    private int level;

    public GameState() {
        this(GameMode.CLASSIC);
    }

    public GameState(GameMode gameMode) {
        this.score = 0;
        this.highScore = 0;
        this.gameMode = gameMode;
        this.status = GameStatus.READY;
        this.startTime = 0;
        this.elapsedTime = 0;
        this.foodEaten = 0;
        this.comboCount = 0;
        this.comboActive = false;
        this.activePowerUpCount = 0;
        this.multiplier = new ScoreMultiplier();
        this.shieldActive = new ShieldActive();
        this.slowMotionActive = false;
        this.paused = false;
        this.level = 1;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void updateHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getElapsedTime() {
        if (status == GameStatus.PLAYING && startTime > 0 && !paused) {
            return System.currentTimeMillis() - startTime;
        }
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public void setFoodEaten(int foodEaten) {
        this.foodEaten = foodEaten;
    }

    public void incrementFoodEaten() {
        this.foodEaten++;
    }

    public int getComboCount() {
        return comboCount;
    }

    public void setComboCount(int comboCount) {
        this.comboCount = comboCount;
    }

    public void incrementCombo() {
        this.comboCount++;
    }

    public void resetCombo() {
        this.comboCount = 0;
    }

    public boolean isComboActive() {
        return comboActive;
    }

    public void setComboActive(boolean comboActive) {
        this.comboActive = comboActive;
    }

    public int getActivePowerUpCount() {
        return activePowerUpCount;
    }

    public void setActivePowerUpCount(int activePowerUpCount) {
        this.activePowerUpCount = activePowerUpCount;
    }

    public ScoreMultiplier getMultiplier() {
        return multiplier;
    }

    public ShieldActive getShieldActive() {
        return shieldActive;
    }

    public boolean isSlowMotionActive() {
        return slowMotionActive;
    }

    public void setSlowMotionActive(boolean slowMotionActive) {
        this.slowMotionActive = slowMotionActive;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void reset() {
        this.score = 0;
        this.startTime = 0;
        this.elapsedTime = 0;
        this.foodEaten = 0;
        this.comboCount = 0;
        this.comboActive = false;
        this.activePowerUpCount = 0;
        this.multiplier.reset();
        this.shieldActive.reset();
        this.slowMotionActive = false;
        this.paused = false;
        this.level = 1;
        this.status = GameStatus.READY;
    }

    public void start() {
        this.status = GameStatus.PLAYING;
        this.startTime = System.currentTimeMillis();
    }

    public void pause() {
        if (status == GameStatus.PLAYING) {
            this.status = GameStatus.PAUSED;
            this.paused = true;
            this.elapsedTime += System.currentTimeMillis() - startTime;
        }
    }

    public void resume() {
        if (status == GameStatus.PAUSED) {
            this.status = GameStatus.PLAYING;
            this.paused = false;
            this.startTime = System.currentTimeMillis();
        }
    }

    public void stop() {
        this.status = GameStatus.GAME_OVER;
        this.elapsedTime += System.currentTimeMillis() - startTime;
        updateHighScore();
    }

    public enum GameStatus {
        READY,
        PLAYING,
        PAUSED,
        GAME_OVER,
        WON,
        LEVEL_UP
    }

    public enum GameMode {
        CLASSIC("Classic"),
        TIME_ATTACK("Time Attack"),
        SURVIVAL("Survival"),
        PUZZLE("Puzzle");

        private final String displayName;

        GameMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static class ScoreMultiplier implements Serializable {
        private static final long serialVersionUID = 1L;
        private int value;
        private int comboThreshold;
        private static final int MAX_MULTIPLIER = 5;

        public ScoreMultiplier() {
            this.value = 1;
            this.comboThreshold = 5;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = Math.min(value, MAX_MULTIPLIER);
        }

        public void increment(int combo) {
            if (combo >= comboThreshold && value < MAX_MULTIPLIER) {
                value++;
            }
        }

        public void reset() {
            this.value = 1;
        }
    }

    public static class ShieldActive implements Serializable {
        private static final long serialVersionUID = 1L;
        private boolean active;
        private long activationTime;
        private int duration;

        public ShieldActive() {
            this.active = false;
            this.activationTime = 0;
            this.duration = 8000;
        }

        public boolean isActive() {
            if (active) {
                return System.currentTimeMillis() - activationTime < duration;
            }
            return false;
        }

        public void activate() {
            this.active = true;
            this.activationTime = System.currentTimeMillis();
        }

        public void deactivate() {
            this.active = false;
        }

        public void reset() {
            this.active = false;
            this.activationTime = 0;
        }
    }
}