package com.snakegame;

import com.snakegame.model.*;
import com.snakegame.view.*;
import com.snakegame.controller.*;
import com.snakegame.util.AppConfig;
import javax.swing.*;

/**
 * Main application entry point for Snake Game: Classic Remastered.
 * Initializes the game components and starts the game loop.
 */
public class SnakeGameApplication {
    private static final int DEFAULT_GRID_WIDTH = AppConfig.gridWidth();
    private static final int DEFAULT_GRID_HEIGHT = AppConfig.gridHeight();
    private static final int DEFAULT_SPEED = AppConfig.speedClassic();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Failed to set look and feel: " + e.getMessage());
            }

            // Start directly on the main menu instead of showing a popup
            showMainMenu();
        });
    }

    private static void showMainMenu() {
        Game game = new Game(DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT);
        game.setBaseSpeed(DEFAULT_SPEED);

        GameFrame frame = new GameFrame(game);
        frame.showScreen("MENU");   // Ensure the menu is the entry point
        frame.setDarkTheme(AppConfig.isDarkTheme());
        frame.setColorblindMode(AppConfig.isColorblindEnabled());
        com.snakegame.util.SoundManager.setSoundEnabled(AppConfig.isSoundEnabled());
        frame.setVisible(true);

        GameController controller = new GameController(game, frame);
        // Do not start the game loop on menu launch.
        // Gameplay should begin only after the user selects a mode.
    }

    public static void startGameWithSettings(int gridWidth, int gridHeight, int speed, GameState.GameMode mode) {
        Game game = new Game(gridWidth, gridHeight);
        game.setBaseSpeed(speed);
        game.setGameMode(mode);

        GameFrame frame = new GameFrame(game);
        frame.showScreen("GAME");
        frame.setDarkTheme(AppConfig.isDarkTheme());
        frame.setColorblindMode(AppConfig.isColorblindEnabled());
        com.snakegame.util.SoundManager.setSoundEnabled(AppConfig.isSoundEnabled());
        frame.setVisible(true);

        GameController controller = new GameController(game, frame);
        controller.start();
    }
}
