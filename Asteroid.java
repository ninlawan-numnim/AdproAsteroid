package se233.asteroidadpro;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Asteroid {
    // Constants
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 3;
    private static final int VERTICES = 8;

    // Position and movement
    private double x, y;
    private double dx, dy;
    private int size;
    private double rotationAngle;
    private double rotationSpeed;

    public Asteroid(double x, double y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;

        initializeMovement();
    }

    private void initializeMovement() {
        double angle = Math.random() * 2 * Math.PI;
        double speed = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
        this.dx = Math.cos(angle) * speed;
        this.dy = Math.sin(angle) * speed;
        this.rotationSpeed = Math.random() * 2 - 1;
    }

    public void move() {
        x += dx;
        y += dy;
        rotationAngle += rotationSpeed;

        wrapAround();
    }

    private void wrapAround() {
        if (x < 0) x = SCREEN_WIDTH;
        if (x > SCREEN_WIDTH) x = 0;
        if (y < 0) y = SCREEN_HEIGHT;
        if (y > SCREEN_HEIGHT) y = 0;
    }

    public void draw(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(x, y);
        g2d.rotate(rotationAngle);

        drawAsteroidShape(g2d);

        g2d.setTransform(oldTransform);
    }

    private void drawAsteroidShape(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);

        int[] xPoints = new int[VERTICES];
        int[] yPoints = new int[VERTICES];

        for (int i = 0; i < VERTICES; i++) {
            double angle = 2 * Math.PI * i / VERTICES;
            double radius = size * (0.8 + 0.2 * Math.sin(i * 4));
            xPoints[i] = (int)(radius * Math.cos(angle));
            yPoints[i] = (int)(radius * Math.sin(angle));
        }

        g2d.fillPolygon(xPoints, yPoints, VERTICES);
    }

    public boolean intersects(Rectangle rect) {
        return getBounds().intersects(rect);
    }

    public boolean intersects(double pointX, double pointY, double radius) {
        double dx = x - pointX;
        double dy = y - pointY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size + radius);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(x - size), (int)(y - size), size * 2, size * 2);
    }

    // Getters
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public int getSize() { return size; }
}