package com.example.xcifers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load the default fragment (e.g., ChatsFragment)
        loadFragment(new MyProductFragment());

        // Set a listener for bottom navigation items
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.allProducts) {
                loadFragment(new MyProductFragment());
                return true;
            } else if (id == R.id.myBids) {
                loadFragment(new MyBidFragment());
                return true;
            } else if (id == R.id.myProducts) {
                loadFragment(new MyProductsFragment());
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // Replace the fragment in the container
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
