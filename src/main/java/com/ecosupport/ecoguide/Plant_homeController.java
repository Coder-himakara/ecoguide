
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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

    @FXML
    private ImageView mapView;

    @FXML
    private StackPane stackpane;

    public void update_planttable(){
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //name.setCellValueFactory(new PropertyValueFactory<>("name"));
        scientific_name.setCellValueFactory(new PropertyValueFactory<>("scientific_name"));
        population.setCellValueFactory(new PropertyValueFactory<>("population"));

        try {
            oblists = DbConfig.getDatausersPlants();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        plantTable.setItems(oblists);
    }

    public void mapMarking(){
        Canvas canvas = new Canvas(mapView.getImage().getWidth() , mapView.getImage().getHeight());

        GraphicsContext gc = canvas.getGraphicsContext2D();

        int i ;
        int last;
        last = oblists.size();
        //System.out.println(last);

        //gc.setFill(javafx.scene.paint.Color.RED);
        //double radius = 5;
        String imagePath = "images/animal_add/pointer_icon.png";

        for(i = 0 ; i<=last-1 ; i++) {
            double x =oblists.get(i).x_position;
            double y =oblists.get(i).y_position;

            //System.out.println(x);
            //System.out.println(y);

            //gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

            Image image = new Image(getClass().getResourceAsStream(imagePath));
            gc.drawImage(image, x, y);

        }
        stackpane.getChildren().add(canvas);

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
        mapMarking();
    }
}
