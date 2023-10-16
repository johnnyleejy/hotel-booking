package com.Service;

import com.Enum.RoomStatusEnum;
import com.Exceptions.InvalidStatusException;
import com.Exceptions.NoSuchRoomException;

import com.Class.Hotel;
import com.Class.Room;

import java.util.PriorityQueue;

public class RoomManagementService {
    private final Hotel hotel;

    public RoomManagementService(Hotel hotel) {
        this.hotel = hotel;
    }

    /**
     * Books the nearest available room and updates its status to occupied.
     * @return the nearest available room
     * @throws NoSuchRoomException when there are no available rooms for booking
     */
    public Room requestForAvailableRoom() throws NoSuchRoomException {
        Room availableRoom = this.hotel.getAvailableRooms().poll();
        if (availableRoom == null) {
            throw new NoSuchRoomException();
        }
        availableRoom.setRoomStatus(RoomStatusEnum.OCCUPIED);
        return availableRoom;
    }

    /**
     * Searches for a room and returns the result.
     * @param roomNumber of the room
     * @return the retrieved room
     * @throws NoSuchRoomException when no such room exists
     */
    public Room findRoom(String roomNumber) throws NoSuchRoomException {
        Room retrievedRoom = this.hotel.getRooms().get(roomNumber);
        if (retrievedRoom == null) {
            System.out.println("Room: " + roomNumber + " does not exist");
            throw new NoSuchRoomException();
        }
        return retrievedRoom;
    }

    /**
     * Checks out the given room. Only occupied rooms can be checked out.
     * @param roomNumber of the room
     * @throws NoSuchRoomException when no such room exists
     * @throws InvalidStatusException when the room is not OCCUPIED
     */
    public void checkOutRoom(String roomNumber) throws NoSuchRoomException, InvalidStatusException {
        Room selectedRoom = this.findRoom(roomNumber);
        // Only occupied rooms can be made vacant
        if (selectedRoom.getRoomStatus() != RoomStatusEnum.OCCUPIED) {
            throw new InvalidStatusException("Only OCCUPIED rooms can be updated to VACANT");
        }
        // Update status
        selectedRoom.setRoomStatus(RoomStatusEnum.VACANT);
    }

    /**
     * Updates a VACANT room to AVAILABLE.
     * @param roomNumber of the room
     * @throws NoSuchRoomException when no such room exists
     * @throws InvalidStatusException when the room is not VACANT
     */
    public void markRoomAsAvailable(String roomNumber) throws NoSuchRoomException, InvalidStatusException {
        Room selectedRoom = this.findRoom(roomNumber);
        // Only vacant rooms can be made available
        if (selectedRoom.getRoomStatus() != RoomStatusEnum.VACANT) {
            throw new InvalidStatusException("Only VACANT rooms can be updated to AVAILABLE");
        }
        // Update status
        selectedRoom.setRoomStatus(RoomStatusEnum.AVAILABLE);
        // Add back to available rooms
        hotel.getAvailableRooms().offer(selectedRoom);
    }

    /**
     * Marks a VACANT room as under REPAIR.
     * @param roomNumber of the room
     * @throws NoSuchRoomException when no such room exists
     * @throws InvalidStatusException when the room is not VACANT
     */
    public void markRoomForRepair(String roomNumber) throws NoSuchRoomException, InvalidStatusException {
        // Only vacant rooms can be marked as repair
        Room selectedRoom = this.findRoom(roomNumber);
        if (selectedRoom.getRoomStatus() != RoomStatusEnum.VACANT) {
            throw new InvalidStatusException("Only VACANT rooms can be marked for REPAIR");
        }
        // Update status
        selectedRoom.setRoomStatus(RoomStatusEnum.REPAIR);
    }

    /**
     * Updates a repaired room to VACANT status.
     * @param roomNumber of the room
     * @throws NoSuchRoomException when no such room exists
     * @throws InvalidStatusException when the room is not of REPAIR status
     */
    public void markRoomAsRepaired(String roomNumber) throws NoSuchRoomException, InvalidStatusException {
        // Only repaired rooms can be updated to vacant
        Room selectedRoom = this.findRoom(roomNumber);
        if (selectedRoom.getRoomStatus() != RoomStatusEnum.REPAIR) {
            throw new InvalidStatusException("Only REPAIRED rooms can be marked as VACANT");
        }
        // Update status
        selectedRoom.setRoomStatus(RoomStatusEnum.VACANT);
    }

    /**
     * Retrieves and prints the list of all available rooms.
     * @return the queue of all available rooms
     */
    public PriorityQueue<Room> listAllAvailableRooms() {
        for (Room availableRoom: this.hotel.getAvailableRooms()) {
            System.out.println("Room: " + availableRoom.getLevel() + availableRoom.getSuffix());
        }
        return this.hotel.getAvailableRooms();
    }
}
