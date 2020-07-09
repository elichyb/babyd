package com.babyd.babyd.models;

import java.util.UUID;

public class Baby {
    private UUID id;
    private String first_name;
    private String last_name;
    private int food_type; // (1- will represent breastfeeding; 2- formula; 3- combined)
    private float wight_table_id;
    private int breast_feed_table_id;
    private int formula_feed_table_id;
    private String baby_birth_day;

    public Baby(UUID id, String first_name, String last_name, int food_type, String baby_birth_day) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.food_type = food_type;
        this.baby_birth_day = baby_birth_day;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getfood_type() {
        return food_type;
    }

    public void setfood_type(int food_type) {
        this.food_type = food_type;
    }

    public float getWight_table_id() {
        return wight_table_id;
    }

    public void setWight_table_id(int wight_table_id) {
        this.wight_table_id = wight_table_id;
    }

    public int getBreast_feed_table_id() {
        return breast_feed_table_id;
    }

    public void setBreast_feed_table_id(int breast_feed_table_id) {
        this.breast_feed_table_id = breast_feed_table_id;
    }

    public int getFormula_feed_table_id() {
        return formula_feed_table_id;
    }

    public void setFormula_feed_table_id(int formula_feed_table_id) {
        this.formula_feed_table_id = formula_feed_table_id;
    }

    public String getBaby_birth_day() {
        return baby_birth_day;
    }

    public void setBaby_birth_day(String baby_birth_day) {
        this.baby_birth_day = baby_birth_day;
    }
}
