Order Dispatcher

Requirements:
--------------
Java 1.8
Gradle 3.0 >

Build Instructions
--------------
In the project directory:
1) ./gradlew run

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

As a result, any time an order is added to the overflow shelf, we perform the above calculation to deem whether the item
is "at risk", it's normalized value would reach zero if left on that shelf. If this is the case, it'll be added to a
Priority Queue sorted by the value it would have if moved to its temperature appropriate shelf. When an order is removed
we then ask the overflow shelf to give us the most "at risk" order for the shelf an item was just removed from, then add
it to the temperate appropriate shelf.

Potential Future Approaches:

1) Instead of strictly tying a driver to a specific order, we can simply dispatch a driver when an order is placed. That
   driver can arrive at the facility and pick up any order.

   Then we can sort the orders by the age required for the value to hit 0. This can be calculated by using 0 as the
   value in our formula and then solving for orderAge.

   This will ensure that the closest drivers will pickup the most time sensitive orders.

   This introduces an issue of shifting wait times for a customer because an order could be placed after there's which
   is more time sensitive, thus resulting in a longer ETA.