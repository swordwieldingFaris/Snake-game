package com.snakegame.view;

import com.snakegame.model.GameState;
import com.snakegame.util.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * High scores screen displaying top player scores.
 */
public class HighScoresScreen extends JPanel {
    private GameFrame gameFrame;
    private GameState.GameMode selectedMode;
    private boolean darkTheme = true;
    private static final Dimension ACTION_BUTTON_SIZE = new Dimension(160, 36);

    public HighScoresScreen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.selectedMode = GameState.GameMode.CLASSIC;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(40, 40, 40));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createScoresPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(40, 40, 40));

        JLabel title = new JLabel("HIGH SCORES");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));

        panel.add(title);
        return panel;
    }

    private JPanel createScoresPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(40, 40, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Get scores from database
        List<com.snakegame.model.PlayerScore> scores = DatabaseManager.getTopScores(10);

        if (scores.isEmpty()) {
            JLabel noScores = new JLabel("No scores recorded yet. Play to create a new record!");
            noScores.setForeground(new Color(150, 150, 150));
            noScores.setFont(new Font("Arial", Font.ITALIC, 14));
            panel.add(noScores);
        } else {
            int rank = 1;
            for (com.snakegame.model.PlayerScore score : scores) {
                JLabel scoreLabel = new JLabel(
                    String.format("%2d. %-20s | Score: %6d | Mode: %-10s | Food: %3d",
                        rank++, score.getPlayerName(), score.getScore(), 
                        score.getGameMode(), score.getFoodEaten())
                );
                scoreLabel.setForeground(Color.WHITE);
                scoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
                panel.add(scoreLabel);
                panel.add(Box.createVerticalStrut(5));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBackground(new Color(40, 40, 40));
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(40, 40, 40));
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(40, 40, 40));

        GlowButton clearScores = new GlowButton("Clear Scores");
        clearScores.setFont(new Font("Arial", Font.BOLD, 12));
        clearScores.setPreferredSize(ACTION_BUTTON_SIZE);
        clearScores.setDarkTheme(darkTheme);
        clearScores.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all scores?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                DatabaseManager.clearAllScores();
                gameFrame.refresh();
            }
        });

        GlowButton backButton = new GlowButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setPreferredSize(ACTION_BUTTON_SIZE);
        backButton.setDarkTheme(darkTheme);
        backButton.addActionListener(e -> gameFrame.showMainMenu());

        panel.add(clearScores);
        panel.add(backButton);
        return panel;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        Color background = darkTheme ? new Color(40, 40, 40) : new Color(250, 250, 250);
        applyThemeRecursively(this, background, darkTheme);
        repaint();
    }

    private void applyThemeRecursively(Component component, Color background, boolean darkTheme) {
        Color text = darkTheme ? new Color(230, 230, 230) : new Color(25, 25, 25);
        if (component instanceof JPanel panel) {
            panel.setBackground(background);
        } else if (component instanceof JLabel label) {
            label.setForeground(text);
        } else if (component instanceof JButton button) {
            if (button instanceof GlowButton glowButton) {
                glowButton.setDarkTheme(darkTheme);
            } else {
                button.setBackground(darkTheme ? new Color(70, 100, 70) : new Color(230, 230, 230));
                button.setForeground(text);
            }
        }

        if (component instanceof JScrollPane scrollPane) {
            scrollPane.setBackground(background);
            scrollPane.getViewport().setBackground(background);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursively(child, background, darkTheme);
            }
        }
    }
}
