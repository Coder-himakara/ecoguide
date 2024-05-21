package com.ecosupport.ecoguide;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminView_UserFeedback implements Initializable
{
    ObservableList<Modeltable_feedback> oblist;
    ObservableList<Modeltable_feedback> datalist;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int selected_id = 0;
    @FXML
    private Button menuBtn;
    @FXML
    private Button menuCloseBtn;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private Button admin_btn;
    @FXML
    private TableView<Modeltable_feedback> feedback_table;
    @FXML
    private TableColumn<Modeltable_feedback, Integer> col_feedback_id;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_name;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_email;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_feedback;
    @FXML
    private TableColumn<Modeltable_feedback, String> col_read_or_not;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
    }

    @FXML
    public void updateTable(){
        col_feedback_id.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_feedback.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        col_read_or_not.setCellValueFactory(new PropertyValueFactory<>("read_or_not"));
        try {
            oblist = DbConfig.getDataFeedback();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        feedback_table.setItems(oblist);
    }

    @FXML
    void Back_to_dashboard(ActionEvent event) {
        try {
            Stage sign_in_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AdminHomeDashboard.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/HomepageMenuCSS.css");
            sign_in_stage.setScene(scene);
            Stage stage = (Stage) admin_btn.getScene().getWindow();
            stage.close();
            sign_in_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Signup_PageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void menuOpen(ActionEvent event) {
        sidebar.setVisible(true);
        menuCloseBtn.setVisible(true);
        menuBtn.setVisible(false);
    }

    @FXML
    void menuClose(ActionEvent event) {
        sidebar.setVisible(false);
        menuCloseBtn.setVisible(false);
        menuBtn.setVisible(true);
    }

}
