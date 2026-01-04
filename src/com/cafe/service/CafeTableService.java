package com.cafe.service;

import com.cafe.dao.CafeTableDAO;
import com.cafe.model.CafeTable;
import java.util.List;

public class CafeTableService {
    private CafeTableDAO tableDAO = new CafeTableDAO();

    // Table status constants
    public static final String STATUS_AVAILABLE = "Trong";
    public static final String STATUS_IN_USE = "DangSuDung";
    public static final String STATUS_RESERVED = "DaDat";

    // Get all tables
    public List<CafeTable> getAll() {
        return tableDAO.getAll();
    }

    // Get table by ID
    public CafeTable getById(int id) {
        return tableDAO.getById(id);
    }

    // Add new table with validation
    public void add(CafeTable table) throws Exception {
        validateTable(table);
        if (table.getStatus() == null || table.getStatus().isEmpty()) {
            table.setStatus(STATUS_AVAILABLE);
        }
        boolean success = tableDAO.add(table);
        if (!success) {
            throw new Exception("Failed to add table!");
        }
    }

    // Update table with validation
    public void update(CafeTable table) throws Exception {
        validateTable(table);
        boolean success = tableDAO.update(table);
        if (!success) {
            throw new Exception("Failed to update table!");
        }
    }

    // Change table status
    public void changeStatus(int id, String status) throws Exception {
        if (!isValidStatus(status)) {
            throw new Exception("Invalid status! Use: Trong, DangSuDung, DaDat");
        }
        boolean success = tableDAO.updateStatus(id, status);
        if (!success) {
            throw new Exception("Failed to update table status!");
        }
    }

    // Delete table
    public void delete(int id) throws Exception {
        boolean success = tableDAO.delete(id);
        if (!success) {
            throw new Exception("Failed to delete table!");
        }
    }

    // Search tables
    public List<CafeTable> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return tableDAO.search(keyword.trim());
    }

    // Get available tables
    public List<CafeTable> getAvailableTables() {
        return tableDAO.getByStatus(STATUS_AVAILABLE);
    }

    // Validate table data
    private void validateTable(CafeTable table) throws Exception {
        if (table.getName() == null || table.getName().trim().isEmpty()) {
            throw new Exception("Table name is required!");
        }
        if (table.getCapacity() <= 0) {
            throw new Exception("Capacity must be greater than 0!");
        }
    }

    // Check if status is valid
    private boolean isValidStatus(String status) {
        return STATUS_AVAILABLE.equals(status) ||
                STATUS_IN_USE.equals(status) ||
                STATUS_RESERVED.equals(status);
    }
}
