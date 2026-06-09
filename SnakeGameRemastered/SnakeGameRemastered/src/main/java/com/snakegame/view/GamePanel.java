package com.snakegame.view;

import com.snakegame.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Main game panel that renders the snake game using pixel art style.
 * Handles all visual rendering including game elements, UI, and animations.
 */
public class GamePanel extends JPanel {
    private Game game;
    private int cellSize = 20;
    private int padding = 2;
    
    private Color[] snakeColors = {
        new Color(0, 200, 0),
        new Color(0, 180, 0),
        new Color(0, 160, 0)
    };
    private Color foodColor = new Color(255, 100, 100);
    private Color obstacleColor = new Color(139, 69, 19);
    private Color gridColor = new Color(40, 40, 40);
    private Color backgroundColor = new Color(20, 20, 20);
    
    private boolean colorblindMode;
    private boolean lightMode;
    private boolean showGrid;
    private boolean showGlow;
    private AnimationManager animationManager;

    public GamePanel(Game game) {
        this.game = game;
        this.colorblindMode = false;
        this.lightMode = false;
        this.showGrid = true;
        this.showGlow = false;
        this.animationManager = new AnimationManager();
        
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(
            game.getGridWidth() * 20,
            game.getGridHeight() * 20
        ));
    }

    public void setGame(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(
            game.getGridWidth() * 20,
            game.getGridHeight() * 20
        ));
    }

    public void toggleColorblindMode() {
        setColorblindMode(!this.colorblindMode);
    }

    public void toggleLightMode() {
        setLightMode(!this.lightMode);
    }

    public void setColorblindMode(boolean enabled) {
        this.colorblindMode = enabled;
        if (colorblindMode) {
            foodColor = new Color(255, 220, 0);
        } else {
            foodColor = new Color(255, 100, 100);
        }
        repaint();
    }

    public void setLightMode(boolean enabled) {
        this.lightMode = enabled;
        if (this.lightMode) {
            backgroundColor = new Color(250, 250, 250);
            gridColor = new Color(215, 215, 215);
            obstacleColor = new Color(90, 60, 35);
            snakeColors = new Color[] {
                new Color(20, 120, 20),
                new Color(15, 105, 15),
                new Color(10, 90, 10)
            };
        } else {
            backgroundColor = new Color(20, 20, 20);
            gridColor = new Color(40, 40, 40);
            obstacleColor = new Color(139, 69, 19);
            snakeColors = new Color[] {
                new Color(0, 200, 0),
                new Color(0, 180, 0),
                new Color(0, 160, 0)
            };
        }
        setBackground(backgroundColor);
        repaint();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    public void setShowGlow(boolean showGlow) {
        this.showGlow = showGlow;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawBackground(g2d);
        
        if (game == null) return;
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int gridWidth = game.getGridWidth();
        int gridHeight = game.getGridHeight();
        
        cellSize = Math.max(1, Math.min(panelWidth / gridWidth, panelHeight / gridHeight));
        padding = Math.max(1, cellSize / 10);
        
        int offsetX = (panelWidth - gridWidth * cellSize) / 2;
        int offsetY = (panelHeight - gridHeight * cellSize) / 2;
        
        g2d.translate(offsetX, offsetY);
        
        drawGrid(g2d);
        drawObstacles(g2d);
        drawFood(g2d);
        drawPowerUps(g2d);
        drawSnake(g2d);
        drawEffects(g2d);
        
        g2d.translate(-offsetX, -offsetY);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawGrid(Graphics2D g2d) {
        if (!showGrid) return;
        
        g2d.setColor(gridColor);
        g2d.setStroke(new BasicStroke(0.5f));
        
        int width = game.getGridWidth() * cellSize;
        int height = game.getGridHeight() * cellSize;
        
        for (int x = 0; x <= width; x += cellSize) {
            g2d.drawLine(x, 0, x, height);
        }
        
        for (int y = 0; y <= height; y += cellSize) {
            g2d.drawLine(0, y, width, y);
        }
    }

    private void drawSnake(Graphics2D g2d) {
        Snake snake = game.getSnake();
        if (snake == null) return;
        
        java.util.List<java.awt.Point> body = snake.getBody();
        
        for (int i = body.size() - 1; i >= 0; i--) {
            java.awt.Point segment = body.get(i);
            int x = segment.x * cellSize + padding;
            int y = segment.y * cellSize + padding;
            int size = cellSize - (padding * 2);
            
            Color color = getSnakeColor(i, body.size());
            
            if (i == 0) {
                drawSnakeHead(g2d, x, y, size, color);
            } else {
                drawSnakeBody(g2d, x, y, size, color);
            }
        }
    }

    private Color getSnakeColor(int index, int total) {
        if (colorblindMode) {
            Color[] cbColors = {
                new Color(0, 100, 200),
                new Color(0, 80, 180),
                new Color(0, 60, 160)
            };
            return cbColors[index % cbColors.length];
        }
        return snakeColors[index % snakeColors.length];
    }

    private void drawSnakeHead(Graphics2D g2d, int x, int y, int size, Color color) {
        if (showGlow) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
            g2d.fillOval(x - 4, y - 4, size + 8, size + 8);
        }
        drawCube(g2d, x, y, size, color);
        
        // Eyes
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + size/4, y + size/4, size/5, size/5);
        g2d.fillOval(x + size*3/4 - size/5, y + size/4, size/5, size/5);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x + size/4 + 1, y + size/4 + 1, size/10, size/10);
        g2d.fillOval(x + size*3/4 - size/5 + 1, y + size/4 + 1, size/10, size/10);
    }

    private void drawSnakeBody(Graphics2D g2d, int x, int y, int size, Color color) {
        if (showGlow) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
            g2d.fillOval(x - 2, y - 2, size + 4, size + 4);
        }
        drawCube(g2d, x, y, size, color);
    }

    private void drawCube(Graphics2D g2d, int x, int y, int size, Color baseColor) {
        int d = size / 3; // Depth of the 3D effect
        
        Color topColor = new Color(
            Math.min(255, baseColor.getRed() + 40),
            Math.min(255, baseColor.getGreen() + 40),
            Math.min(255, baseColor.getBlue() + 40)
        );
        Color rightColor = new Color(
            Math.max(0, baseColor.getRed() - 60),
            Math.max(0, baseColor.getGreen() - 60),
            Math.max(0, baseColor.getBlue() - 60)
        );

        int[] topX = {x, x + d, x + size + d, x + size};
        int[] topY = {y, y - d, y - d, y};
        g2d.setColor(topColor);
        g2d.fillPolygon(topX, topY, 4);

        int[] rightX = {x + size, x + size + d, x + size + d, x + size};
        int[] rightY = {y, y - d, y + size - d, y + size};
        g2d.setColor(rightColor);
        g2d.fillPolygon(rightX, rightY, 4);

        g2d.setColor(baseColor);
        g2d.fillRect(x, y, size, size);
        
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawPolygon(topX, topY, 4);
        g2d.drawPolygon(rightX, rightY, 4);
        g2d.drawRect(x, y, size, size);
    }

    private void drawSphere(Graphics2D g2d, int x, int y, int size, Color baseColor) {
        float cx = x + size * 0.35f;
        float cy = y + size * 0.35f;
        
        Color highlight = new Color(255, 255, 255, 200);
        Color shadow = new Color(
            Math.max(0, baseColor.getRed() - 80),
            Math.max(0, baseColor.getGreen() - 80),
            Math.max(0, baseColor.getBlue() - 80)
        );
        
        java.awt.RadialGradientPaint paint = new java.awt.RadialGradientPaint(
            new java.awt.geom.Point2D.Float(cx, cy),
            size * 0.8f,
            new float[]{0.0f, 0.5f, 1.0f},
            new Color[]{highlight, baseColor, shadow}
        );
        
        g2d.setPaint(paint);
        g2d.fillOval(x, y, size, size);
        
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawOval(x, y, size, size);
    }

    private void drawFood(Graphics2D g2d) {
        Food food = game.getFood();
        if (food == null) return;
        
        java.awt.Point position = food.getPosition();
        int x = position.x * cellSize + padding;
        int y = position.y * cellSize + padding;
        int size = cellSize - (padding * 2);
        
        Color color = getFoodColor(food.getType());
        
        if (showGlow) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
            g2d.fillOval(x - 3, y - 3, size + 6, size + 6);
        }
        
        if (colorblindMode) {
            int[] xs = {x + size / 2, x + size, x + size / 2, x};
            int[] ys = {y, y + size / 2, y + size, y + size / 2};
            g2d.setColor(color);
            g2d.fillPolygon(xs, ys, 4);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xs, ys, 4);
        } else {
            drawSphere(g2d, x, y, size, color);
        }
    }

    private Color getFoodColor(Food.FoodType type) {
        switch (type) {
            case GOLD:
                return new Color(255, 215, 0);
            case SILVER:
                return new Color(192, 192, 192);
            case BONUS:
                return new Color(255, 165, 0);
            default:
                return foodColor;
        }
    }

    private void drawObstacles(Graphics2D g2d) {
        for (Obstacle obstacle : game.getObstacles()) {
            java.awt.Point position = obstacle.getPosition();
            int x = position.x * cellSize + padding;
            int y = position.y * cellSize + padding;
            int size = cellSize - (padding * 2);
            
            Color color = getObstacleColor(obstacle);
            drawCube(g2d, x, y, size, color);
            
            if (obstacle.isDestructible()) {
                int healthPercent = obstacle.getHealth() * 100 / obstacle.getType().getMaxHealth();
                g2d.setColor(new Color(100, 100, 100));
                g2d.fillRect(x, y + size - 4, size * healthPercent / 100, 3);
            }
        }
    }

    private Color getObstacleColor(Obstacle obstacle) {
        if (obstacle.getType().isMoving()) {
            return new Color(180, 100, 50);
        } else if (obstacle.isDestructible()) {
            return new Color(160, 82, 45);
        }
        return obstacleColor;
    }

    private void drawPowerUps(Graphics2D g2d) {
        for (PowerUp powerUp : game.getPowerUps()) {
            java.awt.Point position = powerUp.getPosition();
            int x = position.x * cellSize + padding;
            int y = position.y * cellSize + padding;
            int size = cellSize - (padding * 2);
            
            Color color = getPowerUpColor(powerUp.getType());
            
            if (showGlow) {
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
                g2d.fillOval(x - 3, y - 3, size + 6, size + 6);
            }
            
            g2d.setColor(color);
            g2d.fillOval(x, y, size, size);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String symbol = getPowerUpSymbol(powerUp.getType());
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(symbol, x + (size - fm.stringWidth(symbol)) / 2, y + fm.getAscent() + 2);
        }
    }

    private Color getPowerUpColor(PowerUp.PowerUpType type) {
        switch (type) {
            case SPEED_BOOST:
                return new Color(255, 0, 255);
            case SHIELD:
                return new Color(0, 200, 255);
            case SCORE_MULTIPLIER:
                return new Color(255, 255, 0);
            case SLOW_MOTION:
                return new Color(100, 100, 255);
            default:
                return Color.WHITE;
        }
    }

    private String getPowerUpSymbol(PowerUp.PowerUpType type) {
        switch (type) {
            case SPEED_BOOST:
                return "⚡";
            case SHIELD:
                return "🛡";
            case SCORE_MULTIPLIER:
                return "×2";
            case SLOW_MOTION:
                return "⏳";
            default:
                return "?";
        }
    }

    private void drawEffects(Graphics2D g2d) {
        if (game.getGameState().getShieldActive().isActive()) {
            drawShieldEffect(g2d);
        }
        
        animationManager.drawAnimations(g2d);
    }

    private void drawShieldEffect(Graphics2D g2d) {
        Snake snake = game.getSnake();
        if (snake == null || snake.getHead() == null) return;
        
        java.awt.Point head = snake.getHead();
        int x = head.x * cellSize;
        int y = head.y * cellSize;
        int size = cellSize;
        
        g2d.setColor(new Color(100, 200, 255, 100));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(x - 2, y - 2, size + 4, size + 4);
    }

    public void addAnimation(AnimationManager.Animation animation) {
        animationManager.addAnimation(animation);
    }
}

class AnimationManager {
    private java.util.List<Animation> animations = new java.util.ArrayList<>();

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    public void update() {
        animations.removeIf(Animation::isFinished);
        animations.forEach(Animation::update);
    }

    public void drawAnimations(Graphics2D g2d) {
        for (Animation animation : animations) {
            animation.draw(g2d);
        }
    }

    public interface Animation {
        void update();
        void draw(Graphics2D g2d);
        boolean isFinished();
    }
}