package game.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import javafx.scene.Scene;
import java.util.Random;
import java.util.List;


// Create GameCanvas class that extends Canvas
public class GameCanvas extends Canvas {
    // Create class attributes
    // Graphics
    private final GraphicsContext graphicsContext = this.getGraphicsContext2D();    // GraphicsContext object
    private final GraphicsContext gameStatusText = this.getGraphicsContext2D();     // GameStatusText object
    private final GraphicsContext scoreText = this.getGraphicsContext2D();          // ScoreText object
    private final Scene scene;                                                      // Scene object

    // Game objects
    private List<Brick> bricks;     // List of Brick objects
    private Paddle paddle;          // Paddle object
    private Ball ball;              // Ball object
    private int score = 0;          // Score variable

    // Game statuses
    private Boolean gameRunning = false;                            // Game running boolean
    private enum gameStatusEnum {Running, Won, Lost}                // GameStatusEnum enum
    private gameStatusEnum gameStatus = gameStatusEnum.Running;     // GameStatus variable

    // Create constructor
    private final AnimationTimer animationTimer= new AnimationTimer() {
        // Create lastUpdate variable
        private long lastUpdate;

        // Override handle method
        @Override
        public void handle(long now) {
            // Create diff variable and set it to the difference between now and lastUpdate
            double difference = ((now - lastUpdate) / 1000000000.0);
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
                    // Otherwise
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
        // Check if ball is out of bounds
        if(ball.getY() > getHeight()) {
            // Set game status to game lost
            gameStatus = gameStatusEnum.Lost;
        // Check if there are no bricks left
        } else if(bricks.isEmpty()) {
            // Set game status to game won
            gameStatus = gameStatusEnum.Won;
        }
    }

    // Create draw method
    public void draw() {
        // Check if game status is running
        if(gameStatus.equals(gameStatusEnum.Running)) {
            // Get game status
            getGameStatus();
        }

        // Canvas
        graphicsContext.setFill(Color.BLACK);  // Fill canvas with black color
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());  // Fill canvas with color for given coordinates

        // Score text
        scoreText.setFill(Color.WHITE);  // Fill score text with white color
        scoreText.setFont(javafx.scene.text.Font.font(20));  // Set score text font
        scoreText.fillText("Score: " + score, 10, 20);  // Draw score text

        // Game objects
        paddle.draw(graphicsContext);  // Draw paddle
        ball.draw(graphicsContext);  // Draw ball
        bricks.forEach(brick -> brick.draw(graphicsContext));  // Draw bricks
        drawGameStatus(gameStatus.toString());  // Draw game status
    }

    // Create getGameStatus method
    public void drawGameStatus(String gameStatus) {
        // Draw game status text
        // Check if player lost
        if(gameStatus.equals("Lost")) {
            // Load "You lost!" text
            // Fill game status text with white color
            gameStatusText.setFill(Color.WHITE);
            // Set game status text font
            gameStatusText.setFont(javafx.scene.text.Font.font(60));
            // Draw game status text
            gameStatusText.fillText("You lost!", 200, 400);
        // Check if player won
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
        // Return true if ball lastY coordinate is greater than 0 and y coordinate is lesser or equal to 0
        return ball.lastY > 0 && ball.y <= 0;
    }

    // Create shouldBallBounceFromPaddle method
    private boolean shouldBallBounceFromPaddle() {
        // Return true if
        // ((Ball lastY coordinate + ball height is lesser than paddle y coordinate) and
        // (Ball y coordinate + ball height is greater or equal to paddle y coordinate) and
        // (Ball x coordinate is lesser or equal to paddle x coordinate + paddle width)) and
        // (Ball x coordinate is greater or equal to paddle x coordinate)
        return ((ball.lastY + ball.height < paddle.y) && (ball.y + ball.height >= paddle.y) &&
               (ball.x <= paddle.x + paddle.width) && (ball.x >= paddle.x));
    }

    // Create shouldBallBounceVertically method
    private boolean shouldBallBounceVertically() {
        // Return true if
        // (((Ball x coordinate is lesser or equal to 0) and
        // (Ball last x coordinate is greater than 0))
        // Or
        // ((Ball x coordinate + ball width is greater or equal to width - 1) and
        // (Ball last x coordinate + ball width is lesser than width - 1)))
        return  (((ball.x <= 0) && (ball.lastX > 0)) ||
                ((ball.x + ball.width >= getWidth() - 1) && (ball.lastX + ball.width < getWidth() - 1)));
    }

    // Create loadLevel method
    public void loadLevel() {
        // Set bricks to new ArrayList
        bricks = new ArrayList<>();
        // Set grid for rows and columns
        Brick.setGridRowsAndColumns(50, 10);
        // Create colors array
        Color[] colors = new Color[] {
                Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.PINK,
                Color.ORANGE, Color.BROWN, Color.BLACK, Color.WHITE, Color.GRAY,
                Color.GOLD, Color.SILVER, Color.BLUE, Color.SKYBLUE, Color.LIMEGREEN,
                Color.TEAL, Color.INDIGO, Color.MAGENTA, Color.VIOLET, Color.KHAKI,
                Color.SALMON, Color.CRIMSON, Color.LAVENDER, Color.PLUM, Color.BLUEVIOLET,
                Color.OLIVE, Color.CYAN, Color.MAROON, Color.BEIGE
        };
        // For each row
        for(int i = 0; i < 20; i++) {
            // Create colorIndex variable and set it to random number in range of colors array length
            int colorIndex = (int) ((Math.random() * (colors.length - 1)));
            // For each column
            for(int j = 0; j < Brick.getGridColumns(); j++)
                // Add new Brick object to bricks
                bricks.add(new Brick(j, (i + 2), colors[colorIndex]));
        }
    }
}
