package com.snakegame.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the snake in the game.
 * Manages snake movement, growth, and collision detection.
 */
public class Snake implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private List<Point> body;
    private Direction direction;
    private Direction nextDirection;
    private boolean growing;
    private int growAmount;

    public Snake() {
        this.body = new ArrayList<>();
        this.direction = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
        this.growing = false;
        this.growAmount = 0;
    }

    public Snake(int startX, int startY, int initialLength) {
        this();
        for (int i = 0; i < initialLength; i++) {
            body.add(new Point(startX - i, startY));
        }
    }

    public List<Point> getBody() {
        return new ArrayList<>(body);
    }

    public Point getHead() {
        return body.isEmpty() ? null : body.get(0);
    }

    public Direction getDirection() {
        return direction;
    }

    public Direction getNextDirection() {
        return nextDirection;
    }

    public void setDirection(Direction direction) {
        if (direction == null) return;
        if (!isOppositeDirection(direction)) {
            this.nextDirection = direction;
        }
    }

    public void updateDirection() {
        if (!isOppositeDirection(nextDirection)) {
            this.direction = nextDirection;
        }
    }

    private boolean isOppositeDirection(Direction newDir) {
        return (direction == Direction.UP && newDir == Direction.DOWN) ||
               (direction == Direction.DOWN && newDir == Direction.UP) ||
               (direction == Direction.LEFT && newDir == Direction.RIGHT) ||
               (direction == Direction.RIGHT && newDir == Direction.LEFT);
    }

    public void move() {
        updateDirection();
        Point head = getHead();
        if (head == null) return;
        Point newHead = new Point(head.x + direction.getDx(), head.y + direction.getDy());
        
        body.add(0, newHead);
        
        if (growAmount > 0) {
            growAmount--;
            if (growAmount == 0) {
                growing = false;
            }
        } else if (body.size() > 1) {
            body.remove(body.size() - 1);
        }
    }

    public void grow(int amount) {
        this.growing = true;
        this.growAmount += amount;
    }

    public boolean collidesWithSelf() {
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPoint(Point point) {
        return body.contains(point);
    }

    public void reset(int startX, int startY, int length) {
        body.clear();
        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        growing = false;
        growAmount = 0;
        for (int i = 0; i < length; i++) {
            body.add(new Point(startX - i, startY));
        }
    }

    public int getLength() {
        return body.size();
    }

    public enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }
}