package com.app.karapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.app.karapp.R;
import com.app.karapp.module.UserDataAndPosts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Skills_Adapter extends ArrayAdapter<UserDataAndPosts> {


    private final Activity context;
    ArrayList<UserDataAndPosts> arrayList;

    public Skills_Adapter (Activity context, int resource, ArrayList<UserDataAndPosts> objects) {
        super (context , resource , objects );
        this.context=context;
        this.arrayList = objects;
    }

    @NotNull
    @SuppressLint("InflateParams")

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        if (rowView==null){
            LayoutInflater inflater=context.getLayoutInflater();
            rowView=inflater.inflate(R.layout.expereinces_data_item, null,false);
        };

        TextView title = rowView.findViewById (R.id.title);
        TextView level = rowView.findViewById (R.id.subtitle);

        String s_level = null;
        if(arrayList.get (position).getLevel ().equals ("high")){
            s_level = "عالی";
        }else if(arrayList.get (position).getLevel ().equals ("midum")){
            s_level = "متوسط";
        }else if(arrayList.get (position).getLevel ().equals ("low")){
            s_level = "خوب";
        }
        
        title.setText (arrayList.get (position).getSkill ()+"");

        level.setText (s_level);
        return rowView;
    }



}
