package se233.demo4.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Particle {
    private double x, y, speedX, speedY;
    private Random random = new Random();

    public Particle() {
        this.x = random.nextDouble() * 800;
        this.y = random.nextDouble() * 600;
        this.speedX = random.nextDouble() * 4 - 2;
        this.speedY = random.nextDouble() * 4 - 2;
    }

    public void update() {
        x += speedX;
        y += speedY;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillOval(x, y, 5, 5);
    }
}
