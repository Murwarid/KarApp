package com.app.karapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.karapp.R;
import com.app.karapp.module.Category;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Category> {

    private final Activity context;
    ArrayList<Category> arrayList;

    public CategoryListAdapter(Activity context,int resource, ArrayList<Category> objects) {
        super (context , resource , objects );
        this.context=context;
        this.arrayList = objects;
    }

    @SuppressLint("InflateParams")

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        if (rowView==null){
            LayoutInflater inflater=context.getLayoutInflater();
            rowView=inflater.inflate(R.layout.province_data_item, null,false);
        }
        Animation aniSlide = AnimationUtils.loadAnimation(getContext (), R.anim.slid_up);
        rowView.startAnimation (aniSlide);

        TextView province_name = rowView.findViewById (R.id.mtitle);
        province_name.setText (arrayList.get (position).getCname ());
        return rowView;
    }
}
