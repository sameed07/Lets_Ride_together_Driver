package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.R;

import java.util.ArrayList;
import java.util.List;

import Model.ChatModel;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context mContext;
    List<ChatModel> exampleListFull =  new ArrayList<>();

    public ChatAdapter(Context mContext, List<ChatModel> exampleListFull){

        this.mContext = mContext;
        this.exampleListFull = exampleListFull;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_adapter,
                parent, false);
        return new ChatAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

        ChatModel model = exampleListFull.get(position);
        holder.txt_sender_name.setText(model.getSenderName());
        holder.txt_msg.setText(model.getMsg());
        holder.txt_time.setText(model.getTime());

    }

    @Override
    public int getItemCount() {
        return exampleListFull.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_sender_name,txt_msg,txt_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_sender_name = itemView.findViewById(R.id.txt_driver_name);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_msg = itemView.findViewById(R.id.txt_msg);
        }
    }
}
