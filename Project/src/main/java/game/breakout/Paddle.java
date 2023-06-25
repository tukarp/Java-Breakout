package game.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


// Create Paddle class that extends GraphicsItem
public class Paddle extends GraphicsItem {
    // Create constructor
    public Paddle() {
        // Set width and height of the paddle
        width = (canvasWidth * 0.2);   // Set paddle width
        height = (canvasHeight *.02);  // Set paddle height

        // Set x and y coordinates of the paddle
        x = (canvasWidth - width / 2);  // Set paddle x coordinate
        y = (canvasHeight * .9);        // Set paddle y coordinate
    }

    // Override draw method
    @Override
    public void draw(GraphicsContext graphicsContext) {
        // Set color of paddle
        graphicsContext.setFill(Color.RED);
        // Fill paddle with color for given coordinates
        graphicsContext.fillRect(x, y, width, height);
    }

    // Create setPosition method
    void setPosition(double x) {
        // Set x position of paddle
        this.x = clamp((x - width / 2), 0, (canvasWidth - width));
    }

    // Create clamp method
    public static double clamp(double value, double minimum, double maximum) {
        // Return value if value is between minimum and maximum
        return Math.max(minimum, Math.min(maximum, value));
    }

    // Create getPosition method
    public double getPosition() {
        // Return x position of paddle
        return (x + width / 2);
    }
}
