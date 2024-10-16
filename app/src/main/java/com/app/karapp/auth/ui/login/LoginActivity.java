package com.app.karapp.auth.ui.login;

import static com.app.karapp.tools.Constant.LOGIN;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.MainActivity;
import com.app.karapp.R;
import com.app.karapp.auth.Register;
import com.app.karapp.module.Login;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    TextView create_user_label;
    EditText username;
    EditText password;
    BootstrapButton login;
    String TAG = "LoginActivity";
    TextView loginmessage;
    CheckBox remember;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        boolean is_login = SaveSharedPreference.islogin(LoginActivity.this);

        if (is_login){
            startActivity (new Intent (LoginActivity.this, MainActivity.class));
            finish ();
        }

         username = findViewById (R.id.username);
         password = findViewById (R.id.password);
         login = findViewById (R.id.login);
         create_user_label = findViewById (R.id.register);
         loginmessage = findViewById (R.id.loginmessage);
         remember = findViewById (R.id.rememberme);

        create_user_label.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                startActivity (new Intent (LoginActivity.this, Register.class));
                finish ();
            }
        });

        login.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (Validate ()){
                    Login ();
                }
            }
        });
    }

    public boolean Validate(){

        if (username.getText ().toString ().equals ("")){
            Toast.makeText (this, "اسم کاربری را وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }
        else if(password.getText ().toString ().equals ("")){
            Toast.makeText (this, "رمز عبور را وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }
        return true;
    }

    private void Login(){
        final CustomProgressBar progressBar = new CustomProgressBar ();
        progressBar.show (LoginActivity.this);
        AndroidNetworking.post(LOGIN)
                .addBodyParameter("phone", username.getText ().toString ())
                .addBodyParameter("password", password.getText ().toString ())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(Login.class, new ParsedRequestListener<List<Login>>() {
                    @Override
                    public void onResponse(List<Login> logins) {
                        progressBar.dismiss ();
                        // Toast.makeText (LoginActivity.this, ""+logins.get (0).getUser ().getId (), Toast.LENGTH_SHORT).show ();
                        if (logins.get (0).getUser ().getId ()!=0){
                                if (remember.isChecked ()){
                                    SaveSharedPreference.setLoggedIn (LoginActivity.this, logins.get (0).getUser ().getId (), logins.get (0).getUser ().getName (),logins.get (0).getUser ().getLast_name (),logins.get (0).getUser ().getPhone (),logins.get (0).getUser ().getImage (),logins.get (0).getUser ().getPassword (), true);
                                }
                                SaveSharedPreference.setLoggedIn (LoginActivity.this, logins.get (0).getUser ().getId (), logins.get (0).getUser ().getName (),logins.get (0).getUser ().getLast_name (),logins.get (0).getUser ().getPhone (),logins.get (0).getUser ().getImage (),logins.get (0).getUser ().getPassword (), true);
                                Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                                startActivity (intent);
                                finish ();
                            }

                        else{
                            loginmessage.setText ("اسم کاربری و یا رمز عبور اشتباه میباشد");
                            password.setText ("");
                        }
                    }



                    @Override
                    public void onError(ANError error) {
                        Log.d (TAG,"Detail: "+error.getErrorDetail ());
                        Log.d (TAG,"Body: "+error.getErrorBody ());
                        Log.d (TAG,"message: "+error.getMessage ());
                        password.setText ("");
                        progressBar.dismiss ();
                    }
                });
    }

}
