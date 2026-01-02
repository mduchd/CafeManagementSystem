/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.service;

import com.cafe.dao.ProductDAO;
import com.cafe.model.Product;
import java.util.List;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    // Lấy danh sách sản phẩm (đổ vào JTable)
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    // Thêm sản phẩm mới
    public boolean addProduct(Product p) {
        if (!validateProduct(p)) {
            return false;
        }
        return productDAO.insert(p);
    }

    // Cập nhật sản phẩm
    public boolean updateProduct(Product p) {
        if (p.getId() <= 0 || !validateProduct(p)) {
            return false;
        }
        return productDAO.update(p);
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int id) {
        if (id <= 0) {
            return false;
        }
        return productDAO.delete(id);
    }

    // Kiểm tra dữ liệu đầu vào
    private boolean validateProduct(Product p) {
        if (p == null) return false;
        if (p.getName() == null || p.getName().trim().isEmpty()) return false;
        if (p.getCategory() == null || p.getCategory().trim().isEmpty()) return false;
        if (p.getPrice() <= 0) return false;
        return true;
    }
    
    /**
     * Lấy ID sản phẩm theo tên
     * Dùng cho việc tạo hóa đơn từ SalesPanel
     */
    public int getProductIdByName(String name) {
        return productDAO.getProductIdByName(name);
    }
}
