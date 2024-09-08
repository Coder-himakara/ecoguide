package  com.ecosupport.ecoguide;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Custemer_feedbackController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button home;

    @FXML
    private Button animal;

    @FXML
    private Button plant;

    @FXML
    private Button sendData;

    @FXML
    private TextField fb_name;

    @FXML
    private TextField fb_email;

    @FXML
    private TextArea fb;
    @FXML
    private Button about;

    @FXML
    private Button Menu;

    @FXML
    private AnchorPane menu;

    int conforme = 1 ;
    int show = 0 ;

    @FXML
    void goToAbout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("About_Page.fxml"));
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Close the current stage
            stage.close();

            // Create a new stage for the new scene
            Stage primary = new Stage();
            primary.setScene(scene);
            primary.setResizable(false);
            primary.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void goToAnimal(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Animal_Home.fxml"));
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Close the current stage
            stage.close();

            // Create a new stage for the new scene
            Stage primary = new Stage();
            primary.setScene(scene);
            primary.setResizable(false);
            primary.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToHome(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("HomepageWithMenu.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        Stage primary = new Stage();
        primary.setScene(scene);
        primary.setResizable(false);
        primary.show();
    }

    @FXML
    void goToPlant(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("plant_home.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        Stage primary = new Stage();
        primary.setScene(scene);
        primary.setResizable(false);
        primary.show();
    }

    @FXML
    void saveData(ActionEvent event) {
        String name = fb_name.getText();
        String email = fb_email.getText();
        String feedback = fb.getText();

        if (feedback.isEmpty()){
            Alert alert2 = new Alert(Alert.AlertType.ERROR,"",ButtonType.OK);
            alert2.setContentText("Feedback Field is required!");
            Optional<ButtonType> result = alert2.showAndWait();
        }else {
            sendDataToDatabase(name, email, feedback);

            if (conforme == 1) {
                fb_name.clear();
                fb_email.clear();
                fb.clear();
            }
        }
    }

    // Method to add data to the database
    private void sendDataToDatabase(String name, String E_mail, String feedback) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecoguidedb", "root","")) {
            // SQL query to insert data
            String query = "INSERT INTO feedback (name, email, feedback) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, E_mail);
            preparedStatement.setString(3, feedback);

            int rowsAffected = preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
                //Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                //alert.setHeaderText("Data inserted successfully!");
                alert.setContentText("Data inserted successfully!");
                Optional<ButtonType> result = alert.showAndWait();
            } else {
                System.out.println("Failed to insert data!");
                alert.setContentText("Failed to insert data!");
                Optional<ButtonType> result = alert.showAndWait();
                conforme = 0 ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    void viewMenu(ActionEvent event) {
        if(show == 0){
            menu.setVisible(true);
            show = 1 ;
        }else{
            menu.setVisible(false);
            show = 0 ;
        }
    }

    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
