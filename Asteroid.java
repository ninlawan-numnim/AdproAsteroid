package se233.asteroidadpro;

import java.awt.*;
import java.util.Random;

class Asteroid {
    private int x, y, speed;

    public Asteroid(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = new Random().nextInt(5) + 1;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillOval(x, y, 50, 50);
    }

    public void move() {
        y += speed;
    }

    public boolean intersects(Rocket rocket) {
        return new Rectangle(x, y, 50, 50).intersects(new Rectangle(rocket.x, rocket.y, 50, 50));
    }

    public boolean intersects(Bullet bullet) {
        return new Rectangle(x, y, 50, 50).intersects(new Rectangle(bullet.getX(), bullet.getY(), 5, 10));
    }
}
