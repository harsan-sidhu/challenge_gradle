package com.challenge.order;

import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * {@link GsonTypeAdapterFactory} which allows our {@link Gson} instance to parse a json with Orders and convert them to
 * {@link Order}.
 */
@GsonTypeAdapterFactory
public abstract class AutoValueGsonFactory implements TypeAdapterFactory {

    public static TypeAdapterFactory create() {
        return new AutoValueGson_AutoValueGsonFactory();
    }
}
