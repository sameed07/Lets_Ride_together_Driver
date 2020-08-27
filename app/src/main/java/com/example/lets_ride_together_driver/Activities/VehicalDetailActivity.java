package com.example.lets_ride_together_driver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lets_ride_together_driver.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Common.Common;

public class VehicalDetailActivity extends AppCompatActivity {

    String number;
    String name  ;
    String email ;
    String password ;
    String city ;
    Uri downloadUri;

    private static final String TAG ="Tag";
    private static final Integer WRITE_EXST = 2;
    private static final Integer READ_EXST = 3;
    private Uri filePath;
    private String mUrl;
    private final int PICK_IMAGE_REQUEST = 71;

    private EditText edt_cnic,edt_carname,edt_vehicle_number;
    private Spinner car_spinner;
    private ImageView car_doc_img;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehical_detail);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("vehicle_doc");

        Button btn_submit = findViewById(R.id.btn_submit);
        edt_carname = findViewById(R.id.edt_vehiclename);
        edt_cnic = findViewById(R.id.edt_cnic);
        edt_vehicle_number = findViewById(R.id.edt_vehicle_number);
        car_spinner = findViewById(R.id.cartype_spinner);
        car_doc_img = findViewById(R.id.car_doc_img);

        //get data from register activity
        number = getIntent().getStringExtra("phone_number");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        city = getIntent().getStringExtra("city");


        car_doc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("vehicle_doc");

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
                         downloadUri = task.getResult();
                        Intent intent = new Intent(VehicalDetailActivity.this, VerificationActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("phone_number",number);
                        intent.putExtra("email",email);
                        intent.putExtra("password",password);
                        intent.putExtra("city",city);
                        intent.putExtra("cnic",edt_cnic.getText().toString());
                        intent.putExtra("vehicle_name",edt_carname.getText().toString());
                        intent.putExtra("vehicle_number",edt_vehicle_number.getText().toString());
                        intent.putExtra("car_type",car_spinner.getSelectedItem().toString());
                        intent.putExtra("vehicle_doc",downloadUri);
                        startActivity(intent);

                    } else {
                        Toast.makeText(VehicalDetailActivity.this,"Error: "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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
                car_doc_img.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
