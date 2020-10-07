package com.example.lets_ride_together_driver.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Adapter.UserAdapter;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.NewModel.Chatlist;
import com.example.lets_ride_together_driver.NewModel.Token;
import com.example.lets_ride_together_driver.NewModel.User;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {


    DatabaseReference reference;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<Chatlist> usersList;
    private String type_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        usersList = new ArrayList<>();

   //     System.out.println("gjhasgdhjasjda5 ");

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(FirebaseAuth.getInstance().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //    System.out.println("gjhasgdhjasjda "+dataSnapshot);
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("gjhasgdhjasjda4 "+databaseError);

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken(), FirebaseAuth.getInstance().getUid());
        System.out.println("gjhasgdhjasjda7 ");


        return view;
    }

    private void updateToken(String token, String myid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(myid).setValue(token1);
    }

    private void chatList() {
        //System.out.println("gjhasgdhjasjda1 ");


        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(Common.user_rider_tbl);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                System.out.println("gjhasgdhjasjda2 "+dataSnapshot);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    for (Chatlist chatlist : usersList) {
                        if (snapshot.getKey().equals(chatlist.getId())) {
                            mUsers.add(new User(snapshot.getKey(), snapshot.child("name").getValue().toString(), snapshot.child("email").getValue().toString(), "", "", "", snapshot.child("profile_img").getValue().toString()));


                        }
                    }
                }
                userAdapter = new UserAdapter(getActivity(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
