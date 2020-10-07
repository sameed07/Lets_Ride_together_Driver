package com.example.lets_ride_together_driver.Fragments;

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
import com.example.lets_ride_together_driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ListAllPostsRider extends Fragment {

    RecyclerView recyclerView;
    PostAdapter postAdapter;
    ArrayList<PostRider> postRiderArrayList = new ArrayList<>();
    DatabaseReference myRef;
    String currentUserUid;
    private ArrayList<PostDriver> list = new ArrayList<>();
    private FirebaseAuth mAuth;


    public ListAllPostsRider() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_all_posts_rider, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");

        recyclerView = view.findViewById(R.id.rec_posts_list_all_posts_Rider);

        myRef = FirebaseDatabase.getInstance().getReference("PostsAsPassenger");

        postAdapter = new PostAdapter(getActivity(), list, postRiderArrayList, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postAdapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//to avoid nuul exception
                list.add(new PostDriver("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));


                PostRider value = dataSnapshot.getValue(PostRider.class);

                if (!currentUserUid.equals(value.getUid())) {
                    postRiderArrayList.add(value);
                    postAdapter.notifyDataSetChanged();
                }
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
