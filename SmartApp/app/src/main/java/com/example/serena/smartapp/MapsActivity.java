package com.example.serena.smartapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private LocationManager locationManager;

    private LocationListener locationListener;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        final DrawerLayout drawer = getActivity().findViewById(R.id.left_listview);

//        Button fab = (Button) findViewById(R.id.leftmenu);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.openDrawer(drawer);
//            }
//        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }
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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LatLng Shadyside = new LatLng(40.4556, -79.9277);
        LatLng Allegheny = new LatLng(40.451993, -80.015763);
        LatLng Allentown = new LatLng(40.422303, -79.993398);
        LatLng Bloomfield = new LatLng(40.462161, -79.944453);
        LatLng EastHills = new LatLng(40.454876, -79.875883);

        Marker myShady = mMap.addMarker(new MarkerOptions()
                .position(Shadyside)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenl)));
        myShady.setTag("location: Shadyside");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Shadyside));

        Marker myAllegheny = mMap.addMarker(new MarkerOptions()
                .position(Allegheny)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redl)));

        myAllegheny.setTag("location: Allegheny West");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Allegheny));

        Marker myAllentown = mMap.addMarker(new MarkerOptions()
                .position(Allentown)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redl)));
        myAllentown.setTag("location: Allentown");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Allentown));

        Marker myBloomfield = mMap.addMarker(new MarkerOptions()
                .position(Bloomfield)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenl)));
        myBloomfield.setTag("location: Bloomfield");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Bloomfield));

        Marker myEastHills = mMap.addMarker(new MarkerOptions()
                .position(EastHills)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redl)));
        myEastHills.setTag("location: East Hills");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(EastHills));

        mMap.setOnMarkerClickListener(this);


//        locationListener = new LocationListener() {
//
//            @Override
//            public void onLocationChanged(Location location) {
//
//                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//
//                try {
//
//                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//                    if (listAddresses != null && listAddresses.size() > 0) {
//
//                        Log.i("PlaceInfo", listAddresses.get(0).toString());
//
//                        String address = "";
//
//                        if (listAddresses.get(0).getSubThoroughfare() != null) {
//
//                            address += listAddresses.get(0).getSubThoroughfare() + " ";
//
//                        }
//
//                        if (listAddresses.get(0).getThoroughfare() != null) {
//
//                            address = listAddresses.get(0).getThoroughfare() + ",";
//                        }
//
//                        if (listAddresses.get(0).getPostalCode() != null) {
//
//                            address = listAddresses.get(0).getPostalCode() + ",";
//                        }
//
//                        if (listAddresses.get(0).getCountryName() != null) {
//
//                            address = listAddresses.get(0).getCountryName() + ",";
//                        }
//
//                        Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();
//
//
//                    }
//
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//            }
//        };
//
//        if (Build.VERSION.SDK_INT < 23) {
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//        } else {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            } else {
//
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
//
//                mMap.clear();
//
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//
//            }
//
//        }


    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, (CharSequence) marker.getTag(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
