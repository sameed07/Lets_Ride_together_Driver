package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.DriverRequest;
import Model.UserModel;

public class PassengerRequestAdapter extends RecyclerView.Adapter<PassengerRequestAdapter.ViewHolder> {

    Context mContext;
    List<DriverRequest> mList;


    public PassengerRequestAdapter(Context mContext, List<DriverRequest> mList){

        this.mContext = mContext;
        this.mList = mList;

    }

    @NonNull
    @Override
    public PassengerRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_request_adapter,
                parent, false);

        return new PassengerRequestAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PassengerRequestAdapter.ViewHolder holder, int position) {

        DriverRequest request = mList.get(position);

        FirebaseDatabase mData = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mData.getReference("Users").child("Passengers");

        mRef.child(request.getSender_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    holder.txt_passenger_name.setText(model.getName());
                    if(model.getProfile_img() == null){
                        holder.img_profile.setImageResource(R.drawable.default_pic);
                    }else {
                        Picasso.get().load(model.getProfile_img()).into(holder.img_profile);
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btn_accept;
        TextView txt_passenger_name;
        ImageView img_profile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_accept = itemView.findViewById(R.id.btn_accept);
            txt_passenger_name = itemView.findViewById(R.id.txt_passenger_name);
            img_profile = itemView.findViewById(R.id.user_profile_img);
        }
    }
}
