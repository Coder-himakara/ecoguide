package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Admin_Animal_UpdateController implements Initializable {

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
    private ChoiceBox<String> con_status;

    @FXML
    private Label error_label;

    @FXML
    private ImageView animal_photo;

    @FXML
    private Button update_img;

    @FXML
    private Button reset_btn;

    @FXML
    private AnchorPane mapImageView;

    @FXML
    private ImageView pointerImageView;

    @FXML
    private Button update_btn;

    @FXML
    private Button back_btn;

    int selected_id = 0;
    @FXML
    void chooseImage(ActionEvent event) {

    }

    @FXML
    void handleMapClick(MouseEvent event) {

    }

    @FXML
    void handlePointerDragged(MouseEvent event) {

    }

    @FXML
    void handlePointerPressed(MouseEvent event) {

    }

    @FXML
    void handlePointerReleased(MouseEvent event) {

    }

    @FXML
    void reset_all(ActionEvent event) {

    }

    @FXML
    void return_dashboard(ActionEvent event) {

    }

    @FXML
    void update_animal_data(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setSelectedAttribute(int selectedId) {

    }
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

                String pop = Integer.toString(population);
                // Update labels with retrieved data
                a_name.setText(name);
                a_science_name.setText(scientific_name);
                con_status.setValue(status);
                count.setText(pop);
                foodEats.setText(diet);
                //animal_active.setText(active);
                summary.setText(intro);

                pointerImageView.setLayoutX(x_position - offsetX);
                pointerImageView.setLayoutY(y_position - offsetY);
            } else {
                // Handle case where no data is found
                error_label.setText("Animal not found");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void retrieveImage(int animal_id) throws IOException {
        try (Connection connection = DbConfig.getConnection()) {
            String selectQuery = "SELECT image_data FROM `animal_images` WHERE animal_pid = ?";
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
                //animal_image.setImage(image);
            } else {
                System.out.println("No image found in the database.");
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
