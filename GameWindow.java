package com.mycompany.gameproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JPanel {

    private final Image backgroundImage;
    private final JButton startButton;
    private final JLabel gameNameLabel;
    ImageIcon buttonIcon = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/StartLOGOs.png"));

    public GameWindow() {

        backgroundImage = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/origbig.png")).getImage();

        Font customFont = loadCustomFont("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\fonts\\PressStart2P.ttf", 36f);

        Font buttonFont = loadCustomFont("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\fonts\\PressStart2P.ttf", 24f);

        startButton = new JButton(buttonIcon);
        configureButton(startButton, buttonFont);

        gameNameLabel = new JLabel("DragonNest", SwingConstants.CENTER);
        configureLabel(gameNameLabel, customFont);

        setLayout(null);
        add(startButton);
        add(gameNameLabel);
    }

    private Font loadCustomFont(String path, float size) {
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
        }
        return customFont;
    }

    private void configureButton(JButton button, Font font) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        int buttonWidth = 250;
        int buttonHeight = 80;
        button.setFont(font);

        int buttonX = (800 - buttonWidth) / 2;
        int buttonY = (600 - buttonHeight) / 2 + 70;
        button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        button.addActionListener((ActionEvent e) -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(button);
            frame.getContentPane().removeAll();
            frame.add(new CharacterSelectionWindow());
            frame.revalidate();
            frame.repaint();
        });
    }

    private void configureLabel(JLabel label, Font font) {
        int labelWidth = 640;
        int labelHeight = 150;

        int labelX = (800 - labelWidth) / 2;
        int labelY = (600 - labelHeight) / 2 - 20;
        label.setBounds(labelX, labelY, labelWidth, labelHeight);
        label.setForeground(Color.WHITE);
        label.setFont(font);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("My Game");
        GameWindow gameWindow = new GameWindow();

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameWindow);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
