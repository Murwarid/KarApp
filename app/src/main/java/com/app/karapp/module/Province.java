package com.app.karapp.module;

import java.io.Serializable;

public class Province implements Serializable {

    int id;
    String pName;
    String pNumber;


    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getpName () {
        return pName;
    }

    public void setpName (String pName) {
        this.pName = pName;
    }

    public String getpNumber () {
        return pNumber;
    }

    public void setpNumber (String pNumber) {
        this.pNumber = pNumber;
    }
}
