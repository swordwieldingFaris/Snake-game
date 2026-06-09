package com.snakegame.integration;

import com.snakegame.model.*;
import com.snakegame.controller.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class GameIntegrationTest {
    private Game game;
    private GameController controller;

    @BeforeEach
    void setUp() {
        game = new Game(20, 20);
        controller = new GameController(game, null);
    }

    @Test
    @DisplayName("Full game flow from start to game over")
    void testFullGameFlow() {
        controller.start();
        
        for (int i = 0; i < 100; i++) {
            controller.update();
            
            if (game.getGameState().getStatus() == GameState.GameStatus.GAME_OVER) {
                break;
            }
        }
        
        assertTrue(game.getGameState().getScore() > 0 || 
               game.getGameState().getStatus() == GameState.GameStatus.GAME_OVER);
    }

    @Test
    @DisplayName("Game should progress through eating food")
    void testFoodEatingProgression() {
        game.start();
        
        int initialScore = game.getGameState().getScore();
        
        while (!game.getSnake().getHead().equals(game.getFood().getPosition())) {
            if (game.getGameState().getStatus() != GameState.GameStatus.PLAYING) {
                break;
            }
            game.update();
        }
        
        game.update();
        
        assertTrue(game.getGameState().getScore() > initialScore);
    }

    @Test
    @DisplayName("Game should handle obstacle collision")
    void testObstacleCollision() {
        game.addObstacles(1, Obstacle.ObstacleType.STATIC);
        
        Obstacle obstacle = game.getObstacles().get(0);
        game.getSnake().reset(obstacle.getPosition().x - 1, obstacle.getPosition().y, 3);
        game.getSnake().setDirection(Snake.Direction.RIGHT);
        
        game.start();
        game.update();
        
        assertEquals(GameState.GameStatus.GAME_OVER, game.getGameState().getStatus());
    }

    @Test
    @DisplayName("Game should handle power-up activation")
    void testPowerUpActivation() {
        game.start();
        
        Point head = game.getSnake().getHead();
        PowerUp powerUp = new PowerUp(head, PowerUp.PowerUpType.SHIELD);
        game.getPowerUps().add(powerUp);
        
        game.update();
        
        assertTrue(game.getGameState().getShieldActive().isActive());
    }

    @Test
    @DisplayName("Game should handle slow motion power-up")
    void testSlowMotionPowerUp() {
        int initialSpeed = game.getBaseSpeed();
        
        game.start();
        game.applySlowMotion();
        
        assertEquals(initialSpeed * 2, game.getCurrentSpeed());
    }

    @Test
    @DisplayName("Game should increase difficulty over time")
    void testDifficultyProgression() {
        game.start();
        
        int initialSpeed = game.getBaseSpeed();
        
        for (int i = 0; i < 10; i++) {
            while (!game.getSnake().getHead().equals(game.getFood().getPosition())) {
                if (game.getGameState().getStatus() != GameState.GameStatus.PLAYING) {
                    break;
                }
                game.update();
            }
            
            if (game.getGameState().getStatus() != GameState.GameStatus.PLAYING) {
                break;
            }
            
            game.update();
        }
        
        assertTrue(game.getBaseSpeed() < initialSpeed);
    }

    @Test
    @DisplayName("Game pause and resume should work correctly")
    void testPauseResume() {
        game.start();
        
        game.pause();
        assertEquals(GameState.GameStatus.PAUSED, game.getGameState().getStatus());
        
        game.resume();
        assertEquals(GameState.GameStatus.PLAYING, game.getGameState().getStatus());
    }

    @Test
    @DisplayName("Game should support multiple game modes")
    void testGameModes() {
        for (GameState.GameMode mode : GameState.GameMode.values()) {
            game.reset();
            game.setGameMode(mode);
            
            assertEquals(mode, game.getGameState().getGameMode());
        }
    }

    @Test
    @DisplayName("Game should maintain score across game state changes")
    void testScoreMaintenance() {
        game.start();
        
        while (!game.getSnake().getHead().equals(game.getFood().getPosition())) {
            if (game.getGameState().getStatus() != GameState.GameStatus.PLAYING) {
                break;
            }
            game.update();
        }
        
        if (game.getGameState().getStatus() == GameState.GameStatus.PLAYING) {
            game.update();
        }
        
        int scoreAfterEat = game.getGameState().getScore();
        
        game.pause();
        game.resume();
        
        assertEquals(scoreAfterEat, game.getGameState().getScore());
    }
}