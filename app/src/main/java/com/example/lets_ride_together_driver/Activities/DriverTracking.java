package com.example.lets_ride_together_driver.Activities;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Helper.DirectionJSONParser;
import com.example.lets_ride_together_driver.NewModel.DataMessage;
import com.example.lets_ride_together_driver.NewModel.FCMResponse;
import com.example.lets_ride_together_driver.NewModel.Token;
import com.example.lets_ride_together_driver.NewModel.TripHistory;
import com.example.lets_ride_together_driver.R;
import com.example.lets_ride_together_driver.Remote.IFCMService;
import com.example.lets_ride_together_driver.Remote.IGoogleApi;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverTracking extends FragmentActivity implements OnMapReadyCallback {

    private static final int PLAY_SERVICES_RES_REQUEST = 7001;

    //play services
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    Circle riderMarker;
    Marker driverMarker;
    Polyline direction;
    IGoogleApi mService;
    IFCMService mFCMService;
    GeoFire geoFire;
    Button btnStartTrip;
    Location pickuplocation;
    //new
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String riderlat, riderlng, distancerider, timerider, address, riderName, riderId, avatarUri;
    private String customerId;
    private DatabaseReference drivers;
    private String IsAfterAuction;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (getIntent() != null) {

            riderlat = getIntent().getStringExtra("lat");
            riderlng = getIntent().getStringExtra("lng");
            this.customerId = getIntent().getStringExtra("customerId");

            IsAfterAuction = getIntent().getStringExtra("IsAfterAuction");
            riderName = getIntent().getStringExtra("riderName");
            riderId = getIntent().getStringExtra("riderId");
            this.avatarUri = getIntent().getStringExtra("avatarUri");

            distancerider = getIntent().getStringExtra("distance");
            timerider = getIntent().getStringExtra("time");
            this.address = getIntent().getStringExtra("address");

            amount = getIntent().getStringExtra("amount");

            //   Toast.makeText(this, "" + customerId, Toast.LENGTH_SHORT).show();

        }
        System.err.println("mycus" + customerId);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mService = Common.getGoogleAPI();
        mFCMService = Common.getFCMService();


        setUpLocation();


        btnStartTrip = findViewById(R.id.btnStartTrip);
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStartTrip.getText().toString().equals("START TRIP")) {

                    pickuplocation = Common.mLastLocation;
                    btnStartTrip.setText("DROP OFF HERE");

                } else if (btnStartTrip.getText().toString().equals("DROP OFF HERE")) {

                    calculateCashFee(pickuplocation, Common.mLastLocation);

                }
            }
        });

    }

    private void calculateCashFee(final Location pickuplocation, Location mLastLocation) {

        System.err.println("pickup loc" + pickuplocation.getLatitude());
        System.err.println("des loc" + mLastLocation.getLatitude());


        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode = driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + pickuplocation.getLatitude() + "," + pickuplocation.getLongitude() + "&" +
                    "destination=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);


            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {


                                //      new ParserTask().execute(response.body().toString());


                                JSONObject jsonObject = new JSONObject(response.body());
                                JSONArray routes = jsonObject.getJSONArray("routes");

                                JSONObject object = routes.getJSONObject(0);
                                JSONArray legs = object.getJSONArray("legs");
                                JSONObject legsobject = legs.getJSONObject(0);


                                //get distance

                                final JSONObject distance = legsobject.getJSONObject("distance");
                                String distance_text = distance.getString("text");

                                Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                                //time
                                final JSONObject time = legsobject.getJSONObject("duration");
                                String time_text = time.getString("text");
                                Integer time_value = Integer.parseInt(time_text.replaceAll("\\D+", ""));


                                sendDropOffNotification(customerId);

                                Intent intent = new Intent(getApplicationContext(), TripDetails.class);

                                intent.putExtra("start_address", legsobject.getString("start_address"));
                                intent.putExtra("end_address", legsobject.getString("end_address"));

                                intent.putExtra("time", String.valueOf(time_value));
                                intent.putExtra("distance", String.valueOf(distance_value));


                                if (IsAfterAuction != null && IsAfterAuction.equals("")) {
                                    if (IsAfterAuction.equals("true")) {

                                        intent.putExtra("total", amount);

                                    } else {

                                        intent.putExtra("total", Common.formulaPrice(distance_value, time_value));

                                    }
                                } else {
                                    intent.putExtra("total", Common.formulaPrice(distance_value, time_value));
                                }

                                intent.putExtra("location_start", String.format("%f,%f", pickuplocation.getLatitude(), pickuplocation.getLongitude()));
                                intent.putExtra("location_end", String.format("%f,%f", Common.mLastLocation.getLatitude(), Common.mLastLocation.getLongitude()));


                                TripHistory tripHistory = new TripHistory();
                                tripHistory.setAddress(address);
                                tripHistory.setCustomerId(riderId);
                                tripHistory.setCustomerName(riderName);
                                tripHistory.setDistance(distancerider);
                                tripHistory.setTime(timerider);
                                tripHistory.setAvatarUri(avatarUri);


                                Common.triphistoryStatic = tripHistory;
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Common.driver_TripHistory_tbl).child(FirebaseAuth.getInstance().getUid()).child(Common.driver_TripCompleted_tbl);

                                ref.push().setValue(tripHistory);


                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
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

    private void getPrice(String mLocation, String mDestination) {

        String requestUrl = null;

        try {


            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + mLocation + "&"
                    + "destination=" + mDestination + "&"
                    + "key=" + getResources().getString(R.string.google_direction_api);

            mService.getPath(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        Log.i("error is", response.toString());

                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray routes = jsonObject.getJSONArray("routes");

                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsobject = legs.getJSONObject(0);


                        //get distance

                        JSONObject distance = legsobject.getJSONObject("distance");
                        String distance_text = distance.getString("text");

                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                        //get time

                        JSONObject time = legsobject.getJSONObject("duration");
                        String time_text = time.getString("text");
                        Integer time_value = Integer.parseInt(time_text.replaceAll("\\D+", ""));


                        sendDropOffNotification(customerId);

                        Intent intent = new Intent(getApplicationContext(), TripDetails.class);

                        intent.putExtra("start_address", legsobject.getString("start_address"));
                        intent.putExtra("end_address", legsobject.getString("end_address"));

                        intent.putExtra("time", String.valueOf(time_value));
                        intent.putExtra("distance", String.valueOf(distance_value));


                        if (IsAfterAuction != null && IsAfterAuction.equals("")) {
                            if (IsAfterAuction.equals("true")) {

                                intent.putExtra("total", amount);

                            } else {

                                intent.putExtra("total", Common.formulaPrice(distance_value, time_value));

                            }
                        } else {
                            intent.putExtra("total", Common.formulaPrice(distance_value, time_value));
                        }


                        intent.putExtra("location_start", String.format("%f,%f", pickuplocation.getLatitude(), pickuplocation.getLongitude()));
                        intent.putExtra("location_end", String.format("%f,%f", Common.mLastLocation.getLatitude(), Common.mLastLocation.getLongitude()));


                        TripHistory tripHistory = new TripHistory();
                        tripHistory.setAddress(address);
                        tripHistory.setCustomerId(riderId);
                        tripHistory.setCustomerName(riderName);
                        tripHistory.setDistance(distancerider);
                        tripHistory.setTime(timerider);
                        tripHistory.setAvatarUri(avatarUri);


                        Common.triphistoryStatic = tripHistory;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Common.driver_TripHistory_tbl).child(FirebaseAuth.getInstance().getUid()).child(Common.driver_TripCompleted_tbl);

                        ref.push().setValue(tripHistory);


                        System.err.println("data is gone" + Common.formulaPrice(distance_value, time_value));

                        startActivity(intent);
                        finish();


                    } catch (Exception e) {

                        e.printStackTrace();

                    }


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            Log.e("on erroe 2", "erroe 2 ");
        }
    }


    private void displayLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {


                            Common.mLastLocation = location;


                            if (Common.mLastLocation != null) {
                                final double latitude = Common.mLastLocation.getLatitude();
                                final double longitude = Common.mLastLocation.getLongitude();

                                if (driverMarker != null)
                                    driverMarker.remove();
                                driverMarker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude, longitude))
                                        .title("You")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_source)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));


                                if (direction != null) {
                                    direction.remove();
                                }

                                getDirection();


                                System.err.println("myloc" + location.getLatitude());


                                System.err.println("myloc" + Common.mLastLocation.getLatitude());


                                //new changes working so far
                                drivers = FirebaseDatabase.getInstance().getReference("Drivers").child(Common.currentDriver.getCarType());
                                geoFire = new GeoFire(drivers);
                                geoFire.setLocation(FirebaseAuth.getInstance().getUid(), new GeoLocation(Common.mLastLocation.getLatitude(), Common.mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {

                                        getDirection();

                                    }
                                });

                                //  pickuplocation = location;


                            } else {
                                Log.d("ERROR", "cannot Get Location");
                            }


                        }
                    });


        }


    }

    private void getDirection() {


        System.err.println("directionb w");


        LatLng currentPosition = new LatLng(Common.mLastLocation.getLatitude(), Common.mLastLocation.getLongitude());
        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode = driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + currentPosition.latitude + "," + currentPosition.longitude + "&" +
                    "destination=" + riderlat + "," + riderlng + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.d("Usama", requestApi);//print url for debug
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                System.err.println("directionb w" + response.body());

                                new ParserTask().execute(response.body());

                            } catch (Exception e) {
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


    private void setUpLocation() {

        buildLocationRequest();
        buildLocationCallBack();

        displayLocation();
        if (checkPlayServices()) {


        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RES_REQUEST).show();

            } else {

                Toast.makeText(this, "The device Is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        try {
            boolean isSucess = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_map));
            if (!isSucess) {
                Toast.makeText(this, "Map style error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


        mMap = googleMap;

        riderMarker = mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(riderlat), Double.parseDouble(riderlng)))
                .radius(80) //radius is 50m
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f));
        //creating Geo Fencing with 50m radius


        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference(Common.driver_tbl).child(Common.currentDriver.getCarType()));

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(Double.parseDouble(riderlat), Double.parseDouble(riderlng)), 0.05f);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                //here we will send customer id(rider id) to send notificaton
                //so , we will pass it from CustomerCall Activity

                Toast.makeText(DriverTracking.this, "working here", Toast.LENGTH_SHORT).show();
                if (key.equals(FirebaseAuth.getInstance().getUid())) {
                    sendArrivedNotification(customerId);

                    btnStartTrip.setEnabled(true);
                    //   btnStartTrip.setText("DROP OFF HERE");
                }


            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            displayLocation();
        }

    }

    private void sendArrivedNotification(String customerId) {
        Token token = new Token(customerId);
//        Notification notification = new Notification("Arrived", String.format("The Driver %s Has Arrived At Your Location", Common.currentDriver.getName()));
//        Sender sender = new Sender(token.getToken(), notification);

        Map<String, String> content = new HashMap<>();
        content.put("title", "Arrived");
        content.put("message", String.format("The Driver %s Has Arrived At Your Location", Common.currentDriver.getName()));
        DataMessage dataMessage = new DataMessage(token.getToken(), content);

        mFCMService.sendMessage(dataMessage).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {

                if (response.body().success != 1) {
                    Toast.makeText(DriverTracking.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                Toast.makeText(DriverTracking.this, "Failed to do it", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void sendDropOffNotification(final String customerId) {


        Token token = new Token(customerId);
//        Notification notification = new Notification("DropOff",customerId);
//        Sender sender = new Sender(token.getToken(), notification);
        Map<String, String> content = new HashMap<>();
        content.put("title", "DropOff");
        content.put("message", customerId);
        content.put("drivernewid", FirebaseAuth.getInstance().getUid());
        DataMessage dataMessage = new DataMessage(token.getToken(), content);

        System.err.println("cusid is" + customerId);

        mFCMService.sendMessage(dataMessage).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {

                if (response.body().success != 1) {
                    Toast.makeText(DriverTracking.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                Toast.makeText(DriverTracking.this, "Failed to do it", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void buildLocationCallBack() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    Common.mLastLocation = location;
                }
                displayLocation();
            }
        };

    }

    private void buildLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10);


    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        ProgressDialog mDialog = new ProgressDialog(DriverTracking.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Please Wait.....");
            mDialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {

                jObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                routes = parser.parse(jObject);


            } catch (Exception e) {

            }

            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            mDialog.dismiss();

            ArrayList points;
            PolylineOptions polylineOptions = null;

            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = lists.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polylineOptions.addAll(points);
                polylineOptions.width(10);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);

            }
            if (direction != null) {

                System.err.println("directionb w" + direction.getId());
                direction.remove();

            }
            direction = mMap.addPolyline(polylineOptions);

        }
    }
}
