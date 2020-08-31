/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Elichy Barak 2020
 *
 *  The source code for this program is not published or other-
 * wise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * ----------------------------------------------------------------------------
 */

package com.babyd.babyd.models;

public class BabyFullInfo {
    private String measure_date;                // Will hold the date of today.
    private String measure_time;                // Will hold the last time the baby eat
    private boolean wet_diaper;                 // Represent wet diaper
    private boolean dirty_diaper;               // Represent dirty diaper
    private Integer feed_amount;                // Can be null if we are on breast feeding
    private double weight;                      // Baby weight
    private String feed_type;                   // Baby feed type for this current time
    private String breast_side;                 // Last breast side the baby eat from
    private Integer breast_feeding_time_length; // The time the baby breast feed in minutes
    private Integer sleeping_time;              // Time in minutes the baby slept.

    public BabyFullInfo(String measure_date, String measure_time, double weight, boolean wet_diaper, boolean dirty_diaper, Integer feed_amount,
                        String breast_side, Integer breast_feeding_time_length, Integer sleeping_time, String feed_type) {
        this.measure_date = measure_date;
        this.measure_time = measure_time;
        this.weight = weight;
        this.wet_diaper = wet_diaper;
        this.dirty_diaper = dirty_diaper;
        this.breast_side = breast_side;
        this.feed_amount = feed_amount;
        this.breast_feeding_time_length = breast_feeding_time_length;
        this.sleeping_time = sleeping_time;
        this.feed_type = feed_type;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
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

    public String getMeasure_time() {
        return measure_time;
    }

    public void setMeasure_time(String measure_time) {
        this.measure_time = measure_time;
    }

    public String getMeasure_date() {
        return measure_date;
    }

    public void setMeasure_date(String measure_date) {
        this.measure_date = measure_date;
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

    public boolean isWet_diaper() {
        return wet_diaper;
    }

    public void setWet_diaper(boolean wet_diaper) {
        this.wet_diaper = wet_diaper;
    }

    public boolean isDirty_diaper() {
        return dirty_diaper;
    }

    public void setDirty_diaper(boolean dirty_diaper) {
        this.dirty_diaper = dirty_diaper;
    }
}
