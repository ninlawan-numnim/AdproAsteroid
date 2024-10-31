package se233.demo4.entities;

import javafx.scene.canvas.GraphicsContext;

// Base class for all game characters
public class Character {
    protected double x, y;  // All characters have position
    protected double speed; // All characters have movement speed

    public Character(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    // Common getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Methods that all characters must implement
    public void update() {
        // Default update behavior (can be overridden by subclasses)
    }

    public boolean isOffScreen(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public void render(GraphicsContext gc) {
        // Default render behavior (can be overridden by subclasses)
    }

    public boolean isHit(Bullet bullet) {
        return false;  // Default: no collision (can be overridden by subclasses)
    }
}