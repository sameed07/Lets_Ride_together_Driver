package com.example.lets_ride_together_driver.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.NewModel.TripHistory;
import com.example.lets_ride_together_driver.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripDetailsAdapter extends RecyclerView.Adapter<TripDetailsAdapter.MyViewHolder> {


    Context context;
    private ArrayList<TripHistory> list = new ArrayList<>();


    public TripDetailsAdapter(Context context, ArrayList<TripHistory> listrider) {
        this.list = listrider;

        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tripdetails_rec_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final int pos = position;
        System.err.println("working " + position);


        TripHistory obj = list.get(position);


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "clicked " + pos, Toast.LENGTH_SHORT).show();

            }
        });

        holder.customerName.setText(obj.getCustomerName());
        holder.address.setText(obj.getAddress());
        holder.time.setText(obj.getTime());

        holder.distance.setText(obj.getDistance());

        System.err.println("qwe" + obj.getAvatarUri());
        if (obj.getAvatarUri() != null && !obj.getAvatarUri().equals("")) {

            System.err.println("qwe" + obj.getAvatarUri());
            Picasso.get()
                    .load(obj.getAvatarUri())
                    .placeholder(R.drawable.ic_person_marker)
                    .error(R.drawable.ic_person_marker)
                    .into(holder.user_profile_img_rec_post);
        } else {
            holder.user_profile_img_rec_post.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_marker));
        }


    }

    @Override
    public int getItemCount() {


        return list.size();


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        CircleImageView user_profile_img_rec_post;
        private CardView mainLayout;
        private TextView distance, time, address, customerName;

        public MyViewHolder(View view) {
            super(view);
            distance = view.findViewById(R.id.distancetxt);
            address = view.findViewById(R.id.txtAddressrec);
            time = view.findViewById(R.id.txtTimerec);
            customerName = view.findViewById(R.id.name_rec_post);
            mainLayout = view.findViewById(R.id.main_layout);
            user_profile_img_rec_post = view.findViewById(R.id.user_profile_img_rec_post);
        }
    }

}
