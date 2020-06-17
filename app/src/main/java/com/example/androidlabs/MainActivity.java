package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private CheckBox checkBox;
    private Switch sw;
    private EditText email;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        preferences = getSharedPreferences("sp", MODE_PRIVATE);
        email = (EditText) findViewById(R.id.email);
        String emailAddress = preferences.getString("Email Address", "");
        email.setText(emailAddress);

        button = (Button) findViewById(R.id.log_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                goToProfile.putExtra("EMAIL", email.getText().toString().trim());
                startActivity(goToProfile);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Email Address", email.getText().toString().trim());
        editor.commit();
    }
//        button = (Button) findViewById(R.id.bu);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
//            }
//        });

//        checkBox = (CheckBox) findViewById(R.id.cb);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.cb_messageOn), Snackbar.LENGTH_LONG)
//                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    compoundButton.setChecked(!b);
//                                }
//                            }).show();
//                } else {
//                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.cb_messageOff), Snackbar.LENGTH_LONG)
//                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    compoundButton.setChecked(!b);
//                                }
//                            }).show();
//                }
//            }
//        });
//
//        sw = (Switch) findViewById(R.id.sw);
//        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.sw_messageOn), Snackbar.LENGTH_LONG)
//                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    compoundButton.setChecked(!b);
//                                }
//                            }).show();
//                } else {
//                    Snackbar.make(getWindow().getCurrentFocus(), getResources().getString(R.string.sw_messageOff), Snackbar.LENGTH_LONG)
//                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    compoundButton.setChecked(!b);
//                                }
//                            }).show();
//                }
//            }
//        });

    public void send(View view) {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
    }


}