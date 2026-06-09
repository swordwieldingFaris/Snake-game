package com.snakegame.view;

import com.snakegame.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Main window frame for the game.
 * Contains menus, game panel, and status bar with multi-screen support.
 */
public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private StatusBar statusBar;
    private MenuBar menuBar;
    private Map<String, JPanel> screens;
    private CardLayout cardLayout;
    private Container contentPane;
    private Game currentGame;
    private MainMenuScreen mainMenuScreen;
    private HighScoresScreen highScoresScreen;
    private StatisticsScreen statisticsScreen;
    private AchievementsScreen achievementsScreen;
    private JPanel pauseOverlay;
    private JPanel pauseDialogPanel;
    private JLabel pauseTitleLabel;
    private GlowButton pauseResumeButton;
    private GlowButton pauseRestartButton;
    private GlowButton pauseQuitButton;
    private boolean darkTheme = true;
    private boolean colorblindMode = false;

    public GameFrame(Game game) {
        this.currentGame = game;
        this.screens = new HashMap<>();
        
        setTitle("SNAKE : CLASSIC REMASTERED");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initComponents(game);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents(Game game) {
        cardLayout = new CardLayout();
        contentPane = getContentPane();
        contentPane.setLayout(cardLayout);
        
        gamePanel = new GamePanel(game);
        statusBar = new StatusBar();
        menuBar = new MenuBar(this);
        
        setJMenuBar(menuBar);
        
        // Main game screen
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        screens.put("GAME", mainPanel);
        
        // Main menu screen
        mainMenuScreen = new MainMenuScreen(this);
        screens.put("MENU", mainMenuScreen);
        
        // High scores screen
        highScoresScreen = new HighScoresScreen(this);
        screens.put("SCORES", highScoresScreen);

        // Statistics screen
        statisticsScreen = new StatisticsScreen(this);
        screens.put("STATS", statisticsScreen);

        // Achievements screen
        achievementsScreen = new AchievementsScreen(this);
        screens.put("ACHIEVEMENTS", achievementsScreen);
        
        // Add all screens to content pane
        for (String key : screens.keySet()) {
            contentPane.add(screens.get(key), key);
        }

        initPauseOverlay();
        applyTheme();
        
        showScreen("MENU");
    }

    private void initPauseOverlay() {
        pauseOverlay = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 170));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        pauseOverlay.setOpaque(false);
        pauseOverlay.setVisible(false);

        pauseDialogPanel = new JPanel();
        pauseDialogPanel.setLayout(new BoxLayout(pauseDialogPanel, BoxLayout.Y_AXIS));
        pauseDialogPanel.setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));
        pauseDialogPanel.setPreferredSize(new Dimension(280, 250));
        pauseDialogPanel.setBackground(new Color(30, 30, 30));

        pauseTitleLabel = new JLabel("PAUSED", SwingConstants.CENTER);
        pauseTitleLabel.setForeground(new Color(0, 255, 0));
        pauseTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pauseTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pauseResumeButton = new GlowButton("Resume");
        pauseResumeButton.setActionCommand("RESUME");
        pauseResumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        pauseRestartButton = new GlowButton("Restart");
        pauseRestartButton.setActionCommand("RESTART");
        pauseRestartButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        pauseQuitButton = new GlowButton("Quit");
        pauseQuitButton.setActionCommand("QUIT");
        pauseQuitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        java.awt.event.ActionListener overlayListener = e -> {
            pauseOverlay.setVisible(false);
            if ("QUIT".equals(e.getActionCommand())) {
                dispose();
            } else {
                menuBar.dispatchAction(e.getActionCommand());
            }
        };
        pauseResumeButton.addActionListener(overlayListener);
        pauseRestartButton.addActionListener(overlayListener);
        pauseQuitButton.addActionListener(overlayListener);

        stylePauseOverlayButton(pauseResumeButton);
        stylePauseOverlayButton(pauseRestartButton);
        stylePauseOverlayButton(pauseQuitButton);

        pauseDialogPanel.add(pauseTitleLabel);
        pauseDialogPanel.add(Box.createVerticalStrut(16));
        pauseDialogPanel.add(pauseResumeButton);
        pauseDialogPanel.add(Box.createVerticalStrut(10));
        pauseDialogPanel.add(pauseRestartButton);
        pauseDialogPanel.add(Box.createVerticalStrut(10));
        pauseDialogPanel.add(pauseQuitButton);
        pauseOverlay.add(pauseDialogPanel, new GridBagConstraints());

        setGlassPane(pauseOverlay);
    }

    private void stylePauseOverlayButton(JButton button) {
        button.setMaximumSize(new Dimension(180, 42));
        button.setPreferredSize(new Dimension(180, 42));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void showScreen(String screenName) {
        if (screens.containsKey(screenName)) {
            cardLayout.show(contentPane, screenName);
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public void updateStatusBar(GameState gameState) {
        statusBar.update(gameState);
    }

    public void showMainMenu() {
        showScreen("MENU");
    }

    public void showGame() {
        showScreen("GAME");
    }

    public void showHighScores() {
        highScoresScreen = new HighScoresScreen(this);
        highScoresScreen.setDarkTheme(darkTheme);
        screens.put("SCORES", highScoresScreen);
        contentPane.add(highScoresScreen, "SCORES");
        showScreen("SCORES");
    }

    public void showStatistics() {
        statisticsScreen = new StatisticsScreen(this);
        statisticsScreen.setDarkTheme(darkTheme);
        screens.put("STATS", statisticsScreen);
        contentPane.add(statisticsScreen, "STATS");
        showScreen("STATS");
    }

    public void showAchievements() {
        achievementsScreen = new AchievementsScreen(this);
        achievementsScreen.setDarkTheme(darkTheme);
        screens.put("ACHIEVEMENTS", achievementsScreen);
        contentPane.add(achievementsScreen, "ACHIEVEMENTS");
        showScreen("ACHIEVEMENTS");
    }

    public void startNewGame() {
        showScreen("GAME");
    }

    public void togglePause() {
        pauseOverlay.setVisible(!pauseOverlay.isVisible());
    }

    public void refresh() {
        // Refresh the high scores display
        if (highScoresScreen != null) {
            screens.remove("SCORES");
            highScoresScreen = new HighScoresScreen(this);
            screens.put("SCORES", highScoresScreen);
            contentPane.add(highScoresScreen, "SCORES");
        }
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void addMenuListener(java.awt.event.ActionListener listener) {
        menuBar.addActionListener(listener);
    }

    public void showPauseOverlay() {
        pauseOverlay.setVisible(true);
    }

    public void hidePauseOverlay() {
        pauseOverlay.setVisible(false);
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        applyTheme();
    }

    public void setColorblindMode(boolean colorblindMode) {
        this.colorblindMode = colorblindMode;
        gamePanel.setColorblindMode(colorblindMode);
        repaint();
    }

    private void applyTheme() {
        gamePanel.setLightMode(!darkTheme);
        statusBar.setDarkTheme(darkTheme);
        mainMenuScreen.setDarkTheme(darkTheme);
        highScoresScreen.setDarkTheme(darkTheme);
        statisticsScreen.setDarkTheme(darkTheme);
        achievementsScreen.setDarkTheme(darkTheme);
        if (pauseDialogPanel != null) {
            Color dialogBg = darkTheme ? new Color(30, 30, 30) : new Color(248, 248, 248);
            Color dialogBorder = darkTheme ? new Color(110, 150, 110) : new Color(170, 185, 170);
            Color title = darkTheme ? new Color(0, 255, 0) : new Color(20, 120, 20);
            pauseDialogPanel.setBackground(dialogBg);
            pauseDialogPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(dialogBorder, 2, true),
                BorderFactory.createEmptyBorder(24, 26, 24, 26)
            ));
            pauseTitleLabel.setForeground(title);
            pauseResumeButton.setDarkTheme(darkTheme);
            pauseRestartButton.setDarkTheme(darkTheme);
            pauseQuitButton.setDarkTheme(darkTheme);
        }
        Color frameBg = darkTheme ? new Color(30, 30, 30) : new Color(250, 250, 250);
        getContentPane().setBackground(frameBg);
        repaint();
    }

    public void showToast(String message) {
        JWindow toast = new JWindow(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 170, 100), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        panel.setBackground(new Color(35, 35, 35, 230));
        JLabel label = new JLabel(message);
        label.setForeground(new Color(220, 255, 220));
        label.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(label, BorderLayout.CENTER);
        toast.setContentPane(panel);
        toast.pack();

        Point location = getLocationOnScreen();
        int x = location.x + getWidth() - toast.getWidth() - 24;
        int y = location.y + 54;
        toast.setLocation(x, y);
        toast.setAlwaysOnTop(true);
        toast.setVisible(true);

        javax.swing.Timer timer = new javax.swing.Timer(2200, e -> {
            toast.setVisible(false);
            toast.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}