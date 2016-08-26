package com.amicly.bignerdranchadvanced;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class Location {
    @SerializedName("lat") private double mLatitude;
    @SerializedName("lng") private double mLongitude;
    @SerializedName("formattedAddress") private List<String> mFormattedAddress;
}
