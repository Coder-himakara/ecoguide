
package com.ecosupport.ecoguide;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Plant_homeController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    ObservableList<Modeltable_plants> oblists;
    ObservableList<Modeltable_plants> oblists_show;
    int index = -1;
    int id = 0 ;
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

    @FXML
    private AnchorPane Anchorpane_menu;

    @FXML
    private Button menu;

    @FXML
    private Button home;

    @FXML
    private Button animal;

    @FXML
    private Button feedback;

    @FXML
    private Button about;

    int show = 0 ;

    @FXML
    void goToAbout(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("About_Page.fxml"));
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
    void goToAnimal(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("animal_home.fxml"));
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
    void goToFeedback(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Custemer_feedback.fxml"));
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
    void showMenu(ActionEvent event) {
        if(show == 0){
            Anchorpane_menu.setVisible(true);
            show = 1;
        }else{
            Anchorpane_menu.setVisible(false);
            show = 0;
        }
    }

    public void update_planttable(){
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //id.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
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
            double width = 57;
            double height = 53;
            gc.drawImage(image, x, y, width, height);

        }
        stackpane.getChildren().add(canvas);

    }


    private static IntegerProperty getIdForRowNumber(ObservableList<Modeltable_plants> dataList, int rowNumber) {
        if (rowNumber >= 0 && rowNumber <= dataList.size()) {
            Modeltable_plants rowData = dataList.get(rowNumber); // Row numbers are 1-indexed, so subtract 1
            return rowData.idProperty(); // Assuming DataModel has a getId() method
        } else {
            System.out.println("Invalid row number: " + rowNumber);
            IntegerProperty x = new SimpleIntegerProperty(-1);
            return x; // Or handle invalid row numbers in some other way
        }
    }

    @FXML
    void viewData(ActionEvent event) {
        //plantTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        int index = plantTable.getSelectionModel().getFocusedIndex();
        int id = getIdForRowNumber(oblists, index).get();
        //System.out.println("ID for row number " + index + ": " + id);
        //System.out.println(name.getCellData(index).toString());


        if (id != 0) {
            Stage sign_in_stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Separate_plant.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the controller of the Plant_user_view.fxml
            Separate_plantController controller = loader.getController();

            // Pass the selected variable to the controller
            controller.setSelectedAttribute(id);

            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/plant_user_view.css");

            sign_in_stage.setScene(scene);
            Stage stage = (Stage) showPlant.getScene().getWindow();
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

    public void species(){
        int rowCount = oblists.size();
        plant_species.setText(Integer.toString(rowCount));
        System.out.println(rowCount);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update_planttable();
        mapMarking();
        species();
    }
}
