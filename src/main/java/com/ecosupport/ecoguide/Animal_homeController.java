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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
    private ImageView mapView;

    @FXML
    private StackPane stackpane;

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

    public void mapMarking(){
        Canvas canvas = new Canvas(mapView.getImage().getWidth() , mapView.getImage().getHeight());

        GraphicsContext gc = canvas.getGraphicsContext2D();

        int i ;
        int last;
        last = oblist.size();
        //System.out.println(last);

        //gc.setFill(javafx.scene.paint.Color.RED);
        //double radius = 5;
        String imagePath = "images/animal_add/pointer_icon.png";

        for(i = 0 ; i<=last-1 ; i++) {
            double x =oblist.get(i).x_position;
            double y =oblist.get(i).y_position;

            //System.out.println(x);
            //System.out.println(y);

            //gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

            Image image = new Image(getClass().getResourceAsStream(imagePath));
            gc.drawImage(image, x, y);

        }
        stackpane.getChildren().add(canvas);

    }


    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Todo
        updateTabel();
        mapMarking();
    }
}

