package com.ecosupport.ecoguide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Animal_super_controller {

    @FXML
    ChoiceBox<String> con_status;
    String[] conversation = {"Endangered", "Critically Endangered", "Vulnerable", "Extinct", "Near threatened", "Not Evaluated", "None"};
    double pointerX;
    double pointerY;
    @FXML
    AnchorPane mapImageView;
    @FXML
    ImageView pointerImageView;
    File selectedFile;
    private double offsetX;
    private double offsetY;
    @FXML
    private ImageView animal_photo;


    //below method is to trigger the event to chose an image of the animal from file explorer
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

    //below methods are to handle the pointer on the map
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

    Point2D getPointerFinalCoordinates() {
        double x = pointerImageView.getLayoutX() + pointerImageView.getFitWidth() / 2;
        double y = pointerImageView.getLayoutY() + pointerImageView.getFitHeight() / 2;
        return new Point2D(x, y);
    }


}
