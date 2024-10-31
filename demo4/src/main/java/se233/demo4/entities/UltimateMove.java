package se233.demo4.entities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UltimateMove {
    private double x, y;
    private double speed = 5; // กำหนดความเร็วในการเคลื่อนที่

    public UltimateMove(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // ยิงกระสุนรอบทิศทางทุก 45 องศา
    public List<Bullet> activate() {
        List<Bullet> bullets = new ArrayList<>();
        for (int angle = 0; angle < 360; angle += 45) {
            bullets.add(new Bullet(x, y, angle));
        }
        return bullets;
    }

    // ฟังก์ชันอัปเดตตำแหน่งของท่าไม้ตาย
    public void update() {
        y -= speed; // ให้ Ultimate Move เคลื่อนที่ขึ้นด้านบน
    }

    // วาดท่าไม้ตายในกราฟิก


    // ตรวจสอบว่า Ultimate Move หลุดจากหน้าจอหรือไม่
    public boolean isOffScreen(int width, int height) {
        return y < 0 || y > height || x < 0 || x > width;
    }
}