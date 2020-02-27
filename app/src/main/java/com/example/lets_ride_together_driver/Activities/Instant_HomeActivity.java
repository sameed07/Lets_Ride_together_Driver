package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lets_ride_together_driver.R;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Common.Common;
import Remote.IGoogleApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Instant_HomeActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final int REQUEST_CODE = 101;
    public static final int REQUEST_LOCATION_CODE = 99;

    //map related
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mlastLocation;
    private LocationCallback locationCallback;
    private View mapView;
    private LocationRequest locationRequest;
    private Marker currentLocationmMarker;

    AutocompleteFilter typeFilter;
    private PlaceAutocompleteFragment places;

    //widgets

    private Button switch_carpool,btnGo;
    private Switch offline_switch;
    private EditText edt_search_location;

    //car animiation
    private List<LatLng> polyLineList;
    private Marker carMarker, pickupLocationMarker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition, currentPosition;
    private int index, next;
    private String destination;
    private PolylineOptions polylineOptions,blackPolyLineOptions;
    private Polyline blackPolyline, greyPolyline;

    private IGoogleApi mService;

    Runnable drawPathRunnable = new Runnable() {
        @Override
        public void run() {

            if (index < polyLineList.size() - 1) {
                index++;
                next = index + 1;

            }
            if (index < polyLineList.size() - 1) {

                startPosition = polyLineList.get(index);
                endPosition = polyLineList.get(next);
            }
            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    v = valueAnimator.getAnimatedFraction();
                    lng = v * endPosition.longitude + (1 - v) * startPosition.longitude;
                    lat = v * endPosition.longitude + (1 - v) * startPosition.latitude;
                    LatLng newPos = new LatLng(lat, lng);
                    carMarker.setPosition(newPos);
                    carMarker.setAnchor(0.5f, 0.5f);
                    carMarker.setRotation(getBearing(startPosition, newPos));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(newPos)
                                    .zoom(15.5f)
                                    .build()
                    ));

                }
            });

            valueAnimator.start();
            handler.postDelayed(this, 3000);

        }
    };
    //firebase
    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
   // GeoFire geoFire;

    String uId;
    double latitude, longitude;

    private float getBearing(LatLng startPosition, LatLng endPosition) {
        double lat = Math.abs(startPosition.latitude - endPosition.latitude);
        double lng = Math.abs(startPosition.longitude - endPosition.longitude);

        if (startPosition.latitude < endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        if (startPosition.latitude < endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_instant__home);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Online_Drivers");

        places = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                if(offline_switch.isChecked()){

                    destination = place.getAddress().toString();
                    destination = destination.replace("","+");

                    getDirections();
                }else{

                    Toast.makeText(Instant_HomeActivity.this, "Please Change your status to Online", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Status status) {

                Toast.makeText(Instant_HomeActivity.this, "" + status.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        uId = Common.currentDriver.getuId();


        mService = Common.getGoogleAPI();

        polyLineList = new ArrayList<>();

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);

        offline_switch = findViewById(R.id.offline_switch);
        switch_carpool = findViewById(R.id.btn_switch_carpool);

        switch_carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Instant_HomeActivity.this, CarpoolMainActivity.class));
                finish();
            }
        });

//        layoutBottomSheet = findViewById(R.id.bottom_sheet);


        offline_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

                    mMap.clear();
                    handler.removeCallbacks(drawPathRunnable);
                    Toast.makeText(Instant_HomeActivity.this, "Offline", Toast.LENGTH_SHORT).show();
                    mRef.child(uId).removeValue();

                }else{
                    fetchLocation();



                }
            }
        });



        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

    }

    private void getDirections() {

        currentPosition = new LatLng(mlastLocation.getLatitude(),mlastLocation.getLatitude());

        String requestApi = null;

        try{

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin="+currentPosition.latitude+","+currentPosition.longitude+"&" +
                    "destination="+destination+"&" +
                    "key="+getResources().getString(R.string.google_direction_api);

            Log.d("myTag",requestApi);

            mService.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");

                        for (int i = 0; i<jsonArray.length(); i++){

                            JSONObject route = jsonArray.getJSONObject(i);
                            JSONObject poly = route.getJSONObject("overview_polyline");
                            String polyline = poly.getString("points");
                            polyLineList = decodePoly(polyline);

                            //adjusting bounds

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for(LatLng latLng: polyLineList)
                                builder.include(latLng);
                            LatLngBounds bounds = builder.build();
                            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,2);
                            mMap.animateCamera(mCameraUpdate);

                            polylineOptions = new PolylineOptions();
                            polylineOptions.color(Color.GRAY);
                            polylineOptions.width(5);
                            polylineOptions.startCap(new SquareCap());
                            polylineOptions.endCap(new SquareCap());
                            polylineOptions.jointType(JointType.ROUND);
                            polylineOptions.addAll(polyLineList);
                            greyPolyline = mMap.addPolyline(polylineOptions);

                            blackPolyLineOptions = new PolylineOptions();
                            blackPolyLineOptions.color(Color.BLACK  );
                            blackPolyLineOptions.width(5);
                            blackPolyLineOptions.startCap(new SquareCap());
                            blackPolyLineOptions.endCap(new SquareCap());
                            blackPolyLineOptions.jointType(JointType.ROUND);
                            blackPolyline = mMap.addPolyline(blackPolyLineOptions);

                            mMap.addMarker(new MarkerOptions()
                            .position(polyLineList.get(polyLineList.size() -1))
                            .title("Pickup location"));


                            //animation
                            ValueAnimator polylineAnimator = ValueAnimator.ofInt(0,100);
                            polylineAnimator.setDuration(2000);
                            polylineAnimator.setInterpolator(new LinearInterpolator());
                            polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {

                                    List<LatLng> points = greyPolyline.getPoints();
                                    int percentValue = (int) animation.getAnimatedValue();
                                    int size = points.size();
                                    int newPoints = (int) (size * (percentValue/100.0f));
                                    List<LatLng> p = points.subList(0, newPoints);
                                    blackPolyline.setPoints(p);
                                }
                            });

                            polylineAnimator.start();

                            carMarker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_car)));

                            handler = new Handler();
                            index =-1;
                            next = 1;
                            handler.postDelayed(drawPathRunnable,3000);

                        }

                    } catch (JSONException e) {



                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(Instant_HomeActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception ex){

            ex.printStackTrace();

        }

    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mlastLocation = location;
                   // Toast.makeText(getApplicationContext(), mlastLocation.getLatitude() + "" + mlastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Instant_HomeActivity.this);
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));


            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        if(mlastLocation != null) {
            LatLng latLng = new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude());

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setTrafficEnabled(false);
            googleMap.setIndoorEnabled(false);
            googleMap.setBuildingsEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_car));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            //  mMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.addMarker(markerOptions);

            driverLatLng();
        }else{
            System.out.println("My location is null");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.instant_ride_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.switch_carpool:
                startActivity(new Intent(Instant_HomeActivity.this, CarpoolMainActivity.class));
                return true;
            case R.id.contact_us:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void driverLatLng(){

        if(mlastLocation != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("lat", mlastLocation.getLatitude());
            data.put("lng", mlastLocation.getLongitude());
            data.put("car_type",Common.currentDriver.getCar_type());


            mRef.child(uId).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(Instant_HomeActivity.this, "You are online", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Not loaded yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10);


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

    private void displayLocation() {


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//
//            mFusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//
//
//                            Common.mLastLocation = location;
//
//
//                            if (Common.mLastLocation != null) {
//
//                                if (offline_switch.isChecked()) {
//
//                                    latitude = Common.mLastLocation.getLatitude();
//                                    longitude = Common.mLastLocation.getLongitude();
//
//
//                                    LatLng center = new LatLng(latitude, longitude);
//
//
//                                    LatLng northside = SphericalUtil.computeOffset(center, 100000, 0);
//                                    LatLng southside = SphericalUtil.computeOffset(center, 100000, 100);
//
//
//                                    LatLngBounds bounds = LatLngBounds.builder()
//                                            .include(northside)
//                                            .include(southside)
//                                            .build();
//
//                                    places.setBoundsBias(bounds);
//                                    places.setFilter(typeFilter);
//
//
//
//
//
//                                            mRef = FirebaseDatabase.getInstance().getReference("Drivers").child(Common.currentDriver.getCar_type());
//                                            geoFire = new GeoFire(mRef);
//                                            geoFire.setLocation(uId, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
//                                                @Override
//                                                public void onComplete(String key, DatabaseError error) {
//
//
//                                                    if (currentLocationmMarker != null) {
//                                                        currentLocationmMarker.remove();
//
//                                                    }
//                                                    Log.d("lat = ", "" + latitude);
//
//
//                                                    LatLng latLng = new LatLng(Common.mLastLocation.getLatitude(), Common.mLastLocation.getLongitude());
//                                                    MarkerOptions markerOptions = new MarkerOptions();
//                                                    markerOptions.position(latLng);
//                                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_car));
//                                                    markerOptions.title("You are Here");
//                                                    // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_marker));
//                                                    currentLocationmMarker = mMap.addMarker(markerOptions);
//
//                                                    //below code makes it move very fast
//                                                   /* mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                                                    mMap.animateCamera(CameraUpdateFactory.zoomBy(15));*/
//
//                                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
//
//
//                                                }
//                                            });
//
//
//                                        }
//
//
//
//                                }
//
//
//                            }
//
//                    });
//
//
//        } else {
//
//            checkLocationPermission();
//
//        }


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

}
