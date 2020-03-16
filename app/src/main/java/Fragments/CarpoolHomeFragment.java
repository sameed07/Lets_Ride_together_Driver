package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lets_ride_together_driver.Activities.ProfileActivity;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Adapter.PassengerPostsAdapter;
import Common.Common;
import Model.PassengerPostedModel;
import Model.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;


public class CarpoolHomeFragment extends Fragment {

    private RecyclerView postlist_recycler;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txt_toolbar;
    private CircleImageView user_profileImage;

    private List<PassengerPostedModel> pList = new ArrayList<>();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;


    public CarpoolHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_carpool_home_fragmet, container, false);

        //toolbar
        FirebaseDatabase mdata = FirebaseDatabase.getInstance();
        DatabaseReference ref = mdata.getReference("Users").child("Drivers").child(Common.currentDriver.getuId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);

                //Toast.makeText(DriverProfile.this, "" + model.getName(), Toast.LENGTH_SHORT).show();
                txt_toolbar.setText(model.getName());

                Picasso.get().load(model.getProfile_img()).into(user_profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //firebase init
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("PostAsPassenger");

        postlist_recycler = view.findViewById(R.id.post_ride_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        postlist_recycler.setLayoutManager(layoutManager);
        txt_toolbar = view.findViewById(R.id.txt_toolbar);
        txt_toolbar.setText("User Name");
        user_profileImage = view.findViewById(R.id.user_profile_img);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        user_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    PassengerPostedModel post = postSnapshot.getValue(PassengerPostedModel.class);
                    pList.add(post);
                  //  Toast.makeText(getContext(), "" + post.getRide_type(), Toast.LENGTH_SHORT).show();
                    PassengerPostsAdapter adapter = new PassengerPostsAdapter(getContext(), pList);
                    postlist_recycler.setAdapter(adapter);

//                    Toast.makeText(getContext(), ""+ post.getProfile_img() , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        //passing dummy data
//        ArrayList<PassengerPostedModel> postList = new ArrayList<>();
//
//        postList.add(new PassengerPostedModel("After Earth"
//                ,"Bike","12-3-2020","09:00 PM", "Round Trip",
//                "Pindi Road Kohat Cantt","Kohat University of science and technology"));
//        postList.add(new PassengerPostedModel("After Earth"
//                ,"Bike","12-3-2020","09:00 PM", "Round Trip",
//                "Pindi Road Kohat Cantt","Kohat University of science and technology"));
//        postList.add(new PassengerPostedModel("After Earth"
//                ,"Bike","12-3-2020","09:00 PM", "Round Trip",
//                "Pindi Road Kohat Cantt","Kohat University of science and technology"));
//        postList.add(new PassengerPostedModel("After Earth"
//                ,"Bike","12-3-2020","09:00 PM", "Round Trip",
//                "Pindi Road Kohat Cantt","Kohat University of science and technology"));
//        postList.add(new PassengerPostedModel("After Earth"
//                ,"Bike","12-3-2020","09:00 PM", "Round Trip",
//                "Pindi Road Kohat Cantt","Kohat University of science and technology"));
//
//
//
//        PassengerPostsAdapter adapter = new PassengerPostsAdapter(getContext(), postList);
//        postlist_recycler.setAdapter(adapter);


        return view;
    }


}
