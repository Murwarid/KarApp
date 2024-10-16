package com.app.karapp.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

import java.lang.reflect.Field;

public class TypefaceUtil {



    /**
     *  TypefaceUtil.overrideFont(MainActivity.this, "SERIF", "fonts/Mj_Tunisia_Bd.ttf");
     *  <item name="android:typeface">serif</item>
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(defaultFontNameToOverride, customFontTypeface);
        } catch (Exception e) {
//            Log.d("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
            Toast.makeText(context,"Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride,Toast.LENGTH_LONG).show();
        }
    }
}