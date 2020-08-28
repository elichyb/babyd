/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Elichy Barak 2020
 *
 *  The source code for this program is not published or other-
 * wise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * ----------------------------------------------------------------------------
 */

package com.babyd.babyd.services;

import com.babyd.babyd.exceptions.EtBadBirthdayFormat;
import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.exceptions.EtUnableConnectToDB;
import com.babyd.babyd.models.Baby;
import com.babyd.babyd.models.BabyFullInfo;
import com.babyd.babyd.repositories.BabyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
public class BabyServiceImpl implements BabyService {
    @Autowired
    BabyRepository babyRepository;

    @Override
    public List<Baby> fetchAllBabies(UUID parent_id) {
        return babyRepository.fetchAllBabies(parent_id);
    }

    @Override
    public Baby fetchBabyById(UUID parent_id, UUID baby_id) throws EtResourceFoundException {
        return babyRepository.findById(parent_id, baby_id);
    }


    @Override
    public Baby addBaby(UUID parent_id, String first_name, String last_name, int food_type, String birth_day, double weight)
            throws EtResourceFoundException {
        Pattern pattern = Pattern.compile("^\\d{4}(-)(((0)[0-9])|((1)[0-2]))(-)([0-2][0-9]|(3)[0-1])$"); // validate date pattern
        if (! pattern.matcher(birth_day).matches())
            throw new EtBadBirthdayFormat("Birth day is in the wrong format");
        UUID baby_id = babyRepository.createBaby(parent_id, first_name, last_name, food_type, birth_day, weight);
        return new Baby(baby_id, first_name, last_name, food_type, birth_day);
    }

    @Override
    public void removeBaby(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException {
        babyRepository.removeBaby(parent_id, baby_id);
    }

    @Override
    public void setBabyWeight(UUID baby_id, double weight, String measure_date) throws EtUnableConnectToDB {
        try {
            babyRepository.update_baby_weight(baby_id, weight, measure_date);
        }
        catch (Exception e){
            throw new EtUnableConnectToDB("Failed to set new weight");
        }
    }

    @Override
    public List<BabyFullInfo> getBabyFullInfoForDate(UUID baby_id, String date) {
        try {
            babyRepository.getBabyFullInfoForDate(baby_id, date);
        }
        catch (Exception e){
            throw new EtResourceNotFoundException("Can't find baby full info in DB");
        }
        return null;
    }

    @Override
    public void setDiaper(UUID baby_id, String measure_date, String measure_time, Boolean wet_diaper, Boolean dirty_diaper) {
        check_format_date_and_time(measure_date, measure_time);
        babyRepository.setDiaper( baby_id, measure_date, measure_time, wet_diaper, dirty_diaper);
    }

    @Override
    public void setSleepingTime(UUID baby_id, String measure_date, String measure_time, int sleeping_time) {
        check_format_date_and_time(measure_date, measure_time);
        babyRepository.setSleepingTime(baby_id, measure_date, measure_time, sleeping_time);
    }

    @Override
    public void setFormula(UUID baby_id, String measure_date, String measure_time, int amount, String feed_type) throws EtResourceFoundException {
        check_format_date_and_time(measure_date, measure_time);
        babyRepository.setFormula(baby_id, measure_date, measure_time, amount, feed_type);
    }

    @Override
    public void setBreast(UUID baby_id, String measure_date, String measure_time, String breast_side, int breast_feeding_time_length, String feed_type) throws EtResourceFoundException {
        check_format_date_and_time(measure_date, measure_time);
        babyRepository.setBreast(baby_id, measure_date, measure_time, breast_side, breast_feeding_time_length, feed_type);
    }

    private void check_format_date_and_time(String date, String time)
    {
        Pattern date_attern = Pattern.compile("^\\d{4}(-)(((0)[0-9])|((1)[0-2]))(-)([0-2][0-9]|(3)[0-1])$");
        Pattern time_attern = Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$");
        if ((! date_attern.matcher(date).matches()) || (! time_attern.matcher(time).matches()))
            throw new EtResourceNotFoundException("Problem in the data we got Date/Time isn't send good");
    }
}
