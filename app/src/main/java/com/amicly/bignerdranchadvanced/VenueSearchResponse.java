package com.amicly.bignerdranchadvanced;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class VenueSearchResponse {
    @SerializedName("venues")
    List<Venue> mVenueList;

    public List<Venue> getVenueList() {
        return mVenueList;
    }
}
