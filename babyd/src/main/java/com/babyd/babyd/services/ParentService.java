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

import com.babyd.babyd.exceptions.EtAuthException;
import com.babyd.babyd.models.Parent;

import java.util.List;

public interface ParentService {
    Parent validateParent(String email, String password) throws EtAuthException;

    Parent registerParent(String first_name, String last_name, String email, String phone, String password) throws EtAuthException;

    List<Parent> getAllParents();

    void deleteAllParents();
}
