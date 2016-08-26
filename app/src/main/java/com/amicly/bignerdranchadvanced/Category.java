package com.amicly.bignerdranchadvanced;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class Category {
    @SerializedName("id") private String mId;
    @SerializedName("name") private String mName;
    @SerializedName("icon") private Icon mIcon;
    @SerializedName("primary") private boolean mPrimary;
}
