package com.cafe.model;

import java.util.Date;
import java.util.List;

/**
 * Model for Order (Hóa đơn)
 * Maps to HoaDon table
 */
public class Order {
    private int id;
    private Date createdDate;
    private double totalAmount;
    private String createdBy;
    private List<OrderDetail> details;
    
    // Constructors
    public Order() {
        this.createdDate = new Date();
    }
    
    public Order(int id, Date createdDate, double totalAmount, String createdBy) {
        this.id = id;
        this.createdDate = createdDate;
        this.totalAmount = totalAmount;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public Date getCreatedDate() { 
        return createdDate; 
    }
    
    public void setCreatedDate(Date createdDate) { 
        this.createdDate = createdDate; 
    }
    
    public double getTotalAmount() { 
        return totalAmount; 
    }
    
    public void setTotalAmount(double totalAmount) { 
        this.totalAmount = totalAmount; 
    }
    
    public String getCreatedBy() { 
        return createdBy; 
    }
    
    public void setCreatedBy(String createdBy) { 
        this.createdBy = createdBy; 
    }
    
    public List<OrderDetail> getDetails() { 
        return details; 
    }
    
    public void setDetails(List<OrderDetail> details) { 
        this.details = details; 
    }
}
