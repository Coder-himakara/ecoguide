package com.ecosupport.ecoguide;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class LoadingScreenController {

    @FXML
    private ProgressBar loadingBar;

    @FXML
    public void initialize() {
        animateLoadingBar();
    }

    private void animateLoadingBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> loadingBar.setProgress(0)),
                new KeyFrame(Duration.seconds(1), e -> loadingBar.setProgress(0.25)),
                new KeyFrame(Duration.seconds(2), e -> loadingBar.setProgress(0.50)),
                new KeyFrame(Duration.seconds(3), e -> loadingBar.setProgress(0.75)),
                new KeyFrame(Duration.seconds(4), e -> loadingBar.setProgress(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}