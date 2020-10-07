package com.example.lets_ride_together_driver.ProfilePageStuff;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Adapter.PostAdapter;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.PostDriver;
import com.example.lets_ride_together_driver.NewModel.PostRider;
import com.example.lets_ride_together_driver.NewModel.RequestsModel;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class RecivedRequestsActivity extends AppCompatActivity {
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<PostRider> listrider = new ArrayList<>();
    private String currentUserUid;

    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recived_requests);

        toolbar = findViewById(R.id.req_toolbar);
        if (getIntent() != null) {
            postId = getIntent().getStringExtra("id");

            System.err.println("podtid" + postId);
        }


        SharedPreferences prefs = getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");


        recyclerView = findViewById(R.id.rec_my_requests1);

        myRef = FirebaseDatabase.getInstance().getReference("Requests").child(currentUserUid).child(postId);


        postAdapter = new PostAdapter(true, getApplicationContext(), postDriverArrayList, listrider, "not");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(postAdapter);


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnap, @Nullable String s) {
                RequestsModel mode = dataSnap.getValue(RequestsModel.class);

                //to avoid nuul exception
                postDriverArrayList.add(new PostDriver("", mode.getPostid(), "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getReciverid(), "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid(), ""));


                listrider.add(new PostRider("", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                toolbar.setTitle("Recived Requests For Post (" + postDriverArrayList.size() + ")");
                postAdapter.notifyDataSetChanged();

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


    }
}
