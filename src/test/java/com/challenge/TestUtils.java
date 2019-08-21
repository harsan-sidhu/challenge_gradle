package com.challenge;

import com.challenge.order.Order;

import static com.challenge.order.OrderType.*;

public class TestUtils {

    public static Order createHotOrder() {
        return Order.create("Beef Hash", HOT, 30, 0.74);
    }

    public static Order createColdOrder() {
        return Order.create("Sushi", COLD, 251, 0.25);
    }

    public static Order createFrozenOrder() {
        return Order.create("Icy", FROZEN, 230, 0.6);
    }
}
