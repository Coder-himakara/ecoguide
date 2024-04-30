package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminLoginController {

    @FXML
    private AnchorPane main;

    @FXML
    private TextField uName;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signup_btn;

    @FXML
    private TextField admin_id;


    @FXML
    void admin_login(ActionEvent event){
        PreparedStatement statement;
        ResultSet result;
        Alert alert;
        String id = admin_id.getText();
        String username = uName.getText();
        String pass_word = password.getText();

        String query = "SELECT * FROM `new_admin` WHERE id_no= ? and username = ? and password = ?";
        try{
            statement = DbConfig.getConnection().prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, username);
            statement.setString(3, pass_word);
            result = statement.executeQuery();
            if (admin_id.getText().isEmpty() || uName.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else{
                if(result.next()){
                    try {
                        Stage sign_in_stage = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("AdminHomeDashboard.fxml"));
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add("/styles/AdminHomeDashboard.css");
                        sign_in_stage.setScene(scene);
                        Stage stage = (Stage) loginBtn.getScene().getWindow();
                        stage.close();
                        sign_in_stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(AdminLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Admin ID/ Username/ Password");
                    alert.showAndWait();
                    admin_id.setText("");
                    uName.setText("");
                    password.setText("");
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

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
