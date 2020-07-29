package com.babyd.babyd.services;

import com.babyd.babyd.exceptions.EtBadBirthdayFormat;
import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;
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
    public Baby addBaby(UUID parent_id, String first_name, String last_name, int food_type, String birth_day)
            throws EtResourceFoundException {
        Pattern pattern = Pattern.compile("^([0-2][0-9]|(3)[0-1])(-)(((0)[0-9])|((1)[0-2]))(-)\\d{4}$"); // validate date pattern
        if (! pattern.matcher(birth_day).matches())
            throw new EtBadBirthdayFormat("birth day is in the wrong format");
        UUID baby_id = babyRepository.createBaby(parent_id, first_name, last_name, food_type, birth_day);
        Baby new_baby = new Baby(baby_id, first_name, last_name, food_type, birth_day);
        return new_baby;
    }

    @Override
    public void removeBaby(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException {
        babyRepository.removeBaby(parent_id, baby_id);
    }
}
