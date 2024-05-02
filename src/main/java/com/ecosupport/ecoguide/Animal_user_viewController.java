package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class Animal_user_viewController implements Initializable {

    @FXML
    private Label animal_name;
    @FXML
    private Label animal_sname;
    @FXML
    private Label animal_population;
    @FXML
    private Label animal_status;
    @FXML
    private Label animal_diet;
    @FXML
    private Label animal_active;
    @FXML
    private Label animal_inro;
    @FXML
    private Button back_btn;
    @FXML
    private AnchorPane mapImageView;
    @FXML
    private ImageView pointerImageView;

    @FXML
    private ImageView animal_image;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
    }

    @FXML
    private void back_to_dashboard(ActionEvent event) throws IOException {
        Stage sign_in_stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminHomeDashboard.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/AdminHomeDashboard.css");
        sign_in_stage.setScene(scene);
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
        sign_in_stage.show();
    }

    public void setSelectedAttribute(int attribute) {
        // Use the selected attribute in the interface
        setAnimalData(attribute);
        //String s = Integer.toString(animal_id_selected);
        //animal_name.setText(s);
        try {
            retrieveImage(attribute);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                animal_name.setText(name);
                animal_sname.setText(scientific_name);
                animal_status.setText(status);
                animal_population.setText(pop);
                animal_diet.setText(diet);
                animal_active.setText(active);
                animal_inro.setText(intro);

                pointerImageView.setLayoutX(x_position - offsetX);
                pointerImageView.setLayoutY(y_position - offsetY);
            } else {
                // Handle case where no data is found
                animal_name.setText("Animal not found");
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
                animal_image.setImage(image);
            } else {
                System.out.println("No image found in the database.");
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}