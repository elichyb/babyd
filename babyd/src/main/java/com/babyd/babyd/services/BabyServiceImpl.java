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
            throw new EtBadBirthdayFormat("birth day is in the wrong format");
        UUID baby_id = babyRepository.createBaby(parent_id, first_name, last_name, food_type, birth_day, weight);
        return new Baby(baby_id, first_name, last_name, food_type, birth_day);
    }

    @Override
    public void removeBaby(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException {
        babyRepository.removeBaby(parent_id, baby_id);
    }

    @Override
    public void setBabyWeight(UUID baby_id, double weight) throws EtUnableConnectToDB {
        try {
            babyRepository.update_baby_weight(baby_id, weight);
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
    public void setDipper(UUID baby_id, String dipper_date, String time, String dipper) {
        Pattern date_attern = Pattern.compile("^\\d{4}(-)(((0)[0-9])|((1)[0-2]))(-)([0-2][0-9]|(3)[0-1])$");
        Pattern time_attern = Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$");
        if ((! date_attern.matcher(dipper_date).matches()) || (! time_attern.matcher(time).matches()))
            throw new EtResourceNotFoundException("Problem in the data we got Date/Time isn't send good");
        if ((!dipper.equals("wet")) && (!dipper.equals("dirty")))
            throw new EtResourceNotFoundException("Problem in the data we got dipper that isn't wet nor dirty");

        babyRepository.setDipper( baby_id, dipper_date, time, dipper);
    }
}
