package com.babyd.babyd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EtUnableConnectToDB extends RuntimeException {
    public EtUnableConnectToDB (String message) {
        super(message);
    }
}
