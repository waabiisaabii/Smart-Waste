/*
 * @author Yachen Lin
 * @date 4/22/18
 */

package com.smartwaste.googlemaps;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final String REQUEST_URGENT = "requestUrgent";
    private static final String LAST_PICKUP_TIME = "lastPickUpTime";

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
                result.addAll(processJSONArray(jsonArray));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Item> processJSONArray(JSONArray jsonArray) {
        List<Item> result = new ArrayList<>();
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            Map<String, Object> itemMap = (Map<String, Object>) jsonObject.get("fields");
            try {
                long binId = (long) itemMap.get(BIN_ID_NAME);
                String geoLocationStr = (String) itemMap.get(GEO_LOCATION_NAME);
                long binStatus = (long) itemMap.get(BIN_STATUS_NAME);
                long requestUrgent = (long) itemMap.get(REQUEST_URGENT);
                String lastPickUpTime = (String) itemMap.get(LAST_PICKUP_TIME);

                Item item = new Item(binId, binStatus, geoLocationStr, requestUrgent, lastPickUpTime);
                result.add(item);

            } catch (Exception e) {
                // ignore illegal case
                continue;
            }
        }
        return result;
    }

    @Override
    protected List<Item> doInBackground(Void... voids) {
        try {
            return getBinStatusFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static class Item implements Serializable {
        private long binId;
        private long binStatus;
        private long requestUrgent;
        private String lastPickUpTime;
        private double lat, lon;
        private String latLonStr;

        public Item(long binId, long binStatus, String geoLocationStr,
                    long requestUrgent, String lastPickUpTime) {
            this.binId = binId;
            this.binStatus = binStatus;
            double[] coords = parseGeoLocation(geoLocationStr);
            if (coords == null) {
                lat = 0.0;
                lon = 0.0;
            } else {
                lat = coords[0];
                lon = coords[1];
            }

            this.requestUrgent = requestUrgent;
            this.lastPickUpTime = lastPickUpTime;
            latLonStr = lat + ", " + lon;
        }

        private static double[] parseGeoLocation(String geoLocationInStr) {
            if (geoLocationInStr == null) {
                return null;
            }
            String[] coords = geoLocationInStr.split(",");
            double lat = Double.parseDouble(coords[0]);
            double lon = Double.parseDouble(coords[1]);
            return new double[]{lat, lon};
        }

        /**
         * Getter for latLon {@link LatLng}.
         *
         * @return Getter for latLon {@link LatLng}
         */
        public LatLng getLatLng() {
            return new LatLng(lat, lon);
        }

        public String getLatLonStr() {
            return latLonStr;
        }

        /**
         * Getter for requestUrgent.
         *
         * @return requestUrgent
         */
        public long getRequestUrgent() {
            return requestUrgent;
        }

        /**
         * Getter for lastPickUpTime.
         *
         * @return lastPickUpTime
         */
        public String getLastPickUpTime() {
            return lastPickUpTime;
        }

        /**
         * Getter for binId.
         *
         * @return binId
         */
        public long getBinId() {
            return binId;
        }

        /**
         * Getter for binStatus.
         *
         * @return binStatus
         */
        public long getBinStatus() {
            return binStatus;
        }

        /**
         * Return true if the bin is full.
         *
         * @return Return true if the bin is full.
         */
        public boolean isFull() {
            return binStatus == 1L;
        }

        /**
         * Getter for latitude.
         *
         * @return latitude
         */
        public double getLat() {
            return lat;
        }

        /**
         * Getter for longitude.
         *
         * @return longitude
         */
        public double getLon() {
            return lon;
        }

        public String getEncodedString() {
            StringBuilder sb = new StringBuilder();
            sb.append(binId).append("#");
            sb.append(binStatus == 0 ? "Empty" : "Full").append("#");
            sb.append(lat).append(", ").append(lon);
            return sb.toString();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Bin ID: ").append(binId);
            sb.append(String.format("\n"));
            sb.append(": (").append(lat).append(", ").append(lon).append(")");
            return sb.toString();
        }
    }
}
