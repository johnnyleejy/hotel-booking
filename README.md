# hotel-booking

## Introduction

This is a simple CLI based application that supports the below operations:

1. Requesting for the nearest available room
2. Checking out of a room
3. Marking a room as cleaned (Available)
4. Marking a room for repair
5. Marking a room as repaired (VACANT)
6. List all the available rooms

## Side notes

Some design choices I made:

1. Used a priority queue (min heap) to store all available rooms. This is done so retrieving the nearest avaialble room is **O(1)** which is the first element of the priority queue.
2. Used a hashmap to store all hotel rooms so retrieval can be done performantly. (Equivalent to a primay index) 
3. Used a scanner for the user to interact with the CLI interface
4. Created a `RoomManagementService` class that is responsible for room specific operations (booking, checking out, marking and also listing available rooms).
5. Created a `DataSeedingService` to insert test data into memory

## Additional notes
Although finding the nearest available room takes O(1) using a priority queue, removing any element will restructure the priority queue and thus, requires O(log n) time complexity.

## Tests

Written test scenarios can be found [here](https://github.com/johnnyleejy/hotel-booking/blob/master/com/Tests/RoomManagementServiceTest.java)
