package se233.demo4.entities;

// Player ship class
public class PlayerShip extends Character {
    private double angle;
    private int health;

    public PlayerShip(double x, double y) {
        super(x, y, 5.0); // Player speed from original code
        this.angle = 0;
        this.health = 3;
    }

    @Override
    public void update() {
        // Movement is handled by key input in the game class
    }

    @Override
    public boolean isHit(Bullet bullet) {
        return Math.abs(bullet.getX() - x) < 20 &&
                Math.abs(bullet.getY() - y) < 20;
    }

    public void rotate(double degrees) {
        angle += degrees;
    }

    public double getAngle() {
        return angle;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth() {
        health--;
    }
}
