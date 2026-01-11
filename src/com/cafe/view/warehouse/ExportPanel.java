package com.cafe.view.warehouse;

import com.cafe.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Export Management Panel - CRUD operations for exporting ingredients
 * Style đồng bộ với TablePanel và EmployeePanel
 */
public class ExportPanel extends JPanel {

    // Table components
    private JTable tblExports;
    private DefaultTableModel tableModel;

    // Form input fields
    private JComboBox<String> cboIngredient;
    private JTextField txtQuantity;
    private JLabel lblStock;

    // Currently selected export ID
    private int selectedId = -1;

    // Constructor
    public ExportPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Xuất kho nguyên liệu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table area
        JPanel pCenter = new JPanel(new BorderLayout(10, 10));
        pCenter.setBackground(Color.WHITE);

        // Data table setup
        String[] columns = { "Mã xuất", "Nguyên liệu", "Số lượng", "Ngày xuất" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblExports = new JTable(tableModel);
        tblExports.setRowHeight(28);
        tblExports.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblExports.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblExports.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblExports.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblExports);
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
        JLabel lblForm = new JLabel("Xuất kho");
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
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStock.setForeground(new Color(76, 175, 80));
        lblStock.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(lblStock);
        pRight.add(Box.createVerticalStrut(15));

        // Quantity field
        pRight.add(createLabel("Số lượng xuất *"));
        txtQuantity = new JTextField();
        txtQuantity.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtQuantity);
        pRight.add(Box.createVerticalStrut(20));

        // Action buttons
        JPanel pButtons = new JPanel(new GridLayout(2, 2, 5, 5));
        pButtons.setBackground(new Color(245, 245, 245));
        pButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        pButtons.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAdd = new JButton("Xuất kho");
        btnAdd.setBackground(new Color(255, 152, 0)); // Orange
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

    private void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT export_id, ingredient_name, quantity, export_date FROM tbl_export ORDER BY export_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("export_id"),
                        rs.getString("ingredient_name"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("export_date") != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                                .format(rs.getTimestamp("export_date")) : ""
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

        // Tồn kho = Tổng nhập - Tổng xuất
        String sql = """
                    SELECT
                        (SELECT IFNULL(SUM(quantity), 0) FROM tbl_import WHERE ingredient_name = ?) -
                        (SELECT IFNULL(SUM(quantity), 0) FROM tbl_export WHERE ingredient_name = ?) AS stock
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ingredient);
            ps.setString(2, ingredient);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                lblStock.setText("Tồn kho: " + stock);
                lblStock.setForeground(stock > 0 ? new Color(76, 175, 80) : new Color(244, 67, 54));
            }
        } catch (Exception e) {
            lblStock.setText("Tồn kho: 0");
        }
    }

    private void selectRow() {
        int row = tblExports.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            String ingredient = (String) tableModel.getValueAt(row, 1);
            cboIngredient.setSelectedItem(ingredient);
            txtQuantity.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        }
    }

    private void clearForm() {
        selectedId = -1;
        cboIngredient.setSelectedIndex(0);
        txtQuantity.setText("");
        tblExports.clearSelection();
    }

    private void add() {
        try {
            String ingredient = (String) cboIngredient.getSelectedItem();
            int quantity = Integer.parseInt(txtQuantity.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải > 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check stock
            int currentStock = getCurrentStock(ingredient);
            if (quantity > currentStock) {
                JOptionPane.showMessageDialog(this, "Không đủ tồn kho! Hiện có: " + currentStock, "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO tbl_export (ingredient_name, quantity, export_date) VALUES (?, ?, NOW())";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, ingredient);
                ps.setInt(2, quantity);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Xuất kho thành công!");
                loadData();
                clearForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getCurrentStock(String ingredient) {
        String sql = """
                    SELECT
                        (SELECT IFNULL(SUM(quantity), 0) FROM tbl_import WHERE ingredient_name = ?) -
                        (SELECT IFNULL(SUM(quantity), 0) FROM tbl_export WHERE ingredient_name = ?) AS stock
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ingredient);
            ps.setString(2, ingredient);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void delete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa lần xuất này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_export WHERE export_id = ?";

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
