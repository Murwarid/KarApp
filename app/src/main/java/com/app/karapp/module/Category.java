package com.app.karapp.module;

import java.io.Serializable;

public class Category implements Serializable {

    int id;
    String Cname;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getCname () {
        return Cname;
    }

    public void setCname (String cname) {
        Cname = cname;
    }
}
