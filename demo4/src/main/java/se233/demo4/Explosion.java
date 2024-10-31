package se233.demo4;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Explosion {
    private double x, y;
    private double size = 10;
    private double expansionRate = 2;

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        size += expansionRate;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    public boolean isFinished() {
        return size > 50;  // การระเบิดจะสิ้นสุดเมื่อขนาดเกิน 50
    }
}
