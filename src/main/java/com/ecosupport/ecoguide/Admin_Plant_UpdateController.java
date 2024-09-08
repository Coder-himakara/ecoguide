package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class Admin_Plant_UpdateController extends Plant_super_controller implements Initializable {
    @FXML
    private TextField p_name;
    @FXML
    private TextField p_science_name;
    @FXML
    private TextField count;
    @FXML
    private TextArea summary;
    @FXML
    private Label error_label;
    @FXML
    private ImageView plant_photo;
    @FXML
    private Button update_img;
    @FXML
    private Button reset_btn;
    @FXML
    private Button update_btn;
    @FXML
    private Button back_btn;
    int selected_id2 = 0;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con_status.getItems().addAll(conversation);
        hab_status.getItems().addAll(habitate);
        exp_status.getItems().addAll(expansion);
        root_status.getItems().addAll(root_system);
        mapImageView.setOnMouseClicked(this::handleMapClick);
    }

    public void setSelectedAttribute(int selectedId) {
        this.selected_id2 = selectedId;
        setPlantData(selectedId);
        try {
            retrieveImage(selectedId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Method to set the plant's data in the text fields
    //after the plant is selected from the database
    public void setPlantData(int plant_id) {
        PreparedStatement statement;
        double offsetX = pointerImageView.getFitWidth() / 2; // Adjust based on pointer image width
        double offsetY = pointerImageView.getFitHeight() / 2; // Adjust based on pointer image height
        String query = "SELECT * FROM `plants` WHERE plant_id = ?";
        try {
            statement = DbConfig.getConnection().prepareStatement(query);
            statement.setInt(1, plant_id);
            ResultSet resultSet = statement.executeQuery();
            // Process the result set
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String scientific_name = resultSet.getString("scientific_name");
                String status = resultSet.getString("status");
                int population = resultSet.getInt("population");
                String habitate = resultSet.getString("habitate");
                String expansion = resultSet.getString("expansion");
                String root_system = resultSet.getString("root_system");
                String intro = resultSet.getString("intro");
                double x_position = resultSet.getDouble("x_position");
                double y_position = resultSet.getDouble("y_position");
                pointerX = x_position;
                pointerY = y_position;

                String pop = Integer.toString(population);
                // Update labels with retrieved data
                p_name.setText(name);
                p_science_name.setText(scientific_name);
                con_status.setValue(status);
                count.setText(pop);
                hab_status.setValue(habitate);
                exp_status.setValue(expansion);
                root_status.setValue(root_system);
                summary.setText(intro);
                pointerImageView.setLayoutX(x_position - offsetX);
                pointerImageView.setLayoutY(y_position - offsetY);
            } else {
                // Handle case where no data is found
                error_label.setText("Plant not found");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //Get the plant's image from the database
    private void retrieveImage(int plant_id) throws IOException {
        try (Connection connection = DbConfig.getConnection()) {
            String selectQuery = "SELECT image_data FROM `plant_images` WHERE plant_pid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1,plant_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Get the image data from the database
                Blob blob = resultSet.getBlob("image_data");
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());

                // Create an Image object from the byte array
                Image image = new Image(new ByteArrayInputStream(imageBytes));

                // Display the image in the ImageView
                plant_photo.setImage(image);
            } else {
                System.out.println("No image found in the database.");
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // You can call this method to get the final coordinates of the pointer
    private void getFinalPointerCoordinates() {
        Point2D finalCoordinates = getPointerFinalCoordinates();
        // Now you have the final coordinates of the pointer
        System.out.println("Pointer final coordinates: " + finalCoordinates);
    }

    //Update the plant's database table with the new data
    @FXML
    void update_plant_data(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to update the data?");

        String sql = "UPDATE plants SET name = ? , scientific_name = ? , status = ? , population = ? , " +
                "habitate = ? , expansion = ? , root_system = ?, intro = ? , x_position = ? , y_position = ?"
                + "WHERE plant_id = ?";

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res == ButtonType.OK) {

                try (Connection conn = DbConfig.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, p_name.getText());
                    pstmt.setString(2, p_science_name.getText());
                    pstmt.setString(3, con_status.getValue());
                    pstmt.setInt(4, Integer.parseInt(count.getText()));
                    pstmt.setString(5, hab_status.getValue());
                    pstmt.setString(6, exp_status.getValue());
                    pstmt.setString(7, root_status.getValue());
                    pstmt.setString(8, summary.getText());
                    pstmt.setDouble(9, pointerX);
                    pstmt.setDouble(10, pointerY);
                    pstmt.setInt(11, selected_id2);

                    pstmt.executeUpdate();
                    updateImageInDatabase(selectedFile);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("OK pressed");
            } else if (res == ButtonType.CANCEL) {
                System.out.println("Canceled");
            }
        });

    }

    //Method to update the plant's image in the database method
    private void updateImageInDatabase(File imageFile) {
        // Check if an image has been selected
        if (selectedFile == null) {
            System.err.println("No image selected.");
            return;
        }

        try (Connection connection = DbConfig.getConnection()) {
            String updateQuery = "UPDATE plant_images SET image_data = ? WHERE plant_pid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                FileInputStream fis = new FileInputStream(imageFile);
                preparedStatement.setBinaryStream(1, fis, (int) imageFile.length());
                preparedStatement.setInt(2, selected_id2); // Assuming the image row has an ID of 1
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void return_dashboard(ActionEvent event) throws IOException {
        Stage sign_in_stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AdminHomeDashboard.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add("/com/ecosupport/styles/admin_animal_add.css");
        sign_in_stage.setScene(scene);
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
        sign_in_stage.setResizable(false);
        sign_in_stage.show();
    }

    @FXML
    void reset_all(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Reset Confirmation");
        alert.setContentText("Are you sure you want to reset all fields?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            setPlantData(selected_id2);
        }
    }
}
