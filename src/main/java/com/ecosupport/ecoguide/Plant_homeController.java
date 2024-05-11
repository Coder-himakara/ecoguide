
package com.ecosupport.ecoguide;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Plant_homeController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    ObservableList<Modeltable_plants> oblists;

    int index = -1;

    Connection conn = null;
    ResultSet rs = null ;
    PreparedStatement pst = null ;

    @FXML
    private TableView<Modeltable_plants> plantTable;

    @FXML
    private TableColumn<Modeltable_plants, String> name;

    @FXML
    private TableColumn<Modeltable_plants, String> scientific_name;

    @FXML
    private TableColumn<Modeltable_plants, Integer> population;

    @FXML
    private Button showPlant;

    @FXML
    private Label plant_species;

    @FXML
    private Label endangered_species;

    public void update_planttable(){
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //name.setCellValueFactory(new PropertyValueFactory<>("name"));
        scientific_name.setCellValueFactory(new PropertyValueFactory<>("scientific_name"));
        population.setCellValueFactory(new PropertyValueFactory<>("population"));

        oblists = DbConfig_Plant.getDataPlants();

        plantTable.setItems(oblists);
    }

    @FXML
    void viewData(ActionEvent event) {

    }

    public void updateTable(){

    }

    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update_planttable();

    }
}
