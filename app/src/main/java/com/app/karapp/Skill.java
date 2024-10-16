package com.app.karapp;
import static com.app.karapp.tools.Constant.ADD_Skills;
import static com.app.karapp.tools.Constant.DELETE_Skills;
import static com.app.karapp.tools.Constant.GET_USER_SKILLS;
import static com.app.karapp.tools.Constant.UPDATE_Skills;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
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
import com.app.karapp.adapters.Skills_Adapter;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.User;
import com.app.karapp.module.UserDataAndPosts;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class Skill extends AppCompatActivity {


    int user_id;

    ArrayList<UserDataAndPosts> arrayList;
    Skills_Adapter ArrayAdapter;
    ListView post_ListView;
    TextView empty;
    LinearLayout emp_layout;
    ImageView emp_img;
    private String TAG = "Experiences";

    LinearLayout connectionerorshow;
    LinearLayout mainlayouthidel;

    boolean isSelected=false;
    String s_level;

    String u_skill;
    String u_level;
    int skill_id;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_skill);

        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        post_ListView = findViewById (R.id.listview);
        arrayList = new ArrayList<UserDataAndPosts> ();
        empty = findViewById (R.id.emp_text);
        emp_layout = findViewById (R.id.emp_layout);
        emp_img = findViewById (R.id.emp_img);

        connectionerorshow = findViewById (R.id.connectionerror);
        mainlayouthidel = findViewById (R.id.fcnerror);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(Skill.this);
        int p_user_id =   auth_user.get (0).getId ();

        Intent intent = getIntent();
        user_id = intent.getIntExtra ("user_id", 0);

        FloatingActionButton fab = findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                add_skills_alert();
            }
        });

        getUserExperiences();

        if (p_user_id!=user_id){
            fab.setVisibility (View.GONE);
        }else{
            post_ListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                @Override
                public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                    skill_id = arrayList.get (i).getS_id ();//post id
                    u_level = arrayList.get (i).getLevel ();
                    u_skill = arrayList.get (i).getSkill ();
                    choose_action_skills_alert();
                }
            });
        }




        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        titlebar.setText ("مهارت ها");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void getUserExperiences(){
        final CustomProgressBar progressBar  = new CustomProgressBar();
        progressBar.show (Skill.this);
        AndroidNetworking.get(GET_USER_SKILLS)
                .addPathParameter ("id", String.valueOf (user_id))
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
                                    getUserExperiences ();
                                }
                            });
                        }
                        for(int i=0;i<response.get(0).getPostData ().size();i++) {
                            UserDataAndPosts obj=new UserDataAndPosts();
                            obj.setS_id (response.get (0).getPostData ().get (i).getS_id ());
                            obj.setSkill (response.get (0).getPostData ().get (i).getSkill ());
                            obj.setLevel (response.get (0).getPostData ().get (i).getLevel ());
                            arrayList.add(obj);
                        }
                        ArrayAdapter =new Skills_Adapter ( Skill.this,R.layout.expereinces_data_item,arrayList );
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


    public void add_skills_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Skill.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_skill_dialog);

        BootstrapButton send = dialog.findViewById (R.id.submit);
        final BootstrapEditText title = dialog.findViewById (R.id.title);
        final BootstrapDropDown level = dialog.findViewById (R.id.spin2);

        level.setOnDropDownItemClickListener (new BootstrapDropDown.OnDropDownItemClickListener () {
            @Override
            public void onItemClick (ViewGroup parent, View v, int id) {
                String[] dropdownitem= level.getDropdownData();
                String mbshow = dropdownitem[id];
                s_level = mbshow;
                level.setText (mbshow);
                isSelected = true;
            }
        });

        send.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                if (title.getText ().toString ().equals ("")){
                    Toast.makeText (Skill.this, "عنوان مهارت را وارید کنید", Toast.LENGTH_SHORT).show ();
                }else if(!isSelected){
                    Toast.makeText (Skill.this, "سطح مهارت را انتخاب کنید", Toast.LENGTH_SHORT).show ();

                }else{
                    final CustomProgressBar progressBar = new CustomProgressBar();
                    progressBar.show (Skill.this);
                    AndroidNetworking.post (ADD_Skills)
                            .addBodyParameter ("user_id", String.valueOf (user_id))
                            .addBodyParameter ("skill", title.getText ().toString ())
                            .addBodyParameter ("level", s_level)
                            .setTag (this)
                            .setPriority (Priority.LOW)
                            .build ()
                            .getAsJSONArray (new JSONArrayRequestListener () {
                                @Override
                                public void onResponse (JSONArray response) {
                                    Toast.makeText (Skill.this, "موفقانه ثبت شد", Toast.LENGTH_SHORT).show ();
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
                                    Toast.makeText (Skill.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                                }

                            });

                    dialog.dismiss ();
                }



            }
        });
        dialog.show();
    }

    public void update_skills_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Skill.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_skill_dialog);

        BootstrapButton send = dialog.findViewById (R.id.submit);
        final BootstrapEditText title = dialog.findViewById (R.id.title);
        final BootstrapDropDown level = dialog.findViewById (R.id.spin2);
        TextView dialog_title = dialog.findViewById (R.id.dialog_title);

        send.setText ("ویرایش");
        dialog_title.setText ("ویرایش مهارت");

        title.setText (u_skill);
        level.setText (u_level);

        level.setOnDropDownItemClickListener (new BootstrapDropDown.OnDropDownItemClickListener () {
            @Override
            public void onItemClick (ViewGroup parent, View v, int id) {
                String[] dropdownitem= level.getDropdownData();
                String mbshow = dropdownitem[id];
                s_level = mbshow;
                level.setText (mbshow);
                isSelected = true;
            }
        });

        send.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                if (title.getText ().toString ().equals ("")){
                    Toast.makeText (Skill.this, "عنوان مهارت را وارید کنید", Toast.LENGTH_SHORT).show ();
                }else if(!isSelected){
                    Toast.makeText (Skill.this, "سطح مهارت را انتخاب کنید", Toast.LENGTH_SHORT).show ();

                }else{
                    final CustomProgressBar progressBar = new CustomProgressBar();
                    progressBar.show (Skill.this);
                    AndroidNetworking.post (UPDATE_Skills)
                            .addBodyParameter ("id", String.valueOf (skill_id))
                            .addBodyParameter ("skill", title.getText ().toString ())
                            .addBodyParameter ("level", s_level)
                            .setTag (this)
                            .setPriority (Priority.LOW)
                            .build ()
                            .getAsJSONArray (new JSONArrayRequestListener () {
                                @Override
                                public void onResponse (JSONArray response) {
                                    Toast.makeText (Skill.this, "موفقانه ثبت شد", Toast.LENGTH_SHORT).show ();
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
                                    Toast.makeText (Skill.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                                }

                            });

                    dialog.dismiss ();
                }



            }
        });
        dialog.show();
    }

    public void delete_skills_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Skill.this);
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
                    progressBar.show (Skill.this);
                    AndroidNetworking.get (DELETE_Skills)
                            .addPathParameter ("id", String.valueOf (skill_id))
                            .setTag (this)
                            .setPriority (Priority.LOW)
                            .build ()
                            .getAsJSONArray (new JSONArrayRequestListener () {
                                @Override
                                public void onResponse (JSONArray response) {
                                    Toast.makeText (Skill.this, "موفقانه حدف شد", Toast.LENGTH_SHORT).show ();
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
                                    Toast.makeText (Skill.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                                }

                            });

                    dialog.dismiss ();

            }
        });
        dialog.show();
    }

    public void choose_action_skills_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(Skill.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.action_du_dialog);

        LinearLayout delete = dialog.findViewById (R.id.delete_layout);
        LinearLayout update = dialog.findViewById (R.id.update_layout);

        delete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                delete_skills_alert ();
                dialog.dismiss ();
            }
        });

        update.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                update_skills_alert ();
                dialog.dismiss ();
            }
        });

        dialog.show();
    }


}
