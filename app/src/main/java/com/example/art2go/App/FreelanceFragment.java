package com.example.art2go.App;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.art2go.Common.Common;
import com.example.art2go.Custom.CustomInfoWindowAdapter;
import com.example.art2go.CustomDialogs.NewArtDialog;
import com.example.art2go.Model.Art;
import com.example.art2go.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FreelanceFragment extends Fragment implements OnMapReadyCallback {

    ImageView filter;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private LatLng currentLocation;
    private Location getLastLocation;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private boolean isFirst = true;

    private boolean isSculptureShown, isArchitectureShown, isMonumentsShown;

    public FreelanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_freelance, container, false);

        filter = view.findViewById(R.id.filter_map);
        listItems = getResources().getStringArray(R.array.arts);
        checkedItems = new boolean[listItems.length];
        isArchitectureShown = false;
        isMonumentsShown = false;
        checkedItems[0] = Boolean.TRUE;
        isSculptureShown = true;
        mUserItems.add(0);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Filter map")
                        .setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if (isChecked) {
                                    if (position == 0) {
                                        isSculptureShown = true;
                                    } else if (position == 1) {
                                        isMonumentsShown = true;
                                    } else if (position == 2) {
                                        isArchitectureShown = true;
                                    }
                                } else {
                                    if (position == 0) {
                                        isSculptureShown = false;
                                    } else if (position == 1) {
                                        isMonumentsShown = false;
                                    } else if (position == 2) {
                                        isArchitectureShown = false;
                                    }
                                }
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mUserItems.size() == 0) {
                                    isArchitectureShown = false;
                                    isSculptureShown = false;
                                    isMonumentsShown = false;
                                }
                                onMapReady(mMap);
                            }
                        });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        setUpMap();


        return view;
    }

    public void setUpMap() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //TRY TO GET LOCATION
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                currentLocation = new LatLng(getLastLocation.getLatitude(), getLastLocation.getLongitude());
                if (mMap != null)
                    onMapReady(mMap);
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Turn on location to continue", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Common.REQUEST_LOCATION);
        }

        //ENABLE MAP
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception ex) {
            Log.e("ERROR: ", ex.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (currentLocation != null && isFirst) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.5f));
            isFirst = false;
        }

        if (Common.sculptures != null && Common.currentUser.getUnlockedSculptures() != null && isSculptureShown) {//
            for (Art sculpture : Common.sculptures) {
                if (Common.currentUser.getUnlockedSculptures().contains(sculpture.imagePath)) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(sculpture.latitude), Double.valueOf(sculpture.longitude))).title(sculpture.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    marker.setTag(sculpture);
                } else {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(sculpture.latitude), Double.valueOf(sculpture.longitude))).title("Unknown"));
                }
            }
        }

        if (Common.monuments != null && Common.currentUser.getUnlockedSculptures() != null && isMonumentsShown) {//&&
            for (Art sculpture : Common.monuments) {
                if (Common.currentUser.getUnlockedSculptures().contains(sculpture.imagePath)) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(sculpture.latitude), Double.valueOf(sculpture.longitude))).title(sculpture.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    marker.setTag(sculpture);
                } else {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(sculpture.latitude), Double.valueOf(sculpture.longitude))).title("Unknown").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                }
            }
        }

        if (Common.architecture != null && Common.currentUser.getUnlockedSculptures() != null && isArchitectureShown) {//
            for (Art sculpture : Common.architecture) {
                if (Common.currentUser.getUnlockedSculptures().contains(sculpture.imagePath)) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(sculpture.latitude), Double.valueOf(sculpture.longitude))).title(sculpture.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    marker.setTag(sculpture);
                } else {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(sculpture.latitude), Double.valueOf(sculpture.longitude))).title("Unknown").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                }
            }
        }
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if (!marker.getTitle().equals("Unknown") && marker.getTag() != null) {
                    for (Art artTmp : Common.allArt) {
                        if (artTmp.name.equals(marker.getTitle())) {
                            //marker.showInfoWindow();
                            NewArtDialog.getInstance().showDialog(getContext(), artTmp);
                        }
                    }
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }
}