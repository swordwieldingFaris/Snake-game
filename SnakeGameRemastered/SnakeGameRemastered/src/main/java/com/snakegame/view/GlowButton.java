package com.snakegame.view;

import javax.swing.*;
import java.awt.*;

/**
 * Rounded gradient button with hover glow effect.
 */
public class GlowButton extends JButton {
    private boolean hovered;
    private boolean darkTheme = true;

    public GlowButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(new Font("Arial", Font.BOLD, 13));
        setMargin(new Insets(10, 16, 10, 16));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color top = darkTheme ? new Color(72, 124, 72) : new Color(238, 246, 238);
        Color bottom = darkTheme ? new Color(45, 86, 45) : new Color(214, 230, 214);
        if (hovered) {
            top = darkTheme ? new Color(95, 168, 95) : new Color(225, 245, 225);
            bottom = darkTheme ? new Color(58, 110, 58) : new Color(202, 224, 202);
            g2.setColor(darkTheme ? new Color(80, 255, 80, 95) : new Color(60, 170, 60, 65));
            g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
        }

        GradientPaint gradient = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
        g2.setPaint(gradient);
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 14, 14);

        g2.setColor(darkTheme ? new Color(132, 184, 132) : new Color(150, 175, 150));
        g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 14, 14);
        g2.dispose();

        setForeground(darkTheme ? Color.WHITE : new Color(30, 50, 30));
        super.paintComponent(g);
    }
}
