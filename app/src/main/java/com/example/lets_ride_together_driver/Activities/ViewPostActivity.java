package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import Adapter.PassengerRequestAdapter;
import Fragments.RidesFragment;
import Model.DriverPost;
import Model.DriverRequest;

public class ViewPostActivity extends AppCompatActivity {

    private TextView txt_starting,txt_ending;
    private ImageView img_back;
    private Button btn_start;

    RecyclerView request_recycler;
    RecyclerView.LayoutManager manager;

    String post_id;

    //firebase
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    List<DriverRequest> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);


        post_id = getIntent().getStringExtra("post_key");

        request_recycler = findViewById(R.id.request_recycler);
        manager = new LinearLayoutManager(this);
        request_recycler.setLayoutManager(manager);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("PostAsDriver").child(post_id);

        txt_ending = findViewById(R.id.txt_endingpoint);
        txt_starting = findViewById(R.id.txt_startingpoint);
        btn_start = findViewById(R.id.btn_start_ride);


//
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(ViewPostActivity.this, RidesFragment.class));
////                finish();
//            }
//        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DriverPost post = dataSnapshot.getValue(DriverPost.class);
                txt_starting.setText(post.getStarting_point());
                txt_ending.setText(post.getEnding_point());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("DriverRequests");
        ref.orderByChild("post_id").equalTo(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    DriverRequest request= ds.getValue(DriverRequest.class);
                    mList.add(request);

                    PassengerRequestAdapter adapter = new PassengerRequestAdapter(ViewPostActivity.this,mList);
                    request_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
