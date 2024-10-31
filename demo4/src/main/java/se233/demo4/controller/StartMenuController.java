package se233.demo4.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import se233.demo4.game.AsteroidGame;

public class StartMenuController {

    @FXML
    private Button startButton;

    private AsteroidGame gameApp;  // เก็บตัวอ้างอิงถึงเกม

    public StartMenuController() {
        gameApp = new AsteroidGame();  // สร้างตัวเกมเพียงครั้งเดียว
    }

    @FXML
    public void onStartButtonClick(ActionEvent event) {
        try {
            Stage stage = (Stage) startButton.getScene().getWindow();
            gameApp.setPrimaryStage(stage);  // ส่ง Stage ไปยัง AsteroidGame
            gameApp.startGame();  // เริ่มเกม
        } catch (Exception e) {
            System.out.println("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }
}