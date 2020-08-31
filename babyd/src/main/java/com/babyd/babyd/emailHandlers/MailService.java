/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Elichy Barak 2020
 *
 *  The source code for this program is not published or other-
 * wise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * ----------------------------------------------------------------------------
 */

package com.babyd.babyd.emailHandlers;

public interface MailService {
    void sendMailRegisterSuccessfully(String to, String name);
    void sendMailBabyAdded(String to, String baby_name);
}
