
package com.ecosupport.ecoguide;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Plant_homeController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private TableView<?> plantTable;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> scientific_name;

    @FXML
    private TableColumn<?, ?> population;

    @FXML
    private Button showPlant;

    @FXML
    private Label plant_species;

    @FXML
    private Label endangered_species;

    public void updateTable(){

    }

    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
