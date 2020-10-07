package com.example.lets_ride_together_driver.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lets_ride_together_driver.ChatStuff.MessageActivity;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Model.PassengerPostedModel;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class PassengerDetailFragment extends Fragment {

    TextView txt_start,txt_end,txt_date_time,txt_name;
    ImageView img_profile;

    private TextView txt_startingpoint,txt_endingpoint,txt_ride_type,txt_trip,txt_date
            ,txt_sunday,txt_monday,txt_tuesday,txt_wednesday,txt_thursday,txt_friday,txt_saturday;

    PassengerPostedModel model;
    private Button request_btn;

    //Firebase

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    private String post_id,id;
    private Button sendchat;
    private String passendgerid;

    public PassengerDetailFragment(String post_id, String id) {
        // Required empty public constructor

        this.id = id;
        this.post_id = post_id;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_passenger_detail, container, false);


        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("PostAsPassenger").child(post_id);


        txt_date = view.findViewById(R.id.txt_date);
        txt_startingpoint = view.findViewById(R.id.txt_startingpoint);
        txt_endingpoint = view.findViewById(R.id.txt_endingpoint);
        txt_ride_type = view.findViewById(R.id.txt_seats);
        txt_trip = view.findViewById(R.id.txt_trip);
        txt_sunday = view.findViewById(R.id.txt_sunday);
        txt_monday = view.findViewById(R.id.txt_monday);
        txt_tuesday = view.findViewById(R.id.txt_tuesday);
        txt_wednesday = view.findViewById(R.id.txt_wednesday);
        txt_thursday = view.findViewById(R.id.txt_thursday);
        txt_friday = view.findViewById(R.id.txt_friday);
        txt_saturday = view.findViewById(R.id.txt_saturday);

        request_btn = view.findViewById(R.id.request_btn);
        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        sendchat = view.findViewById(R.id.sendchat);
        sendchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passendgerid !=null){
                    System.out.println("fhkjfhshfhshf "+passendgerid);
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("userid", passendgerid);
                getActivity().startActivity(intent);
            }}
        });

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                model = dataSnapshot.getValue(PassengerPostedModel.class);
                txt_date.setText(model.getDate());
                passendgerid = model.getId();
                txt_ride_type.setText(model.getRide_type());
                txt_endingpoint.setText(model.getEnding_point());
                txt_startingpoint.setText(model.getStarting_point());
                txt_trip.setText(model.getTrip());



                String[] days = model.getSchedule().split(",");

                for (int i = 0; i < days.length -1; i++) {

                    switch (days[i]){

                        case "mon":{


                            txt_monday.setTextColor(getResources().getColor(R.color.white));
                            txt_monday.setBackground(getResources().getDrawable(R.drawable.days_bg_driver_detail_farg));
                            txt_monday.setPadding(8,8,8,8);

                            txt_monday.setTextSize(10.0f);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(4,4,4,4);
                            txt_monday.setLayoutParams(params);

                            break;
                        }
                        case "tue":{

                            txt_tuesday.setTextColor(getResources().getColor(R.color.white));
                            txt_tuesday.setBackground(getResources().getDrawable(R.drawable.days_bg_driver_detail_farg));
                            txt_tuesday.setPadding(8,8,8,8);
                            txt_tuesday.setTextSize(10.0f);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(4,4,4,4);
                            txt_tuesday.setLayoutParams(params);

                            break;
                        }
                        case "wed":{

                            txt_wednesday.setTextColor(getResources().getColor(R.color.white));
                            txt_wednesday.setBackground(getResources().getDrawable(R.drawable.days_bg_driver_detail_farg));
                            txt_wednesday.setPadding(8,8,8,8);
                            txt_wednesday.setTextSize(10.0f);


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(4,4,4,4);
                            txt_wednesday.setLayoutParams(params);

                            break;
                        }
                        case "thu":{
                            txt_thursday.setTextColor(getResources().getColor(R.color.white));
                            txt_thursday.setBackground(getResources().getDrawable(R.drawable.days_bg_driver_detail_farg));
                            txt_thursday.setPadding(8,8,8,8);
                            txt_thursday.setTextSize(10.0f);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(4,4,4,4);
                            txt_thursday.setLayoutParams(params);

                            break;
                        }
                        case "fri":{
                            txt_friday.setTextColor(getResources().getColor(R.color.white));
                            txt_friday.setBackground(getResources().getDrawable(R.drawable.days_bg_driver_detail_farg));
                            txt_friday.setPadding(8,8,8,8);
                            txt_friday.setTextSize(10.0f);


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(4,4,4,4);
                            txt_friday.setLayoutParams(params);


                            break;
                        }
                        case "sat":{

                            txt_saturday.setTextColor(getResources().getColor(R.color.white));
                            txt_saturday.setBackground(getResources().getDrawable(R.drawable.days_bg_driver_detail_farg));
                            txt_saturday.setPadding(8,8,8,8);
                            txt_saturday.setTextSize(10.0f);


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(4,4,4,4);
                            txt_saturday.setLayoutParams(params);

                            break;
                        }
                        case "sun":{
                            txt_sunday.setTextColor(getResources().getColor(R.color.white));
                            txt_sunday.setBackground(getResources().getDrawable(R.drawable.days_bg_driver_detail_farg));
                            txt_sunday.setPadding(8,8,8,8);
                            txt_sunday.setTextSize(10.0f);


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(4,4,4,4);
                            txt_sunday.setLayoutParams(params);

                            break;
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
    public void showDialog(){


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.ride_confirm_dialog);



        FirebaseDatabase data = FirebaseDatabase.getInstance();
        mRef = data.getReference("Users").child("Passengers").child(id);

        txt_start = dialog.findViewById(R.id.txt_startingpoint);
        txt_end = dialog.findViewById(R.id.txt_endingpoint);

        txt_name = dialog.findViewById(R.id.txt_driver_name);
        txt_date_time = dialog.findViewById(R.id.txt_date_time);

        img_profile = dialog.findViewById(R.id.driver_profile_img);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);

                txt_name.setText(model.getName());

                if(model.getProfile_img() == null){
                    img_profile.setImageResource(R.drawable.default_profile);
                }else {
                    Picasso.get().load(model.getProfile_img()).into(img_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Button btn_request = dialog.findViewById(R.id.btn_confirm);

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase myData = FirebaseDatabase.getInstance();
                DatabaseReference dataRef = myData.getReference("PassengerRequest");

                Map<String, String> map = new HashMap<>();
                map.put("post_id",post_id);
                map.put("passenger_id",id);
                map.put("sender_id", Common.currentDriver.getuId());
                map.put("status","Pending");
                dataRef.push().setValue(map);

                Toast.makeText(getContext(), "Request Sent!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        txt_start.setText(txt_startingpoint.getText().toString());
        txt_end.setText(txt_endingpoint.getText().toString());
        txt_date.setText(this.txt_date.getText().toString());


        dialog.show();
    }

}
