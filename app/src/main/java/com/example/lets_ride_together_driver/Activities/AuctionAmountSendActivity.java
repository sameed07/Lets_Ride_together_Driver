package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.R;
import com.example.lets_ride_together_driver.Remote.IFCMService;
import com.example.lets_ride_together_driver.Remote.IGoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuctionAmountSendActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference rateDetailsRef;
    DatabaseReference driverInfoRef;


    IFCMService mFCMService;
    IGoogleApi mService;
    TextView descrition;
    private MaterialEditText edtComment;
    private String lat, lng, riderId, customerId;
    private String customerName;
    private String riderFromLocation, riderToLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_amount_send);

        database = FirebaseDatabase.getInstance();

        driverInfoRef = database.getReference(Common.user_driver_tbl);

        edtComment = findViewById(R.id.edtComment);

        descrition = findViewById(R.id.rider_description);

        mService = Common.getGoogleAPI();
        mFCMService = Common.getFCMService();

        if (getIntent() != null) {

            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");
            riderId = getIntent().getStringExtra("riderId");

            riderFromLocation = getIntent().getStringExtra("riderFromLocation");
            riderToLocation = getIntent().getStringExtra("riderToLocation");


            customerId = getIntent().getStringExtra("customer");


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.user_rider_tbl).child(riderId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                    customerName = (String) td.get("name");

                    System.err.println("cusidis" + customerName);


                    getDirection(lat, lng);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!descrition.getText().equals("description")) {


                    if (!edtComment.getText().equals("")) {
                        submitAuctionAmount(riderId, FirebaseAuth.getInstance().getUid());
                    }


                }
            }
        });
    }

    private void submitAuctionAmount(String rideridlocal, final String driverId) {

        final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(AuctionAmountSendActivity.this).build();
        waitingDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Common.auction_list_result_tbl);


        HashMap<String, Object> map = new HashMap<>();

        map.put("amount", edtComment.getText().toString());
        map.put("driverid", driverId);

        ref.child(rideridlocal)
                .child(driverId)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        waitingDialog.dismiss();
                        Toast.makeText(AuctionAmountSendActivity.this, "Amount Added to Auction", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AuctionAmountSendActivity.this, DriverHome.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Toast.makeText(AuctionAmountSendActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void getDirection(String lat, String lng) {
        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode = driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + Common.mLastLocation.getLatitude() + "," + Common.mLastLocation.getLongitude() + "&" +
                    "destination=" + lat + "," + lng + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.d("Usama", requestApi);//print url for debug
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body());

                                JSONArray routes = jsonObject.getJSONArray("routes");

                                //after getting routes , just get first element of routes
                                JSONObject object = routes.getJSONObject(0);
                                //after getting first element we need to get array with legs
                                JSONArray legs = object.getJSONArray("legs");
                                //and get first elenment of legs array
                                JSONObject legsObject = legs.getJSONObject(0);
                                //get distance
                                JSONObject distance = legsObject.getJSONObject("distance");
                                String dis = distance.getString("text");
                                //get time
                                JSONObject time = legsObject.getJSONObject("duration");
                                String tim = time.getString("text");
                                //get address
                                String address = legsObject.getString("end_address");


                                descrition.setText("Rider Name: " + customerName + "\n"
                                        + "Rider Current Address: " + address + "\n"
                                        + "Time: " + tim + "\n"
                                        + "Distance: " + dis + "\n"
                                        + "Rider From Location: " + riderFromLocation + "\n"
                                        + "Rider To Location: " + riderToLocation);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {

        }

    }


}
