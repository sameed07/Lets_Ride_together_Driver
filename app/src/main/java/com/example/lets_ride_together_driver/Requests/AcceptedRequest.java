package com.example.lets_ride_together_driver.Requests;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Adapter.PostAdapter;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.PostDriver;
import com.example.lets_ride_together_driver.NewModel.PostRider;
import com.example.lets_ride_together_driver.ProfilePageStuff.MyAddedPosts;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class AcceptedRequest extends Fragment {


    String currentUserUid;
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    ListView listView;
    ArrayList<String> myStringData = new ArrayList<>();
    private MyAddedPosts.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<PostRider> listrider = new ArrayList<>();
    private ArrayList<String> myStringDataForNextActivity = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_accepted_request, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences(Common.MY_SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");


        recyclerView = view.findViewById(R.id.rec_acceptedreq_posts);


        listView = view.findViewById(R.id.mylistV);


        recyclerView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        myRef = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(currentUserUid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
                System.err.println("mydata" + dataSnapshot.getKey());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    myStringData.add("Requests For Post No " + i);
                    myStringDataForNextActivity.add(ds.getKey());
                    i++;

                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myStringData);

                listView.setAdapter(arrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(getActivity(), AcceptActivityPart2.class);

                        intent.putExtra("myid", myStringDataForNextActivity.get(position));
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;


    }


}
