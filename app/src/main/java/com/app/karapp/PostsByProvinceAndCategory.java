package com.app.karapp;

import static com.app.karapp.tools.Constant.POST_BY_CATEGORY;
import static com.app.karapp.tools.Constant.POST_BY_CP;
import static com.app.karapp.tools.Constant.POST_BY_PROVINCE;

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
import com.app.karapp.adapters.ShowPostsAdapter;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.UserDataAndPosts;
import com.app.karapp.tools.CustomProgressBar;

import java.util.ArrayList;
import java.util.List;

public class PostsByProvinceAndCategory extends AppCompatActivity {

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
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.listview);

        post_ListView = findViewById (R.id.listview);
        arrayList = new ArrayList<UserDataAndPosts> ();
        empty = findViewById (R.id.emp_text);
        emp_layout = findViewById (R.id.emp_layout);
        emp_img = findViewById (R.id.emp_img);

        connectionerorshow = findViewById (R.id.connectionerror);
        mainlayouthidel = findViewById (R.id.fcnerror);

        Intent intent = getIntent();
       type = intent.getStringExtra ("type");






        post_ListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                int p_id = arrayList.get (position).getId ();
                int u_id = arrayList.get (position).getUser_id ();
                Intent intent = new Intent (PostsByProvinceAndCategory.this, SinglePost.class);
                intent.putExtra ("p_id", p_id);
                startActivity (intent);
            }
        });

        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        if (type.equals ("p")){
            province_id =  intent.getIntExtra ("p_id", 0);
            String pName =  intent.getStringExtra ("province_name");
            getPostByProvince();
            titlebar.setText("جستجو در ولایت "+pName);
        }else if(type.equals ("c")){
            category_id = intent.getIntExtra ("c_id", 0);
            String cName =  intent.getStringExtra ("category_name");
            getPostByCategory ();
            titlebar.setText("جستجو در کتگوری "+cName);
        }else if (type.equals ("s")){
            province_id =  intent.getIntExtra ("p_id", 0);
            category_id = intent.getIntExtra ("c_id", 0);
            String province_name = intent.getStringExtra ("pname");
            String category_name = intent.getStringExtra ("cname");
            getPostByCategoryAndProvinc();
            titlebar.setText("جستجو در ولایت "+province_name+" و کتگوری "+category_name);
            Toast.makeText (this, ""+type, Toast.LENGTH_SHORT).show ();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getPostByProvince(){
//request to outcome data
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (PostsByProvinceAndCategory.this);
        AndroidNetworking.get(POST_BY_PROVINCE)
                .addPathParameter ("id", String.valueOf (province_id ))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>>() {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> response) {
                        progressBar.dismiss();
                        if (response.get(0).getPostData ().size()==0){
                            emp_layout.setVisibility (View.VISIBLE);
                            mainlayouthidel.setVisibility (View.GONE);

                            emp_layout.setOnClickListener (new View.OnClickListener () {
                                @Override
                                public void onClick (View v) {
                                    emp_layout.setVisibility (View.GONE);
                                    mainlayouthidel.setVisibility (View.VISIBLE);
                                    getPostByProvince ();
                                }
                            });
                        }
                        for(int i=0;i<response.get(0).getPostData ().size();i++) {
                            UserDataAndPosts obj=new UserDataAndPosts();
                            obj.setId (response.get (0).getPostData ().get (i).getId ());
                            obj.setName (response.get (0).getPostData ().get (i).getName ());
                            obj.setLast_name (response.get (0).getPostData ().get (i).getLast_name ());
                            obj.setImage (response.get (0).getPostData ().get (i).getImage ());
                            obj.setUser_id (response.get (0).getPostData ().get (i).getUser_id ());
                            obj.setJob (response.get (0).getPostData ().get (i).getJob ());
                            obj.setPhone (response.get (0).getPostData ().get (i).getPhone ());
                            obj.setTitle (response.get (0).getPostData ().get (i).getTitle ());
                            obj.setText (response.get (0).getPostData ().get (i).getText ());
                            obj.setU_image (response.get (0).getPostData ().get (i).getU_image ());
                            obj.setAdress (response.get (0).getPostData ().get (i).getAdress ());
                            obj.setCname (response.get (0).getPostData ().get (i).getCname ());
                            obj.setpName (response.get (0).getPostData ().get (i).getpName ());
                            arrayList.add(obj);
                        }
                        ArrayAdapter =new ShowPostsAdapter ( PostsByProvinceAndCategory.this,R.layout.home_data_item,arrayList );
                        post_ListView.setAdapter ( ArrayAdapter );
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "ErrorDetail : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        Log.d(TAG, "Message : " + anError.getMessage () );
                        progressBar.dismiss();

                        connectionerorshow.setVisibility (View.VISIBLE);
                        mainlayouthidel.setVisibility (View.GONE);

                        connectionerorshow.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v) {
                                getPostByProvince ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });

                    }
                });
    }

    public void getPostByCategory(){
//request to outcome data
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (PostsByProvinceAndCategory.this);
        AndroidNetworking.get(POST_BY_CATEGORY)
                .addPathParameter ("id", String.valueOf (category_id ))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> response) {
                        progressBar.dismiss();
                        if (response.get(0).getPostData ().size()==0){
                            emp_layout.setVisibility (View.VISIBLE);
                            mainlayouthidel.setVisibility (View.GONE);

                            emp_layout.setOnClickListener (new View.OnClickListener () {
                                @Override
                                public void onClick (View v) {
                                    emp_layout.setVisibility (View.GONE);
                                    mainlayouthidel.setVisibility (View.VISIBLE);
                                    getPostByCategory ();
                                }
                            });
                        }
                        for(int i=0;i<response.get(0).getPostData ().size();i++) {
                            UserDataAndPosts obj=new UserDataAndPosts();
                            obj.setId (response.get (0).getPostData ().get (i).getId ());
                            obj.setName (response.get (0).getPostData ().get (i).getName ());
                            obj.setLast_name (response.get (0).getPostData ().get (i).getLast_name ());
                            obj.setImage (response.get (0).getPostData ().get (i).getImage ());
                            obj.setUser_id (response.get (0).getPostData ().get (i).getUser_id ());
                            obj.setJob (response.get (0).getPostData ().get (i).getJob ());
                            obj.setPhone (response.get (0).getPostData ().get (i).getPhone ());
                            obj.setTitle (response.get (0).getPostData ().get (i).getTitle ());
                            obj.setText (response.get (0).getPostData ().get (i).getText ());
                            obj.setU_image (response.get (0).getPostData ().get (i).getU_image ());
                            obj.setAdress (response.get (0).getPostData ().get (i).getAdress ());
                            obj.setCname (response.get (0).getPostData ().get (i).getCname ());
                            obj.setpName (response.get (0).getPostData ().get (i).getpName ());
                            arrayList.add(obj);
                        }
                        ArrayAdapter =new ShowPostsAdapter ( PostsByProvinceAndCategory.this,R.layout.home_data_item,arrayList );
                        post_ListView.setAdapter ( ArrayAdapter );
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "ErrorDetail : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        Log.d(TAG, "Message : " + anError.getMessage () );
                        progressBar.dismiss();

                        connectionerorshow.setVisibility (View.VISIBLE);
                        mainlayouthidel.setVisibility (View.GONE);

                        connectionerorshow.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v) {
                                getPostByCategory ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });

                    }
                });
    }


    public void getPostByCategoryAndProvinc(){
//request to outcome data
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (PostsByProvinceAndCategory.this);
        AndroidNetworking.post(POST_BY_CP)
                .addBodyParameter ("cid", String.valueOf (category_id ))
                .addBodyParameter ("pid", String.valueOf (province_id ))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> response) {
                        progressBar.dismiss();
                        if (response.get(0).getPostData ().size()==0){
                            emp_layout.setVisibility (View.VISIBLE);
                            mainlayouthidel.setVisibility (View.GONE);

                            emp_layout.setOnClickListener (new View.OnClickListener () {
                                @Override
                                public void onClick (View v) {
                                    emp_layout.setVisibility (View.GONE);
                                    mainlayouthidel.setVisibility (View.VISIBLE);
                                    getPostByCategory ();
                                }
                            });
                        }
                        for(int i=0;i<response.get(0).getPostData ().size();i++) {
                            UserDataAndPosts obj=new UserDataAndPosts();
                            obj.setId (response.get (0).getPostData ().get (i).getId ());
                            obj.setName (response.get (0).getPostData ().get (i).getName ());
                            obj.setLast_name (response.get (0).getPostData ().get (i).getLast_name ());
                            obj.setImage (response.get (0).getPostData ().get (i).getImage ());
                            obj.setUser_id (response.get (0).getPostData ().get (i).getUser_id ());
                            obj.setJob (response.get (0).getPostData ().get (i).getJob ());
                            obj.setPhone (response.get (0).getPostData ().get (i).getPhone ());
                            obj.setTitle (response.get (0).getPostData ().get (i).getTitle ());
                            obj.setText (response.get (0).getPostData ().get (i).getText ());
                            obj.setU_image (response.get (0).getPostData ().get (i).getU_image ());
                            obj.setAdress (response.get (0).getPostData ().get (i).getAdress ());
                            obj.setCname (response.get (0).getPostData ().get (i).getCname ());
                            obj.setpName (response.get (0).getPostData ().get (i).getpName ());
                            arrayList.add(obj);
                        }
                        ArrayAdapter =new ShowPostsAdapter ( PostsByProvinceAndCategory.this,R.layout.home_data_item,arrayList );
                        post_ListView.setAdapter ( ArrayAdapter );
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "ErrorDetail : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        Log.d(TAG, "Message : " + anError.getMessage () );
                        progressBar.dismiss();

                        connectionerorshow.setVisibility (View.VISIBLE);
                        mainlayouthidel.setVisibility (View.GONE);

                        connectionerorshow.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v) {
                                getPostByCategory ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });

                    }
                });
    }

}
