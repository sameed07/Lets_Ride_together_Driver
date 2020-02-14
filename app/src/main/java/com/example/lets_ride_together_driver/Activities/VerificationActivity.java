package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lets_ride_together_driver.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import Common.Common;
import Model.UserModel;

public class VerificationActivity extends AppCompatActivity {

    private TextView txtPhonenumber;
    private EditText edt_code;
    private Button btnVerification;
    private ProgressBar mProgressbar;

    //firebase auth object
    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference mRef;

    private String verificationId;
    String number;
    String name  ;
    String email ;
    String password ;
    String city ;
    String cnic;
    String vehicle_number;
    String vehicle_name;
    String cartype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        txtPhonenumber = findViewById(R.id.textPhonenumber);
        btnVerification = findViewById(R.id.btnVerify);
        mProgressbar = findViewById(R.id.mProgressbar);
        edt_code = findViewById(R.id.edt_code);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        mRef = db.getReference("Users").child("Drivers");

        number = getIntent().getStringExtra("phone_number");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        city = getIntent().getStringExtra("city");
        cnic = getIntent().getStringExtra("cnic");
        vehicle_name = getIntent().getStringExtra("vehicle_name");
        vehicle_number = getIntent().getStringExtra("vehicle_number");
        cartype = getIntent().getStringExtra("car_type");


        Toast.makeText(this, "" + cnic + vehicle_number + vehicle_name + cartype, Toast.LENGTH_SHORT).show();
        txtPhonenumber.setText(number);

        senderVerificationCode(number);

        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = edt_code.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    edt_code.setError("Code not valid...");
                    edt_code.requestFocus();
                    return;
                }


                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
//                            Intent intent = new Intent(VerifyPhoneActivity.this, ProfileActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
                            mProgressbar.setVisibility(View.GONE);

                            sendDataToFirebase();
                            Intent intent = new Intent(VerificationActivity.this, RegistrationSubActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            ///Toast.makeText(VerificationActivity.this, "Registered Successfully!!", Toast.LENGTH_SHORT).show();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }


    public void senderVerificationCode(String number){

        mProgressbar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code  = phoneAuthCredential.getSmsCode();
                    if(code != null){

                        edt_code.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    Toast.makeText(VerificationActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void sendDataToFirebase(){


        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(number);
        user.setCity(city);
        user.setPassword(password);
        user.setCnic(cnic);
        user.setCar_type(cartype);
        user.setVehicle_name(vehicle_name);
        user.setVehicle_number(vehicle_number);
        user.setProfile_status(false);
        user.setuId(mAuth.getCurrentUser().getUid());

        Common.currentUser = user.getuId();

        mRef.child(user.getuId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(VerificationActivity.this, "User registerd Sucessfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerificationActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
