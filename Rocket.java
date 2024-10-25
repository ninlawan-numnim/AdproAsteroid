package se233.asteroidadpro;

import java.util.ArrayList;

import java.awt.*;

public class Rocket {
    public int x, y;
    private final int SPEED = 10;  // ความเร็วของจรวด

    public Rocket(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // วาดจรวด
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 50, 50);  // วาดสี่เหลี่ยมแทนจรวด
    }

    // การเคลื่อนที่ของจรวด
    public void moveLeft() {
        if (x > 0) {
            x -= SPEED;
        }
    }

    public void moveRight() {
        if (x < 750) {  // 750 คือขอบขวาของหน้าจอ (800 - ขนาดจรวด)
            x += SPEED;
        }
    }

    public void moveUp() {
        if (y > 0) {
            y -= SPEED;
        }
    }

    public void moveDown() {
        if (y < 550) {  // 550 คือขอบล่างของหน้าจอ (600 - ขนาดจรวด)
            y += SPEED;
        }
    }


    public void shoot() {
        bullets.add(new Bullet(x + 20, y));
    }

    public void moveBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.move();
            if (bullet.getY() < 0) {
                bullets.remove(i);
                i--;
            }
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getHealth() {
        return health;
    }

    public void reduceHealth() {
        health--;
    }
}


