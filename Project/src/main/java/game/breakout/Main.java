package game.breakout;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.stage.Stage;


// Create Main class that extends Application
public class Main extends Application {
    // Override the start method
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create a new GameCanvas object
        GameCanvas gameCanvas = new GameCanvas();
        // Set the title of the game window
        primaryStage.setTitle("Breakout");
        // Create a new GridPane object
        GridPane gridPane = new GridPane();

        // Add the gameCanvas to the pane
        gridPane.add(gameCanvas, 0, 0);
        // Create a new scene with the gridPane
        primaryStage.setScene(new Scene(gridPane));
        // Set resizable argument to false
        primaryStage.setResizable(false);

        // Initialize the gameCanvas
        gameCanvas.initialize();
        // Draw the gameCanvas
        gameCanvas.draw();
        // Show the gameCanvas
        primaryStage.show();
    }

    // Main method
    public static void main(String[] args) {
        // Launch the application
        launch(args);
    }
}
