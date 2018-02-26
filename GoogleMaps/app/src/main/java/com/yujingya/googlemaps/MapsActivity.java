package com.yujingya.googlemaps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
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
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, (CharSequence) marker.getTag(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
