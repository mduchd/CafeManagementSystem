package com.cafe.view.warehouse;

import com.cafe.database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Supplier Management Panel - CRUD operations for suppliers
 * Style đồng bộ với TablePanel và EmployeePanel
 */
public class SupplierPanel extends JPanel {

    // Table components
    private JTable tblSuppliers;
    private DefaultTableModel tableModel;

    // Form input fields
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtPhone;
    private JTextField txtSearch;

    // Currently selected supplier ID (-1 = none)
    private int selectedId = -1;

    // Constructor - initialize UI and load data
    public SupplierPanel() {
        initComponents();
        loadData();
    }

    // Initialize all UI components
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Quản lý Nhà cung cấp");
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
        String[] columns = { "Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "Số điện thoại" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSuppliers = new JTable(tableModel);
        tblSuppliers.setRowHeight(28);
        tblSuppliers.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblSuppliers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Row selection listener
        tblSuppliers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblSuppliers);
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
        JLabel lblForm = new JLabel("Thông tin nhà cung cấp");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pTitle.add(lblForm);
        pRight.add(pTitle);
        pRight.add(Box.createVerticalStrut(15));

        // Name field
        pRight.add(createLabel("Tên nhà cung cấp *"));
        txtName = new JTextField();
        txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtName.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtName);
        pRight.add(Box.createVerticalStrut(10));

        // Address field
        pRight.add(createLabel("Địa chỉ"));
        txtAddress = new JTextField();
        txtAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtAddress);
        pRight.add(Box.createVerticalStrut(10));

        // Phone field
        pRight.add(createLabel("Số điện thoại"));
        txtPhone = new JTextField();
        txtPhone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPhone.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtPhone);
        pRight.add(Box.createVerticalStrut(20));

        // Action buttons
        JPanel pButtons = new JPanel(new GridLayout(2, 2, 5, 5));
        pButtons.setBackground(new Color(245, 245, 245));
        pButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        pButtons.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAdd = new JButton("Thêm");
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> add());
        pButtons.add(btnAdd);

        JButton btnUpdate = new JButton("Sửa");
        btnUpdate.setBackground(new Color(33, 150, 243));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> update());
        pButtons.add(btnUpdate);

        JButton btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> delete());
        pButtons.add(btnDelete);

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

    // Load all suppliers from database
    private void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM tbl_supplier ORDER BY supplier_id";

        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("address"),
                        rs.getString("phone")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle table row selection
    private void selectRow() {
        int row = tblSuppliers.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtAddress.setText((String) tableModel.getValueAt(row, 2));
            txtPhone.setText((String) tableModel.getValueAt(row, 3));
        }
    }

    // Clear all form fields
    private void clearForm() {
        selectedId = -1;
        txtName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        tblSuppliers.clearSelection();
    }

    // Add new supplier
    private void add() {
        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO tbl_supplier (supplier_name, address, phone) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, phone);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update existing supplier
    private void update() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE tbl_supplier SET supplier_name = ?, address = ?, phone = ? WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, phone);
            ps.setInt(4, selectedId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected supplier
    private void delete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_supplier WHERE supplier_id = ?";

            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, selectedId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thể xóa! NCC đang được sử dụng.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Search suppliers
    private void search() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        String sql = "SELECT * FROM tbl_supplier WHERE LOWER(supplier_name) LIKE ? OR phone LIKE ? ORDER BY supplier_id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("address"),
                        rs.getString("phone")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
