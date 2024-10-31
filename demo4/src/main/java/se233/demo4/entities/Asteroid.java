package se233.demo4.entities;

public class Asteroid extends Character {
    public Asteroid(double x, double y) {
        super(x, y, 2.0); // Using the original speed value
    }

    @Override
    public void update() {
        y += speed;
    }

    @Override
    public boolean isOffScreen(int width, int height) {
        return false;
    }

    @Override
    public boolean isHit(Bullet bullet) {
        return bullet.getX() >= x && bullet.getX() <= x + getSize() &&
                bullet.getY() >= y && bullet.getY() <= y + getSize();
    }

    public boolean isOffScreen() {
        return y > 600;
    }

    public double getSize() {
        return 30;  // Standard asteroid size
    }
}