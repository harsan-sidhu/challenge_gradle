package com.challenge.order;

import com.google.gson.annotations.SerializedName;

/**
 * Enum representing the type of the order. Note that the server only returns hot, cold, or frozen.
 */
public enum OrderType {
    @SerializedName("hot")
    HOT,
    @SerializedName("cold")
    COLD,
    @SerializedName("frozen")
    FROZEN,
    @SerializedName("overflow")
    OVERFLOW
}
