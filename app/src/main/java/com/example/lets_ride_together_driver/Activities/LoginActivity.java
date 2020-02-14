package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.UserModel;

public class LoginActivity extends AppCompatActivity {

    private TextView txt_signup;
    private Button btn_signin;
    private EditText edt_email,edt_password;

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
                final String email = edt_email.getText().toString();
                final String password = edt_password.getText().toString();

                if (edt_email.getText().toString().equals("") && edt_password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                UserModel model = data.getValue(UserModel.class);
                                if(email.equals(model.getEmail()) && password.equals(model.getPassword())) {
                                    Log.i("dxdiag", "login success");

                                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {


                                    Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();

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
