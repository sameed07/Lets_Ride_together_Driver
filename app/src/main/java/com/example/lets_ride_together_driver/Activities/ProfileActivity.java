package com.example.lets_ride_together_driver.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lets_ride_together_driver.Adapter.MyProfileViewPager;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Fragments.UserProfileDetailFragment;
import com.example.lets_ride_together_driver.Fragments.UserReviewFragment;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {


    TabLayout MyTabs;
    ViewPager MyPage;

    private ImageView driver_img,edt_img;
    private TextView txt_name;



    private Uri filePath;
    private String mUrl;

    int CAM_CONS = 1;
    String mCameraFileName;


    Bitmap mbitmap;

    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users").child("Drivers").child(Common.currentDriver.getuId());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Images");




        MyTabs = (TabLayout)findViewById(R.id.MyTabs);
        MyPage = (ViewPager)findViewById(R.id.MyPage);
        MyTabs.setSelectedTabIndicatorColor(Color.parseColor("#2AC940"));
//        MyTabs.setSelectedTabIndicatorHeight((int) (1 * getResources().getDisplayMetrics().density));
        MyTabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));

        driver_img = findViewById(R.id.driver_profile_img);
        edt_img = findViewById(R.id.edt_img);
        txt_name = findViewById(R.id.txt_driver_name);

        edt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,EditProfile.class));
            }
        });

        driver_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // chooseImage();

            }
        });

        //get name
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    txt_name.setText(model.getName());

                    if(model.getProfile_img() == null){
                        driver_img.setImageResource(R.drawable.default_pic);
                    }else {
                        Picasso.get().load(model.getProfile_img()).into(driver_img);
                    }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);
    }
    public void SetUpViewPager (ViewPager viewpage){
        MyProfileViewPager Adapter = new MyProfileViewPager(getSupportFragmentManager());

        Adapter.AddFragmentPage(new UserProfileDetailFragment(), "Personal Detail");
        Adapter.AddFragmentPage(new UserReviewFragment(), "Reviews");


        viewpage.setAdapter(Adapter);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                driver_img.setImageBitmap(bitmap);


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }



}
