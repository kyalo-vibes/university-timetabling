package com.kyalo.universitytimetabling.domain;

public class RoomDTO {
    private String roomName;
    private int roomCapacity;
    private String roomType;
    private boolean isAvailable;
    private String deptName;

    public RoomDTO() {
    }

    public RoomDTO(String roomName, int roomCapacity, String roomType, boolean isAvailable, String deptName) {
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
        this.deptName = deptName;
    }

    // getters and setters

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "roomName='" + roomName + '\'' +
                ", roomCapacity=" + roomCapacity +
                ", roomType='" + roomType + '\'' +
                ", isAvailable=" + isAvailable +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
