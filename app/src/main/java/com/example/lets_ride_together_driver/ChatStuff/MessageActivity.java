package com.example.lets_ride_together_driver.ChatStuff;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lets_ride_together_driver.Activities.CarpoolMainActivity;
import com.example.lets_ride_together_driver.Adapter.MessageAdapter;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.NewModel.Chat;
import com.example.lets_ride_together_driver.NewModel.DataMessage;
import com.example.lets_ride_together_driver.NewModel.FCMResponse;
import com.example.lets_ride_together_driver.NewModel.Token;
import com.example.lets_ride_together_driver.NewModel.User;
import com.example.lets_ride_together_driver.Notifications.Client;
import com.example.lets_ride_together_driver.Notifications.Data;
import com.example.lets_ride_together_driver.Notifications.Sender;
import com.example.lets_ride_together_driver.R;
import com.example.lets_ride_together_driver.Remote.IFCMService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    // FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send, btn_record;
    EditText text_send;


    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;

    ValueEventListener seenListener;

    String userid;

    MediaRecorder recorder;
    StorageReference storageReference;
    String fileName = null;
    ProgressDialog progressDialog;
    IFCMService apiService;

    boolean notify = false;

    private String audioFileName = "";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and this
                startActivity(new Intent(MessageActivity.this, CarpoolMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(IFCMService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        btn_record = findViewById(R.id.btn_record);

        progressDialog = new ProgressDialog(this);

        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();


        btn_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    fileName = Environment.getExternalStorageDirectory().getAbsolutePath();

                    audioFileName = System.currentTimeMillis() + ".3gp";
                    fileName += "/" + audioFileName;

                    startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                }


                return false;
            }
        });


        intent = getIntent();
        userid = intent.getStringExtra("userid");


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {

                    System.err.println("otheruser" + userid);
                    sendMessage(FirebaseAuth.getInstance().getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");


            }
        });


        reference = FirebaseDatabase.getInstance().getReference(Common.user_rider_tbl).child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {

                System.out.println("sadfhjsdfgbsd "+dataSnapshot);

                username.setText(dataSnapshot.child("name").getValue().toString());
                if (dataSnapshot.child("profile_img").getValue().toString().equals("")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    //and this
                    Picasso.get().load(dataSnapshot.child("profile_img").getValue().toString()).placeholder(R.mipmap.ic_launcher).into(profile_image);
                }

                readMesagges(FirebaseAuth.getInstance().getUid(), userid, dataSnapshot.child("profile_img").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);


        //make user online
        //     status("true");

    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(FirebaseAuth.getInstance().getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(final String sender, final String receiver, final String message) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);


        reference.child("Chats").push().setValue(hashMap);


        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(FirebaseAuth.getInstance().getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(FirebaseAuth.getInstance().getUid());
        chatRefReceiver.child("id").setValue(FirebaseAuth.getInstance().getUid());

        final String msg = message;


        reference = FirebaseDatabase.getInstance().getReference(Common.user_rider_tbl).child(receiver);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              //  System.err.println("myfull" + user.getName());
                if (notify) {
                    sendNotifiaction(receiver, dataSnapshot.child("name").getValue().toString(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendNotifiaction(final String receiver, final String username, final String message) {


        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(FirebaseAuth.getInstance().getUid(), R.drawable.utube, username + ": " + message, "New Message",
                            userid);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("title", "New Message");
                    map.put("user", FirebaseAuth.getInstance().getUid());
                    map.put("sented", userid);
                    map.put("body", username + ": " + message);
                    map.put("icon", String.valueOf(R.drawable.utube));


                    DataMessage message1 = new DataMessage(token.getToken(), map);
                    Sender sender = new Sender(data, token.getToken());

                    //TODO
                    apiService.sendMessage(message1).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readMesagges(final String myid, final String userid, final String imageurl) {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid) {


        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status) {


//
//            reference = FirebaseDatabase.getInstance().getReference("Drivers").child(fuser.getUid());
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("online", status);
//
//            reference.updateChildren(hashMap);


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("true");
//        currentUser(userid);
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        reference.removeEventListener(seenListener);
//        status("false");
//        currentUser("none");
//    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Recording Failed", Toast.LENGTH_SHORT).show();
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        uploadAudio();
    }

    private void uploadAudio() {

        progressDialog.setMessage("Uploading Audio.....");
        progressDialog.show();

        final StorageReference filepath = storageReference.child("Audio").child(audioFileName);


        Uri uri = Uri.fromFile(new File(fileName));


        UploadTask uploadTask = filepath.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    System.err.println("Upload " + downloadUri);

                    progressDialog.dismiss();
                    Toast.makeText(MessageActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    if (downloadUri != null) {

                        String audioStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                        System.err.println("Upload " + audioStringLink);

                    }
                }


            }
        });
//
//                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
//
//                progressDialog.dismiss();
//                filepath.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        // Got the download URL for 'users/me/profile.png'
//                        Uri downloadUri = uri;
//                       String  generatedFilePath = downloadUri.toString(); /// The string(file link) that you need
//
//                        System.err.println("urlismy"+generatedFilePath);
//
//
//
//
//                        Toast.makeText(MessageActivity.this, "Audio Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                        Task<Uri> downloadU = taskSnapshot.getStorage().getDownloadUrl();
//
//                        if(downloadU.isSuccessful()) {
//                            String generatedFile = downloadU.getResult().toString();
//                            System.out.println("Stored path is " + generatedFile);
//
//                        }
//
//
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle any errors
//                    }
//                });
//
//
//
//
//
//            }
//        });


    }


    @Override
    public void onBackPressed() {
super.onBackPressed();    }
}
