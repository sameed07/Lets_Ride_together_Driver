package com.example.lets_ride_together_driver.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.lets_ride_together_driver.ChatStuff.MessageMainActivity;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.Token;
import com.example.lets_ride_together_driver.NewModel.User;
import com.example.lets_ride_together_driver.R;
import com.example.lets_ride_together_driver.Remote.IGoogleApi;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.SphericalUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    public static final int REQUEST_LOCATION_CODE = 99;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    //new
    SwitchMaterial location_switch;
    SupportMapFragment mapFragment;
    DatabaseReference drivers;
    GeoFire geoFire;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    DatabaseReference onlineRef, currentUserRef;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private Marker currentLocationmMarker;
    //car animiation
    private List<LatLng> polyLineList;
    private Marker carMarker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition, currentPosition;
    private int index, next;
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
    // private Button btnGo;
    private AutocompleteSupportFragment places;
    private String destination;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyline;
    private IGoogleApi mService;

    private DatabaseReference users;
    private View navView;
    private TextView txtname;
    private TextView txtStars;

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
        setContentView(R.layout.activity_drawar_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (!Places.isInitialized()) {
            Places.initialize(DriverHome.this, "AIzaSyC7aO2ri5wsZEBhI3iqm70A1-m7vTYmehg");

        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        users = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
        navView = navigationView.getHeaderView(0);
        txtname = navView.findViewById(R.id.txtDriverName);

        txtStars = navView.findViewById(R.id.txtStars);

        //  Toast.makeText(this, ""+Common.currentDriver.getName(), Toast.LENGTH_SHORT).show();
        try {
            txtStars.setText(Common.currentDriver.getRates());

            txtname.setText(Common.currentDriver.getName());

            Toast.makeText(this, "car type " + Common.currentDriver.getCarType(), Toast.LENGTH_SHORT).show();
            CircleImageView imageAvatar = navView.findViewById(R.id.imageView_avatar);


            if (Common.currentDriver.getProfile_img() != null && (!TextUtils.isEmpty(Common.currentDriver.getProfile_img()))) {

                Picasso.get()
                        .load(Common.currentDriver.getProfile_img())
                        .placeholder(R.drawable.ic_star_black_24dp)
                        .into(imageAvatar);

            }


        } catch (Exception e) {
            txtStars.setText("0.0");

            txtname.setText("No Name");
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        location_switch = findViewById(R.id.location_switch);


        location_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOnline) {
                if (isOnline) {
                    FirebaseDatabase.getInstance().goOnline();

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    buildLocationRequest();
                    buildLocationCallBack();


                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    drivers = FirebaseDatabase.getInstance().getReference(Common.driver_tbl);
                    geoFire = new GeoFire(drivers);

                    displayLocation();
                    Snackbar.make(mapFragment.getView(), "You are online", Snackbar.LENGTH_SHORT).show();


                } else {


                    DatabaseReference dri = FirebaseDatabase.getInstance().getReference(Common.driver_tbl).child(Common.currentDriver.getCarType());


                    dri.child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //      Toast.makeText(DriverHome.this, "done removing", Toast.LENGTH_SHORT).show();
                        }
                    });


                    FirebaseDatabase.getInstance().goOffline();

                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                    if (currentLocationmMarker != null) {
                        currentLocationmMarker.remove();
                        mMap.clear();


                    }
                    if (handler != null) {
                        handler.removeCallbacks(drawPathRunnable);
                    }
                    Snackbar.make(mapFragment.getView(), "You are offline", Snackbar.LENGTH_SHORT).show();


                }
            }
        });

//        location_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isOnline) {
//
//
//            }
//
//        });


        //new polyline
        polyLineList = new ArrayList<>();


//        typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
//                .setTypeFilter(3)
//                .build();


        places = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                if (place.getName() != null) {
                    if (location_switch.isChecked()) {

                        destination = place.getName();
                        destination = destination.replace(" ", "+"); //to replace empty space with + to fetch data

                        getDirection();
                    } else {

                        Toast.makeText(getApplicationContext(), "Please change your status to ONLINE", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(), "" + status.toString(), Toast.LENGTH_SHORT).show();

            }
        });


        setupLocation();

        mService = Common.getGoogleAPI();

        updateFirebaseToken();


    }


    @Override
    protected void onDestroy() {


        DatabaseReference dri = FirebaseDatabase.getInstance().getReference(Common.driver_tbl).child(Common.currentDriver.getCarType());


        dri.child(Common.currentDriver.getuId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DriverHome.this, "done removing", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseDatabase.getInstance().goOffline();

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        if (currentLocationmMarker != null) {
            currentLocationmMarker.remove();
            mMap.clear();


        }
        if (handler != null) {
            handler.removeCallbacks(drawPathRunnable);
        }
        if (mapFragment.getView() != null) {
            Snackbar.make(mapFragment.getView(), "You are offline", Snackbar.LENGTH_SHORT).show();
        }


        super.onDestroy();
    }

    @Override
    protected void onResume() {


        super.onResume();


        //presence system

        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        //    currentUserRef = FirebaseDatabase.getInstance().getReference(Common.driver_tbl);
//                        .child(Common.currentDriver.getCarType())
        //                .child(FirebaseAuth.getInstance().getUid());

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        currentUserRef = FirebaseDatabase.getInstance().getReference(Common.driver_tbl)
                .child(FirebaseAuth.getInstance().getUid());


        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserRef.onDisconnect().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setupLocation() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);


            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);


            }


        } else {

            buildLocationRequest();
            buildLocationCallBack();

            if (location_switch.isChecked()) {
                drivers = FirebaseDatabase.getInstance().getReference(Common.driver_tbl).child(Common.currentDriver.getCarType());
                geoFire = new GeoFire(drivers);

                displayLocation();

            }

        }


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawar_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trip_history) {

            startActivity(new Intent(getApplicationContext(), TripHistoryActivity.class));

        } else if (id == R.id.nav_help) {

            //  startActivity(new Intent(getApplicationContext(), MessageMainActivity.class));


        } else if (id == R.id.nav_message) {

            startActivity(new Intent(getApplicationContext(), MessageMainActivity.class));
            finish();

        } else if (id == R.id.nav_carpoool) {
            startActivity(new Intent(getApplicationContext(), CarpoolMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        } else if (id == R.id.nav_car_type) {
            showDialogUpdateCarType();

        } else if (id == R.id.nav_update_information) {
            showDialogUpdateInformation();

        } else if (id == R.id.nav_signout) {
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogUpdateCarType() {

        androidx.appcompat.app.AlertDialog.Builder diloag = new androidx.appcompat.app.AlertDialog.Builder(this);
        diloag.setCancelable(false);
        diloag.setTitle("Update Vehicle");

        LayoutInflater inflater = LayoutInflater.from(this);
        View carType = inflater.inflate(R.layout.layout_update_car_type, null);

        final RadioButton rdi_uberX = carType.findViewById(R.id.rdi_uberX);
        final RadioButton rdi_uber_black = carType.findViewById(R.id.rdi_uber_black);

        //load default data from user information

        if (Common.currentDriver.getCarType().equals("UberX")) {
            rdi_uberX.setChecked(true);
        } else if (Common.currentDriver.getCarType().equals("Uber Black")) {
            rdi_uber_black.setChecked(true);
        }


        diloag.setView(carType);

        diloag.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


                //loading Dialog
                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(DriverHome.this).build();
                waitingDialog.show();


                HashMap<String, Object> hashMap = new HashMap<>();

                if (rdi_uberX.isChecked()) {
                    hashMap.put("car_type", rdi_uberX.getText().toString());
                } else if (rdi_uber_black.isChecked()) {
                    hashMap.put("car_type", rdi_uber_black.getText().toString());
                }


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                databaseReference.child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {

                                if (task.isSuccessful()) {

                                    currentUserRef = FirebaseDatabase.getInstance().getReference(Common.driver_tbl)
                                            .child(Common.currentDriver.getCarType())
                                            .child(FirebaseAuth.getInstance().getUid());

                                    Toast.makeText(DriverHome.this, "Vehicle updated!", Toast.LENGTH_SHORT).show();


                                    drivers.child(FirebaseAuth.getInstance().getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Common.currentDriver = dataSnapshot.getValue(User.class);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                    waitingDialog.dismiss();
                                } else {

                                    Toast.makeText(DriverHome.this, "Vehicle update failed!", Toast.LENGTH_SHORT).show();
                                    waitingDialog.dismiss();
                                }

                            }
                        });


            }
        });

        diloag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        diloag.show();
    }

    private void showDialogUpdateInformation() {

        androidx.appcompat.app.AlertDialog.Builder diloag = new androidx.appcompat.app.AlertDialog.Builder(this);
        diloag.setCancelable(false);
        diloag.setTitle("Update Information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_update_information, null);

        final MaterialEditText edtName = login_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = login_layout.findViewById(R.id.edtPhone);
        final ImageView uploadImage = login_layout.findViewById(R.id.image_upload);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        diloag.setView(login_layout);

        diloag.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


                //loading Dialog
                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(DriverHome.this).build();
                waitingDialog.show();


                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();

                if (!TextUtils.isEmpty(name)) {

                    hashMap.put("name", name);
                }

                if (!TextUtils.isEmpty(phone)) {

                    hashMap.put("phone", phone);
                }


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                databaseReference.child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {

                                if (task.isSuccessful()) {

                                    users.child(FirebaseAuth.getInstance().getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                    Common.currentDriver = dataSnapshot.getValue(User.class);

                                                    updateFirebaseToken();


                                                    SharedPreferences.Editor editor = getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE).edit();
                                                    editor.putString("name", Common.currentDriver.getName());
                                                    editor.putString("imageUri", Common.currentDriver.getProfile_img());
                                                    editor.putString("uid", FirebaseAuth.getInstance().getUid());
                                                    editor.putString("phone", Common.currentDriver.getPhone());
                                                    editor.putString("car_type", Common.currentDriver.getCarType());

                                                    editor.apply();


                                                    try {
                                                        txtStars.setText(Common.currentDriver.getRates());

                                                        txtname.setText(Common.currentDriver.getName());


                                                        CircleImageView imageAvatar = navView.findViewById(R.id.imageView_avatar);


                                                        if (Common.currentDriver.getProfile_img() != null && (!TextUtils.isEmpty(Common.currentDriver.getProfile_img()))) {

                                                            Picasso.get()
                                                                    .load(Common.currentDriver.getProfile_img())
                                                                    .placeholder(R.drawable.ic_star_black_24dp)
                                                                    .into(imageAvatar);

                                                        }


                                                    } catch (Exception e) {
                                                        txtStars.setText("0.0");

                                                        txtname.setText("No Name");
                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                    Toast.makeText(DriverHome.this, "Information updated!", Toast.LENGTH_SHORT).show();
                                    waitingDialog.dismiss();
                                } else {

                                    Toast.makeText(DriverHome.this, "Information update failed!", Toast.LENGTH_SHORT).show();
                                    waitingDialog.dismiss();
                                }

                            }
                        });


            }
        });

        diloag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        diloag.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri saveUri = data.getData();

            if (saveUri != null) {

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Uploading....");
                dialog.show();


                String Imagename = UUID.randomUUID().toString();

                final StorageReference imageFolder = storageReference.child("images/" + Imagename);

                imageFolder.putFile(saveUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                dialog.dismiss();


                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {


                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("profile_img", uri.toString());
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                                        databaseReference.child(FirebaseAuth.getInstance().getUid())
                                                .updateChildren(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(DriverHome.this, "Uploaded!", Toast.LENGTH_SHORT).show();

                                                        } else {

                                                            Toast.makeText(DriverHome.this, "Upload failed!", Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                });


                                    }
                                });

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                dialog.setMessage("Uploading  " + progress + "%");
                            }
                        });


            }


        }
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PICK_IMAGE);

    }

    private void showDialogChangePassword() {

        androidx.appcompat.app.AlertDialog.Builder diloag = new androidx.appcompat.app.AlertDialog.Builder(this);
        diloag.setCancelable(false);
        diloag.setTitle("ChangePassword");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_change_password, null);

        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtNewPassword = login_layout.findViewById(R.id.edtNewPassword);
        final MaterialEditText edtNewConfirmPassword = login_layout.findViewById(R.id.edtNewConfirmPassword);

        diloag.setView(login_layout);

        diloag.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(DriverHome.this).build();
                waitingDialog.show();


                //login
                if (edtNewPassword.getText().toString().equals(edtNewConfirmPassword.getText().toString())) {
                    if (!(TextUtils.isEmpty(edtPassword.getText().toString())) && !(TextUtils.isEmpty(edtNewPassword.getText().toString()))) {


                        String curemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                        AuthCredential credential = EmailAuthProvider.getCredential(curemail, edtPassword.getText().toString());

                        FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {

                                        if (task.isSuccessful()) {


                                            FirebaseAuth.getInstance().getCurrentUser()
                                                    .updatePassword(edtNewPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(Task<Void> task) {

                                                            if (task.isSuccessful()) {


                                                                HashMap<String, Object> password = new HashMap<>();

                                                                password.put("password", edtNewPassword.getText().toString());

                                                                DatabaseReference driverinfo = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);

                                                                driverinfo.child(FirebaseAuth.getInstance().getUid())
                                                                        .updateChildren(password)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    waitingDialog.dismiss();
                                                                                    Toast.makeText(DriverHome.this, "Password changed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });


                                                            } else {
                                                                Toast.makeText(DriverHome.this, "Password change Failed", Toast.LENGTH_SHORT).show();
                                                                waitingDialog.dismiss();
                                                            }


                                                        }
                                                    });

                                        } else {

                                            waitingDialog.dismiss();

                                            Toast.makeText(DriverHome.this, "Wrong old Password", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                    } else {
                        Toast.makeText(DriverHome.this, "Please Enter Missing Values", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(DriverHome.this, "Password not Matching", Toast.LENGTH_SHORT).show();
                }
            }
        });

        diloag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        diloag.show();

    }

    private void signOut() {

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

    }


    private void updateFirebaseToken() {


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tokens = db.getReference(Common.token_tbl);


        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {

                                              Toast.makeText(DriverHome.this, "" + e, Toast.LENGTH_SHORT).show();
                                          }
                                      }
                ).addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                if (Common.currentDriver.getuId() != null) {
                    Token token = new Token(instanceIdResult.getToken());
                    tokens.child(Common.currentDriver.getuId())
                            .setValue(token);
                } else {
                    Toast.makeText(DriverHome.this, "User Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getDirection() {

        try {
            currentPosition = new LatLng(Common.mLastLocation.getLatitude(), Common.mLastLocation.getLongitude());
            String requestApi = null;
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode = driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + currentPosition.latitude + "," + currentPosition.longitude + "&" +
                    "destination=" + destination + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.d("Usama", requestApi);//print url for debug
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                //  for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject route = jsonArray.getJSONObject(0);
                                JSONObject poly = route.getJSONObject("overview_polyline");
                                String polyline = poly.getString("points");
                                polyLineList = decodePoly(polyline);
                                //  }

                                if (!polyLineList.isEmpty()) {
                                    // Adjusting Bounds
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng latLng : polyLineList) {
                                        builder = builder.include(latLng);
                                    }
                                    LatLngBounds bounds = builder.build();
                                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                                    mMap.animateCamera(mCameraUpdate);
                                }
                                polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.GRAY);
                                polylineOptions.width(8);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.endCap(new SquareCap());
                                polylineOptions.jointType(JointType.ROUND);
                                polylineOptions.addAll(polyLineList);
                                greyPolyline = mMap.addPolyline(polylineOptions);


                                blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.color(Color.BLACK);
                                blackPolylineOptions.width(8);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(JointType.ROUND);
                                blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                mMap.addMarker(new MarkerOptions()
                                        .position(polyLineList.get(polyLineList.size() - 1))
                                        .title("Location To Go"));

                               /* //animation
                                ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0,100);
                                polyLineAnimator.setDuration(2000);
                                polyLineAnimator.setInterpolator(new LinearInterpolator());
                                polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                                      List<LatLng> points  = greyPolyline.getPoints() ;
                                      int percentValue = (int) valueAnimator.getAnimatedValue();
                                      int size = points.size();
                                      int newPoints = (int) (size + (percentValue / 100.0f));
                                      List<LatLng> p = points.subList(0,newPoints);
                                      blackPolyline.setPoints(p);

                                    }
                                });
                                polyLineAnimator.start();*/

                                carMarker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                                );

                               /* handler = new Handler();
                                index = -1;
                                next = 1;
                                handler.postDelayed(drawPathRunnable,3000);*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(DriverHome.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {

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

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    //close activty on back press
//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder al = new AlertDialog.Builder(this)
//                .setCancelable(false)
//                .setMessage("Are You Sure You Want To Leave ?");
//        al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        al.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        al.show();
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                        mMap.setMyLocationEnabled(true);
                    }
                    buildLocationCallBack();
                    buildLocationRequest();
                    if (location_switch.isChecked()) {
                        displayLocation();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
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


        //new settings for map
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

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

                                if (location_switch.isChecked()) {

                                    latitude = Common.mLastLocation.getLatitude();
                                    longitude = Common.mLastLocation.getLongitude();


                                    LatLng center = new LatLng(latitude, longitude);


                                    LatLng northside = SphericalUtil.computeOffset(center, 100000, 0);
                                    LatLng southside = SphericalUtil.computeOffset(center, 100000, 100);


                                    LatLngBounds bounds = LatLngBounds.builder()
                                            .include(northside)
                                            .include(southside)
                                            .build();
//
//                                    places.setBoundsBias(bounds);
//                                    places.setFilter(typeFilter);
//


                                    drivers = FirebaseDatabase.getInstance().getReference(Common.driver_tbl).child(Common.currentDriver.getCarType());
                                    geoFire = new GeoFire(drivers);
                                    geoFire.setLocation(FirebaseAuth.getInstance().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                                        @Override
                                        public void onComplete(String key, DatabaseError error) {


                                            if (currentLocationmMarker != null) {
                                                currentLocationmMarker.remove();

                                            }
                                            Log.d("lat = ", "" + latitude);


                                            LatLng latLng = new LatLng(Common.mLastLocation.getLatitude(), Common.mLastLocation.getLongitude());
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            markerOptions.position(latLng);
                                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_source));
                                            markerOptions.title("You are Here");
                                            // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_marker));
                                            currentLocationmMarker = mMap.addMarker(markerOptions);

                                            //below code makes it move very fast
                                                   /* mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                                    mMap.animateCamera(CameraUpdateFactory.zoomBy(15));*/

                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));


                                        }
                                    });


                                }


                            }


                        }
                    });


        } else {

            checkLocationPermission();

        }


    }


    void get_location(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Common.mLastLocation = location;
        if (currentLocationmMarker != null) {
            currentLocationmMarker.remove();

        }
        Log.d("lat = ", "" + latitude);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are Here");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_marker));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

        drivers = FirebaseDatabase.getInstance().getReference("Drivers");
        geoFire = new GeoFire(drivers);
        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {


            }
        });


    }

    void firebaseUpdate() {
        drivers = FirebaseDatabase.getInstance().getReference("Drivers");
        geoFire = new GeoFire(drivers);

        //update to Firebase
        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                //add marker
                if (currentLocationmMarker != null) {
                    currentLocationmMarker.remove(); //remove previous marker
                    currentLocationmMarker = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                            .position(new LatLng(latitude, longitude))
                            .title("You Are Here"));

                    //move camera to your position
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));


                }


            }
        });
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }


    public void chat(View view) {


    }
}