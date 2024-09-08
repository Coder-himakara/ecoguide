package com.ecosupport.ecoguide;

import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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

public class AdminView_UserFeedback implements Initializable
{
    ObservableList<Modeltable_feedback> oblist;
    ObservableList<Modeltable_feedback> datalist;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int selected_id = 0;
    @FXML
    private Button menuBtn;
    @FXML
    private Button menuCloseBtn;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private Button admin_btn;
    @FXML
    private Button view_btn;
    @FXML
    private TableView<Modeltable_feedback> feedback_table;
    @FXML
    private TableColumn<Modeltable_feedback, Integer> col_feedback_id;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_name;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_email;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_feedback;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_read_or_not;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
    }

    @FXML
    public void updateTable(){
        String str = "No";
        col_feedback_id.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_feedback.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        col_read_or_not.setCellValueFactory(new PropertyValueFactory<>("read_or_not"));
        try {
            oblist = DbConfig.getDataFeedback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            feedback_table.setItems(oblist);
    }

    @FXML
    void Back_to_dashboard(ActionEvent event) throws IOException {
        // Initialize stage
        Stage sign_in_stage = (Stage) admin_btn.getScene().getWindow();
        // Show the loading screen
        Handle_Transitions transitions = new Handle_Transitions();//create object of the class Handle_Transitions
        transitions.showLoadingScreen(sign_in_stage);

        // Load the actual scene with data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHomeDashboard.fxml"));
        Parent root = loader.load();
        AdminHomeDashboardController controller = loader.getController();
        Scene scene = new Scene(root);

        // Switch to the actual scene after the loading screen
        transitions.switchSceneWithLoading(scene, sign_in_stage);

    }

    private void storeSelected(IntegerProperty attribute) {
        // Do something with the attribute
        selected_id = attribute.get();
        //System.out.println("Name is " + selected_id);
    }

    public void select_feedback(javafx.scene.input.MouseEvent mouseEvent) {
        Modeltable_feedback selectedObject = feedback_table.getSelectionModel().getSelectedItem();
        if (selectedObject != null) {
            // Retrieve the desired attribute from the selected object
            IntegerProperty attribute1 = selectedObject.idProperty();

            // Call another method with the attribute
            storeSelected(attribute1);
        }
    }

    @FXML
    void view_feedback(ActionEvent event) throws IOException{
        if (selected_id != 0) {
            Stage sign_in_stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("feedback_check_view.fxml"));
            Parent root = loader.load();

            // Get the controller of the Animal_user_view.fxml
            feedback_check_viewController controller = loader.getController();

            // Pass the selected variable to the controller
            controller.setSelectedAttribute(selected_id);

            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/animal_user_view.css");

            sign_in_stage.setScene(scene);
            Stage stage = (Stage) view_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.setResizable(false);
            sign_in_stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setHeaderText("No Selection");
            alert.setContentText("Select an feedback to view");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(res -> {
                if (res == ButtonType.OK) {

                    System.out.println("OK pressed");
                }
            });
        }
    }

    @FXML
    void menuOpen(ActionEvent event) {
        sidebar.setVisible(true);
        menuCloseBtn.setVisible(true);
        menuBtn.setVisible(false);
    }

    @FXML
    void menuClose(ActionEvent event) {
        sidebar.setVisible(false);
        menuCloseBtn.setVisible(false);
        menuBtn.setVisible(true);
    }

}
