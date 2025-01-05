package com.example.revhelper.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


/**
 * Order class object
 *
 * type - revision type: 1 - in move, 2 - in service station
 */
public class Order {
    private String number;
    private String route;
    private LocalDate date;
    private Map<String, String> directors = new HashMap<>();
    private TrainDto train;
    private int type;

    public Order(String number, String route, LocalDate date, Map<String, String> directors, TrainDto train) {
        this.number = number;
        this.route = route;
        this.date = date;
        this.directors = directors;
        this.train = train;
    }

    public Order() {

    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setTrain(TrainDto train) {
        this.train = train;
    }

    public TrainDto getTrain() {
        return this.train;
    }

    public void setDirectors(Map<String, String> directors) {
        this.directors = directors;
    }

    public Map<String, String> getDirectors() {
        return this.directors;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNumber() {
        return this.number;
    }

    public String getRoute() {
        return this.route;
    }

    public LocalDate getDate() {
        return this.date;
    }
}
