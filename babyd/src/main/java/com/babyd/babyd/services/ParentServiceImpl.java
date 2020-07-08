package com.babyd.babyd.services;
import com.babyd.babyd.exceptions.EtAuthException;
import com.babyd.babyd.models.Parent;
import com.babyd.babyd.repositories.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
public class ParentServiceImpl implements ParentService {
    @Autowired
    ParentRepository parentRepository;

    @Override
    public Parent validateParent(String email, String password) throws EtAuthException {
        if (email != null) email = email.toLowerCase();
        return parentRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public Parent registerParent(String first_name, String last_name, String email, String password) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$"); //email validator pattern.
        if (email != null) email = email.toLowerCase(); // validate emil isn't null
        if (! pattern.matcher(email).matches())
            throw new EtAuthException("email is in the wrong format"); // valdate email match pattern
        Integer count = parentRepository.getCountByEmail(email);
        if (count > 0) throw new EtAuthException("Email already in use");
        UUID parentId = parentRepository.createParent(first_name, last_name, email, password);
        return parentRepository.findParentById(parentId);
    }

    @Override
    public List<Parent> getAllParents() {
        return parentRepository.getAllParents();
    }

    @Override
    public void deleteAllParents() {
        parentRepository.deleteAllParents();
    }
}
