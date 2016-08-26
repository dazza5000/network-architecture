package com.amicly.bignerdranchadvanced;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by darrankelinske on 8/25/16.
 */
public interface VenueInterface {
    @GET("venues/search")
    Call<VenueSearchResponse> venueSearch(@Query("ll") String latLngString);

    }

