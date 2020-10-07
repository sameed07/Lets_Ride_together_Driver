package com.example.lets_ride_together_driver.ProfilePageStuff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.lets_ride_together_driver.Activities.HomePageMap;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;


public class ProfilePage extends AppCompatActivity implements
        MySentRequests.OnFragmentInteractionListener,
        MyAddedPosts.OnFragmentInteractionListener {


    CircularImageView circularImageView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar app_toolbar;
    AppBarLayout appBarLayout;
    TextView UserNameTxt, TypeUserTxt, EmailUserTxt;
    private String email_user, type_user, photo_url_user, fullname_user;
    private ImageView ImageViewNav, main_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        circularImageView = findViewById(R.id.image);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        app_toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout);


        app_toolbar.setTitle("My Profile");

        setSupportActionBar(app_toolbar);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                int j = collapsingToolbarLayout.getExpandedTitleMarginTop() * 2;


                float f = ((float) (i + j)) / ((float) j);
                circularImageView.setScaleX(f >= 0.0f ? f : 0.0f);
                CircularImageView circularImagev = circularImageView;
                if (f < 0.0f) {
                    f = 0.0f;
                }
                circularImagev.setScaleY(f);
            }
        });


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("My Posts", MyAddedPosts.class)
                .add("Approved Requests", MySentRequests.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager_profilepage);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab_profilepage);
        viewPagerTab.setViewPager(viewPager);
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Toast.makeText(ProfilePage.this, "Changed to " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        SharedPreferences prefs = getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE);


        photo_url_user = prefs.getString("imageUri", "");
        email_user = prefs.getString("phone", "");
        type_user = prefs.getString("car_type", "");
        fullname_user = prefs.getString("name", "");


        ImageViewNav = findViewById(R.id.image_header);
        main_image = findViewById(R.id.image);
        UserNameTxt = findViewById(R.id.name_txt_profile);
        TypeUserTxt = findViewById(R.id.type_txt_profie);
        EmailUserTxt = findViewById(R.id.email_txt_profie);


        UserNameTxt.setText(fullname_user);
        TypeUserTxt.setText(type_user);
        EmailUserTxt.setText(email_user);

        Picasso.get()
                .load(photo_url_user)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(main_image);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomePageMap.class));
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
