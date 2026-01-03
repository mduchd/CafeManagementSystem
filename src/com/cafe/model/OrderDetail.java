package com.cafe.model;

/**
 * Model for OrderDetail (Chi tiết hóa đơn)
 * Maps to ChiTietHoaDon table
 */
public class OrderDetail {
    private int orderId;
    private int productId;
    private String productName;  // For display purposes
    private int quantity;
    private double unitPrice;    // For display purposes
    private double totalPrice;
    
    // Constructors
    public OrderDetail() {}
    
    public OrderDetail(int orderId, int productId, String productName, 
                       int quantity, double unitPrice, double totalPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public int getOrderId() { 
        return orderId; 
    }
    
    public void setOrderId(int orderId) { 
        this.orderId = orderId; 
    }
    
    public int getProductId() { 
        return productId; 
    }
    
    public void setProductId(int productId) { 
        this.productId = productId; 
    }
    
    public String getProductName() { 
        return productName; 
    }
    
    public void setProductName(String productName) { 
        this.productName = productName; 
    }
    
    public int getQuantity() { 
        return quantity; 
    }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }
    
    public double getUnitPrice() { 
        return unitPrice; 
    }
    
    public void setUnitPrice(double unitPrice) { 
        this.unitPrice = unitPrice; 
    }
    
    public double getTotalPrice() { 
        return totalPrice; 
    }
    
    public void setTotalPrice(double totalPrice) { 
        this.totalPrice = totalPrice; 
    }
}
