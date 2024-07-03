package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private Button menuBtn;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private Button menuCloseBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private TextField admin_id;

    @FXML
    private Button about;

    @FXML
    private Button feedback;


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
                    AppState.getInstance().setAdminId(admin_id.getText());
                    // Initialize stage
                    Stage sign_in_stage = (Stage) loginBtn.getScene().getWindow();
                    // Show the loading screen
                    Handle_Transitions transitions = new Handle_Transitions();//create object of the class Handle_Transitions
                    transitions.showLoadingScreen(sign_in_stage);

                    // Load the actual scene with data
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHomeDashboard.fxml"));
                    Parent root = loader.load();
                    AdminHomeDashboardController controller = loader.getController();
                    controller.setAdminId(admin_id.getText());
                    Scene scene = new Scene(root);

                    // Switch to the actual scene after the loading screen
                    transitions.switchSceneWithLoading(scene, sign_in_stage);

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

    @FXML
    void goToAbout(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("About_Page.fxml"));
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
}
