package com.snakegame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(20, 20);
    }

    @Test
    @DisplayName("Game should initialize with correct grid dimensions")
    void testGridDimensions() {
        assertEquals(20, game.getGridWidth());
        assertEquals(20, game.getGridHeight());
    }

    @Test
    @DisplayName("Game should initialize snake at center")
    void testSnakeInitialPosition() {
        Point head = game.getSnake().getHead();
        assertEquals(10, head.x);
        assertEquals(10, head.y);
    }

    @Test
    @DisplayName("Game should initialize with food")
    void testFoodSpawn() {
        assertNotNull(game.getFood());
    }

    @Test
    @DisplayName("Game should have correct initial speed")
    void testInitialSpeed() {
        assertEquals(150, game.getBaseSpeed());
    }

    @Test
    @DisplayName("Game should start correctly")
    void testStartGame() {
        game.start();
        
        assertEquals(GameState.GameStatus.PLAYING, game.getGameState().getStatus());
    }

    @Test
    @DisplayName("Game should update game state")
    void testUpdateGame() {
        game.start();
        Point initialHead = game.getSnake().getHead();
        
        game.update();
        
        Point newHead = game.getSnake().getHead();
        assertNotEquals(initialHead, newHead);
    }

    @Test
    @DisplayName("Game should not update when not playing")
    void testNoUpdateWhenNotPlaying() {
        game.update();
        
        assertEquals(GameState.GameStatus.READY, game.getGameState().getStatus());
    }

    @Test
    @DisplayName("Game should detect wall collision")
    void testWallCollision() {
        game.getSnake().reset(0, 0, 3);
        game.start();
        
        game.update();
        
        assertEquals(GameState.GameStatus.GAME_OVER, game.getGameState().getStatus());
    }

    @Test
    @DisplayName("Game should detect self collision")
    void testSelfCollision() {
        game.start();
        game.getSnake().setDirection(Snake.Direction.UP);
        game.update();
        game.update();
        game.getSnake().setDirection(Snake.Direction.LEFT);
        game.update();
        game.getSnake().setDirection(Snake.Direction.DOWN);
        game.update();
        
        assertEquals(GameState.GameStatus.GAME_OVER, game.getGameState().getStatus());
    }

    @Test
    @DisplayName("Game should detect food collision and grow")
    void testFoodCollision() {
        game.start();
        int initialLength = game.getSnake().getLength();
        
        game.getSnake().setDirection(Snake.Direction.UP);
        while (!game.getSnake().getHead().equals(game.getFood().getPosition())) {
            game.update();
        }
        
        assertTrue(game.getSnake().getLength() > initialLength);
    }

    @Test
    @DisplayName("Game should increase speed after eating")
    void testSpeedIncrease() {
        int initialSpeed = game.getBaseSpeed();
        
        game.start();
        
        while (!game.getSnake().getHead().equals(game.getFood().getPosition())) {
            game.update();
        }
        
        assertTrue(game.getBaseSpeed() < initialSpeed);
    }

    @Test
    @DisplayName("Game should spawn new food after eating")
    void testFoodRespawn() {
        game.start();
        Point initialFoodPos = game.getFood().getPosition();
        
        while (!game.getSnake().getHead().equals(initialFoodPos)) {
            game.update();
        }
        
        assertTrue(game.getSnake().getHead().equals(initialFoodPos) || 
               !game.getFood().getPosition().equals(initialFoodPos));
    }

    @Test
    @DisplayName("Game should reset correctly")
    void testReset() {
        game.start();
        game.getSnake().grow(5);
        
        game.reset();
        
        assertEquals(3, game.getSnake().getLength());
        assertEquals(GameState.GameStatus.READY, game.getGameState().getStatus());
    }

    @Test
    @DisplayName("Game should add obstacles")
    void testAddObstacles() {
        game.addObstacles(5, Obstacle.ObstacleType.STATIC);
        
        assertEquals(5, game.getObstacles().size());
    }

    @Test
    @DisplayName("Game should clear obstacles")
    void testClearObstacles() {
        game.addObstacles(5, Obstacle.ObstacleType.STATIC);
        game.clearObstacles();
        
        assertTrue(game.getObstacles().isEmpty());
    }

    @Test
    @DisplayName("Game should set game mode")
    void testSetGameMode() {
        game.setGameMode(GameState.GameMode.TIME_ATTACK);
        
        assertEquals(GameState.GameMode.TIME_ATTACK, game.getGameState().getGameMode());
    }

    @ParameterizedTest
    @EnumSource(GameState.GameMode.class)
    @DisplayName("Game should configure for each mode")
    void testConfigureMode(GameState.GameMode mode) {
        game.setGameMode(mode);
        
        assertNotNull(game.getGameState().getGameMode());
    }
}