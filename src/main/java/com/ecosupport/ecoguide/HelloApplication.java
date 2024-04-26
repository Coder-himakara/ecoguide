package com.ecosupport.ecoguide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminHomeDashboard.fxml")));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/com/ecosupport/styless/AdminHomeDashboard.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}