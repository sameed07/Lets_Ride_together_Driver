package com.example.lets_ride_together_driver.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostRequestActivity extends AppCompatActivity {

    private ImageView selectedBike,selectedCar;
    private boolean isCar;
    private TextView txt_datepicker,txt_timePicker;
    private Button post_btn;
    private EditText edt_starting;
    private EditText edt_fare;
    private EditText edt_ending;
    private RadioGroup rd_seat,rd_trip;
    private RadioButton rd_select_seat, rd_select_trip;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    //for time picker return time for round trip
    int Hour;
    int Minute;


    FirebaseDatabase mDatabase;
    DatabaseReference mRef;


    private Toolbar myToolbar;
    private LinearLayout mainLayout;
    private LinearLayout offerLiftLayout;
    private RelativeLayout riderLayout;
    private ImageButton riderImage;
    private TextView riderText;
    private RelativeLayout vehicleLayout;
    private ImageButton vehicleImage;
    private TextView vehicleText;
    private ImageView routeArrow;
    private LinearLayout startPointLayout;
    private TextView startPointTx;
    private TextView offerLiftStartName;
    private TextView offerLiftStartAddress;
    private LinearLayout endPointLayout;
    private TextView endPointTx;
    private TextView offerLiftEndName;
    private TextView offerLiftEndAddress;
    private LinearLayout daysLayout;
    private Switch regularTripsw;
    private LinearLayout offerdaysLayout;
    private ToggleButton sat;
    private ToggleButton sun;
    private ToggleButton mon;
    private ToggleButton tue;
    private ToggleButton wed;
    private ToggleButton thu;
    private ToggleButton fri;
    private LinearLayout startDateTimeLayout;
    private TextView offerdeparturedate;
    private TextView offerdeparturedateHeader;
    private TextView offerdeparturedateDetail;
    private Switch offerRoundTripSw;
    private LinearLayout returnTimeLayout;
    private TextView offerReturnTimetx;
    private TextView offerreturndateHeader;
    private TextView offerreturndateDetail;
    private Button offerLiftConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("PostAsDriver");



        regularTripsw = (Switch) findViewById(R.id.regularTripsw);
        offerdaysLayout = (LinearLayout) findViewById(R.id.offerdaysLayout);
        sat = (ToggleButton) findViewById(R.id.Sat);
        sun = (ToggleButton) findViewById(R.id.Sun);
        mon = (ToggleButton) findViewById(R.id.Mon);
        tue = (ToggleButton) findViewById(R.id.Tue);
        wed = (ToggleButton) findViewById(R.id.Wed);
        thu = (ToggleButton) findViewById(R.id.Thu);
        fri = (ToggleButton) findViewById(R.id.Fri);
        
        txt_datepicker = findViewById(R.id.txt_datepicker);
        txt_timePicker = findViewById(R.id.txt_timepicker);
        post_btn = findViewById(R.id.post_btn);
        edt_starting = findViewById(R.id.edt_starting);
        edt_ending = findViewById(R.id.edt_ending);
        rd_seat = findViewById(R.id.rd_seats);
        rd_trip = findViewById(R.id.rd_trip);
        edt_fare = findViewById(R.id.edt_amount);

        

//        txt_timePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//
////                 hour = calendar.get(Calendar.HOUR_OF_DAY);
////                 mMinute = calendar.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(PostRequestActivity.this,
//                        new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                                hour = hourOfDay;
//                                mMinute = minute;
//                            }
//                        },hour,mMinute,true);
//
//                txt_timePicker.setText(" " +hour+":"+mMinute );
//                timePickerDialog.show();
//            }
//        });
        
        txt_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialo = new DatePickerDialog(PostRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                //*************Call Time Picker Here ********************

                                // Get Current Time
                                Calendar c = Calendar.getInstance();
                                Hour = c.get(Calendar.HOUR_OF_DAY);
                                Minute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialo = new TimePickerDialog(PostRequestActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                Hour = hourOfDay;
                                                Minute = minute;

                                                txt_datepicker.setText(date_time + ", " + hourOfDay + ":" + minute);
                                            }
                                        }, Hour, Minute, false);
                                timePickerDialo.show();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialo.show();



            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postTravel();

            }
        });

        //switches
        regularTripsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    offerdaysLayout.setVisibility(View.VISIBLE);
                } else {
                    offerdaysLayout.setVisibility(View.GONE);

                }

            }
        });

        selectedBike = findViewById(R.id.img_bike);
        selectedCar = findViewById(R.id.img_car);

        selectedBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isCar= false;
                checkCar();
            }
        });
        selectedCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isCar= true;
                checkCar();
            }
        });

    }


    //post travel function
    public void postTravel(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait...");
        progressDialog.setCancelable(false);


        mRef.push();

        if(edt_starting.getText().toString().equals("")&& edt_ending.getText().toString().equals("")){
            Toast.makeText(this, "Location must not be empty", Toast.LENGTH_SHORT).show();

        }else{

            progressDialog.show();
            int selectedId = rd_seat.getCheckedRadioButtonId();
            int selectTripId = rd_trip.getCheckedRadioButtonId();

            rd_select_seat = findViewById(selectedId);
            rd_select_trip = findViewById(selectTripId);

            String name = Common.currentDriver.getName();
            String profile_url = Common.currentDriver.getProfile_img();
            String carName = "car";
            String price = edt_fare.getText().toString();
            String seats = rd_select_seat.getText().toString();
            String trip = rd_select_trip.getText().toString();
            String id = Common.currentDriver.getuId();
            String date = txt_datepicker.getText().toString();
            String time = txt_timePicker.getText().toString();
            String vehicle;
            String staring_point = edt_starting.getText().toString();
            String ending = edt_ending.getText().toString();


            if(isCar){

                vehicle = "Car";

            }else{

                vehicle = "Bike";
            }

            String regulartripstring = "";
            if(regularTripsw.isChecked()){


                if (sat.isChecked()) {

                    regulartripstring = regulartripstring + "sat,";
                }
                if (sun.isChecked()) {

                    regulartripstring = regulartripstring + "sun,";
                }
                if (mon.isChecked()) {

                    regulartripstring = regulartripstring + "mon,";
                }
                if (tue.isChecked()) {

                    regulartripstring = regulartripstring + "tue,";
                }
                if (wed.isChecked()) {

                    regulartripstring = regulartripstring + "wed,";
                }
                if (thu.isChecked()) {

                    regulartripstring = regulartripstring + "thu,";
                }
                if (fri.isChecked()) {

                    regulartripstring = regulartripstring + "fri,";
                }

                Map<String , Object> map = new HashMap<>();

                map.put("price",price);
                map.put("seats",seats);
                map.put("trip",trip);
                map.put("id",id);
                map.put("date",date);
                map.put("schedule", regulartripstring);
                map.put("ride_type",vehicle);
                map.put("starting_point",staring_point);
                map.put("ending_point", ending);

                mRef.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PostRequestActivity.this, "Travel Posted", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(PostRequestActivity.this,CarpoolMainActivity.class));
                        finish();
                    }
                });


            }



            else{

                Toast.makeText(this, "Select Trip days", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }



        }

    }

    public void toggleClickListners() {
        sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sat.setTextColor(getResources().getColor(R.color.white));
                } else {

                    sat.setTextColor(getResources().getColor(R.color.black));

                }

            }
        });
        sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sun.setTextColor(getResources().getColor(R.color.white));
                } else {

                    sun.setTextColor(getResources().getColor(R.color.black));

                }
            }
        });
        mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mon.setTextColor(getResources().getColor(R.color.white));
                } else {

                    mon.setTextColor(getResources().getColor(R.color.black));

                }
            }
        });
        tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tue.setTextColor(getResources().getColor(R.color.white));
                } else {

                    tue.setTextColor(getResources().getColor(R.color.black));

                }
            }
        });
        wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    wed.setTextColor(getResources().getColor(R.color.white));
                } else {

                    wed.setTextColor(getResources().getColor(R.color.black));

                }
            }
        });
        thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    thu.setTextColor(getResources().getColor(R.color.white));
                } else {

                    thu.setTextColor(getResources().getColor(R.color.black));

                }
            }
        });
        fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fri.setTextColor(getResources().getColor(R.color.white));
                } else {

                    fri.setTextColor(getResources().getColor(R.color.black));

                }
            }
        });


    }
        public void checkCar(){
        if(isCar){

            selectedBike.setImageResource(R.drawable.not_selected_bike);
            selectedCar.setImageResource(R.drawable.selected_car);
        }else{

            selectedBike.setImageResource(R.drawable.selected_bike);
            selectedCar.setImageResource(R.drawable.not_selected_car);
        }
    }
}
