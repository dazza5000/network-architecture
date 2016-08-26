package com.amicly.bignerdranchadvanced;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by darrankelinske on 8/25/16.
 */
public class VenueListDeserializer implements JsonDeserializer<VenueSearchResponse> {
    @Override
    public VenueSearchResponse deserialize(JsonElement json, Type typeOfT,
                                           JsonDeserializationContext context) throws JsonParseException {

        JsonElement responseElement = json.getAsJsonObject().get("response");
        return new Gson().fromJson(responseElement, VenueSearchResponse.class);

    }
}
