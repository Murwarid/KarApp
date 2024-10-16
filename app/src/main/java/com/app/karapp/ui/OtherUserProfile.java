package com.app.karapp.ui;

import static com.app.karapp.tools.Constant.BASE_URL;
import static com.app.karapp.tools.Constant.CHECK_RATING;
import static com.app.karapp.tools.Constant.COUNT_RATING;
import static com.app.karapp.tools.Constant.GET_USER_DATA;
import static com.app.karapp.tools.Constant.REPORT_USER;
import static com.app.karapp.tools.Constant.SAVE_RATING_VALUE;
import static com.app.karapp.tools.Constant.UPDATE_RATING_VALUE;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.ChangeUserInfo;
import com.app.karapp.Experiences;
import com.app.karapp.OtherUserProfilePost;
import com.app.karapp.R;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.Skill;
import com.app.karapp.module.User;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OtherUserProfile extends AppCompatActivity {

    CardView about, skill, experiences, posts;
    BootstrapButton edit_profile;

    private String TAG = "OtherUserProfile";

    int user_id,logined_user_id;
    String name, last_name, profile_img, province, job, p_name, p_lastname;
    int phone;

    TextView name_lastname, user_rate, post_count;
    CircleImageView profile_image;
    MaterialRatingBar rate_user;
    BootstrapButton save_rate;

    int star1, star2, star3, star4, star5, totel_rating_star;
    float rate_value;
    int rate_count;
    int ratting_id;

    TextView t_average, t_num_of_ratting;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_other_user_profile);

        about = findViewById (R.id.about);
        skill = findViewById (R.id.skill);
        experiences = findViewById (R.id.experiences);
        posts = findViewById (R.id.posts);
        name_lastname = findViewById (R.id.namelastname);
        user_rate = findViewById (R.id.userrate);
        post_count = findViewById (R.id.postcount);
        profile_image = findViewById (R.id.profileimg);
        edit_profile = findViewById (R.id.btn_edit_profile);
        rate_user = findViewById (R.id.md_rate_review);
        save_rate = findViewById (R.id.save_rate);
        t_average = findViewById (R.id.average);
        t_num_of_ratting = findViewById (R.id.num_of_ratting);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(OtherUserProfile.this);
        logined_user_id =   auth_user.get (0).getId ();

        Intent intent = getIntent();
        user_id =  intent.getIntExtra ("user_id", 0);

        if (logined_user_id!=user_id){
            edit_profile.setText ("گزارش");
        }

        Get_user_data();
        Count_rating();

        //doing rate
        totel_rating_star = rate_user.getNumStars();

        experiences.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent  = new Intent (OtherUserProfile.this, Experiences.class);
                intent.putExtra ("user_id", user_id);
                intent.putExtra ("type", "ex");
                startActivity (intent);
            }
        });

        skill.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent  = new Intent (OtherUserProfile.this, Skill.class);
                intent.putExtra ("user_id", user_id);
                startActivity (intent);
            }
        });

        posts.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent  = new Intent (OtherUserProfile.this, OtherUserProfilePost.class);
                intent.putExtra ("user_id", user_id);
                intent.putExtra ("user_name", name);
                intent.putExtra ("user_lastname", last_name);
                startActivity (intent);
            }
        });

        about.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                user_info_alert();
            }
        });

        edit_profile.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                edit_profile.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View view) {
                        if (logined_user_id!=user_id){
                            edit_profile.setText ("گزارش");
                            //report function
                            report_user_alert();

                        }else {
                            startActivity (new Intent (OtherUserProfile.this, ChangeUserInfo.class));
                        }

                    }
                });
            }
        });

    }

    private void Get_user_data(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (OtherUserProfile.this);
        AndroidNetworking.get(GET_USER_DATA)
                .addPathParameter ("id", String.valueOf (user_id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> userDataAndPosts) {

                        name_lastname.setText (userDataAndPosts.get (0).getPostData ().get (0).getName ()+" "+userDataAndPosts.get (0).getPostData ().get (0).getLast_name ());

                        name = userDataAndPosts.get (0).getPostData ().get (0).getName ();
                        last_name  = userDataAndPosts.get (0).getPostData ().get (0).getLast_name ();
                        phone = userDataAndPosts.get (0).getPostData ().get (0).getPhone ();
                        job = userDataAndPosts.get (0).getPostData ().get (0).getJob ();
                        province = userDataAndPosts.get (0).getPostData ().get (0).getpName ();

                        Glide.with(OtherUserProfile.this)
                                .load(BASE_URL+userDataAndPosts.get (0).getPostData ().get (0).getU_image ())
                                .into(profile_image);

                        progressBar.dismiss();

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

    public void user_info_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(OtherUserProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.about_user_info_dialog);

        TextView t_name_lastname = dialog.findViewById (R.id.namelastname);
        TextView t_phone = dialog.findViewById (R.id.phone);
        TextView t_job = dialog.findViewById (R.id.job);
        TextView t_province = dialog.findViewById (R.id.province);

        t_name_lastname.setText (name+" "+ last_name);
        t_phone.setText (phone+"");
        t_job.setText (job+"");
        t_province.setText (province+"");

        dialog.show();
    }
    //count ratting
    private void Count_rating(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (OtherUserProfile.this);
        AndroidNetworking.post(COUNT_RATING)
                .addBodyParameter ("user_id", String.valueOf (user_id))
                .addBodyParameter ("user_id_r", String.valueOf (logined_user_id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> userDataAndPosts) {

                        rate_count = userDataAndPosts.get (0).getPostData ().get (0).getR_count ();

                        if (rate_count!=0){
                            final CustomProgressBar progressBar  = new CustomProgressBar ();
                            progressBar.show (OtherUserProfile.this);
                            AndroidNetworking.post(CHECK_RATING)
                                    .addBodyParameter ("user_id", String.valueOf (user_id))
                                    .addBodyParameter ("user_id_r", String.valueOf (logined_user_id))
                                    .setTag(this)
                                    .setPriority(Priority.LOW)
                                    .build()
                                    .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                                        @Override
                                        public void onResponse(List<GetUserDataAndPosts> userDataAndPosts) {

                                            rate_count = userDataAndPosts.get (0).getPostData ().get (0).getR_count ();
                                            rate_value = userDataAndPosts.get (0).getPostData ().get (0).getValue ();

                                            ratting_id = userDataAndPosts.get (0).getPostData ().get (0).getR_id ();

                                            rate_user.setRating (rate_value);
                                            t_average.setText (userDataAndPosts.get (0).getAverage ()+"");
                                            t_num_of_ratting.setText (userDataAndPosts.get (0).getCount ()+"");


                                            progressBar.dismiss();

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

                            save_rate.setText ("بروز رسانی");

                            save_rate.setOnClickListener (new View.OnClickListener () {
                                @Override
                                public void onClick (View view) {
                                    UpdateRatingValue (ratting_id);
                                }
                            });
                        }else{

                            save_rate.setOnClickListener (new View.OnClickListener () {
                                @Override
                                public void onClick (View view) {
                                    Toast.makeText (OtherUserProfile.this, ""+rate_user.getRating (), Toast.LENGTH_SHORT).show ();
                                    SaveRatingValue();
                                }
                            });

                            Toast.makeText (OtherUserProfile.this, "You hav not rate the user", Toast.LENGTH_SHORT).show ();
                        }

                        progressBar.dismiss();

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
// check rating
    private void Get_user_rating(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (OtherUserProfile.this);
        AndroidNetworking.post(CHECK_RATING)
                .addBodyParameter ("user_id", String.valueOf (user_id))
                .addBodyParameter ("user_id_r", String.valueOf (logined_user_id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> userDataAndPosts) {

                        rate_count = userDataAndPosts.get (0).getPostData ().get (0).getR_count ();
                        rate_value = userDataAndPosts.get (0).getPostData ().get (0).getValue ();

                        ratting_id = userDataAndPosts.get (0).getPostData ().get (0).getR_id ();

                            rate_user.setRating (rate_value);

                        progressBar.dismiss();

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

    public void SaveRatingValue(){
        final CustomProgressBar progressBar = new CustomProgressBar ();
        progressBar.show (OtherUserProfile.this);
        AndroidNetworking.post(SAVE_RATING_VALUE)
                .addBodyParameter ("user_id",String.valueOf (user_id))
                .addBodyParameter ("user_id_r", String.valueOf (logined_user_id))
                .addBodyParameter ("value", String.valueOf (rate_user.getRating ()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray (new JSONArrayRequestListener () {
                    @Override
                    public void onResponse (JSONArray response) {
                        progressBar.dismiss ();

                        save_rate.setText ("بروز رسانی");
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d (TAG,"message: "+error.getMessage ());
                        Log.d (TAG,"Detail: "+error.getErrorDetail ());
                        Log.d (TAG,"Body: "+error.getErrorBody ());
                        progressBar.dismiss ();
                    }
                });

    }

    public void UpdateRatingValue(int rate_id){
        final CustomProgressBar progressBar = new CustomProgressBar ();
        progressBar.show (OtherUserProfile.this);
        AndroidNetworking.post(UPDATE_RATING_VALUE)
                .addBodyParameter ("id",String.valueOf (rate_id))
                .addBodyParameter ("value",String.valueOf (rate_user.getRating ()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray (new JSONArrayRequestListener () {
                    @Override
                    public void onResponse (JSONArray response) {
                        progressBar.dismiss ();

                        Toast.makeText (OtherUserProfile.this, "update successfully: "+rate_id+" "+rate_user.getRating (), Toast.LENGTH_SHORT).show ();
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d (TAG,"message: "+error.getMessage ());
                        Log.d (TAG,"Detail: "+error.getErrorDetail ());
                        Log.d (TAG,"Body: "+error.getErrorBody ());
                        progressBar.dismiss ();
                    }
                });

    }


    public void report_user_alert(){
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(OtherUserProfile.this);
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

        title.setText ("گزارش کاربر");
        level.setVisibility (View.GONE);
        detail.setHint ("جزییات...");

        send.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                if (detail.getText ().toString ().equals ("")){
                    Toast.makeText (OtherUserProfile.this, "جزییات گزارش را وارید کنید", Toast.LENGTH_SHORT).show ();
                }else{
                    final CustomProgressBar progressBar = new CustomProgressBar();
                    progressBar.show (OtherUserProfile.this);
                    AndroidNetworking.post (REPORT_USER)
                            .addBodyParameter ("user_report", String.valueOf (logined_user_id))
                            .addBodyParameter ("user_reported", String.valueOf (user_id))
                            .addBodyParameter ("detail", title.getText ().toString ())
                            .setTag (this)
                            .setPriority (Priority.LOW)
                            .build ()
                            .getAsJSONArray (new JSONArrayRequestListener () {
                                @Override
                                public void onResponse (JSONArray response) {
                                    Toast.makeText (OtherUserProfile.this, "موفقانه ثبت شد", Toast.LENGTH_SHORT).show ();
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
