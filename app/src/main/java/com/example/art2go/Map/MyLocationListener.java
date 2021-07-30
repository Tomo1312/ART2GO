package com.example.art2go.Map;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import com.example.art2go.Common.Common;
import com.example.art2go.CustomDialogs.NewArtDialog;
import com.example.art2go.Model.Art;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/*----------Listener class to get coordinates ------------- */
public class MyLocationListener implements LocationListener {

    private Context context;

    public MyLocationListener(Context context){
        this.context = context;
    }
    @Override
    public void onLocationChanged(@NonNull Location loc) {
        Location loc1 = new Location("");
        loc1.setLongitude(loc.getLongitude());
        loc1.setLatitude(loc.getLatitude());
        if (Common.sculptures != null && Common.monuments != null && Common.architecture != null) {
            checkIfCurrentLocationIsNear(Common.allArt, loc1);
        }
    }

    private void checkIfCurrentLocationIsNear(ArrayList<Art> Arts, Location loc1) {
        for (Art sculpture : Arts) {
            Double sculptureLatitude = Double.valueOf(sculpture.latitude);
            Double sculptureLongitude = Double.valueOf(sculpture.longitude);

            Location loc2 = new Location("");
            loc2.setLongitude(sculptureLongitude);
            loc2.setLatitude(sculptureLatitude);

            float distanceInMeters = loc1.distanceTo(loc2);
            if (distanceInMeters < 50.0 &&
                    checkIfSculptureUnlocked(sculpture.imagePath)) {
                Common.currentUser.setUserPoints(Common.currentUser.getUserPoints() + Integer.valueOf(sculpture.points));
                Common.currentUser.setUnlockedSculptures(Common.currentUser.getUnlockedSculptures() + ", " + sculpture.imagePath);
                if (Common.currentUser.getUserName() != null && Common.currentUser.getUserEmail() != null) {
                    DocumentReference user = FirebaseFirestore.getInstance().collection("Users").document(Common.currentUser.getUserId());
                    user.update("userPoints", Common.currentUser.getUserPoints());
                    user.update("unlockedSculptures", Common.currentUser.getUnlockedSculptures());
                    NewArtDialog.getInstance().showDialog(context, sculpture);
                }
            }
        }
    }
    protected boolean checkIfSculptureUnlocked(String name) {
        return !Common.currentUser.getUnlockedSculptures().contains(name);
    }
}