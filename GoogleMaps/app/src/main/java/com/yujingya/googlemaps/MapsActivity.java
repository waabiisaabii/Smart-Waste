package com.yujingya.googlemaps;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {


    public static final String serverURL = "https://peaceful-island-64716.herokuapp.com/iot/";
    public static final String serverActionGetBinStatus = serverURL + "returnJSON";
    public static final String serverActionDamageReport = serverURL + "reportDamage";
    public static final String serverActionGetAllDamageReport = serverURL + "getAllDamageReports";
    public static final String LAT_LON = "LatLon";
    private GoogleMap mMap;
    private Button damageReportButton;
    private Marker selectedMarker;
    private Map<String, BinStatus.Item> itemMap;

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
        itemMap = new HashMap<>();
        displayTrashBins(serverActionGetBinStatus);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        damageReportButton = findViewById(R.id.damageReportButton);

    }

    private void displayTrashBins(String url) {
        try {

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
        selectedMarker = marker;
        Toast.makeText(this, (CharSequence) marker.getTag(), Toast.LENGTH_SHORT).show();

        System.out.println("> ######marker clicked" + marker.getTag());

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
}
