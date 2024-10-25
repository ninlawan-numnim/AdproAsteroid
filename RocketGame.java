package se233.asteroidadpro;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class RocketGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Rocket;
    private ArrayList<Asteroid> asteroids;
    private int score = 0;

    public RocketGame() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);

        rocket = new Rocket(400, 500);  // เริ่มต้นตำแหน่งจรวด
        asteroids = new ArrayList<>();
        timer = new Timer(30, this);  // อัปเดตทุก 30ms
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // วาดจรวด
        rocket.draw(g);

        // วาดอุกกาบาต
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(g);
        }

        // วาดคะแนนและเลือด
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Health: " + rocket.getHealth(), 10, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        spawnAsteroids();

        // อัปเดตการเคลื่อนที่ของอุกกาบาต
        for (Asteroid asteroid : asteroids) {
            asteroid.move();

            // ตรวจสอบการชนกับจรวด
            if (asteroid.intersects(rocket)) {
                rocket.reduceHealth();
                asteroids.remove(asteroid);
                if (rocket.getHealth() <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
                    System.exit(0);
                }
            }
        }

        repaint();  // วาดหน้าจอใหม่
    }

    private void spawnAsteroids() {
        if (new Random().nextInt(50) == 0) {  // สุ่มสร้างอุกกาบาต
            asteroids.add(new Asteroid(new Random().nextInt(800), -50));
        }
    }

    // จัดการการกดปุ่ม
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            rocket.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            rocket.moveRight();
        } else if (key == KeyEvent.VK_UP) {
            rocket.moveUp();
        } else if (key == KeyEvent.VK_DOWN) {
            rocket.moveDown();
        } else if (key == KeyEvent.VK_SPACE) {
            rocket.shoot();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}


