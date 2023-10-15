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

    public Room requestForAvailableRoom() throws NoSuchRoomException {
        Room availableRoom = this.hotel.getAvailableRooms().poll();
        if (availableRoom == null) {
            throw new NoSuchRoomException();
        }
        availableRoom.setRoomStatus(RoomStatusEnum.OCCUPIED);
        return availableRoom;
    }

    public Room findRoom(String roomNumber) throws NoSuchRoomException {
        Room retrievedRoom = this.hotel.getRooms().get(roomNumber);
        if (retrievedRoom == null) {
            System.out.println("Room: " + roomNumber + " does not exist");
            throw new NoSuchRoomException();
        }
        return retrievedRoom;
    }

    public void checkOutRoom(String roomNumber) throws NoSuchRoomException, InvalidStatusException {
        Room selectedRoom = this.findRoom(roomNumber);
        // Only occupied rooms can be made vacant
        if (selectedRoom.getRoomStatus() != RoomStatusEnum.OCCUPIED) {
            throw new InvalidStatusException("Only OCCUPIED rooms can be updated to VACANT");
        }
        // Update status
        selectedRoom.setRoomStatus(RoomStatusEnum.VACANT);
    }

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

    public void markRoomForRepair(String roomNumber) throws NoSuchRoomException, InvalidStatusException {
        // Only vacant rooms can be marked as repair
        Room selectedRoom = this.findRoom(roomNumber);
        if (selectedRoom.getRoomStatus() != RoomStatusEnum.VACANT) {
            throw new InvalidStatusException("Only VACANT rooms can be marked for REPAIR");
        }
        // Update status
        selectedRoom.setRoomStatus(RoomStatusEnum.REPAIR);
    }

    public PriorityQueue<Room> listAllAvailableRooms() {
        for (Room availableRoom: this.hotel.getAvailableRooms()) {
            System.out.println("Room: " + availableRoom.getLevel() + availableRoom.getSuffix());
        }
        return this.hotel.getAvailableRooms();
    }
}
