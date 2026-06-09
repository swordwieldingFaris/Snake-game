package com.snakegame.model;

import java.awt.Point;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Represents a power-up in the game.
 * Power-ups provide temporary special abilities.
 */
public class PowerUp implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Point position;
    private PowerUpType type;
    private int duration;
    private long spawnTime;
    private boolean active;
    private static final long DEFAULT_DURATION = 8000;

    public PowerUp(Point position, PowerUpType type) {
        this.position = position;
        this.type = type;
        this.duration = type.getDefaultDuration();
        this.spawnTime = System.currentTimeMillis();
        this.active = false;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public PowerUpType getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
        this.spawnTime = System.currentTimeMillis();
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isExpired() {
        if (!active) return false;
        return System.currentTimeMillis() - spawnTime > duration;
    }

    public static PowerUp generateRandom(int gridWidth, int gridHeight) {
        Random random = new Random();
        PowerUpType[] types = PowerUpType.values();
        PowerUpType type = types[random.nextInt(types.length)];
        int x = random.nextInt(gridWidth);
        int y = random.nextInt(gridHeight);
        return new PowerUp(new Point(x, y), type);
    }

    public enum PowerUpType {
        SPEED_BOOST(150, "Speed Boost"),
        SHIELD(500, "Shield"),
        SCORE_MULTIPLIER(200, "2x Score"),
        SLOW_MOTION(100, "Slow Motion");

        private final int defaultDuration;
        private final String displayName;

        PowerUpType(int defaultDuration, String displayName) {
            this.defaultDuration = defaultDuration;
            this.displayName = displayName;
        }

        public int getDefaultDuration() {
            return defaultDuration;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}