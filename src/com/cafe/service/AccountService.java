package com.cafe.service;

import com.cafe.dao.AccountDAO;
import com.cafe.model.Account;

import java.util.List;

/**
 * Service layer for Account management
 * Handles business logic and validation
 */
public class AccountService {
    private final AccountDAO accountDAO;

    // Default constructor - creates its own DAO
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    // Constructor injection for testing
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Get all accounts
    public List<Account> getAll() {
        return accountDAO.getAll();
    }

    // Get account by username
    public Account getByUsername(String username) {
        return accountDAO.getByUsername(username);
    }

    // Check if username exists
    public boolean existsByUsername(String username) {
        return accountDAO.existsByUsername(username);
    }

    // Add new account with validation
    public void add(Account acc) throws Exception {
        validateAccount(acc, true);

        // Check if username already exists
        if (accountDAO.existsByUsername(acc.getUsername())) {
            throw new Exception("Tên đăng nhập '" + acc.getUsername() + "' đã tồn tại!");
        }

        boolean success = accountDAO.add(acc);
        if (!success) {
            throw new Exception("Không thể thêm tài khoản!");
        }
    }

    // Update account with validation
    public void update(Account acc, boolean changePassword) throws Exception {
        validateAccount(acc, changePassword);

        // Check if account exists
        if (!accountDAO.existsByUsername(acc.getUsername())) {
            throw new Exception("Tài khoản '" + acc.getUsername() + "' không tồn tại!");
        }

        boolean success;
        if (changePassword) {
            success = accountDAO.update(acc);
        } else {
            success = accountDAO.updateWithoutPassword(acc);
        }

        if (!success) {
            throw new Exception("Không thể cập nhật tài khoản!");
        }
    }

    // Delete account by username
    public void delete(String username) throws Exception {
        // Prevent deleting the last admin account
        List<Account> admins = getAll().stream()
                .filter(Account::isAdmin)
                .toList();

        Account toDelete = accountDAO.getByUsername(username);
        if (toDelete != null && toDelete.isAdmin() && admins.size() <= 1) {
            throw new Exception("Không thể xóa tài khoản Admin cuối cùng!");
        }

        boolean success = accountDAO.delete(username);
        if (!success) {
            throw new Exception("Không thể xóa tài khoản!");
        }
    }

    // Search accounts by keyword
    public List<Account> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return accountDAO.search(keyword.trim());
    }

    // Validate account data
    private void validateAccount(Account acc, boolean validatePassword) throws Exception {
        // Username validation
        if (acc.getUsername() == null || acc.getUsername().trim().isEmpty()) {
            throw new Exception("Tên đăng nhập không được để trống!");
        }

        if (acc.getUsername().length() < 3) {
            throw new Exception("Tên đăng nhập phải có ít nhất 3 ký tự!");
        }

        if (!acc.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            throw new Exception("Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới!");
        }

        // Password validation (only for new accounts or password change)
        if (validatePassword) {
            if (acc.getPassword() == null || acc.getPassword().isEmpty()) {
                throw new Exception("Mật khẩu không được để trống!");
            }

            if (acc.getPassword().length() < 3) {
                throw new Exception("Mật khẩu phải có ít nhất 3 ký tự!");
            }
        }

        // Role validation
        if (acc.getRole() == null || acc.getRole().trim().isEmpty()) {
            throw new Exception("Vai trò không được để trống!");
        }

        if (!acc.getRole().equals("Admin") && !acc.getRole().equals("NhanVien")) {
            throw new Exception("Vai trò phải là 'Admin' hoặc 'NhanVien'!");
        }
    }
}
