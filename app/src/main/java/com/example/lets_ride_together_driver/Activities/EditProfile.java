package com.example.lets_ride_together_driver.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {


    private static final String TAG ="Tag";
    private static final Integer WRITE_EXST = 2;
    private static final Integer READ_EXST = 3;
    private Uri filePath;
    private String mUrl;

    int CAM_CONS = 1;
    String mCameraFileName;
    Bitmap mbitmap;

    private ImageView driver_img;
    private Button btn_save_changes;
    private EditText edt_username;


    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users").child("Drivers");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Profile_Images");

        driver_img = findViewById(R.id.driver_profile_img);
        edt_username = findViewById(R.id.edt_username);
        btn_save_changes = findViewById(R.id.btn_save_changes);
        getCurrentInfo();
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);
        driver_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                chooseImage();

//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,
//                        CAM_CONS);
                //openCamera();
            }
        });
        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(edt_username.getText().toString().equals("")){

                    Toast.makeText(EditProfile.this, "Enter user name", Toast.LENGTH_SHORT).show();
                }else {


                    uploadImage();
                }
            }
        });

    }

    public void getCurrentInfo(){

        FirebaseDatabase mdata = FirebaseDatabase.getInstance();
        DatabaseReference mInfo = mdata.getReference("Users").child("Drivers").
                child(Common.currentDriver.getuId());

        mInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel model =  dataSnapshot.getValue(UserModel.class);
                if(model.getProfile_img() == null){
                    driver_img.setImageResource(R.drawable.default_pic);
                }else {
                    Picasso.get().load(model.getProfile_img()).into(driver_img);
                }
                edt_username.setText(model.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

//    private void uploadImage() {
//
//        if(mbitmap != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//            final String time = String.valueOf(System.currentTimeMillis());
//
//            final StorageReference ref = storageReference.child("Images/" + time);
//
//
//            UploadTask uploadTask = ref.putBytes(getByte(mbitmap));
//
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    // Continue with the task to get the download URL
//                    return ref.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                        mUrl = downloadUri.toString();
//                        String name = edt_username.getText().toString();
//
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("name", name);
//                        map.put("profile_img", mUrl);
//                        mRef.child(com.example.lets_ride_together_driver.Common.currentDriver.getuId()).setValue(map);
//                        progressDialog.dismiss();
//                        Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
////                        edtNotes.setText("");
////                        myImg.setImageResource(R.drawable.default_profile);
//
//
//                    } else {
//                        // Handle failures
//                        // ...
//                        Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }else{
//            Toast.makeText(this, "Select any image", Toast.LENGTH_SHORT).show();
//        }
//
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAM_CONS && resultCode == RESULT_OK) {
//
//
//            Bitmap bmp = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            // convert byte array to Bitmap
//
//            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
//                    byteArray.length);
//            mbitmap = bitmap;
//            driver_img.setImageBitmap(bitmap);
//
//
//        }
//
//
//    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(Common.currentDriver.getuId()).child(UUID.randomUUID().toString());

            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        mUrl = downloadUri.toString();

                        Map<String, Object> map = new HashMap<>();
                        map.put("name", edt_username.getText().toString());
                        map.put("profile_img", mUrl);
                        mRef.child(Common.currentDriver.getuId()).updateChildren(map);
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProfile.this,ProfileActivity.class));

                    } else {
                        Toast.makeText(EditProfile.this,"Error: "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                driver_img.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }



    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(EditProfile.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(EditProfile.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(EditProfile.this, new String[]{permission}, requestCode);
            }
        } else {
           // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

}
