module se233.demo4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;


    opens se233.demo4 to javafx.fxml;
    exports se233.demo4;
    exports se233.demo4.entities;
    opens se233.demo4.entities to javafx.fxml;
    exports se233.demo4.game;
    opens se233.demo4.game to javafx.fxml;
    exports se233.demo4.controller;
    opens se233.demo4.controller to javafx.fxml;
}