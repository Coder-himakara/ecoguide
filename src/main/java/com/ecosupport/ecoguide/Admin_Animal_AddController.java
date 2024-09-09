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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class Admin_Animal_AddController extends Animal_super_controller implements Initializable {
    @FXML
    private ToggleGroup active_time;
    @FXML
    private RadioButton pattern_1;
    @FXML
    private RadioButton pattern_3;
    @FXML
    private RadioButton pattern_2;
    @FXML
    private TextField a_name;
    @FXML
    private TextField a_science_name;

    @FXML
    private TextField population;
    @FXML
    private TextField diet;
    @FXML
    private TextArea intro;
    @FXML
    private Button submit_btn;
    @FXML
    private Label error_label;

    private String active_selected = "";
    @FXML
    private ImageView animal_photo;
    @FXML
    private Button add_img;

    @FXML
    private Button reset_btn;
    @FXML
    private Button back_btn;
    int primaryKeyValue = -1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Controller initialized.");
        // TODO
        con_status.getItems().addAll(conversation);
        con_status.setValue("None");
        pointerImageView.setVisible(false);
        mapImageView.setOnMouseClicked(this::handleMapClick);
    }


    @FXML
    private void add_animal(ActionEvent event) {

        PreparedStatement statement;
        ResultSet resultSet = null;
        Image a_image = animal_photo.getImage();

        String name = a_name.getText();
        String science_name = a_science_name.getText();
        String status = con_status.getValue();
        String pop = population.getText();

        String food = diet.getText();
        Toggle active = active_time.getSelectedToggle();
        String summary = intro.getText();
        boolean isVisible = pointerImageView.isVisible();

        active_time.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) -> {
            RadioButton selectedRadioButton = (RadioButton) t1.getToggleGroup().getSelectedToggle();
            String selectedText = selectedRadioButton.getText();
            active_selected = selectedText;
            //System.out.println("Selected Radio Button: " + selectedText);
        });

        int fields = fields_empty(name, science_name, status, pop, food, active_selected);
        if (fields == 1) {  //Check whether all the data has been added
            error_label.setText("Mandotary Fields are Empty");

        } else if (a_image == null) {   //Check whether an image is added
            error_label.setText("Add an image");
        } else if (isVisible == false) {
            error_label.setText("Add a location on the map");
        } else {
            String query = "INSERT INTO `animals`(`name`,`scientific_name`,`status`,`population`,`diet`,`active`,`intro`,`x_position`,`y_position`)"
                    + "VALUES(?,?,?,?,?,?,?,?,?)";
            int pop_size = Integer.parseInt(pop);

            Point2D finalCoordinates = getPointerFinalCoordinates(); // Assuming this method gets pointer coordinates

            try {
                Connection connection = DbConfig.getConnection(); // Assuming DbConfig.getConnection() returns a valid database connection
                statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, name);
                statement.setString(2, science_name);
                statement.setString(3, status);
                statement.setInt(4, pop_size);
                statement.setString(5, food);
                statement.setString(6, active_selected);
                statement.setString(7, summary);
                statement.setDouble(8, finalCoordinates.getX());
                statement.setDouble(9, finalCoordinates.getY());

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected != 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        primaryKeyValue = generatedKeys.getInt(1);
                        saveImageToDatabase(selectedFile);// Assuming the primary key is an integer
                        error_label.setText("Done");

                        // Reset form fields
                        a_name.setText("");
                        a_science_name.setText("");
                        con_status.setValue(null);
                        population.setText("");
                        diet.setText("");
                        active_time.getUserData();
                        intro.setText("");
                        animal_photo.setImage(null);
                        pointerImageView.setVisible(false);

                        // Now you have the primary key (primaryKey) for the inserted row
                    } else {
                        error_label.setText("Failed to retrieve primary key");
                    }
                } else {
                    error_label.setText("Something went wrong");
                    a_name.setText("");
                    a_science_name.setText("");
                    con_status.setValue(null);
                    population.setText("");
                    diet.setText("");
                    active_time.getUserData();
                    intro.setText("");
                    animal_photo.setImage(null);
                    pointerImageView.setVisible(false);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    //below method is used to check whether the textfileds are null or not
    public int fields_empty(String ani_name, String ani_sc_name, String ani_status, String ani_pop, String ani_diet, String ani_active) {
        if (ani_name.equals("") || ani_sc_name.equals("") || ani_status.equals("None")
                || ani_pop.equals("") || ani_diet.equals("") || ani_active.equals("")) {
            return 1;
        } else {
            return 0;
        }
    }


    //below method is to store the added animal image in the database
    private void saveImageToDatabase(File imageFile) {
        // Check if the ImageView is empty
        if (animal_photo.getImage() == null) {
            System.err.println("Insert an image");
            return; // Stop execution if the ImageView is empty
        }

        try (Connection connection = DbConfig.getConnection()) {
            String name = a_name.getText();
            String insertQuery = "INSERT INTO `animal_images` (`image_data`,`animal_name`,`animal_fid`) VALUES (?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                FileInputStream fis = new FileInputStream(imageFile);
                preparedStatement.setBinaryStream(1, fis, (int) imageFile.length());
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, primaryKeyValue);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    // You can call this method to get the final coordinates of the pointer
    private void getFinalPointerCoordinates() {
        Point2D finalCoordinates = getPointerFinalCoordinates();
        // Now you have the final coordinates of the pointer
        System.out.println("Pointer final coordinates: " + finalCoordinates);
    }

    @FXML
    private void reset_all(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to reset all things?");

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res == ButtonType.OK) {
                a_name.setText("");
                a_science_name.setText("");
                con_status.setValue(null);
                population.setText("");
                diet.setText("");
                active_time.getUserData();
                intro.setText("");
                animal_photo.setImage(null);
                pointerImageView.setVisible(false);
                System.out.println("OK pressed");
            } else if (res == ButtonType.CANCEL) {
                System.out.println("Canceled");
            }
        });
    }

    @FXML
    private void return_dashboard(ActionEvent event) throws IOException {
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


}
