package com.example.lets_ride_together_driver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.ChatStuff.MessageActivity;
import com.example.lets_ride_together_driver.Model.DriverPost;
import com.example.lets_ride_together_driver.Model.PassengerRequest;
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


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<PassengerRequest> exampleListFull =  new ArrayList<>();

    private Context mContext;

    public RequestAdapter(List<PassengerRequest> exampleListFull, Context mContext) {
        this.exampleListFull = exampleListFull;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_adapter,
                parent, false);
        return new RequestAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final PassengerRequest currentItem = exampleListFull.get(position);

        holder.request_status.setText(currentItem.getStatus());
        FirebaseDatabase driverDatabase = FirebaseDatabase.getInstance();
        DatabaseReference drierRef = driverDatabase.getReference("Users").child("Passengers");
        drierRef.child(currentItem.getPassenger_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UserModel model = dataSnapshot.getValue(UserModel.class);
                holder.driver_name.setText(model.getName());

                holder.btn_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String num  = model.getPhone();

                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse("tel:"+num));
                        mContext.startActivity(i);
                    }
                });


                if(model.getProfile_img() == null){
                    holder.driver_img.setImageResource(R.drawable.default_profile);
                }else {
                    Picasso.get().load(model.getProfile_img()).into(holder.driver_img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase postData = FirebaseDatabase.getInstance();
        DatabaseReference postRef = postData.getReference("PostAsPassenger");
        postRef.child(currentItem.getPost_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DriverPost post = dataSnapshot.getValue(DriverPost.class);
                holder.start_point.setText(post.getStarting_point());
                holder.end_point.setText(post.getEnding_point());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", currentItem.getPassenger_id());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return exampleListFull.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView driver_name,request_status, start_point, end_point;
        Button btn_call, btn_msg;
        ImageView driver_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            driver_img = itemView.findViewById(R.id.driver_img);
            driver_name = itemView.findViewById(R.id.txt_driver_name);
            request_status = itemView.findViewById(R.id.txt_request_status);
            start_point = itemView.findViewById(R.id.txt_startpoint);
            end_point = itemView.findViewById(R.id.txt_endpoint);
            btn_call = itemView.findViewById(R.id.call_driver_btn);
            btn_msg = itemView.findViewById(R.id.msg_driver_btn);

        }
    }
}
