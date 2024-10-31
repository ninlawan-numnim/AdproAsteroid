package se233.demo4.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import se233.demo4.entities.*;
import se233.demo4.game.GameLogger;  // <--- Import GameLogger

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsteroidGame extends Application {
    private double spaceshipX = 400, spaceshipY = 300;
    private final double spaceshipSpeed = 15;
    private double spaceshipAngle = 0;

    private int playerHealth = 100;
    private int score = 0;
    private int destroyedEnemyCount = 0;
    private final int MAX_DESTROYED_ENEMIES = 5;

    private List<Bullet> bullets = new ArrayList<>();
    private List<Asteroid> asteroids = new ArrayList<>();
    private List<EnemyShip> enemyShips = new ArrayList<>();
    private List<UltimateMove> ultimateMoves = new ArrayList<>();

    private Boss boss;
    private boolean bossActive = false;
    private boolean bossDefeated = false;
    private Stage primaryStage;

    private Random random = new Random();
    private AnimationTimer gameLoop;
    private ComboSystem comboSystem = new ComboSystem();  // สร้างระบบคอมโบ
    // 🎯 ประกาศ Logger ที่นี่
    public static final Logger logger = GameLogger.getLogger(); // ประกาศ logger

    // บันทึกการขยับของยานผู้เล่น
    private void logPlayerMovement(double x, double y) {
        logger.log(Level.INFO, "Player moved to X: " + x + ", Y: " + y);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;  // รับ Stage จาก StartMenuController
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            showStartMenu();
            logger.log(Level.INFO, "Game application started"); // ตรวจสอบว่ามีการเรียกใช้งาน Logger อย่างถูกต้อง
        } catch (Exception e) {
            GameExceptionHandler.handleException("Failed to start game", e);
        }
    }

    private void showStartMenu() {
        try {
            Parent startMenu = FXMLLoader.load(getClass().getResource("/se233/demo4/startmenu.fxml"));
            Scene startScene = new Scene(startMenu);
            primaryStage.setScene(startScene);
            primaryStage.show();
            logger.log(Level.INFO,"Start menu loaded successfully");
        } catch (IOException e) {
            GameExceptionHandler.handleException("Failed to load start menu", e);
        }
    }

    public void startGame() {


        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);
        Scene gameScene = new Scene(root);
        primaryStage.setScene(gameScene);  // ใช้ primaryStage ที่ถูกต้อง

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gameScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A -> spaceshipAngle -= 10;
                case D -> spaceshipAngle += 10;
                case LEFT -> spaceshipX -= spaceshipSpeed;
                case RIGHT -> spaceshipX += spaceshipSpeed;
                case UP -> spaceshipY -= spaceshipSpeed;
                case DOWN -> spaceshipY += spaceshipSpeed;
                case SPACE -> bullets.add(new Bullet(spaceshipX, spaceshipY, spaceshipAngle));
                case SHIFT -> {
                    UltimateMove ultimate = new UltimateMove(spaceshipX, spaceshipY);
                    ultimateMoves.add(ultimate);
                    bullets.addAll(ultimate.activate());
                }
            }
            logPlayerMovement(spaceshipX, spaceshipY); // บันทึกการขยับทุกครั้ง
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                gc.save();
                gc.translate(spaceshipX, spaceshipY);
                gc.rotate(spaceshipAngle);
                gc.setFill(Color.BLUE);
                gc.fillRect(-10, -20, 20, 40);
                gc.restore();

                updateUltimateMoves(gc);

                if (random.nextDouble() < 0.02) {
                    if (random.nextDouble() < 0.5) {
                        asteroids.add(new Asteroid(random.nextInt(800), 0));
                    } else {
                        asteroids.add(new AsteroidLevel2(random.nextInt(800), 0));
                    }
                }

                if (destroyedEnemyCount < MAX_DESTROYED_ENEMIES && random.nextDouble() < 0.01) {
                    enemyShips.add(new EnemyShip(random.nextInt(800), 0));
                }

                if (destroyedEnemyCount >= MAX_DESTROYED_ENEMIES && !bossActive && !bossDefeated) {
                    boss = new Boss(400, 50);
                    bossActive = true;
                    logger.log(Level.WARNING, "Boss appeared.");  // <--- Logging บอสปรากฏตัว
                }

                if (bossActive && random.nextDouble() < 0.02) {
                    boss.specialAttack();
                    logger.log(Level.WARNING, "Boss used special attack.");  // <--- Logging การโจมตีบอส
                }

                if (bossActive) {
                    boss.update();
                    boss.render(gc);
                }
                comboSystem.drawComboCounter(gc);
                updateBullets(gc);
                updateAsteroids(gc);
                updateEnemyShips(gc);
                drawScoreAndHealth(gc);
                checkCollisions();
                checkPlayerCollision();

            }
        };

        gameLoop.start();
    }
    // เพิ่มในคลาส AsteroidGame
    public void handleKeyPress(KeyCode code) {
        switch (code) {
            case A -> spaceshipAngle -= 10;
            case D -> spaceshipAngle += 10;
            case LEFT -> spaceshipX -= spaceshipSpeed;
            case RIGHT -> spaceshipX += spaceshipSpeed;
            case UP -> spaceshipY -= spaceshipSpeed;
            case DOWN -> spaceshipY += spaceshipSpeed;
            case SPACE -> bullets.add(new Bullet(spaceshipX, spaceshipY, spaceshipAngle));
            case SHIFT -> {
                UltimateMove ultimate = new UltimateMove(spaceshipX, spaceshipY);
                ultimateMoves.add(ultimate);
                bullets.addAll(ultimate.activate());
            }
        }
    }
    private void updateUltimateMoves(GraphicsContext gc) {
        Iterator<UltimateMove> iterator = ultimateMoves.iterator();
        while (iterator.hasNext()) {
            UltimateMove ultimate = iterator.next();
            ultimate.update();
            if (ultimate.isOffScreen(800, 600)) {
                iterator.remove();
            }
        }
    }

    private void drawScoreAndHealth(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 60, 30);
        gc.fillText("Health: " + playerHealth, 650, 30);
    }

    private void updateBullets(GraphicsContext gc) {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            gc.setFill(Color.RED);
            gc.fillOval(bullet.getX(), bullet.getY(), 5, 5);
            if (bullet.isOffScreen(800, 600)) {
                bulletIterator.remove();
            }
        }
    }

    private void updateAsteroids(GraphicsContext gc) {
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            asteroid.update();
            gc.setFill(Color.GRAY);
            gc.fillOval(asteroid.getX(), asteroid.getY(), asteroid.getSize(), asteroid.getSize());
            if (asteroid.isOffScreen()) {
                asteroidIterator.remove();
            }
        }
    }

    private void updateEnemyShips(GraphicsContext gc) {
        Iterator<EnemyShip> enemyIterator = enemyShips.iterator();
        while (enemyIterator.hasNext()) {
            EnemyShip enemy = enemyIterator.next();
            enemy.update(spaceshipX, spaceshipY);
            gc.setFill(Color.GREEN);
            gc.fillRect(enemy.getX(), enemy.getY(), 20, 20);
            if (enemy.isOffScreen()) {
                enemyIterator.remove();
            }
        }
    }

    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();

            // ตรวจสอบการชนกับอุกกาบาต
            Iterator<Asteroid> asteroidIterator = asteroids.iterator();
            while (asteroidIterator.hasNext()) {
                Asteroid asteroid = asteroidIterator.next();
                if (asteroid.isHit(bullet)) {
                    int points = (asteroid instanceof AsteroidLevel2) ? 2 : 1;
                    comboSystem.registerHit();
                    score += points;
                    asteroidIterator.remove();
                    bulletIterator.remove();
                    logger.log(Level.INFO, "Asteroid hit! Score increased by " + points + ". Current Score: " + score);
                    break;
                }
            }

            // ตรวจสอบการชนกับยานศัตรู
            Iterator<EnemyShip> enemyIterator = enemyShips.iterator();
            while (enemyIterator.hasNext()) {
                EnemyShip enemy = enemyIterator.next();
                if (enemy.isHit(bullet)) {
                    bulletIterator.remove();
                    if (enemy.takeDamage()) {
                        destroyedEnemyCount++;
                        score += 5; // สมมติว่าคะแนนจากการทำลายศัตรูคือ 5
                        comboSystem.registerHit();
                        enemyIterator.remove();
                        logger.log(Level.INFO, "Enemy ship destroyed! Score increased by 5. Current Score: " + score);
                    }
                    break;
                }
            }

            // ตรวจสอบการชนกับบอส
            if (bossActive && boss.isHit(bullet)) {
                bulletIterator.remove();
                comboSystem.registerHit();
                if (boss.takeDamage()) {
                    bossActive = false;
                    bossDefeated = true;
                    logger.log(Level.FINE, "Boss defeated! Final Score: " + score);
                    showEndGameMessage("You Win! Final Score: " + score);
                }
            }
        }
    }

    public void checkPlayerCollision() {
        // ตรวจสอบและแก้ไขตำแหน่งของจรวดถ้าหลุดขอบจอ
        if (spaceshipX < 0) {
            spaceshipX = 800;  // โผล่ทางขวา
        } else if (spaceshipX > 800) {
            spaceshipX = 0;  // โผล่ทางซ้าย
        }

        if (spaceshipY < 0) {
            spaceshipY = 600;  // โผล่ล่าง
        } else if (spaceshipY > 600) {
            spaceshipY = 0;  // โผล่บน
        }
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            if (Math.abs(asteroid.getX() - spaceshipX) < 20 &&
                    Math.abs(asteroid.getY() - spaceshipY) < 20) {
                playerHealth--;
                logger.log(Level.WARNING,"Player hit by Asteroid! Health: " + playerHealth);
                asteroidIterator.remove();
                break;
            }
        }

        Iterator<EnemyShip> enemyIterator = enemyShips.iterator();
        while (enemyIterator.hasNext()) {
            EnemyShip enemy = enemyIterator.next();
            if (Math.abs(enemy.getX() - spaceshipX) < 20 &&
                    Math.abs(enemy.getY() - spaceshipY) < 20) {
                playerHealth--;
                logger.log(Level.WARNING,"Player hit by Enemy Ship! Health: " + playerHealth);
                enemyIterator.remove();
                break;
            }
        }

        if (bossActive) {
            for (BossBullet bullet : boss.getBossBullets()) {
                if (bullet.isHitPlayer(spaceshipX, spaceshipY)) {
                    playerHealth -= 5;
                    logger.log(Level.WARNING,"Player hit by Boss Bullet! Health: " + playerHealth);
                    boss.getBossBullets().remove(bullet);
                    break;
                }
            }
        }

        if (playerHealth <= 0) {
            logger.log(Level.OFF,"Game Over! Final Score: " + score);
            showEndGameMessage("Game Over! Final Score: " + score);
        }
    }

    private void showEndGameMessage(String message) {
        gameLoop.stop();
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setContentText(message);
            alert.showAndWait();
            System.exit(0);
        });

    }
    public double getSpaceshipX() { return spaceshipX; }
    public double getSpaceshipY() { return spaceshipY; }
    public double getSpaceshipAngle() { return spaceshipAngle; }
    public void setSpaceshipX(double x) { spaceshipX = x; }
    public void setSpaceshipY(double y) { spaceshipY = y; }
    public List<Bullet> getBullets() { return bullets; }
    public List<UltimateMove> getUltimateMoves() { return ultimateMoves; }
    public int getPlayerHealth() { return playerHealth; }
    public int getScore() { return score; }
    public int getDestroyedEnemyCount() { return destroyedEnemyCount; }
    public boolean isBossActive() { return bossActive; }
    public int getMaxDestroyedEnemies() { return MAX_DESTROYED_ENEMIES; }

    // Simulation methods for testing
    public void simulateAsteroidCollision() {
        playerHealth--;
    }

    public void simulateEnemyShipCollision() {
        playerHealth--;
    }

    public void simulateBossBulletCollision() {
        playerHealth -= 5;
    }

    public void simulateAsteroidDestruction(boolean isLevel2) {
        score += isLevel2 ? 2 : 1;
    }

    public void simulateEnemyShipDestruction() {
        destroyedEnemyCount++;
        if (destroyedEnemyCount >= MAX_DESTROYED_ENEMIES && !bossActive) {
            bossActive = true;
        }
    }


}