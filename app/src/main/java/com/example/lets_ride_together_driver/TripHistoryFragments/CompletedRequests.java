package com.example.lets_ride_together_driver.TripHistoryFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Adapter.TripDetailsAdapter;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.TripHistory;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CompletedRequests extends Fragment {
    LinearLayoutManager layoutManager;
    private ArrayList<TripHistory> myList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private TripDetailsAdapter postAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_requests, container, false);


        recyclerView = view.findViewById(R.id.rec_list_all_requests);


        postAdapter = new TripDetailsAdapter(getActivity(), myList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(postAdapter);
        // recyclerView.setAdapter(postAdapter);

        myRef = FirebaseDatabase.getInstance().getReference(Common.driver_TripHistory_tbl).child(FirebaseAuth.getInstance().getUid()).child(Common.driver_TripCompleted_tbl);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    TripHistory value = ds.getValue(TripHistory.class);
                    System.err.println("mydatais" + value.getAddress());

                    myList.add(value);

                    postAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}