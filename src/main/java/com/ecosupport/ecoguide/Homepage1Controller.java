package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Homepage1Controller {

    @FXML
    private Button animalBtn;

    @FXML
    private Button plantBtn;

    @FXML
    private Button menuBtn;

    @FXML
    private void openMenu(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("HomepageWithMenu.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) menuBtn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Homepage1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
