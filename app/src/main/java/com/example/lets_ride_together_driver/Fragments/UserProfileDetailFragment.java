package com.example.lets_ride_together_driver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserProfileDetailFragment extends Fragment {


    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    TextView txt_email,txt_phone,txt_city,txt_vehicl_type, txt_vehicle_name, txt_vehicle_number;


    public UserProfileDetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile_detail, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users").child("Drivers").child(Common.currentDriver.getuId());

        txt_email = view.findViewById(R.id.txt_email);
        txt_phone = view.findViewById(R.id.txt_phone);
        txt_city = view.findViewById(R.id.txt_city);
        txt_vehicl_type = view.findViewById(R.id.txt_vehicle_type);
        txt_vehicle_name = view.findViewById(R.id.txt_vehicle_name);
        txt_vehicle_number = view.findViewById(R.id.txt_vehicle_no);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    UserModel model = dataSnapshot.getValue(UserModel.class);

                    txt_email.setText(model.getEmail());
                    txt_phone.setText(model.getPhone());
                    txt_city.setText(model.getCity());
                    txt_vehicl_type.setText(model.getCar_type());
                    txt_vehicle_name.setText(model.getVehicle_name());
                    txt_vehicle_number.setText(model.getVehicle_number());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
