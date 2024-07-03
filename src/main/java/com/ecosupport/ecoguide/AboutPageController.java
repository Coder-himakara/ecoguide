package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AboutPageController implements Initializable {

    @FXML
    private Button menuBtn;

    @FXML
    private Button continueBtn;

    @FXML
    private Button menuCloseBtn;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private Button homeBtn;

    @FXML
    private Button feedback;

    @FXML
    private Button admin_btn;

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
    void backToHome(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("HomepageWithMenu.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goToFeedback(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Custemer_feedback.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        Stage primary = new Stage();
        primary.setScene(scene);
        primary.show();
    }

    @FXML
    void menuClose(ActionEvent event) {
        sidebar.setVisible(false);
        menuCloseBtn.setVisible(false);
        menuBtn.setVisible(true);
    }

    @FXML
    void menuOpen(ActionEvent event) {
        sidebar.setVisible(true);
        menuCloseBtn.setVisible(true);
        menuBtn.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
