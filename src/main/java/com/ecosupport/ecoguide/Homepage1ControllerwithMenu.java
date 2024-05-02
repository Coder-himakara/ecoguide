package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Homepage1ControllerwithMenu {

    @FXML
    private Button animalBtn1;

    @FXML
    private Button plantBtn1;

    @FXML
    private Button menuBtn;

    @FXML
    private Button login_btn;

    @FXML
    void admin_login(ActionEvent event) {
        Stage sign_in_stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        sign_in_stage.setScene(scene);
        Stage stage = (Stage) login_btn.getScene().getWindow();
        stage.close();
        sign_in_stage.show();
    }

}
