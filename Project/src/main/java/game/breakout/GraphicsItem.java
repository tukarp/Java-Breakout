package game.breakout;

import javafx.scene.canvas.GraphicsContext;


// Create GraphicsItem class
public abstract class GraphicsItem {
    // Create class attributes
    protected static double canvasWidth;   // Create canvasWidth variable
    protected static double canvasHeight;  // Create canvasHeight variable
    protected double width;                // Create width variable
    protected double height;               // Create height variable
    protected double x;                    // Create x variable
    protected double y;                    // Create y variable

    // Create getX method
    public double getX() {
        // Return x
        return x;
    }

    // Create getY method
    public double getY() {
        // Return y
        return y;
    }

    // Create getWidth method
    public double getWidth() {
        // Return width
        return width;
    }

    // Create getHeight method
    public double getHeight() {
        // Return height
        return height;
    }

    // Create setCanvasSize method
    public static void setCanvasSize(double canvasWidth, double canvasHeight) {
        // Set canvasWidth and canvasHeight
        GraphicsItem.canvasWidth = canvasWidth;
        GraphicsItem.canvasHeight = canvasHeight;
    }

    // Create draw method
    public abstract void draw(GraphicsContext graphicsContext);
}
