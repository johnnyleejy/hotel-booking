package com.Tests;

import com.Class.Hotel;
import com.Class.Room;
import com.Enum.RoomStatusEnum;
import com.Exceptions.InvalidStatusException;
import com.Exceptions.NoSuchRoomException;
import com.Service.RoomManagementService;
import com.Service.DataSeedingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

public class RoomManagementServiceTest {
    // Test fixtures setup
    private RoomManagementService roomManagementService;

    @BeforeEach
    public void setUp() {
        // Set up 20 rooms, 4 levels, 5 rooms per level
        Hotel hotel = DataSeedingService.createSeedData();
        roomManagementService = new RoomManagementService(hotel);
        Assertions.assertEquals(20, roomManagementService.listAllAvailableRooms().size());
    }

    @Nested
    class RequestForAvailableRoomScenarios {
        @Test
        public void requestForAvailableRoom_success() throws NoSuchRoomException {
            // Given some available rooms
            // When a room is requested
            Room requestedRoom = roomManagementService.requestForAvailableRoom();

            // Then a room should be successfully occupied
            Assertions.assertEquals(19, roomManagementService.listAllAvailableRooms().size());
            Assertions.assertEquals(requestedRoom.getRoomStatus(), RoomStatusEnum.OCCUPIED);
            Assertions.assertEquals(requestedRoom.getRoomNumber(), "1A");
            Assertions.assertEquals(requestedRoom.getDistance(), 1);
            Assertions.assertEquals(requestedRoom.getLevel(), 1);
            Assertions.assertEquals(requestedRoom.getSuffix(), 'A');

            // And when another room is requested
            Room requestedRoom2 = roomManagementService.requestForAvailableRoom();

            // Then the next available room should be assigned correctly
            Assertions.assertEquals(18, roomManagementService.listAllAvailableRooms().size());
            Assertions.assertEquals(requestedRoom2.getRoomStatus(), RoomStatusEnum.OCCUPIED);
            Assertions.assertEquals(requestedRoom2.getRoomNumber(), "1B");
            Assertions.assertEquals(requestedRoom2.getDistance(), 2);
            Assertions.assertEquals(requestedRoom2.getLevel(), 1);
            Assertions.assertEquals(requestedRoom2.getSuffix(), 'B');
        }

        @Test
        public void requestForAvailableRoom_success_assignNewlyAvailableRoom() throws NoSuchRoomException, InvalidStatusException {
            // Given 3 vacant rooms. 1A, 1B and 1C.
            Room room1 = roomManagementService.requestForAvailableRoom();
            Room room2 = roomManagementService.requestForAvailableRoom();
            Room room3 = roomManagementService.requestForAvailableRoom();
            roomManagementService.checkOutRoom(room1.getRoomNumber());
            roomManagementService.checkOutRoom(room2.getRoomNumber());
            roomManagementService.checkOutRoom(room3.getRoomNumber());
            Assertions.assertEquals(17, roomManagementService.listAllAvailableRooms().size());

            // When room 1B is made available
            roomManagementService.markRoomAsAvailable(room2.getRoomNumber());

            // Then the next requestForAvailableRoom call should assign room 1B
            Room requestedRoom = roomManagementService.requestForAvailableRoom();
            Assertions.assertEquals(requestedRoom.getRoomStatus(), RoomStatusEnum.OCCUPIED);
            Assertions.assertEquals(requestedRoom.getRoomNumber(), room2.getRoomNumber());
            Assertions.assertEquals(requestedRoom.getDistance(), room2.getDistance());
            Assertions.assertEquals(requestedRoom.getLevel(), room2.getLevel());
            Assertions.assertEquals(requestedRoom.getSuffix(), room2.getSuffix());
        }

        @Test
        public void requestForAvailableRoom_error_noMoreRooms() throws NoSuchRoomException {
            // Given that no rooms are available
            for (int i = 0; i < 20; i++) {
                roomManagementService.requestForAvailableRoom();
            }
            Assertions.assertEquals(0, roomManagementService.listAllAvailableRooms().size());

            // When a room is requested
            // Then an exception will be thrown
            Assertions.assertThrows(NoSuchRoomException.class, ()-> roomManagementService.requestForAvailableRoom());
        }
    }

    @Nested
    class FindRoomScenarios {
        @Test
        public void findRoom_success() throws NoSuchRoomException {
            // Given some rooms
            // When a room is retrieved
            Room searchRoom = roomManagementService.findRoom("1A");

            // Then the correct room should be retrieved
            Assertions.assertEquals(searchRoom.getRoomStatus(), RoomStatusEnum.AVAILABLE);
            Assertions.assertEquals(searchRoom.getRoomNumber(), "1A");
            Assertions.assertEquals(searchRoom.getDistance(), 1);
            Assertions.assertEquals(searchRoom.getLevel(), 1);
            Assertions.assertEquals(searchRoom.getSuffix(), 'A');

            // And when the room is requested
            roomManagementService.requestForAvailableRoom();
            Assertions.assertEquals(19, roomManagementService.listAllAvailableRooms().size());
            Room updated = roomManagementService.findRoom("1A");

            // Then the retrieved room should be updated
            Assertions.assertEquals(updated.getRoomStatus(), RoomStatusEnum.OCCUPIED);
            Assertions.assertEquals(updated.getRoomNumber(), "1A");
            Assertions.assertEquals(updated.getDistance(), 1);
            Assertions.assertEquals(updated.getLevel(), 1);
            Assertions.assertEquals(updated.getSuffix(), 'A');
        }

        @Test
        public void findRoom_error_noSuchRoom() {
            // Given some rooms
            // When an invalid room is retrieved
            // Then an exception will be thrown
            Assertions.assertThrows(NoSuchRoomException.class, ()-> roomManagementService.findRoom("99A"));
        }
    }

    @Nested
    class CheckOutRoomScenarios {
        @Test
        public void checkOutRoom_success() throws InvalidStatusException, NoSuchRoomException {
            // Given a requested room
            Room requestedRoom = roomManagementService.requestForAvailableRoom();
            Assertions.assertEquals(19, roomManagementService.listAllAvailableRooms().size());
            Assertions.assertEquals(requestedRoom.getRoomStatus(), RoomStatusEnum.OCCUPIED);
            Assertions.assertEquals(requestedRoom.getRoomNumber(), "1A");
            Assertions.assertEquals(requestedRoom.getDistance(), 1);
            Assertions.assertEquals(requestedRoom.getLevel(), 1);
            Assertions.assertEquals(requestedRoom.getSuffix(), 'A');

            // When the requested room is checked out
            roomManagementService.checkOutRoom(requestedRoom.getRoomNumber());

            // Then the room should be checked out successfully
            Room checkedOutRoom = roomManagementService.findRoom(requestedRoom.getRoomNumber());
            Assertions.assertEquals(checkedOutRoom.getRoomStatus(), RoomStatusEnum.VACANT);
            Assertions.assertEquals(checkedOutRoom.getRoomNumber(), "1A");
            Assertions.assertEquals(checkedOutRoom.getDistance(), 1);
            Assertions.assertEquals(checkedOutRoom.getLevel(), 1);
            Assertions.assertEquals(checkedOutRoom.getSuffix(), 'A');
        }

        @Test
        public void checkOutRoom_error_invalidStatus() {
            // Given some rooms
            // When an available room is checked out
            // Then an exception will be thrown
            Exception exception = Assertions.assertThrows(InvalidStatusException.class, ()->
                    roomManagementService.checkOutRoom("1A"));
            Assertions.assertEquals(exception.getMessage(), "Only OCCUPIED rooms can be updated to VACANT");
        }
    }

    @Nested
    class MarkRoomAsAvailableScenarios {
        @Test
        public void markRoomAsAvailable_success() throws InvalidStatusException, NoSuchRoomException {
            // Given a vacant room
            Room requestedRoom = roomManagementService.requestForAvailableRoom();
            Assertions.assertEquals(19, roomManagementService.listAllAvailableRooms().size());
            roomManagementService.checkOutRoom(requestedRoom.getRoomNumber());

            // When the vacant room is marked as available
            roomManagementService.markRoomAsAvailable(requestedRoom.getRoomNumber());

            // Then the room should be updated correctly
            Room room = roomManagementService.findRoom(requestedRoom.getRoomNumber());
            Assertions.assertEquals(room.getRoomStatus(), RoomStatusEnum.AVAILABLE);
            Assertions.assertEquals(room.getRoomNumber(), "1A");
            Assertions.assertEquals(room.getDistance(), 1);
            Assertions.assertEquals(room.getLevel(), 1);
            Assertions.assertEquals(room.getSuffix(), 'A');

            // And the newly available room should be added back to the list of available rooms
            Assertions.assertEquals(20, roomManagementService.listAllAvailableRooms().size());
        }

        @Test
        public void markRoomAsAvailable_error_invalidStatus() {
            // Given an available room
            // When the available room is marked as available
            // Then an exception will be thrown
            Exception exception = Assertions.assertThrows(InvalidStatusException.class, ()->
                    roomManagementService.markRoomAsAvailable("1A"));
            Assertions.assertEquals(exception.getMessage(), "Only VACANT rooms can be updated to AVAILABLE");
        }
    }

    @Nested
    class MarkRoomForRepairScenarios {
        @Test
        public void markRoomForRepair_success() throws InvalidStatusException, NoSuchRoomException {
            // Given a vacant room
            Room requestedRoom = roomManagementService.requestForAvailableRoom();
            roomManagementService.checkOutRoom(requestedRoom.getRoomNumber());

            // When the vacant room is marked as available
            roomManagementService.markRoomForRepair(requestedRoom.getRoomNumber());

            // Then the room should be updated correctly
            Room room = roomManagementService.findRoom(requestedRoom.getRoomNumber());
            Assertions.assertEquals(room.getRoomStatus(), RoomStatusEnum.REPAIR);
            Assertions.assertEquals(room.getRoomNumber(), "1A");
            Assertions.assertEquals(room.getDistance(), 1);
            Assertions.assertEquals(room.getLevel(), 1);
            Assertions.assertEquals(room.getSuffix(), 'A');
        }

        @Test
        public void markRoomForRepair_error_invalidStatus() {
            // Given an available room
            // When the available room is marked for repair
            // Then an exception will be thrown
            Exception exception = Assertions.assertThrows(InvalidStatusException.class, ()->
                    roomManagementService.markRoomForRepair("1A"));
            Assertions.assertEquals(exception.getMessage(), "Only VACANT rooms can be marked for REPAIR");
        }
    }
}
