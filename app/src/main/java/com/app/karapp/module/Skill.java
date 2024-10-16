package com.app.karapp.module;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Skill implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("skill")
    String skill;
    @SerializedName("level")
    String level;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getSkill () {
        return skill;
    }

    public void setSkill (String skill) {
        this.skill = skill;
    }

    public String getLevel () {
        return level;
    }

    public void setLevel (String level) {
        this.level = level;
    }
}
