package com.example.lets_ride_together_driver.Requests;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Adapter.PostAdapter;
import com.example.lets_ride_together_driver.AllPostsWork.ShowRouteOnMap;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.DataMessage;
import com.example.lets_ride_together_driver.NewModel.FCMResponse;
import com.example.lets_ride_together_driver.NewModel.PostDriver;
import com.example.lets_ride_together_driver.NewModel.PostRider;
import com.example.lets_ride_together_driver.NewModel.RequestsModel;
import com.example.lets_ride_together_driver.NewModel.Token;
import com.example.lets_ride_together_driver.Notifications.Client;
import com.example.lets_ride_together_driver.ProfilePageStuff.MyAddedPosts;
import com.example.lets_ride_together_driver.R;
import com.example.lets_ride_together_driver.Remote.IFCMService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AcceptActivityPart2 extends AppCompatActivity {

    String currentUserUid;
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    ArrayList<RequestsModel> requestsModelArrayList = new ArrayList<>();
    Button startrideforpostbtn;
    String idfromint;
    IFCMService apiService;
    private MyAddedPosts.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<PostRider> listrider = new ArrayList<>();

    private String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_part2);


        SharedPreferences prefs = getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");

        fullname = prefs.getString("name", "");
        recyclerView = findViewById(R.id.rec_acceptedreq_posts);

        startrideforpostbtn = findViewById(R.id.startrideforpost);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(IFCMService.class);
        if (getIntent() != null) {
            idfromint = getIntent().getStringExtra("myid");
        }

        if (getIntent() != null) {


            idfromint = getIntent().getStringExtra("myid");

            System.err.println("mychect" + idfromint + "?????" + currentUserUid);


            Toast.makeText(this, "Long press on item to delete it!", Toast.LENGTH_SHORT).show();

            myRef = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(currentUserUid).child(idfromint);

            postAdapter = new PostAdapter(true, this, postDriverArrayList, listrider, "Daccept");
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(postAdapter);


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        RequestsModel mode = ds.getValue(RequestsModel.class);

                        requestsModelArrayList.add(mode);

                        //to avoid nuul exception
                        postDriverArrayList.add(new PostDriver("", mode.getPostid(), "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getReciverid(), "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid(), ""));


                        listrider.add(new PostRider("", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                        postAdapter.notifyDataSetChanged();
                        System.err.println("driver oput is " + mode.getSendername());

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            Toast.makeText(this, "Error!! Reload App", Toast.LENGTH_SHORT).show();
        }


        startrideforpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AcceptActivityPart2.this);
                builder1.setMessage("Are You Sure You Want to Start Ride?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                                sendrequesttodriversforridestart();


                                DatabaseReference re = FirebaseDatabase.getInstance().getReference("PostsAsDriver").child(idfromint);

                                re.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        PostDriver driver = dataSnapshot.getValue(PostDriver.class);


                                        Intent intent = new Intent(AcceptActivityPart2.this, ShowRouteOnMap.class);

                                        intent.putExtra("latstart", driver.getLatstart());

                                        intent.putExtra("latend", driver.getLatend());
                                        intent.putExtra("lngstart", driver.getLngstart());
                                        intent.putExtra("lngend", driver.getLngend());
                                        intent.putExtra("title", "request");


                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });


    }


    void sendrequesttodriversforridestart() {


        for (int i = 0; i < requestsModelArrayList.size(); i++) {

            final int p = i;
            DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
            Query query = tokens.orderByKey().equalTo(requestsModelArrayList.get(i).getSenderid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Token token = snapshot.getValue(Token.class);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("title", "request");
                        map.put("reciver", requestsModelArrayList.get(p).getSenderid());
                        map.put("drivername", fullname);

                        DataMessage sender = new DataMessage(token.getToken(), map);

                        apiService.sendMessage(sender)
                                .enqueue(new Callback<FCMResponse>() {
                                    @Override
                                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}
