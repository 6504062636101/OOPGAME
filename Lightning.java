/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;



public class Lightning {
    
    private int x, y; // พิกัด X และ Y ของฟ้าผ่า
    private int speed = 4; // ความเร็วในการตกลงของฟ้าผ่า
    private Image lightningImage;
    public Lightning(int x, int y) {
        this.x = x;
        this.y = y;
        this.lightningImage = new ImageIcon("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\FireEX.png").getImage();
    }

    // ฟังก์ชันอัปเดตตำแหน่ง
    public void update() {
        y += speed; // ให้ฟ้าผ่าตกลงมาทุกครั้งที่ update
    }

    // ฟังก์ชันวาดฟ้าผ่า
    public void draw(Graphics g) {
        g.drawImage(lightningImage, x, y,50,50, null);  // วาดฟ้าผ่าที่ตำแหน่ง x, y
        
    }

    // ฟังก์ชันตรวจสอบการชนกับ Mage
    public boolean collidesWithMage(int mageX, int mageY, int mageWidth, int mageHeight) {
        return x > mageX && x < mageX + mageWidth && y > mageY && y < mageY + mageHeight;
    }

    // Getter สำหรับ Y
    public int getY() {
        return y;
    }
}
