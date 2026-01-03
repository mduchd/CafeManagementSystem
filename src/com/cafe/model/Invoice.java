package com.cafe.model;

public class Invoice {
    private int id;
    private int customerId;
    private int employeeId;
    private double totalAmount;
    private String status;

    public Invoice() {
    }

    // Constructor for full invoice details 
    public Invoice(int id, int customerId, int employeeId, double totalAmount, String status) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }   

    // toString method for easy debugging   
    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }   
}
