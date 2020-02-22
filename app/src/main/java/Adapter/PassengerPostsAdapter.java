package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.R;

import java.util.ArrayList;
import java.util.List;

import Model.PassengerPostedModel;

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
    public void onBindViewHolder(@NonNull PassengerPostsAdapter.ViewHolder holder, int position) {

        PassengerPostedModel currentItem = exampleListFull.get(position);

        holder.txt_driverName.setText(currentItem.getPassenger_name());
//        holder.txt_carType.setText(currentItem.getVehicle_type());

        holder.txt_currentLocation.setText(currentItem.getStarting_lat());
        holder.txt_destination.setText(currentItem.getEnding_lat());
        //        holder.txt_date.setText(currentItem.getDate());
        // holder.txt_time.setText(currentItem.getTime());
        //holder.txt_tripDetails.setText(currentItem.getTrip_detail());

//        holder.txt_driverName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(mContext, DriverProfile.class);
//                mContext.startActivity(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return exampleListFull.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_driverName,txt_rate,txt_carType,txt_seats_available,txt_date,txt_time,
                txt_tripDetails,txt_currentLocation, txt_destination;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txt_driverName = itemView.findViewById(R.id.txt_driver_name);

            txt_currentLocation = itemView.findViewById(R.id.txt_current_location);
            txt_destination = itemView.findViewById(R.id.txt_destination);
            //  txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            //txt_tripDetails = itemView.findViewById(R.id.txt_round_trip);
        }
    }
}
