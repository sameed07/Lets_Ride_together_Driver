package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lets_ride_together_driver.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Adapter.MyViewPageAdapter;
import Fragments.PassengerDetailFragment;
import Fragments.PassengerReviewFragment;
import Model.UserModel;

public class PassengerRequestActivity extends AppCompatActivity {

    private String id, post_key;

    private ImageView img_driver,back_img,fvrt_img;
    private TextView txt_name,txt_carname;

    //firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private TabLayout MyTabs;
    private ViewPager MyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passgenger_request);


        id = getIntent().getStringExtra("driver_id");
        post_key = getIntent().getStringExtra("post_key");

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users").child("Passengers");

        img_driver = findViewById(R.id.driver_profile_img);

        txt_name = findViewById(R.id.txt_driver_name);

        back_img = findViewById(R.id.back_img);
        fvrt_img = findViewById(R.id.fvrt_img);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PassengerRequestActivity.this,CarpoolMainActivity.class));
                finish();
            }
        });
        MyTabs = (TabLayout)findViewById(R.id.MyTabs);
        MyPage = (ViewPager)findViewById(R.id.MyPage);
        //MyTabs.setSelectedTabIndicatorColor(Color.parseColor("#2AC940"));
//        MyTabs.setSelectedTabIndicatorHeight((int) (1 * getResources().getDisplayMetrics().density));
       // MyTabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));
        // MyTabs.setPadding(10,10,10,10);

        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);

        mRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel model = dataSnapshot.getValue(UserModel.class);

                //Toast.makeText(DriverProfile.this, "" + model.getName(), Toast.LENGTH_SHORT).show();
                txt_name.setText(model.getName());

                Picasso.get().load(model.getProfile_img()).into(img_driver);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i  = new Intent(PassengerRequestActivity.this,PassengerProfileActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });

    }

    public void SetUpViewPager (ViewPager viewpage){
        MyViewPageAdapter Adapter = new MyViewPageAdapter(getSupportFragmentManager());

        Adapter.AddFragmentPage(new PassengerDetailFragment(post_key,id), "Post Detail");
        //Adapter.AddFragmentPage(new PassengerReviewFragment(post_key,id), "Reviews");


        viewpage.setAdapter(Adapter);

    }
}
