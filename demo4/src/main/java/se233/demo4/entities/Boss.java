package se233.demo4.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Boss extends Character {
    private int health;
    private List<BossBullet> bossBullets = new ArrayList<>();

    public Boss(double x, double y) {
        super(x, y, 2.0); // ความเร็วของบอส
        this.health = 10;
    }

    @Override
    public void update() {
        // บอสเคลื่อนที่ในรูปแบบไซน์
        x += Math.sin(System.currentTimeMillis() * 0.001) * speed;
        updateBullets();  // อัปเดตการเคลื่อนที่ของกระสุนบอส
    }

    @Override
    public void render(GraphicsContext gc) {
        // วาดบอสเป็นสี่เหลี่ยมสีแดง
        gc.setFill(Color.RED);
        gc.fillRect(x, y, 100, 50);

        // วาดกระสุนที่บอสยิงออกมา
        gc.setFill(Color.YELLOW);
        for (BossBullet bullet : bossBullets) {
            gc.fillOval(bullet.getX(), bullet.getY(), 10, 10);
        }
    }

    @Override
    public boolean isHit(Bullet bullet) {
        // ตรวจสอบการชนกับกระสุนผู้เล่น
        return bullet.getX() >= x && bullet.getX() <= x + 100 &&
                bullet.getY() >= y && bullet.getY() <= y + 50;
    }

    public boolean takeDamage() {
        health--;
        return health <= 0;
    }

    // ฟังก์ชันยิงกระสุนแบบสุ่มทิศทาง
    public void specialAttack() {
        for (int i = 0; i < 8; i++) {  // ยิง 8 ทิศทาง รอบตัว
            double angle = Math.toRadians(i * 45);
            double bulletSpeedX = Math.cos(angle) * 3;
            double bulletSpeedY = Math.sin(angle) * 3;
            bossBullets.add(new BossBullet(x + 50, y + 25, bulletSpeedX, bulletSpeedY));
        }
    }

    private void updateBullets() {
        // อัปเดตการเคลื่อนที่ของกระสุนบอส
        bossBullets.removeIf(bullet -> bullet.isOffScreen(800, 600));
        for (BossBullet bullet : bossBullets) {
            bullet.update();
        }
    }

    public List<BossBullet> getBossBullets() {
        return bossBullets;
    }

}