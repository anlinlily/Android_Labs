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
        setContentView(R.layout.activity_main_relative);
//        Button button = (Button) findViewById(R.id.bu);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
//            }
//        });
        CheckBox checkBox = (CheckBox) findViewById(R.id.cb);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.cb_messageOn), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    compoundButton.setChecked(!b);
                                }
                            }).show();
                } else {
                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.cb_messageOff), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    compoundButton.setChecked(!b);
                                }
                            }).show();
                }
            }
        });

        Switch sw = (Switch) findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.sw_messageOn), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    compoundButton.setChecked(!b);
                                }
                            }).show();
                } else {
                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.sw_messageOff), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    compoundButton.setChecked(!b);
                                }
                            }).show();
                }
            }
        });

    }

    public void send(View view) {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
    }
}