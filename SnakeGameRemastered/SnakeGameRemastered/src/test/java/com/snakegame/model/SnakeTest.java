package com.snakegame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {
    private Snake snake;

    @BeforeEach
    void setUp() {
        snake = new Snake();
    }

    @Test
    @DisplayName("Snake should initialize with correct default values")
    void testInitialState() {
        assertNotNull(snake.getBody());
        assertTrue(snake.getBody().isEmpty());
        assertEquals(Snake.Direction.RIGHT, snake.getDirection());
        assertEquals(0, snake.getLength());
    }

    @Test
    @DisplayName("Snake should initialize at correct position with given length")
    void testInitializedSnake() {
        snake = new Snake(10, 10, 5);
        
        assertEquals(5, snake.getLength());
        assertEquals(new Point(10, 10), snake.getHead());
    }

    @Test
    @DisplayName("Snake should move correctly in RIGHT direction")
    void testMoveRight() {
        snake = new Snake(5, 5, 3);
        snake.move();
        
        Point head = snake.getHead();
        assertEquals(6, head.x);
        assertEquals(5, head.y);
    }

    @Test
    @DisplayName("Snake should move correctly in UP direction")
    void testMoveUp() {
        snake = new Snake(5, 5, 3);
        snake.setDirection(Snake.Direction.UP);
        snake.move();
        
        Point head = snake.getHead();
        assertEquals(5, head.x);
        assertEquals(4, head.y);
    }

    @Test
    @DisplayName("Snake should move correctly in DOWN direction")
    void testMoveDown() {
        snake = new Snake(5, 5, 3);
        snake.setDirection(Snake.Direction.DOWN);
        snake.move();
        
        Point head = snake.getHead();
        assertEquals(5, head.x);
        assertEquals(6, head.y);
    }

    @Test
    @DisplayName("Snake should move correctly in LEFT direction")
    void testMoveLeft() {
        snake = new Snake(5, 5, 3);
        snake.setDirection(Snake.Direction.LEFT);
        snake.move();
        
        Point head = snake.getHead();
        assertEquals(4, head.x);
        assertEquals(5, head.y);
    }

    @Test
    @DisplayName("Snake should not reverse into itself when changing direction")
    void testNoReverseDirection() {
        snake = new Snake(5, 5, 3);
        
        snake.setDirection(Snake.Direction.LEFT);
        assertEquals(Snake.Direction.RIGHT, snake.getDirection());
        
        snake.setDirection(Snake.Direction.DOWN);
        assertEquals(Snake.Direction.DOWN, snake.getDirection());
    }

    @Test
    @DisplayName("Snake should grow when food is eaten")
    void testGrow() {
        snake = new Snake(5, 5, 3);
        int initialLength = snake.getLength();
        
        snake.grow(1);
        snake.move();
        
        assertEquals(initialLength + 1, snake.getLength());
    }

    @Test
    @DisplayName("Snake should not grow body when not growing")
    void testNoGrowWhenNotGrowing() {
        snake = new Snake(5, 5, 3);
        int initialLength = snake.getLength();
        
        snake.move();
        
        assertEquals(initialLength - 1, snake.getLength());
    }

    @Test
    @DisplayName("Snake should detect self collision")
    void testSelfCollision() {
        snake = new Snake(5, 5, 5);
        
        assertFalse(snake.collidesWithSelf());
        
        snake.setDirection(Snake.Direction.UP);
        snake.move();
        snake.move();
        snake.setDirection(Snake.Direction.LEFT);
        snake.move();
        snake.setDirection(Snake.Direction.DOWN);
        snake.move();
        
        assertTrue(snake.collidesWithSelf());
    }

    @Test
    @DisplayName("Snake should detect point containment")
    void testContainsPoint() {
        snake = new Snake(5, 5, 3);
        
        assertTrue(snake.containsPoint(new Point(5, 5)));
        assertTrue(snake.containsPoint(new Point(4, 5)));
        assertFalse(snake.containsPoint(new Point(0, 0)));
    }

    @Test
    @DisplayName("Snake should reset correctly")
    void testReset() {
        snake = new Snake(5, 5, 5);
        snake.setDirection(Snake.Direction.DOWN);
        snake.grow(2);
        
        snake.reset(10, 10, 3);
        
        assertEquals(3, snake.getLength());
        assertEquals(new Point(10, 10), snake.getHead());
        assertEquals(Snake.Direction.RIGHT, snake.getDirection());
    }

    @ParameterizedTest
    @EnumSource(Snake.Direction.class)
    @DisplayName("All directions should have valid movement values")
    void testDirectionValues(Snake.Direction direction) {
        assertNotNull(direction);
        assertTrue(direction.getDx() >= -1 && direction.getDx() <= 1);
        assertTrue(direction.getDy() >= -1 && direction.getDy() <= 1);
    }

    @ParameterizedTest
    @CsvSource({"-1,0,UP", "1,0,DOWN", "0,-1,LEFT", "0,1,RIGHT"})
    @DisplayName("Direction should return correct delta values")
    void testDirectionDelta(int dx, int dy, Snake.Direction direction) {
        assertEquals(dx, direction.getDx());
        assertEquals(dy, direction.getDy());
    }
}