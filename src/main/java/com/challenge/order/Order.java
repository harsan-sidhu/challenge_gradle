package com.challenge.order;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Data model representing an Order placed by a customer.
 */
@AutoValue
public abstract class Order {

    public static Order create(String name, OrderType orderType, Integer shelfLife, Double decayRate) {
        return new AutoValue_Order(name, orderType, shelfLife, decayRate);
    }

    @SerializedName("name")
    public abstract String getName();

    @SerializedName("temp")
    public abstract OrderType getTemp();

    @SerializedName("shelfLife")
    public abstract Integer getShelfLife();

    @SerializedName("decayRate")
    public abstract Double getDecayRate();

    public static TypeAdapter<Order> typeAdapter(Gson gson) {
        return new AutoValue_Order.GsonTypeAdapter(gson);
    }
}
