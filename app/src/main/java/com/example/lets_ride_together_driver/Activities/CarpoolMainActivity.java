package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.lets_ride_together_driver.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.CarpoolHomeFragment;
import Fragments.ChatFragment;
import Fragments.ProfileFragment;
import Fragments.RideRequestFragment;
import Fragments.RidesFragment;

public class CarpoolMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new CarpoolHomeFragment());


    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.bot_home:
                            openFragment(new CarpoolHomeFragment());

                            return true;
                        case R.id.bot_msg:
                            openFragment(new ChatFragment());
                            return true;
                        case R.id.bot_post_ride:
                            openFragment(new RidesFragment());
                            return true;
                        case R.id.bot_requests:
                            openFragment(new RideRequestFragment());
                            return true;
                        case R.id.bot_profile:
                            openFragment(new ProfileFragment());
                            return true;
                    }
                    return false;
                }
            };



    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
