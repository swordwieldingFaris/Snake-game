package com.snakegame.controller;

import com.snakegame.model.*;
import java.awt.event.*;

/**
 * Controller for handling user input.
 * Processes keyboard events and translates them to game actions.
 */
public class InputController implements KeyListener {
    private Game game;
    private boolean keyProcessingEnabled;
    private Snake lastProcessedDirection;

    public InputController(Game game) {
        this.game = game;
        this.keyProcessingEnabled = true;
        this.lastProcessedDirection = null;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setKeyProcessingEnabled(boolean enabled) {
        this.keyProcessingEnabled = enabled;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!keyProcessingEnabled || game == null) return;
        
        GameState state = game.getGameState();
        if (state.getStatus() != GameState.GameStatus.PLAYING) {
            handleNonPlayingState(e);
            return;
        }
        
        Snake snake = game.getSnake();
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                snake.setDirection(Snake.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (!e.isShiftDown()) {
                    snake.setDirection(Snake.Direction.DOWN);
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                snake.setDirection(Snake.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                snake.setDirection(Snake.Direction.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                togglePause();
                break;
        }
    }

    private void handleNonPlayingState(KeyEvent e) {
        GameState state = game.getGameState();
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_SPACE:
                if (state.getStatus() == GameState.GameStatus.READY ||
                    state.getStatus() == GameState.GameStatus.GAME_OVER) {
                    startGame();
                } else if (state.getStatus() == GameState.GameStatus.PAUSED) {
                    resumeGame();
                }
                break;
            case KeyEvent.VK_N:
                if (keyCode == KeyEvent.VK_N) {
                    startNewGame();
                }
                break;
        }
    }

    private void togglePause() {
        GameState state = game.getGameState();
        if (state.getStatus() == GameState.GameStatus.PLAYING) {
            game.pause();
        } else if (state.getStatus() == GameState.GameStatus.PAUSED) {
            game.resume();
        }
    }

    private void startGame() {
        game.start();
    }

    private void resumeGame() {
        game.resume();
    }

    public void startNewGame() {
        game.reset();
        game.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}