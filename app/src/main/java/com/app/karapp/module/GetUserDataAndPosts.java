package com.app.karapp.module;

import java.util.List;

public class GetUserDataAndPosts {

    private  List<UserDataAndPosts> PostData;
    private float average;
    private int count;

    public List<UserDataAndPosts> getPostData () {
        return PostData;
    }

    public void setPostData (List<UserDataAndPosts> postData) {
        PostData = postData;
    }

    public float getAverage () {
        return average;
    }

    public void setAverage (float average) {
        this.average = average;
    }

    public int getCount () {
        return count;
    }

    public void setCount (int count) {
        this.count = count;
    }
}
