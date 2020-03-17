package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lets_ride_together_driver.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Common.Common;
import Model.UserModel;

public class LoginActivity extends AppCompatActivity {

    private TextView txt_signup;
    private Button btn_signin;
    private EditText edt_email,edt_password;
    private ProgressBar mProgressbar;
    private RelativeLayout relativeLayout;

    //firebase
    private FirebaseDatabase db;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_signup = findViewById(R.id.txt_signup);
        btn_signin = findViewById(R.id.signin_btn);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        mProgressbar = findViewById(R.id.mProgressbar);
        relativeLayout = findViewById(R.id.my_id);

        db = FirebaseDatabase.getInstance();
        mRef = db.getReference("Users").child("Drivers");



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

                mProgressbar.setVisibility(View.VISIBLE);
                final String email = edt_email.getText().toString();
                final String password = edt_password.getText().toString();

                if (edt_email.getText().toString().equals("") && edt_password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                    mProgressbar.setVisibility(View.GONE);
                }
                else {

                    mRef.orderByChild("email").equalTo(edt_email.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                UserModel model = data.getValue(UserModel.class);
                                if(email.equals(model.getEmail()) && password.equals(model.getPassword())) {

                                    if(model.getProfile_status()){
                                    Log.i("dxdiag", "login success");

                                    mProgressbar.setVisibility(View.GONE);

                                    Common.currentDriver = model;

                                    startActivity(new Intent(LoginActivity.this,SelectModeActivity.class));
                                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{

                                        Snackbar snackbar = Snackbar
                                                .make(relativeLayout, "your account disable by admin", Snackbar.LENGTH_LONG);

                                        snackbar.show();
                                        mProgressbar.setVisibility(View.GONE);
                                    }
                                }
                                else {


                                    Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                                    mProgressbar.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
