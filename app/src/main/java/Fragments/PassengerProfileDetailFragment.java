package Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.UserModel;


public class PassengerProfileDetailFragment extends Fragment {


    TextView txt_phone,txt_email,txt_city;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;


    private String id;
    public PassengerProfileDetailFragment(String id) {
        // Required empty public constructor
        this.id = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_passenger_profile_detail, container, false);


        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users").child("Passengers").child(id);

        txt_city = v.findViewById(R.id.txt_city);
        txt_phone = v.findViewById(R.id.txt_phone);
        txt_email = v.findViewById(R.id.txt_email);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                UserModel model = dataSnapshot.getValue(UserModel.class);

                txt_email.setText(model.getEmail());
                txt_phone.setText(model.getPhone());
                txt_city.setText(model.getCity());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }


}
