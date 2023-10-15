package com.Service;

import com.Class.Hotel;
import com.Class.Room;
import com.Enum.RoomStatusEnum;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class DataSeedingService {
    public static Hotel createSeedData() {
        // Initialise seed data
        Comparator<Room> distanceComparator = Comparator.comparing(Room::getDistance);
        PriorityQueue<Room> availableRooms = new PriorityQueue<>(distanceComparator);
        HashMap<String, Room> rooms = new HashMap<>();
        char[] suffixes = { 'A', 'B', 'C', 'D', 'E' };
        char[] reversedSuffixes = { 'E', 'D', 'C', 'B', 'A' };
        int distance = 1;
        // 4 storeys
        for (int i = 1; i < 5; i++) {
            // 5 rooms per storey
            for (int j = 1; j < 6; j++) {
                char suffix;
                if (i % 2 != 0) {
                    suffix = suffixes[j - 1];
                }
                else {
                    suffix = reversedSuffixes[j - 1];
                }
                Room newRoom = new Room(distance, i, suffix, RoomStatusEnum.AVAILABLE);
                availableRooms.offer(newRoom);
                rooms.put(newRoom.getRoomNumber(), newRoom);
                distance++;
            }
        }
        return new Hotel("Botique Hotel", rooms, availableRooms);
    }
}
