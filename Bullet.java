package se233.asteroidadpro;

import java.awt.*;

public class Bullet {
    // Constants
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int SPEED = 10;
    private static final int BULLET_RADIUS = 2;
    private static final Color BULLET_COLOR = Color.RED;

    // Position and movement
    private double x, y;
    private double dx, dy;
    private boolean active;
    private long creationTime;
    private static final long BULLET_LIFETIME = 1500; // 1.5 seconds

    public Bullet(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.dx = Math.cos(angle) * SPEED;
        this.dy = Math.sin(angle) * SPEED;
        this.active = true;
        this.creationTime = System.currentTimeMillis();
    }

    public void move() {
        x += dx;
        y += dy;

        checkBounds();
        checkLifetime();
    }

    private void checkBounds() {
        if (x < 0 || x > SCREEN_WIDTH || y < 0 || y > SCREEN_HEIGHT) {
            active = false;
        }
    }

    private void checkLifetime() {
        if (System.currentTimeMillis() - creationTime > BULLET_LIFETIME) {
            active = false;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(BULLET_COLOR);
        int drawX = (int)x - BULLET_RADIUS;
        int drawY = (int)y - BULLET_RADIUS;
        g2d.fillOval(drawX, drawY, BULLET_RADIUS * 2, BULLET_RADIUS * 2);
    }

    public boolean intersects(Asteroid asteroid) {
        return asteroid.intersects(x, y, BULLET_RADIUS);
    }

    // Getters
    public boolean isActive() { return active; }
    public Rectangle getBounds() {
        return new Rectangle(
                (int)(x - BULLET_RADIUS),
                (int)(y - BULLET_RADIUS),
                BULLET_RADIUS * 2,
                BULLET_RADIUS * 2
        );
    }
}