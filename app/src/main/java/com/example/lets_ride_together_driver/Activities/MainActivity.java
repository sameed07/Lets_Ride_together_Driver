package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lets_ride_together_driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Common.Common;
import Model.UserModel;

public class MainActivity extends AppCompatActivity {


    private Button signin_btn, signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signin_btn = findViewById(R.id.signin_btn);
        signup_btn = findViewById(R.id.signup_btn);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

//                if(FirebaseAuth.getInstance().getCurrentUser() != null){
//
//
//                    FirebaseDatabase.getInstance().getReference("Users")
//                            .child("Drivers")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(
//                            new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if(dataSnapshot.exists()){
//
//
//
//                                        UserModel model = dataSnapshot.getValue(UserModel.class);
//
//                                        Common.currentDriver = model;
////                                        Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
////                                        Intent intent = new Intent(MainActivity.this, SelectModeActivity.class);
////
////                                        startActivity(intent);
//
//                                        return;
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            }
//                    ) ;
//
//            Intent intent = new Intent(MainActivity.this, SelectModeActivity.class);
//
//            startActivity(intent);
//        }


    }
    @Override
    protected void onStart() {
        super.onStart();

//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//
//            Intent intent = new Intent(MainActivity.this, SelectModeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
        //}
    }
}
