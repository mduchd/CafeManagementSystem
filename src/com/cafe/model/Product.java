/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

/**
 *
 * @author Owner
 */
public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private String status;
    
    public Product() {
    }
    
    public Product(int id, String name, String category, double price, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.status = status;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public double getPrice() {
        return price;
    }
    
    public String getStatus() {
        return status;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}