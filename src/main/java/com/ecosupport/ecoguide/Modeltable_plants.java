package com.ecosupport.ecoguide;

public class Modeltable_plants {
    int id , population;
    String name, scientific_name, status, habitate,expantion, intro,root_system;
    double x_position,y_position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
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

    public String getHabitate() {
        return habitate;
    }

    public void setHabitate(String habitate) {
        this.habitate = habitate;
    }

    public String getExpantion() {
        return expantion;
    }

    public void setExpantion(String expantion) {
        this.expantion = expantion;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRoot_system() {
        return root_system;
    }

    public void setRoot_system(String root_system) {
        this.root_system = root_system;
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

    //public Modeltable_plants(int plantId, String name, String scientificName, String status, int population, String habitate, String expansion, String rootSystem, String intro, double xPosition, double yPosition) {
    //}

    public Modeltable_plants(int id, String name, String scientific_name,String status, int population, String habitate, String expantion, String intro, String root_system, double x_position, double y_position) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.scientific_name = scientific_name;
        this.population = population;
        this.habitate = habitate;
        this.expantion = expantion;
        this.intro = intro;
        this.root_system = root_system;
        this.x_position = x_position;
        this.y_position = y_position;
    }
}
