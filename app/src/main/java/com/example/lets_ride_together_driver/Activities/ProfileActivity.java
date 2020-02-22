package com.example.lets_ride_together_driver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;

import com.example.lets_ride_together_driver.R;
import com.google.android.material.tabs.TabLayout;

import Adapter.MyViewPageAdapter;
import Fragments.UserProfileDetailFragment;
import Fragments.UserTripHistoryFragment;

public class ProfileActivity extends AppCompatActivity {


    TabLayout MyTabs;
    ViewPager MyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MyTabs = (TabLayout)findViewById(R.id.MyTabs);
        MyPage = (ViewPager)findViewById(R.id.MyPage);
        MyTabs.setSelectedTabIndicatorColor(Color.parseColor("#2AC940"));
//        MyTabs.setSelectedTabIndicatorHeight((int) (1 * getResources().getDisplayMetrics().density));
        MyTabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));

        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);
    }
    public void SetUpViewPager (ViewPager viewpage){
        MyViewPageAdapter Adapter = new MyViewPageAdapter(getSupportFragmentManager());

        Adapter.AddFragmentPage(new UserProfileDetailFragment(), "Personal Detail");
        Adapter.AddFragmentPage(new UserTripHistoryFragment(), "History");


        viewpage.setAdapter(Adapter);

    }

}
