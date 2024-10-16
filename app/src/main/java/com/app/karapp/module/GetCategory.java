package com.app.karapp.module;

import java.util.List;

public class GetCategory {

    boolean isSuccussfull;
    List<Category> category;

    public boolean isSuccussfull () {
        return isSuccussfull;
    }

    public void setSuccussfull (boolean succussfull) {
        isSuccussfull = succussfull;
    }

    public List<Category> getCategory () {
        return category;
    }

    public void setCategory (List<Category> category) {
        this.category = category;
    }
}
