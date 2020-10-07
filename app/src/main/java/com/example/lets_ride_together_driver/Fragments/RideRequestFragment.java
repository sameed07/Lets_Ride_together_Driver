package com.example.lets_ride_together_driver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Adapter.RequestAdapter;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Model.PassengerRequest;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RideRequestFragment extends Fragment {


    RecyclerView request_recycler;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    List<PassengerRequest> mList = new ArrayList<>();

    public RideRequestFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_request, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("PassengerRequest");



        request_recycler = view.findViewById(R.id.request_recycler);
        layoutManager  =  new LinearLayoutManager(getContext());
        request_recycler.setLayoutManager(layoutManager);

        mRef.orderByChild("sender_id").equalTo(Common.currentDriver.getuId()).addValueEventListener(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            PassengerRequest post = postSnapshot.getValue(PassengerRequest.class);


                            mList.add(post);

                            RequestAdapter adapter = new RequestAdapter(mList,getContext());
                            request_recycler.setAdapter(adapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

        return view;
    }

}
