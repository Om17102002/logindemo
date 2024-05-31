package com.example.logindemo;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class MainActivity3 extends AppCompatActivity {
    private static final String BUCKET_NAME = "your-bucket-name";
    private static final String REGION = "your-bucket-region";
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore firebase;
    Uri imageuri;
    StorageReference storagereference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);




        Button btn_register=findViewById(R.id.button6);
        EditText pname=findViewById(R.id.editTextText4);
        EditText padd=findViewById(R.id.editTextText5);
        EditText pcno=findViewById(R.id.editTextText3);




        btn_register.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= pname.getText().toString();
                String nadd= padd.getText().toString();
                String ncno= pcno.getText().toString();
                firebase = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("address", nadd);
                user.put("contactNumber", ncno);

// Add the data to Firestore
                firebase.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // DocumentSnapshot added with ID: documentReference.getId()
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                Toast.makeText(MainActivity3.this, "Case Registered Successfully", Toast.LENGTH_SHORT).show();
                startframetwo();
            }
        }));
        Button openGalleryButton = findViewById(R.id.button3);

        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void startframetwo() {
        Intent intent=new Intent(this,MainActivity2.class);
        startActivity(intent);
        finish();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            File imageFile = new File(getRealPathFromURI(selectedImageUri));

            // Now you can call the method to upload the image to S3
            uploadImageToS3(imageFile);

        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }


    private void uploadImageToS3(File imageFile) {
        AmazonS3 s3Client = new AmazonS3Client(new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "your-identity-pool-id",
                Regions.fromName(REGION)
        ));

        // Upload the image file to S3
        s3Client.putObject(new PutObjectRequest(
                BUCKET_NAME,
                "image.jpg", // Desired key (name) of the object in S3
                imageFile
        ));
    }
}

