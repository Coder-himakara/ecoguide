package com.ecosupport.ecoguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DbConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/ecoguidedb?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
}
