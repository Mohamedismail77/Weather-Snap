package com.application.weathersnap.utils;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ApiError {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private HashMap<String, String[]> errors;

    @SerializedName("error")
    @Expose
    private String error;



    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HashMap<String, String[]> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String[]> errors) {
        this.errors = errors;
    }

    public String getError() {
        return error;
    }

    public String[] getError(String key) {
        if(errors != null){
            String[] error = errors.get(key);
            return error;
        }
        return null;
    }

    public void setError(String error) {
        this.error = error;
    }

}
