package com.example.xcifers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    EditText itemName, itemDescription, itemHighestBid;
    Button insertData;
    Uri uriItemImg;
    String imageUrl;
    ImageView imgItem;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth = FirebaseAuth.getInstance();

        insertData = findViewById(R.id.insertData);
        imgItem = findViewById(R.id.imgItem);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemHighestBid = findViewById(R.id.itemHighestBid);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });
        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String owner = email.toString();
                String name = itemName.getText().toString();
                String description = itemDescription.getText().toString();
                Double highestBidder = Double.parseDouble(itemHighestBid.getText().toString());
                String HighestBidPlacer = "open";

                // Upload the image to Firebase Storage and get the download URL
                uploadImageToFirebaseStorage(owner, name, description, highestBidder, HighestBidPlacer);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriItemImg = data.getData();
            Log.d("ImageURI", "Selected Image URI: " + uriItemImg); // Add this logging statement

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriItemImg);
                imgItem.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void uploadImageToFirebaseStorage(String owner, String name, String description, Double highestBidder, String HighestBidPlacer) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("productpics/" + System.currentTimeMillis() + ".jpg");
        if (uriItemImg != null) {
            storageReference.putFile(uriItemImg)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL from the taskSnapshot
                            Task<Uri> downloadUrlTask = storageReference.getDownloadUrl();
                            downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    // Now you have the correct download URL
                                    // Store the data in the database
                                    storeDataInDatabase(owner, name, description, highestBidder, HighestBidPlacer);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Home.this, "Error during photo upload", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void storeDataInDatabase(String owner, String name, String description, Double highestBidder, String HighestBidPlacer) {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("name", name);
        map.put("description", description);
        map.put("highestBidder", highestBidder);
        map.put("HighestBidPlacer", HighestBidPlacer);
        map.put("image", imageUrl);

        FirebaseDatabase.getInstance().getReference().child("products").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Home.this, "Data successfully inserted", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(Home.this, MainPage.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "Adding data failed, please check the data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), CHOOSE_IMAGE);
    }
}
