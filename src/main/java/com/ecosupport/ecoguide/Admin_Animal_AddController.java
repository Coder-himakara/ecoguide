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
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Admin_Animal_AddController implements Initializable {
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
    private ChoiceBox<String> con_status;
    private String[] conversation = {"Endangered", "Critically Endangered", "Vulnerable", "Extinct", "Near threatened", "Not Evaluated", "None"};
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

    private File selectedFile;
    @FXML
    private AnchorPane mapImageView;
    @FXML
    private ImageView pointerImageView;

    private double pointerX;
    private double pointerY;
    private double offsetX;
    private double offsetY;
    @FXML
    private Button reset_btn;
    @FXML
    private Button back_btn;

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
        //ResultSet queryResult;
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
            saveImageToDatabase(selectedFile);
            Point2D finalCoordinates = getPointerFinalCoordinates();//map pointer coordinates
            try {

                statement = DbConfig.getConnection().prepareStatement(query);
                statement.setString(1, name);
                statement.setString(2, science_name);
                statement.setString(3, status);
                statement.setInt(4, pop_size);
                statement.setString(5, food);
                statement.setString(6, active_selected);
                statement.setString(7, summary);
                statement.setDouble(8, finalCoordinates.getX());
                statement.setDouble(9, finalCoordinates.getY());

                if (statement.executeUpdate() != 0) {
                    error_label.setText("Done");
                    a_name.setText("");
                    a_science_name.setText("");
                    con_status.setValue(null);
                    population.setText("");
                    diet.setText("");
                    active_time.getUserData();
                    intro.setText("");
                    animal_photo.setImage(null);
                    pointerImageView.setVisible(false);
                } else {
                    error_label.setText("Somthing Went Wrong");
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

    //below method is to trigger the event to chose an image of the animal from file explorer
    @FXML
    private void chooseImage(ActionEvent event) {
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

    //below method is to store the added animal image in the database
    private void saveImageToDatabase(File imageFile) {
        // Check if the ImageView is empty
        if (animal_photo.getImage() == null) {
            System.err.println("Insert an image");
            return; // Stop execution if the ImageView is empty
        }

        try (Connection connection = DbConfig.getConnection()) {
            String name = a_name.getText();
            String insertQuery = "INSERT INTO `animal_images` (`image_data`,`animal_name`) VALUES (?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                FileInputStream fis = new FileInputStream(imageFile);
                preparedStatement.setBinaryStream(1, fis, (int) imageFile.length());
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePointerReleased(javafx.scene.input.MouseEvent event) {
    }

    //Map pointer Dragging Functionality
    @FXML
    private void handlePointerDragged(javafx.scene.input.MouseEvent event) {
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

    //Map pointer clicking functionality
    @FXML
    private void handleMapClick(javafx.scene.input.MouseEvent event) {
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
    private void handlePointerPressed(javafx.scene.input.MouseEvent event) {
        offsetX = event.getX() - pointerImageView.getLayoutX();
        offsetY = event.getY() - pointerImageView.getLayoutY();
    }

    // Method to get the final coordinates of the pointer image
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
        scene.getStylesheets().add("/com/ecosupport/styles/admin_animal_add.css");
        sign_in_stage.setScene(scene);
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.close();
        sign_in_stage.show();
    }


}
