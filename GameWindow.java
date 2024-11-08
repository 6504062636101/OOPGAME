package com.mycompany.gameproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JPanel {

    // Member variables for background image, start button, and game name label
    private final Image backgroundImage;
    private final JButton startButton;
    private final JLabel gameNameLabel;
    ImageIcon buttonIcon = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/StartLOGOs.png"));


    // Constructor for the GameWindow class
    public GameWindow() {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/origbig.png")).getImage();

        // Load the custom font for the game name
        Font customFont = loadCustomFont("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\fonts\\PressStart2P.ttf", 36f);
        
        // Load the custom font for the button
        Font buttonFont = loadCustomFont("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\fonts\\PressStart2P.ttf", 24f);

        // Create the start button with an icon
        startButton = new JButton(buttonIcon);
        configureButton(startButton, buttonFont);

        // Create the game name label
        gameNameLabel = new JLabel("DragonNest", SwingConstants.CENTER);
        configureLabel(gameNameLabel, customFont);

        // Set layout to null for absolute positioning
        setLayout(null);
        add(startButton);
        add(gameNameLabel);
    }

    // Method to load a custom font from a file
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

    // Method to configure the start button properties
    private void configureButton(JButton button, Font font) {
        button.setContentAreaFilled(false); // Disable button background
        button.setBorderPainted(false); // Disable button border
        button.setFocusPainted(false); // Disable focus painting

        int buttonWidth = 250;
        int buttonHeight = 80;
        button.setFont(font); // Set custom font for the button

        // Center the button on the screen
        int buttonX = (800 - buttonWidth) / 2;
        int buttonY = (600 - buttonHeight) / 2 + 70;
        button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        // Add action listener for button click
        button.addActionListener((ActionEvent e) -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(button);
            frame.getContentPane().removeAll(); // Remove all content from the frame
            frame.add(new CharacterSelectionWindow()); // Add character selection window
            frame.revalidate(); // Refresh the frame
            frame.repaint(); // Repaint the frame
        });
    }

    // Method to configure the game name label properties
    private void configureLabel(JLabel label, Font font) {
        int labelWidth = 640;
        int labelHeight = 150;

        // Center the label on the screen
        int labelX = (800 - labelWidth) / 2;
        int labelY = (600 - labelHeight) / 2 - 20;
        label.setBounds(labelX, labelY, labelWidth, labelHeight);
        label.setForeground(Color.WHITE); // Set text color to white
        label.setFont(font); // Set custom font for the label
    }

    // Override the paintComponent method to draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // Main method to run the application
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
