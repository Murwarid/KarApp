package com.app.karapp.module;

import java.io.Serializable;

public class Login implements Serializable {

    public Auth auth;

    public User user;

    public Auth getAuth () {
        return auth;
    }

    public void setAuth (Auth auth) {
        this.auth = auth;
    }

    public User getUser () {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }
}
