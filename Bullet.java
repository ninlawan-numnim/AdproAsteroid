package se233.asteroidadpro;

import java.awt.*;

class Bullet {
    private int x, y;
    private int speed = 10;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 5, 10);
    }

    public void move() {
        y -= speed;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
