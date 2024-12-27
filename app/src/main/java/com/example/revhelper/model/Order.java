package com.example.revhelper.model;

import java.time.LocalDate;

public class Order {
    private String number;
    private String route;
    private LocalDate date;

    public Order(String number, String route, LocalDate date) {
        this.number = number;
        this.route = route;
        this.date = date;
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
