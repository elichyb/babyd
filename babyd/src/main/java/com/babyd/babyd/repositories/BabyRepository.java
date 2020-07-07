package com.babyd.babyd.repositories;

import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;

import java.util.Date;
import java.util.List;

public interface BabyRepository {
    List<Baby> findAll(int parent_id) throws EtResourceNotFoundException;

    Baby findById(int parent_id, int baby_id) throws EtResourceNotFoundException;

    int createBaby(int parent_id, int baby_id, String first_name, String last_name, int feed_type, float wight, Date birth_day)
            throws EtResourceFoundException;

    void removeBaby(int parent_id, int baby_id) throws EtResourceNotFoundException;

}
