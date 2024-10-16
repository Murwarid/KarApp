package com.app.karapp.ui.home;

import static com.app.karapp.tools.Constant.BASE_URL;
import static com.app.karapp.tools.Constant.GET_POSTS;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.Post;
import com.app.karapp.R;
import com.app.karapp.SinglePost;
import com.app.karapp.adapters.ShowPostsAdapter;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.User;
import com.app.karapp.module.UserDataAndPosts;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ArrayList<UserDataAndPosts> arrayList;
    ShowPostsAdapter ArrayAdapter;
    ListView post_ListView;

    String title, text,image, province, category;
    private String TAG = "HomeFragment";

    LinearLayout connectionerorshow;
    LinearLayout mainlayouthidel;

    TextView empty;
    LinearLayout emp_layout;
    ImageView emp_img;

    String img;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of (this).get (HomeViewModel.class);
        View root = inflater.inflate (R.layout.fragment_home, container, false);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(getActivity ());
        img = auth_user.get (0).getImage ();

        arrayList = new ArrayList<UserDataAndPosts> ();
        post_ListView = root.findViewById (R.id.post_listview);

        connectionerorshow = root.findViewById (R.id.connectionerror);
        mainlayouthidel = root.findViewById (R.id.fcnerror);

        empty = root.findViewById (R.id.emp_text);
        emp_layout = root.findViewById (R.id.emp_layout);
        emp_img = root.findViewById (R.id.emp_img);

        getPosts();

        post_ListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){

                }else{
                    int pos = i-1;
                    int p_id = arrayList.get (pos).getId ();//post id
                    int u_id = arrayList.get (pos).getUser_id ();//user id
                    Intent intent = new Intent (getActivity (), SinglePost.class);
                    intent.putExtra ("p_id", p_id);
                    Log.d (TAG, "p id: "+p_id+" u id"+u_id);
                    startActivity (intent);
                }

            }
        });

        return root;
    }

    public void getPosts(){
        //request to outcome data
        final CustomProgressBar progressBar  = new CustomProgressBar();
        progressBar.show (getActivity ());
        AndroidNetworking.get(GET_POSTS)
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
                            obj.setStatus (response.get (0).getPostData ().get (i).getStatus ());
                            obj.setF_id (response.get (0).getPostData ().get (i).getF_id ());
                            arrayList.add(obj);
                        }
                        ArrayAdapter =new ShowPostsAdapter (getActivity (),R.layout.home_data_item,arrayList );
                        post_ListView.setAdapter ( ArrayAdapter );

                        LayoutInflater inflater = getLayoutInflater();
                        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.home_header, post_ListView, false);
                        post_ListView.addHeaderView(header, null, false);
                        CircleImageView profile = header.findViewById (R.id.profilepic);

                        Glide.with(getActivity ())
                                .load(BASE_URL+img)
                                .into(profile);

                        header.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View view) {
                                Intent intent  = new Intent (getActivity (), Post.class);
                                intent.putExtra ("hint", "new");
                                startActivity (intent);
                            }
                        });

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
}
