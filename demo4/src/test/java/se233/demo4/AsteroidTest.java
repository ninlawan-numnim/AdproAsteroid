package se233.demo4;

import org.junit.jupiter.api.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import se233.demo4.entities.Bullet;
import se233.demo4.game.AsteroidGame;

import static org.junit.jupiter.api.Assertions.*;

public class AsteroidTest {
    private AsteroidGame game;
    private static final double DELTA = 0.01;
    private static final double INITIAL_X = 400;
    private static final double INITIAL_Y = 300;
    private static final double SHIP_SPEED = 15;

    @BeforeEach
    void setUp() {
        game = new AsteroidGame();
    }

    // Basic Movement Tests
    @Nested
    class BasicMovementTests {
        @Test
        void testMoveLeft() {
            double initialX = game.getSpaceshipX();
            game.handleKeyPress(KeyCode.LEFT);
            assertEquals(initialX - SHIP_SPEED, game.getSpaceshipX(), DELTA,
                    "Spaceship should move left by SHIP_SPEED units");
        }

        @Test
        void testMoveRight() {
            double initialX = game.getSpaceshipX();
            game.handleKeyPress(KeyCode.RIGHT);
            assertEquals(initialX + SHIP_SPEED, game.getSpaceshipX(), DELTA,
                    "Spaceship should move right by SHIP_SPEED units");
        }

        @Test
        void testMoveUp() {
            double initialY = game.getSpaceshipY();
            game.handleKeyPress(KeyCode.UP);
            assertEquals(initialY - SHIP_SPEED, game.getSpaceshipY(), DELTA,
                    "Spaceship should move up by SHIP_SPEED units");
        }

        @Test
        void testMoveDown() {
            double initialY = game.getSpaceshipY();
            game.handleKeyPress(KeyCode.DOWN);
            assertEquals(initialY + SHIP_SPEED, game.getSpaceshipY(), DELTA,
                    "Spaceship should move down by SHIP_SPEED units");
        }

        @Test
        void testRotateLeft() {
            double initialAngle = game.getSpaceshipAngle();
            game.handleKeyPress(KeyCode.A);
            assertEquals(initialAngle - 10, game.getSpaceshipAngle(), DELTA,
                    "Spaceship should rotate left by 10 degrees");
        }

        @Test
        void testRotateRight() {
            double initialAngle = game.getSpaceshipAngle();
            game.handleKeyPress(KeyCode.D);
            assertEquals(initialAngle + 10, game.getSpaceshipAngle(), DELTA,
                    "Spaceship should rotate right by 10 degrees");
        }
    }

    // Screen Wrapping Tests
    @Nested
    class ScreenWrappingTests {
        @Test
        void testScreenWrapping() {
            // Test right edge wrapping
            game.setSpaceshipX(801);
            game.checkPlayerCollision();
            assertEquals(0, game.getSpaceshipX(), DELTA,
                    "Spaceship should wrap to left edge");

            // Test left edge wrapping
            game.setSpaceshipX(-1);
            game.checkPlayerCollision();
            assertEquals(800, game.getSpaceshipX(), DELTA,
                    "Spaceship should wrap to right edge");

            // Test bottom edge wrapping
            game.setSpaceshipY(601);
            game.checkPlayerCollision();
            assertEquals(0, game.getSpaceshipY(), DELTA,
                    "Spaceship should wrap to top edge");

            // Test top edge wrapping
            game.setSpaceshipY(-1);
            game.checkPlayerCollision();
            assertEquals(600, game.getSpaceshipY(), DELTA,
                    "Spaceship should wrap to bottom edge");
        }
    }

    // Basic Action Tests
    @Nested
    class BasicActionTests {
        @Test
        void testFireBullet() {
            int initialBulletCount = game.getBullets().size();
            game.handleKeyPress(KeyCode.SPACE);
            assertEquals(initialBulletCount + 1, game.getBullets().size(),
                    "One bullet should be added when spacebar is pressed");
        }

        @Test
        void testUltimateMove() {
            int initialUltimateMoveCount = game.getUltimateMoves().size();
            game.handleKeyPress(KeyCode.SHIFT);
            assertEquals(initialUltimateMoveCount + 1, game.getUltimateMoves().size(),
                    "Ultimate move should be activated when shift is pressed");
            assertTrue(game.getBullets().size() > 0,
                    "Ultimate move should create multiple bullets");
        }
    }

    // Basic Scoring Tests
    @Nested
    class BasicScoringTests {
        @Test
        void testAsteroidCollisionReducesHealth() {
            int initialHealth = game.getPlayerHealth();
            game.simulateAsteroidCollision();
            assertEquals(initialHealth - 1, game.getPlayerHealth(),
                    "Health should decrease by 1 when hit by asteroid");
        }

        @Test
        void testEnemyShipCollisionReducesHealth() {
            int initialHealth = game.getPlayerHealth();
            game.simulateEnemyShipCollision();
            assertEquals(initialHealth - 1, game.getPlayerHealth(),
                    "Health should decrease by 1 when hit by enemy ship");
        }

        @Test
        void testBossBulletCollisionReducesHealth() {
            int initialHealth = game.getPlayerHealth();
            game.simulateBossBulletCollision();
            assertEquals(initialHealth - 5, game.getPlayerHealth(),
                    "Health should decrease by 5 when hit by boss bullet");
        }
    }

    // Advanced Scoring Tests
    @Nested
    class AdvancedScoringTests {
        @Test
        void testDestroyAsteroidIncreasesScore() {
            int initialScore = game.getScore();
            game.simulateAsteroidDestruction(false);  // Regular asteroid
            assertEquals(initialScore + 1, game.getScore(),
                    "Score should increase by 1 when destroying regular asteroid");

            game.simulateAsteroidDestruction(true);   // Level 2 asteroid
            assertEquals(initialScore + 3, game.getScore(),
                    "Score should increase by 2 when destroying level 2 asteroid");
        }

        @Test
        void testScoreMultiplier() {
            int initialScore = game.getScore();

            game.simulateAsteroidDestruction(true);  // Level 2 asteroid
            game.simulateAsteroidDestruction(true);  // Level 2 asteroid
            game.simulateAsteroidDestruction(false); // Regular asteroid

            assertEquals(initialScore + 5, game.getScore(),
                    "Score should increase correctly for multiple asteroid destructions");
        }
    }

    // Enemy and Boss Tests
    @Nested
    class EnemyAndBossTests {
        @Test
        void testDestroyEnemyShipIncreasesCount() {
            int initialCount = game.getDestroyedEnemyCount();
            game.simulateEnemyShipDestruction();
            assertEquals(initialCount + 1, game.getDestroyedEnemyCount(),
                    "Destroyed enemy count should increase by 1");
        }

        @Test
        void testBossSpawnsAfterMaxEnemies() {
            for (int i = 0; i < game.getMaxDestroyedEnemies(); i++) {
                game.simulateEnemyShipDestruction();
            }
            assertTrue(game.isBossActive(),
                    "Boss should spawn after destroying maximum number of enemies");
        }
    }

    // Health System Tests
    @Nested
    class HealthSystemTests {
        @Test
        void testCriticalHealth() {
            while (game.getPlayerHealth() > 10) {
                game.simulateAsteroidCollision();
            }

            int healthBeforeHit = game.getPlayerHealth();
            game.simulateAsteroidCollision();

            assertTrue(game.getPlayerHealth() < healthBeforeHit,
                    "Health should still decrease when at critical levels");
        }

        @Test
        void testMultipleCollisionsDamage() {
            int initialHealth = game.getPlayerHealth();

            game.simulateAsteroidCollision();
            game.simulateEnemyShipCollision();
            game.simulateBossBulletCollision();

            assertEquals(initialHealth - 7, game.getPlayerHealth(),
                    "Health should decrease correctly for multiple collision types");
        }
    }
}