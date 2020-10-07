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

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.User;
import com.example.lets_ride_together_driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        mRef = db.getReference(Common.user_driver_tbl);



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
                Log.i("dxdiag1", "login success0");

                mProgressbar.setVisibility(View.VISIBLE);
                final String email = edt_email.getText().toString();
                final String password = edt_password.getText().toString();

                if (edt_email.getText().toString().equals("") && edt_password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                    mProgressbar.setVisibility(View.GONE);
                }
                else {
                    Log.i("dxdiag", "login success1");

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.i("dxdiag", "login success882 " + FirebaseAuth.getInstance().getUid());

                            if (task.isSuccessful()) {
                                Log.i("dxdiag", "login success3");

                                mRef.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.i("dxdiag", "login success124 " + dataSnapshot);

//                                    avatarUri:
//                                    city:
//                                    email:
//                                    name:
//                                    password:
//                                    phone:
//                                    profile_img:
//                                    uId:


                                        //       Log.i("dxdiag", "login success5 "+model.getProfile_img());

//                                        if (email.equals(dataSnapshot.child("email").getValue()) && password.equals(dataSnapshot.child("password").getValue())) {

                                        //  if (model.getProfile_status()) {
                                        Log.i("dxdiag", "login success");

                                        mProgressbar.setVisibility(View.GONE);
//
                                        Common.currentDriver = new User(dataSnapshot.child("uId").getValue().toString()
                                                , dataSnapshot.child("name").getValue().toString()
                                                , dataSnapshot.child("email").getValue().toString()
                                                , dataSnapshot.child("phone").getValue().toString()
                                                , dataSnapshot.child("password").getValue().toString()
                                                , dataSnapshot.child("city").getValue().toString()
                                                , dataSnapshot.child("rates").getValue().toString()
                                                , dataSnapshot.child("car_type").getValue().toString()
                                                , dataSnapshot.child("profile_img").getValue().toString());

//                                                Common.currentUser = new User(dataSnapshot.child("uId").getValue().toString()
//                                                        ,dataSnapshot.child("name").getValue().toString()
//                                                        ,dataSnapshot.child("email").getValue().toString()
//                                                        ,dataSnapshot.child("phone").getValue().toString()
//                                                        ,dataSnapshot.child("password").getValue().toString()
//                                                        ,dataSnapshot.child("city").getValue().toString()
//                                                        ,dataSnapshot.child("profile_img").getValue().toString());

                                        startActivity(new Intent(LoginActivity.this, SelectModeActivity.class));
                                        Toast.makeText(LoginActivity.this, "Login success " + Common.currentDriver.getCity(), Toast.LENGTH_SHORT).show();
                                        return;
//                                            } else {
//
//                                                Snackbar snackbar = Snackbar
//                                                        .make(relativeLayout, "your account is under review", Snackbar.LENGTH_LONG);
//
//                                                snackbar.show();
//                                                mProgressbar.setVisibility(View.GONE);
//                                            }
//                                        } else {
//
//
//                                            Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
//                                            mProgressbar.setVisibility(View.GONE);
//
//                                    }
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
        });
    }
}
