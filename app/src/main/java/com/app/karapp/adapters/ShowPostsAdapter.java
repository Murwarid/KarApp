package com.app.karapp.adapters;
import static com.app.karapp.tools.Constant.BASE_URL;
import static com.app.karapp.tools.Constant.DELETE_FAVORITE;
import static com.app.karapp.tools.Constant.SAVE_FAVORITE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.app.karapp.R;
import com.app.karapp.module.Favorite;
import com.app.karapp.module.User;
import com.app.karapp.module.UserDataAndPosts;
import com.app.karapp.tools.SaveSharedPreference;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ShowPostsAdapter extends ArrayAdapter<UserDataAndPosts> {
    private final Activity context;
    ArrayList<UserDataAndPosts> arrayList;
    int isFavStatus = 0;
    int p_id = 0;
    ImageView favorate;
    String TAG = "ShowPostsAdapter";
    int user_id;
    int fav_id = 0;

    ImageView image;
    ImageView favorite;
    TextView title;
    TextView text;
    TextView province;
    TextView category;

    public ShowPostsAdapter (Activity context, int resource, ArrayList<UserDataAndPosts> objects) {
        super ( context , resource , objects );
        this.context=context;
        this.arrayList = objects;
    }

    @SuppressLint("InflateParams")

    @Override
    public View getView(final int position, View rowView, ViewGroup parent) {

        if (rowView==null){
            LayoutInflater inflater=context.getLayoutInflater();
            rowView=inflater.inflate(R.layout.home_data_item,parent,false);
        }
//        Animation aniSlide = AnimationUtils.loadAnimation(getContext (),R.anim.slid_up);
//        rowView.startAnimation (aniSlide);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(getContext ());
         user_id =   auth_user.get (0).getId ();


        image = rowView.findViewById (R.id.image);
        title = rowView.findViewById (R.id.title);
        text = rowView.findViewById (R.id.text);
        province = rowView.findViewById (R.id.province);
        category = rowView.findViewById (R.id.category);
        favorate = rowView.findViewById (R.id.favorite);


        final String p_title = arrayList.get (position).getTitle ();
        final String p_text = arrayList.get (position).getText ();
        final String p_image = arrayList.get (position).getImage ();
        final  String p_category = arrayList.get (position).getCname ();
        final  String p_pProvince = arrayList.get (position).getpName ();

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/2;
        int height = size.y;


        Glide.with(context)
                .load(BASE_URL+arrayList.get (position).getImage ())
                .into(image);

        if (arrayList.get (position).getStatus () >0){
            favorate.setImageResource(R.drawable.ic_favorite_red_24dp);

        }else{
            favorate.setImageResource(R.drawable.ic_favorite_border_redd_24dp);
        }

        title.setText (p_title+"");
        text.setText (p_text+"");
        category.setText (p_category);
        province.setText (p_pProvince+"");

        favorate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (arrayList.get (position).getStatus ()>0) {
                    favorate.setImageResource(R.drawable.ic_favorite_red_24dp);
                    arrayList.get (position).setStatus (0);
                    fav_id = arrayList.get (position).getF_id ();
                    Disfavorite (position, fav_id);
                } else{
                    favorate.setImageResource(R.drawable.ic_favorite_border_redd_24dp);
                    arrayList.get (position).setStatus (1);
                    favorite(position);
                }
                notifyDataSetChanged ();
            }
        });

        return rowView;
    }

    // share function
    public void sharedata(String text){
        Intent in = new Intent(Intent.ACTION_SEND);
        in.setType("text/plain");
        in.putExtra(in.EXTRA_TEXT, text);
        getContext ().startActivity(in.createChooser(in, "اشتراگ گذاری توسط: "));
    }

    @Override
    public void notifyDataSetChanged () {
        super.notifyDataSetChanged ();
    }
    // when click to favorite the post
    public void favorite(int pos){
        p_id = arrayList.get (pos).getId ();
        AndroidNetworking.post(SAVE_FAVORITE)
                .addBodyParameter ("p_id", String.valueOf (p_id))
                .addBodyParameter ("user_id", String.valueOf (user_id ))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Favorite.class, new ParsedRequestListener<List<Favorite>>() {
                    @Override
                    public void onResponse(List<Favorite> response) {
                        Toast.makeText (context, "liked", Toast.LENGTH_SHORT).show ();
                        arrayList.get (pos).setStatus (1);
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d (TAG,"message: "+error.getMessage ());
                        Log.d (TAG,"Detail: "+error.getErrorDetail ());
                        Log.d (TAG,"Body: "+error.getErrorBody ());
                    }
                });

    }

    // when click to favorite the post
    public void Disfavorite(int pos, int fa_id) {
        AndroidNetworking.get(DELETE_FAVORITE)
                .addPathParameter ("id", String.valueOf (fa_id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener () {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText (context, "Dis liked"+fa_id, Toast.LENGTH_SHORT).show ();
                        arrayList.get (pos).setStatus (0);
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d (TAG,"message: "+error.getMessage ());
                        Log.d (TAG,"Detail: "+error.getErrorDetail ());
                        Log.d (TAG,"Body: "+error.getErrorBody ());
                    }
                });
    }


}
