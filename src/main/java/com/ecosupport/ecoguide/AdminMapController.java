package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMapController {

    @FXML
    private Button back_btn;

    @FXML
    void back_to_dashboard(ActionEvent event) throws IOException {
        Stage sign_in_stage = (Stage) back_btn.getScene().getWindow();
        // Show the loading screen
        showLoadingScreen(sign_in_stage);
        // Load the actual scene with data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHomeDashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // Switch to the actual scene after the loading screen
        sign_in_stage.setScene(scene);
        sign_in_stage.setResizable(false);
        sign_in_stage.show();
    }

    private void showLoadingScreen(Stage stage) throws IOException {
        Parent loadingRoot = FXMLLoader.load(getClass().getResource("LoadingScreen.fxml"));
        Scene loadingScene = new Scene(loadingRoot);
        stage.setScene(loadingScene);
        stage.show();
    }
}
