package com.challenge.order;

import com.google.gson.annotations.SerializedName;

enum OrderType {
    @SerializedName("hot")
    HOT,
    @SerializedName("cold")
    COLD,
    @SerializedName("frozen")
    FROZEN
}
