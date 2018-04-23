package com.smartwaste.googlemaps;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yachen on 4/22/18.
 */

public class ReportItem {
    private static final String BIN_ID_NAME = "binID";
    private static final String GEO_LOCATION_NAME = "geoLocation";
    private static final String DESCRIPTION = "description";

    public Map<String, String> getItems() {
        return new HashMap<>(items);
    }

    private Map<String, String> items;

    /**
     * Constructor.
     *
     * @param binId       given binId
     * @param geoLocation given geoLocation.
     *                    format: "([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)"
     * @param description given description
     */
    public ReportItem(long binId, String geoLocation, String description) {
        if (binId < 0) {
            throw new IllegalArgumentException("Illegal bin ID");
        }
        if (!geoLocation.matches("([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)")) {
            throw new IllegalArgumentException("Illegal coordinates");
        }
        if (description == null || description.length() == 0) {
            throw new IllegalArgumentException("You must provide detailed description of that bin.");
        }

        items = new HashMap<>();
        items.put(BIN_ID_NAME, String.valueOf(binId));
        items.put(GEO_LOCATION_NAME, geoLocation);
        items.put(DESCRIPTION, description);
    }
}
