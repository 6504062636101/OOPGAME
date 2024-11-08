package com.mycompany.gameproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import com.mycompany.gameproject.Lightning;
import java.util.Random;

public class MageBattle extends CharacterBattle {

    private Timer fireballTimer, lightningTimer;
    private ArrayList<Fireball> fireballs;
    private ArrayList<Lightning> lightnings;
    private Image[] fireballFrames;
    private Image[] waterwaveFramesLeft;
    public int lightningDelay;
    private boolean frameChanged = false;
    private Timer dragonMovementTimer;
    private Random random;

    public MageBattle(int lightningDelay) {
        this.lightningDelay = lightningDelay;
        loadImages();
        initializeGameObjects();
        initializeTimers();
        setupKeyListener();
    }

    private void loadImages() {
        this.bg = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/MAP2.png")).getImage();
        this.characterImagesForward = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Mage1.png")).getImage(),
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Mage2.png")).getImage(),};
        this.characterImagesBackward = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Mage3BW.png")).getImage(),
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Mage4BW.png")).getImage(),};
        this.dragonImages = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Dragon1.png")).getImage(),
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Dragon2.png")).getImage(),};

        this.fireballFrames = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Water2.png")).getImage(),};
        this.waterwaveFramesLeft = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/waterleft.png")).getImage()
        };
    }

    // การตั้งค่าเริ่มต้นต่างๆ
    private void initializeGameObjects() {
        lightnings = new ArrayList<>();
        fireballs = new ArrayList<>();
        this.characterX = -90;
        this.characterY = 320;
        this.currentFrame = 0;

        startDragonAnimation();
    }

    private void initializeTimers() {

        fireballTimer = new Timer(50, e -> {
            updateFireballs();
            repaint();
        });
        fireballTimer.start();

        lightningTimer = new Timer(50, e -> {
            updateLightnings();
            repaint();
        });
        lightningTimer.start();

        Timer spawnLightningTimer = new Timer(lightningDelay, e -> spawnLightning());
        spawnLightningTimer.start();
        dragonMovementTimer = new Timer(500, e -> moveDragonRandomly());
        dragonMovementTimer.start();
    }

    private void moveDragonRandomly() {

        int direction = (int) (Math.random() * 4);

        int moveDistance = 25;

        switch (direction) {
            case 0:
                if (dragonY > 240) {
                    dragonY -= moveDistance;
                }
                break;
            case 1:

                if (dragonY < 360) {
                    dragonY += moveDistance;
                }

                break;
            case 2:

                dragonX -= moveDistance;

                break;
            case 3:

                dragonX += moveDistance;
                break;
        }

    }

    private boolean[] keyStates = new boolean[256];

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                keyStates[keyCode] = true;

                if (keyCode == KeyEvent.VK_SPACE) {
                    shootFireball();
                }

                moveCharacter(keyCode);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                keyStates[keyCode] = false;

                frameChanged = false;

                if (!keyStates[KeyEvent.VK_LEFT] && !keyStates[KeyEvent.VK_RIGHT]) {
                    currentFrame = 0;
                }
            }
        });
    }

    @Override
    public void moveCharacter(int keyCode) {

        if (keyStates[KeyEvent.VK_LEFT]) {
            characterX -= 5;
            isMovingLeft = true;
            if (!frameChanged) {
                currentFrame = (currentFrame + 1) % characterImagesBackward.length;
                frameChanged = true;
            }
        } else if (keyStates[KeyEvent.VK_RIGHT]) {
            characterX += 5;
            isMovingLeft = false;
            if (!frameChanged) {
                currentFrame = (currentFrame + 1) % characterImagesForward.length;
                frameChanged = true;
            }
        }

        if (keyStates[KeyEvent.VK_UP] && characterY > 239) {
            characterY -= 5;
        }
        if (keyStates[KeyEvent.VK_DOWN] && characterY < 359) {
            characterY += 5;
        }

        if (!isWalking) {
            isWalking = true;
            startAnimation();
        }

        checkBoundary();
    }

    private void spawnLightning() {
        int x = (int) (Math.random() * getWidth());
        Lightning lightning = new Lightning(x, 0);
        lightnings.add(lightning);
    }

    private void updateLightnings() {
        Iterator<Lightning> iterator = lightnings.iterator();
        while (iterator.hasNext()) {
            Lightning lightning = iterator.next();
            lightning.update();

            if (lightning.collidesWithMage(characterX + 110, characterY + 120, 75, 50)) {
                health -= 10;
                if (health <= 0) {
                    health = 0;
                    showGameOverScreen(false);
                }
                iterator.remove();
            }

            if (lightning.getY() > getHeight()) {
                iterator.remove();
            }
        }
    }

    // ฟังก์ชันการยิงลูกไฟ
    private void shootFireball() {
        Fireball mageattack;
        if (isMovingLeft) {
            mageattack = new Fireball(characterX + 80, characterY + 100, isMovingLeft, waterwaveFramesLeft);
        } else {
            mageattack = new Fireball(characterX + 150, characterY + 100, isMovingLeft, fireballFrames);
        }
        fireballs.add(mageattack);
    }

    private void updateFireballs() {
        Iterator<Fireball> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            fireball.move();

            if (fireball.collidesWithDragon(dragonX + 80, dragonY + 70, 200, 140)) {
                dragonHP -= 10;
                if (dragonHP <= 0) {
                    dragonHP = 0;
                    showGameOverScreen(true);
                }
                iterator.remove();
            }

            if (fireball.x > getWidth() || fireball.x < 0) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        drawBackground(g);
        drawMage(g);
        drawDragon(g);
        drawFireballs(g);
        drawLightnings(g);
        drawHealthBars(g);
        g.setColor(Color.RED);
        drawLightningSpeed(g);

    }

    private void drawBackground(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

    private void drawMage(Graphics g) {

        if (isMovingLeft) {
            g.drawImage(characterImagesBackward[currentFrame], characterX, characterY, 300, 250, this);
        } else {
            g.drawImage(characterImagesForward[currentFrame], characterX, characterY, 300, 250, this);
        }
    }

    private void drawDragon(Graphics g) {
        g.drawImage(dragonImages[dragonFrame], dragonX, dragonY, 400, 250, this);
    }

    private void drawFireballs(Graphics g) {
        for (Fireball fireball : fireballs) {
            fireball.draw(g);
        }
    }

    private void drawLightnings(Graphics g) {
        for (Lightning lightning : lightnings) {
            lightning.draw(g);
        }
    }

    private void drawHealthBars(Graphics g) {
        drawDragonHealthBar(g);
        drawMageHealthBar(g);
    }

    private void drawDragonHealthBar(Graphics g) {
        int hpBarWidth = 200;
        int hpBarHeight = 20;
        int xPosition = getWidth() - hpBarWidth - 10;
        int yPosition = 10;

        g.setColor(Color.BLACK);
        g.fillRect(xPosition, yPosition, hpBarWidth, hpBarHeight);

        g.setColor(Color.GREEN);
        g.fillRect(xPosition, yPosition, (int) (dragonHP * hpBarWidth / (double) MAX_DRAGON_HP), hpBarHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Dragon HP: " + dragonHP + "/" + MAX_DRAGON_HP, xPosition + 5, yPosition + 15);
    }

    private void drawMageHealthBar(Graphics g) {
        int hpBarWidth = 200;
        int hpBarHeight = 20;
        int xPosition = 10;
        int yPosition = 10;

        g.setColor(Color.BLACK);
        g.fillRect(xPosition, yPosition, hpBarWidth, hpBarHeight);

        g.setColor(Color.BLUE);
        g.fillRect(xPosition, yPosition, (int) (health * hpBarWidth / (double) max_health), hpBarHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Mage HP: " + health + "/" + max_health, xPosition + 5, yPosition + 15);
    }

    private void drawCharacterPosition(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Character Position: X = " + characterX + " Y = " + characterY, 20, 20);
    }

    private void drawLightningSpeed(Graphics g) {
        g.setColor(Color.YELLOW); // กำหนดสีเป็นสีเหลือง
        g.setFont(new Font("Arial", Font.BOLD, 20)); // กำหนดฟอนต์

        // แสดงค่า lightningDelay / 10
        String speedText = "SPEED DELAY: " + (lightningDelay / 10.0);

        g.drawString(speedText, 10, 80); // วางข้อความที่ตำแหน่ง (10, 40)
    }

    public void showGameOverScreen(boolean victory) {

        fireballTimer.stop();
        lightningTimer.stop();

        JFrame gameOverFrame = new JFrame("Game Over");
        gameOverFrame.setSize(800, 600);
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/origbig.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        JLabel label;
        if (victory) {
            label = new JLabel("Dragon is defeated! You Win!", JLabel.CENTER);
        } else {
            label = new JLabel("You Lost!", JLabel.CENTER);
        }
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        backgroundLabel.add(label, BorderLayout.NORTH);

        JButton retryButton = new JButton("Play Again");
        retryButton.setFont(new Font("Arial", Font.PLAIN, 20));
        retryButton.addActionListener(e -> {
            JFrame gameWindow = new JFrame("Character Battle");
            CharacterBattle gamePanel = new MageBattle(lightningDelay);

            gameWindow.setSize(800, 600);
            gameWindow.setResizable(false);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.add(gamePanel);
            gameWindow.setVisible(true);

            gameOverFrame.dispose();
        });

        JButton retryhardButton = new JButton("Play Again Harder");
        retryhardButton.setFont(new Font("Arial", Font.PLAIN, 20));
        retryhardButton.addActionListener(e -> {

            if (this.lightningDelay > 1000) {
                this.lightningDelay -= 500;
            } else if (this.lightningDelay > 500) {
                this.lightningDelay -= 200;
            } else if (this.lightningDelay > 100) {
                this.lightningDelay -= 100;
            }

            JFrame gameWindow = new JFrame("Battle Harder");
            MageBattle gamePanel = new MageBattle(this.lightningDelay);

            gameWindow.setSize(800, 600);
            gameWindow.setResizable(false);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.add(gamePanel);
            gameWindow.setVisible(true);

            gameOverFrame.dispose();
        });

        JButton gamewd = new JButton("Main menu");
        gamewd.setFont(new Font("Arial", Font.PLAIN, 20));
        gamewd.addActionListener(e -> {

            JFrame gameWindow = new JFrame("Game Window");
            GameWindow gameWindowPanel = new GameWindow();

            gameWindow.setSize(800, 600);
            gameWindow.setResizable(false);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.add(gameWindowPanel);
            gameWindow.setVisible(true);

            gameOverFrame.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        buttonPanel.add(retryButton, gbc);

        gbc.gridy = 1;

        buttonPanel.add(gamewd, gbc);

        gbc.gridy = 2;

        buttonPanel.add(retryhardButton, gbc);

        backgroundLabel.add(buttonPanel, BorderLayout.CENTER);

        gameOverFrame.add(backgroundLabel);
        gameOverFrame.setVisible(true);

        SwingUtilities.getWindowAncestor(this).dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Character Battle");
        CharacterBattle gamePanel = new MageBattle(2000);

        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setVisible(true);
    }

}
