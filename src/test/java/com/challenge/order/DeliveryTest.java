package com.challenge.order;

import com.challenge.shelf.HotShelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

import static com.challenge.order.OrderType.HOT;

class DeliveryTest {

    Delivery delivery;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getValue_returnProperValue() {
        HotShelf hotShelf = new HotShelf(new ConcurrentLinkedQueue<>());
        Order order = Order.create("Beef Hash", HOT, 30, 0.74);
        delivery = new Delivery(order, 1, 5);
        delivery.setShelf(hotShelf);

        double orderValue = delivery.getValue();

        System.out.println(orderValue);

    }
}
