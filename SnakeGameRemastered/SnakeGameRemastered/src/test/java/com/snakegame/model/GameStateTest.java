package com.snakegame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    @DisplayName("GameState should initialize with correct default values")
    void testInitialValues() {
        assertEquals(0, gameState.getScore());
        assertEquals(0, gameState.getHighScore());
        assertEquals(GameState.GameMode.CLASSIC, gameState.getGameMode());
        assertEquals(GameState.GameStatus.READY, gameState.getStatus());
    }

    @Test
    @DisplayName("GameState should add score correctly")
    void testAddScore() {
        gameState.addScore(100);
        
        assertEquals(100, gameState.getScore());
    }

    @Test
    @DisplayName("GameState should apply score multiplier")
    void testScoreWithMultiplier() {
        gameState.getMultiplier().setValue(2);
        gameState.addScore(50);
        
        assertEquals(100, gameState.getScore());
    }

    @Test
    @DisplayName("GameState should update high score")
    void testUpdateHighScore() {
        gameState.addScore(100);
        gameState.updateHighScore();
        
        assertEquals(100, gameState.getHighScore());
    }

    @Test
    @DisplayName("GameState should start correctly")
    void testStart() {
        gameState.start();
        
        assertEquals(GameState.GameStatus.PLAYING, gameState.getStatus());
    }

    @Test
    @DisplayName("GameState should pause correctly")
    void testPause() {
        gameState.start();
        gameState.pause();
        
        assertEquals(GameState.GameStatus.PAUSED, gameState.getStatus());
        assertTrue(gameState.isPaused());
    }

    @Test
    @DisplayName("GameState should resume correctly")
    void testResume() {
        gameState.start();
        gameState.pause();
        gameState.resume();
        
        assertEquals(GameState.GameStatus.PLAYING, gameState.getStatus());
        assertFalse(gameState.isPaused());
    }

    @Test
    @DisplayName("GameState should stop correctly")
    void testStop() {
        gameState.start();
        gameState.stop();
        
        assertEquals(GameState.GameStatus.GAME_OVER, gameState.getStatus());
    }

    @Test
    @DisplayName("GameState should increment food eaten")
    void testIncrementFoodEaten() {
        gameState.incrementFoodEaten();
        
        assertEquals(1, gameState.getFoodEaten());
    }

    @Test
    @DisplayName("GameState should handle combo")
    void testCombo() {
        gameState.incrementCombo();
        
        assertEquals(1, gameState.getComboCount());
    }

    @Test
    @DisplayName("GameState should reset combo")
    void testResetCombo() {
        gameState.incrementCombo();
        gameState.resetCombo();
        
        assertEquals(0, gameState.getComboCount());
    }

    @Test
    @DisplayName("GameState should reset correctly")
    void testReset() {
        gameState.addScore(100);
        gameState.incrementFoodEaten();
        
        gameState.reset();
        
        assertEquals(0, gameState.getScore());
        assertEquals(0, gameState.getFoodEaten());
        assertEquals(GameState.GameStatus.READY, gameState.getStatus());
    }

    @Test
    @DisplayName("GameState should not pause if not playing")
    void testNoPauseIfNotPlaying() {
        gameState.pause();
        
        assertEquals(GameState.GameStatus.READY, gameState.getStatus());
    }

    @Test
    @DisplayName("GameState should have all game modes")
    void testGameModes() {
        assertEquals(4, GameState.GameMode.values().length);
    }

    @Test
    @DisplayName("ScoreMultiplier should reset correctly")
    void testMultiplierReset() {
        gameState.getMultiplier().setValue(3);
        gameState.getMultiplier().reset();
        
        assertEquals(1, gameState.getMultiplier().getValue());
    }

    @Test
    @DisplayName("Shield should activate correctly")
    void testShieldActivation() {
        gameState.getShieldActive().activate();
        
        assertTrue(gameState.getShieldActive().isActive());
    }
}