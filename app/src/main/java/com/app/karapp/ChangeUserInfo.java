package com.app.karapp;
import static com.app.karapp.tools.Constant.DELETE_ACCOUNT;
import static com.app.karapp.tools.Constant.GET_USER_DATA;
import static com.app.karapp.tools.Constant.UPDATE_USER_INFO;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.auth.Change_Password;
import com.app.karapp.auth.ui.login.LoginActivity;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.User;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ChangeUserInfo extends AppCompatActivity {

    BootstrapEditText edit_name, edit_lastname, edit_phone, edit_job;
    Spinner edit_province;
    BootstrapButton edit_btn, change_password_btn, delete_account_btn;

    String TAG = "ChangeUserInfo";
    String edit_image, edit_password;
    int p_user_id;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_change_user_info);

        edit_name = findViewById (R.id.edit_name);
        edit_lastname = findViewById (R.id.edit_last_name);
        edit_phone = findViewById (R.id.edit_phone);
        edit_job = findViewById (R.id.edit_job);
        edit_btn = findViewById (R.id.edit_btn);
        change_password_btn = findViewById (R.id.changePasword_btn);
        delete_account_btn = findViewById (R.id.delete_btn);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(ChangeUserInfo.this);
        p_user_id =   auth_user.get (0).getId ();

        Get_user_data();

        edit_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                editProfile();
            }
        });

        change_password_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                startActivity (new Intent (ChangeUserInfo.this, Change_Password.class));
            }
        });

        delete_account_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                delete_account_alert();
            }
        });

        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        titlebar.setText ("ویرایش اطلاعات شخصی");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void Get_user_data(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (ChangeUserInfo.this);
        AndroidNetworking.get(GET_USER_DATA)
                .addPathParameter ("id", String.valueOf (p_user_id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>>() {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> userDataAndPosts) {

                        edit_name.setText (userDataAndPosts.get (0).getPostData ().get (0).getName ());
                        edit_lastname.setText (userDataAndPosts.get (0).getPostData ().get (0).getLast_name ());
                        edit_phone.setText (userDataAndPosts.get (0).getPostData ().get (0).getPhone ()+"");
                        edit_job.setText (userDataAndPosts.get (0).getPostData ().get (0).getJob ());
                        edit_image = userDataAndPosts.get (0).getPostData ().get (0).getU_image ();
                        edit_password = userDataAndPosts.get (0).getPostData ().get (0).getPassword ();
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

    public void editProfile(){
        final CustomProgressBar progressBar = new CustomProgressBar ();
        progressBar.show (ChangeUserInfo.this);
        AndroidNetworking.post(UPDATE_USER_INFO)
                .addBodyParameter ("user_id",String.valueOf (p_user_id))
                .addBodyParameter ("name", edit_name.getText ().toString ())
                .addBodyParameter ("last_name", edit_lastname.getText ().toString ())
                .addBodyParameter ("phone", edit_phone.getText ().toString ())
                .addBodyParameter ("job", edit_job.getText ().toString ())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray (new JSONArrayRequestListener () {
                    @Override
                    public void onResponse (JSONArray response) {
                        progressBar.dismiss ();
                        Toast.makeText (ChangeUserInfo.this, "موفقانه ویرایش شد.", Toast.LENGTH_SHORT).show ();
                        SaveSharedPreference.setLoggedIn (ChangeUserInfo.this, 8, edit_name.getText ().toString (),edit_lastname.getText ().toString (),Integer.parseInt (edit_phone.getText ().toString ()),edit_image,edit_password, true);

                        finish();
                        startActivity(getIntent());
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

    public void delete_account_alert(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(ChangeUserInfo.this);
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
                progressBar.show (ChangeUserInfo.this);
                AndroidNetworking.get (DELETE_ACCOUNT)
                        .addPathParameter ("id", String.valueOf (p_user_id))
                        .setTag (this)
                        .setPriority (Priority.LOW)
                        .build ()
                        .getAsJSONArray (new JSONArrayRequestListener () {
                            @Override
                            public void onResponse (JSONArray response) {
                                Toast.makeText (ChangeUserInfo.this, "موفقانه حدف شد", Toast.LENGTH_SHORT).show ();
                                progressBar.dismiss ();
                                startActivity(new Intent (ChangeUserInfo.this, LoginActivity.class));
                                finish();

                            }

                            @Override
                            public void onError (ANError anError) {
                                Log.d (TAG, "ErrorDetail : " + anError.getErrorDetail ());
                                Log.d (TAG, "ErrorBody : " + anError.getErrorBody ());
                                Log.d (TAG, "ErrorCode : " + anError.getErrorCode ());
                                Log.d (TAG, "Message : " + anError.getMessage ());
                                progressBar.dismiss ();
                                Toast.makeText (ChangeUserInfo.this, "" + anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                            }

                        });

                dialog.dismiss ();

            }
        });
        dialog.show();
    }

}
