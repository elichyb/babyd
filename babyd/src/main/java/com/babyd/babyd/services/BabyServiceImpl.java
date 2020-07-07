package com.babyd.babyd.services;

import com.babyd.babyd.exceptions.EtAuthException;
import com.babyd.babyd.exceptions.EtBadBirthdayFormat;
import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;
import com.babyd.babyd.repositories.BabyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class BabyServiceImpl implements BabyService {
    @Autowired
    BabyRepository babyRepository;

    @Override
    public List<Baby> fetchAllBabies(int parent_id) {
        return null;
    }

    @Override
    public Baby fetchBabyById(int parent_id, int baby_id) throws EtResourceFoundException {
        return null;
    }

    @Override
    public Baby addBaby(int parent_id, String first_name, String last_name, int feed_type, float wight, String birth_day)
            throws EtResourceFoundException {
        Pattern pattern = Pattern.compile("^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$"); // validate date pattern
        if (! pattern.matcher(birth_day).matches())
            throw new EtBadBirthdayFormat("birth day is in the wrong format");
        int baby_id = babyRepository.createBaby(parent_id, first_name, last_name, feed_type, wight, birth_day);
        return babyRepository.findById(parent_id, baby_id);
    }

    @Override
    public void removeBaby(int parent_id, int baby_id) throws EtResourceNotFoundException {

    }
}
