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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
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
    private String[] conversation = {"Endangered", "Critically Endangered", "Vulnerable", "Extinct", "Near threatened", "Not Evaluated", "None"};

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

    private double pointerX;
    private double pointerY;
    private double offsetX;
    private double offsetY;
    private File selectedFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con_status.getItems().addAll(conversation);
        mapImageView.setOnMouseClicked(this::handleMapClick);
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
                animal_photo.setImage(image);
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

    @FXML
    void handleMapClick(MouseEvent event) {
        pointerX = event.getX();
        pointerY = event.getY();
        offsetX = pointerImageView.getFitWidth() / 2; // Adjust based on pointer image width
        offsetY = pointerImageView.getFitHeight() / 2; // Adjust based on pointer image height

        pointerImageView.setLayoutX(pointerX - offsetX);
        pointerImageView.setLayoutY(pointerY - offsetY);
        pointerImageView.setVisible(true);
        //System.out.println("Mouse clicked");
    }

    @FXML
    void handlePointerDragged(MouseEvent event) {
        // Calculate the new position of the pointer image based on the mouse drag position and offset
        double newX = event.getX() - offsetX;
        double newY = event.getY() - offsetY;

        // Ensure that the pointer image stays within the bounds of the background pane
        if (newX >= 0 && newX <= mapImageView.getWidth() - pointerImageView.getFitWidth()) {
            pointerImageView.setLayoutX(newX);
        }
        if (newY >= 0 && newY <= mapImageView.getHeight() - pointerImageView.getFitHeight()) {
            pointerImageView.setLayoutY(newY);
        }
    }

    @FXML
    void handlePointerPressed(MouseEvent event) {
        offsetX = event.getX() - pointerImageView.getLayoutX();
        offsetY = event.getY() - pointerImageView.getLayoutY();
    }

    @FXML
    void handlePointerReleased(MouseEvent event) {

    }

    private Point2D getPointerFinalCoordinates() {
        double x = pointerImageView.getLayoutX() + pointerImageView.getFitWidth() / 2;
        double y = pointerImageView.getLayoutY() + pointerImageView.getFitHeight() / 2;
        return new Point2D(x, y);
    }

    // You can call this method to get the final coordinates of the pointer
    private void getFinalPointerCoordinates() {
        Point2D finalCoordinates = getPointerFinalCoordinates();

        // Now you have the final coordinates of the pointer
        System.out.println("Pointer final coordinates: " + finalCoordinates);
    }

    @FXML
    void reset_all(ActionEvent event) {

    }



    @FXML
    void update_animal_data(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to update the data?");

        AtomicReference<String> active_selected = new AtomicReference<>("");
        active_time.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) -> {
            RadioButton selectedRadioButton = (RadioButton) t1.getToggleGroup().getSelectedToggle();
            String selectedText = selectedRadioButton.getText();
            active_selected.set(selectedText);
            //System.out.println("Selected Radio Button: " + selectedText);
        });

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
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("OK pressed");
            } else if (res == ButtonType.CANCEL) {
                System.out.println("Canceled");
            }
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
                summary.setText(intro);
                String[] activePatterns = {"Active During Day", "Active at Night","Active During Dawn and Dusk"};
                RadioButton[] patterns = {pattern_1, pattern_2, pattern_3};

                for (int i = 0; i < activePatterns.length; i++) {
                    if (active.equals(activePatterns[i])) {
                        active_time.selectToggle(patterns[i]);
                        return;
                    }
                }
                active_time.selectToggle(patterns[2]);

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
                animal_photo.setImage(image);
            } else {
                System.out.println("No image found in the database.");
            }


        } catch (SQLException e) {
            System.out.println(e);
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
}
