package com.example.art2go.App;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.art2go.Common.Common;
import com.example.art2go.CustomDialogs.NewArtDialog;
import com.example.art2go.Map.MyLocationListener;
import com.example.art2go.Model.Art;
import com.example.art2go.Model.User;
import com.example.art2go.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FloatingActionButton freelanceBtn;
    private Location getLastLocation;
    private LocationManager locationManager;
    private LatLng currentLocation;
    private String cityName;

    CollectionReference userRef;
    FirebaseAuth firebaseAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getUser();

    }

    private void getUser() {
        navigationView.setVisibility(View.GONE);
        if (Common.currentUser == null) {
            userRef = FirebaseFirestore.getInstance().collection("Users");
            firebaseAuth = FirebaseAuth.getInstance();
//            Bundle extras = getIntent().getExtras();
//            userId = extras.getString(Common.KEY_USER_ID);
//            if (TextUtils.isEmpty(userId)) {
//                Paper.init(this);
//                userId = Paper.book().read(Common.KEY_LOGGED);
//            }
            userId = firebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = userRef.document(userId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                    if (task.isSuccessful()) {
                        DocumentSnapshot userSnapshot = task.getResult();
                        if (userSnapshot.exists()) {
                            Common.currentUser = userSnapshot.toObject(User.class);
                            Common.currentUser.setUserId(userId);
                            initView();
                            if (Common.currentUser.isBanned()) {
                                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this);
                                dialog.setMessage("Iz nekog razloga ste banani, za vise informacija obratite se administraciji na reservationapk@gmail.com!")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                System.exit(0);
                                            }
                                        })
                                        .setCancelable(false);
                                AlertDialog alert = dialog.create();
                                alert.show();
                            }
                        }
                    } else {
                        Log.e("MainActivity", "Failed to login:" + task.getException());
                    }
                }
            });

        } else {
            initView();
        }
    }

    private void loadArts() {
        Geocoder gcd = new Geocoder(getBaseContext(),
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(getLastLocation.getLatitude(), getLastLocation
                    .getLongitude(), 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
            cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        }
        getAllThingsForMap();
    }


    private void getAllThingsForMap() {
        try {
            Common.sculptures = getXmlFiles("sculptures.xml", "Sculpture");
            Common.monuments = getXmlFiles("monuments.xml", "Spomenik");
            Common.architecture = getXmlFiles("architecture.xml", "Arhitektura");

            Common.allArt = new ArrayList<>(Common.sculptures);
            Common.allArt.addAll(Common.monuments);
            Common.allArt.addAll(Common.architecture);


            if (displayGpsStatus()) {
                try {
                    MyLocationListener listener = new MyLocationListener(this);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 30, listener);
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Location access not granted ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            } else {
                Toast.makeText(MainActivity.this, "Location access not granted ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } catch (XmlPullParserException ex) {
            Log.e("MainActivity", "No data in XML: " + ex.getMessage());
        } catch (IOException ex) {
            Log.e("MainActivity", "Couldn't open XML: " + ex.getMessage());
        }

    }

    /*----Method to Check GPS is enable or disable ----- */
    protected Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }

    private ArrayList<Art> getXmlFiles(String xmlFile, String firstTag) throws IOException, XmlPullParserException {
        XmlPullParserFactory parserFactory;
        XmlPullParser parser;
        parserFactory = XmlPullParserFactory.newInstance();
        parser = parserFactory.newPullParser();
        try {
            InputStream is = getAssets().open(cityName + "/" + xmlFile);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
        } catch (IOException e) {
        }
        ArrayList<Art> namesInXml = new ArrayList<>();
        int eventType = parser.getEventType();
        Art currentSculpture = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    if (firstTag.equals(eltName)) {
                        currentSculpture = new Art();
                        namesInXml.add(currentSculpture);
                    } else if (currentSculpture != null) {
                        if ("name".equals(eltName)) {
                            currentSculpture.name = parser.nextText();
                        } else if ("description".equals(eltName)) {
                            currentSculpture.description = parser.nextText();
                        } else if ("author".equals(eltName)) {
                            currentSculpture.author = parser.nextText();
                        } else if ("imagePath".equals(eltName)) {
                            currentSculpture.imagePath = parser.nextText();
                        } else if ("points".equals(eltName)) {
                            currentSculpture.points = parser.nextText();
                        } else if ("latitude".equals(eltName)) {
                            currentSculpture.latitude = parser.nextText();
                        } else if ("longitude".equals(eltName)) {
                            currentSculpture.longitude = parser.nextText();
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
        return namesInXml;
    }

    private void initView() {
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                currentLocation = new LatLng(getLastLocation.getLatitude(), getLastLocation.getLongitude());
                loadArts();
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Turn on location to continue", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Common.REQUEST_LOCATION);
        }

        navigationView.setVisibility(View.VISIBLE);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(2).setEnabled(false);
        navigationView.setBackground(null);
        freelanceBtn = findViewById(R.id.freelance);
        loadFragment(new FreelanceFragment());

        freelanceBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                loadFragment(new FreelanceFragment());
                for (int i = 0; i < navigationView.getChildCount(); i++) {
                    View itemView = navigationView.getChildAt(i);
                    //itemView.setShiftingMode(false);
                    navigationView.clearChildFocus(itemView);
                }
            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.story) {
                    fragment = new StoryFragment();
                } else if (item.getItemId() == R.id.cityart) {
                    fragment = new PremiumFragment();
                } else if (item.getItemId() == R.id.tickets) {
                    fragment = new TicketFragment();
                } else if (item.getItemId() == R.id.profile) {
                    fragment = new ProfileFragment();
                }
                return loadFragment(fragment);
            }
        });
        navigationView.setSelectedItemId(R.id.placeholder);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

}