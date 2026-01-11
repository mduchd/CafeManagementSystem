package com.cafe.view.warehouse;

import com.cafe.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Import Management Panel - CRUD operations for importing ingredients
 * Style đồng bộ với TablePanel và EmployeePanel
 */
public class ImportPanel extends JPanel {

    // Table components
    private JTable tblImports;
    private DefaultTableModel tableModel;

    // Form input fields
    private JComboBox<String> cboIngredient;
    private JTextField txtQuantity;
    private JTextField txtPrice;
    private JComboBox<String> cboSupplier;
    private JLabel lblStock;

    // Currently selected import ID (-1 = none)
    private int selectedId = -1;
    private int selectedIngredientId = -1;
    private int selectedQuantity = 0;

    // Constructor
    public ImportPanel() {
        initComponents();
        loadSuppliers();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Nhập kho nguyên liệu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table area
        JPanel pCenter = new JPanel(new BorderLayout(10, 10));
        pCenter.setBackground(Color.WHITE);

        // Data table setup
        String[] columns = { "Mã nhập", "Nguyên liệu", "Số lượng", "Đơn giá", "Nhà cung cấp" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblImports = new JTable(tableModel);
        tblImports.setRowHeight(28);
        tblImports.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblImports.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblImports.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblImports.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblImports);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        add(pCenter, BorderLayout.CENTER);

        // Right - Form panel
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBackground(new Color(245, 245, 245));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pRight.setPreferredSize(new Dimension(280, 0));

        // Form title
        JPanel pTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pTitle.setBackground(new Color(245, 245, 245));
        pTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblForm = new JLabel("Thông tin nhập kho");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pTitle.add(lblForm);
        pRight.add(pTitle);
        pRight.add(Box.createVerticalStrut(15));

        // Ingredient field
        pRight.add(createLabel("Nguyên liệu *"));
        cboIngredient = new JComboBox<>(new String[] {
                "Cà phê bột", "Cà phê hạt", "Đường", "Bột Béo",
                "Sữa tươi", "Sữa đặc", "Kem béo(Rich)", "Siro", "Mứt/sốt trái cây"
        });
        cboIngredient.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboIngredient.setAlignmentX(Component.CENTER_ALIGNMENT);
        cboIngredient.addActionListener(e -> updateStock());
        pRight.add(cboIngredient);
        pRight.add(Box.createVerticalStrut(10));

        // Stock display
        lblStock = new JLabel("Tồn kho: 0");
        lblStock.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStock.setForeground(new Color(33, 150, 243));
        lblStock.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(lblStock);
        pRight.add(Box.createVerticalStrut(10));

        // Quantity field
        pRight.add(createLabel("Số lượng nhập *"));
        txtQuantity = new JTextField();
        txtQuantity.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtQuantity);
        pRight.add(Box.createVerticalStrut(10));

        // Price field
        pRight.add(createLabel("Đơn giá (VNĐ)"));
        txtPrice = new JTextField();
        txtPrice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtPrice);
        pRight.add(Box.createVerticalStrut(10));

        // Supplier field
        pRight.add(createLabel("Nhà cung cấp"));
        cboSupplier = new JComboBox<>();
        cboSupplier.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboSupplier.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(cboSupplier);
        pRight.add(Box.createVerticalStrut(20));

        // Action buttons
        JPanel pButtons = new JPanel(new GridLayout(2, 2, 5, 5));
        pButtons.setBackground(new Color(245, 245, 245));
        pButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        pButtons.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAdd = new JButton("Nhập kho");
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> add());
        pButtons.add(btnAdd);

        JButton btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> delete());
        pButtons.add(btnDelete);

        JButton btnClear = new JButton("Xóa form");
        btnClear.addActionListener(e -> clearForm());
        pButtons.add(btnClear);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        pButtons.add(btnRefresh);

        pRight.add(pButtons);
        add(pRight, BorderLayout.EAST);
    }

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

    private void loadSuppliers() {
        cboSupplier.removeAllItems();
        String sql = "SELECT supplier_id, supplier_name FROM tbl_supplier";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                cboSupplier.addItem(rs.getInt("supplier_id") + " - " + rs.getString("supplier_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        String sql = """
                    SELECT i.import_id, i.ingredient_name, i.quantity, i.price, s.supplier_name
                    FROM tbl_import i
                    LEFT JOIN tbl_supplier s ON i.supplier_id = s.supplier_id
                    ORDER BY i.import_id DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("import_id"),
                        rs.getString("ingredient_name"),
                        rs.getInt("quantity"),
                        String.format("%,.0fđ", rs.getDouble("price")),
                        rs.getString("supplier_name")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateStock();
    }

    private void updateStock() {
        String ingredient = (String) cboIngredient.getSelectedItem();
        if (ingredient == null)
            return;

        String sql = "SELECT IFNULL(SUM(quantity), 0) AS stock FROM tbl_import WHERE ingredient_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ingredient);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblStock.setText("Tồn kho: " + rs.getInt("stock"));
            }
        } catch (Exception e) {
            lblStock.setText("Tồn kho: 0");
        }
    }

    private void selectRow() {
        int row = tblImports.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            String ingredient = (String) tableModel.getValueAt(row, 1);
            cboIngredient.setSelectedItem(ingredient);
            txtQuantity.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            String priceStr = (String) tableModel.getValueAt(row, 3);
            txtPrice.setText(priceStr.replace("đ", "").replace(",", "").replace(".", ""));
        }
    }

    private void clearForm() {
        selectedId = -1;
        cboIngredient.setSelectedIndex(0);
        txtQuantity.setText("");
        txtPrice.setText("");
        if (cboSupplier.getItemCount() > 0)
            cboSupplier.setSelectedIndex(0);
        tblImports.clearSelection();
    }

    private void add() {
        try {
            String ingredient = (String) cboIngredient.getSelectedItem();
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            double price = Double.parseDouble(txtPrice.getText().trim().replace(",", ""));

            if (cboSupplier.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Chưa có nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String supplierText = (String) cboSupplier.getSelectedItem();
            int supplierId = Integer.parseInt(supplierText.split(" - ")[0]);

            String sql = "INSERT INTO tbl_import (ingredient_name, quantity, price, supplier_id) VALUES (?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, ingredient);
                ps.setInt(2, quantity);
                ps.setDouble(3, price);
                ps.setInt(4, supplierId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Nhập kho thành công!");
                loadData();
                clearForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng hoặc đơn giá không hợp lệ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void delete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa lần nhập này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_import WHERE import_id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, selectedId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
