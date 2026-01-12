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
    public boolean insertProduct(Product p) {
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
    
    public int getProductIdByName(String name) {
        return productDAO.getProductIdByName(name);
    }
    
    // Lọc sản phẩm theo loại
    public List<Product> getProductsByCategory(String category) {
        return productDAO.findByCategory(category);
    }
}
