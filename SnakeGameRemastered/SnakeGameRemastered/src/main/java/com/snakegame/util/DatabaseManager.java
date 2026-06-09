package com.snakegame.util;

import com.snakegame.model.GameState;
import com.snakegame.model.PlayerScore;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Manages SQLite database operations for storing and retrieving player scores.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:snake_game.db";
    private static final String TABLE_NAME = "player_scores";
    
    static {
        initializeDatabase();
    }

    /**
     * Initialize database schema if it doesn't exist.
     */
    private static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_name TEXT NOT NULL," +
                "score INTEGER NOT NULL," +
                "game_mode TEXT NOT NULL," +
                "food_eaten INTEGER NOT NULL," +
                "play_duration LONG NOT NULL," +
                "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    /**
     * Get a database connection.
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Save a player's score to the database.
     */
    public static void saveScore(PlayerScore score) {
        String insertSQL = "INSERT INTO " + TABLE_NAME + 
                " (player_name, score, game_mode, food_eaten, play_duration, played_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, score.getPlayerName());
            pstmt.setInt(2, score.getScore());
            pstmt.setString(3, score.getGameMode().name());
            pstmt.setInt(4, score.getFoodEaten());
            pstmt.setLong(5, score.getPlayDuration());
            pstmt.setTimestamp(6, Timestamp.valueOf(score.getPlayedAt()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }

    /**
     * Get top scores from the database.
     */
    public static List<PlayerScore> getTopScores(int limit) {
        List<PlayerScore> scores = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + 
                " ORDER BY score DESC LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PlayerScore score = new PlayerScore();
                score.setId(rs.getInt("id"));
                score.setPlayerName(rs.getString("player_name"));
                score.setScore(rs.getInt("score"));
                score.setGameMode(GameState.GameMode.valueOf(rs.getString("game_mode")));
                score.setFoodEaten(rs.getInt("food_eaten"));
                score.setPlayDuration(rs.getLong("play_duration"));
                score.setPlayedAt(rs.getTimestamp("played_at").toLocalDateTime());
                scores.add(score);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving top scores: " + e.getMessage());
        }
        return scores;
    }

    /**
     * Get high score for a specific game mode.
     */
    public static int getHighScoreByMode(GameState.GameMode mode) {
        String selectSQL = "SELECT MAX(score) as high_score FROM " + TABLE_NAME + 
                " WHERE game_mode = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, mode.name());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("high_score");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving high score: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get all scores for a specific game mode.
     */
    public static List<PlayerScore> getScoresByMode(GameState.GameMode mode) {
        List<PlayerScore> scores = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + 
                " WHERE game_mode = ? ORDER BY score DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, mode.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PlayerScore score = new PlayerScore();
                score.setId(rs.getInt("id"));
                score.setPlayerName(rs.getString("player_name"));
                score.setScore(rs.getInt("score"));
                score.setGameMode(GameState.GameMode.valueOf(rs.getString("game_mode")));
                score.setFoodEaten(rs.getInt("food_eaten"));
                score.setPlayDuration(rs.getLong("play_duration"));
                score.setPlayedAt(rs.getTimestamp("played_at").toLocalDateTime());
                scores.add(score);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving scores by mode: " + e.getMessage());
        }
        return scores;
    }

    /**
     * Clear all scores from the database.
     */
    public static void clearAllScores() {
        String deleteSQL = "DELETE FROM " + TABLE_NAME;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(deleteSQL);
        } catch (SQLException e) {
            System.err.println("Error clearing scores: " + e.getMessage());
        }
    }
}
