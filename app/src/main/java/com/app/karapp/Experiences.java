package com.app.karapp;
import static com.app.karapp.tools.Constant.ADD_EXPERIENCES;
import static com.app.karapp.tools.Constant.DELETE_EXPERIENCE;
import static com.app.karapp.tools.Constant.GET_USER_EXPERIENCES;
import static com.app.karapp.tools.Constant.UPDATE_EXPERIENCE;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.adapters.Experiences_Adapter;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.User;
import com.app.karapp.module.UserDataAndPosts;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Experiences extends AppCompatActivity {

    int user_id;

    ArrayList<UserDataAndPosts> arrayList;
    Experiences_Adapter ArrayAdapter;
    ListView post_ListView;
    TextView empty;
    LinearLayout emp_layout;
    ImageView emp_img;
    private String TAG = "Experiences";

    LinearLayout connectionerorshow;
    LinearLayout mainlayouthidel;

    String type;

    String s_extitle, s_duration;

    String u_title;
    int u_duration;

    int ex_id;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_experiences);

        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        post_ListView = findViewById (R.id.listview);
        arrayList = new ArrayList<UserDataAndPosts> ();
        empty = findViewById (R.id.emp_text);
        emp_layout = findViewById (R.id.emp_layout);
        emp_img = findViewById (R.id.emp_img);

        connectionerorshow = findViewById (R.id.connectionerror);
        mainlayouthidel = findViewById (R.id.fcnerror);


        Intent intent = getIntent();
        user_id = intent.getIntExtra ("user_id", 0);
        type = intent.getStringExtra ("type");

        Toast.makeText (this, ""+user_id, Toast.LENGTH_SHORT).show ();

        FloatingActionButton fab = findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                add_experiences_alert();
            }
        });

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(Experiences.this);
        int p_user_id =   auth_user.get (0).getId ();

        getUserExperiences();

        if (p_user_id!=user_id){
            fab.setVisibility (View.GONE);
        }else{
            post_ListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                @Override
                public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {

                    ex_id = arrayList.get (i).getEx_id ();//post id
                    u_title = arrayList.get (i).getExtitle ();
                    u_duration = arrayList.get (i).getDuration ();

                    choose_action_alert ();
                }
            });
        }


        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        titlebar.setText ("تجربه های کاری");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getUserExperiences(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (Experiences.this);
        AndroidNetworking.get(GET_USER_EXPERIENCES)
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
                                    getUserExperiences ();
                                }
                            });
                        }
                        for(int i=0;i<response.get(0).getPostData ().size();i++) {
                            UserDataAndPosts obj=new UserDataAndPosts();
                            obj.setEx_id (response.get (0).getPostData ().get (i).getEx_id ());
                            obj.setExtitle (response.get (0).getPostData ().get (i).getExtitle ());
                            obj.setDuration (response.get (0).getPostData ().get (i).getDuration ());
                            arrayList.add(obj);
                        }
                        ArrayAdapter =new Experiences_Adapter ( Experiences.this,R.layout.expereinces_data_item,arrayList );
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
                                getUserExperiences ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });

                    }
                });
    }


    public void add_experiences_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Experiences.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_experinces_dialog);

        BootstrapButton send = dialog.findViewById (R.id.submit);
        final BootstrapEditText title = dialog.findViewById (R.id.title);
        final BootstrapEditText duration = dialog.findViewById (R.id.duration);

        send.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Toast.makeText (Experiences.this, ""+title.getText ().toString (), Toast.LENGTH_SHORT).show ();
                final CustomProgressBar progressBar = new CustomProgressBar();
                progressBar.show (Experiences.this);

                if (title.getText ().toString ().equals ("")){
                    Toast.makeText (Experiences.this, "عنوان تجربه کاری را وارید کنید", Toast.LENGTH_SHORT).show ();
                }else if(duration.getText ().toString ().equals ("")){
                    Toast.makeText (Experiences.this, "مدت تجربه کاری را وارید کنید", Toast.LENGTH_SHORT).show ();

                }else{

                    AndroidNetworking.post (ADD_EXPERIENCES)
                            .addBodyParameter ("user_id", String.valueOf (8))
                            .addBodyParameter ("extitle", title.getText ().toString ())
                            .addBodyParameter ("duration", duration.getText ().toString ())
                            .setTag (this)
                            .setPriority (Priority.LOW)
                            .build ()
                            .getAsJSONArray (new JSONArrayRequestListener () {
                                @Override
                                public void onResponse (JSONArray response) {
                                    Toast.makeText (Experiences.this, "موفقانه ثبت شد", Toast.LENGTH_SHORT).show ();
                                    progressBar.dismiss ();

                                    finish();
                                    startActivity(getIntent());
                                }

                                @Override
                                public void onError (ANError anError) {
                                    Log.d (TAG, "ErrorDetail : " + anError.getErrorDetail ());
                                    Log.d (TAG, "ErrorBody : " + anError.getErrorBody ());
                                    Log.d (TAG, "ErrorCode : " + anError.getErrorCode ());
                                    Log.d (TAG, "Message : " + anError.getMessage ());
                                    progressBar.dismiss ();
                                    Toast.makeText (Experiences.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                                }



                            });

                }


                dialog.dismiss ();
            }
        });
        dialog.show();
    }

    public void update_experiences_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Experiences.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_experinces_dialog);

        BootstrapButton send = dialog.findViewById (R.id.submit);
        final BootstrapEditText title = dialog.findViewById (R.id.title);
        final BootstrapEditText duration = dialog.findViewById (R.id.duration);
        TextView dialog_title = dialog.findViewById (R.id.dialog_title);

        dialog_title.setText ("ویرایش تجربه کاری");

        send.setText ("ویرایش");

        title.setText (u_title);
        duration.setText (u_duration+"");


        send.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                if (title.getText ().toString ().equals ("")){
                    Toast.makeText (Experiences.this, "عنوان تجریه کاری را وارید کنید", Toast.LENGTH_SHORT).show ();
                }else if(title.getText ().toString ().equals ("")){
                    Toast.makeText (Experiences.this, "مدت تجربه کاری را انتخاب کنید", Toast.LENGTH_SHORT).show ();

                }else{
                    final CustomProgressBar progressBar = new CustomProgressBar();
                    progressBar.show (Experiences.this);
                    AndroidNetworking.post (UPDATE_EXPERIENCE)
                            .addBodyParameter ("id", String.valueOf (ex_id))
                            .addBodyParameter ("extitle", title.getText ().toString ())
                            .addBodyParameter ("duration", duration.getText ().toString ())
                            .setTag (this)
                            .setPriority (Priority.LOW)
                            .build ()
                            .getAsJSONArray (new JSONArrayRequestListener () {
                                @Override
                                public void onResponse (JSONArray response) {
                                    Toast.makeText (Experiences.this, "موفقانه ویرایش شد", Toast.LENGTH_SHORT).show ();
                                    progressBar.dismiss ();

                                    finish();
                                    startActivity(getIntent());
                                }

                                @Override
                                public void onError (ANError anError) {
                                    Log.d (TAG, "ErrorDetail : " + anError.getErrorDetail ());
                                    Log.d (TAG, "ErrorBody : " + anError.getErrorBody ());
                                    Log.d (TAG, "ErrorCode : " + anError.getErrorCode ());
                                    Log.d (TAG, "Message : " + anError.getMessage ());
                                    progressBar.dismiss ();
                                    Toast.makeText (Experiences.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                                }

                            });

                    dialog.dismiss ();
                }



            }
        });
        dialog.show();
    }

    public void delete_experiences_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Experiences.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_skill_dialog);

        BootstrapButton delete = dialog.findViewById (R.id.submit);
        BootstrapButton cancel = dialog.findViewById (R.id.cancel);

        cancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                dialog.dismiss ();
            }
        });

        delete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                final CustomProgressBar progressBar = new CustomProgressBar();
                progressBar.show (Experiences.this);
                AndroidNetworking.get (DELETE_EXPERIENCE)
                        .addPathParameter ("id", String.valueOf (ex_id))
                        .setTag (this)
                        .setPriority (Priority.LOW)
                        .build ()
                        .getAsJSONArray (new JSONArrayRequestListener () {
                            @Override
                            public void onResponse (JSONArray response) {
                                Toast.makeText (Experiences.this, "موفقانه حدف شد", Toast.LENGTH_SHORT).show ();
                                progressBar.dismiss ();

                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onError (ANError anError) {
                                Log.d (TAG, "ErrorDetail : " + anError.getErrorDetail ());
                                Log.d (TAG, "ErrorBody : " + anError.getErrorBody ());
                                Log.d (TAG, "ErrorCode : " + anError.getErrorCode ());
                                Log.d (TAG, "Message : " + anError.getMessage ());
                                progressBar.dismiss ();
                                Toast.makeText (Experiences.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                            }

                        });

                dialog.dismiss ();

            }
        });
        dialog.show();
    }

    public void choose_action_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Experiences.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.action_du_dialog);

        LinearLayout delete = dialog.findViewById (R.id.delete_layout);
        LinearLayout update = dialog.findViewById (R.id.update_layout);

        delete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                delete_experiences_alert ();
                dialog.dismiss ();
            }
        });

        update.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                update_experiences_alert ();
                dialog.dismiss ();
            }
        });

        dialog.show();
    }



}
