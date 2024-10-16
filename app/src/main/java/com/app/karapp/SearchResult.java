package com.app.karapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.karapp.adapters.ShowPostsAdapter;
import com.app.karapp.module.UserDataAndPosts;

import java.util.ArrayList;


public class SearchResult extends AppCompatActivity {


    ArrayList<UserDataAndPosts> arrayList;
    ShowPostsAdapter ArrayAdapter;
    ListView post_ListView;
    int province_id = 0;
    TextView empty;
    LinearLayout emp_layout;
    ImageView emp_img;
    private String TAG = "PostByProvince";

    LinearLayout connectionerorshow;
    LinearLayout mainlayouthidel;

    String type;
    int category_id;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.listview);

    }
}
