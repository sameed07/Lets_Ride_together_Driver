package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.TripHistory;
import com.example.lets_ride_together_driver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class TripDetails extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView txtData;
    private TextView txtFee;
    private TextView txtBaseFare;
    private TextView txtTime;
    private TextView txtDistance;
    private TextView txtEstimatedPyout;
    private TextView txtFrom;
    private TextView txtTo;
    private Button btnDoneRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        txtData = findViewById(R.id.txtData);
        txtFee = findViewById(R.id.txtFee);
        txtBaseFare = findViewById(R.id.txtBaseFare);
        txtTime = findViewById(R.id.txtTime);
        txtDistance = findViewById(R.id.txtDistance);
        txtEstimatedPyout = findViewById(R.id.txtEstimatedPyout);
        txtFrom = findViewById(R.id.txtFrom);
        txtTo = findViewById(R.id.txtTo);

        btnDoneRide = findViewById(R.id.btnDoneRide);

        btnDoneRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), DriverHome.class));
                finish();

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        settingInformation();
    }

    private void settingInformation() {

        if (getIntent() != null) {


            Calendar calender = Calendar.getInstance();
            String data = String.format("%s, %d/%d", convertToDayOfWeek(calender.get(Calendar.DAY_OF_WEEK)), calender.get(Calendar.DAY_OF_MONTH), calender.get(Calendar.MONTH));

            txtData.setText(data);

            txtFee.setText(String.format("$ %.2f", getIntent().getDoubleExtra("total", 0.0)));

            txtEstimatedPyout.setText(String.format("$ %.2f", getIntent().getDoubleExtra("total", 0.0)));

            txtBaseFare.setText(String.format("$ %.2f", Common.base_fare));

            txtTime.setText(String.format("%s min", getIntent().getStringExtra("time")));
            txtDistance.setText(String.format("%s km", getIntent().getStringExtra("distance")));

            txtFrom.setText("From:\n" + getIntent().getStringExtra("start_address"));
            txtTo.setText("To:\n" + getIntent().getStringExtra("end_address"));


            String[] location_end = getIntent().getStringExtra("location_end").split(",");

            LatLng dropoff = new LatLng(Double.parseDouble(location_end[0]), Double.parseDouble(location_end[1]));

            mMap.addMarker(new MarkerOptions()
                    .position(dropoff)
                    .title("Drop off here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropoff, 12.0f));


            TripHistory tripHistory = Common.triphistoryStatic;

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Common.driver_TripHistory_tbl).child(Common.driver_TripAccept_tbl);

            ref.child(FirebaseAuth.getInstance().getUid()).setValue(tripHistory);


        }

    }

    private String convertToDayOfWeek(int day) {

        switch (day) {

            case Calendar.SUNDAY:
                return "SUNDAY";

            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";


            default:
                return "UNK";

        }


    }

}
