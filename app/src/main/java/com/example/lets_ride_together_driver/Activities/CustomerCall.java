package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.DataMessage;
import com.example.lets_ride_together_driver.NewModel.FCMResponse;
import com.example.lets_ride_together_driver.NewModel.Token;
import com.example.lets_ride_together_driver.NewModel.TripHistory;
import com.example.lets_ride_together_driver.R;
import com.example.lets_ride_together_driver.Remote.IFCMService;
import com.example.lets_ride_together_driver.Remote.IGoogleApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerCall extends AppCompatActivity {

    TextView txtTime, txtAddress, txtDistance, txtcountdown;
    Button btnCancel, btnAccept;
    MediaPlayer mediaPlayer;
    IFCMService mFCMService;
    IGoogleApi mService;
    String lat, lng, riderId, avatarUri;
    String customerName = "";
    private String customerId;
    private String IsAfterAuction;
    private String amount;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);

        txtTime = findViewById(R.id.txtTime);
        txtAddress = findViewById(R.id.txtAddress);
        txtDistance = findViewById(R.id.txtDistance);

        txtcountdown = findViewById(R.id.txt_count_down);


        btnAccept = findViewById(R.id.btnAccept);
        btnCancel = findViewById(R.id.btnDecline);

        mService = Common.getGoogleAPI();
        mFCMService = Common.getFCMService();

        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() != null) {

            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");
            riderId = getIntent().getStringExtra("riderId");
            avatarUri = getIntent().getStringExtra("avatarUri");
            customerId = getIntent().getStringExtra("customer");
            IsAfterAuction = getIntent().getStringExtra("IsAfterAuction");
            amount = getIntent().getStringExtra("amount");


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.user_rider_tbl).child(riderId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println("djjsdhjfjskdsd "+dataSnapshot);
                  //  Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                    customerName = dataSnapshot.child("name").getValue().toString();

                  ///  System.err.println("cusidis" + customerName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            getDirection(lat, lng);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (!TextUtils.isEmpty(customerId)) {
                    cancelBooking(customerId);
                }
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TripHistory tripHistory = new TripHistory();
                tripHistory.setAddress(txtAddress.getText().toString());
                tripHistory.setCustomerId(riderId);
                tripHistory.setCustomerName(customerName);
                tripHistory.setDistance(txtDistance.getText().toString());
                tripHistory.setTime(txtTime.getText().toString());
                tripHistory.setAvatarUri(avatarUri);


                Common.triphistoryStatic = tripHistory;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Common.driver_TripHistory_tbl).child(FirebaseAuth.getInstance().getUid()).child(Common.driver_TripAccept_tbl);

                ref.push().setValue(tripHistory);


                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                Intent intent = new Intent(getApplicationContext(), DriverTracking.class);
                //send customer location to new activity
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("customerId", customerId);
                intent.putExtra("riderName", customerName);
                intent.putExtra("riderId", riderId);
                intent.putExtra("avatarUri", avatarUri);
                intent.putExtra("IsAfterAuction", IsAfterAuction);
                intent.putExtra("amount", amount);


                intent.putExtra("distance", txtDistance.getText().toString());
                intent.putExtra("time", txtTime.getText().toString());
                intent.putExtra("address", txtAddress.getText().toString());

                startActivity(intent);
                finish();


            }
        });

        startTimer();

    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                txtcountdown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

                if (!TextUtils.isEmpty(customerId)) {
                    cancelBooking(customerId);
                } else {
                    Toast.makeText(CustomerCall.this, "cusdtomer id must not be null", Toast.LENGTH_SHORT).show();
                }
            }
        };
        countDownTimer.start();

    }

    private void cancelBooking(final String customerId) {


        TripHistory tripHistory = new TripHistory();
        tripHistory.setAddress(txtAddress.getText().toString());
        tripHistory.setCustomerId(riderId);
        tripHistory.setCustomerName(customerName);
        tripHistory.setDistance(txtDistance.getText().toString());
        tripHistory.setTime(txtTime.getText().toString());

        tripHistory.setAvatarUri(avatarUri);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Common.driver_TripHistory_tbl).child(FirebaseAuth.getInstance().getUid()).child(Common.driver_TripDecline_tbl);

        ref.push().setValue(tripHistory);


        Token token = new Token(customerId);

//        Notification notification = new Notification("Cancel","Driver has cancelled your request");
//        Sender sender = new Sender(token.getToken(),notification);
        Map<String, String> content = new HashMap<>();
        content.put("title", "Cancel");
        content.put("message", "Driver has cancelled your request");
        DataMessage dataMessage = new DataMessage(token.getToken(), content);

        mFCMService.sendMessage(dataMessage)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1) {
                            Toast.makeText(CustomerCall.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    protected void onStop() {
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {

        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        mediaPlayer.start();
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
                                txtDistance.setText(distance.getString("text"));
                                //get time
                                JSONObject time = legsObject.getJSONObject("duration");
                                txtTime.setText(time.getString("text"));
                                //get address
                                String address = legsObject.getString("end_address");
                                txtAddress.setText(address);

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
