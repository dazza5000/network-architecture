package com.amicly.bignerdranchadvanced;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class VenueListAdapter extends RecyclerView.Adapter<VenueHolder> {
    private List<Venue> mVenueList;

    public VenueListAdapter(List<Venue> venueList) {
        mVenueList = venueList;
    }

    public void setVenueList(List<Venue> venueList) {
        mVenueList = venueList;
        notifyDataSetChanged();
    }

    @Override
    public VenueHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        VenueView venueView = new VenueView(viewGroup.getContext());
        return new VenueHolder(venueView);
    }

    @Override
    public void onBindViewHolder(VenueHolder venueHolder, int position) {
        venueHolder.bindVenue(mVenueList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVenueList.size();
    }
}
