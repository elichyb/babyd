/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Elichy Barak 2020
 *
 *  The source code for this program is not published or other-
 * wise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * ----------------------------------------------------------------------------
 */

package com.babyd.babyd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EtUnableConnectToDB extends RuntimeException {
    public EtUnableConnectToDB (String message) {
        super(message);
    }
}
