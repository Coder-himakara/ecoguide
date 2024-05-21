package com.ecosupport.ecoguide;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Modeltable_feedback {
    private final IntegerProperty feedback_id;

    String name, email, feedback, read_or_not;

    public Modeltable_feedback(int feedback_id, String name, String email, String feedback, String read_or_not){
        this.feedback_id = new SimpleIntegerProperty(feedback_id);
        this.name = name;
        this.email = email;
        this.feedback = feedback;
        this.read_or_not = read_or_not;
    }

    public IntegerProperty idProperty(){
        return feedback_id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getFeedback(){
        return feedback;
    }

    public void setFeedback(String feedback){
        this.feedback = feedback;
    }

    public String getRead_or_not(){
        return read_or_not;
    }

    public void setRead_or_not(String read_or_not){
        this.read_or_not = read_or_not;
    }
}
