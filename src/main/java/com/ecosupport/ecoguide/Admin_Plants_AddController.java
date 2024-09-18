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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class Admin_Plants_AddController extends Plant_super_controller implements Initializable {
    @FXML
    private TextField p_name;
    @FXML
    private TextField p_science_name;
    @FXML
    private TextField population;
    @FXML
    private TextArea intro;
    @FXML
    private Label error_label;
    @FXML
    private ImageView plant_photo;
    @FXML
    private Button add_img;
    @FXML
    private Button reset_btn;
    @FXML
    private Button submit_btn;
    @FXML
    private Button back_btn;
    int primaryKeyValue = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialized.");
        // TODO
        con_status.getItems().addAll(conversation);
        con_status.setValue("None");
        hab_status.getItems().addAll(habitate);
        hab_status.setValue("None");
        exp_status.getItems().addAll(expansion);
        exp_status.setValue("None");
        root_status.getItems().addAll(root_system);
        root_status.setValue("None");
        pointerImageView.setVisible(false);
        mapImageView.setOnMouseClicked(this::handleMapClick);
    }

    @FXML
    private void add_plant(ActionEvent event) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        Image p_image = plant_photo.getImage();

        String name = p_name.getText();
        String science_name = p_science_name.getText();
        String status = con_status.getValue();
        String pop = population.getText();
        String hab = hab_status.getValue();
        String expan = exp_status.getValue();
        String root = root_status.getValue();
        String summary = intro.getText();
        boolean isVisible = pointerImageView.isVisible();

        int fields = fields_empty(name, science_name, status, pop, hab, expan, root);
        if (fields == 1) {  //Check whether all the data has been added
            error_label.setText("Mandotary Fields are Empty");

        }
        else if (p_image == null) {   //Check whether an image is added
            error_label.setText("Add an image");
        }
        else if (isVisible == false) {
            error_label.setText("Add a location on the map");
        }
        else {
            String query = "INSERT INTO `plants`(`name`,`scientific_name`,`status`,`population`,`habitate`,`expansion`,`root_system`,`intro`,`x_position`,`y_position`)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?)";
            int pop_size = Integer.parseInt(pop);

            Point2D finalCoordinates = getPointerFinalCoordinates(); // Assuming this method gets pointer coordinates

            try {
                Connection connection = DbConfig.getConnection(); // Assuming DbConfig.getConnection() returns a valid database connection
                statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, name);
                statement.setString(2, science_name);
                statement.setString(3, status);
                statement.setInt(4, pop_size);
                statement.setString(5, hab);
                statement.setString(6, expan);
                statement.setString(7, root);
                statement.setString(8, summary);
                statement.setDouble(9, finalCoordinates.getX());
                statement.setDouble(10, finalCoordinates.getY());

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected != 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        primaryKeyValue = generatedKeys.getInt(1);
                        saveImageToDatabase(selectedFile);// Assuming the primary key is an integer
                        error_label.setText("Done");

                        // Reset form fields
                        p_name.setText("");
                        p_science_name.setText("");
                        con_status.setValue(null);
                        population.setText("");
                        hab_status.setValue(null);
                        exp_status.setValue(null);
                        root_status.setValue(null);
                        intro.setText("");
                        plant_photo.setImage(null);
                        pointerImageView.setVisible(false);

                        // Now you have the primary key (primaryKey) for the inserted row
                    } else {
                        error_label.setText("Failed to retrieve primary key");
                    }
                } else {
                    error_label.setText("Something went wrong");
                    p_name.setText("");
                    p_science_name.setText("");
                    con_status.setValue(null);
                    population.setText("");
                    hab_status.setValue(null);
                    exp_status.setValue(null);
                    root_status.setValue(null);
                    intro.setText("");
                    plant_photo.setImage(null);
                    pointerImageView.setVisible(false);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    public int fields_empty(String pla_name, String pla_sc_name, String pla_status, String pla_pop, String pla_hab, String pla_expan, String pla_root) {
        if (pla_name.equals("") || pla_sc_name.equals("") || pla_status.equals("None")
                || pla_pop.equals("") || pla_hab.equals("") || pla_expan.equals("") || pla_root.equals("")) {
            return 1;
        } else {
            return 0;
        }
    }

    private void saveImageToDatabase(File imageFile) {
        // Check if the ImageView is empty
        if (plant_photo.getImage() == null) {
            System.err.println("Insert an image");
            return; // Stop execution if the ImageView is empty
        }

        try (Connection connection = DbConfig.getConnection()) {
            String name = p_name.getText();
            String insertQuery = "INSERT INTO `plant_images` (`image_data`,`plant_name`,`plant_fid`) VALUES (?,?,?)";
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
                p_name.setText("");
                p_science_name.setText("");
                con_status.setValue(null);
                population.setText("");
                hab_status.setValue(null);
                exp_status.setValue(null);
                root_status.setValue(null);
                intro.setText("");
                plant_photo.setImage(null);
                pointerImageView.setVisible(false);
                System.out.println("OK pressed");
            } else if (res == ButtonType.CANCEL) {
                System.out.println("Canceled");
            }
        });
    }

    @FXML
    private void return_dashboard(ActionEvent event) throws IOException {
        Stage sign_in_stage = (Stage) back_btn.getScene().getWindow();
        // Show the loading screen
        showLoadingScreen(sign_in_stage);
        // Load the actual scene with data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHomeDashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // Switch to the actual scene after the loading screen
        sign_in_stage.setScene(scene);
        sign_in_stage.setResizable(false);
        sign_in_stage.show();
    }
    private void showLoadingScreen(Stage stage) throws IOException {
        Parent loadingRoot = FXMLLoader.load(getClass().getResource("LoadingScreen.fxml"));
        Scene loadingScene = new Scene(loadingRoot);
        stage.setScene(loadingScene);
        stage.show();
    }
}
