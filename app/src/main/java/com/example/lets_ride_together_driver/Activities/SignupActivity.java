package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lets_ride_together_driver.R;
import com.hbb20.CountryCodePicker;

import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity {


    //private CountryCodePicker ccp;


    private CountryCodePicker ccp;
    private EditText edtPhoneNumber,edt_name,edt_email,edt_password,edt_confirmPassword;
    private Spinner citySpinner;
    private Button btnSignup;
    private TextView signin_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ccp = (CountryCodePicker) findViewById(R.id.ccpicker);
        edtPhoneNumber = findViewById(R.id.edt_phone);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirmPassword = findViewById(R.id.edt_confirmPassword);
        citySpinner  = findViewById(R.id.city_spinner);


        signin_txt = findViewById(R.id.txt_signin);
        btnSignup = findViewById(R.id.signup_btn);




        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String city = citySpinner.getSelectedItem().toString();
                String phone = ccp.getSelectedCountryCode() + " "+ edtPhoneNumber.getText().toString();
                String name = edt_name.getText().toString();
                String password = edt_password.getText().toString();
                String cPassword = edt_confirmPassword.getText().toString();
                String email = edt_email.getText().toString();

                if(!name.equals("") && !email.equals("") && !password.equals("") && !cPassword.equals("")
                        && !edtPhoneNumber.getText().toString().equals("")) {
                    if (isValidEmailId(email)) {


                        if(cPassword.equals(password)){


                            Intent intent = new Intent(SignupActivity.this,VehicalDetailActivity.class);
                            intent.putExtra("phone_number", "+"+phone);
                            intent.putExtra("name", name);
                            intent.putExtra("password",  password);
                            intent.putExtra("email",   email);
                            intent.putExtra("city", city);


                            startActivity(intent);


                        }else{

                            Toast.makeText(SignupActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }



                    }else{

                        Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(SignupActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }






            }
        });

        signin_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
