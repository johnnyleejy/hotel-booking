package com.Class;

import com.Enum.RoomStatusEnum;

public class Room {
    private int distance;
    private int level;
    private char suffix;
    private RoomStatusEnum roomStatus;

    public Room(int distance, int level, char suffix, RoomStatusEnum roomStatus) {
        this.setDistance(distance);
        this.setLevel(level);
        this.setSuffix(suffix);
        this.setRoomStatus(roomStatus);
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public char getSuffix() {
        return suffix;
    }

    public void setSuffix(char suffix) {
        this.suffix = suffix;
    }

    public RoomStatusEnum getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatusEnum roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomNumber() {
        return Integer.toString(this.getLevel()) + this.getSuffix();
    }
}
