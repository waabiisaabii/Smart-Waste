package com.yujingya.googlemaps;

import android.os.AsyncTask;
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
import com.google.maps.android.data.kml.KmlLayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private GoogleMap mMap;

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
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private static final String serverURL = "https://peaceful-island-64716.herokuapp.com/iot/";
    private static final String serverActionGetBinStatus = serverURL + "returnJSON";

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

        // Add kml layer to the map
        createKMLLayer();

        // Display trash bins
        displayTrashBins(serverActionGetBinStatus);

        mMap.setOnMarkerClickListener(this);
    }

    private void displayTrashBins(String url) {
        try {

            List<BinStatus.Item> items = new BinStatus(url).execute().get();
            for (BinStatus.Item item : items) {
                LatLng coord = new LatLng(item.getLat(), item.getLon());
                System.out.println(coord);
                int binColor = item.getBinStatus() == 0 ? R.drawable.greenl : R.drawable.redl;
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(coord)
                        .icon(BitmapDescriptorFactory.fromResource(binColor)));
                newMarker.setTag("location: testtest");
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, (CharSequence) marker.getTag(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
