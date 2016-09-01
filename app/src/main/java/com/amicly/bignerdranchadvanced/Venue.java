package com.amicly.bignerdranchadvanced;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class Venue {
    @SerializedName("id") private String mId;
    @SerializedName("name") private String mName;
    @SerializedName("verified") private boolean mVerified;
    @SerializedName("location") private Location mLocation;
    @SerializedName("categories") private List<Category> mCategoryList;

    public String getName() {
        return mName;
    }

    public String getFormattedAddress() {
        return "hello";
    }

    public String getId() {
        return mId;
    }
}
