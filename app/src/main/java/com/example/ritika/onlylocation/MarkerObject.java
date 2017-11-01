package com.example.ritika.onlylocation;

/**
 * Created by RITIKA on 24-09-2017.
 */

public class MarkerObject {

    private int markerId;
    private double latitude;
    private double longitude;
    private String title;
    private String description;

    MarkerObject(int id, double lat, double lon)
    {
        markerId = id;
        latitude = lat;
        longitude = lon;
    }

    int returnId()
    {
        return markerId;
    }
}
