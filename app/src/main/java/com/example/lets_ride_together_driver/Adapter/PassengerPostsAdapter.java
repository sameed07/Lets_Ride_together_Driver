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

import com.example.lets_ride_together_driver.Activities.PassengerRequestActivity;
import com.example.lets_ride_together_driver.Model.PassengerPostedModel;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PassengerPostsAdapter extends RecyclerView.Adapter<PassengerPostsAdapter.ViewHolder> {

    private List<PassengerPostedModel> exampleListFull =  new ArrayList<>();

    private Context mContext;

    public PassengerPostsAdapter(Context context, List<PassengerPostedModel> exampleListFull){

        this.mContext = context;
        this.exampleListFull = exampleListFull;
    }


    @NonNull
    @Override
    public PassengerPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_posts_adapter,
                parent, false);
        return new PassengerPostsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PassengerPostsAdapter.ViewHolder holder, int position) {

        final PassengerPostedModel currentItem = exampleListFull.get(position);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference("Users").child("Passengers").child(currentItem.getId());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                holder.txt_driverName.setText(model.getName());

                if(model.getProfile_img() == null){
                    holder.passenger_img.setImageResource(R.drawable.default_pic);
                }else {
                    Picasso.get().load(model.getProfile_img()).into(holder.passenger_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




       holder.txt_carType.setText(currentItem.getRide_type());

        holder.txt_currentLocation.setText(currentItem.getStarting_point());
        holder.txt_destination.setText(currentItem.getEnding_point());
        holder.txt_date.setText(currentItem.getDate());


        holder.txt_driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(mContext, DriverProfile.class);
                //mContext.startActivity(i);
            }
        });

        holder.btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, PassengerRequestActivity.class);
                i.putExtra("driver_id",currentItem.getId());
                i.putExtra("post_key",currentItem.getPost_id());
                mContext.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return exampleListFull.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_driverName,txt_rate,txt_carType,txt_seats_available,txt_date,txt_time,
                txt_tripDetails,txt_currentLocation, txt_destination;

        Button btn_request;
        ImageView passenger_img;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            passenger_img  = itemView.findViewById(R.id.passenger_img);
            btn_request = itemView.findViewById(R.id.btn_request);
            txt_driverName = itemView.findViewById(R.id.txt_driver_name);
            txt_carType = itemView.findViewById(R.id.txt_cartype);
            txt_currentLocation = itemView.findViewById(R.id.txt_current_location);
            txt_destination = itemView.findViewById(R.id.txt_destination);
            txt_date = itemView.findViewById(R.id.txt_date);
           // txt_time = itemView.findViewById(R.id.txt_time);
            //txt_tripDetails = itemView.findViewById(R.id.txt_round_trip);
        }
    }
}
