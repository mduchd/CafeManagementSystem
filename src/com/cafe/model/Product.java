package com.cafe.model;

/**
 * Lớp Model đại diện cho sản phẩm trong hệ thống Cafe
 * @author Owner
 */
public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private String status;
    private String image; 

    // Constructor không tham số
    public Product() {
    }

    // Constructor đầy đủ tham số
    public Product(int id, String name, String category, double price, String status, String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.status = status;
        this.image = image;
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

    public String getImage() {
        return image;
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

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", price=" + price + '}';
    }
}