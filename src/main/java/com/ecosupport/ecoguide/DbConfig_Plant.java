package com.ecosupport.ecoguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DbConfig_Plant {
    Connection conn = null;

    public static Connection ConnectDb(){
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ecoguidedb","root","");
            return conn;
        } catch (Exception e){
            System.out.println("Not connected");
            return null;
        }
    }

    public static  ObservableList<Modeltable_plants> getDataPlants(){
        Connection conn = ConnectDb();
        ObservableList<Modeltable_plants> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from plants");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                list.add(new Modeltable_plants(rs.getInt("plant_id"),rs.getString("name"),rs.getString("scientific_name"), rs.getString("status"),rs.getInt("population"),rs.getString("habitate"),rs.getString("expansion"), rs.getString("root_system"),rs.getString("intro"),rs.getDouble("x_position"),rs.getDouble("y_position")));
            }

        }catch (Exception e){
            System.out.println(" not working");
        }
        return list;
    }
}
