package com.app.karapp;

import static com.app.karapp.tools.Constant.BASE_URL;
import static com.app.karapp.tools.Constant.DELETE_POST;
import static com.app.karapp.tools.Constant.REPORT_POST;
import static com.app.karapp.tools.Constant.SINGL_POST;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.User;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.app.karapp.ui.OtherUserProfile;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SinglePost extends AppCompatActivity {

    private String TAG = "SinglePost";
    int p_id;
    ImageView p_image;
    TextView p_title, p_text, p_adress, title_bar, p_catgory, p_phone, p_province;
    BootstrapButton p_share, p_userInfo, edit_post, delete_post, report_post;

    LinearLayout connectionerorshow,edit_layout,mainlayouthidel,emp_layout;

    TextView empty;
    ImageView emp_img;

    String s_title, s_province, s_detail, s_adress, s_category;
    int s_phone, user_id;
    int p_user_id;

    String pprovince, ccategory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_single_post);


        p_image = findViewById (R.id.image);
        p_title = findViewById (R.id.title);
        p_text = findViewById (R.id.text);
        p_share = findViewById (R.id.share);
        p_userInfo = findViewById (R.id.user_info);
        p_adress = findViewById (R.id.adress);
        p_catgory = findViewById (R.id.category);
        p_phone = findViewById (R.id.phone);
        p_province = findViewById (R.id.province);
        edit_post = findViewById (R.id.edit_post);
        delete_post = findViewById (R.id.delete_post);
        report_post = findViewById (R.id.report_post);

        connectionerorshow = findViewById (R.id.connectionerror);
        mainlayouthidel = findViewById (R.id.fcnerror);
        edit_layout = findViewById (R.id.edit_layout);

        empty = findViewById (R.id.emp_text);
        emp_layout = findViewById (R.id.emp_layout);
        emp_img = findViewById (R.id.emp_img);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(SinglePost.this);
        p_user_id =   auth_user.get (0).getId ();

        Intent intent = getIntent();
        p_id =  intent.getIntExtra ("p_id", 0);

        getSinglePost();

        p_share.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                sharedata ("عنوان کار: "+s_title+"/"+" ولایت: "+s_province+"/"+"جزییات: "+s_detail+"/"+" آدرس: "+s_adress);
            }
        });
        p_userInfo.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                    Intent intent  = new Intent (SinglePost.this, OtherUserProfile.class);
                    intent.putExtra ("user_id", user_id);
                    Toast.makeText (SinglePost.this, ""+user_id, Toast.LENGTH_SHORT).show ();
                    startActivity (intent);


            }
        });

        edit_post.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent  = new Intent (SinglePost.this, Post.class);
                intent.putExtra ("hint", "edit");
                intent.putExtra ("p_id", p_id);
                intent.putExtra ("pprovince", pprovince);
                intent.putExtra ("ccategory", ccategory);
                startActivity (intent);
            }
        });

        delete_post.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                delete_post_alert();
            }
        });

        report_post.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                report_post_alert();
            }
        });

        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view =getSupportActionBar().getCustomView();

        ImageView back = findViewById(R.id.backarrowsingl);
        title_bar = findViewById(R.id.titlebarsingl);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getSinglePost(){
        //request to outcome data
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (SinglePost.this);
        AndroidNetworking.get(SINGL_POST)
                .addPathParameter ("id", String.valueOf (p_id))
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
                                    getSinglePost ();
                                }
                            });
                        }

                        Glide.with(SinglePost.this)
                                .load(BASE_URL+response.get (0).getPostData ().get (0).getImage ())
                                .into(p_image);


                       s_title =  response.get (0).getPostData ().get (0).getTitle ();
                       s_detail = response.get (0).getPostData ().get (0).getText ();
                       s_adress = response.get (0).getPostData ().get (0).getAdress ();
                       s_category = response.get (0).getPostData ().get (0).getCname ();
                       s_phone = response.get (0).getPostData ().get (0).getPhone ();
                       s_province = response.get (0).getPostData ().get (0).getpName ();
                       user_id = response.get (0).getPostData ().get (0).getUser_id ();
                       pprovince = response.get (0).getPostData ().get (0).getpName ();
                       ccategory = response.get (0).getPostData ().get (0).getCname ();

                       if (user_id!=p_user_id){
                           edit_post.setVisibility (View.GONE);
                           delete_post.setVisibility (View.GONE);
                           edit_layout.setWeightSum (3);
                       }else{
                           p_userInfo.setVisibility (View.GONE);
                           report_post.setVisibility (View.GONE);
                           edit_layout.setWeightSum (3);
                       }
                        title_bar.setText (s_title+" ");
                        p_text.setText (s_detail+" ");
                        p_adress.setText (s_adress+"");
                        p_phone.setText (s_phone+"");
                        p_catgory.setText (s_category+"");
                        p_province.setText (s_province+" ");

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
                                getSinglePost ();
                                connectionerorshow.setVisibility (View.GONE);
                                mainlayouthidel.setVisibility (View.VISIBLE);
                            }
                        });

                    }
                });
    }
    // share function
    public void sharedata(String text){
        Intent in = new Intent(Intent.ACTION_SEND);
        in.setType("text/plain");
        in.putExtra(in.EXTRA_TEXT, text);
        startActivity(in.createChooser(in, "اشتراگ گذاری توسط: "));
    }

    public void delete_post_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(SinglePost.this);
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
                progressBar.show (SinglePost.this);
                AndroidNetworking.get (DELETE_POST)
                        .addPathParameter ("id", String.valueOf (p_id))
                        .setTag (this)
                        .setPriority (Priority.LOW)
                        .build ()
                        .getAsJSONArray (new JSONArrayRequestListener () {
                            @Override
                            public void onResponse (JSONArray response) {
                                Toast.makeText (SinglePost.this, "موفقانه حدف شد", Toast.LENGTH_SHORT).show ();
                                progressBar.dismiss ();
                                startActivity(new Intent (SinglePost.this, MainActivity.class));
                                finish();

                            }

                            @Override
                            public void onError (ANError anError) {
                                Log.d (TAG, "ErrorDetail : " + anError.getErrorDetail ());
                                Log.d (TAG, "ErrorBody : " + anError.getErrorBody ());
                                Log.d (TAG, "ErrorCode : " + anError.getErrorCode ());
                                Log.d (TAG, "Message : " + anError.getMessage ());
                                progressBar.dismiss ();
                                Toast.makeText (SinglePost.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                            }

                        });

                dialog.dismiss ();

            }
        });
        dialog.show();
    }

    public void report_post_alert(){
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(SinglePost.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_skill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));


        BootstrapButton send = dialog.findViewById (R.id.submit);
        final BootstrapEditText detail = dialog.findViewById (R.id.title);
        final BootstrapDropDown level = dialog.findViewById (R.id.spin2);
        final TextView title = dialog.findViewById (R.id.dialog_title);

        detail.setHintTextColor (Color.BLACK);
        detail.setTextColor (Color.BLACK);

        title.setText ("گزارش پست");
        level.setVisibility (View.GONE);
        detail.setHint ("جزییات...");

        send.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                if (detail.getText ().toString ().equals ("")){
                    Toast.makeText (SinglePost.this, "جزییات گزارش را وارید کنید", Toast.LENGTH_SHORT).show ();
                }else{
                    final CustomProgressBar progressBar = new CustomProgressBar();
                    progressBar.show (SinglePost.this);
                    AndroidNetworking.post (REPORT_POST)
                            .addBodyParameter ("user_report", String.valueOf (p_user_id))
                            .addBodyParameter ("p_id", String.valueOf (p_id))
                            .addBodyParameter ("detail", title.getText ().toString ())
                            .setTag (this)
                            .setPriority (Priority.LOW)
                            .build ()
                            .getAsJSONArray (new JSONArrayRequestListener () {
                                @Override
                                public void onResponse (JSONArray response) {
                                    Toast.makeText (SinglePost.this, "موفقانه ثبت شد", Toast.LENGTH_SHORT).show ();
                                    progressBar.dismiss ();
                                    dialog.dismiss ();
                                }

                                @Override
                                public void onError (ANError anError) {
                                    Log.d (TAG, "ErrorDetail : " + anError.getErrorDetail ());
                                    Log.d (TAG, "ErrorBody : " + anError.getErrorBody ());
                                    Log.d (TAG, "ErrorCode : " + anError.getErrorCode ());
                                    Log.d (TAG, "Message : " + anError.getMessage ());
                                    progressBar.dismiss ();
                                }

                            });

                    dialog.dismiss ();
                }



            }
        });
        dialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
