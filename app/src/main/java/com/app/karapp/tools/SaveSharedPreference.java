package com.app.karapp.tools;
import static com.app.karapp.tools.Constant.LASTNAME;
import static com.app.karapp.tools.Constant.LOGGED_IN_PREF;
import static com.app.karapp.tools.Constant.NAME;
import static com.app.karapp.tools.Constant.PASSWORD;
import static com.app.karapp.tools.Constant.PHONE_NUMBER;
import static com.app.karapp.tools.Constant.USERNAME;
import static com.app.karapp.tools.Constant.USER_ID;
import static com.app.karapp.tools.Constant.USER_PASSWORD;
import static com.app.karapp.tools.Constant.USER_PROFILE_PIC;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.karapp.module.User;

import java.util.ArrayList;
import java.util.List;

public class SaveSharedPreference {


    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static void setLoggedIn(Context context,int id,String name, String last_name, int phone, String image,String password, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(USER_ID, id);
        editor.putString(NAME, name);
        editor.putString (LASTNAME, last_name);
        editor.putInt (USERNAME, phone);
        editor.putString(USER_PROFILE_PIC, image);
        editor.putString (USER_PASSWORD, password);
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

//
    public static String getAuthInfo(Context context,  final String FIEDL) {
        return getPreferences(context).getString(FIEDL, null);
    }
    public static boolean islogin( Context context ) {

        return getPreferences( context ).getBoolean( LOGGED_IN_PREF, false );
    }

    public static List<User> getAuthInfo(Context context) {
        ArrayList<User> auth_user;
        auth_user = new ArrayList<User>();
        User user = new User();
        int id = getPreferences( context ).getInt( USER_ID, 0 );
        user.setId( id );
        user.setName( ""+getPreferences( context ).getString( NAME, null )  );
        user.setLast_name ( "" +getPreferences( context ).getString( LASTNAME, null ));
        user.setImage( "" + getPreferences( context ).getString( USER_PROFILE_PIC, null ) );
        user.setPassword (""+getPreferences (context).getString (PASSWORD, null));
        int phone = getPreferences (context).getInt (PHONE_NUMBER, 0);
        user.setPhone (phone);
        auth_user.add( user );
        return auth_user;
    }



}