package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController {

    @FXML
    private AnchorPane main;

    @FXML
    private Button signup_btn;

    @FXML
    void admin_signup(ActionEvent event) {
        Stage sign_in_stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Signup_Page.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        sign_in_stage.setScene(scene);
        Stage stage = (Stage) signup_btn.getScene().getWindow();
        stage.close();
        sign_in_stage.show();
    }

}
