package com.example.lets_ride_together_driver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.lets_ride_together_driver.R;


public class PassengerReviewFragment extends Fragment {

    private String post_id,id;

    public PassengerReviewFragment(String post_id,String id) {
        // Required empty public constructor

        this.post_id = post_id;
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_review, container, false);

        return view;
    }


}
