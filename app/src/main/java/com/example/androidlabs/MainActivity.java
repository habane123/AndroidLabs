package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    EditText emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lab3_linear_layout);
        prefs =getSharedPreferences("FileName", Context.MODE_PRIVATE);

        String savedString = prefs.getString("email", "");
        emailAddress = findViewById(R.id.emailAddress);
        emailAddress.setText(savedString);




        Button myLoginButton = findViewById(R.id.myLoginButton);

        myLoginButton.setOnClickListener(btn -> passEmailIntent());


    }
    private void passEmailIntent(){
        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
        goToProfile.putExtra("EMAIL", emailAddress.getText().toString());
        startActivity(goToProfile);

    }



    private void saveSharedPrefs(String key, String stringToSave){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,stringToSave);
        editor.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs("email", emailAddress.getText().toString());

    }


}