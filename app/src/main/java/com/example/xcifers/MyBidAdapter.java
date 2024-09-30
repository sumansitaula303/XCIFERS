package com.example.xcifers;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBidAdapter extends FirebaseRecyclerAdapter<MainModel, MyBidAdapter.myViewHolder> {

    public MyBidAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.owner.setText("Owner:"+model.getOwner());
        holder.description.setText(model.getDescription());
        holder.bidPrice.setText("HighestBid:"+model.getHighestBidPlacer());
        Glide.with(holder.image.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.image);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mybids,parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name, description, bidPrice, owner;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image= (CircleImageView)itemView.findViewById(R.id.img1);
            name= (TextView) itemView.findViewById(R.id.txtname);
            description= (TextView) itemView.findViewById(R.id.details);
            owner= (TextView) itemView.findViewById(R.id.own);
            bidPrice= (TextView) itemView.findViewById(R.id.bidPrice);
        }
    }
}
