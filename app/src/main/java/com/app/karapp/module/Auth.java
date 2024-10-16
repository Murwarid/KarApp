package com.app.karapp.module;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Auth implements Serializable {

    @SerializedName("isSuccussfull")
    public boolean isSuccussfull;

    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccussfull() {
        return isSuccussfull;
    }

    public void setSuccussfull(boolean succussfull) {
        isSuccussfull = succussfull;
    }

}
