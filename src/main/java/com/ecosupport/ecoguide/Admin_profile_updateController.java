package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class Admin_profile_updateController implements Initializable {

    @FXML
    private Button back_btn;

    @FXML
    private TextField email;

    @FXML
    private TextField phone_no;

    @FXML
    private TextField job_role;

    @FXML
    private TextField user_name;

    @FXML
    private Button reset_btn;

    @FXML
    private Button update_btn;

    @FXML
    private PasswordField new_pass;

    @FXML
    private PasswordField retype_pass;

    @FXML
    private PasswordField current_pass;

    @FXML
    private Button update_pswrd_btn;

    @FXML
    private Label admin_id;

    @FXML
    private Label f_name;

    @FXML
    private Label l_name;
    private String adminId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    void password_update(ActionEvent event) {

    }

    @FXML
    void reset_original(ActionEvent event) {

    }

    @FXML
    void update_date(ActionEvent event) {

    }

    @FXML
    private void return_dashboard(ActionEvent event) throws IOException {
        Stage sign_in_stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AdminHomeDashboard.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add("/styles/admin_animal_add.css");
        sign_in_stage.setScene(scene);
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
        sign_in_stage.show();
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
        setAdmin_data(adminId);
    }

    public void setAdmin_data(String adminId) {
        //get admin data from database
        //set the data to the labels
        PreparedStatement statement;
        try {
            statement = DbConfig.getConnection().prepareStatement("SELECT * FROM new_admin WHERE id_no = ?");
            statement.setString(1, adminId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("id_no");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String email_txt = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String phone_number = resultSet.getString("phone_no");
                String job_role_txt = resultSet.getString("job_role");


                // Update labels with retrieved data
                admin_id.setText(id);
                f_name.setText(first_name);
                l_name.setText(last_name);
                email.setText(email_txt);
                user_name.setText(username);
                phone_no.setText(phone_number);
                job_role.setText(job_role_txt);
                current_pass.setText(password);


            } else {
                // Handle case where no data is found
                admin_id.setText("User not found");
            }

        } catch (Exception e) {
            System.out.println(e);
        }


    }
}
