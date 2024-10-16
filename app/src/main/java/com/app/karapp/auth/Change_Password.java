package com.app.karapp.auth;
import static com.app.karapp.tools.Constant.CHANGE_PASSWORD;
import static com.app.karapp.tools.Constant.GET_PASSWORD;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.app.karapp.MainActivity;
import com.app.karapp.R;
import com.app.karapp.module.Login;
import com.app.karapp.module.User;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Change_Password extends AppCompatActivity {

    LinearLayout confirmlayout, newpasswordlayout;
    EditText oldpass, newpass, confirmpass;
    BootstrapButton saveverify, savenewpass;

    String TAG = "Change_Password";
    int user_id=0;
    int user_phone;
    TextView title_bar;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_chaange__password);

        saveverify = findViewById (R.id.confirmtonew);
        savenewpass = findViewById (R.id.save_newpassword);

        confirmlayout = findViewById (R.id.confirmpass);
        newpasswordlayout = findViewById (R.id.chagepssword);


        oldpass = findViewById (R.id.oldpassword);
        newpass = findViewById (R.id.newpassword);
        confirmpass = findViewById (R.id.confirmpassword);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(Change_Password.this);
        user_id = auth_user.get (0).getId ();
        user_phone = auth_user.get (0).getPhone ();

        savenewpass.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (newpass.getText ().toString ().equals (confirmpass.getText ().toString ())){
                    chageUserpassword();
                }else{
                    Toast.makeText (Change_Password.this, "تایید رمز عبور اشتباه میباشد!", Toast.LENGTH_SHORT).show ();
                }
            }
        });

        saveverify.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getUserpassword();
            }
        });

        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));

        View view =getSupportActionBar().getCustomView();

        ImageView back = findViewById(R.id.backarrowsingl);
        title_bar = findViewById(R.id.titlebarsingl);
        title_bar.setText ("تغیر رمز عبور");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void getUserpassword(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (Change_Password.this);
        AndroidNetworking.post(GET_PASSWORD)
                .addBodyParameter ("phone", String.valueOf (user_phone))
                .addBodyParameter ("password", oldpass.getText ().toString ())
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Login.class, new ParsedRequestListener<List<Login>>() {
                    @Override
                    public void onResponse(List<Login> response) {
                        progressBar.dismiss();
                        if (response.get (0).getAuth ().isSuccussfull){
                            confirmlayout.setVisibility (View.GONE);
                            newpasswordlayout.setVisibility (View.VISIBLE);

                        }else{
                            Toast.makeText (Change_Password.this, ""+response.get (0).getAuth ().getMessage (), Toast.LENGTH_SHORT).show ();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "ErrorDetail : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        Log.d(TAG, "Message : " + anError.getMessage () );
                        progressBar.dismiss();
                        Toast.makeText (Change_Password.this, ""+anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                    }
                });
    }

    public void chageUserpassword(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (Change_Password.this);
        AndroidNetworking.post(CHANGE_PASSWORD)
                .addBodyParameter ("id", String.valueOf (user_id))
                .addBodyParameter ("password", newpass.getText ().toString ())
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray (new JSONArrayRequestListener () {
                    @Override
                    public void onResponse (JSONArray response) {
                        Toast.makeText (Change_Password.this, "موفقانه انجام شد", Toast.LENGTH_SHORT).show ();
                        startActivity (new Intent (Change_Password.this, MainActivity.class));
                        finish ();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "ErrorDetail : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        Log.d(TAG, "Message : " + anError.getMessage () );
                        progressBar.dismiss();
                        Toast.makeText (Change_Password.this, ""+anError.getErrorDetail (), Toast.LENGTH_SHORT).show ();
                    }
                });
    }
}
