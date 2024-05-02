package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class Signup_PageController implements Initializable {
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
        } else {
            String query = "INSERT INTO `new_admin`(`admin_id`,`first_name`,`last_name`,`email`,`username`,`password`)" + "VALUES(?,?,?,?,?,?)";
            try {

                statement = DbConfig.getConnection().prepareStatement(query);
                statement.setString(1, ID);
                statement.setString(2, FirstName);
                statement.setString(3, LastName);
                statement.setString(4, Email);
                statement.setString(5, Username);
                statement.setString(6, Password);

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
                    alert.setContentText("Some thing went wrong");
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

    public int check_approve(String id) {
        PreparedStatement statement;
        int flag = 0;
        // SQL query to check if admin id exists in the admin_pid column in approved_admin table
        String query = "SELECT COUNT(*) AS count FROM approved_admin WHERE admin_pid = ?";
        try {
            statement = DbConfig.getConnection().prepareStatement(query);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            if (count > 0)
                flag = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag == 0)
            return 1;
        else
            return 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
