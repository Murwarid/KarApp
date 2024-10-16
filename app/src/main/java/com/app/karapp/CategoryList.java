package com.app.karapp;
import static com.app.karapp.tools.Constant.GET_CATEGORY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.adapters.CategoryListAdapter;
import com.app.karapp.module.Category;
import com.app.karapp.module.GetCategory;
import com.app.karapp.tools.CustomProgressBar;

import java.util.ArrayList;
import java.util.List;

public class CategoryList extends AppCompatActivity {

    private static final String TAG = "CategoryList";
    ListView provineListview;
    ArrayList<Category> arrayList;
    CategoryListAdapter adapter;
    String provinceString;

    LinearLayout connectionerorshow;
    LinearLayout mainlayouthidel;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_province_list);
        provineListview = findViewById (R.id.provincelistview);
        arrayList = new ArrayList<Category> ();

        connectionerorshow = findViewById (R.id.connectionerror);
        mainlayouthidel = findViewById (R.id.fcnerror);

        getCatgories();

        provineListview.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int i, long id) {
                int c_id = arrayList.get (i).getId ();//post id
                String c_name = arrayList.get (i).getCname ();
                Intent intent = new Intent (CategoryList.this, PostsByProvinceAndCategory.class);
                intent.putExtra ("c_id", c_id);
                intent.putExtra ("category_name", c_name);
                intent.putExtra ("type", "c");
                Toast.makeText (CategoryList.this, ""+c_id, Toast.LENGTH_SHORT).show ();
                startActivity (intent);
            }
        });


        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        titlebar.setText("جستجو در کتگوری");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity (new Intent (ProvinceList.this, MainActivity.class));
                finish();
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
                .getAsObjectList(GetCategory.class, new ParsedRequestListener<List<GetCategory>>() {
                    @Override
                    public void onResponse(List<GetCategory> getCategories) {
                        progressBar.dismiss();
                        for(int i=0;i<getCategories.get (0).getCategory ().size();i++) {
                            Category obj=new Category ();
                            obj.setCname (getCategories.get (0).getCategory ().get (i).getCname ());
                            obj.setId (getCategories.get (0).getCategory ().get (i).getId ());
                            arrayList.add(obj);
//                            Log.d (TAG, "pro: "+getprovince.get (0).getProvince ().get (i).getpName ());
                            provinceString = getCategories.get (0).getCategory ().get (i).getCname ();
                        }
                        adapter =new CategoryListAdapter (CategoryList.this, R.layout.province_data_item,arrayList);
                        provineListview.setAdapter ( adapter );

                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d(TAG, "ErrorDetail usertype : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        progressBar.dismiss ();

                        connectionerorshow.setVisibility (View.VISIBLE);
                        mainlayouthidel.setVisibility (View.GONE);

                        connectionerorshow.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v) {
                                getCatgories ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });
                    }
                });
    }

}
