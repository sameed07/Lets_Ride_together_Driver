package com.example.lets_ride_together_driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity {


    //private CountryCodePicker ccp;

    private Button btnSignup;
    private TextView signin_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       // ccp = (CountryCodePicker) findViewById(R.id.ccp);
        signin_txt = findViewById(R.id.txt_signin);
        btnSignup = findViewById(R.id.signup_btn);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,SelectModeActivity.class));
                finish();
            }
        });

        signin_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}
