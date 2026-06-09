package com.snakegame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Lifetime player statistics and achievements.
 */
public class GameStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalGamesPlayed;
    private int totalApplesEaten;
    private int longestSnakeLength;
    private final Set<String> achievements;

    public GameStatistics() {
        this.totalGamesPlayed = 0;
        this.totalApplesEaten = 0;
        this.longestSnakeLength = 0;
        this.achievements = new LinkedHashSet<>();
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public int getTotalApplesEaten() {
        return totalApplesEaten;
    }

    public int getLongestSnakeLength() {
        return longestSnakeLength;
    }

    public Set<String> getAchievements() {
        return new LinkedHashSet<>(achievements);
    }

    public void recordGameStarted() {
        totalGamesPlayed++;
    }

    public List<String> recordGameEnded(GameState state, int snakeLength) {
        if (state == null) {
            return new ArrayList<>();
        }

        totalApplesEaten += state.getFoodEaten();
        longestSnakeLength = Math.max(longestSnakeLength, snakeLength);
        List<String> unlocked = new ArrayList<>();

        if (state.getFoodEaten() >= 10 && achievements.add("Eat 10 apples in one game")) {
            unlocked.add("Eat 10 apples in one game");
        }
        if (state.getScore() >= 300 && achievements.add("Score 300+ in one game")) {
            unlocked.add("Score 300+ in one game");
        }
        if (snakeLength >= 15 && achievements.add("Reach snake length 15")) {
            unlocked.add("Reach snake length 15");
        }
        return unlocked;
    }
}
