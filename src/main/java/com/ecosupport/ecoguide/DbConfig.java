package com.ecosupport.ecoguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DbConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/ecoguidedb?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC", USERNAME, PASSWORD);

        Statement statement = connection.createStatement();

        // Create the database if it does not exist
        String sqlCreateDatabase = "CREATE DATABASE IF NOT EXISTS ecoguidedb";
        statement.execute(sqlCreateDatabase);

        // Use the created database
        String sqlUseDatabase = "USE ecoguidedb";
        statement.execute(sqlUseDatabase);

        // Create the tables if they do not exist
        String sqlCreateAnimalsTable = "CREATE TABLE IF NOT EXISTS animals (" +
                "animal_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name CHAR(30), " +
                "scientific_name VARCHAR(50), " +
                "status CHAR(30), " +
                "population INT, " +
                "diet VARCHAR(50), " +
                "active VARCHAR(50), " +
                "intro TEXT NULL, " +
                "x_position DOUBLE, " +
                "y_position DOUBLE)";
        statement.execute(sqlCreateAnimalsTable);

        String sqlCreatePlantsTable = "CREATE TABLE IF NOT EXISTS plants (" +
                "plant_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name CHAR(20), " +
                "scientific_name VARCHAR(50), " +
                "status CHAR(20), " +
                "population INT, " +
                "habitate VARCHAR(20), " +
                "expansion VARCHAR(20), " +
                "root_system VARCHAR(20), " +
                "intro TEXT NULL, " +
                "x_position DOUBLE, " +
                "y_position DOUBLE)";
        statement.execute(sqlCreatePlantsTable);

        String sqlCreatePlantsImageTable = "CREATE TABLE IF NOT EXISTS plant_images (" +
                "plant_pid INT PRIMARY KEY AUTO_INCREMENT, " +
                "plant_name CHAR(30), " +
                "image_data LONGBLOB NULL, " +
                "plant_fid INT)";

        statement.execute(sqlCreatePlantsImageTable);

        String sqlCreateAnimalsImageTable = "CREATE TABLE IF NOT EXISTS animal_images (" +
                "animal_pid INT PRIMARY KEY AUTO_INCREMENT, " +
                "animal_name CHAR(30), " +
                "image_data LONGBLOB NULL, " +
                "animal_fid INT)";

        statement.execute(sqlCreateAnimalsImageTable);

        String sqlCreateFeedbackTable = "CREATE TABLE IF NOT EXISTS feedback (" +
                "feedback_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(30), " +
                "email VARCHAR(50), " +
                "feedback TEXT,"
                + "read_or_not VARCHAR(5) DEFAULT 'No')";

        statement.execute(sqlCreateFeedbackTable);

        String sqlCreateNewAdminTable = "CREATE TABLE IF NOT EXISTS new_admin (" +
                "id_no  VARCHAR(20) PRIMARY KEY, " +
                "first_name VARCHAR(30), " +
                "last_name VARCHAR(30), " +
                "email VARCHAR(50), " +
                "username VARCHAR(30), " +
                "password VARCHAR(30), " +
                "phone_no VARCHAR(30) NULL, " +
                "job_role VARCHAR(30) NULL, " +
                "img_data LONGBLOB NULL)";


        statement.execute(sqlCreateNewAdminTable);

        return connection;
    }

    public static ObservableList<Modeltable_animals> getDatausers() throws SQLException {
        Connection conn = getConnection();
        ObservableList<Modeltable_animals> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM animals");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Modeltable_animals(rs.getInt("animal_id"), rs.getString("name"), rs.getString("scientific_name"),
                        rs.getString("status"), rs.getInt("population"), rs.getString("diet"), rs.getString("active"),
                        rs.getString("intro"), rs.getDouble("x_position"), rs.getDouble("y_position")));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public static ObservableList<Modeltable_plants> getDatausersPlants() throws SQLException {
        Connection conn = getConnection();
        ObservableList<Modeltable_plants> list2 = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM plants");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list2.add(new Modeltable_plants(rs.getInt("plant_id"), rs.getString("name"), rs.getString("scientific_name"),
                        rs.getString("status"), rs.getInt("population"), rs.getString("habitate"), rs.getString("expansion"), rs.getString("root_system"),
                        rs.getString("intro"), rs.getDouble("x_position"), rs.getDouble("y_position")));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list2;
    }

    public static ObservableList<Modeltable_feedback> getDataFeedback() throws SQLException{
        Connection conn = getConnection();
        ObservableList<Modeltable_feedback> list3 = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from feedback order by read_or_not asc");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                list3.add(new Modeltable_feedback(rs.getInt("feedback_id"),rs.getString("name"),rs.getString("email"), rs.getString("feedback"),rs.getString("read_or_not")));
            }
        }catch(Exception ex){
            System.out.println(" not working");
        }
        return list3;
    }
}
