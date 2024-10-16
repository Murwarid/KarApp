package com.app.karapp.module;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    String name;
    @SerializedName("last_name")
    String last_name;
    @SerializedName("phone")
    int phone;
    @SerializedName("province_id")
    int province_id;
    @SerializedName("password")
    String password;
    @SerializedName("image")
    String image;
    @SerializedName ("job")
    String job;


    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getLast_name () {
        return last_name;
    }

    public void setLast_name (String last_name) {
        this.last_name = last_name;
    }

    public int getPhone () {
        return phone;
    }

    public void setPhone (int phone) {
        this.phone = phone;
    }

    public int getProvince_id () {
        return province_id;
    }

    public void setProvince_id (int province_id) {
        this.province_id = province_id;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public String getJob () {
        return job;
    }

    public void setJob (String job) {
        this.job = job;
    }
}
