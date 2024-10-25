package se233.asteroidadpro;
import javax.swing.*;

public class GameLauncher {
    public static void main(String[] args) {
        // Run the game setup on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create the main window
            JFrame frame = new JFrame("Asteroid Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create and add the game panel
            RocketGame game = new RocketGame();
            frame.add(game);

            // Pack and center the window
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            // Make the window visible and start the game
            frame.setVisible(true);
            game.timer.start();  // Start the game timer
        });
    }
}
