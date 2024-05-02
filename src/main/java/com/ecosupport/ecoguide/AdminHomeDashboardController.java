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

public class AdminHomeDashboardController implements Initializable {

    ObservableList<Modeltable_animals> oblist;
    ObservableList<Modeltable_animals> datalist;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int selected_id = 0;
    @FXML
    private Button add_animal_btn;

    @FXML
    private Button update_btn;

    @FXML
    private Button profile_update_btn;

    @FXML
    private Button logout_btn;
    @FXML
    private TableView<Modeltable_animals> animal_table;
    @FXML
    private TableColumn<Modeltable_animals, Integer> col_animal_id;
    @FXML
    private TableColumn<Modeltable_animals, String> col_animal_name;
    @FXML
    private TableColumn<Modeltable_animals, String> col_animal_sname;
    @FXML
    private Button animal_view_btn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button LogoutBtn;
    @FXML
    private TableColumn<?, ?> col_plant_id;

    //private TableColumn<?, ?> col_plant_id;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        updateTabel();
    }

    public void updateTabel() {

        col_animal_id.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        col_animal_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_animal_sname.setCellValueFactory(new PropertyValueFactory<>("scientific_name"));

        try {
            oblist = DbConfig.getDatausers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        animal_table.setItems(oblist);

    }

    @FXML
    private void add_new_animal(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Admin_Animal_Add.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/admin_animal_add.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) add_animal_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void admin_profile_update(ActionEvent event) throws IOException {
        Stage sign_in_stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Admin_profile_update.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/admin_animal_add.css");
        sign_in_stage.setScene(scene);
        Stage stage = (Stage) profile_update_btn.getScene().getWindow();
        stage.close();
        sign_in_stage.show();
    }


    private void storeSelected(IntegerProperty attribute) {
        // Do something with the attribute
        selected_id = attribute.get();
        //System.out.println("Name is " + selected_id);
    }

    @FXML
    private void view_animal(ActionEvent event) throws IOException {
        if (selected_id != 0) {
            Stage sign_in_stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Animal_user_view.fxml"));
            Parent root = loader.load();

            // Get the controller of the Animal_user_view.fxml
            Animal_user_viewController controller = loader.getController();

            // Pass the selected variable to the controller
            controller.setSelectedAttribute(selected_id);

            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/animal_user_view.css");

            sign_in_stage.setScene(scene);
            Stage stage = (Stage) animal_view_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setHeaderText("No Selection");
            alert.setContentText("Select an animal to view");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(res -> {
                if (res == ButtonType.OK) {

                    System.out.println("OK pressed");
                }
            });
        }

    }

    public void select_animal(javafx.scene.input.MouseEvent mouseEvent) {
        Modeltable_animals selectedObject = animal_table.getSelectionModel().getSelectedItem();
        if (selectedObject != null) {
            // Retrieve the desired attribute from the selected object
            IntegerProperty attribute = selectedObject.idProperty();

            // Call another method with the attribute
            storeSelected(attribute);
        }
    }

    @FXML
    void backToHome(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("HomepageWithMenu.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) homeBtn.getScene().getWindow();

            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    void logout(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) LogoutBtn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @FXML
    void goto_updatePage(ActionEvent event) {
        if (selected_id != 0) {
            Stage sign_in_stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_animal_update.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the controller of the Admin_animal_update.fxml
            Admin_Animal_UpdateController controller = loader.getController();

            // Pass the selected variable to the controller
            controller.setSelectedAttribute(selected_id);

            Scene scene = new Scene(root);

            sign_in_stage.setScene(scene);
            Stage stage = (Stage) update_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setHeaderText("No selection");
            alert.setContentText("Select an animal to update the data?");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(res -> {
                if (res == ButtonType.OK) {

                    System.out.println("OK pressed");
                }
            });

        }
    }
}



