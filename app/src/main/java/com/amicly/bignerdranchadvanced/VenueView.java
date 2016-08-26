package com.amicly.bignerdranchadvanced;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class VenueView extends LinearLayout {
    private TextView mTitleTextView;
    private TextView mAddressTextView;

    public VenueView(Context context) {
        this(context, null);
    }

    public VenueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        setLayoutParams(params);

        LayoutInflater inflater = LayoutInflater.from(context);
        VenueView view = (VenueView) inflater.inflate(
                R.layout.view_venue, this, true);
        mTitleTextView = (TextView) view.findViewById(
                R.id.view_venue_list_VenueTitleTextView);
        mAddressTextView = (TextView) view.findViewById(
                R.id.view_venue_list_VenueLocationTextView);
    }

    public void setVenueTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setVenueAddress(String address) {
        mAddressTextView.setText(address);
    }
}
