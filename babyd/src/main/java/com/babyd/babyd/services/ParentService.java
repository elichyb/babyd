package com.babyd.babyd.services;

import com.babyd.babyd.exceptions.EtAuthException;
import com.babyd.babyd.models.Parent;

import java.util.List;

public interface ParentService {
    Parent validateParent(String email, String password) throws EtAuthException;

    Parent registerParent(String first_name, String last_name, String email, String password) throws EtAuthException;

    List<Parent> getAllParents();

    void deleteAllParents();
}
