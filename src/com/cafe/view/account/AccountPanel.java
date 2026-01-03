package com.cafe.view.account;

import com.cafe.model.Account;
import com.cafe.service.AccountService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Account Management Panel - CRUD operations for user accounts
 * UI design follows EmployeePanel pattern
 */
public class AccountPanel extends JPanel {

    // Service for database operations
    private AccountService accountService = new AccountService();

    // Table components
    private JTable tblAccounts;
    private DefaultTableModel tableModel;

    // Form input fields
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtDisplayName;
    private JComboBox<String> cboRole;
    private JTextField txtSearch;

    // Currently selected username (null = none)
    private String selectedUsername = null;

    // Constructor - initialize UI and load data
    public AccountPanel() {
        initComponents();
        loadData();
    }

    // Initialize all UI components
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Quản lý Tài khoản");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table area
        JPanel pCenter = new JPanel(new BorderLayout(10, 10));
        pCenter.setBackground(Color.WHITE);

        // Search panel
        JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSearch.setBackground(Color.WHITE);
        pSearch.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        pSearch.add(txtSearch);
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> search());
        pSearch.add(btnSearch);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });
        pSearch.add(btnRefresh);
        pCenter.add(pSearch, BorderLayout.NORTH);

        // Data table setup
        String[] columns = { "Tên đăng nhập", "Mật khẩu", "Tên hiển thị", "Vai trò" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        tblAccounts = new JTable(tableModel);
        tblAccounts.setRowHeight(28);
        tblAccounts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblAccounts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Row selection listener
        tblAccounts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblAccounts);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        add(pCenter, BorderLayout.CENTER);

        // Right - Form panel
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBackground(new Color(245, 245, 245));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pRight.setPreferredSize(new Dimension(280, 0));

        // Form title - centered
        JPanel pTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pTitle.setBackground(new Color(245, 245, 245));
        pTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblForm = new JLabel("Thông tin tài khoản");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pTitle.add(lblForm);
        pRight.add(pTitle);
        pRight.add(Box.createVerticalStrut(15));

        // Username field
        pRight.add(createLabel("Tên đăng nhập *"));
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtUsername);
        pRight.add(Box.createVerticalStrut(8));

        // Password field
        pRight.add(createLabel("Mật khẩu *"));
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtPassword);
        pRight.add(Box.createVerticalStrut(8));

        // Confirm Password field
        pRight.add(createLabel("Xác nhận mật khẩu *"));
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtConfirmPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtConfirmPassword);
        pRight.add(Box.createVerticalStrut(8));

        // Display Name field
        pRight.add(createLabel("Tên hiển thị"));
        txtDisplayName = new JTextField();
        txtDisplayName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtDisplayName.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtDisplayName);
        pRight.add(Box.createVerticalStrut(8));

        // Role field
        pRight.add(createLabel("Vai trò *"));
        cboRole = new JComboBox<>(new String[] { "NhanVien", "Admin" });
        cboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(cboRole);
        pRight.add(Box.createVerticalStrut(8));

        // Password hint for edit mode
        JPanel pHint = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pHint.setBackground(new Color(245, 245, 245));
        pHint.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblHint = new JLabel("<html><i>* Để trống mật khẩu nếu không muốn đổi</i></html>");
        lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblHint.setForeground(new Color(100, 100, 100));
        pHint.add(lblHint);
        pRight.add(pHint);
        pRight.add(Box.createVerticalStrut(15));

        // Action buttons
        JPanel pButtons = new JPanel(new GridLayout(2, 2, 5, 5));
        pButtons.setBackground(new Color(245, 245, 245));
        pButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        pButtons.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add button
        JButton btnAdd = new JButton("Thêm");
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> add());
        pButtons.add(btnAdd);

        // Update button
        JButton btnUpdate = new JButton("Sửa");
        btnUpdate.setBackground(new Color(33, 150, 243));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> update());
        pButtons.add(btnUpdate);

        // Delete button
        JButton btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> delete());
        pButtons.add(btnDelete);

        // Clear form button
        JButton btnClear = new JButton("Xóa form");
        btnClear.addActionListener(e -> clearForm());
        pButtons.add(btnClear);

        pRight.add(pButtons);

        add(pRight, BorderLayout.EAST);
    }

    // Helper method to create form labels
    private JPanel createLabel(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(label);
        return panel;
    }

    // Load all accounts from database to table
    private void loadData() {
        tableModel.setRowCount(0);
        List<Account> accounts = accountService.getAll();
        for (Account acc : accounts) {
            tableModel.addRow(new Object[] {
                    acc.getUsername(),
                    acc.getPassword(),
                    acc.getDisplayName(),
                    formatRole(acc.getRole())
            });
        }
    }

    // Format role for display
    private String formatRole(String role) {
        if ("Admin".equals(role)) {
            return "Quản trị viên";
        } else if ("NhanVien".equals(role)) {
            return "Nhân viên";
        }
        return role;
    }

    // Handle table row selection - populate form with selected data
    private void selectRow() {
        int row = tblAccounts.getSelectedRow();
        if (row >= 0) {
            selectedUsername = (String) tableModel.getValueAt(row, 0);

            // Get full account data from service
            Account acc = accountService.getByUsername(selectedUsername);
            if (acc != null) {
                txtUsername.setText(acc.getUsername());
                txtUsername.setEditable(false); // Disable username editing
                txtPassword.setText(acc.getPassword()); // Show current password
                txtConfirmPassword.setText(acc.getPassword());
                txtDisplayName.setText(acc.getDisplayName());
                cboRole.setSelectedItem(acc.getRole());
            }
        }
    }

    // Clear all form fields and reset selection
    private void clearForm() {
        selectedUsername = null;
        txtUsername.setText("");
        txtUsername.setEditable(true); // Enable username editing for new account
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtDisplayName.setText("");
        cboRole.setSelectedIndex(0);
        tblAccounts.clearSelection();
    }

    // Get Account object from form fields with validation
    private Account getFormData(boolean requirePassword) throws Exception {
        Account acc = new Account();
        acc.setUsername(txtUsername.getText().trim());

        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        // Password validation
        if (requirePassword || !password.isEmpty()) {
            if (password.isEmpty()) {
                throw new Exception("Mật khẩu không được để trống!");
            }
            if (!password.equals(confirmPassword)) {
                throw new Exception("Mật khẩu xác nhận không khớp!");
            }
            acc.setPassword(password);
        }

        acc.setDisplayName(txtDisplayName.getText().trim());
        acc.setRole((String) cboRole.getSelectedItem());

        return acc;
    }

    // Add new account to database
    private void add() {
        try {
            Account acc = getFormData(true); // Password required for new account
            accountService.add(acc);
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update existing account in database
    private void update() {
        if (selectedUsername == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String password = new String(txtPassword.getPassword());
            boolean changePassword = !password.isEmpty();

            Account acc = getFormData(false); // Password not required for update
            acc.setUsername(selectedUsername); // Ensure username is set correctly

            // If not changing password, get existing password
            if (!changePassword) {
                Account existing = accountService.getByUsername(selectedUsername);
                if (existing != null) {
                    acc.setPassword(existing.getPassword());
                }
            }

            accountService.update(acc, changePassword);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected account from database
    private void delete() {
        if (selectedUsername == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa tài khoản '" + selectedUsername + "'?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                accountService.delete(selectedUsername);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Search accounts by keyword
    private void search() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Account> accounts = accountService.search(keyword);
        for (Account acc : accounts) {
            tableModel.addRow(new Object[] {
                    acc.getUsername(),
                    acc.getPassword(),
                    acc.getDisplayName(),
                    formatRole(acc.getRole())
            });
        }
    }
}
