package com.app.karapp;

import static com.app.karapp.tools.Constant.BASE_URL;
import static com.app.karapp.tools.Constant.GET_CATEGORY;
import static com.app.karapp.tools.Constant.GET_POSTS_TO_EDIT;
import static com.app.karapp.tools.Constant.GET_PROVINCE;
import static com.app.karapp.tools.Constant.POST_Edit;
import static com.app.karapp.tools.Constant.POST_SOMETHING;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.karapp.module.GetCategory;
import com.app.karapp.module.GetProvince;
import com.app.karapp.module.GetUserDataAndPosts;
import com.app.karapp.module.User;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Post extends AppCompatActivity {

    private int GALLERY = 1, CAMERA = 2;
    int[] province_ids;
    public static final int REQUEST_IMAGE = 100;

    List<String> provinceList;

    int posit;
    int province_id =0;
    String SelectProvince;
    int pr_id =0 ;
    int ProvinceID[] = new int[35];

    int[] category_ids;
    List<String> categoryList;
    int cat_id =0 ;
    int categoryID[] = new int[6];
    int cat_posit;

    int provpos=0;
    int catpost=0;
    String cat_name;

    File imageURL = null;
    String TAG = "Post";

    EditText title, adress,details;
    Spinner category, province;
    TextView image_choose_hint;
    BootstrapButton save;
    ImageView pickImage, viewImage;

    int user_id, p_id;
    String p_hint, pprovince, ccategory;
    LinearLayout edit_post_show_image_layout;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_post);

        title = findViewById (R.id.title);
        adress = findViewById (R.id.adress);
        details = findViewById (R.id.detail);
        category = findViewById (R.id.category_spin);
        province = findViewById (R.id.province_spin);
        pickImage = findViewById (R.id.pick_image);
        image_choose_hint = findViewById (R.id.img_label);
        save = findViewById (R.id.save_btn);
        viewImage = findViewById (R.id._image);
        edit_post_show_image_layout = findViewById (R.id.edit_post_layout);

        ArrayList<User> auth_user;
        auth_user = (ArrayList<User>) SaveSharedPreference.getAuthInfo(Post.this);
        user_id =   auth_user.get (0).getId ();

        Intent intent = getIntent();
        p_hint =  intent.getStringExtra ("hint");
        p_id = intent.getIntExtra ("p_id", 0);
        pprovince = intent.getStringExtra ("pprovince");
        ccategory = intent.getStringExtra ("ccategory");

        ProvinceID[0] = 77;
        ProvinceID[1] = 78;//kabul
        ProvinceID[2] = 79;//kapisa
        ProvinceID[3] = 80;//parwan
        ProvinceID[4] = 81;//wardak
        ProvinceID[5] = 82;//logar
        ProvinceID[6] = 83;//nangarhar
        ProvinceID[7] = 84;//laghman
        ProvinceID[8] = 85;//panchsher
        ProvinceID[9] = 86;//baghlan
        ProvinceID[10] = 87;//bameyan
        ProvinceID[11] = 88;//ghazni
        ProvinceID[12] = 89;//paktika
        ProvinceID[13] = 90;//paktia
        ProvinceID[14] = 91;//khost
        ProvinceID[15] = 92;//konar
        ProvinceID[16] = 93;//norestan
        ProvinceID[17] = 94;//badakhsahn
        ProvinceID[18] = 95;//takhar
        ProvinceID[19] = 96;//kondoz
        ProvinceID[20] = 97;//samangan
        ProvinceID[21] = 98;//balkh
        ProvinceID[22] = 99;//sarpol
        ProvinceID[23] = 100;//orozgan
        ProvinceID[24] = 101;//daekonde
        ProvinceID[25] = 102;//orozgan
        ProvinceID[26] = 103;//zabol
        ProvinceID[27] = 104;//kandahar
        ProvinceID[28] = 105;//josjan
        ProvinceID[29] = 106;//faryab
        ProvinceID[30] = 107;//helmand
        ProvinceID[31] = 108;//badghis
        ProvinceID[32] = 109;//herat
        ProvinceID[33] = 110;//fara
        ProvinceID[34] = 111;//nemroz

        categoryID[0] = 0;
        categoryID[1] = 1;
        categoryID[2] = 2;
        categoryID[3] = 3;
        categoryID[4] = 4;
        categoryID[5] = 5;

        //province list
        provinceList = new ArrayList<String> ();
        provinceList.add ("ولایت");
        //calling the function of getting provinces data
        GetProvinces();
        //province spinner adapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String> (this,R.layout.spinner_item,provinceList ){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        province.setAdapter (spinnerArrayAdapter);

            province.setSelection (provpos);

// province on item click lister
        province.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                pr_id = ProvinceID[position];

                posit = position;

                Toast.makeText (Post.this, ""+pr_id, Toast.LENGTH_SHORT).show ();
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////

        //category list
        categoryList = new ArrayList<String> ();
        categoryList.add ("کتگوری");
        //calling the function of getting category data
        getCatgories();
        //category spinner adapter
        final ArrayAdapter<String> cat_spinnerArrayAdapter = new ArrayAdapter<String> (this,R.layout.spinner_item,categoryList ){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        category.setAdapter (cat_spinnerArrayAdapter);

// category on item click lister
        category.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                cat_id = categoryID[position];
                cat_posit = position;
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

        pickImage.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                showPictureDialog();
            }
        });

        save.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (p_hint.equals ("new")){
                    if (checkValidation()){
                        PostSomething();
                    }
                }else if(p_hint.equals ("edit")){
                    if (checkValidation()){
                        EditPost ();
                    }
                }

            }
        });

        //actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customeactionbar);

        ImageView back = findViewById(R.id.backarrowsingl);
        TextView titlebar = findViewById(R.id.titlebarsingl);

        if (p_hint.equals ("new")){
            titlebar.setText("ایجاد پست");
            edit_post_show_image_layout.setWeightSum (1);
            viewImage.setVisibility (View.GONE);
        }else if(p_hint.equals ("edit")){
            titlebar.setText("ویرایش پست");
            save.setText ("ویرایش");
            image_choose_hint.setText (" ");
            getPost();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity (new Intent (ProvinceList.this, MainActivity.class));
                finish();
            }
        });

    }

    //get province function
    public void GetProvinces(){
        AndroidNetworking.get(GET_PROVINCE)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetProvince.class, new ParsedRequestListener<List<GetProvince>> () {
                    @Override

                    public void onResponse(List<GetProvince> getprovince) {
                        int j=0;
                        province_ids = new int[getprovince.get(0).getProvince ().size ()];
                        for (j=0; j<getprovince.get(0).getProvince ().size ();  j++){
                            provinceList.add(getprovince.get(0).getProvince ().get(j).getpName ()+"");
                        }

//                        switch (pprovince) {
//                            case "کابل":
//                                provpos = 1;
//                                break;
//                            case "کاپیسا":
//                                provpos = 2;
//                                break;
//                            case "پروان":
//                                provpos = 3;
//                                break;
//                            case "وردک":
//                                provpos = 4;
//                                break;
//                            case "لوگر":
//                                provpos = 5;
//                                break;
//                            case "ننگرهار":
//                                provpos = 6;
//                                break;
//                            case "لغمان":
//                                provpos = 7;
//                                break;
//                            case "پنجشیر":
//                                provpos = 8;
//                                break;
//                            case "بغلان":
//                                provpos = 9;
//                                break;
//                            case "بامیان":
//                                provpos = 10;
//                                break;
//                            case "غزنی":
//                                provpos = 11;
//                                break;
//                            case "پکتیکا":
//                                provpos = 12;
//                                break;
//                            case "پکتیا":
//                                provpos = 13;
//                                break;
//                            case "خوست":
//                                provpos = 14;
//                                break;
//                            case "کنر":
//                                provpos = 15;
//                                break;
//                            case "نورستان":
//                                provpos = 16;
//                                break;
//                            case "بدخشان":
//                                provpos = 17;
//                                break;
//                            case "تخار":
//                                provpos = 18;
//                                break;
//                            case "کندز":
//                                provpos = 19;
//                                break;
//                            case "سمنگان":
//                                provpos = 20;
//                                break;
//                            case "بلخ":
//                                provpos = 21;
//                                break;
//                            case "سرپل":
//                                provpos = 22;
//                                break;
//                            case "غوری":
//                                provpos = 23;
//                                break;
//                            case "دایکندی":
//                                provpos = 24;
//                                break;
//                            case "ارزگان":
//                                provpos = 25;
//                                break;
//                            case "زابل":
//                                provpos = 26;
//                                break;
//                            case "کندهار":
//                                provpos = 27;
//                                break;
//                            case "جوزجان":
//                                provpos = 28;
//                                break;
//                            case "فاریاب":
//                                provpos = 29;
//                                break;
//                            case "هلمند":
//                                provpos = 30;
//                                break;
//                            case "بادغیس":
//                                provpos = 31;
//                                break;
//                            case "هرات":
//                                provpos = 32;
//                                break;
//                            case "فراه":
//                                provpos = 33;
//                                break;
//                            case "نمیروز":
//                                provpos = 34;
//                                break;
//                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d(TAG, "ErrorDetail usertype : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                    }
                });
    }

    public void getCatgories(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (this);
        AndroidNetworking.get(GET_CATEGORY)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetCategory.class, new ParsedRequestListener<List<GetCategory>> () {
                    @Override
                    public void onResponse(List<GetCategory> getCategories) {
                        progressBar.dismiss();
                        int j=0;
                        category_ids = new int[getCategories.get(0).getCategory ().size ()];
                        for (j=0; j<getCategories.get(0).getCategory ().size ();  j++){
                            categoryList.add(getCategories.get(0).getCategory ().get(j).getCname ()+"");
                        }
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

    //dialog for imagepick
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("انتخاب عکس");
        String[] pictureDialogItems = {
                "انتخاب از گالری",
                "باز کردن کامره" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                launchGalleryIntent();
                                break;
                            case 1:
                                launchCameraIntent();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(Post.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Post.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    Bundle bundle = data.getExtras();

                    imageURL = new File(Objects.requireNonNull (uri.getPath ()));

                    if (p_hint.equals ("edit")){
                        Glide.with(Post.this)
                                .load(imageURL)
                                .into(viewImage);
                    }
                    image_choose_hint.setText (imageURL.getName ()+"");
                    Toast.makeText (this, ""+imageURL.getName (), Toast.LENGTH_LONG).show ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkValidation() {
        TextInputLayout textInputLayout = null;
        if (title.getText ().toString ().equals ("") ) {
            Toast.makeText (this, "عنوان را وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if (adress.getText ().toString ().equals ("")) {
            Toast.makeText (this, "آدرس محل کار وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if (details.getText ().toString ().equals ("")) {
            Toast.makeText (this, "جزییات کار وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }
        else if(posit==0){
            Toast.makeText (this, "ولایت خویش را انتخاب کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if(imageURL==null){
            image_choose_hint.setText ("عکس انتخاب نشده!");
//            image_name.setTextColor (Color.red (R.color.red));
            return false;
        }else if(cat_posit==0){
            Toast.makeText (this, "کتگوری را انتخاب کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }

        return true;
    }

    // Post something
    public void PostSomething(){
        final CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show (Post.this);
        AndroidNetworking.upload(POST_SOMETHING)
                .addMultipartParameter("user_id", String.valueOf (user_id))
                .addMultipartParameter("cat_id", String.valueOf (cat_id))
                .addMultipartParameter("province_id", String.valueOf (pr_id))
                .addMultipartParameter("title",title.getText ().toString ())
                .addMultipartParameter("text",details.getText ().toString ())
                .addMultipartParameter("adress",adress.getText ().toString ())
                .addMultipartFile("image", new File (imageURL.toString ()))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener () {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsObjectList (User.class, new ParsedRequestListener<List<User>> () {

                    @Override
                    public void onResponse (List<User> response) {
                        progressBar.dismiss ();
                        Toast.makeText (Post.this, "موفقانه پست شد!", Toast.LENGTH_SHORT).show ();
                        Intent intent = new Intent (Post.this, MainActivity.class);
                        startActivity (intent);
                        finish ();
                    }
                    @Override
                    public void onError (ANError error) {
                        if (error.getErrorDetail ().equals ("parseError")){
                            progressBar.dismiss ();

                            Intent intent = new Intent (Post.this, MainActivity.class);
                            startActivity (intent);
                            finish ();
                        }else{
                            Log.d (TAG,"message: "+error.getMessage ());
                            Log.d (TAG,"Detail: "+error.getErrorDetail ());
                            Log.d (TAG,"Body: "+error.getErrorBody ());
                            progressBar.dismiss ();
                        }

                    }
                });

    }

    // Post edit
    public void EditPost(){
        final CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show (Post.this);
        AndroidNetworking.upload(POST_Edit)
                .addMultipartParameter("p_id", String.valueOf (p_id))
                .addMultipartParameter("user_id", String.valueOf (user_id))
                .addMultipartParameter("cat_id", String.valueOf (cat_id))
                .addMultipartParameter("province_id", String.valueOf (pr_id))
                .addMultipartParameter("title",title.getText ().toString ())
                .addMultipartParameter("text",details.getText ().toString ())
                .addMultipartParameter("adress",adress.getText ().toString ())
                .addMultipartFile("image", new File (imageURL.toString ()))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener () {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsObjectList (User.class, new ParsedRequestListener<List<User>> () {

                    @Override
                    public void onResponse (List<User> response) {
                        progressBar.dismiss ();
                        Toast.makeText (Post.this, "موفقانه ویرایش شد!", Toast.LENGTH_SHORT).show ();
                        Intent intent = new Intent (Post.this, MainActivity.class);
                        startActivity (intent);
                        finish ();
                    }
                    @Override
                    public void onError (ANError error) {
                        if (error.getErrorDetail ().equals ("parseError")){
                            progressBar.dismiss ();
                            Toast.makeText (Post.this, "موفقانه ویرایش شد!", Toast.LENGTH_SHORT).show ();
                            Intent intent = new Intent (Post.this, MainActivity.class);
                            startActivity (intent);
                            finish ();
                        }else{
                            Log.d (TAG,"message: "+error.getMessage ());
                            Log.d (TAG,"Detail: "+error.getErrorDetail ());
                            Log.d (TAG,"Body: "+error.getErrorBody ());
                            progressBar.dismiss ();
                        }

                    }
                });

    }

    public void getPost(){
        final CustomProgressBar progressBar  = new CustomProgressBar ();
        progressBar.show (Post.this);
        AndroidNetworking.get(GET_POSTS_TO_EDIT)
                .addPathParameter ("id", String.valueOf (p_id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetUserDataAndPosts.class, new ParsedRequestListener<List<GetUserDataAndPosts>> () {
                    @Override
                    public void onResponse(List<GetUserDataAndPosts> response) {

                        progressBar.dismiss();


                        Glide.with(Post.this)
                                .load(BASE_URL+response.get (0).getPostData ().get (0).getImage ())
                                .into(viewImage);

                        title.setText (response.get (0).getPostData ().get (0).getTitle ());
                        adress.setText (response.get (0).getPostData ().get (0).getAdress ());
                        details.setText (response.get (0).getPostData ().get (0).getText ());
                        pprovince = response.get (0).getPostData ().get (0).getpName ();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "ErrorDetail : " + anError.getErrorDetail() );
                        Log.d(TAG, "ErrorBody : " + anError.getErrorBody() );
                        Log.d(TAG, "ErrorCode : " + anError.getErrorCode() );
                        Log.d(TAG, "Message : " + anError.getMessage () );
                        progressBar.dismiss();
                    }
                });
    }
}
