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

import com.babyd.babyd.exceptions.EtAuthException;
import com.babyd.babyd.models.Parent;

import java.util.List;
import java.util.UUID;

public interface ParentRepository {
    UUID createParent(String first_name, String last_name, String email, String phone, String password) throws EtAuthException;

    Parent findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    Parent findParentById(UUID id);

    List<Parent> getAllParents();

    void deleteAllParents();
}
