import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class RockAvoidanceGame extends JPanel implements ActionListener, KeyListener {
    // Game settings
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int OBSTACLE_WIDTH = 50;
    private static final int OBSTACLE_HEIGHT = 50;
    private static final int PLAYER_WIDTH = 60;
    private static final int PLAYER_HEIGHT = 60;

    // Player's spaceship
    private int playerX = WINDOW_WIDTH / 2 - PLAYER_WIDTH / 2;
    private int playerY = WINDOW_HEIGHT - PLAYER_HEIGHT - 20;

    // List of obstacles (rocks)
    private ArrayList<Rectangle> obstacles = new ArrayList<>();
    private int obstacleSpeed = 5;

    // Game state
    private boolean gameOver = false;
    private Timer timer;

    // Images
    private Image spaceshipImage;
    private Image rockImage;

    // Restart Button
    private JButton restartButton;

    public RockAvoidanceGame() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Load images for spaceship and rocks
        spaceshipImage = new ImageIcon("spaceship.png").getImage();
        rockImage = new ImageIcon("rock.png").getImage();

        timer = new Timer(700 / 60, this); // 60 FPS
        timer.start();

        // Initialize the Restart button
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBackground(Color.GREEN);
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(); // Reset game state when the button is clicked
            }
        });
        restartButton.setVisible(false); // Initially, it's not visible
        add(restartButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Move obstacles (rocks)
            for (int i = 0; i < obstacles.size(); i++) {
                Rectangle obstacle = obstacles.get(i);
                obstacle.y += obstacleSpeed;
                if (obstacle.y > WINDOW_HEIGHT) {
                    obstacles.remove(i);
                    i--;
                }

                // Check for collision with the player's spaceship
                if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT))) {
                    gameOver = true;
                    restartButton.setVisible(true); // Show the restart button on game over
                    break;
                }
            }

            // Create new obstacle (rock)
            if (Math.random() < 0.02) {
                int obstacleX = new Random().nextInt(WINDOW_WIDTH - OBSTACLE_WIDTH);
                obstacles.add(new Rectangle(obstacleX, 0, OBSTACLE_WIDTH, OBSTACLE_HEIGHT));
            }

            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            String gameOverMessage = "Game Over!";
            Font gameOverFont = new Font("Arial", Font.BOLD, 50);
            g.setColor(Color.RED);
            g.setFont(gameOverFont);
            FontMetrics fm = g.getFontMetrics();
            int x = (WINDOW_WIDTH - fm.stringWidth(gameOverMessage)) / 2;
            int y = WINDOW_HEIGHT / 2;

            // Draw the "Game Over" message at the center
            g.drawString(gameOverMessage, x, y);

            // Display restart button below the "Game Over" message
            restartButton.setBounds(WINDOW_WIDTH / 2 - 75, y + 60, 150, 50);
        } else {
            // Draw the player's spaceship
            g.drawImage(spaceshipImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);

            // Draw obstacles (rocks)
            for (Rectangle obstacle : obstacles) {
                g.drawImage(rockImage, obstacle.x, obstacle.y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT, this);
            }

            // Display current speed
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Speed: " + obstacleSpeed, 10, 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Left Arrow Key
            if (playerX > 0) {
                playerX -= 10;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Right Arrow Key
            if (playerX < WINDOW_WIDTH - PLAYER_WIDTH) {
                playerX += 10;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_EQUALS) { // "=" Key (also for "+")
            if (e.isShiftDown()) { // Shift + "=" is the "+" key
                obstacleSpeed += 1; // Increase speed
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_MINUS) { // "-" Key
            obstacleSpeed = Math.max(1, obstacleSpeed - 1); // Decrease speed but not below 1
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // Method to restart the game
    private void restartGame() {
        playerX = WINDOW_WIDTH / 2 - PLAYER_WIDTH / 2;
        playerY = WINDOW_HEIGHT - PLAYER_HEIGHT - 20;
        obstacles.clear();
        obstacleSpeed = 5;
        gameOver = false;
        restartButton.setVisible(false); // Hide the restart button
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rock Avoidance Game");
        RockAvoidanceGame gamePanel = new RockAvoidanceGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}
