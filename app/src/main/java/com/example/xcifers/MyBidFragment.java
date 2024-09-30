package com.example.xcifers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MyBidFragment extends Fragment {

    RecyclerView recyclerView;
    MyBidAdapter myBidAdapter;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email= currentUser.getEmail();
        // Inflate the layout for this fragment (customize it according to your chat UI)

        View view = inflater.inflate(R.layout.mybidfragment, container, false);

        recyclerView = view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("products").orderByChild("HighestBidPlacer").equalTo(email), MainModel.class)
                        .build();

        myBidAdapter = new MyBidAdapter(options);
        recyclerView.setAdapter(myBidAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myBidAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myBidAdapter.stopListening();
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void txtSearch(String str) {
        try {
            FirebaseRecyclerOptions<MainModel> options =
                    new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("products").orderByChild("name").startAt(str).endAt(str + "~"), MainModel.class)
                            .build();
            myBidAdapter= new MyBidAdapter(options);
            myBidAdapter.startListening();
            recyclerView.setAdapter(myBidAdapter);
        }
        catch (IndexOutOfBoundsException e) {
            // Handle the exception here, for example:
            // Log.e("TAG", "IndexOutOfBoundsException occurred during search: " + e.getMessage());
        }
    }
}
