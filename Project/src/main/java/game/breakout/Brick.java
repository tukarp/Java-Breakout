package game.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;


// Create Brick class that extends GraphicsItem
public class Brick extends GraphicsItem {
    // Create class attributes
    private static int gridRows;        // Create gridRows variable
    private static int gridColumns;     // Create gridColumns variable
    private Color color;                // Create color variable

    // Create Brick constructor
    public Brick(int gridX, int gridY, Color color) {
        // Set color of brick
        this.color = color;

        // Set width and height of brick
        width = (canvasWidth / gridColumns);  // Set brick width
        height = (canvasHeight / gridRows);   // Set brick height

        // Set coordinates of brick
        x = (gridX * width);   // Set x brick coordinate
        y = (gridY * height);  // Set y brick coordinate
    }

    // Create enum CrushType
    public enum CrushType {NoCrush, HorizontalCrush, VerticalCrush};

    // Create getGridRows method
    public static int getGridRows() {
        // Return gridRows
        return gridRows;
    }

    // Create getGridColumns method
    public static int getGridColumns() {
        // Return gridColumns
        return gridColumns;
    }

    // Create setGridRowsAndColumns method
    public static void setGridRowsAndColumns(int gridRows, int gridColumns) {
        // Set brick rows and columns grid
        Brick.gridRows = gridRows;        // Set brick gridRows
        Brick.gridColumns = gridColumns;  // Set brick gridColumns
    }

    // Override draw method
    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(color);  // Set color of brick
        graphicsContext.fillRect(x, y, width, height);  // Fill brick with color for given coordinates
        graphicsContext.setStroke(color.brighter());    // Set color of stroke
        graphicsContext.strokeLine(x, y, (x + width), y);     // Draw line for given coordinates
        graphicsContext.strokeLine(x, y, x, (y + height));    // Draw line for given coordinates
        graphicsContext.setStroke(color.darker());  // Set color of stroke
        graphicsContext.strokeLine(x, (y + height), (x + width), (y + height));  // Draw line for given coordinates
        graphicsContext.strokeLine((x + width), y, (x + width), (y + height));   // Draw line for given coordinates
    }

    // Create crushes method
    CrushType crushes(Point2D left, Point2D right, Point2D top, Point2D bottom) {
        // If contains left or right, return HorizontalCrush
        if(contains(left) || contains(right)) return CrushType.HorizontalCrush;
        // If contains top or bottom, return VerticalCrush
        if(contains(top) || contains(bottom)) return CrushType.VerticalCrush;
        // Otherwise, return NoCrush
        return CrushType.NoCrush;
    }

    // Create contains method
    boolean contains(Point2D point) {
        // Return true if point is within brick
        return ((x <= point.getX()) && (point.getX() <= x + width) && (y <= point.getY()) && (point.getY() <= y + height));
    }
}
