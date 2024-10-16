package com.app.karapp.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.karapp.R;


public class CustomProgressBar {

    private Dialog dialog;

    // defualt value is true if you need to change so you can do it as your wish!
    private boolean backgroundTransparent = true;

    public Dialog show(Context context) {
        return show(context, null);
    }

    public Dialog show(Context context, CharSequence title) {
        return show(context, title, false);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable) {
        return show(context, title, cancelable, null);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.progress_bar_no_title, null);


//       dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);

        dialog = new Dialog(context, R.style.NoTitleBar);
        if(backgroundTransparent) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        if(title !=null){
            TextView tv = view.findViewById(R.id.tv_id);
            tv.setText(title);
        }

        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();

        return dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void dismiss() {
         this.dialog.dismiss();
    }

    public boolean isBackgroundTransparent() {
        return backgroundTransparent;
    }

    public void setBackgroundTransparent(boolean backgroundTransparent) {
        this.backgroundTransparent = backgroundTransparent;
    }

}