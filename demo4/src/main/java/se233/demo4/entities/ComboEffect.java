package se233.demo4.entities;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
public class ComboEffect {
    private double x, y;
    private int duration;
    private int comboCount;
    private List<Particle> particles;

    public ComboEffect(int comboCount) {
        this.comboCount = comboCount;
        this.duration = 60; // frames
        initializeParticles();
    }

    private void initializeParticles() {
        particles = new ArrayList<>();
        int particleCount = Math.min(comboCount * 2, 20);
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle());
        }
    }

    public void draw(GraphicsContext gc) {
        for (Particle particle : particles) {
            particle.update();
            particle.draw(gc);
        }
        duration--;
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}
