package com.ecosupport.ecoguide;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class feedback_check_viewController implements Initializable {
    @FXML
    private TextField fb_name;
    @FXML
    private TextField fb_email;
    @FXML
    private TextArea fb;
    @FXML
    private Button readData;
    @FXML
    private Button menuBtn;
    @FXML
    private Button menuCloseBtn;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private Button back_btn;
    int selected_id = 0;

    @FXML
    void Back_to_feedbackTable(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AdminView_UserFeedback.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) back_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Signup_PageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSelectedAttribute(int selectedId) {
        this.selected_id = selectedId;
        setFeedbackData(selectedId);
    }

    public void setFeedbackData(int feedback_id) {
        PreparedStatement statement;
        String query = "SELECT * FROM `feedback` WHERE feedback_id = ?";
        try {
            statement = DbConfig.getConnection().prepareStatement(query);
            statement.setInt(1, feedback_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String feedback = resultSet.getString("feedback");

                fb_name.setText(name);
                fb_email.setText(email);
                fb.setText(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @FXML
    void mark_as_read(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to mark as read the feedback?");

        String sql = "UPDATE feedback SET read_or_not = ?" + "WHERE feedback_id = ?";
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res == ButtonType.OK) {
                try (Connection conn = DbConfig.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, "Read");
                    pstmt.setInt(2, selected_id);
                    pstmt.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("OK pressed");
            }
            else if(res == ButtonType.CANCEL){
                System.out.println("Canceled");
            }
        });
    }

    @FXML
    void menuClose(ActionEvent event) {
        sidebar.setVisible(false);
        menuCloseBtn.setVisible(false);
        menuBtn.setVisible(true);
    }

    @FXML
    void menuOpen(ActionEvent event) {
        sidebar.setVisible(true);
        menuCloseBtn.setVisible(true);
        menuBtn.setVisible(false);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
