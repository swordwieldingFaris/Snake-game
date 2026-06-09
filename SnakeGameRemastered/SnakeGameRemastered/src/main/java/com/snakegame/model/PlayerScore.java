package com.snakegame.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a player's score record.
 */
public class PlayerScore implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String playerName;
    private int score;
    private GameState.GameMode gameMode;
    private int foodEaten;
    private long playDuration;
    private LocalDateTime playedAt;

    public PlayerScore() {
    }

    public PlayerScore(String playerName, int score, GameState.GameMode gameMode, 
                      int foodEaten, long playDuration, LocalDateTime playedAt) {
        this.playerName = playerName;
        this.score = score;
        this.gameMode = gameMode;
        this.foodEaten = foodEaten;
        this.playDuration = playDuration;
        this.playedAt = playedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameState.GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameState.GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public void setFoodEaten(int foodEaten) {
        this.foodEaten = foodEaten;
    }

    public long getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(long playDuration) {
        this.playDuration = playDuration;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }

    @Override
    public String toString() {
        return String.format("%-20s | Score: %-6d | Mode: %-10s | Food: %-3d | Time: %d min",
                playerName, score, gameMode, foodEaten, playDuration / 60000);
    }
}
