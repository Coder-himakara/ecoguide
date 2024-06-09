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

    public void switchSceneWithLoading(Scene newScene, Stage primaryStage) {
        if (newScene == null) {
            System.out.println("newScene is null");
            return;
        }
        if (primaryStage == null) {
            System.out.println("primaryStage is null");
            return;
        }

        // Simulate loading
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate loading time
                Platform.runLater(() -> {
                    primaryStage.setScene(newScene);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}




