package com.smartwaste.googlemaps;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;

public class WayPointRoute extends AsyncTask<String, Void, List<LatLng>> {
    @Override
    protected List<LatLng> doInBackground(String... strings) {
        return getRoutes(strings[0]);
    }


    private List<LatLng> getRoutes(String requestURL) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(requestURL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());

            if (!jsonObject.get("status").equals("OK")) {
                return Collections.emptyList();
            }
            rd.close();
            return parseLegs(jsonObject);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<LatLng> parseLegs(JSONObject jsonObject) {
        List<LatLng> result = new ArrayList<>();

        JSONArray routes = (JSONArray) jsonObject.get("routes");
        JSONArray legs = (JSONArray) ((JSONObject) routes.get(0)).get("legs");
        for (Object obj : legs) {
            JSONObject leg = (JSONObject) obj;
            LatLng startLocation = parseLatLng(leg, "start_location");
            result.add(startLocation);

            List<LatLng> middlePoints = parseMiddlePoints(leg, "steps");
            result.addAll(middlePoints);

            LatLng endLocation = parseLatLng(leg, "end_location");
            result.add(endLocation);
        }
        return result;
    }

    private List<LatLng> parseMiddlePoints(JSONObject leg, String key) {
        List<LatLng> result = new ArrayList<>();
        JSONArray steps = (JSONArray) leg.get(key);
        for (Object obj : steps) {
            JSONObject step = (JSONObject) obj;

            LatLng startLocation = parseLatLng(step, "start_location");
            result.add(startLocation);

            LatLng endLocation = parseLatLng(step, "end_location");
            result.add(endLocation);
        }
        return result;
    }

    private LatLng parseLatLng(JSONObject leg, String key) {
        JSONObject latLngJSON = (JSONObject) leg.get(key);
        double lat = (double) latLngJSON.get("lat");
        double lon = (double) latLngJSON.get("lng");
        return new LatLng(lat, lon);
    }
}
