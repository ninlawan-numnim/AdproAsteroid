package se233.demo4.entities;

public class EnemyShip {
    private double x, y;
    private int health = 2;  // พลังชีวิตของยานศัตรู

    public EnemyShip(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update(double playerX, double playerY) {
        // เคลื่อนที่ตามผู้เล่น
        double directionX = playerX - x;
        double directionY = playerY - y;
        double length = Math.sqrt(directionX * directionX + directionY * directionY);

        x += (directionX / length) * 2;  // ปรับความเร็วให้เป็น 2
        y += (directionY / length) * 2;
    }

    public boolean isOffScreen() {
        return x < 0 || x > 800 || y < 0 || y > 600;
    }

    public boolean isHit(Bullet bullet) {
        return bullet.getX() >= x && bullet.getX() <= x + 20 &&
                bullet.getY() >= y && bullet.getY() <= y + 20;
    }

    public boolean takeDamage() {
        health--;
        return health <= 0;  // ยานศัตรูถูกทำลายเมื่อ health <= 0
    }

    public double getX() { return x; }
    public double getY() { return y; }
}