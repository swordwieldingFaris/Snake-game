package com.snakegame.model;

import java.awt.Point;

/**
 * Represents an obstacle in the game.
 * Obstacles can be static, moving, or destructible.
 */
public class Obstacle implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Point position;
    private ObstacleType type;
    private boolean destructible;
    private int health;
    private Point velocity;

    public Obstacle(Point position, ObstacleType type) {
        this.position = position;
        this.type = type;
        this.destructible = type.isDestructible();
        this.health = type.getMaxHealth();
        this.velocity = new Point(0, 0);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public ObstacleType getType() {
        return type;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        if (destructible) {
            health = Math.max(0, health - damage);
        }
    }

    public boolean isDestroyed() {
        return destructible && health <= 0;
    }

    public Point getVelocity() {
        return velocity;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public void update(int gridWidth, int gridHeight) {
        if (type.isMoving()) {
            int newX = position.x + velocity.x;
            int newY = position.y + velocity.y;
            
            if (newX < 0 || newX >= gridWidth) {
                velocity.x = -velocity.x;
            }
            if (newY < 0 || newY >= gridHeight) {
                velocity.y = -velocity.y;
            }
            
            position.x = Math.max(0, Math.min(gridWidth - 1, newX));
            position.y = Math.max(0, Math.min(gridHeight - 1, newY));
        }
    }

    public enum ObstacleType {
        STATIC(false, false, 1),
        MOVING(true, false, 1),
        DESTRUCTIBLE(false, true, 3),
        MOVING_DESTRUCTIBLE(true, true, 3);

        private final boolean moving;
        private final boolean destructible;
        private final int maxHealth;

        ObstacleType(boolean moving, boolean destructible, int maxHealth) {
            this.moving = moving;
            this.destructible = destructible;
            this.maxHealth = maxHealth;
        }

        public boolean isMoving() {
            return moving;
        }

        public boolean isDestructible() {
            return destructible;
        }

        public int getMaxHealth() {
            return maxHealth;
        }
    }
}