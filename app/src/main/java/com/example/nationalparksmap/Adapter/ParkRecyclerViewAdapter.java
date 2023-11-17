package com.example.nationalparksmap.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nationalparksmap.R;
import com.example.nationalparksmap.model.Park;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> {
    private final List<Park> parkList;
    private final OnParkClickListener onParkClickListener;

    public ParkRecyclerViewAdapter(List<Park> parkList, OnParkClickListener parkClickListener) {
        this.parkList = parkList;
        this.onParkClickListener = parkClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.park_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Park park = parkList.get(position);
        holder.parkName.setText(park.getName());
        holder.parkType.setText(park.getDesignation());
        holder.parkState.setText(park.getStates());

        if(park.getImages().size()>0){
            Picasso.get()
                    .load(park.getImages().get(0).getUrl())
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(100,100)
                    .centerCrop()
                    .into(holder.parkImage);
        }
    }

    @Override
    public int getItemCount() {
        return parkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView parkImage;
        public TextView parkName;
        public TextView parkType;
        public TextView parkState;
        OnParkClickListener onParkClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkImage = itemView.findViewById(R.id.rpImageView);
            parkName = itemView.findViewById(R.id.rp_tv_name);
            parkType = itemView.findViewById(R.id.rp_tv_type);
            parkState = itemView.findViewById(R.id.rp_tv_state);
            this.onParkClickListener=ParkRecyclerViewAdapter.this.onParkClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Park currPark = parkList.get(getAdapterPosition());
            onParkClickListener.onParkClicked(currPark);
        }
    }
}
