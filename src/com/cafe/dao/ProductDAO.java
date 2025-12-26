package com.cafe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.Product;

public class ProductDAO {
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, name, category, price FROM product";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setCategory(rs.getString("category"));
                p.setPrice(rs.getDouble("price"));
                list.add(p);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

