package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lets_ride_together_driver.Activities.PostRequestActivity;
import com.example.lets_ride_together_driver.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class RidesFragment extends Fragment {

    FloatingActionButton post_fab;

    public RidesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_rides, container, false);
        post_fab = view.findViewById(R.id.post_fab);
        post_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostRequestActivity.class));
            }
        });

        return view;
    }

}
