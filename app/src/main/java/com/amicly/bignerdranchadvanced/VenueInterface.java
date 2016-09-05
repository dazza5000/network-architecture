package com.amicly.bignerdranchadvanced;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by darrankelinske on 8/25/16.
 */
public interface VenueInterface {
    @GET("venues/search")
    Call<VenueSearchResponse> venueSearch(@Query("ll") String latLngString);

    @FormUrlEncoded
    @POST("checkins/add")
    Observable<Object> venueCheckIn(@Field("venueId") String venueId);

    }

