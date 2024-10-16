package com.app.karapp.tools;

import android.annotation.SuppressLint;
import android.app.Application;

import com.app.karapp.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
@SuppressLint("Registered")
public class DemoActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("Mj_Tunisia_Bd.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor (
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("Mj_Tunisia_Bd.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }
}
