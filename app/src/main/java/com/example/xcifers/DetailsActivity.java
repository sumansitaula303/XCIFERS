package com.example.xcifers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xcifers.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    private TextView name, owner, description, highestBidder;
    private ImageView image;
    private EditText userBid;
    String username;
    String highestbidplacer;
    private Button Bid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsxml);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
             username = user.getEmail();

            if (username != null) {
                // Now, 'username' contains the display name of the currently logged-in user
                // You can use it as needed
                Log.i("TAG", username);
            }
        }
        name = findViewById(R.id.name); // Replace with your TextView IDs
        owner = findViewById(R.id.owner);
        description = findViewById(R.id.description);
        image = findViewById(R.id.image);
        highestBidder= findViewById(R.id.highestBidder);
        userBid= (EditText) findViewById(R.id.userBid);
        Bid= findViewById(R.id.btnBid);
        // Retrieve the clicked item from the Intent
        String clickedItemJson = getIntent().getStringExtra("clicked_item_json");
        MainModel clickedItem = MainModel.fromJson(clickedItemJson);
        String position= getIntent().getStringExtra("position");

        // Display the details of the clicked item
        if (clickedItem != null) {
            name.setText(clickedItem.getName());
            owner.setText("owner: "+clickedItem.getOwner());
            description.setText(clickedItem.getDescription());
            highestBidder.setText(""+clickedItem.getHighestBidder());
            highestbidplacer= clickedItem.getHighestBidPlacer();
            Glide.with(this)
                    .load(clickedItem.getImage())
                    .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                    .into(image);
        }
        Bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newHighest= String.valueOf(userBid.getText());
                long newH= Long.parseLong(newHighest);
                String previousHighest= String.valueOf(highestBidder.getText());
                long prevH= Long.parseLong(previousHighest);
                if(newH>prevH&& !highestbidplacer.equals(username)){
                // Get a DatabaseReference to the specific item you want to update
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products")
                        .child(position);

// Create a map to update the specific field(s)
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("highestBidder", newH);
                updateMap.put("HighestBidPlacer",username);

// Update the data in the database
                databaseReference.updateChildren(updateMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data updated successfully
                                // You can add your code here to handle success
                                Toast.makeText(DetailsActivity.this,"bid successful",Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(DetailsActivity.this, MainPage.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to update data
                                // Handle the error here
                            }
                        });

            }
                else{
                    Toast.makeText(DetailsActivity.this,"Please enter a higher bid if you are not the highest bidder",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
