package  com.ecosupport.ecoguide;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button save;

    @FXML
    void goToAnimal(ActionEvent event) {
        Stage primary = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Animal_Home.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        primary.setScene(scene);
        Stage stage = (Stage) animal.getScene().getWindow();
        stage.close();
        primary.show();
        System.out.println("done");
    }

    @FXML
    void goToHome(ActionEvent event) {

    }

    @FXML
    void goToPlant(ActionEvent event) {

    }

    @FXML
    void saveData(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
