package com.snakegame.view;

import com.snakegame.model.GameStatistics;
import com.snakegame.util.StatisticsManager;

import javax.swing.*;
import java.awt.*;

/**
 * Screen that displays persistent game statistics and achievements.
 */
public class StatisticsScreen extends JPanel {
    private final GameFrame gameFrame;
    private boolean darkTheme = true;
    private JPanel metricsCard;
    private JPanel achievementsCard;
    private GlowButton backButton;

    public StatisticsScreen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
        setDarkTheme(true);
    }

    private JComponent createHeader() {
        JLabel title = new JLabel("STATISTICS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.CENTER);
        return panel;
    }

    private JComponent createBody() {
        GameStatistics stats = StatisticsManager.load();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        metricsCard = createCardPanel("LIFETIME TOTALS");
        metricsCard.add(createInfoLabel("Games Played: " + stats.getTotalGamesPlayed()));
        metricsCard.add(Box.createVerticalStrut(8));
        metricsCard.add(createInfoLabel("Apples Eaten: " + stats.getTotalApplesEaten()));
        metricsCard.add(Box.createVerticalStrut(8));
        metricsCard.add(createInfoLabel("Longest Snake Length: " + stats.getLongestSnakeLength()));

        achievementsCard = createCardPanel("ACHIEVEMENTS");
        if (stats.getAchievements().isEmpty()) {
            achievementsCard.add(createInfoLabel("- None unlocked yet"));
        } else {
            for (String achievement : stats.getAchievements()) {
                achievementsCard.add(createInfoLabel("- " + achievement));
                achievementsCard.add(Box.createVerticalStrut(6));
            }
        }

        content.add(metricsCard);
        content.add(Box.createVerticalStrut(14));
        content.add(achievementsCard);

        JScrollPane pane = new JScrollPane(content);
        pane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pane.getVerticalScrollBar().setUnitIncrement(14);
        return pane;
    }

    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return card;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    private JComponent createFooter() {
        backButton = new GlowButton("Back to Menu");
        backButton.setPreferredSize(new Dimension(170, 38));
        backButton.setDarkTheme(darkTheme);
        backButton.addActionListener(e -> gameFrame.showMainMenu());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(backButton);
        return panel;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        Color bg = darkTheme ? new Color(32, 32, 32) : new Color(250, 250, 250);
        Color fg = darkTheme ? new Color(220, 255, 220) : new Color(24, 24, 24);
        applyTheme(this, bg, fg, darkTheme);
        styleCardBorders();
        repaint();
    }

    private void styleCardBorders() {
        Color borderColor = darkTheme ? new Color(100, 140, 100) : new Color(170, 185, 170);
        styleCardBorder(metricsCard, borderColor);
        styleCardBorder(achievementsCard, borderColor);
    }

    private void styleCardBorder(JPanel card, Color borderColor) {
        if (card == null) return;
        if (card.getBorder() instanceof javax.swing.border.CompoundBorder compound &&
            compound.getOutsideBorder() instanceof javax.swing.border.TitledBorder titledBorder) {
            titledBorder.setTitleColor(darkTheme ? new Color(150, 220, 150) : new Color(35, 100, 35));
            titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 13));
            titledBorder.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        }
    }

    private void applyTheme(Component component, Color bg, Color fg, boolean dark) {
        if (component instanceof JPanel panel) {
            panel.setBackground(bg);
        } else if (component instanceof JLabel label) {
            label.setForeground(fg);
        } else if (component instanceof JButton button) {
            if (button instanceof GlowButton glowButton) {
                glowButton.setDarkTheme(dark);
            } else {
                button.setBackground(dark ? new Color(70, 100, 70) : new Color(230, 230, 230));
                button.setForeground(fg);
            }
        } else if (component instanceof JScrollPane scrollPane) {
            scrollPane.setBackground(bg);
            scrollPane.getViewport().setBackground(bg);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyTheme(child, bg, fg, dark);
            }
        }
    }
}
