package game.breakout;

import javafx.scene.canvas.GraphicsContext;


// Create GraphicsItem class
public abstract class GraphicsItem {
    // Create class attributes
    protected static double canvasWidth;   // Create canvasWidth variable
    protected static double canvasHeight;  // Create canvasHeight variable
    protected double width;                // Create width variable
    protected double height;               // Create height variable
    protected double x;                    // Create x coordinate variable
    protected double y;                    // Create y coordinate variable

    // Create getX method
    public double getX() {
        // Return x coordinate
        return x;
    }

    // Create getY method
    public double getY() {
        // Return y coordinate
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
        // Set canvas width and height
        GraphicsItem.canvasWidth = canvasWidth;    // Set canvas width
        GraphicsItem.canvasHeight = canvasHeight;  // Set canvas height
    }

    // Create draw method
    public abstract void draw(GraphicsContext graphicsContext);
}
