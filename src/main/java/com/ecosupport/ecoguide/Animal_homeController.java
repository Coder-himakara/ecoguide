package com.ecosupport.ecoguide;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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
    int id = 0 ;
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
    private Button viewData;

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

    private static IntegerProperty getIdForRowNumber(ObservableList<Modeltable_animals> dataList, int rowNumber) {
        if (rowNumber >= 0 && rowNumber <= dataList.size()) {
            Modeltable_animals rowData = dataList.get(rowNumber); // Row numbers are 1-indexed, so subtract 1
            return rowData.idProperty(); // Assuming DataModel has a idProperty() method
        } else {
            System.out.println("Invalid row number: " + rowNumber);
            IntegerProperty x = new SimpleIntegerProperty(-1);
            return x; // Or handle invalid row numbers in some other way
        }
    }

    @FXML
    void viewAnimalData(ActionEvent event) {
        //plantTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        int index = table.getSelectionModel().getFocusedIndex();
        int id = getIdForRowNumber(oblist, index).get();
        //System.out.println("ID for row number " + index + ": " + id);
        //System.out.println(name.getCellData(index).toString());


        if (id != 0) {
            Stage sign_in_stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Animal_user_view.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the controller of the Animal_user_view.fxml
            Animal_user_viewController controller = loader.getController();

            // Pass the selected variable to the controller
            controller.setSelectedAttribute(id);

            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/plant_user_view.css");

            sign_in_stage.setScene(scene);
            Stage stage = (Stage) viewData.getScene().getWindow();
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

