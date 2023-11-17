package com.example.nationalparksmap.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nationalparksmap.MapsActivity;
import com.example.nationalparksmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Context context;
    private final View view;
    private final LayoutInflater layoutInflater;
    private boolean not_first_time_showing_info_window = false;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_info_window, null);
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        TextView parkName = view.findViewById(R.id.infoTitle);
        TextView parkState = view.findViewById(R.id.infoState);
        ImageView parkImage = view.findViewById(R.id.infoImage);

        parkName.setText(marker.getTitle());

        String[] ar = Objects.requireNonNull(marker.getSnippet()).split("#",2);
        // set image view like this:
        if (not_first_time_showing_info_window) {
            Picasso.get()
                    .load(ar[0])
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(80, 70)
                    .centerCrop()
                    .into(parkImage);
        }
        else { // if it's the first time, load the image with the callback set
            not_first_time_showing_info_window = true;
            Picasso.get()
                    .load(ar[0])
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(80, 70)
                    .centerCrop()
                    .into(parkImage, new InfoWindowRefresher(marker));
        }
        parkState.setText(ar[1]);

        return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }
}
