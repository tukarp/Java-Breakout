package game.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;


// Create Ball class and extend it from GraphicsItem
public class Ball extends GraphicsItem {
    // Create class attributes
    private Point2D moveVector = new Point2D(1, -1).normalize();  // Create moveVector variable and set it to (1, -1)
    private double velocity = 600;  // Create velocity variable and set it to 600
    double lastX;  // Create lastX ball coordinate
    double lastY;  // Create lastY ball coordinate

    // Create Ball constructor
    public Ball() {
        // Set width and height of ball
        width = height = canvasHeight * 0.01;
    }

    // Override draw method
    @Override
    public void draw(GraphicsContext graphicsContext) {
        // Set color of ball
        graphicsContext.setFill(Color.WHITE);
        // Fill ball with color for given coordinates
        graphicsContext.fillOval(x,y, width, height);
    }

    // Create setPosition method
    void setPosition(Point2D point) {
        // Set x and y position of ball
        this.x = point.getX() - width / 2;
        this.y = point.getY() - width / 2 - 5;
    }

    // Create bounceHorizontally method
    public void bounceVertically() {
        // Set moveVector to new Point2D and normalize it
        moveVector = new Point2D(-moveVector.getX(), moveVector.getY()).normalize();
    }

    // Create bounceVertically method
    public void bounceHorizontally() {
        // Set moveVector to new Point2D and normalize it
        moveVector = new Point2D(moveVector.getX(), -moveVector.getY()).normalize();
    }

    // Create bounceFromPaddle method
    public void bounceFromPaddle(double position) {
        // Set moveVector to new Point2D and normalize it
        moveVector = new Point2D(position * 0.8, -moveVector.getY()).normalize();
    }

    // Create updatePosition method
    public void updatePosition(double difference) {
        // Set lastX and lastY ball coordinates
        lastX = this.x;
        lastY = this.y;

        // Set x and y ball coordinates
        this.x += moveVector.getX() * velocity * difference;
        this.y += moveVector.getY() * velocity * difference;
    }

    // Create borderPoints method
    public Point2D[] borderPoints() {
        // Create Point2D result array
        Point2D[] result = new Point2D[4];
        // Set result array values
        result[0] = new Point2D(x, y + height / 2);
        result[1] = new Point2D(x + width, y + height / 2);
        result[2] = new Point2D(x + width / 2, y);
        result[3] = new Point2D(x + width / 2, y + height);
        // Return result array
        return result;
    }
}
