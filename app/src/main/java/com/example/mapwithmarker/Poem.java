package com.example.mapwithmarker;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jirka on 17.6.17.
 */

public class Poem {
    String text;
    Author author;
    LatLng position;

    public Poem(String text) {
        this.text = text;
    }
}
