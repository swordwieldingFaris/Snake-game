package com.snakegame.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game model that coordinates all game components.
 * Handles collision detection, food generation, and game logic.
 */
public class Game implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Snake snake;
    private Food food;
    private List<Obstacle> obstacles;
    private List<PowerUp> powerUps;
    private GameState gameState;
    private int gridWidth;
    private int gridHeight;
    private int baseSpeed;
    private int currentSpeed;
    private Random random;
    private long lastMoveTime;
    private long modeStartTime;
    private long lastSurvivalSpawnTime;

    public Game(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.snake = new Snake(gridWidth / 2, gridHeight / 2, 3);
        this.food = null;
        this.obstacles = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.gameState = new GameState();
        this.baseSpeed = 150;
        this.currentSpeed = baseSpeed;
        this.random = new Random();
        this.modeStartTime = 0;
        this.lastSurvivalSpawnTime = 0;
        spawnFood();
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public List<Obstacle> getObstacles() {
        return new ArrayList<>(obstacles);
    }

    public List<PowerUp> getPowerUps() {
        return new ArrayList<>(powerUps);
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
        this.currentSpeed = baseSpeed;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setSpeed(int speed) {
        this.baseSpeed = Math.max(50, Math.min(300, speed));
        if (!isSlowMotionActive()) {
            this.currentSpeed = baseSpeed;
        }
    }

    public void increaseSpeed() {
        this.baseSpeed = Math.max(50, baseSpeed - 10);
        if (!isSlowMotionActive()) {
            this.currentSpeed = baseSpeed;
        }
    }

    public void applySpeedBoost() {
        this.currentSpeed = baseSpeed / 2;
    }

    public void applySlowMotion() {
        this.currentSpeed = baseSpeed * 2;
    }

    public void removeSpeedModifiers() {
        if (!isSlowMotionActive()) {
            this.currentSpeed = baseSpeed;
        }
    }

    public boolean isSlowMotionActive() {
        return gameState.isSlowMotionActive();
    }

    public void start() {
        gameState.start();
        lastMoveTime = System.currentTimeMillis();
        modeStartTime = lastMoveTime;
        lastSurvivalSpawnTime = lastMoveTime;
    }

    public void pause() {
        gameState.pause();
    }

    public void resume() {
        gameState.resume();
        lastMoveTime = System.currentTimeMillis();
    }

    public void update() {
        if (gameState.getStatus() != GameState.GameStatus.PLAYING) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= currentSpeed) {
            lastMoveTime = currentTime;

            snake.move();

            if (checkWallCollision()) {
                handleCollision();
                return;
            }

            if (checkSelfCollision()) {
                handleCollision();
                return;
            }

            if (checkObstacleCollision()) {
                handleCollision();
                return;
            }

            if (checkFoodCollision()) {
                handleFoodEaten();
            }

            checkPowerUpCollision();
            updateObstacles();
            updatePowerUps();
            updateModeSpecificRules(currentTime);
        }
    }

    private boolean checkWallCollision() {
        Point head = snake.getHead();
        return head.x < 0 || head.x >= gridWidth || 
               head.y < 0 || head.y >= gridHeight;
    }

    private boolean checkSelfCollision() {
        if (gameState.getShieldActive().isActive()) {
            return false;
        }
        return snake.collidesWithSelf();
    }

    private boolean checkObstacleCollision() {
        Point head = snake.getHead();
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(head)) {
                if (obstacle.isDestructible()) {
                    obstacle.takeDamage(1);
                    if (obstacle.isDestroyed()) {
                        obstacles.remove(obstacle);
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkFoodCollision() {
        Point head = snake.getHead();
        return food != null && food.getPosition().equals(head);
    }

    private void checkPowerUpCollision() {
        Point head = snake.getHead();
        PowerUp toRemove = null;
        
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getPosition().equals(head)) {
                applyPowerUp(powerUp);
                toRemove = powerUp;
                break;
            }
        }
        
        if (toRemove != null) {
            powerUps.remove(toRemove);
        }
    }

    private void handleCollision() {
        if (gameState.getShieldActive().isActive()) {
            gameState.getShieldActive().deactivate();
        } else {
            gameState.stop();
        }
    }

    private void handleFoodEaten() {
        snake.grow(1);
        gameState.addScore(10);
        
        gameState.incrementFoodEaten();
        gameState.incrementCombo();
        
        int combo = gameState.getComboCount();
        gameState.getMultiplier().increment(combo);
        
        if (combo > 5) {
            spawnBonusFood();
        }
        
        int currentScore = gameState.getScore();
        int newLevel = 1 + (currentScore / 200);
        
        if (newLevel > 5) {
            gameState.setStatus(GameState.GameStatus.WON);
            return;
        }

        if (newLevel > gameState.getLevel()) {
            gameState.setStatus(GameState.GameStatus.LEVEL_UP);
            gameState.setLevel(newLevel);
            setSpeedForLevel(newLevel);
        }
        
        spawnFood();
        
        if (gameState.getGameMode() == GameState.GameMode.CLASSIC) {
            checkWinCondition();
        }
    }

    private void updateModeSpecificRules(long currentTime) {
        if (gameState.getGameMode() == GameState.GameMode.TIME_ATTACK) {
            long elapsed = currentTime - modeStartTime;
            if (elapsed >= 60_000) {
                gameState.stop();
            }
            return;
        }

        if (gameState.getGameMode() == GameState.GameMode.SURVIVAL) {
            if (currentTime - lastSurvivalSpawnTime >= 12_000) {
                lastSurvivalSpawnTime = currentTime;
                if (obstacles.size() < 30) {
                    addObstacles(1, Obstacle.ObstacleType.MOVING);
                }
                increaseSpeed();
            }
        }
    }

    public void prepareNextLevel() {
        snake.reset(gridWidth / 2, gridHeight / 2, 3);
        obstacles.clear();
        generateMapForLevel(gameState.getLevel());
        
        food = null;
        spawnFood();
        powerUps.clear();
        
        lastMoveTime = System.currentTimeMillis();
    }

    private void generateMapForLevel(int level) {
        if (level == 2) {
            for (int y = 4; y < gridHeight - 4; y++) {
                obstacles.add(new Obstacle(new Point(5, y), Obstacle.ObstacleType.STATIC));
                obstacles.add(new Obstacle(new Point(gridWidth - 6, y), Obstacle.ObstacleType.STATIC));
            }
        } else if (level == 3) {
            int minX = gridWidth / 4;
            int maxX = gridWidth * 3 / 4;
            int minY = gridHeight / 4;
            int maxY = gridHeight * 3 / 4;
            
            for (int x = minX; x <= maxX; x++) {
                if (x < gridWidth / 2 - 2 || x > gridWidth / 2 + 2) {
                    obstacles.add(new Obstacle(new Point(x, minY), Obstacle.ObstacleType.STATIC));
                    obstacles.add(new Obstacle(new Point(x, maxY), Obstacle.ObstacleType.STATIC));
                }
            }
            for (int y = minY; y <= maxY; y++) {
                if (y < gridHeight / 2 - 2 || y > gridHeight / 2 + 2) {
                    obstacles.add(new Obstacle(new Point(minX, y), Obstacle.ObstacleType.STATIC));
                    obstacles.add(new Obstacle(new Point(maxX, y), Obstacle.ObstacleType.STATIC));
                }
            }
        } else if (level == 4) {
            int midX = gridWidth / 2;
            int midY = gridHeight / 2;
            for (int i = 0; i < 8; i++) {
                obstacles.add(new Obstacle(new Point(midX, i), Obstacle.ObstacleType.STATIC));
                obstacles.add(new Obstacle(new Point(midX, gridHeight - 1 - i), Obstacle.ObstacleType.STATIC));
                obstacles.add(new Obstacle(new Point(i, midY), Obstacle.ObstacleType.STATIC));
                obstacles.add(new Obstacle(new Point(gridWidth - 1 - i, midY), Obstacle.ObstacleType.STATIC));
            }
        } else if (level == 5) {
            for (int x = 4; x < gridWidth - 4; x += 4) {
                for (int y = 4; y < gridHeight - 4; y += 4) {
                    if (x != gridWidth / 2 && y != gridHeight / 2) {
                        obstacles.add(new Obstacle(new Point(x, y), Obstacle.ObstacleType.STATIC));
                    }
                }
            }
        }
    }

    private void setSpeedForLevel(int level) {
        int newSpeed = 150 - ((level - 1) * 10);
        this.baseSpeed = newSpeed;
        if (!isSlowMotionActive()) {
            this.currentSpeed = baseSpeed;
        }
    }

    private void applyPowerUp(PowerUp powerUp) {
        PowerUp.PowerUpType type = powerUp.getType();
        
        switch (type) {
            case SPEED_BOOST:
                applySpeedBoost();
                break;
            case SHIELD:
                gameState.getShieldActive().activate();
                break;
            case SCORE_MULTIPLIER:
                gameState.getMultiplier().setValue(gameState.getMultiplier().getValue() + 1);
                break;
            case SLOW_MOTION:
                applySlowMotion();
                gameState.setSlowMotionActive(true);
                break;
        }
        
        gameState.setActivePowerUpCount(gameState.getActivePowerUpCount() + 1);
    }

    private void updateObstacles() {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getType().isMoving()) {
                obstacle.update(gridWidth, gridHeight);
            }
        }
    }

    private void updatePowerUps() {
        PowerUp toRemove = null;
        
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isExpired()) {
                toRemove = powerUp;
            }
        }
        
        if (toRemove != null) {
            powerUps.remove(toRemove);
        }
    }

    private void checkWinCondition() {
        if (snake.getLength() >= gridWidth * gridHeight) {
            gameState.setStatus(GameState.GameStatus.WON);
        }
    }

    public void spawnFood() {
        Point position = generateFreePosition();
        if (position != null) {
            this.food = new Food(position);
        }
    }

    public void spawnBonusFood() {
        Point position = generateFreePosition();
        if (position != null) {
            this.food = new Food(position, Food.FoodType.BONUS);
        }
    }

    public void spawnPowerUp() {
        Point position = generateFreePosition();
        if (position != null) {
            powerUps.add(PowerUp.generateRandom(gridWidth, gridHeight));
        }
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void clearObstacles() {
        obstacles.clear();
    }

    public void addObstacles(int count, Obstacle.ObstacleType type) {
        for (int i = 0; i < count; i++) {
            Point position = generateFreePosition();
            if (position != null) {
                obstacles.add(new Obstacle(position, type));
            }
        }
    }

    private Point generateFreePosition() {
        List<Point> occupied = new ArrayList<>();
        occupied.addAll(snake.getBody());
        
        if (food != null) {
            occupied.add(food.getPosition());
        }
        
        for (PowerUp powerUp : powerUps) {
            occupied.add(powerUp.getPosition());
        }
        
        for (Obstacle obstacle : obstacles) {
            occupied.add(obstacle.getPosition());
        }
        
        List<Point> freePositions = new ArrayList<>();
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Point point = new Point(x, y);
                if (!occupied.contains(point)) {
                    freePositions.add(point);
                }
            }
        }
        
        if (freePositions.isEmpty()) {
            return null;
        }
        
        return freePositions.get(random.nextInt(freePositions.size()));
    }

    public void reset() {
        snake.reset(gridWidth / 2, gridHeight / 2, 3);
        gameState.reset();
        food = null;
        obstacles.clear();
        powerUps.clear();
        configureForMode(gameState.getGameMode());
        currentSpeed = baseSpeed;
        spawnFood();
        lastMoveTime = System.currentTimeMillis();
        modeStartTime = lastMoveTime;
        lastSurvivalSpawnTime = lastMoveTime;
    }

    public void setGameMode(GameState.GameMode mode) {
        gameState.setGameMode(mode);
        configureForMode(mode);
    }

    private void configureForMode(GameState.GameMode mode) {
        obstacles.clear();
        powerUps.clear();
        
        switch (mode) {
            case CLASSIC:
                baseSpeed = 150;
                break;
            case TIME_ATTACK:
                baseSpeed = 100;
                addObstacles(5, Obstacle.ObstacleType.STATIC);
                break;
            case SURVIVAL:
                baseSpeed = 120;
                addObstacles(10, Obstacle.ObstacleType.MOVING);
                break;
            case PUZZLE:
                baseSpeed = 180;
                addObstacles(8, Obstacle.ObstacleType.DESTRUCTIBLE);
                break;
        }
        
        currentSpeed = baseSpeed;
    }
}