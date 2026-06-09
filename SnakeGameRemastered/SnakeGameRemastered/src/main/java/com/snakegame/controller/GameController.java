package com.snakegame.controller;

import com.snakegame.model.*;
import com.snakegame.view.GamePanel;
import com.snakegame.view.GameFrame;
import com.snakegame.service.GameService;
import com.snakegame.model.GameStatistics;
import com.snakegame.util.StatisticsManager;
import com.snakegame.util.AppConfig;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Main game controller that coordinates game loop and view updates.
 * Manages game state, timing, and rendering.
 */
public class GameController {
    private Game game;
    private GameFrame frame;
    private InputController inputController;
    private GameLoop gameLoop;
    private GameService gameService;
    private GameStatistics statistics;
    private boolean colorblindEnabled;
    private volatile boolean levelUpInProgress;
    private volatile boolean gameEndInProgress;
    private boolean running;

    public GameController(Game game, GameFrame frame) {
        this.game = game;
        this.frame = frame;
        this.inputController = new InputController(game);
        this.gameLoop = new GameLoop(this);
        this.gameService = new GameService();
        this.statistics = StatisticsManager.load();
        this.colorblindEnabled = AppConfig.isColorblindEnabled();
        this.levelUpInProgress = false;
        this.gameEndInProgress = false;
        this.running = false;
        
        setupMenuListeners();
    }

    private void setupMenuListeners() {
        if (frame == null) return;
        frame.addMenuListener(e -> handleMenuAction(e.getActionCommand()));
    }

    private void handleMenuAction(String action) {
        if (action == null) return;
        switch (action) {
            case "NEW_GAME":
                reset();
                start();
                break;
            case "PAUSE":
                pause();
                break;
            case "RESTART":
                reset();
                start();
                break;
            case "THEME_DARK":
                frame.setDarkTheme(true);
                AppConfig.setDarkTheme(true);
                break;
            case "THEME_LIGHT":
                frame.setDarkTheme(false);
                AppConfig.setDarkTheme(false);
                break;
            case "COLORBLIND":
                colorblindEnabled = !colorblindEnabled;
                frame.setColorblindMode(colorblindEnabled);
                AppConfig.setColorblindEnabled(colorblindEnabled);
                break;
            case "SAVE":
                saveGame();
                break;
            case "LOAD":
                loadGame();
                break;
            case "RESUME":
                resume();
                break;
        }
    }

    public void start() {
        if (running) return;
        
        running = true;
        gameEndInProgress = false;
        game.start();
        frame.showGame();
        frame.hidePauseOverlay();
        statistics.recordGameStarted();
        StatisticsManager.save(statistics);
        
        frame.getGamePanel().addKeyListener(inputController);
        frame.getGamePanel().requestFocusInWindow();
        setupPauseKeyBinding();
        
        gameLoop.start();
    }

    private void setupPauseKeyBinding() {
        JComponent panel = frame.getGamePanel();
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pauseOverlay");
        panel.getActionMap().put("pauseOverlay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }
        });
    }

    public void stop() {
        running = false;
        gameLoop.stop();
    }

    public void pause() {
        GameState.GameStatus status = game.getGameState().getStatus();
        if (status == GameState.GameStatus.PLAYING) {
            game.pause();
            frame.showPauseOverlay();
        } else if (status == GameState.GameStatus.PAUSED) {
            resume();
        }
    }

    public void resume() {
        game.resume();
        frame.hidePauseOverlay();
    }

    public void reset() {
        game.reset();
    }

    public void update() {
        if (!running) return;
        
        game.update();
        
        frame.getGamePanel().repaint();
        frame.updateStatusBar(game.getGameState());
        
        if (game.getGameState().getStatus() == GameState.GameStatus.GAME_OVER ||
            game.getGameState().getStatus() == GameState.GameStatus.WON) {
            if (!gameEndInProgress) {
                gameEndInProgress = true;
                javax.swing.SwingUtilities.invokeLater(this::handleGameEnd);
            }
        } else if (game.getGameState().getStatus() == GameState.GameStatus.LEVEL_UP && !levelUpInProgress) {
            levelUpInProgress = true;
            javax.swing.SwingUtilities.invokeLater(this::handleLevelUp);
        }
    }

    private void handleLevelUp() {
        if (game.getGameState().getStatus() != GameState.GameStatus.LEVEL_UP) {
            levelUpInProgress = false;
            return;
        }

        try {
            int nextLevel = game.getGameState().getLevel();
            String message = "Level " + (nextLevel - 1) + " Complete!\n\nGet ready for Level " + nextLevel + ".\nObstacles have been added!";

            javax.swing.JOptionPane.showMessageDialog(
                frame, message, "Level Up!",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            game.prepareNextLevel();
            game.getGameState().setStatus(GameState.GameStatus.PLAYING);
        } finally {
            levelUpInProgress = false;
        }
    }

    private void handleGameEnd() {
        GameState.GameStatus status = game.getGameState().getStatus();
        if (status != GameState.GameStatus.GAME_OVER && status != GameState.GameStatus.WON) {
            gameEndInProgress = false;
            return;
        }

        running = false;
        gameLoop.stop();
        
        GameState state = game.getGameState();
        java.util.List<String> newBadges = statistics.recordGameEnded(state, game.getSnake().getLength());
        StatisticsManager.save(statistics);
        gameService.saveScore("Player", state);
        for (String badge : newBadges) {
            frame.showToast("Achievement unlocked: " + badge);
        }
        if (state.getStatus() == GameState.GameStatus.WON) {
            showGameWonDialog();
        } else {
            showGameOverDialog();
        }
        gameEndInProgress = false;
    }

    private void showGameOverDialog() {
        String message = String.format(
            "Game Over!\n\nScore: %d\nFood Eaten: %d\nCombo: %d",
            game.getGameState().getScore(),
            game.getGameState().getFoodEaten(),
            game.getGameState().getComboCount()
        );
        Object[] options = {"Start Again", "Exit"};
        int n = javax.swing.JOptionPane.showOptionDialog(
            frame, message, "Game Over",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (n == 0) {
            reset();
            start();
        } else {
            reset();
            frame.showMainMenu();
        }
    }

    private void showGameWonDialog() {
        String message = String.format(
            "Congratulations! You have finished the game!\n\nScore: %d\nTime: %d seconds\nFood Eaten: %d",
            game.getGameState().getScore(),
            game.getGameState().getElapsedTime() / 1000,
            game.getGameState().getFoodEaten()
        );
        
        Object[] options = {"Start Again", "Exit"};
        int n = javax.swing.JOptionPane.showOptionDialog(
            frame, message, "You Win!",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (n == 0) {
            reset();
            start();
        } else {
            reset();
            frame.showMainMenu();
        }
    }

    private void saveGame() {
        try (java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream("savegame.dat"))) {
            out.writeObject(game);
            out.writeObject(statistics);
            javax.swing.JOptionPane.showMessageDialog(frame, "Game saved successfully!", "Save Game", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(frame, "Failed to save game: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream("savegame.dat"))) {
            Game loadedGame = (Game) in.readObject();
            Object loadedStatistics = null;
            try {
                loadedStatistics = in.readObject();
            } catch (java.io.EOFException ignored) {
                // Backward-compatible with older save files.
            }
            boolean wasRunning = running;
            if (running) {
                gameLoop.stop();
            }
            this.game = loadedGame;
            if (loadedStatistics instanceof GameStatistics) {
                this.statistics = (GameStatistics) loadedStatistics;
            }
            frame.getGamePanel().setGame(this.game);
            inputController.setGame(this.game);
            frame.showGame();
            if (wasRunning) {
                gameLoop.start();
            } else {
                // If it wasn't running, we might need to force a repaint to show loaded state
                frame.getGamePanel().repaint();
                frame.updateStatusBar(game.getGameState());
            }
            javax.swing.JOptionPane.showMessageDialog(frame, "Game loaded successfully!", "Load Game", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(frame, "Failed to load game: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.inputController.setGame(game);
        if (this.frame != null) {
            this.frame.getGamePanel().setGame(game);
        }
    }

    public GameFrame getFrame() {
        return frame;
    }

    public InputController getInputController() {
        return inputController;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public boolean isRunning() {
        return running;
    }
}

class GameLoop implements Runnable {
    private Thread thread;
    private GameController controller;
    private int targetFPS = 60;
    private long frameTime;

    public GameLoop(GameController controller) {
        this.controller = controller;
        this.frameTime = 1000 / targetFPS;
    }

    public void start() {
        if (thread != null && thread.isAlive()) return;
        
        thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        long accumulator = 0;
        
        while (!Thread.currentThread().isInterrupted()) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastTime;
            lastTime = currentTime;
            
            accumulator += deltaTime;
            
            while (accumulator >= frameTime) {
                controller.update();
                accumulator -= frameTime;
            }
            
            long sleepTime = frameTime - accumulator;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void setTargetFPS(int targetFPS) {
        this.targetFPS = targetFPS;
        this.frameTime = 1000 / targetFPS;
    }

    public int getTargetFPS() {
        return targetFPS;
    }
}