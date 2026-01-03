package com.cafe.view.table;

import com.cafe.model.CafeTable;
import com.cafe.service.CafeTableService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Table Management Panel - CRUD operations for cafe tables
public class TablePanel extends JPanel {
    // Service for database operations
    private CafeTableService tableService = new CafeTableService();

    // Table components
    private JTable tblTables;
    private DefaultTableModel tableModel;

    // Form input fields
    private JTextField txtName;
    private JComboBox<String> cboStatus;
    private JSpinner spnCapacity;
    private JTextField txtLocation;
    private JTextField txtSearch;

    // Currently selected table ID (-1 = none)
    private int selectedId = -1;

    // Constructor - initialize UI and load data
    public TablePanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Quản lý Bàn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table
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

        // Table
        String[] columns = { "Mã bàn", "Tên bàn", "Trạng thái", "Số chỗ", "Vị trí" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTables = new JTable(tableModel);
        tblTables.setRowHeight(28);
        tblTables.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblTables.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblTables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTables.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblTables);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        add(pCenter, BorderLayout.CENTER);

        // Right - Form
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBackground(new Color(245, 245, 245));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pRight.setPreferredSize(new Dimension(280, 0));

        // Form title - căn giữa bằng panel riêng
        JPanel pTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pTitle.setBackground(new Color(245, 245, 245));
        pTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblForm = new JLabel("Thông tin bàn");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pTitle.add(lblForm);
        pRight.add(pTitle);
        pRight.add(Box.createVerticalStrut(15));

        // Name
        pRight.add(createLabel("Tên bàn *"));
        txtName = new JTextField();
        txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtName.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtName);
        pRight.add(Box.createVerticalStrut(10));

        // Status
        pRight.add(createLabel("Trạng thái"));
        cboStatus = new JComboBox<>(new String[] { "Trong", "DangSuDung", "DaDat" });
        cboStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(cboStatus);
        pRight.add(Box.createVerticalStrut(10));

        // Capacity
        pRight.add(createLabel("Số chỗ ngồi"));
        spnCapacity = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        spnCapacity.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        spnCapacity.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(spnCapacity);
        pRight.add(Box.createVerticalStrut(10));

        // Location
        pRight.add(createLabel("Vị trí"));
        txtLocation = new JTextField();
        txtLocation.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtLocation.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtLocation);
        pRight.add(Box.createVerticalStrut(20));

        // Buttons
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
        pRight.add(Box.createVerticalStrut(15));

        // Status change buttons
        JLabel lblQuickStatus = new JLabel("Đổi trạng thái nhanh:");
        lblQuickStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblQuickStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(lblQuickStatus);
        pRight.add(Box.createVerticalStrut(5));

        JPanel pStatus = new JPanel(new GridLayout(1, 3, 5, 0));
        pStatus.setBackground(new Color(245, 245, 245));
        pStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAvailable = new JButton("Trống");
        btnAvailable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnAvailable.addActionListener(e -> changeStatus("Trong"));
        pStatus.add(btnAvailable);

        JButton btnInUse = new JButton("Sử dụng");
        btnInUse.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnInUse.addActionListener(e -> changeStatus("DangSuDung"));
        pStatus.add(btnInUse);

        JButton btnReserved = new JButton("Đã đặt");
        btnReserved.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnReserved.addActionListener(e -> changeStatus("DaDat"));
        pStatus.add(btnReserved);

        pRight.add(pStatus);

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

    // Load all tables from database to table
    private void loadData() {
        tableModel.setRowCount(0);
        List<CafeTable> tables = tableService.getAll();
        for (CafeTable t : tables) {
            tableModel.addRow(new Object[] {
                    t.getId(),
                    t.getName(),
                    formatStatus(t.getStatus()),
                    t.getCapacity(),
                    t.getLocation()
            });
        }
    }

    // Convert status code to display text
    private String formatStatus(String status) {
        switch (status) {
            case "Trong":
                return "Trống";
            case "DangSuDung":
                return "Đang sử dụng";
            case "DaDat":
                return "Đã đặt";
            default:
                return status;
        }
    }

    // Handle table row selection - populate form with selected data
    private void selectRow() {
        int row = tblTables.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtName.setText((String) tableModel.getValueAt(row, 1));

            String statusDisplay = (String) tableModel.getValueAt(row, 2);
            if ("Trống".equals(statusDisplay))
                cboStatus.setSelectedItem("Trong");
            else if ("Đang sử dụng".equals(statusDisplay))
                cboStatus.setSelectedItem("DangSuDung");
            else if ("Đã đặt".equals(statusDisplay))
                cboStatus.setSelectedItem("DaDat");

            spnCapacity.setValue(tableModel.getValueAt(row, 3));
            txtLocation.setText((String) tableModel.getValueAt(row, 4));
        }
    }

    // Clear all form fields and reset selection
    private void clearForm() {
        selectedId = -1;
        txtName.setText("");
        cboStatus.setSelectedIndex(0);
        spnCapacity.setValue(4);
        txtLocation.setText("");
        tblTables.clearSelection();
    }

    // Get CafeTable object from form fields
    private CafeTable getFormData() {
        CafeTable table = new CafeTable();
        table.setId(selectedId);
        table.setName(txtName.getText().trim());
        table.setStatus((String) cboStatus.getSelectedItem());
        table.setCapacity((int) spnCapacity.getValue());
        table.setLocation(txtLocation.getText().trim());
        return table;
    }

    // Add new table to database
    private void add() {
        try {
            CafeTable table = getFormData();
            tableService.add(table);
            JOptionPane.showMessageDialog(this, "Thêm bàn thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update existing table in database
    private void update() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            CafeTable table = getFormData();
            tableService.update(table);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected table from database
    private void delete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bàn này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                tableService.delete(selectedId);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Search tables by keyword
    private void search() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<CafeTable> tables = tableService.search(keyword);
        for (CafeTable t : tables) {
            tableModel.addRow(new Object[] {
                    t.getId(),
                    t.getName(),
                    formatStatus(t.getStatus()),
                    t.getCapacity(),
                    t.getLocation()
            });
        }
    }

    // Quick change table status
    private void changeStatus(String status) {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            tableService.changeStatus(selectedId, status);
            JOptionPane.showMessageDialog(this, "Đổi trạng thái thành công!");
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
