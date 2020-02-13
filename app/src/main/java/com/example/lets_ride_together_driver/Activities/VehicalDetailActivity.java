package com.example.lets_ride_together_driver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.lets_ride_together_driver.R;

public class VehicalDetailActivity extends AppCompatActivity {

    String number;
    String name  ;
    String email ;
    String password ;
    String city ;

    private EditText edt_cnic,edt_carname,edt_vehicle_number;
    private Spinner car_spinner;
    private ImageView car_doc_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehical_detail);

        Button btn_submit = findViewById(R.id.btn_submit);
        edt_carname = findViewById(R.id.edt_vehiclename);
        edt_cnic = findViewById(R.id.edt_cnic);
        edt_vehicle_number = findViewById(R.id.edt_vehicle_number);
        car_spinner = findViewById(R.id.cartype_spinner);
        car_doc_img = findViewById(R.id.car_doc_img);

        //get data from register activity
        number = getIntent().getStringExtra("phone_number");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        city = getIntent().getStringExtra("city");


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VehicalDetailActivity.this, VerificationActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("phone_number",number);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                intent.putExtra("city",city);
                intent.putExtra("cnic",edt_cnic.getText().toString());
                intent.putExtra("vehicle_name",edt_carname.getText().toString());
                intent.putExtra("vehicle_number",edt_vehicle_number.getText().toString());
                intent.putExtra("car_type",car_spinner.getSelectedItem().toString());
                startActivity(intent);

            }
        });
    }
}
