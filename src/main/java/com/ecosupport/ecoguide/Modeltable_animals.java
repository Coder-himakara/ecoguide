package com.ecosupport.ecoguide;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Modeltable_animals {
    private final IntegerProperty animal_id;

    int population;
    String name, scientific_name, status, diet, active, intro;
    double x_position, y_position;

    public Modeltable_animals(int animal_id, String name, String scientific_name, String status, int population, String diet, String active, String intro, double x_position, double y_position) {
        this.animal_id = new SimpleIntegerProperty(animal_id);
        this.population = population;
        this.name = name;
        this.scientific_name = scientific_name;
        this.status = status;
        this.diet = diet;
        this.active = active;
        this.intro = intro;
        this.x_position = x_position;
        this.y_position = y_position;
    }

    public IntegerProperty idProperty() {
        return animal_id;
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

    public String getDiet() {
        return diet;
    }

//    public void setAnimal_id(int animal_id) {
//        this.animal_id = animal_id;
//    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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
