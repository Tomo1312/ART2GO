package com.example.art2go.Custom;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.art2go.Model.Art;
import com.example.art2go.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;


    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view) {

        String title = marker.getTitle();
        Art art = (Art) marker.getTag();
        TextView tvTitle = view.findViewById(R.id.custom_art_title);
        TextView tvDescription = view.findViewById(R.id.custom_txt_art_description);
        TextView tvAuthor = view.findViewById(R.id.custom_txt_art_author);

        if (!title.equals("") && title.equals("Unknown")) {
            tvTitle.setText(title);
            tvAuthor.setText("");
            tvDescription.setText("");
        } else if (!title.equals("")){
            tvTitle.setText(title);
            if (art.getDescription() != null)
                tvDescription.setText(art.getDescription());
            if (art.getAuthor() != null)
                tvAuthor.setText(art.getAuthor());
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}