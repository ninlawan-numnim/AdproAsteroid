package se233.asteroidadpro;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;

public class Rocket {
    // Constants
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int SHIP_SIZE = 30;
    private static final int SPEED = 5;
    private static final int ROTATION_SPEED = 5;
    private static final int INITIAL_LIVES = 3;
    private static final long INVULNERABILITY_DURATION = 3000; // 3 seconds

    // Position and movement
    private double x, y;
    private double angle;
    private double dx, dy;
    private ArrayList<Bullet> bullets;
    private int lives;
    private long lastHitTime;
    private boolean isInvulnerable;

    public Rocket(int x, int y) {
        this.x = x;
        this.y = y;
        this.angle = -90; // Start facing upward
        this.bullets = new ArrayList<>();
        this.lives = INITIAL_LIVES;
        this.lastHitTime = 0;
        this.dx = 0;
        this.dy = 0;
    }

    public void moveForward() {
        double rad = Math.toRadians(angle);
        dx += Math.cos(rad) * 0.5;
        dy += Math.sin(rad) * 0.5;

        // Limit speed
        double speed = Math.sqrt(dx * dx + dy * dy);
        if (speed > SPEED) {
            dx = (dx / speed) * SPEED;
            dy = (dy / speed) * SPEED;
        }
    }

    public void update() {
        // Apply movement
        x += dx;
        y += dy;

        // Apply friction
        dx *= 0.98;
        dy *= 0.98;

        // Check invulnerability
        isInvulnerable = (System.currentTimeMillis() - lastHitTime) < INVULNERABILITY_DURATION;

        wrapAround();
        updateBullets();
    }

    private void wrapAround() {
        if (x < 0) x = SCREEN_WIDTH;
        if (x > SCREEN_WIDTH) x = 0;
        if (y < 0) y = SCREEN_HEIGHT;
        if (y > SCREEN_HEIGHT) y = 0;
    }

    public void draw(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();

        // Draw ship
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(angle));

        if (!isInvulnerable || System.currentTimeMillis() % 200 < 100) {
            drawShip(g2d);
        }

        g2d.setTransform(oldTransform);

        // Draw bullets
        bullets.forEach(bullet -> bullet.draw(g2d));
    }

    private void drawShip(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        int[] xPoints = {0, -SHIP_SIZE/2, SHIP_SIZE/2};
        int[] yPoints = {-SHIP_SIZE/2, SHIP_SIZE/2, SHIP_SIZE/2};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    public void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.move();
            if (!bullet.isActive()) {
                it.remove();
            }
        }
    }

    public void shoot() {
        double rad = Math.toRadians(angle);
        // Create bullet at ship's nose
        double bulletX = x + Math.cos(rad) * (SHIP_SIZE/2);
        double bulletY = y + Math.sin(rad) * (SHIP_SIZE/2);
        bullets.add(new Bullet(bulletX, bulletY, rad));
    }

    public void hit() {
        if (!isInvulnerable) {
            lives--;
            lastHitTime = System.currentTimeMillis();
            // Reset position
            x = SCREEN_WIDTH / 2;
            y = SCREEN_HEIGHT / 2;
            dx = 0;
            dy = 0;
        }
    }

    public void rotateLeft() { angle -= ROTATION_SPEED; }
    public void rotateRight() { angle += ROTATION_SPEED; }
    public void clearBullets() { bullets.clear(); }

    // Getters
    public ArrayList<Bullet> getBullets() { return bullets; }
    public int getLives() { return lives; }
    public Rectangle getBounds() {
        return new Rectangle(
                (int)(x - SHIP_SIZE/2),
                (int)(y - SHIP_SIZE/2),
                SHIP_SIZE,
                SHIP_SIZE
        );
    }
}