package com.example.xcifers;
import android.content.Intent;
import android.os.Parcelable;
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

public class MyProductsAdapter extends FirebaseRecyclerAdapter<MainModel, MyProductsAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyProductsAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.owner.setText(model.getOwner());
        holder.description.setText(model.getDescription());
        Glide.with(holder.image.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MainModel clickedItem = getItem(position);

                    // Start the DetailsActivity and pass the clicked item
                    Intent intent = new Intent(view.getContext(), MyProductDetails.class);
                    intent.putExtra("clicked_item_json", clickedItem.toJson());
                    view.getContext().startActivity(intent);
                }}
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name, description, owner;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image= (CircleImageView)itemView.findViewById(R.id.img1);
            name= (TextView) itemView.findViewById(R.id.txtname);
            description= (TextView) itemView.findViewById(R.id.details);
            owner= (TextView) itemView.findViewById(R.id.own);
        }
    }
}
