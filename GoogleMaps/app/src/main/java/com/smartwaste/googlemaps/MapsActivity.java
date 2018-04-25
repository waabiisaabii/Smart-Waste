package com.smartwaste.googlemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Main Activity.
 */
public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {

    public static final String serverURL = "https://peaceful-island-64716.herokuapp.com/iot/";
    public static final String serverActionGetBinStatus = serverURL + "returnJSON";
    public static final String serverActionDamageReport = serverURL + "reportDamage";
    public static final String LAT_LON = "LatLon";
    private static final double shadyLat = 40.4548835;
    private static final double shadyLon = -79.9411988;
    private static final int ZOOM_LEVEL = 13;
    private static final double hill1Lat = 40.4351725;
    private static final double hill1Lon = -79.9346548;
    private static final double hill2Lat = 40.4414185;
    private static final double hill2Lon = -79.9320408;
    private static final double oaklandLat = 40.4455836;
    private static final double oaklandLon = -79.9560714;
    private GoogleMap mMap;
    private Button damageReportButton;
    private Marker selectedMarker;
    private Map<String, BinStatus.Item> itemMap;
    private DrawerLayout mDrawerLayout;
    private List<Marker> markers;

    /**
     * Getter for map.
     *
     * @return map
     */
    public GoogleMap getmMap() {
        return mMap;
    }

    private void createKMLLayer() {
        try {
            KmlLayer kmlLayer = new KmlLayer(getmMap(), R.raw.kml, getApplicationContext());
            kmlLayer.addLayerToMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markers = new ArrayList<>();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                //5 seconds
                handler.postDelayed(this, 5000);
                displayTrashBins(serverActionGetBinStatus);
            }
        };
        handler.postDelayed(r, 0000);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        createKMLLayer();

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            System.out.println(">>> My location is enabled.");
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        }

        damageReportButton = findViewById(R.id.damageReportButton);

        // disable redirection to external Google Map App
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        // move my location button to left bottom
        if (findViewById(Integer.parseInt("1")) != null) {
            // Get the view
            View locationCompass = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("5"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationCompass.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 1500,30, 0);

            View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams2.setMargins(0, 1500,500, 0);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // https://developer.android.com/training/permissions/requesting.html
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mMap.setMyLocationEnabled(true);
                    System.out.println(">>> My location is enabled.");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MapsActivity.this,
                            "Permission denied to get your current location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void displayTrashBins(String url) {
        try {
            Marker tmpSelectedMarker = selectedMarker;
            updateMarkers();
            selectedMarker = tmpSelectedMarker;

            itemMap = new HashMap<>();
            List<BinStatus.Item> items = new BinStatus(url).execute().get();
            for (BinStatus.Item item : items) {
                itemMap.put(item.toString(), item);
                LatLng coord = new LatLng(item.getLat(), item.getLon());
                System.out.println(coord);
                int binColor = item.getBinStatus() == 0 ? R.drawable.greenl : R.drawable.redl;
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(coord)
                        .icon(BitmapDescriptorFactory.fromResource(binColor)));

                newMarker.setTag(item.toString());

                markers.add(newMarker);

//                mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void updateMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        selectedMarker = marker;
        Toast.makeText(this, (CharSequence) marker.getTag(), Toast.LENGTH_SHORT).show();

        System.out.println("> ###### marker clicked" + marker.getTag());

        damageReportButton.setVisibility(View.VISIBLE);
        return false;
    }

    public void damageReport(View view) {
        // Do something in response to button click

        System.out.print(">####### damage report button clicked! ");
        System.out.println(selectedMarker.getTag());

        Intent intent = new Intent(this, ReportDamageActivity.class);
        String itemKey = selectedMarker.getTag().toString();

        intent.putExtra(LAT_LON, itemMap.get(itemKey));
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println(">>>>>>map clicked");
        damageReportButton.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            String popUpMsg;
            if (resultCode == RESULT_OK) {
                System.out.println("####result ok!!!!");
                popUpMsg = "Report submitted.";

            } else {
                System.out.println("####result bad");
                popUpMsg = "Failed to submit report.";
            }

            Snackbar.make(findViewById(R.id.slidingLayout), popUpMsg,
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void shadysideFocus(MenuItem item) {
        System.out.println("shady!");
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(shadyLat, shadyLon))
                .zoom(ZOOM_LEVEL)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        mDrawerLayout.closeDrawers();
    }

    public void hill1Focus(MenuItem item) {
        System.out.println("hill1!");
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(hill1Lat, hill1Lon))
                .zoom(ZOOM_LEVEL)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        mDrawerLayout.closeDrawers();
    }

    public void hill2Focus(MenuItem item) {
        System.out.println("hill2");
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(hill2Lat, hill2Lon))
                .zoom(ZOOM_LEVEL)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        mDrawerLayout.closeDrawers();
    }

    public void oaklandFocus(MenuItem item) {
        System.out.println("oakland");
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(oaklandLat, oaklandLon))
                .zoom(ZOOM_LEVEL)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
}
