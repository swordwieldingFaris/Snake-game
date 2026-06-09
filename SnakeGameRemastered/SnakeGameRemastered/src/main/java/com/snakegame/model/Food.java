package com.snakegame.model;

import java.awt.Point;
import java.util.Random;

/**
 * Represents a food item that the snake can eat.
 * Food provides points and triggers snake growth.
 */
public class Food implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Point position;
    private FoodType type;
    private int points;
    private long spawnTime;
    private static final long DEFAULT_LIFETIME = 10000;

    public Food(Point position, FoodType type) {
        this.position = position;
        this.type = type;
        this.points = type.getBasePoints();
        this.spawnTime = System.currentTimeMillis();
    }

    public Food(Point position) {
        this(position, FoodType.NORMAL);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
        this.points = type.getBasePoints();
    }

    public int getPoints() {
        return points;
    }

    public boolean isExpired() {
        return type.hasLifetime() && 
               (System.currentTimeMillis() - spawnTime) > type.getLifetime();
    }

    public static Food generateRandom(int gridWidth, int gridHeight) {
        return generateRandom(gridWidth, gridHeight, FoodType.NORMAL);
    }

    public static Food generateRandom(int gridWidth, int gridHeight, FoodType type) {
        Random random = new Random();
        int x = random.nextInt(gridWidth);
        int y = random.nextInt(gridHeight);
        return new Food(new Point(x, y), type);
    }

    public enum FoodType {
        NORMAL(10, false, 0),
        GOLD(50, false, 0),
        SILVER(25, false, 0),
        BONUS(30, true, 10000);

        private final int basePoints;
        private final boolean hasLifetime;
        private final long lifetime;

        FoodType(int basePoints, boolean hasLifetime, long lifetime) {
            this.basePoints = basePoints;
            this.hasLifetime = hasLifetime;
            this.lifetime = lifetime;
        }

        public int getBasePoints() {
            return basePoints;
        }

        public boolean hasLifetime() {
            return hasLifetime;
        }

        public long getLifetime() {
            return lifetime;
        }
    }
}