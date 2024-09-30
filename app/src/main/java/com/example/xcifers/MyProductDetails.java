package com.example.xcifers;

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

public class MyProductDetails extends AppCompatActivity {
    private TextView name, owner, description, highestBidder;
    private ImageView image;
    private EditText userBid;
    private Button Bid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproductdetails);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String username = user.getEmail();

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
        // Retrieve the clicked item from the Intent
        String clickedItemJson = getIntent().getStringExtra("clicked_item_json");
        MainModel clickedItem = MainModel.fromJson(clickedItemJson);

        // Display the details of the clicked item
        if (clickedItem != null) {
            name.setText(clickedItem.getName());
            owner.setText("Highest Bidder: "+clickedItem.getHighestBidPlacer());
            description.setText(clickedItem.getDescription());
            highestBidder.setText(""+clickedItem.getHighestBidder());
            Glide.with(this)
                    .load(clickedItem.getImage())
                    .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                    .into(image);
        }

    }
}
