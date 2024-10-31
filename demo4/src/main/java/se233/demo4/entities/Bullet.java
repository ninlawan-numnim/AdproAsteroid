package se233.demo4.entities;

public class Bullet {
    private double x, y;
    private final double speed = 10;
    private double angle;

    public Bullet(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void update() {
        x += speed * Math.cos(Math.toRadians(angle));
        y += speed * Math.sin(Math.toRadians(angle));
    }

    public boolean isOffScreen(int screenWidth, int screenHeight) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public boolean isHit(Character target) {
        return getX() >= target.getX() && getX() <= target.getX() + 20 &&
                getY() >= target.getY() && getY() <= target.getY() + 20;
    }
}