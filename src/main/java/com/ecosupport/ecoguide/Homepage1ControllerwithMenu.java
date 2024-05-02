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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Homepage1ControllerwithMenu {

    @FXML
    private Button animalBtn1;

    @FXML
    private Button plantBtn1;

    @FXML
    private Button admin_btn;

    @FXML
    private Button menuBtn;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private Button menuCloseBtn;

    @FXML
    void administrator(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) admin_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Homepage1ControllerwithMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void menuOpen(ActionEvent event) {
        sidebar.setVisible(true);
        menuCloseBtn.setVisible(true);
        menuBtn.setVisible(false);
    }

    @FXML
    void menuClose(ActionEvent event) {
        sidebar.setVisible(false);
        menuCloseBtn.setVisible(false);
        menuBtn.setVisible(true);
    }
}
