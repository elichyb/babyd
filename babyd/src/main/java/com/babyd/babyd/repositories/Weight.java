package com.babyd.babyd.repositories;

public class Weight {
    private double baby_weight;
    private String date; // MUST BE from format yyyy-MM-dd

    public Weight(double baby_weight, String date) {
        this.baby_weight = baby_weight;
        this.date = date;
    }

    public double getBaby_weight() {
        return baby_weight;
    }

    public void setBaby_weight(double baby_weight) {
        this.baby_weight = baby_weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
