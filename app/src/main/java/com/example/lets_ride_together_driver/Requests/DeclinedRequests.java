package com.example.lets_ride_together_driver.Requests;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Adapter.PostAdapter;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.PostDriver;
import com.example.lets_ride_together_driver.NewModel.PostRider;
import com.example.lets_ride_together_driver.NewModel.RequestsModel;
import com.example.lets_ride_together_driver.ProfilePageStuff.MyAddedPosts;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class DeclinedRequests extends Fragment {

    String currentUserUid;
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    private MyAddedPosts.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<PostRider> listrider = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_declined_requests, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");


        recyclerView = view.findViewById(R.id.rec_declinedreq_posts);


        myRef = FirebaseDatabase.getInstance().getReference("Declined Requests").child(currentUserUid);

        postAdapter = new PostAdapter(true, getActivity(), postDriverArrayList, listrider, "not");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postAdapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnap, @Nullable String s) {

                //        for (DataSnapshot ds:dataSnap.getChildren()) {

                RequestsModel mode = dataSnap.getValue(RequestsModel.class);

                //to avoid nuul exception
                postDriverArrayList.add(new PostDriver("", mode.getPostid(), "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getReciverid(), "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid(), ""));


                listrider.add(new PostRider("", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                postAdapter.notifyDataSetChanged();

                //    }


                // System.err.println("driver oput is " + value.getFullname());

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }


}
