module game.breakout {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens game.breakout to javafx.fxml;
    exports game.breakout;
}
