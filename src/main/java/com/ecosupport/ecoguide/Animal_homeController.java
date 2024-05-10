package com.ecosupport.ecoguide;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Animal_homeController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private TableView<Modeltable_animals> table;

    @FXML
    private TableColumn<Modeltable_animals, String> name;

    @FXML
    private TableColumn<Modeltable_animals, String> scientific_name;

    @FXML
    private TableColumn<Modeltable_animals, Integer> population;

    @FXML
    void viewAnimalData(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

