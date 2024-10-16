package com.app.karapp;
import static com.app.karapp.tools.Constant.GET_USER_POSTS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class OtherUserProfilePost extends AppCompatActivity {

    ArrayList<UserDataAndPosts> arrayList;
    ShowPostsAdapter ArrayAdapter;
    ListView post_ListView;

    String title, text,image, province, category;
    private String TAG = "OtherUserProfilePost";

    LinearLayout connectionerorshow;
    LinearLayout mainlayouthidel;

    TextView empty;
    LinearLayout emp_layout;
    ImageView emp_img;

    int user_id;
    String p_name, p_lastname;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.listview);

        arrayList = new ArrayList<UserDataAndPosts> ();
        post_ListView = findViewById (R.id.listview);

        connectionerorshow = findViewById (R.id.connectionerror);
        mainlayouthidel = findViewById (R.id.fcnerror);

        empty = findViewById (R.id.emp_text);
        emp_layout = findViewById (R.id.emp_layout);
        emp_img = findViewById (R.id.emp_img);

        Intent intent = getIntent();
        user_id =  intent.getIntExtra ("user_id", 0);
        p_name = intent.getStringExtra ("user_name");
        p_lastname = intent.getStringExtra ("user_lastname");

        getPosts ();

        post_ListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                int p_id = arrayList.get (position).getId ();
                int u_id = arrayList.get (position).getUser_id ();
                Intent intent = new Intent (OtherUserProfilePost.this, SinglePost.class);
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
        titlebar.setText ("پست های "+p_name+" "+p_lastname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getPosts(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (OtherUserProfilePost.this);
        AndroidNetworking.get(GET_USER_POSTS)
                .addPathParameter ("id", String.valueOf (user_id))
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
                                    getPosts ();
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
                        ArrayAdapter =new ShowPostsAdapter (OtherUserProfilePost.this,R.layout.home_data_item,arrayList );
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
                                getPosts ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });

                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
