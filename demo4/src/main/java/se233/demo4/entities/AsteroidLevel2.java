package se233.demo4.entities;

public class AsteroidLevel2 extends Asteroid {
    public AsteroidLevel2(double x, double y) {
        super(x, y);
        speed = 1.5; // Override speed for Level 2 asteroid
    }

    @Override
    public void update() {
        y += speed;  // Uses the modified speed
    }

    @Override
    public boolean isHit(Bullet bullet) {
        if (super.isHit(bullet)) {
            System.out.println("Hit Asteroid Level 2! +2 points");
            return true;
        }
        return false;
    }

    @Override
    public double getSize() {
        return 40;  // Larger size than regular asteroid
    }
}