package com.babyd.babyd.models;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class BabyFullInfo {
    private UUID baby_id;                       // Baby id
    private String dipper;                      // Can be wet or dry
    private Integer feed_amount;                // can be null if we are on breast feeding
    private double weight;                      // baby weight
    private Time measure_time;                  // Will hold the last time the baby eat
    private Date today;                         // Will hold the date of today.
    private String breast_side;                 // Last breast side the baby eat from
    private Integer breast_feeding_time_length; // the time the baby breast feed
    private Integer sleeping_time;              // time in minutes the baby slept.

    public BabyFullInfo(UUID baby_id, String dipper, Integer feed_amount, double weight, Time measure_time, Date today, String breast_side, Integer breast_feeding_time_length, Integer sleeping_time) {
        this.baby_id = baby_id;
        this.dipper = dipper;
        this.feed_amount = feed_amount;
        this.weight = weight;
        this.measure_time = measure_time;
        this.today = today;
        this.breast_side = breast_side;
        this.breast_feeding_time_length = breast_feeding_time_length;
        this.sleeping_time = sleeping_time;
    }

    public UUID getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(UUID baby_id) {
        this.baby_id = baby_id;
    }

    public String getDipper() {
        return dipper;
    }

    public void setDipper(String dipper) {
        this.dipper = dipper;
    }

    public Integer getFeed_amount() {
        return feed_amount;
    }

    public void setFeed_amount(Integer feed_amount) {
        this.feed_amount = feed_amount;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Time getMeasure_time() {
        return measure_time;
    }

    public void setMeasure_time(Time measure_time) {
        this.measure_time = measure_time;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getBreast_side() {
        return breast_side;
    }

    public void setBreast_side(String breast_side) {
        this.breast_side = breast_side;
    }

    public Integer getBreast_feeding_time_length() {
        return breast_feeding_time_length;
    }

    public void setBreast_feeding_time_length(Integer breast_feeding_time_length) {
        this.breast_feeding_time_length = breast_feeding_time_length;
    }

    public Integer getSleeping_time() {
        return sleeping_time;
    }

    public void setSleeping_time(Integer sleeping_time) {
        this.sleeping_time = sleeping_time;
    }
}
