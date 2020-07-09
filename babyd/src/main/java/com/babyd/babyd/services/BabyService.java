package com.babyd.babyd.services;

import com.babyd.babyd.exceptions.EtBadBirthdayFormat;
import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;

import java.util.List;
import java.util.UUID;

public interface BabyService {

    List<Baby> fetchAllBabies(UUID baby_id);

    Baby fetchBabyById(UUID parent_id, int baby_id) throws EtResourceFoundException;

    Baby addBaby(UUID parent_id, String first_name, String last_name, int food_type, String birth_day)
            throws EtResourceFoundException, EtBadBirthdayFormat;

    void removeBaby(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException;

}
