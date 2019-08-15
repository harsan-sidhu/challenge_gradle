package com.challenge.order;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Order {

    @SerializedName("name")
    public abstract String getName();

    @SerializedName("temp")
    public abstract String getTemp();

    @SerializedName("shelfLife")
    public abstract Integer getShelfLife();

    @SerializedName("decayRate")
    public abstract Double getDecayRate();

    public static TypeAdapter<Order> typeAdapter(Gson gson) {
        return new AutoValue_Order.GsonTypeAdapter(gson);
    }
}
