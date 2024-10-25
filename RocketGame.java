package se233.asteroidadpro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
public class RocketGame extends JPanel {
    // Constants
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int FRAME_DELAY = 16; // ~60 FPS
    private static final int INITIAL_ASTEROIDS = 4;
    private static final int MIN_ASTEROIDS = 5;
    private static final double SPAWN_PROBABILITY = 0.02;

    // Game objects
    private Rocket rocket;
    private ArrayList<Asteroid> asteroids;
    private int score;
    private int highScore;
    private GameState gameState;
    private boolean isPaused;
    private Timer gameTimer;
    private boolean[] keyStates;

    private enum GameState { MENU, PLAYING, PAUSED, GAME_OVER }

    public RocketGame() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        keyStates = new boolean[256];
        gameState = GameState.MENU;

        setupGame();
        setupTimer();
        setupKeyBindings();
    }

    private void setupGame() {
        rocket = new Rocket(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
        asteroids = new ArrayList<>();
        score = 0;
        isPaused = false;

        initializeAsteroids();
    }

    private void setupTimer() {
        gameTimer = new Timer(FRAME_DELAY, e -> updateGame());
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Movement keys
        setupKeyBinding(im, am, KeyEvent.VK_LEFT, "LEFT");
        setupKeyBinding(im, am, KeyEvent.VK_RIGHT, "RIGHT");
        setupKeyBinding(im, am, KeyEvent.VK_UP, "UP");

        // Action keys
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "SPACE");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "PAUSE");

        am.put("SPACE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSpaceKey();
            }
        });

        am.put("PAUSE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameState.PLAYING) {
                    togglePause();
                }
            }
        });
    }

    private void setupKeyBinding(InputMap im, ActionMap am, int keyCode, String binding) {
        im.put(KeyStroke.getKeyStroke(keyCode, 0, false), binding + "_PRESSED");
        im.put(KeyStroke.getKeyStroke(keyCode, 0, true), binding + "_RELEASED");

        am.put(binding + "_PRESSED", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyStates[keyCode] = true;
            }
        });

        am.put(binding + "_RELEASED", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyStates[keyCode] = false;
            }
        });
    }

    private void initializeAsteroids() {
        asteroids.clear();
        for (int i = 0; i < INITIAL_ASTEROIDS; i++) {
            spawnAsteroid();
        }
    }

    private void spawnAsteroid() {
        double angle = Math.random() * 2 * Math.PI;
        double distance = Math.max(WINDOW_WIDTH, WINDOW_HEIGHT) / 2.0;
        double x = WINDOW_WIDTH/2 + Math.cos(angle) * distance;
        double y = WINDOW_HEIGHT/2 + Math.sin(angle) * distance;
}
}