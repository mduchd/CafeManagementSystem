package com.cafe.view.product;

import com.cafe.service.ProductService;
import com.cafe.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Product Management Panel - CRUD operations for products
 * Style đồng bộ với TablePanel và EmployeePanel
 */
public class ProductPanel extends JPanel {

    // Service for database operations
    private ProductService productService = new ProductService();

    // Table components
    private JTable tblProducts;
    private DefaultTableModel tableModel;

    // Form input fields
    private JTextField txtName;
    private JComboBox<String> cboCategory;
    private JTextField txtPrice;
    private JComboBox<String> cboStatus;
    private JTextField txtSearch;
    private JLabel lblImage;
    private String selectedImagePath = "";

    // Currently selected product ID (-1 = none)
    private int selectedId = -1;

    // Constructor - initialize UI and load data
    public ProductPanel() {
        initComponents();
        loadData();
    }

    // Initialize all UI components
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Quản lý Sản phẩm");
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

        // Category filter
        pSearch.add(new JLabel("Loại:"));
        JComboBox<String> cboFilterCategory = new JComboBox<>(
                new String[] { "Tất cả", "Cà phê", "Trà", "Nước ngọt", "Bánh" });
        cboFilterCategory.addActionListener(e -> filterByCategory((String) cboFilterCategory.getSelectedItem()));
        pSearch.add(cboFilterCategory);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cboFilterCategory.setSelectedIndex(0);
            loadData();
        });
        pSearch.add(btnRefresh);
        pCenter.add(pSearch, BorderLayout.NORTH);

        // Data table setup
        String[] columns = { "Mã SP", "Tên sản phẩm", "Loại", "Giá bán", "Trạng thái" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        tblProducts = new JTable(tableModel);
        tblProducts.setRowHeight(28);
        tblProducts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Row selection listener
        tblProducts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblProducts);
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
        JLabel lblForm = new JLabel("Thông tin sản phẩm");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pTitle.add(lblForm);
        pRight.add(pTitle);
        pRight.add(Box.createVerticalStrut(15));

        // Product Name field
        pRight.add(createLabel("Tên sản phẩm *"));
        txtName = new JTextField();
        txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtName.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtName);
        pRight.add(Box.createVerticalStrut(10));

        // Category field
        pRight.add(createLabel("Loại sản phẩm"));
        cboCategory = new JComboBox<>(new String[] { "Cà phê", "Trà", "Nước ngọt", "Bánh" });
        cboCategory.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboCategory.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(cboCategory);
        pRight.add(Box.createVerticalStrut(10));

        // Price field
        pRight.add(createLabel("Giá bán (VNĐ)"));
        txtPrice = new JTextField();
        txtPrice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtPrice);
        pRight.add(Box.createVerticalStrut(10));

        // Status field
        pRight.add(createLabel("Trạng thái"));
        cboStatus = new JComboBox<>(new String[] { "Đang bán", "Ngừng bán" });
        cboStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(cboStatus);
        pRight.add(Box.createVerticalStrut(10));

        // Image preview
        pRight.add(createLabel("Hình ảnh"));
        JPanel pImage = new JPanel(new BorderLayout(5, 5));
        pImage.setBackground(new Color(245, 245, 245));
        pImage.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        pImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblImage = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(80, 80));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblImage.setOpaque(true);
        lblImage.setBackground(Color.WHITE);
        pImage.add(lblImage, BorderLayout.CENTER);

        JButton btnChooseImage = new JButton("Chọn ảnh");
        btnChooseImage.addActionListener(e -> chooseImage());
        pImage.add(btnChooseImage, BorderLayout.SOUTH);

        pRight.add(pImage);
        pRight.add(Box.createVerticalStrut(20));

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

    // Load all products from database to table
    private void loadData() {
        tableModel.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[] {
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    formatPrice(p.getPrice()),
                    p.getStatus()
            });
        }
    }

    // Format price with Vietnamese currency
    private String formatPrice(double price) {
        return String.format("%,.0fđ", price);
    }

    // Handle table row selection - populate form with selected data
    private void selectRow() {
        int row = tblProducts.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            cboCategory.setSelectedItem(tableModel.getValueAt(row, 2));

            // Parse price from formatted string
            String priceStr = (String) tableModel.getValueAt(row, 3);
            priceStr = priceStr.replace("đ", "").replace(",", "").replace(".", "");
            txtPrice.setText(priceStr);

            cboStatus.setSelectedItem(tableModel.getValueAt(row, 4));

            // Load image if available
            List<Product> products = productService.getAllProducts();
            for (Product p : products) {
                if (p.getId() == selectedId && p.getImage() != null && !p.getImage().isEmpty()) {
                    selectedImagePath = p.getImage();
                    displayImage(selectedImagePath);
                    break;
                }
            }
        }
    }

    // Display image in preview label
    private void displayImage(String imagePath) {
        try {
            java.io.File file = new java.io.File("src/icon/" + imagePath);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
                lblImage.setText("");
            } else {
                lblImage.setIcon(null);
                lblImage.setText("Không tìm thấy");
            }
        } catch (Exception e) {
            lblImage.setIcon(null);
            lblImage.setText("Lỗi load ảnh");
        }
    }

    // Choose image from file
    private void chooseImage() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fc.getSelectedFile();
            selectedImagePath = file.getName();

            // Copy to icon folder
            try {
                java.io.File destDir = new java.io.File("src/icon");
                if (!destDir.exists())
                    destDir.mkdirs();
                java.io.File dest = new java.io.File(destDir, file.getName());
                java.nio.file.Files.copy(file.toPath(), dest.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                displayImage(selectedImagePath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thể copy ảnh: " + e.getMessage());
            }
        }
    }

    // Clear all form fields and reset selection
    private void clearForm() {
        selectedId = -1;
        txtName.setText("");
        cboCategory.setSelectedIndex(0);
        txtPrice.setText("");
        cboStatus.setSelectedIndex(0);
        selectedImagePath = "";
        lblImage.setIcon(null);
        lblImage.setText("Chưa có ảnh");
        tblProducts.clearSelection();
    }

    // Get Product object from form fields with validation
    private Product getFormData() throws Exception {
        Product p = new Product();
        p.setId(selectedId);

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            throw new Exception("Vui lòng nhập tên sản phẩm!");
        }
        p.setName(name);

        p.setCategory((String) cboCategory.getSelectedItem());

        String priceStr = txtPrice.getText().trim().replace(",", "");
        if (priceStr.isEmpty()) {
            throw new Exception("Vui lòng nhập giá!");
        }
        try {
            p.setPrice(Double.parseDouble(priceStr));
        } catch (NumberFormatException e) {
            throw new Exception("Giá không hợp lệ!");
        }

        p.setStatus((String) cboStatus.getSelectedItem());
        p.setImage(selectedImagePath);

        return p;
    }

    // Add new product to database
    private void add() {
        try {
            Product p = getFormData();
            if (productService.insertProduct(p)) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update existing product in database
    private void update() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Product p = getFormData();
            if (productService.updateProduct(p)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected product from database
    private void delete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (productService.deleteProduct(selectedId)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Search products by keyword
    private void search() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword) ||
                    String.valueOf(p.getId()).contains(keyword)) {
                tableModel.addRow(new Object[] {
                        p.getId(),
                        p.getName(),
                        p.getCategory(),
                        formatPrice(p.getPrice()),
                        p.getStatus()
                });
            }
        }
    }

    // Filter products by category
    private void filterByCategory(String category) {
        if ("Tất cả".equals(category)) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        List<Product> products = productService.getProductsByCategory(category);
        for (Product p : products) {
            tableModel.addRow(new Object[] {
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    formatPrice(p.getPrice()),
                    p.getStatus()
            });
        }
    }
}
