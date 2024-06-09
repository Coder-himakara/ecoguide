package com.ecosupport.ecoguide;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminHomeDashboardController implements Initializable {

    ObservableList<Modeltable_animals> oblist;
    ObservableList<Modeltable_animals> datalist;
    ObservableList<Modeltable_plants> oblist2;
    ObservableList<Modeltable_plants> datalist2;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int selected_id = 0;
    int selected_id2 = 0;
    @FXML
    private Button add_animal_btn;
    @FXML
    private Button update_btn;
    @FXML
    private Button update_btn_plant;
    @FXML
    private Button profile_update_btn;
    @FXML
    private Button logout_btn;
    @FXML
    private TableView<Modeltable_animals> animal_table;
    @FXML
    private TableView<Modeltable_plants> plant_table;
    @FXML
    private TableColumn<Modeltable_animals, Integer> col_animal_id;
    @FXML
    private TableColumn<Modeltable_animals, String> col_animal_name;
    @FXML
    private TableColumn<Modeltable_animals, String> col_animal_sname;
    @FXML
    private TableColumn<Modeltable_plants, Integer> col_plant_id;
    @FXML
    private TableColumn<Modeltable_plants, String> col_plant_name;
    @FXML
    private TableColumn<Modeltable_plants, String> col_plant_sname;
    @FXML
    private Button animal_view_btn;
    @FXML
    private Button plant_view_btn;
    @FXML
    private Button add_plant_btn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button LogoutBtn;
    @FXML
    private Button user_feedback;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label plantCountLabel;
    @FXML
    private Label feedbackCountLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ImageView profile_pic;
    @FXML
    private Label my_name;
    private String adminId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        adminId = AppState.getInstance().getAdminId();
        updateTabel();

        updateTabelPlants();

        retrieveAndSetProfileImage();
        // Create a Circle object
        double radius = 72.0; // adjust the radius as needed
        Circle clip = new Circle(radius, radius, radius);

        // Set the Circle as the clip of the ImageView
        profile_pic.setClip(clip);
        setAnimalCount();
        setPlantCount();
        setFeedbackCount();
        setCurrentTime();

        // Create a Timeline to update the time every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> setCurrentTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private void setCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        timeLabel.setText(currentTime.format(formatter));
    }


    public void setAdminId(String adminId) {
        this.adminId = adminId;
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

    public void updateTabelPlants() {

        col_plant_id.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        col_plant_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_plant_sname.setCellValueFactory(new PropertyValueFactory<>("scientific_name"));

        try {
            oblist2 = DbConfig.getDatausersPlants();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        plant_table.setItems(oblist2);

    }

    @FXML
    private void add_new_animal(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Admin_Animal_Add.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/admin_animal_add.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) add_animal_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Enter Admin Profile Update Page
    @FXML
    private void admin_profile_update(ActionEvent event) throws IOException {
        // Initialize stage
        Stage sign_in_stage = (Stage) profile_update_btn.getScene().getWindow();

        // Show the loading screen
        Handle_Transitions transitions = new Handle_Transitions();
        transitions.showLoadingScreen(sign_in_stage);

        // Load the actual scene with data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_profile_update.fxml"));
        Parent root = loader.load();
        Admin_profile_updateController controller = loader.getController();
        controller.setAdminId(this.adminId);
        Scene scene = new Scene(root);

        // Switch to the actual scene after the loading screen
        transitions.switchSceneWithLoading(scene, sign_in_stage);
    }


    private void storeSelected(IntegerProperty attribute) {
        // Do something with the attribute
        selected_id = attribute.get();
        selected_id2 = attribute.get();
        //System.out.println("Name is " + selected_id);
    }

    //View Selected Animal Details
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
            //scene.getStylesheets().add("/styles/animal_user_view.css");

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

    //View Selected Plant Details
    @FXML
    private void view_plant(ActionEvent event) throws IOException {
        if (selected_id2 != 0) {
            Stage sign_in_stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Plant_user_view.fxml"));
            Parent root = loader.load();

            // Get the controller of the Plant_user_view.fxml
            Plant_user_viewController controller = loader.getController();

            // Pass the selected variable to the controller
            controller.setSelectedAttribute(selected_id2);

            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/plant_user_view.css");

            sign_in_stage.setScene(scene);
            Stage stage = (Stage) plant_view_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setHeaderText("No Selection");
            alert.setContentText("Select an plant to view");
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
            IntegerProperty attribute1 = selectedObject.idProperty();

            // Call another method with the attribute
            storeSelected(attribute1);
        }
    }

    public void select_plant(javafx.scene.input.MouseEvent mouseEvent) {
        Modeltable_plants selectedObject = plant_table.getSelectionModel().getSelectedItem();
        if (selectedObject != null) {
            // Retrieve the desired attribute from the selected object
            IntegerProperty attribute2 = selectedObject.idProperty();

            // Call another method with the attribute
            storeSelected(attribute2);
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
    void goto_user_feedback(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AdminView_UserFeedback.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) user_feedback.getScene().getWindow();

            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Are you want to logout?");
            if(alert.showAndWait().get() == ButtonType.OK){
                Stage sign_in_stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
                Scene scene = new Scene(root);
                //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
                sign_in_stage.setScene(scene);
                Stage stage = (Stage) LogoutBtn.getScene().getWindow();
                stage.close();
                sign_in_stage.show();
            }
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


    @FXML
    void goto_updatePage_Plant(ActionEvent event) {
        if (selected_id2 != 0) {
            Stage sign_in_stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_plant_update.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the controller of the Admin_animal_update.fxml
            Admin_Plant_UpdateController controller = loader.getController();

            // Pass the selected variable to the controller
            controller.setSelectedAttribute(selected_id2);

            Scene scene = new Scene(root);

            sign_in_stage.setScene(scene);
            Stage stage = (Stage) update_btn_plant.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setHeaderText("No selection");
            alert.setContentText("Select an plant to update the data?");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(res -> {
                if (res == ButtonType.OK) {

                    System.out.println("OK pressed");
                }
            });

        }
    }


    @FXML
    private void add_new_plant(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Admin_Plants_Add.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/admin_animal_add.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) add_plant_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void retrieveAndSetProfileImage() {
        try (Connection connection = DbConfig.getConnection()) {
            String selectQuery = "SELECT `img_data`,`last_name` FROM `new_admin` WHERE id_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, adminId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Get the image data from the database
                String last_name = resultSet.getString("last_name");
                Blob blob = resultSet.getBlob("img_data");
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());

                // Create an Image object from the byte array
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                profile_pic.setImage(image);
                my_name.setText(last_name);

            } else {
                System.out.println("No image found in the database for this user.");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void setAnimalCount() {
        try (Connection connection = DbConfig.getConnection()) {
            String countQuery = "SELECT COUNT(*) FROM animals";
            PreparedStatement preparedStatement = connection.prepareStatement(countQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                animalCountLabel.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void setPlantCount() {
        try (Connection connection = DbConfig.getConnection()) {
            String countQuery = "SELECT COUNT(*) FROM plants";
            PreparedStatement preparedStatement = connection.prepareStatement(countQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                plantCountLabel.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void setFeedbackCount() {
        try (Connection connection = DbConfig.getConnection()) {
            String countQuery = "SELECT COUNT(*) FROM feedback where read_or_not='No'";
            PreparedStatement preparedStatement = connection.prepareStatement(countQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                feedbackCountLabel.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}



