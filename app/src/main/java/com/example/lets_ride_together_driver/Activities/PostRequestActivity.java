package com.example.lets_ride_together_driver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.lets_ride_together_driver.R;

import java.util.Calendar;

public class PostRequestActivity extends AppCompatActivity {

    private ImageView selectedBike,selectedCar;
    private boolean isCar;
    private TextView txt_datepicker;

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
        
        txt_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PostRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();

                txt_datepicker.setText("" +dayOfMonth + " - "+month+ " - "+year );
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
