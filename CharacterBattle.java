package com.mycompany.gameproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class CharacterBattle extends JPanel {

    protected Image bg;                      // Image for the background
    protected Image[] characterImagesForward; // Array for character moving forward
    protected Image[] characterImagesBackward;// Array for character moving backward
    protected Image[] dragonImages;          // Array for dragon images
    protected int characterX, characterY;    // Character's X and Y position
    protected int currentFrame;              // Current frame for character animation
    protected int dragonX=350, dragonY=205;          // Dragon's X and Y position
    protected int dragonFrame;               // Current frame for dragon animation
    protected int health=100;                    // Character health
    protected int max_health=100;
    protected boolean isWalking;             // Flag to check if the character is walking
    protected boolean isMovingLeft;          // Flag to check if the character is moving left
    protected Thread animationThread;        // Thread for character animation
    protected Thread dragonAnimationThread;  // Thread for dragon animation
    protected ArrayList<Fireball> fireballs;
    protected Image[] fireballFrames;
    protected int dragonHP = 1000;  // เพิ่มตัวแปรสำหรับเก็บค่าของเลือดมังกร
    protected int MAX_DRAGON_HP = 1000;
    protected ArrayList<Lightning> lightnings;
    public abstract void moveCharacter(int keyCode);
    // Constructor to set up initial properties
    public CharacterBattle() {
        fireballs = new ArrayList<>();
         fireballFrames = new Image[]{
            new ImageIcon("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\Water2.png").getImage(),
            new ImageIcon("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\Water2.png").getImage()
        };
        // Default health
        health = 100;

        // Set focusable to capture keyboard events
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                moveCharacter(keyCode); // Move the character
                repaint();               // Repaint the panel
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                stopWalking(keyCode); // Stop walking when the key is released
            }
        });

        startDragonAnimation(); // Start dragon animation
    }

    // Abstract method to be implemented in subclasses for character movement
    

    // Stop walking method
    protected void stopWalking(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            isWalking = false;
        }
    }

    // Method to start character animation
    protected void startAnimation() {
        animationThread = new Thread(() -> {
            while (isWalking) {
                try {
                    Thread.sleep(1000); // Delay between frames
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentFrame = (currentFrame + 1) % 2; // Switch between frames 0 and 1
                checkBoundary();
                repaint();
            }
        });

        animationThread.start();
    }

    // Method to start dragon animation
    protected void startDragonAnimation() {
        dragonAnimationThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Delay between dragon frames
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dragonFrame = (dragonFrame + 1) % 2; // Switch between dragon frames
                repaint();  // Repaint the panel
            }
        });

        dragonAnimationThread.start();
    }

    // Check boundary of character's movement
    protected void checkBoundary() {
        if (characterX < -200) {
            characterX = 680;  // Wrap around if character moves too far left
        }

        if (characterX > 680) {
            characterX = -190; // Wrap around if character moves too far right
        }
    }

    // Abstract method to be implemented in subclasses to handle drawing of the character
    @Override
    protected abstract void paintComponent(Graphics g);

    // Health getter and setter
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // Method to handle damage
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // Main method to run the game

}
