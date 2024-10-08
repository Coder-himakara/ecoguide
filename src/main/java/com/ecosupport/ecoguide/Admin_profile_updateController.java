package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
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
    private TextField show_pswd_text;

    @FXML
    private Button update_pswrd_btn;

    @FXML
    private ToggleButton show_pswd_btn;


    @FXML
    private Label admin_id;

    @FXML
    private Label f_name;

    @FXML
    private Label l_name;
    @FXML
    private Button add_pic_btn;
    private String adminId;
    @FXML
    private ImageView profile_pic;

    private File selectedFile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        show_pswd_text.setVisible(false);
        show_pswd_text.setManaged(false);
        show_pswd_text.textProperty().bindBidirectional(current_pass.textProperty());

    }


    @FXML
    void show_current_password(ActionEvent event) {
        if (show_pswd_text.isVisible()) {
            // Hide the text field and show the password field
            show_pswd_text.setManaged(false);
            show_pswd_text.setVisible(false);
            current_pass.setManaged(true);
            current_pass.setVisible(true);
            show_pswd_btn.setText("Show Password");
        } else {
            // Show the text field and hide the password field
            show_pswd_text.setManaged(true);
            show_pswd_text.setVisible(true);
            current_pass.setManaged(false);
            current_pass.setVisible(false);
            show_pswd_btn.setText("Hide Password");
        }
    }


    @FXML
    void password_update(ActionEvent event) {
        if (!new_pass.getText().equals(retype_pass.getText())) {
            // Show an error message if the passwords do not match
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Password Mismatch");
            alert.setContentText("New password and retype password do not match.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to update the password?");

        String sql = "UPDATE `new_admin` SET password = ? WHERE id_no = ?";

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res == ButtonType.OK) {
                try (Connection conn = DbConfig.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, new_pass.getText());
                    pstmt.setString(2, adminId);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("OK pressed");
                setAdmin_data(adminId);
                new_pass.setText(null);
                retype_pass.setText(null);

            } else if (res == ButtonType.CANCEL) {
                System.out.println("Canceled");
            }
        });
    }

    @FXML
    void reset_original(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Reset Confirmation");
        alert.setContentText("Are you sure you want to reset all fields?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            setAdmin_data(adminId);
        }
    }

    @FXML
    void update_data(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to update the data?");

        String sql;
        if (selectedFile != null) {
            sql = "UPDATE `new_admin` SET email = ? , username = ? , phone_no = ? , job_role = ?, img_data = ? "
                    + "WHERE id_no = ?";
        } else {
            sql = "UPDATE `new_admin` SET email = ? , username = ? , phone_no = ? , job_role = ? "
                    + "WHERE id_no = ?";
        }

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res == ButtonType.OK) {
                try (Connection conn = DbConfig.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, email.getText());
                    pstmt.setString(2, user_name.getText());
                    pstmt.setString(3, phone_no.getText());
                    pstmt.setString(4, job_role.getText());
                    if (selectedFile != null) {
                        FileInputStream fis = new FileInputStream(selectedFile);
                        pstmt.setBinaryStream(5, fis, (int) selectedFile.length());
                        pstmt.setString(6, adminId);
                    } else {
                        pstmt.setString(5, adminId);
                    }
                    pstmt.executeUpdate();
                } catch (SQLException | FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("OK pressed");
                setAdmin_data(adminId);
            } else if (res == ButtonType.CANCEL) {
                System.out.println("Canceled");
            }
        });
    }

    @FXML
    private void return_dashboard(ActionEvent event) throws IOException {
        // Initialize stage
        Stage sign_in_stage = (Stage) back_btn.getScene().getWindow();
        // Show the loading screen
        Handle_Transitions transitions = new Handle_Transitions();//create object of the class Handle_Transitions
        transitions.showLoadingScreen(sign_in_stage);

        // Load the actual scene with data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHomeDashboard.fxml"));
        Parent root = loader.load();
        AdminHomeDashboardController controller = loader.getController();
        Scene scene = new Scene(root);

        // Switch to the actual scene after the loading screen
        transitions.switchSceneWithLoading(scene, sign_in_stage);
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
        setAdmin_data(adminId);
        try {
            retrieveImage(adminId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @FXML
    void chooseImage(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                profile_pic.setImage(image);
                // You can now save this image to the database
                // For demonstration purposes, let's assume you have a method saveImageToDatabase()
                //saveImageToDatabase(selectedFile);
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Insert an Image");
        }
    }


    //Get the admin's image from the database
    private void retrieveImage(String adminId) throws IOException {
        try (Connection connection = DbConfig.getConnection()) {
            String selectQuery = "SELECT img_data FROM `new_admin` WHERE id_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, adminId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Get the image data from the database
                Blob blob = resultSet.getBlob("img_data");
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());

                // Create an Image object from the byte array
                Image image = new Image(new ByteArrayInputStream(imageBytes));

                // Display the image in the ImageView
                profile_pic.setImage(image);
            } else {
                System.out.println("No image found in the database.");
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
