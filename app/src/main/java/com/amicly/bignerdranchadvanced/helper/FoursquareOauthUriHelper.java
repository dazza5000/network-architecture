package com.amicly.bignerdranchadvanced.helper;

import android.net.Uri;

/**
 * Created by daz on 8/30/16.
 */

public class FoursquareOauthUriHelper {
    private static final String ACCESS_TOKEN_PARAM = "access_token=";
    private Uri mOauthUri;

    public FoursquareOauthUriHelper(String oauthUri) {
        mOauthUri = Uri.parse(oauthUri);
    }

    public String getAccessToken() {
        String uriFragment = mOauthUri.getFragment();
        if (uriFragment.contains(ACCESS_TOKEN_PARAM)) {
            return uriFragment.substring(ACCESS_TOKEN_PARAM.length());
        }
        return null;
    }

    public boolean isAuthorized() {
        return getAccessToken() != null;
    }
}