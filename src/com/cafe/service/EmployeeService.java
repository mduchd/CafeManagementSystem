package com.cafe.service;

import com.cafe.dao.EmployeeDAO;
import com.cafe.model.Employee;
import java.util.List;

public class EmployeeService {
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    // Get all employees
    public List<Employee> getAll() {
        return employeeDAO.getAll();
    }

    // Get employee by ID
    public Employee getById(int id) {
        return employeeDAO.getById(id);
    }

    // Add new employee with validation
    public void add(Employee emp) throws Exception {
        validateEmployee(emp);
        boolean success = employeeDAO.add(emp);
        if (!success) {
            throw new Exception("Failed to add employee!");
        }
    }

    // Update employee with validation
    public void update(Employee emp) throws Exception {
        validateEmployee(emp);
        boolean success = employeeDAO.update(emp);
        if (!success) {
            throw new Exception("Failed to update employee!");
        }
    }

    // Delete employee by ID
    public void delete(int id) throws Exception {
        boolean success = employeeDAO.delete(id);
        if (!success) {
            throw new Exception("Failed to delete employee!");
        }
    }

    // Search employees by keyword
    public List<Employee> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return employeeDAO.search(keyword.trim());
    }

    // Validate employee data
    private void validateEmployee(Employee emp) throws Exception {
        if (emp.getFullName() == null || emp.getFullName().trim().isEmpty()) {
            throw new Exception("Name is required!");
        }
        if (emp.getPhoneNumber() != null && !emp.getPhoneNumber().isEmpty()) {
            if (!emp.getPhoneNumber().matches("\\d{10,11}")) {
                throw new Exception("Invalid phone number!");
            }
        }
        if (emp.getEmail() != null && !emp.getEmail().isEmpty()) {
            if (!emp.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                throw new Exception("Invalid email format!");
            }
        }
        if (emp.getSalary() < 0) {
            throw new Exception("Salary cannot be negative!");
        }
    }
}
