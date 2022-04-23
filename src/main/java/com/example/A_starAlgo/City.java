package com.example.A_starAlgo;

import javafx.scene.shape.Circle;

import java.util.LinkedList;

public class City {
    String cityName;
    double X, Y;
    Circle Circle;
    LinkedList<Branching> branches = new LinkedList<>();


    public City(String cityName, double x, double y){
        this.cityName = cityName;
        this.X = x;
        this.Y = y;

    }


}