package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lets_ride_together_driver.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Common.Common;

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
    private ImageView switch_carpool;
    private Button btnPickupRequest;
    private Switch offline_switch;


    //firebase
    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
   // GeoFire geoFire;

    String uId;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_instant__home);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users").child("Drivers");

        mAuth = FirebaseAuth.getInstance();
        uId = Common.currentUser;

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);

        offline_switch = findViewById(R.id.offline_switch);
        switch_carpool = findViewById(R.id.switch_carpool);

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
                    Toast.makeText(Instant_HomeActivity.this, "Offline", Toast.LENGTH_SHORT).show();
                    mRef.child(mAuth.getCurrentUser().getUid()).child("Latlng").removeValue();

                }else{
                    fetchLocation();


//                    Toast.makeText(Instant_HomeActivity.this, " " + Common.currentDriver.getCar_type(), Toast.LENGTH_SHORT).show();
//                    FirebaseDatabase.getInstance().goOnline();
//
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                        return;
//                    }
//
//                    buildLocationRequest();
//                    buildLocationCallBack();
//
//
//                    mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//
//                    mRef = FirebaseDatabase.getInstance().getReference(Common.driver_tbl);
//                    geoFire = new GeoFire(mRef);
//
//                    displayLocation();
//                   // Snackbar.make(mapFragment.getView(), "You are online", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(Instant_HomeActivity.this, "You are online", Toast.LENGTH_SHORT).show();

                }
            }
        });



//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

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

            mRef.child(mAuth.getCurrentUser().getUid()).child("Latlng").setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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
