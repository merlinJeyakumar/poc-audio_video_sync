package com.domain.resmodels;

import java.io.Serializable;


public class BaseRes implements Serializable {

    private int status;
    private String message = "";

    public int getStatus() {
        return status;
    }

    public Boolean isSuccess() {
        return status == 200;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
