package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.lets_ride_together_driver.Activities.Instant_HomeActivity;
import com.example.lets_ride_together_driver.Activities.ProfileActivity;
import com.example.lets_ride_together_driver.R;

public class ProfileFragment extends Fragment {

    private Switch carpool_switch;
    private LinearLayout profile_layout, upgrade_car_layout, support_layout,logout_layout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_profile, container, false);

        profile_layout = view.findViewById(R.id.profile_layout);
        upgrade_car_layout = view.findViewById(R.id.upgrade_car_layout);
        support_layout = view.findViewById(R.id.support_layout);
        logout_layout = view.findViewById(R.id.logout_layout);

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        carpool_switch = view.findViewById(R.id.carpool_switch);
        carpool_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

                    startActivity(new Intent(getActivity(), Instant_HomeActivity.class));

                }
            }
        });


        return view;
    }

}
