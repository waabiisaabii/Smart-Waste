package com.yujingya.googlemaps;

/**
 * Created by Yachen on 4/22/18.
 */

//TODO
public class ReportItem {
    private final int binId;
    private final String geoLocation;
    private final String description;

    /**
     * Constructor.
     *
     * @param binId       given binId
     * @param geoLocation given geoLocation.
     *                    format: "([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)"
     * @param description given description
     */
    public ReportItem(int binId, String geoLocation, String description) {
        if (binId < 0) {
            throw new IllegalArgumentException("Illegal bin ID");
        }
        if (!geoLocation.matches("([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)")) {
            throw new IllegalArgumentException("Illegal coordinates");
        }
        if (description == null || description.length() == 0) {
            throw new IllegalArgumentException("You must provide detailed description of that bin.");
        }

        this.binId = binId;
        this.geoLocation = geoLocation;
        this.description = description;
    }

    /**
     * Getter for binId.
     *
     * @return binId
     */
    public int getBinId() {
        return binId;
    }

    /**
     * Getter for geo location.
     *
     * @return geoLocation
     */
    public String getGeoLocation() {
        return geoLocation;
    }

    /**
     * Getter for description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }
}
