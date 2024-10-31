package se233.demo4.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BossBullet extends Character {
    private double speedX, speedY;

    public BossBullet(double x, double y, double speedX, double speedY) {
        super(x, y, 0);  // ความเร็วพื้นฐานส่งจาก Character
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public void update() {
        x += speedX;
        y += speedY;
    }

    @Override
    public boolean isHit(Bullet bullet) {
        // ตรวจสอบการชนกับกระสุนของผู้เล่น
        return Math.abs(bullet.getX() - x) < 5 &&
                Math.abs(bullet.getY() - y) < 5;
    }

    public boolean isHitPlayer(double spaceshipX, double spaceshipY) {
        // ตรวจสอบการชนกับยานผู้เล่น
        return Math.abs(this.x - spaceshipX) < 10 && Math.abs(this.y - spaceshipY) < 10;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);  // สีของกระสุนบอส
        gc.fillOval(x, y, 10, 10);  // วาดเป็นวงกลมขนาด 10x10
    }

    @Override
    public boolean isOffScreen(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }
}