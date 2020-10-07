package com.example.lets_ride_together_driver.Activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.lets_ride_together_driver.AllPostsWork.ListAllPosts;
import com.example.lets_ride_together_driver.AllPostsWork.PostYourTravel;
import com.example.lets_ride_together_driver.ChatStuff.MessageMainActivity;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.StartRideRequestModel;
import com.example.lets_ride_together_driver.NewModel.UberDriver;
import com.example.lets_ride_together_driver.NewModel.User;
import com.example.lets_ride_together_driver.ProfilePageStuff.ProfilePage;
import com.example.lets_ride_together_driver.R;
import com.example.lets_ride_together_driver.Requests.AllRequests;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class HomePageMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MainActivity";
    private static final int[] COLORS = new int[]{R.color.primary_dark1, R.color.primary1, R.color.primary_light1, R.color.accent1, R.color.primary_dark};
    FloatingActionButton fab_homepage;
    ///navigasstion
    ImageView ImageViewNav;
    TextView EmailUserTxt, TypeUserTxt;
    String photo_url_user, email_user;
    ArrayList<User> data = new ArrayList<>();
    BottomAppBar bottomapp;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<StartRideRequestModel> rideRequestModelArrayList = new ArrayList<>();
    private EditText name;
    private EditText age;
    private Button addData;
    private Button getData;
    private RecyclerView showDataTxt;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 20 sec */
    private LocationManager locationManager;
    private LatLng latLng;
    private boolean isPermission;
    private DrawerLayout drawer;
    private DatabaseReference reference;
    private String uid;
    private String fullname;
    private List<Polyline> polylines;
    private LatLng start;
    private LatLng end;
    private boolean showRides = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home_page_map);
        bottomapp = findViewById(R.id.bottomAppBar);

        setSupportActionBar(bottomapp);


        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        if (requestSinglePermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            //it was pre written
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation(); //check whether location service is enable or not in your  phone


        }

        fab_homepage = findViewById(R.id.fab_homepage);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, bottomapp, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        ImageViewNav = headerview.findViewById(R.id.ImageViewNav);
        EmailUserTxt = headerview.findViewById(R.id.UsernameTxtNav);
        TypeUserTxt = headerview.findViewById(R.id.PhoneNumberTxtNav);


        DatabaseReference re = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl).child(FirebaseAuth.getInstance().getUid());


        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UberDriver driver = dataSnapshot.getValue(UberDriver.class);


                photo_url_user = driver.getAvatarUri();
                email_user = driver.getPhone();

                fullname = driver.getName();


                EmailUserTxt.setText(email_user);
                TypeUserTxt.setText(fullname + "\n\t\t" + "Driver");


                if (photo_url_user != null && !photo_url_user.equals("")) {
                    Picasso.get()
                            .load(photo_url_user)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(ImageViewNav);

                }
                SharedPreferences.Editor editor = getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE).edit();
                editor.putString("name", driver.getName());
                editor.putString("imageUri", driver.getAvatarUri());
                editor.putString("uid", FirebaseAuth.getInstance().getUid());
                editor.putString("phone", driver.getPhone());
                editor.putString("car_type", driver.getCarType());

                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fab_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PostYourTravel.class));
                finish();
            }
        });


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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        //   mMap.getUiSettings().setZoomControlsEnabled(true);

        if (latLng != null) {
            mMap.clear();
            //  mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation)));
            // CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            //  mMap.animateCamera(cameraUpdate);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            mMap.setMyLocationEnabled(true);

        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng2) {

//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions()
//                          .position(latLng2)
//                          .title("Your Selected Location"));
//
//
//                    System.err.println("your address is "+getCompleteAddressString(latLng2.latitude,latLng2.longitude));

            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed. Error: " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                location.getLatitude() + "," +
                location.getLongitude();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //it was pre written
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//TODO
        //add update location feature

    }


    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

        }


    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,

                        Manifest.permission.CALL_PHONE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.isAnyPermissionPermanentlyDenied()) {
                    // check for permanent denial of permission

                    isPermission = false;


                    final AlertDialog.Builder dialog = new AlertDialog.Builder(HomePageMap.this);
                    dialog.setTitle("Permissions")
                            .setMessage("You have to give all permissions!!, the app will close now")

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    finishAffinity();
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();

                } else if (report.areAllPermissionsGranted()) {

                    //Single Permission is granted
                    Toast.makeText(getApplicationContext(), "permissions are granted!", Toast.LENGTH_SHORT).show();
                    isPermission = true;

                }


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();


        return isPermission;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottomappbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.navigation_explore) {

            startActivity(new Intent(getApplicationContext(), ListAllPosts.class));
            finish();

        }

        if (item.getItemId() == R.id.navigation_profile) {


            AlertDialog.Builder builder1 = new AlertDialog.Builder(HomePageMap.this);
            builder1.setMessage("Are You Sure You Want to Show Current Rides?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Show",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            showRides = true;

                            // sendrequesttodriversforridestart();

                            if (showRides) {

                                showCurrentRides();
                            }

                        }
                    });

            builder1.setNegativeButton(
                    "Stop Showing",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            stopShowingCurrentRides();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();


        }
        if (item.getItemId() == R.id.navigation_profile22) {

            startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void showCurrentRides() {


        DatabaseReference re = FirebaseDatabase.getInstance().getReference("CurrentRides");

        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        final StartRideRequestModel requestModel = ds.getValue(StartRideRequestModel.class);

                        if (requestModel != null) {

                            // rideRequestModelArrayList.add(requestModel);

                            System.err.println("mylat" + requestModel.getSourcelat());

                            start = new LatLng(Double.parseDouble(requestModel.getSourcelat()), Double.parseDouble(requestModel.getSourcelng()));

                            end = new LatLng(Double.parseDouble(requestModel.getDestenationlat()), Double.parseDouble(requestModel.getDestenationlng()));

                            if (showRides) {
                                Routing routing = new Routing.Builder()
                                        .travelMode(Routing.TravelMode.DRIVING)
                                        .withListener(new RoutingListener() {
                                            @Override
                                            public void onRoutingFailure(RouteException e) {
                                                Toast.makeText(HomePageMap.this, "failer", Toast.LENGTH_SHORT).show();


                                            }

                                            @Override
                                            public void onRoutingStart() {
                                                Toast.makeText(HomePageMap.this, "start", Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onRoutingSuccess(ArrayList<Route> route, int position) {
                                                Toast.makeText(HomePageMap.this, "Success", Toast.LENGTH_SHORT).show();


//                        if(polylines.size()>0) {
//                            for (Polyline poly : polylines) {
//                                poly.remove();
//                            }
//                        }

                                                polylines = new ArrayList<>();
                                                //add route(s) to the map.
                                                for (int i = 0; i < route.size(); i++) {

                                                    //In case of more than 5 alternative routes
                                                    int colorIndex = i % COLORS.length;

                                                    PolylineOptions polyOptions = new PolylineOptions();
                                                    polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                                                    polyOptions.width(10 + i * 3);
                                                    polyOptions.addAll(route.get(i).getPoints());
                                                    Polyline polyline = mMap.addPolyline(polyOptions);
                                                    polylines.add(polyline);

                                                    Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();


                                                }

                                                // Start marker
                                                mMap.addMarker(new MarkerOptions().position(start).title("Start Point"));

                                                // End marker
                                                mMap.addMarker(new MarkerOptions().position(end).title("End Point"));


//                                  CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(13.0f).build();
//                                  CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//                                  mMap.animateCamera(cameraUpdate);

                                            }

                                            @Override
                                            public void onRoutingCancelled() {
                                                Toast.makeText(HomePageMap.this, "cancelled", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .waypoints(start, end)
                                        .key(getResources().getString(R.string.google_maps_key))
//                .alternativeRoutes(true)
                                        .build();
                                routing.execute();
                            }
                        } else {
                            Toast.makeText(HomePageMap.this, "No Current Rides Found!", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(HomePageMap.this, "No Current Rides Found!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void stopShowingCurrentRides() {

        showRides = false;

        polylines.clear();
        mMap.clear();


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profileMenu) {
            startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            finish();
        } else if (id == R.id.findserviceMenu) {
            startActivity(new Intent(getApplicationContext(), PostYourTravel.class));
            finish();

        } else if (id == R.id.messangerMenue) {
            startActivity(new Intent(HomePageMap.this, MessageMainActivity.class));
            finish();
        } else if (id == R.id.contactMenu) {
            Toast.makeText(this, "contactMenu", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.requests) {
            startActivity(new Intent(getApplicationContext(), AllRequests.class));
            finish();
        } else if (id == R.id.ubersection) {
            startActivity(new Intent(getApplicationContext(), DriverHome.class));
            finish();
        } else if (id == R.id.logoutMenu) {

            SharedPreferences.Editor editor = getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE).edit();
            editor.putString("name", "");
            editor.putString("imageUri", "");
            editor.putString("uid", "");
            editor.putString("phone", "");
            editor.putString("car_type", "");

            editor.apply();

            Toast.makeText(this, "Logged Out Successful", Toast.LENGTH_SHORT).show();

            if (FirebaseAuth.getInstance() != null) {

                Paper.init(this);
                Paper.book().destroy();


                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {

                Paper.init(this);
                Paper.book().destroy();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }


        } else if (id == R.id.nav_shareNav) {
            Toast.makeText(this, "nav_shareNav", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_sendNav) {
            Toast.makeText(this, "nav_sendNav", Toast.LENGTH_SHORT).show();

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}