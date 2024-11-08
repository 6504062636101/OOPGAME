package com.mycompany.gameproject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Waterwave {

    int x, y;               // ตำแหน่งของลูกไฟ
    boolean isMovingLeft;    // ทิศทางการเคลื่อนที่ (ซ้ายหรือขวา)
    int speed = 10;          // ความเร็วในการเคลื่อนที่
    private Image[] frames;  // อาร์เรย์เก็บภาพของลูกไฟ
    private int distanceTravelled = 0;  // ระยะทางที่ลูกไฟเคลื่อนที่
    private final int MAX_DISTANCE = 30;  // ระยะทางที่ลูกไฟสามารถพุ่งไปได้

    // คอนสตรัคเตอร์
    public Waterwave(int startX, int startY, boolean isMovingLeft, Image[] frames) {
        this.x = startX;
        this.y = startY;
        this.isMovingLeft = isMovingLeft;
        this.frames = frames;
    }

    // ฟังก์ชันเคลื่อนที่ของลูกไฟ
    public void move() {
        if (distanceTravelled < MAX_DISTANCE) {  // ถ้ายังไม่ถึงระยะที่กำหนด
            if (isMovingLeft) {
                x -= speed;  // ถ้ากำลังเคลื่อนที่ไปทางซ้าย
            } else {
                x += speed;  // ถ้ากำลังเคลื่อนที่ไปทางขวา
            }
            distanceTravelled += speed;  // เพิ่มระยะทางที่ลูกไฟเคลื่อนที่
        }
    }

    // ฟังก์ชันตรวจสอบการชนกับมังกร
    public boolean collidesWithDragon(int dragonX, int dragonY, int dragonWidth, int dragonHeight) {
        Rectangle fireballRect = new Rectangle(x, y, 30, 30); // ขนาดของลูกไฟ
        Rectangle dragonRect = new Rectangle(dragonX, dragonY, dragonWidth, dragonHeight); // ขนาดของมังกร
        return fireballRect.intersects(dragonRect);  // ตรวจสอบการชน
    }

    // ฟังก์ชันวาดลูกไฟ
    public void draw(Graphics g) {
        g.drawImage(frames[0], x, y, 50, 50, null); // วาดภาพลูกไฟที่ตำแหน่ง x, y
    }
}
