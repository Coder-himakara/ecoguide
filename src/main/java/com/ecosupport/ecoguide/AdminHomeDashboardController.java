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
import java.time.LocalDate;
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
    private Button delete_animal;
    @FXML
    private Button update_btn_plant;
    @FXML
    private Button delete_plant;
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
    private Button view_map;
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
    private Label date_label;
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
        setAnimalCount();
        setPlantCount();
        setFeedbackCount();
        setCurrentDate();
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
    private void setCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        date_label.setText(currentDate.format(formatter));
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    //Update animal table
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
            storeSelected(attribute1);
        }
    }

    public void select_plant(javafx.scene.input.MouseEvent mouseEvent) {
        Modeltable_plants selectedObject = plant_table.getSelectionModel().getSelectedItem();
        if (selectedObject != null) {
            // Retrieve the desired attribute from the selected object
            IntegerProperty attribute2 = selectedObject.idProperty();
            storeSelected(attribute2);
        }
    }

    @FXML
    void backToHome(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Go back to Home");
        alert.setContentText("Are you sure you want to go back to the home page?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                Stage sign_in_stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("HomepageWithMenu.fxml"));
                Scene scene = new Scene(root);
                sign_in_stage.setScene(scene);
                Stage stage = (Stage) homeBtn.getScene().getWindow();
                stage.close();
                sign_in_stage.show();
            } catch (IOException ex) {
                Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // User chose CANCEL or closed the dialog
            alert.close();
        }
    }
    @FXML
    void goto_user_feedback(ActionEvent event) throws IOException {
        // Initialize stage
        Stage sign_in_stage = (Stage) user_feedback.getScene().getWindow();
        // Show the loading screen
        Handle_Transitions transitions = new Handle_Transitions();//create object of the class Handle_Transitions
        transitions.showLoadingScreen(sign_in_stage);
        // Load the actual scene with data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView_UserFeedback.fxml"));
        Parent root = loader.load();
        AdminView_UserFeedback controller = loader.getController();
        Scene scene = new Scene(root);
        // Switch to the actual scene after the loading screen
        transitions.switchSceneWithLoading(scene, sign_in_stage);

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

    // Update page of selected animal
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
            });}
    }
    // Update page of selected plant
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
            Admin_Plant_UpdateController controller = loader.getController();
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
    private void add_new_animal(ActionEvent event) {
        try {
            Stage sign_in_stage = (Stage) add_animal_btn.getScene().getWindow();
            // Show the loading screen
            Handle_Transitions transitions = new Handle_Transitions();
            transitions.showLoadingScreen(sign_in_stage);
            // Load the actual scene with data
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Animal_Add.fxml"));
            Parent root = loader.load();
            Admin_Animal_AddController controller = loader.getController();
            Scene scene = new Scene(root);
            // Switch to the actual scene after the loading screen
            transitions.switchSceneWithLoading(scene, sign_in_stage);
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void add_new_plant(ActionEvent event) {
        try {
            Stage sign_in_stage = (Stage) add_plant_btn.getScene().getWindow();
            // Show the loading screen
            Handle_Transitions transitions = new Handle_Transitions();
            transitions.showLoadingScreen(sign_in_stage);
            // Load the actual scene with data
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Plants_Add.fxml"));
            Parent root = loader.load();
            Admin_Plants_AddController controller = loader.getController();
            Scene scene = new Scene(root);
            // Switch to the actual scene after the loading screen
            transitions.switchSceneWithLoading(scene, sign_in_stage);
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void retrieveAndSetProfileImage() {
        try (Connection connection = DbConfig.getConnection()) {
            String selectQuery = "SELECT `img_data`, `last_name` FROM `new_admin` WHERE id_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, adminId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Get the image data from the database
                String last_name = resultSet.getString("last_name");
                Blob blob = resultSet.getBlob("img_data");
                if (blob != null) {
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());

                    // Create an Image object from the byte array
                    Image image = new Image(new ByteArrayInputStream(imageBytes));

                    double radius = 72.0;  // You can change this to your desired circle radius

                    // Set the larger dimension of the ImageView to fill the circle
                    // Use the max of the image's width/height to fill the circle
                    double scaleFactor = Math.max(image.getWidth(), image.getHeight()) / (radius * 2);

                    profile_pic.setFitWidth(image.getWidth() / scaleFactor);  // Scale to cover the circle
                    profile_pic.setFitHeight(image.getHeight() / scaleFactor);

                    // Create a Circle for clipping the image
                    Circle clip = new Circle(radius, radius, radius);  // CenterX, CenterY, Radius

                    // Set the Circle as the clip for the ImageView
                    profile_pic.setClip(clip);

                    // Set the image to the ImageView
                    profile_pic.setImage(image);
                } else {
                    System.out.println("No image found in the database for this user.");
                }
                my_name.setText(last_name);
            } else {
                System.out.println("No data found for this user.");
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

    @FXML
    void remove_animal(ActionEvent event) {
        Modeltable_animals selectedAnimal = animal_table.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Animal");
            alert.setContentText("Are you sure you want to delete this animal?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteAnimal(selectedAnimal.idProperty().get());
                updateTabel(); // Refresh the table
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Selection");
            alert.setContentText("Please select an animal to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void remove_plant(ActionEvent event) {
        Modeltable_plants selectedPlant = plant_table.getSelectionModel().getSelectedItem();
        if (selectedPlant != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Plant");
            alert.setContentText("Are you sure you want to delete this plant?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deletePlant(selectedPlant.idProperty().get());
                updateTabelPlants(); // Refresh the table
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Selection");
            alert.setContentText("Please select a plant to delete.");
            alert.showAndWait();
        }
    }

    private void deleteAnimal(int animalId) {
        String deleteQuery = "DELETE FROM animals WHERE animal_id = ?";
        String insertQuery = "INSERT INTO removed_animals SELECT * FROM animals WHERE animal_id = ?";
        try (Connection connection = DbConfig.getConnection()) {
            // Insert the record into removed_animal table
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, animalId);
                insertStatement.executeUpdate();
            }
            // Delete the record from animals table
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, animalId);
                deleteStatement.executeUpdate();
            }
            setAnimalCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePlant(int plantId) {
        String deleteQuery = "DELETE FROM plants WHERE plant_id = ?";
        String insertQuery = "INSERT INTO removed_plants SELECT * FROM plants WHERE plant_id = ?";
        try (Connection connection = DbConfig.getConnection()) {
            // Insert the record into removed_plants table
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, plantId);
                insertStatement.executeUpdate();
            }
            // Delete the record from plants table
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, plantId);
                deleteStatement.executeUpdate();
            }
            setPlantCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void go_to_map(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("admin_map.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/com/ecosupport/styles/admin_animal_add.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) view_map.getScene().getWindow();
            stage.close();
            sign_in_stage.setResizable(false);
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminHomeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



