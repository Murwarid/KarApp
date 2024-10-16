package com.app.karapp.module;

import java.util.List;

public class GetProvince {

    boolean isSuccussfull;
    List<Province> province;

    public boolean isSuccussfull () {
        return isSuccussfull;
    }

    public void setSuccussfull (boolean succussfull) {
        isSuccussfull = succussfull;
    }

    public List<Province> getProvince () {
        return province;
    }

    public void setProvince (List<Province> province) {
        this.province = province;
    }
}
