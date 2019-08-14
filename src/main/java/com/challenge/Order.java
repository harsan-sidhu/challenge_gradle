
package com.challenge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// TODO Consider AutoValue
public class Order {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("temp")
    @Expose
    private String temp;

    @SerializedName("shelfLife")
    @Expose
    private Integer shelfLife;

    @SerializedName("decayRate")
    @Expose
    private Double decayRate;

    @SerializedName("value")
    @Expose
    private Double value;

    @SerializedName("order_time_stamp")
    @Expose
    private Long orderTimeStamp;

    @SerializedName("time_to_pickup_order")
    @Expose
    private int timeToPickupOrder;

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Double getDecayRate() {
        return decayRate;
    }

    public void setDecayRate(Double decayRate) {
        this.decayRate = decayRate;
    }

    private Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getOrderTimeStamp() {
        return orderTimeStamp;
    }

    public void setOrderTimeStamp(Long orderTimeStamp) {
        this.orderTimeStamp = orderTimeStamp;
    }

    // TODO Reconsider this
    @Override
    public String toString() {
        return getName() + " " + getValue();
    }

    public void setTimeToPickupOrder(int timeToPickupOrder) {
        this.timeToPickupOrder = timeToPickupOrder;
    }

    public int getTimeToPickupOrder() {
        return timeToPickupOrder;
    }
}
