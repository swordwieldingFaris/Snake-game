package com.snakegame.view;

import com.snakegame.model.GameStatistics;
import com.snakegame.util.StatisticsManager;

import javax.swing.*;
import java.awt.*;

/**
 * Dedicated achievements screen with simple badge-style cards.
 */
public class AchievementsScreen extends JPanel {
    private final GameFrame gameFrame;
    private boolean darkTheme = true;

    public AchievementsScreen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
        setDarkTheme(true);
    }

    private JComponent createHeader() {
        JLabel title = new JLabel("ACHIEVEMENTS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.CENTER);
        return panel;
    }

    private JComponent createBody() {
        GameStatistics stats = StatisticsManager.load();
        JPanel badges = new JPanel(new GridLayout(0, 1, 10, 10));

        if (stats.getAchievements().isEmpty()) {
            badges.add(createBadge("No badges unlocked yet", false));
        } else {
            for (String achievement : stats.getAchievements()) {
                badges.add(createBadge(achievement, true));
            }
        }

        JScrollPane pane = new JScrollPane(badges);
        pane.setBorder(BorderFactory.createEmptyBorder());
        return pane;
    }

    private JPanel createBadge(String text, boolean unlocked) {
        JPanel badge = new JPanel(new BorderLayout());
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(unlocked ? new Color(120, 180, 120) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JLabel icon = new JLabel(unlocked ? "★" : "○");
        icon.setFont(new Font("Dialog", Font.BOLD, 20));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        badge.add(icon, BorderLayout.WEST);
        badge.add(label, BorderLayout.CENTER);
        return badge;
    }

    private JComponent createFooter() {
        GlowButton back = new GlowButton("Back to Menu");
        back.setDarkTheme(darkTheme);
        back.addActionListener(e -> gameFrame.showMainMenu());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(back);
        return panel;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        Color bg = darkTheme ? new Color(34, 34, 34) : new Color(250, 250, 250);
        Color fg = darkTheme ? new Color(220, 255, 220) : new Color(24, 24, 24);
        applyTheme(this, bg, fg);
        repaint();
    }

    private void applyTheme(Component component, Color bg, Color fg) {
        if (component instanceof JPanel panel) {
            panel.setBackground(bg);
        } else if (component instanceof JLabel label) {
            label.setForeground(fg);
        } else if (component instanceof JButton button) {
            if (button instanceof GlowButton glowButton) {
                glowButton.setDarkTheme(darkTheme);
            } else {
                button.setBackground(darkTheme ? new Color(70, 100, 70) : new Color(230, 230, 230));
                button.setForeground(fg);
            }
        } else if (component instanceof JScrollPane scrollPane) {
            scrollPane.setBackground(bg);
            scrollPane.getViewport().setBackground(bg);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyTheme(child, bg, fg);
            }
        }
    }
}
