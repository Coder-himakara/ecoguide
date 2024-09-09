package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Signup_PageController {
    @FXML
    private TextField adminId;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private TextField email;

    @FXML
    private TextField uName;

    @FXML
    private TextField password;

    @FXML
    private Button signupBtn;

    @FXML
    private Button admin_btn;

    @FXML
    private Button menuBtn;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private Button menuCloseBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private Button about;

    @FXML
    private Button feedback;


    // Add this method to the `Signup_PageController` class
    private boolean isAdminIdExists(String adminId) {
        PreparedStatement statement;
        ResultSet resultSet;
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM `new_admin` WHERE `id_no` = ?";
        try {
            statement = DbConfig.getConnection().prepareStatement(query);
            statement.setString(1, adminId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }
    // Update the `handleSignup` method in the `Signup_PageController` class
    @FXML
    private void handleSignup(ActionEvent event) {
        PreparedStatement statement;
        String ID = adminId.getText();
        String FirstName = fName.getText();
        String LastName = lName.getText();
        String Email = email.getText();
        String Username = uName.getText();
        String Password = password.getText();
        Alert alert;
        int fields = fields_empty(ID, FirstName, LastName, Email, Username, Password);
        int check = check_approve(ID);
        File imageFile = new File("src\\main\\resources\\com\\ecosupport\\ecoguide\\images\\profile_dp.png");

        if (fields == 1) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else if (check == 1) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Access Denied");
            alert.showAndWait();
        } else if (isAdminIdExists(ID)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Admin ID already exists");
            alert.showAndWait();
        } else {
            String query = "INSERT INTO `new_admin`(`id_no`,`first_name`,`last_name`,`email`,`username`,`password`,`phone_no`,`job_role`,`img_data`)" +
                    "VALUES(?,?,?,?,?,?,?,?,?)";
            try {
                statement = DbConfig.getConnection().prepareStatement(query);
                statement.setString(1, ID);
                statement.setString(2, FirstName);
                statement.setString(3, LastName);
                statement.setString(4, Email);
                statement.setString(5, Username);
                statement.setString(6, Password);
                statement.setString(7, null);
                statement.setString(8, null);
                FileInputStream fis = new FileInputStream(imageFile);
                statement.setBinaryStream(9, fis, (int) imageFile.length());

                if (statement.executeUpdate() != 0) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful..");
                    alert.showAndWait();
                    adminId.setText("");
                    fName.setText("");
                    lName.setText("");
                    email.setText("");
                    uName.setText("");
                    password.setText("");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong");
                    alert.showAndWait();
                    adminId.setText("");
                    fName.setText("");
                    lName.setText("");
                    email.setText("");
                    uName.setText("");
                    password.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //below method is used to check whether the textfileds are null or not
    public int fields_empty(String id, String first_name, String last_name, String email, String username, String password) {
        if (id.equals("") || first_name.equals("") || last_name.equals("")
                || email.equals("") || username.equals("") || password.equals("")) {
            return 1;
        } else {
            return 0;
        }
    }

    public int check_approve(String id){
        PreparedStatement statement;
        int flag = 0;
        // SQL query to check if admin id exists in the admin_pid column in approved_admin table
        String query = "SELECT COUNT(*) AS count FROM approved_admin WHERE admin_pid = ?";
        try{
            statement = DbConfig.getConnection().prepareStatement(query);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            if (count > 0)
                flag = 1;
        }catch(Exception e){
            e.printStackTrace();
        }
        if(flag == 0)
            return 1;
        else
            return 0;
    }

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
            sign_in_stage.setResizable(false);
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Signup_PageController.class.getName()).log(Level.SEVERE, null, ex);
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
            sign_in_stage.setResizable(false);
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Signup_PageController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        primary.setResizable(false);
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
        primary.setResizable(false);
        primary.show();
    }
}
