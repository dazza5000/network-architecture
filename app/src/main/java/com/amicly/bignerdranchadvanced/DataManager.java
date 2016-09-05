package com.amicly.bignerdranchadvanced;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.amicly.bignerdranchadvanced.exception.UnauthorizedException;
import com.amicly.bignerdranchadvanced.listener.VenueCheckInListener;
import com.amicly.bignerdranchadvanced.model.TokenStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class DataManager {
    private static final String TAG = "DataManager";
    private static final String FOURSQUARE_ENDPOINT
            = "https://api.foursquare.com/v2/";
    private static final String OAUTH_ENDPOINT ="" +
            "https://foursquare.com/oauth2/authenticate";
    public static final String OAUTH_REDIRECT_URI =
            "http://bignerdranch.com";
    private static final String CLIENT_ID = "RV5EALWWU2UQEAXUQVIEKFSNJBI55PFPCMIGS2450YPFD0TZ";
    private static final String CLIENT_SECRET = "MVJONWL2OTLGKSO0U0KNCD1H2XJVPRW4IKBBKOFZ1AJ4TX4P";
    private static final String FOURSQUARE_VERSION = "20150406";
    private static final String FOURSQUARE_MODE = "foursquare";
    private static final String SWARM_MODE = "swarm";
    private static final String TEST_LAT_LNG = "33.759,-84.332";
    private List<Venue> venueList;
    private List<VenueSearchListener> searchListenerList;
    private List<VenueCheckInListener> checkInListenerList;

    private static DataManager sDataManager;
    private Context mContext;
    private static TokenStore tokenStore;
    private Retrofit mBasicRestAdapter;
    private Retrofit authenticatedRestAdapter;

    public static DataManager get(Context context) {
        if (sDataManager == null ) {
            Gson gson = new GsonBuilder().registerTypeAdapter(VenueSearchResponse.class,
                    new VenueListDeserializer())
                    .create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(sRequestInterceptor)
                    .addInterceptor(sUnauthorizedInterceptor)
                    .build();

            OkHttpClient authenticatedHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(sAuthenticatedRequestInterceptor)
                    .addInterceptor(sUnauthorizedInterceptor)
                    .build();

            Retrofit basicRestAdapter = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(FOURSQUARE_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RxJavaCallAdapterFactory rxAdapter =
                    RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

            Retrofit authenticatedRestAdapter = new Retrofit.Builder()
                    .client(authenticatedHttpClient)
                    .baseUrl(FOURSQUARE_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(rxAdapter)
                    .build();

            sDataManager = new DataManager(context, basicRestAdapter, authenticatedRestAdapter);
        }

        return sDataManager;

    }

    private DataManager(Context context, Retrofit basicRestAdapter,
                        Retrofit authenticatedRestAdapter){
        mContext = context.getApplicationContext();
        tokenStore = TokenStore.get(mContext);
        mBasicRestAdapter = basicRestAdapter;
        this.authenticatedRestAdapter = authenticatedRestAdapter;
        searchListenerList = new ArrayList<>();
        checkInListenerList = new ArrayList<>();
    }

    private static Interceptor sRequestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("client_id", CLIENT_ID)
                    .addQueryParameter("client_secret", CLIENT_SECRET)
                    .addQueryParameter("v", FOURSQUARE_VERSION)
                    .addQueryParameter("m", FOURSQUARE_MODE)
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    };

    public static Interceptor sAuthenticatedRequestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("oauth_token", tokenStore.getAccessToken())
                    .addQueryParameter("v", FOURSQUARE_VERSION)
                    .addQueryParameter("m", SWARM_MODE)
                    .build();

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    };

    public static Interceptor sUnauthorizedInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            boolean unAuthorized = (response.code() == 401);
            if (unAuthorized) {
                throw new UnauthorizedException(new IOException("Unauthorized Exception"));
            }
            return response;
        }
    };

    public void fetchVenueSearch() {
        VenueInterface venueInterface =
                mBasicRestAdapter.create(VenueInterface.class);
        Call<VenueSearchResponse> call = venueInterface.venueSearch(TEST_LAT_LNG);
        call.enqueue(new Callback<VenueSearchResponse>() {
            @Override
            public void onResponse(Call<VenueSearchResponse> call, retrofit2.Response<VenueSearchResponse> response) {
                if (response.isSuccessful()) {
                    venueList = response.body().getVenueList();
                    notifySearchListeners();
                }

            }

            @Override
            public void onFailure(Call<VenueSearchResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " +t.toString());

            }
        });
    }

    public void checkInToVenue(String venueId) {
        VenueInterface venueInterface =
                authenticatedRestAdapter.create(VenueInterface.class);
        Observable<Object> call = venueInterface.venueCheckIn(venueId);
        call.subscribe(o -> {
            notifycheckInListeners();
        }, throwable -> {
            Log.d("venueCheckIn", "Have error: " + throwable.toString());
            if (throwable instanceof UnauthorizedException) {
                tokenStore.setAccessToken(null);
                notifyCheckInListenersTokenExpired();
            }
        });

    }

    public List<Venue> getVenueList() {
        return venueList;
    }

    public interface VenueSearchListener {
        void onVenueSearchFinished();
    }

    public void addVenueSearchListener(VenueSearchListener listener) {
        searchListenerList.add(listener);
    }

    public void removeVenueSearchListener(VenueSearchListener listener) {
        searchListenerList.remove(listener);
    }

    private void notifySearchListeners() {
        for(VenueSearchListener listener : searchListenerList) {
            listener.onVenueSearchFinished();
        }
    }

    public void addVenueCheckInListener(VenueCheckInListener listener) {
        checkInListenerList.add(listener);
    }

    public void removeVenueCheckInListener(VenueCheckInListener listener) {
        checkInListenerList.remove(listener);
    }

    private void notifycheckInListeners() {
        for(VenueCheckInListener listener : checkInListenerList) {
            listener.onVenueCheckInFinished();
        }
    }

    public String getAuthenticationUrl() {
        return Uri.parse(OAUTH_ENDPOINT).buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("redirect_uri", OAUTH_REDIRECT_URI)
                .build()
                .toString();
    }

    public Venue getVenue(String venueId) {
        for(Venue venue: venueList) {
            if(venue.getId().equals(venueId))
            {
                return venue;
            }
        }
        return null;
    }

    private void notifyCheckInListenersTokenExpired() {
        for (VenueCheckInListener listener : checkInListenerList) {
            listener.onTokenExpired();
        }
    }
}
