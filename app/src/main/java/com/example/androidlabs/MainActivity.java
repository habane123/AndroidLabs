package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_grid);

        Button myButton = findViewById(R.id.myButton);
        String toastMessage = MainActivity.this.getResources().getString(R.string.Toast_message);
        myButton.setOnClickListener(btn -> Toast.makeText(MainActivity.this,toastMessage,Toast.LENGTH_LONG).show());

        Switch mySwitch = findViewById(R.id.mySwitch);
        String sb_Message1 = MainActivity.this.getResources().getString(R.string.Snackbar_message1);
        String sb_Message2 = MainActivity.this.getResources().getString(R.string.Snackbar_message2);
        mySwitch.setOnCheckedChangeListener(( buttonView, isChecked) -> {
            if(isChecked==true){
                Snackbar.make(mySwitch,sb_Message1,Snackbar.LENGTH_LONG)
                        .setAction("Undo", click -> mySwitch.setChecked(false))
                        .show();
            }else{
                Snackbar.make(mySwitch,sb_Message2,Snackbar.LENGTH_LONG).show();
            }
        });

        CheckBox myCheckBox = findViewById(R.id.myCheckBox);
        String cb_Message1 = MainActivity.this.getResources().getString(R.string.Checkbox_message1);
        String cb_Message2 = MainActivity.this.getResources().getString(R.string.Checkbox_message2);
        myCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked==true){
                Snackbar.make(myCheckBox,cb_Message1,Snackbar.LENGTH_LONG)
                        .setAction("Undo",click -> myCheckBox.setChecked(false))
                        .show();
            }else{
                Snackbar.make(myCheckBox,cb_Message2,Snackbar.LENGTH_LONG).show();
            }
        });

    }
}