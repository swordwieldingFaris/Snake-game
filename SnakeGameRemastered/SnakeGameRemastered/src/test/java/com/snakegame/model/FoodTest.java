package com.snakegame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class FoodTest {
    private Food food;

    @BeforeEach
    void setUp() {
        food = new Food(new Point(5, 5));
    }

    @Test
    @DisplayName("Food should initialize with correct position")
    void testInitialPosition() {
        assertEquals(new Point(5, 5), food.getPosition());
    }

    @Test
    @DisplayName("Food should initialize with NORMAL type")
    void testDefaultType() {
        assertEquals(Food.FoodType.NORMAL, food.getType());
    }

    @Test
    @DisplayName("Food should initialize with correct points")
    void testDefaultPoints() {
        assertEquals(10, food.getPoints());
    }

    @Test
    @DisplayName("Food should set position correctly")
    void testSetPosition() {
        Point newPos = new Point(10, 10);
        food.setPosition(newPos);
        
        assertEquals(newPos, food.getPosition());
    }

    @Test
    @DisplayName("Food should set type correctly")
    void testSetType() {
        food.setType(Food.FoodType.GOLD);
        
        assertEquals(Food.FoodType.GOLD, food.getType());
        assertEquals(50, food.getPoints());
    }

    @Test
    @DisplayName("Gold food should have correct points")
    void testGoldPoints() {
        Food goldFood = new Food(new Point(5, 5), Food.FoodType.GOLD);
        
        assertEquals(50, goldFood.getPoints());
    }

    @Test
    @DisplayName("Silver food should have correct points")
    void testSilverPoints() {
        Food silverFood = new Food(new Point(5, 5), Food.FoodType.SILVER);
        
        assertEquals(25, silverFood.getPoints());
    }

    @Test
    @DisplayName("Bonus food should have correct points and lifetime")
    void testBonusFood() {
        Food bonusFood = new Food(new Point(5, 5), Food.FoodType.BONUS);
        
        assertEquals(30, bonusFood.getPoints());
        assertTrue(bonusFood.getType().hasLifetime());
    }

    @Test
    @DisplayName("Normal food should not be expired")
    void testNormalNotExpired() {
        assertFalse(food.isExpired());
    }

    @Test
    @DisplayName("Non-expired should generate at valid position")
    void testGenerateRandom() {
        Food generatedFood = Food.generateRandom(20, 20);
        
        assertNotNull(generatedFood.getPosition());
        assertTrue(generatedFood.getPosition().x >= 0 && generatedFood.getPosition().x < 20);
        assertTrue(generatedFood.getPosition().y >= 0 && generatedFood.getPosition().y < 20);
    }

    @Test
    @DisplayName("All food types should have valid points")
    void testAllFoodTypesPoints() {
        for (Food.FoodType type : Food.FoodType.values()) {
            Food testFood = new Food(new Point(0, 0), type);
            assertTrue(testFood.getPoints() > 0);
        }
    }
}