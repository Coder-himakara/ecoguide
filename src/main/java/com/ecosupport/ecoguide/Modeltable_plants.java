package com.ecosupport.ecoguide;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Modeltable_plants {
    private final IntegerProperty plant_id;

    int population;
    String name, scientific_name, status, habitate, expansion,  root_system, intro;
    double x_position, y_position;

    public Modeltable_plants(int plant_id, String name, String scientific_name, String status, int population, String habitate, String expansion, String root_system, String intro, double x_position, double y_position) {
        this.plant_id = new SimpleIntegerProperty(plant_id);
        this.population = population;
        this.name = name;
        this.scientific_name = scientific_name;
        this.status = status;
        this.habitate = habitate;
        this.expansion = expansion;
        this.root_system = root_system;
        this.intro = intro;
        this.x_position = x_position;
        this.y_position = y_position;
    }

    public IntegerProperty idProperty() {
        return plant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getHabitate() {
        return habitate;
    }

//    public void setPlant_id(int plant_id) {
//        this.plant_id = plant_id;
//    }

    public void setHabitate(String habitate) {
        this.habitate = habitate;
    }

    public String getExpansion() {
        return expansion;
    }

    public void setExpansion(String expansion) {
        this.expansion = expansion;
    }

    public String getRoot_system() {
        return root_system;
    }

    public void setRoot_system(String root_system) {
        this.root_system = root_system;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public double getX_position() {
        return x_position;
    }

    public void setX_position(double x_position) {
        this.x_position = x_position;
    }

    public double getY_position() {
        return y_position;
    }

    public void setY_position(double y_position) {
        this.y_position = y_position;
    }
}
