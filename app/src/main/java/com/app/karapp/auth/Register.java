package com.app.karapp.auth;
import static com.app.karapp.tools.Constant.GET_PROVINCE;
import static com.app.karapp.tools.Constant.IS_EXIST_EMAIL_PHONE;
import static com.app.karapp.tools.Constant.USER_REGISTER;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.karapp.ImagePickerActivity;
import com.app.karapp.MainActivity;
import com.app.karapp.R;
import com.app.karapp.auth.ui.login.LoginActivity;
import com.app.karapp.module.GetProvince;
import com.app.karapp.module.IsEmailorPhoneExit;
import com.app.karapp.module.User;
import com.app.karapp.tools.CustomProgressBar;
import com.app.karapp.tools.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Register extends AppCompatActivity {

    private int GALLERY = 1, CAMERA = 2;
    int[] province_ids;
    public static final int REQUEST_IMAGE = 100;

    List<String> provinceList;

    int posit;
    int province_id =0;
    String SelectProvince;
    int pr_id =0 ;
    int ProvinceID[] = new int[35];

    File imageURL = null;
    File imgFile;

    int count_phone=0;

    BootstrapEditText name;
    BootstrapEditText last_name;
    BootstrapEditText phone_number;
    BootstrapEditText job;
    BootstrapEditText password;
    ImageView choose_img;
    TextView image_name, login_label;
    BootstrapButton register_btn;
    Spinner spinner_province;

    String TAG = "Register";

    int Id;
    String Name;
    String lastName;
    String Job;
    int Phone;
    String ImageUrl;
    int Pro_id;
    String Password;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);

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

        name = findViewById (R.id.name);
        last_name = findViewById (R.id.last_name);
        phone_number = findViewById (R.id.phone);
        job = findViewById (R.id.job);
        password = findViewById (R.id.password);
        choose_img = findViewById (R.id.chooseimg);
        image_name = findViewById (R.id.image_name);
        register_btn = findViewById (R.id.register);
        spinner_province = findViewById (R.id.chooseprovince);
        login_label = findViewById (R.id.login);

        login_label.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                startActivity (new Intent (Register.this, LoginActivity.class));
                finish ();
            }
        });

        choose_img.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                showPictureDialog();
            }
        });

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

        spinner_province.setAdapter (spinnerArrayAdapter);

// province on item click lister
        spinner_province.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                pr_id = ProvinceID[position];

                posit = position;

                Toast.makeText (Register.this, ""+pr_id, Toast.LENGTH_SHORT).show ();
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });


        register_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (checkValidation()){
                    isEmailorPhoneExist();
                }

            }
        });

    }
    //get province function
    public void GetProvinces(){
        AndroidNetworking.get(GET_PROVINCE)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(GetProvince.class, new ParsedRequestListener<List<GetProvince>>() {
                    @Override

                    public void onResponse(List<GetProvince> getprovince) {
                        int j=0;
                        province_ids = new int[getprovince.get(0).getProvince ().size ()];
                        for (j=0; j<getprovince.get(0).getProvince ().size ();  j++){
                            provinceList.add(getprovince.get(0).getProvince ().get(j).getpName ()+"");
                        }
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

    /**
     * method for validation of form on sign up button click
     */
    private boolean checkValidation() {
        TextInputLayout textInputLayout = null;
        int phone_length  = name.getText().length();
        if (name.getText ().toString ().equals ("") ) {
            Toast.makeText (this, "اسم تانرا وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if (last_name.getText ().toString ().equals ("")) {
            Toast.makeText (this, "تخلص تانرا وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if (phone_number.getText ().toString ().equals ("")) {
            Toast.makeText (this, "شماره تلفون تانرا وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if(job.getText ().toString ().equals ("")){
            Toast.makeText (this, "شغل تانرا وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }
        else if (password.getText ().toString ().equals ("")) {
            Toast.makeText (this, "رمز عبور تانرا وارد کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }
        else if(posit==0){
            Toast.makeText (this, "ولایت خویش را انتخاب کنید", Toast.LENGTH_SHORT).show ();
            return false;
        }else if(imageURL==null){
            image_name.setText ("عکس انتخاب نشده!");
//            image_name.setTextColor (Color.red (R.color.red));
            return false;
        }

        return true;
    }

    public void isEmailorPhoneExist(){
        AndroidNetworking.post(IS_EXIST_EMAIL_PHONE)
                .addBodyParameter("phone",phone_number.getText ().toString ())
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(IsEmailorPhoneExit.class, new ParsedRequestListener<List<IsEmailorPhoneExit>>() {
                    @Override
                    public void onResponse(List<IsEmailorPhoneExit> response) {
                        count_phone = response.get (0).getPhoneCount ();

                        if (count_phone!=0){
                            Toast.makeText (Register.this, "شماره قبلا رجستر شده است", Toast.LENGTH_SHORT).show ();
                        }else{
                            UserRegisters();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "Detail: "+anError.getErrorDetail());
                        Log.d(TAG, "Message: "+anError.getMessage ());
                        Log.d(TAG, "Body: "+anError.getErrorBody ());
                        Toast.makeText (Register.this, ""+anError.getErrorDetail(), Toast.LENGTH_SHORT).show ();
                    }
                });
    }

    // user register
    public void UserRegisters(){
        final CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show (Register.this);
        AndroidNetworking.upload(USER_REGISTER)
                .addMultipartFile("image", new File (imageURL.toString ()))
                .addMultipartParameter("name",name.getText ().toString ())
                .addMultipartParameter("last_name",last_name.getText ().toString ())
                .addMultipartParameter("phone",phone_number.getText ().toString ())
                .addMultipartParameter("province_id", String.valueOf (pr_id))
                .addMultipartParameter("job", job.getText ().toString ())
                .addMultipartParameter("password",password.getText ().toString ())
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
                        Id = response.get (0).getId ();
                        Name = response.get (0).getName ();
                        lastName = response.get (0).getLast_name ();
                        Job = response.get (0).getJob ();
                        Phone = response.get (0).getPhone ();
                        Password = response.get (0).getPassword ();
                        ImageUrl = response.get (0).getImage ();
                        SaveSharedPreference.setLoggedIn (Register.this, response.get (0).getId (), response.get (0).getName (),response.get (0).getLast_name (),response.get (0).getPhone (),response.get (0).getImage (),response.get (0).getPassword (), true);
                        Intent intent = new Intent (Register.this, MainActivity.class);
                        startActivity (intent);
                        finish ();
                    }
                    @Override
                    public void onError (ANError error) {
                        if (error.getErrorDetail ().equals ("parseError")){
                            progressBar.dismiss ();
                            SaveSharedPreference.setLoggedIn (Register.this, Id, Name,lastName,Phone,ImageUrl,Password, true);
                            Intent intent = new Intent (Register.this, MainActivity.class);
                            startActivity (intent);
                            finish ();
                        }else{
                            Toast.makeText (Register.this, ""+error.getErrorDetail ()+Id, Toast.LENGTH_SHORT).show ();
                            Log.d (TAG,"message: "+error.getMessage ());
                            Log.d (TAG,"Detail: "+error.getErrorDetail ());
                            Log.d (TAG,"Body: "+error.getErrorBody ());
                            progressBar.dismiss ();
                        }

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
        Intent intent = new Intent(Register.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(Register.this, ImagePickerActivity.class);
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
                    image_name.setText (imageURL.getName ()+"");
//                    Toast.makeText (this, ""+imageURL, Toast.LENGTH_LONG).show ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
