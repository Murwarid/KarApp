package com.app.karapp;
import static com.app.karapp.tools.Constant.GET_PROVINCE;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.adapters.ProvinceListAdapter;
import com.app.karapp.module.GetProvince;
import com.app.karapp.module.Province;
import com.app.karapp.tools.CustomProgressBar;

import java.util.ArrayList;
import java.util.List;


public class ProvinceList extends AppCompatActivity {
    private static final String TAG = "ProvinceList";
    ListView provineListview;
    ArrayList<Province> arrayList;
    ProvinceListAdapter adapter;
    String provinceString;

    LinearLayout connectionerorshow;
    LinearLayout mainlayouthidel;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_province_list);
        provineListview = findViewById (R.id.provincelistview);
        arrayList = new ArrayList<Province> ();

        connectionerorshow = findViewById (R.id.connectionerror);
        mainlayouthidel = findViewById (R.id.fcnerror);

        getProvince();

        provineListview.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int i, long id) {
                int pr_id = arrayList.get (i).getId ();//post id
                String pr_name = arrayList.get (i).getpName ();
                Intent intent = new Intent (ProvinceList.this, PostsByProvinceAndCategory.class);
                intent.putExtra ("p_id", pr_id);
                intent.putExtra ("province_name", pr_name);
                intent.putExtra ("type", "p");
                Toast.makeText (ProvinceList.this, ""+pr_id, Toast.LENGTH_SHORT).show ();
                startActivity (intent);
            }
        });

        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        titlebar.setText("جستجو به اساس ولایت");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity (new Intent (ProvinceList.this, MainActivity.class));
                finish();
            }
        });

    }

    public void getProvince(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (this);
        AndroidNetworking.get(GET_PROVINCE)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetProvince.class, new ParsedRequestListener<List<GetProvince>>() {
                    @Override
                    public void onResponse(List<GetProvince> getprovince) {
                        progressBar.dismiss();
                        for(int i=0;i<getprovince.get (0).getProvince ().size();i++) {
                           Province obj=new Province();
                            obj.setpName (getprovince.get (0).getProvince ().get (i).getpName ());
                            obj.setId (getprovince.get (0).getProvince ().get (i).getId ());
                            arrayList.add(obj);
//                            Log.d (TAG, "pro: "+getprovince.get (0).getProvince ().get (i).getpName ());
                            provinceString = getprovince.get (0).getProvince ().get (i).getpName ();
                        }
                        adapter =new ProvinceListAdapter (ProvinceList.this, R.layout.province_data_item,arrayList);
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
                                getProvince ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });
                    }
                });
    }

    public void getId(int pos){
        int id =  arrayList.get (pos).getId ();
        String provice = arrayList.get (pos).getpName ();
        Intent intent = new Intent (ProvinceList.this, PostsByProvinceAndCategory.class);
        intent.putExtra ("province_id", id);
        intent.putExtra ("province_name", provice);
        startActivity (intent);

    }

    @Override
    public void onBackPressed () {
        super.onBackPressed ();
        startActivity (new Intent (ProvinceList.this, MainActivity.class));
        finish();
    }
}
