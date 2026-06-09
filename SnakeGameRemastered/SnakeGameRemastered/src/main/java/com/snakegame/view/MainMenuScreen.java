package com.snakegame.view;

import com.snakegame.SnakeGameApplication;
import com.snakegame.model.GameState;
import com.snakegame.util.AppConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Main menu screen for game mode selection.
 */
public class MainMenuScreen extends JPanel {
    private GameFrame gameFrame;
    private boolean darkTheme = true;
    private static final Dimension MODE_BUTTON_SIZE = new Dimension(340, 52);
    private static final Dimension SMALL_BUTTON_SIZE = new Dimension(150, 36);
 
    public MainMenuScreen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));

        add(Box.createVerticalStrut(50));
        add(createTitlePanel());
        add(Box.createVerticalStrut(30));
        add(createGameModesPanel());
        add(Box.createVerticalStrut(30));
        add(createOptionsPanel());
        add(Box.createVerticalGlue());
    }

    private JPanel createTitlePanel() {
        JLabel title = new JLabel("SNAKE : CLASSIC REMASTERED");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(0, 255, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Java 21 Edition");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitle.setForeground(new Color(100, 200, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setBackground(new Color(30, 30, 30));
        titleContainer.add(title);
        titleContainer.add(Box.createVerticalStrut(4));
        titleContainer.add(subtitle);
        titleContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        return titleContainer;
    }

    private JPanel createGameModesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 30, 30));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            "SELECT GAME MODE",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(100, 200, 100)
        ));
        panel.setMaximumSize(new Dimension(420, 260));

        // Classic Mode
        JButton classicButton = createModeButton("Classic Mode",
            "The original snake game - eat food and grow without hitting obstacles");
        classicButton.addActionListener(e -> startGame(GameState.GameMode.CLASSIC, AppConfig.speedClassic()));

        // Survival Mode
        JButton survivalButton = createModeButton("Survival Mode",
            "Navigate through obstacles - dodging hazards while collecting food");
        survivalButton.addActionListener(e -> startGame(GameState.GameMode.SURVIVAL, AppConfig.speedSurvival()));

        // Time Attack Mode
        JButton timedButton = createModeButton("Time Attack Mode",
            "Race against the clock - score as many points as possible in 60 seconds");
        timedButton.addActionListener(e -> startGame(GameState.GameMode.TIME_ATTACK, AppConfig.speedTimeAttack()));

        panel.add(classicButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(survivalButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(timedButton);

        return panel;
    }

    private JButton createModeButton(String title, String description) {
        GlowButton button = new GlowButton(title);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setToolTipText(description);
        button.setPreferredSize(MODE_BUTTON_SIZE);
        button.setMaximumSize(MODE_BUTTON_SIZE);
        button.setMinimumSize(MODE_BUTTON_SIZE);
        button.setDarkTheme(darkTheme);

        return button;
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(30, 30, 30));

        GlowButton highScoresButton = new GlowButton("High Scores");
        highScoresButton.setFont(new Font("Arial", Font.BOLD, 12));
        highScoresButton.setPreferredSize(SMALL_BUTTON_SIZE);
        highScoresButton.setDarkTheme(darkTheme);
        highScoresButton.addActionListener(e -> gameFrame.showHighScores());

        GlowButton quitButton = new GlowButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 12));
        quitButton.setPreferredSize(SMALL_BUTTON_SIZE);
        quitButton.setDarkTheme(darkTheme);
        quitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(gameFrame,
                "Are you sure you want to quit?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        panel.add(highScoresButton);
        panel.add(quitButton);

        return panel;
    }

    private void startGame(GameState.GameMode mode, int speed) {
        int difficulty = AppConfig.getDifficulty();
        int adjustedSpeed = switch (difficulty) {
            case 1 -> speed + 20; // easier: slower snake
            case 3 -> Math.max(60, speed - 20); // harder: faster snake
            default -> speed;
        };
        SnakeGameApplication.startGameWithSettings(AppConfig.gridWidth(), AppConfig.gridHeight(), adjustedSpeed, mode);
        gameFrame.dispose();
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        Color background = darkTheme ? new Color(30, 30, 30) : new Color(250, 250, 250);
        applyThemeRecursively(this, background, darkTheme);
        repaint();
    }

    private void applyThemeRecursively(Component component, Color background, boolean darkTheme) {
        Color foreground = darkTheme ? new Color(220, 255, 220) : new Color(30, 30, 30);
        Color accent = darkTheme ? new Color(100, 200, 100) : new Color(40, 120, 40);

        if (component instanceof JPanel panel) {
            panel.setBackground(background);
            if (panel.getBorder() instanceof javax.swing.border.TitledBorder titledBorder) {
                titledBorder.setTitleColor(accent);
            }
        } else if (component instanceof JLabel label) {
            if (label.getText().contains("SNAKE GAME")) {
                label.setForeground(darkTheme ? new Color(0, 255, 0) : new Color(20, 120, 20));
            } else if (label.getText().contains("Java 21")) {
                label.setForeground(accent);
            } else {
                label.setForeground(foreground);
            }
        } else if (component instanceof JButton button) {
            if (button instanceof GlowButton glowButton) {
                glowButton.setDarkTheme(darkTheme);
            } else {
                button.setBackground(darkTheme ? new Color(60, 100, 60) : new Color(225, 235, 225));
                button.setForeground(darkTheme ? Color.WHITE : new Color(20, 40, 20));
            }
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursively(child, background, darkTheme);
            }
        }
    }
}
