package com.snakegame.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import com.snakegame.util.AppConfig;

/**
 * Menu bar for the game with options for game modes, scores, and settings.
 */
public class MenuBar extends JMenuBar {
    private JMenu gameMenu;
    private JMenu viewMenu;
    private JMenu settingsMenu;
    private List<ActionListener> actionListeners;

    public MenuBar(GameFrame frame) {
        this.actionListeners = new ArrayList<>();
        
        createGameMenu(frame);
        createViewMenu(frame);
        createSettingsMenu();
        
        add(gameMenu);
        add(viewMenu);
        add(settingsMenu);
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    private void notifyListeners(String action) {
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new java.awt.event.ActionEvent(this, 0, action));
        }
    }

    public void dispatchAction(String action) {
        notifyListeners(action);
    }

    private void createGameMenu(GameFrame frame) {
        gameMenu = new JMenu("Game");
        
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> notifyListeners("NEW_GAME"));
        
        JMenuItem pauseResume = new JMenuItem("Pause");
        pauseResume.addActionListener(e -> notifyListeners("PAUSE"));

        JMenuItem saveGame = new JMenuItem("Save Game");
        saveGame.addActionListener(e -> notifyListeners("SAVE"));

        JMenuItem loadGame = new JMenuItem("Load Game");
        loadGame.addActionListener(e -> notifyListeners("LOAD"));
        
        JMenuItem gameOver = new JMenuItem("Exit Game");
        gameOver.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(frame, 
                "Are you sure you want to exit?", 
                "Confirm Exit", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        gameMenu.add(newGame);
        gameMenu.addSeparator();
        gameMenu.add(pauseResume);
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.addSeparator();
        gameMenu.add(gameOver);
    }

    private void createViewMenu(GameFrame frame) {
        viewMenu = new JMenu("View");
        
        JMenuItem highScores = new JMenuItem("High Scores");
        highScores.addActionListener(e -> frame.showHighScores());
        
        JMenuItem statistics = new JMenuItem("Statistics");
        statistics.addActionListener(e -> frame.showStatistics());

        JMenuItem achievements = new JMenuItem("Achievements");
        achievements.addActionListener(e -> frame.showAchievements());
        
        viewMenu.add(highScores);
        viewMenu.add(statistics);
        viewMenu.add(achievements);
    }

    private void createSettingsMenu() {
        settingsMenu = new JMenu("Settings");
        
        JCheckBoxMenuItem soundToggle = new JCheckBoxMenuItem("Enable Sound", AppConfig.isSoundEnabled());
        soundToggle.addActionListener(e -> {
            com.snakegame.util.SoundManager.setSoundEnabled(soundToggle.isSelected());
            AppConfig.setSoundEnabled(soundToggle.isSelected());
        });
        
        JCheckBoxMenuItem darkMode = new JCheckBoxMenuItem("Dark Mode", AppConfig.isDarkTheme());
        darkMode.addActionListener(e -> notifyListeners("THEME_DARK"));

        JCheckBoxMenuItem lightMode = new JCheckBoxMenuItem("Light Mode", !AppConfig.isDarkTheme());
        lightMode.addActionListener(e -> notifyListeners("THEME_LIGHT"));

        ButtonGroup themeGroup = new ButtonGroup();
        themeGroup.add(darkMode);
        themeGroup.add(lightMode);

        JCheckBoxMenuItem colorblindMode = new JCheckBoxMenuItem("Color-Blind Mode", AppConfig.isColorblindEnabled());
        colorblindMode.addActionListener(e -> notifyListeners("COLORBLIND"));

        JMenu difficultyMenu = new JMenu("Difficulty");
        JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy", AppConfig.getDifficulty() == 1);
        JRadioButtonMenuItem normal = new JRadioButtonMenuItem("Normal", AppConfig.getDifficulty() == 2);
        JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard", AppConfig.getDifficulty() == 3);
        ButtonGroup difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easy);
        difficultyGroup.add(normal);
        difficultyGroup.add(hard);
        easy.addActionListener(e -> AppConfig.setDifficulty(1));
        normal.addActionListener(e -> AppConfig.setDifficulty(2));
        hard.addActionListener(e -> AppConfig.setDifficulty(3));
        difficultyMenu.add(easy);
        difficultyMenu.add(normal);
        difficultyMenu.add(hard);
        
        settingsMenu.add(soundToggle);
        settingsMenu.addSeparator();
        settingsMenu.add(darkMode);
        settingsMenu.add(lightMode);
        settingsMenu.add(colorblindMode);
        settingsMenu.addSeparator();
        settingsMenu.add(difficultyMenu);
    }
}
