package com.example.lets_ride_together_driver.ChatStuff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.lets_ride_together_driver.Activities.DriverHome;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Fragments.ChatFragment;
import com.example.lets_ride_together_driver.Fragments.ChatsFragment;
import com.example.lets_ride_together_driver.Fragments.UsersFragment;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.NewModel.Chat;
import com.example.lets_ride_together_driver.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageMainActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;


    DatabaseReference reference;


    private String uid;
    private String fullname;
    private String profileimageurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);


        DatabaseReference refe = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl).child(Common.currentDriver.getuId());

        refe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                final UserModel driver = dataSnapshot.getValue(UserModel.class);
                username.setText(driver.getName());
                if (driver.getProfile_img().equals("")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {

                    //change this

                    Picasso.get().load(driver.getProfile_img()).placeholder(R.mipmap.ic_launcher).into(profile_image);
                    //    Glide.with(getApplicationContext()).load(profileimageurl).into(profile_image);
                }
              //  System.out.println("sdhfsdfs "+dataSnapshot);
                reference = FirebaseDatabase.getInstance().getReference("Chats");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("sdhfsdfs "+dataSnapshot);
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                        int unread = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chat chat = snapshot.getValue(Chat.class);
                            if (chat.getReceiver().equals(Common.currentDriver.getuId()) && !chat.isIsseen()) {
                                unread++;
                            }
                        }

                        if (unread == 0) {
                            viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                        } else {
                            viewPagerAdapter.addFragment(new ChatsFragment(), "(" + unread + ") Chats");
                        }

                        viewPagerAdapter.addFragment(new UsersFragment(), "Users");


                        viewPager.setAdapter(viewPagerAdapter);

                        tabLayout.setupWithViewPager(viewPager);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//            viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
//
//        viewPagerAdapter.addFragment(new UsersFragment(), "Users");
//
//
//        viewPager.setAdapter(viewPagerAdapter);
//
//        tabLayout.setupWithViewPager(viewPager);
//make user online

    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        status("true");
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("true");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        status("false");
//    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DriverHome.class));
        finish();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
