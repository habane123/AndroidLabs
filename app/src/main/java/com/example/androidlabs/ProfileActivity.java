package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DirectAction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    ImageButton mImageButton;
    String email;
    EditText emailAddress;
    Button lab4Btn;
    Button weatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");


        Log.i(ACTIVITY_NAME, "onCreate() called");
        DirectAction data;

         mImageButton = findViewById(R.id.myImgButton);

        mImageButton.setOnClickListener(v ->  dispatchTakePictureIntent());
        emailAddress = findViewById(R.id.emailAddress);
        emailAddress.setText(email);

        lab4Btn = findViewById(R.id.lab4Button);
        weatherBtn = findViewById(R.id.WeatherButton);

        weatherBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    startActivity(new Intent(ProfileActivity.this, WeatherForecast.class));
                }
        });


        lab4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ChatRoomActivity.class));
            }
        });

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i(ACTIVITY_NAME,"onActivityResult called");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME,"onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME,"onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME,"onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME,"onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"onDestroy() called");
    }
}