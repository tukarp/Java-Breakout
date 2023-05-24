package game.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import javafx.scene.Scene;
import java.util.List;


// Create GameCanvas class that extends Canvas
public class GameCanvas extends Canvas {
    // Create class attributes
    // Graphics
    private final GraphicsContext graphicsContext = this.getGraphicsContext2D();  // GraphicsContext object
    private final GraphicsContext gameStatusText = this.getGraphicsContext2D();   // GameStatusText object
    private final GraphicsContext scoreText = this.getGraphicsContext2D();        // ScoreText object
    private final Scene scene;  // Scene object

    // Game objects
    private List<Brick> bricks;     // List of Brick objects
    private Paddle paddle;          // Paddle object
    private Ball ball;              // Ball object
    private int score = 0;          // Score variable

    // Game statuses
    private Boolean gameRunning = false;  // Game running boolean
    private enum gameStatusEnum {Running, Won, Lost}  // GameStatusEnum enum
    private gameStatusEnum gameStatus = gameStatusEnum.Running;  // GameStatus variable

    // Create constructor
    private final AnimationTimer animationTimer= new AnimationTimer() {
        // Create lastUpdate variable
        private long lastUpdate;

        // Create handle method
        @Override
        public void handle(long now) {
            // Create diff variable and set it to the difference between now and lastUpdate
            double difference = (now - lastUpdate) / 1000000000.0;
            // Update ball position
            ball.updatePosition(difference);
            // Draw game objects
            draw();
            // Set lastUpdate to now
            lastUpdate = now;

            // Check if ball should bounce horizontally
            if(shouldBallBounceHorizontally()) {
                // Bounce ball horizontally
                ball.bounceHorizontally();
            }
            // Check if ball should bounce vertically
            if(shouldBallBounceVertically()) {
                // Bounce ball vertically
                ball.bounceVertically();
            }

            // Check if ball should bounce from paddle
            if(shouldBallBounceFromPaddle())
                // Bounce ball from paddle
                ball.bounceFromPaddle((-paddle.getPosition() + ((ball.x + ball.width) / 2)) / paddle.width);
            // For each brick in bricks
            for(var brick : bricks) {
                // Create borderPoints variable and set it to the borderPoints of ball
                Point2D[] borderPoints = ball.borderPoints();
                // Check if brick crushes with borderPoints
                Brick.CrushType crushType = brick.crushes(borderPoints[0], borderPoints[1], borderPoints[2], borderPoints[3]);
                // Check if crushType is not NoCrush
                if(crushType != Brick.CrushType.NoCrush) {
                    // Check if crushType is HorizontalCrush
                    if(crushType == Brick.CrushType.HorizontalCrush)
                        // Bounce ball horizontally
                        ball.bounceVertically();
                    else
                        // Bounce ball vertically
                        ball.bounceHorizontally();
                    // Remove brick from bricks
                    bricks.remove(brick);
                    // Add 1 to score
                    score++;

                    // Break
                    break;
                }
            }
        }

        // Override start method
        @Override
        public void start() {
            // Call super.start()
            super.start();
            // Set lastUpdate timer to current time
            lastUpdate = System.nanoTime();
        }
    };

    // Create constructor
    public GameCanvas() {
        // Create game canvas
        super(640, 800);
        // Set canvas width and height
        GraphicsItem.setCanvasSize(getWidth(), getHeight());
        // Set scene
        scene = getScene();

        // Set on mouse moved event
        this.setOnMouseMoved(mouseEvent -> {
            // Set paddle position to mouseEvent x coordinate
            paddle.setPosition(mouseEvent.getX());
            // Draw game objects
            draw();
            // Check if game is not running
            if(!gameRunning) {
                // Set ball position to mouseEvent x coordinate and paddle y coordinate
                ball.setPosition(new Point2D(mouseEvent.getX(), paddle.getY()));
            }
        });
        // Set on mouse clicked event
        this.setOnMouseClicked(mouseEvent -> {
            // Set gameRunning to true
            gameRunning = true;
            // Start animation timer
            animationTimer.start();
        });
    }

    // Create getGameStatus method
    public void getGameStatus() {
        // If ball is out of bounds
        if(ball.getY() > getHeight()) {
            gameStatus = gameStatusEnum.Lost;
            // If there are no bricks left
        } else if(bricks.isEmpty()) {
            gameStatus = gameStatusEnum.Won;
            // Else return "Running"
        }
    }

    // Create draw method
    public void draw() {
        if(gameStatus.equals(gameStatusEnum.Running)) {
            // Get game status
            getGameStatus();
        }

        // Canvas
        // Fill canvas with black color
        graphicsContext.setFill(Color.BLACK);
        // Fill canvas with color for given coordinates
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());

        // Score text
        // Fill score text with white color
        scoreText.setFill(Color.WHITE);
        // Set score text font
        scoreText.setFont(javafx.scene.text.Font.font(20));
        // Draw score text
        scoreText.fillText("Score: " + score, 10, 20);

        // Game objects
        // Draw paddle
        paddle.draw(graphicsContext);
        // Draw ball
        ball.draw(graphicsContext);
        // Draw bricks
        bricks.forEach(brick -> brick.draw(graphicsContext));
        // Draw game status
        drawGameStatus(gameStatus.toString());
    }

    // Create getGameStatus method
    public void drawGameStatus(String gameStatus) {
        // Draw game status text
        if(gameStatus.equals("Lost")) {
            // Load "You lost!" text
            // Fill game status text with white color
            gameStatusText.setFill(Color.WHITE);
            // Set game status text font
            gameStatusText.setFont(javafx.scene.text.Font.font(60));
            // Draw game status text
            gameStatusText.fillText("You lost!", 200, 400);
        } else if(gameStatus.equals("Won")) {
            // Load "You won!" text
            // Fill game status text with white color
            gameStatusText.setFill(Color.WHITE);
            // Set game status text font
            gameStatusText.setFont(javafx.scene.text.Font.font(60));
            // Draw game status text
            gameStatusText.fillText("You won!", 200, 400);
        }
    }

    // Create initialize method
    public void initialize(){
        // Set paddle to new Paddle object
        paddle = new Paddle();
        // Set ball to new Ball object
        ball = new Ball();
        // Load level
        loadLevel();
    }

    // Create shouldBallBounceHorizontally method
    private boolean shouldBallBounceHorizontally() {
        // Return true if ball matches conditions
        return ball.lastY > 0 && ball.y <= 0;
    }

    // Create shouldBallBounceFromPaddle method
    private boolean shouldBallBounceFromPaddle() {
        // Return true if ball matches conditions
        return ball.lastY + ball.height < paddle.y && ball.y + ball.height >= paddle.y
                && ball.x >= paddle.x && ball.x <= paddle.x + paddle.width;
    }

    // Create shouldBallBounceVertically method
    private boolean shouldBallBounceVertically() {
        // Return true if ball matches conditions
        return  (ball.x <= 0 && ball.lastX > 0)
                || (ball.x + ball.width >= getWidth() - 1 && ball.lastX + ball.width < getWidth() - 1);
    }

    // Create loadLevel method
    public void loadLevel() {
        // Set bricks to new ArrayList
        bricks = new ArrayList<>();
        // Create colors array
        Color[] colors = new Color[]{Color.RED, Color.BEIGE, Color.BROWN, Color.GREENYELLOW, Color.BLUE};
        // Set grid rows and columns
        Brick.setGridRowsAndColumns(20,10);
        // For each row
        for(int i = 0; i < 5; i++) {
            // For each column
            for(int j = 0; j < Brick.getGridColumns(); j++)
                // Add new Brick object to bricks
                bricks.add(new Brick(j, i + 2, colors[i]));
        }
    }
}
