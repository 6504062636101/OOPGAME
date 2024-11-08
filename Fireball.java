package com.mycompany.gameproject;

import java.awt.*;

public class Fireball {

    int x, y;
    boolean isMovingLeft;
    int speed = 5;
    private Image[] frames;
    private int width;
    private int height;

    public Fireball(int startX, int startY, boolean isMovingLeft, Image[] frames) {
        this.x = startX;
        this.y = startY;
        this.isMovingLeft = isMovingLeft;
        this.frames = frames;
    }

    public void move() {
        if (isMovingLeft) {
            x -= 10;
        } else {
            x += 10;
        }
    }

    public boolean collidesWithDragon(int dragonX, int dragonY, int dragonWidth, int dragonHeight) {
        Rectangle fireballRect = new Rectangle(x, y, 30, 30);
        Rectangle dragonRect = new Rectangle(dragonX, dragonY, dragonWidth, dragonHeight);
        return fireballRect.intersects(dragonRect);
    }

    public void draw(Graphics g) {
        g.drawImage(frames[0], x, y, 50, 50, null);
    }

    public void draw(Graphics g, int width, int height) {

        g.drawImage(frames[0], x, y, width, height, null);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
