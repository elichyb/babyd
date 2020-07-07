package com.babyd.babyd.services;

import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;

import java.util.Date;
import java.util.List;

public interface BabyService {

    List<Baby> fetchAllBabies(int id);

    Baby fetchBabyById(int parent_id, int baby_id) throws EtResourceFoundException;

    Baby addBaby(int user_id, int baby_id, String first_name, String last_name, int feed_type, float wight, Date birth_day)
            throws EtResourceFoundException;

    void removeBaby(int parent_id, int baby_id) throws EtResourceNotFoundException;

}
