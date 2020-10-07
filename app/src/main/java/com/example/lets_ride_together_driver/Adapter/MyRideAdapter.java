package com.example.lets_ride_together_driver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Activities.ViewPostActivity;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Model.DriverPost;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRideAdapter extends RecyclerView.Adapter<MyRideAdapter.ViewHolder> {

    Context mContext;
    List<DriverPost> mList;

    public MyRideAdapter(Context mContext, List<DriverPost> mList) {

        this.mContext = mContext;
        this.mList = mList;

    }

    @NonNull
    @Override
    public MyRideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_rides_adapter
        ,parent
        ,false);

        return new MyRideAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRideAdapter.ViewHolder holder, int position) {

        final DriverPost currentItem = mList.get(position);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference("Users").child("Drivers").child(Common.currentDriver.getuId());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                holder.txt_driverName.setText(model.getName());
                holder.txt_carType.setText(model.getVehicle_name());

                if(model.getProfile_img() == null){
                    holder.driver_profile.setImageResource(R.drawable.default_profile);
                }else {
                    Picasso.get().load(model.getProfile_img()).into(holder.driver_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.txt_rate.setText("Rs." + currentItem.getPrice());
        holder.txt_seats_available.setText(currentItem.getSeats() + " Seats Available");
        holder.txt_currentLocation.setText(currentItem.getStarting_point());
        holder.txt_destination.setText(currentItem.getEnding_point());
        holder.txt_date.setText(currentItem.getDate());
        //holder.txt_tripDetails.setText(currentItem.getTrip_detail());


        holder.btn_view_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent i = new Intent(mContext, ViewPostActivity.class);
                 i.putExtra("driver_id",currentItem.getId());
                  i.putExtra("post_key",currentItem.getPostKey());
                 mContext.startActivity(i);
            }
        });
        holder.txt_driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_driverName,txt_rate,txt_carType,txt_seats_available,txt_date,
        txt_currentLocation, txt_destination;

        Button btn_view_post;
        ImageView driver_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_view_post = itemView.findViewById(R.id.btn_request);
            driver_profile = itemView.findViewById(R.id.driver_profile);
            txt_driverName = itemView.findViewById(R.id.txt_driver_name);
            txt_carType = itemView.findViewById(R.id.txt_car_type);
            txt_rate = itemView.findViewById(R.id.txt_price_per_seat);
            txt_seats_available = itemView.findViewById(R.id.txt_seats);
            txt_currentLocation = itemView.findViewById(R.id.txt_current_location);
            txt_destination = itemView.findViewById(R.id.txt_destination);
            txt_date = itemView.findViewById(R.id.txt_date);

        }
    }
}
