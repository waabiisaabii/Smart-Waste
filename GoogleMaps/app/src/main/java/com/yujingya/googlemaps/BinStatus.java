package com.yujingya.googlemaps;

import android.os.AsyncTask;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Getting trash Bin status from Smart Waste server.
 */
public class BinStatus extends AsyncTask<Void, Void, List<BinStatus.Item>> {
    private static final String BIN_ID_NAME = "binID";
    private static final String GEO_LOCATION_NAME = "geoLocation";
    private static final String BIN_STATUS_NAME = "binStatus";

    private URL serverUrl;

    protected BinStatus(String serverUrlStr) throws MalformedURLException {
        serverUrl = new URL(serverUrlStr);
    }

    private List<Item> getBinStatusFromServer() {
        List<Item> result = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(serverUrl.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(inputLine);

                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;
                    Map<String, Object> itemMap = (Map<String, Object>) jsonObject.get("fields");
                    long binId = (long) itemMap.get(BIN_ID_NAME);
                    //TODO: geolocation
                    String geoLocationStr = (String) itemMap.get(GEO_LOCATION_NAME);
                    long binStatus = (long) itemMap.get(BIN_STATUS_NAME);
                    //TODO: geolocation
                    Item item = new Item(binId, binStatus, 0.0, 0.0);
                    result.add(item);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected List<Item> doInBackground(Void... voids) {
        try {
            return getBinStatusFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Item {
        long binId;
        long binStatus;
        double lat, lon;

        public Item(long binId, long binStatus, double lat, double lon) {
            this.binId = binId;
            this.binStatus = binStatus;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "binId=" + binId +
                    ", binStatus=" + binStatus +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }
}
