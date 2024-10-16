package com.app.karapp.ui.UserProfile;

import static com.app.karapp.tools.Constant.BASE_URL;
import static com.app.karapp.tools.Constant.COUNT_USER_POST;
import static com.app.karapp.tools.Constant.GET_USER_DATA;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.ChangeUserInfo;
import com.app.karapp.Experiences;
import com.app.karapp.R;
import com.app.karapp.UserPosts;
import com.app.karapp.auth.ui.login.LoginActivity;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.Skill;
import com.app.karapp.module.User;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    CardView about, skill, experiences, posts, edit_profile;

    private String TAG = "ProfileFragment";

    int user_id;
    String name, last_name, profile_img, province, job;
    int phone;

    TextView name_lastname, user_rate, post_count;
    CircleImageView profile_image;

    BootstrapButton logout;


    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of (this).get (NotificationsViewModel.class);
        View root = inflater.inflate (R.layout.fragment_notifications, container, false);

        about = root.findViewById (R.id.about);
        skill = root.findViewById (R.id.skill);
        experiences = root.findViewById (R.id.experiences);
        posts = root.findViewById (R.id.posts);
        edit_profile = root.findViewById (R.id.edit_profile);
        name_lastname = root.findViewById (R.id.namelastname);
        user_rate = root.findViewById (R.id.userrate);
        post_count = root.findViewById (R.id.postcount);
        profile_image = root.findViewById (R.id.profileimg);
        logout = root.findViewById (R.id.logout);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(getActivity ());
        user_id =   auth_user.get (0).getId ();
        name = auth_user.get (0).getName ();
        last_name = auth_user.get (0).getLast_name ();
        profile_img = auth_user.get (0).getImage ();
        job = auth_user.get (0).getJob ();
        phone = auth_user.get (0).getPhone ();

        Get_user_data();
        Count_user_post();

        edit_profile.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                startActivity (new Intent (getActivity (), ChangeUserInfo.class));
            }
        });

        experiences.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent  = new Intent (getActivity (), Experiences.class);
                intent.putExtra ("user_id", user_id);
                intent.putExtra ("type", "ex");
                startActivity (intent);
            }
        });

        skill.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent  = new Intent (getActivity (), Skill.class);
                intent.putExtra ("user_id", user_id);
                startActivity (intent);
            }
        });

        posts.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent  = new Intent (getActivity (), UserPosts.class);
                intent.putExtra ("user_id", user_id);
                startActivity (intent);
            }
        });

        about.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                user_info_alert();
            }
        });

        logout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SaveSharedPreference.setLoggedIn(getActivity (),0,"","",0, "","",false);
                startActivity(new Intent(getActivity (), LoginActivity.class));
                getActivity ().finish ();
            }
        });

        return root;
    }

    private void Get_user_data(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (getActivity ());
        AndroidNetworking.get(GET_USER_DATA)
                .addPathParameter ("id", String.valueOf (user_id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>>() {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> userDataAndPosts) {

                        name_lastname.setText (userDataAndPosts.get (0).getPostData ().get (0).getName ()+" "+userDataAndPosts.get (0).getPostData ().get (0).getLast_name ());

                        name = userDataAndPosts.get (0).getPostData ().get (0).getName ();
                        last_name  = userDataAndPosts.get (0).getPostData ().get (0).getLast_name ();
                        phone = userDataAndPosts.get (0).getPostData ().get (0).getPhone ();
                        job = userDataAndPosts.get (0).getPostData ().get (0).getJob ();
                        province = userDataAndPosts.get (0).getPostData ().get (0).getpName ();

                        float a = userDataAndPosts.get (0).getAverage ();

                        user_rate.setText (a+" ");

                        Glide.with(getActivity ())
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
                        Toast.makeText (getActivity (), "ratting: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show ();
                        progressBar.dismiss ();

                    }
                });
    }

    private void Count_user_post(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (getActivity ());
        AndroidNetworking.get(COUNT_USER_POST)
                .addPathParameter ("id", String.valueOf (user_id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> userDataAndPosts) {

                        post_count.setText (userDataAndPosts.get (0).getPostData ().get (0).getP_count ()+" ");

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
        Display display = getActivity ().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        final Dialog dialog = new Dialog(getActivity ());
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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
