package com.app.karapp;

import static com.app.karapp.tools.Constant.GET_CATEGORY;
import static com.app.karapp.tools.Constant.GET_PROVINCE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.module.GetCategory;
import com.app.karapp.module.GetProvince;
import com.app.karapp.tools.CustomProgressBar;

import java.util.ArrayList;
import java.util.List;

public class SearchPosts extends AppCompatActivity {
    Spinner s_province, s_category;
    Button btn_search;

    int[] province_ids;
    List<String> provinceList;
    int pr_id =0 ;
    int ProvinceID[] = new int[35];
    int pr_posit;
    String pr_name;

    int[] category_ids;
    List<String> categoryList;
    int cat_id =0 ;
    int categoryID[] = new int[6];
    int cat_posit;
    String cat_name;

    private String TAG = "SearchPosts";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_search_posts);

        s_category = findViewById (R.id.category);
        s_province = findViewById (R.id.province);
        btn_search = findViewById (R.id.searchbtn);

        ProvinceID[0] = 77;
        ProvinceID[1] = 78;//kabul
        ProvinceID[2] = 79;//kapisa
        ProvinceID[3] = 80;//parwan
        ProvinceID[4] = 81;//wardak
        ProvinceID[5] = 82;//logar
        ProvinceID[6] = 83;//nangarhar
        ProvinceID[7] = 84;//laghman
        ProvinceID[8] = 85;//panchsher
        ProvinceID[9] = 86;//baghlan
        ProvinceID[10] = 87;//bameyan
        ProvinceID[11] = 88;//ghazni
        ProvinceID[12] = 89;//paktika
        ProvinceID[13] = 90;//paktia
        ProvinceID[14] = 91;//khost
        ProvinceID[15] = 92;//konar
        ProvinceID[16] = 93;//norestan
        ProvinceID[17] = 94;//badakhsahn
        ProvinceID[18] = 95;//takhar
        ProvinceID[19] = 96;//kondoz
        ProvinceID[20] = 97;//samangan
        ProvinceID[21] = 98;//balkh
        ProvinceID[22] = 99;//sarpol
        ProvinceID[23] = 100;//orozgan
        ProvinceID[24] = 101;//daekonde
        ProvinceID[25] = 102;//orozgan
        ProvinceID[26] = 103;//zabol
        ProvinceID[27] = 104;//kandahar
        ProvinceID[28] = 105;//josjan
        ProvinceID[29] = 106;//faryab
        ProvinceID[30] = 107;//helmand
        ProvinceID[31] = 108;//badghis
        ProvinceID[32] = 109;//herat
        ProvinceID[33] = 110;//fara
        ProvinceID[34] = 111;//nemroz

        categoryID[0] = 0;
        categoryID[1] = 1;
        categoryID[2] = 2;
        categoryID[3] = 3;
        categoryID[4] = 4;
        categoryID[5] = 5;


        //province list
        provinceList = new ArrayList<String> ();
        provinceList.add ("ولایت");
        //calling the function of getting provinces data
        GetProvinces();
        //province spinner adapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String> (this,R.layout.spinner_item,provinceList ){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        s_province.setAdapter (spinnerArrayAdapter);

// province on item click lister
        s_province.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                pr_id = ProvinceID[position];

                pr_posit = position;
                pr_name  = (String) s_province.getSelectedItem ();

            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////////////////////////////
        //category list
        categoryList = new ArrayList<String> ();
        categoryList.add ("کتگوری");
        //calling the function of getting category data
        getCatgories();
        //category spinner adapter
        final ArrayAdapter<String> cat_spinnerArrayAdapter = new ArrayAdapter<String> (this,R.layout.spinner_item,categoryList ){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        s_category.setAdapter (cat_spinnerArrayAdapter);

// category on item click lister
        s_category.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                cat_id = categoryID[position];
                cat_posit = position;
             cat_name  = (String) s_category.getSelectedItem ();

            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });
/////////////////////////////////////press search btn///////////////////////////////
        btn_search.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (Validation ()){

                    Intent intent = new Intent (SearchPosts.this, PostsByProvinceAndCategory.class);
                    intent.putExtra ("p_id", pr_id);
                    intent.putExtra ("c_id", pr_id);
                    intent.putExtra ("type", "s");
                    intent.putExtra ("pname", pr_name);
                    intent.putExtra ("cname", cat_name);
                    startActivity (intent);
                }

            }
        });

        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        titlebar.setText("جستجو");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity (new Intent (ProvinceList.this, MainActivity.class));
                finish();
            }
        });

    }


    //get province function
    public void GetProvinces(){
        AndroidNetworking.get(GET_PROVINCE)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetProvince.class, new ParsedRequestListener<List<GetProvince>>() {
                    @Override

                    public void onResponse(List<GetProvince> getprovince) {
                        int j=0;
                        province_ids = new int[getprovince.get(0).getProvince ().size ()];
                        for (j=0; j<getprovince.get(0).getProvince ().size ();  j++){
                            provinceList.add(getprovince.get(0).getProvince ().get(j).getpName ()+"");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d(TAG, "ErrorDetail usertype : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                    }
                });
    }

    public void getCatgories(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (this);
        AndroidNetworking.get(GET_CATEGORY)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetCategory.class, new ParsedRequestListener<List<GetCategory>> () {
                    @Override
                    public void onResponse(List<GetCategory> getCategories) {
                        progressBar.dismiss();
                            int j=0;
                            category_ids = new int[getCategories.get(0).getCategory ().size ()];
                            for (j=0; j<getCategories.get(0).getCategory ().size ();  j++){
                                categoryList.add(getCategories.get(0).getCategory ().get(j).getCname ()+"");
                            }



                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d(TAG, "ErrorDetail usertype : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        progressBar.dismiss ();

                    }
                });
    }

    public boolean Validation(){
        if (pr_id == 77){
            Toast.makeText (this, "ولایت را انتخاب کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if(cat_id==0){
            Toast.makeText (this, "کتگوری را انتخاب کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }

        return true;
    }
}
