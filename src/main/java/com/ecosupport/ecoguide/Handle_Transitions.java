package com.ecosupport.ecoguide;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Handle_Transitions {


    public void showLoadingScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadingScreen.fxml"));
            Parent root = loader.load();
            Scene loadingScene = new Scene(root);
            stage.setScene(loadingScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchSceneWithLoading(String fxmlFile, Stage primaryStage) {

        if (fxmlFile == null) {
            System.out.println("fxmlFile is null");
            return;
        }
        if (primaryStage == null) {
            System.out.println("primaryStage is null");
            return;
        }
        showLoadingScreen(primaryStage);

        // Get the controller of the loading screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadingScreen.fxml"));
        try {
            Parent loadingRoot = loader.load();
            LoadingScreenController loadingController = loader.getController();

            Scene loadingScene = new Scene(loadingRoot);
            primaryStage.setScene(loadingScene);

            // Simulate loading
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Simulate loading time
                    FXMLLoader newLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent newRoot = newLoader.load();
                    Platform.runLater(() -> {
                        primaryStage.setScene(new Scene(newRoot));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




