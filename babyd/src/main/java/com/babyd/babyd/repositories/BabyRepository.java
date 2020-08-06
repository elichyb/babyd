package com.babyd.babyd.repositories;

import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;
import com.babyd.babyd.models.BabyFullInfo;

import java.util.List;
import java.util.UUID;

public interface BabyRepository {
    List<Baby> fetchAllBabies(UUID parent_id) throws EtResourceNotFoundException;

    Baby findById(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException;

    UUID createBaby(UUID parent_id, String first_name, String last_name, int food_type, String birth_day, double weight)
            throws EtResourceFoundException;

    void removeBaby(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException;

    void update_baby_weight(UUID baby_id, double weight) throws EtResourceNotFoundException;

    List<BabyFullInfo> getBabyFullInfoForDate(UUID baby_id, String date);

    void setDipper(UUID baby_id, String dipper_date, String time, String dipper);
}
