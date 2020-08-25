/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Elichy Barak 2020
 *
 *  The source code for this program is not published or other-
 * wise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * ----------------------------------------------------------------------------
 */

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

    void update_baby_weight(UUID baby_id, double weight, String measure_date) throws EtResourceNotFoundException;

    List<BabyFullInfo> getBabyFullInfoForDate(UUID baby_id, String date);

    void setDiaper(UUID baby_id, String measure_date, String measure_time, Boolean wet_diaper, Boolean dirty_diaper);

    void setSleepingTime(UUID baby_id, String measure_date, String measure_time, int sleeping_time);

    void setFormula(UUID baby_id, String measure_date, String measure_time, int amount, String feed_type);

    void setBreast(UUID baby_id, String measure_date, String measure_time, String breast_side, int breast_feeding_time_length, String feed_type);
}
