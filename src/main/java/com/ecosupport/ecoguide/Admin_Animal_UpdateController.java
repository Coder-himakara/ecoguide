package com.ecosupport.ecoguide;

import javafx.beans.value.ObservableValue;
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

public class Admin_Animal_UpdateController extends Animal_super_controller implements Initializable {

    @FXML
    private RadioButton pattern_1;

    @FXML
    private ToggleGroup active_time;

    @FXML
    private RadioButton pattern_3;

    @FXML
    private RadioButton pattern_2;

    @FXML
    private TextField a_name;

    @FXML
    private TextField a_science_name;

    @FXML
    private TextField count;

    @FXML
    private TextField foodEats;

    @FXML
    private TextArea summary;

    @FXML
    private Label error_label;

    @FXML
    private ImageView animal_photo;

    @FXML
    private Button update_img;

    @FXML
    private Button reset_btn;


    @FXML
    private Button update_btn;

    @FXML
    private Button back_btn;

    int selected_id = 0;
    private AtomicReference<String> active_selected = new AtomicReference<>("");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con_status.getItems().addAll(conversation);
        mapImageView.setOnMouseClicked(this::handleMapClick);

        active_time.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) -> {
            RadioButton selectedRadioButton = (RadioButton) t1.getToggleGroup().getSelectedToggle();
            String selectedText = selectedRadioButton.getText();
            active_selected.set(selectedText);
        });
    }


    public void setSelectedAttribute(int selectedId) {
        this.selected_id = selectedId;
        setAnimalData(selectedId);
        try {
            retrieveImage(selectedId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //Method to set the animal's data in the text fields and radio buttons
    //after the animal is selected from the database
    public void setAnimalData(int animal_id) {
        PreparedStatement statement;
        double offsetX = pointerImageView.getFitWidth() / 2; // Adjust based on pointer image width
        double offsetY = pointerImageView.getFitHeight() / 2; // Adjust based on pointer image height
        String query = "SELECT * FROM `animals` WHERE animal_id = ?";
        try {
            statement = DbConfig.getConnection().prepareStatement(query);
            statement.setInt(1, animal_id);
            ResultSet resultSet = statement.executeQuery();
            // Process the result set
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String scientific_name = resultSet.getString("scientific_name");
                String status = resultSet.getString("status");
                int population = resultSet.getInt("population");
                String diet = resultSet.getString("diet");
                String active = resultSet.getString("active");
                String intro = resultSet.getString("intro");
                double x_position = resultSet.getDouble("x_position");
                double y_position = resultSet.getDouble("y_position");
                pointerX = x_position;
                pointerY = y_position;

                String pop = Integer.toString(population);
                // Update labels with retrieved data
                a_name.setText(name);
                a_science_name.setText(scientific_name);
                con_status.setValue(status);
                count.setText(pop);
                foodEats.setText(diet);
                summary.setText(intro);
                pointerImageView.setLayoutX(x_position - offsetX);
                pointerImageView.setLayoutY(y_position - offsetY);
                String[] activePatterns = {"Active During Day", "Active at Night", "Active During Dawn and Dusk"};
                RadioButton[] patterns = {pattern_1, pattern_2, pattern_3};

                for (int i = 0; i < activePatterns.length; i++) {
                    if (active.equals(activePatterns[i])) {
                        active_time.selectToggle(patterns[i]);
                        return;
                    }
                }
                active_time.selectToggle(patterns[2]);

            } else {
                // Handle case where no data is found
                error_label.setText("Animal not found");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //Get the animal's image from the database
    private void retrieveImage(int animal_id) throws IOException {
        try (Connection connection = DbConfig.getConnection()) {
            String selectQuery = "SELECT image_data FROM `animal_images` WHERE animal_fid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, animal_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Get the image data from the database
                Blob blob = resultSet.getBlob("image_data");
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());

                // Create an Image object from the byte array
                Image image = new Image(new ByteArrayInputStream(imageBytes));

                // Display the image in the ImageView
                animal_photo.setImage(image);
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

    //Update the animal's database table with the new data
    @FXML
    void update_animal_data(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to update the data?");

        String sql = "UPDATE animals SET name = ? , scientific_name = ? , status = ? , population = ? , " +
                "diet = ? , active = ? , intro = ? , x_position = ? , y_position = ?"
                + "WHERE animal_id = ?";

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res == ButtonType.OK) {

                try (Connection conn = DbConfig.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, a_name.getText());
                    pstmt.setString(2, a_science_name.getText());
                    pstmt.setString(3, con_status.getValue());
                    pstmt.setInt(4, Integer.parseInt(count.getText()));
                    pstmt.setString(5, foodEats.getText());
                    pstmt.setString(6, active_selected.get());
                    pstmt.setString(7, summary.getText());
                    pstmt.setDouble(8, pointerX);
                    pstmt.setDouble(9, pointerY);
                    pstmt.setInt(10, selected_id);

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

    //Method to update the animal's image in the database method
    private void updateImageInDatabase(File imageFile) {
        // Check if an image has been selected
        if (selectedFile == null) {
            System.err.println("No image selected.");
            return;
        }

        try (Connection connection = DbConfig.getConnection()) {
            String updateQuery = "UPDATE animal_images SET image_data = ? WHERE animal_pid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                FileInputStream fis = new FileInputStream(imageFile);
                preparedStatement.setBinaryStream(1, fis, (int) imageFile.length());
                preparedStatement.setInt(2, selected_id); // Assuming the image row has an ID of 1
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
        sign_in_stage.show();
    }

    @FXML
    void reset_all(ActionEvent event) {

    }
}
