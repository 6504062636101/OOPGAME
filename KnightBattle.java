package com.mycompany.gameproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import com.mycompany.gameproject.Lightning;
import java.util.Random;

public class KnightBattle extends CharacterBattle {

    private Timer waterwaveTimer, lightningTimer;
    private ArrayList<Waterwave> waterwaves;
    private Image[] waterwaveFrames;
    private Image[] waterwaveFramesLeft;
    private ArrayList<Lightning> lightnings;
    private int lightningDelay;
    private boolean frameChanged = false;
    private Timer dragonMovementTimer;
    private Random random;
    private boolean isShooting = false;

    public KnightBattle(int lightningDelay) {
        this.lightningDelay = lightningDelay;
        loadImages();
        initializeGameObjects();
        initializeTimers();
        setupKeyListener();
    }

    private void loadImages() {
        this.bg = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/MAP2.png")).getImage();
        this.characterImagesForward = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Knight1.png")).getImage(),
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Knight2.png")).getImage()
        };
        this.characterImagesBackward = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Knight3BW.png")).getImage(),
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Knight4BW.png")).getImage()
        };
        this.dragonImages = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Dragon1.png")).getImage(),
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/Dragon2.png")).getImage(),};

        this.waterwaveFrames = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/knightattackleft.png")).getImage(),};
        this.waterwaveFramesLeft = new Image[]{
            new ImageIcon(getClass().getResource("/com/mycompany/gameproject/knightattackright.png")).getImage()
        };
    }

    private void initializeGameObjects() {
        lightnings = new ArrayList<>();
        waterwaves = new ArrayList<>();
        this.characterX = -90;
        this.characterY = 320;
        this.currentFrame = 0;

        startDragonAnimation();
    }

    private void initializeTimers() {
        waterwaveTimer = new Timer(50, e -> {
            updateWaterwaves();
            repaint();
        });
        waterwaveTimer.start();
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

    private void spawnLightning() {
        int x = (int) (Math.random() * getWidth());
        Lightning lightning = new Lightning(x, 0);
        lightnings.add(lightning);
    }
    private boolean[] keyStates = new boolean[256];

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                keyStates[keyCode] = true;

                if (keyCode == KeyEvent.VK_SPACE) {
                    isShooting = true;  // เมื่อกด Spacebar ให้เปลี่ยนเป็นโหมดโจมตี
                    shootWaterwave();    // เรียกใช้ฟังก์ชันยิง Waterwave
                }

                moveCharacter(keyCode);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                keyStates[keyCode] = false;

                if (keyCode == KeyEvent.VK_SPACE) {
                    isShooting = false;  // เมื่อปล่อย Spacebar ให้กลับไปเป็นโหมดปกติ
                }

                frameChanged = false;

                // หากปล่อยปุ่มแล้วให้หยุดการเคลื่อนที่
                if (!keyStates[KeyEvent.VK_LEFT] && !keyStates[KeyEvent.VK_RIGHT]) {
                    currentFrame = 0; // เปลี่ยนไปที่เฟรมแรกเมื่อหยุดเคลื่อนที่
                }
            }
        });
    }

    @Override
    public void moveCharacter(int keyCode) {
        // การเคลื่อนที่ของตัวละคร
        if (keyStates[KeyEvent.VK_LEFT]) {
            characterX -= 5;
            isMovingLeft = true;
            if (!frameChanged) {
                currentFrame = (currentFrame + 1) % characterImagesBackward.length;  // เปลี่ยนเฟรมเมื่อกดปุ่ม
                frameChanged = true;
            }
        } else if (keyStates[KeyEvent.VK_RIGHT]) {
            characterX += 5;
            isMovingLeft = false;
            if (!frameChanged) {
                currentFrame = (currentFrame + 1) % characterImagesForward.length;  // เปลี่ยนเฟรมเมื่อกดปุ่ม
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

    // ฟังก์ชันยิง Waterwave
    private void shootWaterwave() {
        Waterwave knigtattack;
        if (isMovingLeft) {
            knigtattack = new Waterwave(characterX + 80, characterY + 100, isMovingLeft, waterwaveFrames);  // ใช้ waterwaveFramesLeft เมื่อหันหลัง
        } else {
            knigtattack = new Waterwave(characterX + 150, characterY + 100, isMovingLeft, waterwaveFramesLeft);  // ใช้ waterwaveFramesRight เมื่อหันหน้า
        }

        waterwaves.add(knigtattack);
    }

    // อัพเดทตำแหน่งของ Waterwave
    private void updateWaterwaves() {
        Iterator<Waterwave> iterator = waterwaves.iterator();
        while (iterator.hasNext()) {
            Waterwave waterwave = iterator.next();
            waterwave.move();

            if (waterwave.collidesWithDragon(dragonX + 80, dragonY + 70, 200, 140)) {
                dragonHP -= 10;
                if (dragonHP <= 0) {
                    dragonHP = 0;
                    showGameOverScreen(true);
                }
                iterator.remove();
                continue;
            }

            if (waterwave.x > getWidth() || waterwave.x < 0) {
                iterator.remove();
                continue;
            }

            if (Math.abs(waterwave.x - characterX) > 30) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        drawBackground(g);
        drawLightnings(g);
        drawCharacter(g);
        drawDragon(g);
        drawDragonHealthBar(g);
        drawKnightHealthBar(g);
        drawWaterwaves(g);
        drawLightningSpeed(g);
    }

    private void drawBackground(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

    private void drawLightnings(Graphics g) {
        for (Lightning lightning : lightnings) {
            lightning.draw(g);
        }
    }

    public void drawCharacter(Graphics g) {
        // เปลี่ยนภาพตัวละครตามสถานะ isShooting
        if (isShooting) {
            // เมื่อกด Spacebar ให้แสดงภาพที่กำหนดสำหรับการโจมตี
            if (isMovingLeft) {
                g.drawImage(waterwaveFrames[0], characterX, characterY, 300, 250, this);  // แสดงภาพโจมตีทางซ้าย
            } else {
                g.drawImage(waterwaveFramesLeft[0], characterX, characterY, 300, 250, this);  // แสดงภาพโจมตีทางขวา
            }
        } else {
            // เมื่อไม่ได้กด Spacebar ให้แสดงภาพตัวละครปกติ
            if (isMovingLeft) {
                g.drawImage(characterImagesBackward[currentFrame], characterX, characterY, 300, 250, this);
            } else {
                g.drawImage(characterImagesForward[currentFrame], characterX, characterY, 300, 250, this);
            }
        }
    }

    private void drawDragon(Graphics g) {
        g.drawImage(dragonImages[dragonFrame], dragonX, dragonY, 400, 250, this);
    }

    private void drawDragonHealthBar(Graphics g) {
        int dragonHpBarWidth = 200;
        int dragonHpBarHeight = 20;
        int dragonHpBarX = getWidth() - dragonHpBarWidth - 10;
        int dragonHpBarY = 10;

        g.setColor(Color.BLACK);
        g.fillRect(dragonHpBarX, dragonHpBarY, dragonHpBarWidth, dragonHpBarHeight);

        g.setColor(Color.GREEN);
        g.fillRect(dragonHpBarX, dragonHpBarY, (int) (dragonHP * dragonHpBarWidth / (double) MAX_DRAGON_HP), dragonHpBarHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Dragon HP: " + dragonHP + "/" + MAX_DRAGON_HP, dragonHpBarX + 5, dragonHpBarY + 15);
    }

    private void drawKnightHealthBar(Graphics g) {
        int knightHpBarWidth = 200;
        int knightHpBarHeight = 20;
        int knightHpBarX = 10;
        int knightHpBarY = 10;

        g.setColor(Color.BLACK);
        g.fillRect(knightHpBarX, knightHpBarY, knightHpBarWidth, knightHpBarHeight);

        g.setColor(Color.BLUE);
        g.fillRect(knightHpBarX, knightHpBarY, (int) (health * knightHpBarWidth / (double) max_health), knightHpBarHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Knight HP: " + health + "/" + max_health, knightHpBarX + 5, knightHpBarY + 15);
    }

    private void drawWaterwaves(Graphics g) {
        for (Waterwave waterwave : waterwaves) {
            waterwave.draw(g);
        }
    }

    private void drawLightningSpeed(Graphics g) {
        g.setColor(Color.YELLOW); // กำหนดสีเป็นสีเหลือง
        g.setFont(new Font("Arial", Font.BOLD, 20)); // กำหนดฟอนต์

        // แสดงค่า lightningDelay / 10
        String speedText = "SPEED DELAY: " + (lightningDelay / 10.0);

        g.drawString(speedText, 10, 80); // วางข้อความที่ตำแหน่ง (10, 40)
    }

    public void showGameOverScreen(boolean victory) {
        // หยุด timers
        waterwaveTimer.stop();
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
            CharacterBattle gamePanel = new KnightBattle(lightningDelay);

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
            KnightBattle gamePanel = new KnightBattle(this.lightningDelay); // ส่งค่า lightningDelay[0] ใหม่

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

}
