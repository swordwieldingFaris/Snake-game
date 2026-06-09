package com.snakegame.view;

import com.snakegame.model.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Status bar displaying game information at the bottom of the window.
 */
public class StatusBar extends JPanel {
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel modeLabel;
    private JLabel timeLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;

    public StatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 40));

        // Score display
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Level display
        levelLabel = new JLabel("Level: 1");
        levelLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Mode display
        modeLabel = new JLabel("Mode: CLASSIC");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Time display
        timeLabel = new JLabel("Time: 0s");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftPanel.add(scoreLabel);
        leftPanel.add(levelLabel);
        leftPanel.add(modeLabel);

        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightPanel.add(timeLabel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        setDarkTheme(true);
    }

    public void update(GameState gameState) {
        if (gameState != null) {
            scoreLabel.setText("Score: " + gameState.getScore());
            levelLabel.setText("Level: " + gameState.getLevel());
            modeLabel.setText("Mode: " + gameState.getGameMode().name());
            
            long seconds = gameState.getElapsedTime() / 1000;
            timeLabel.setText(String.format("Time: %d:%02d", seconds / 60, seconds % 60));
        }
    }

    public void setDarkTheme(boolean darkTheme) {
        Color bg = darkTheme ? new Color(50, 50, 50) : new Color(245, 245, 245);
        Color text = darkTheme ? new Color(235, 255, 235) : new Color(25, 25, 25);
        Color border = darkTheme ? new Color(100, 130, 100) : new Color(180, 180, 180);

        setBackground(bg);
        setBorder(BorderFactory.createLineBorder(border));
        leftPanel.setBackground(bg);
        rightPanel.setBackground(bg);
        scoreLabel.setForeground(text);
        levelLabel.setForeground(text);
        modeLabel.setForeground(text);
        timeLabel.setForeground(text);
        repaint();
    }
}
