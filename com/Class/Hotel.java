package com.Class;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Hotel {
    private String name;
    private HashMap<String, Room> rooms;
    private PriorityQueue<Room> availableRooms;

    public Hotel(String name, HashMap<String, Room> rooms, PriorityQueue<Room> availableRooms) {
        this.setName(name);
        this.setRooms(rooms);
        this.setAvailableRooms(availableRooms);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PriorityQueue<Room> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(PriorityQueue<Room> availableRooms) {
        this.availableRooms = availableRooms;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, Room> rooms) {
        this.rooms = rooms;
    }
}
