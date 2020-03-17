package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lets_ride_together_driver.Activities.PostRequestActivity;
import com.example.lets_ride_together_driver.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.MyRideAdapter;
import Common.Common;
import Model.DriverPost;


public class RidesFragment extends Fragment {

    private RecyclerView rides_recycler;
    private LinearLayoutManager linearLayoutManager;



    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    List<DriverPost> postList = new ArrayList<>();

    FloatingActionButton post_fab;

    public RidesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_rides, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("PostAsDriver");

        rides_recycler = view.findViewById(R.id.my_rides_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rides_recycler.setLayoutManager(linearLayoutManager);

        post_fab = view.findViewById(R.id.post_fab);
        post_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostRequestActivity.class));
            }
        });

        mRef.orderByChild("id").equalTo(Common.currentDriver.getuId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DriverPost post = postSnapshot.getValue(DriverPost.class);
                    post.setPostKey(postSnapshot.getKey());

                    postList.add(post);
                    MyRideAdapter adapter = new MyRideAdapter(getContext(), postList);
                    rides_recycler.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
