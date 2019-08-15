Order Dispatcher

Requirements:
--------------
Java 1.8
Gradle 3.0 >

Build Instructions
--------------
In the project directory:
1) ./gradlew build
2) ./gradlew run

IntelliJ Instructions
--------------
1) Import this as a gradle project.
2) From the Gradle task options, choose run or Right Click Main and Run

Test Instructions
--------------
1) In the project directory: ./gradlew clean test


Overflow Algorithm
--------------
Current Approach:

Currently, we dispatched a specific driver for a specific order.

Given this, when we assign the driver with an ETA for an order pickup, we can calculate for any order its normalized
value upon pick up. This is because the order age will equal the ETA.

As a result, any time an item is moved off of the shelf and there are items on the overflow shelf, we'll check to see if
the value of any of the orders on the overflow shelf would reach 0 given the decayRate of the overflow shelf.
If that is the case, we'll do another check to see whether moving it to a non overflow shelf will "save" the order. If
that is the case, we'll move the temperature specific orders to their corresponding shelf until they reach capacity.

Potential Future Approachs:

1) An improvement on this existing algorithm would be to perform this calculation the moment the order is placed and
   ensure that an item of this nature never makes it to the overflow shelf in the first place. However, if all the
   shelves are full, we'll fallback to our original approach as items are removed from temperature specific shelves.

2) Instead of strictly tying a driver to a specific order, we can simply dispatch a driver when an order is placed. That
   driver can arrive at the facility and pick up any order.

   Then we can sort the orders by the age required for the value to hit 0. This can be calculated by using 0 as the
   value in our formula and then solving for orderAge.

   This will ensure that the closest drivers will pickup the most time sensitive orders.

   This introduces an issue of shifting wait times for a customer because an order could be placed after there's which
   is more time sensitive, thus resulting in a longer ETA.