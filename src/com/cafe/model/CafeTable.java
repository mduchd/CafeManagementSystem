package com.cafe.model;

public class CafeTable {
    private int id;
    private String name;
    private String status;
    private int capacity;
    private String location;

    // Default constructor
    public CafeTable() {
    }

    // Full constructor
    public CafeTable(int id, String name, String status, int capacity, String location) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.capacity = capacity;
        this.location = location;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // toString
    @Override
    public String toString() {
        return "CafeTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                '}';
    }
}
