package com.example.lets_ride_together_driver.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lets_ride_together_driver.Adapter.MyProfileViewPager;
import com.example.lets_ride_together_driver.Fragments.PassengerProfileDetailFragment;
import com.example.lets_ride_together_driver.Fragments.UserReviewFragment;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PassengerProfileActivity extends AppCompatActivity {

    String id;

    TabLayout MyTabs;
    ViewPager MyPage;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    private ImageView passenger_img,edt_img;
    private TextView txt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        id = getIntent().getStringExtra("id");

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users").child("Passengers").child(id);

        txt_name = findViewById(R.id.txt_passenger_name);
        passenger_img = findViewById(R.id.passenger_profile_img);

        //Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();

        //get name
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                UserModel model = dataSnapshot.getValue(UserModel.class);
                txt_name.setText(model.getName());

                if(model.getProfile_img() == null){
                    passenger_img.setImageResource(R.drawable.default_pic);
                }else {
                    Picasso.get().load(model.getProfile_img()).into(passenger_img);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        MyTabs = (TabLayout)findViewById(R.id.MyTabs);
        MyPage = (ViewPager)findViewById(R.id.MyPage);
        MyTabs.setSelectedTabIndicatorColor(Color.parseColor("#2AC940"));
//        MyTabs.setSelectedTabIndicatorHeight((int) (1 * getResources().getDisplayMetrics().density));
        MyTabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));


        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);
    }

    public void SetUpViewPager (ViewPager viewpage){
        MyProfileViewPager Adapter = new MyProfileViewPager(getSupportFragmentManager());

        Adapter.AddFragmentPage(new PassengerProfileDetailFragment(id), "Personal Detail");
        Adapter.AddFragmentPage(new UserReviewFragment(), "Reviews");


        viewpage.setAdapter(Adapter);

    }
}
