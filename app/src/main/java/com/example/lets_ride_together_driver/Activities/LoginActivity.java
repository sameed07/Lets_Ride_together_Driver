package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lets_ride_together_driver.R;

public class LoginActivity extends AppCompatActivity {

    private TextView txt_signup;
    private Button btn_signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_signup = findViewById(R.id.txt_signup);
        btn_signin = findViewById(R.id.signin_btn);

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SelectModeActivity.class));
                finish();
            }
        });
    }
}
