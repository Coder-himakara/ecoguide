package com.ecosupport.ecoguide;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Animal_homeController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    ObservableList<Modeltable_animals> oblist;
    ObservableList<Modeltable_animals> datalist;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int selected_id = 0;

    @FXML
    private Label animal_species;

    @FXML
    private Label endangered_species;


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
    public void updateTabel() {

        //col_animal_id.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        scientific_name.setCellValueFactory(new PropertyValueFactory<>("scientific_name"));

        population.setCellValueFactory(new PropertyValueFactory<>("population"));

        try {
            oblist = DbConfig.getDatausers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        table.setItems(oblist);
        /*int rowCount = oblist.size();
        animal_species.setText(Integer.toString(rowCount));
        */
    }


    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Todo
        updateTabel();
    }
}

