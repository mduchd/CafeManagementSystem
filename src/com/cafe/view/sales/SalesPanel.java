
package com.cafe.view.sales;

// === ALL IMPORTS (merged from both branches) ===
import com.cafe.model.CafeTable;
import com.cafe.model.Product;
import com.cafe.model.Order;
import com.cafe.model.OrderDetail;
import com.cafe.service.CafeTableService;
import com.cafe.service.OrderService;
import com.cafe.service.ProductService;
import com.cafe.service.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;

// Sales Panel - Point of Sale (POS) interface for cafe orders
public class SalesPanel extends javax.swing.JPanel {

    // === COLOR CONSTANTS (unified naming) ===
    private static final Color COLOR_AVAILABLE = new Color(46, 204, 113);  // Green: available/empty
    private static final Color COLOR_IN_USE = new Color(231, 76, 60);      // Red: in use/busy
    private static final Color COLOR_RESERVED = new Color(241, 196, 15);   // Yellow: reserved
    private static final Color COLOR_SELECTED = new Color(52, 152, 219);   // Blue: selected

    // === SERVICES (merged) ===
    private final CafeTableService tableService = new CafeTableService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    // === TABLE MANAGEMENT (from sonvu branch) ===
    private List<CafeTable> tableList = new ArrayList<>();
    private CafeTable selectedTable = null;
    private List<JButton> tableButtons = new ArrayList<>();

    // === MENU PANEL (from HEAD) ===
    private JPanel pMenuItems;

    // Constructor
    public SalesPanel() {
        initComponents();
        initLogic();
    }

    private void initLogic() {
        // 1) Load tables from database and setup dynamic table grid
        loadTablesFromDatabase();

        // 1.1) Setup refresh button for table diagram
        setupRefreshButton();

        // 2) Customize filter buttons
        btnAll.setText("Tất cả");
        btnCoffee.setText("Cà phê");
        btnTea.setText("Trà");
        btnJuice.setText("Nước");
        btnCake.setText("Bánh");

        // 2b) Setup filter button events
        btnAll.addActionListener(e -> filterMenuByCategory(null));        // Tất cả
        btnCoffee.addActionListener(e -> filterMenuByCategory("Cà phê")); // Cà phê
        btnTea.addActionListener(e -> filterMenuByCategory("Trà"));       // Trà
        btnJuice.addActionListener(e -> filterMenuByCategory("Nước ngọt")); // Nước
        btnCake.addActionListener(e -> filterMenuByCategory("Bánh"));     // Bánh

        // 3) Customize bill header labels
        jLabel1.setText("Chưa chọn bàn");
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

        jLabel2.setText("Dùng tại bàn");
        jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        // 4) Setup bill table model
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Món", "SL", "Đơn giá", "Thành tiền"}
        ));

        // 5) Customize summary labels
        lblSubtotalLabel.setText("Tạm tính:");
        lblSubtotalLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        lblSubtotalValue.setText("0đ");
        lblSubtotalValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblSubtotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblDiscountLabel.setText("Giảm giá (%):");
        lblDiscountLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        lblTotalLabel.setText("Tổng cộng:");
        lblTotalLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

        lblTotalValue.setText("0đ");
        lblTotalValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTotalValue.setForeground(new java.awt.Color(52, 152, 219));
        lblTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        // 6) Customize action buttons
        btnCancel.setText("HỦY");
        btnCancel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnCancel.setBackground(new java.awt.Color(231, 76, 60));
        btnCancel.setForeground(java.awt.Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setPreferredSize(new java.awt.Dimension(120, 40));

        btnCheckout.setText("THANH TOÁN");
        btnCheckout.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnCheckout.setBackground(new java.awt.Color(46, 204, 113));
        btnCheckout.setForeground(java.awt.Color.WHITE);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setPreferredSize(new java.awt.Dimension(150, 40));

        // 7) Customize discount field
        txtDiscountPercent.setText("0");
        txtDiscountPercent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountPercent.setPreferredSize(new java.awt.Dimension(80, 25));

        // 8) Setup event handlers
        txtDiscountPercent.addActionListener(e -> updateTotalAmount());
        txtDiscountPercent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                updateTotalAmount();
            }
        });

        btnCancel.addActionListener(e -> clearBill());
        btnCheckout.addActionListener(e -> handleCheckout());

        // 9) Setup Legend Panel
        JLabel lblEmpty = new JLabel(String.format(
            "<html><span style='font-size:16px; color:rgb(%d,%d,%d);'>●</span> Trống</html>",
            COLOR_AVAILABLE.getRed(), COLOR_AVAILABLE.getGreen(), COLOR_AVAILABLE.getBlue()
        ));
        lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pLegend.add(lblEmpty);

        JLabel lblBusy = new JLabel(String.format(
            "<html><span style='font-size:16px; color:rgb(%d,%d,%d);'>●</span> Có khách</html>",
            COLOR_IN_USE.getRed(), COLOR_IN_USE.getGreen(), COLOR_IN_USE.getBlue()
        ));
        lblBusy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pLegend.add(lblBusy);

        JLabel lblSelected = new JLabel(String.format(
            "<html><span style='font-size:16px; color:rgb(%d,%d,%d);'>●</span> Đang chọn</html>",
            COLOR_SELECTED.getRed(), COLOR_SELECTED.getGreen(), COLOR_SELECTED.getBlue()
        ));
        lblSelected.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pLegend.add(lblSelected);

        JLabel lblReserved = new JLabel(String.format(
            "<html><span style='font-size:16px; color:rgb(%d,%d,%d);'>●</span> Đã đặt</html>",
            COLOR_RESERVED.getRed(), COLOR_RESERVED.getGreen(), COLOR_RESERVED.getBlue()
        ));
        lblReserved.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pLegend.add(lblReserved);

        // 10) Setup menu items panel
        pMenuItems = new JPanel();
        pMenuItems.setLayout(new GridLayout(0, 3, 10, 10));
        pMenuItems.setBackground(Color.WHITE);

        for (java.awt.Component comp : pMenuArea.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                scrollPane.setViewportView(pMenuItems);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                break;
            }
        }

        // Load initial menu from database
        refreshMenu();
    }

    // ==================== TABLE MANAGEMENT (from sonvu) ====================

    private void loadTablesFromDatabase() {
        pTablesGrid.removeAll();
        tableButtons.clear();

        tableList = tableService.getAll();

        int cols = 2;
        int rows = Math.max(1, (int) Math.ceil(tableList.size() / (double) cols));
        pTablesGrid.setLayout(new java.awt.GridLayout(rows, cols, 16, 16));

        for (CafeTable table : tableList) {
            JButton btn = createTableButton(table);
            tableButtons.add(btn);
            pTablesGrid.add(btn);
        }

        if (tableList.isEmpty()) {
            JLabel lblNoTables = new JLabel("Chưa có bàn nào", SwingConstants.CENTER);
            lblNoTables.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 14));
            pTablesGrid.add(lblNoTables);
        } else {
            selectTable(tableList.get(0));
        }

        int buttonHeight = 70;
        int totalHeight = rows * buttonHeight + (rows - 1) * 16 + 20;
        pTablesGrid.setPreferredSize(new java.awt.Dimension(250, totalHeight));

        pTablesGrid.revalidate();
        pTablesGrid.repaint();
    }

    private JButton createTableButton(CafeTable table) {
        JButton btn = new JButton(table.getName());
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btn.setForeground(java.awt.Color.WHITE);
        btn.setPreferredSize(new java.awt.Dimension(120, 60));

        updateTableButtonColor(btn, table);
        btn.addActionListener(e -> selectTable(table));

        return btn;
    }

    private void updateTableButtonColor(JButton btn, CafeTable table) {
        boolean isSelected = (selectedTable != null && selectedTable.getId() == table.getId());

        if (isSelected) {
            btn.setBackground(COLOR_SELECTED);
        } else {
            switch (table.getStatus()) {
                case "Trong":
                    btn.setBackground(COLOR_AVAILABLE);
                    break;
                case "DangSuDung":
                    btn.setBackground(COLOR_IN_USE);
                    break;
                case "DaDat":
                    btn.setBackground(COLOR_RESERVED);
                    break;
                default:
                    btn.setBackground(COLOR_AVAILABLE);
            }
        }
    }

    private void refreshAllTableColors() {
        for (int i = 0; i < tableButtons.size() && i < tableList.size(); i++) {
            updateTableButtonColor(tableButtons.get(i), tableList.get(i));
        }
    }

    private void selectTable(CafeTable table) {
        selectedTable = table;
        jLabel1.setText(table.getName());
        refreshAllTableColors();
    }

    public void refreshTables() {
        loadTablesFromDatabase();
    }

    private void setupRefreshButton() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);

        lblTablesTitle.setText("SƠ ĐỒ BÀN");
        lblTablesTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        headerPanel.add(lblTablesTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBackground(new java.awt.Color(52, 152, 219));
        btnRefresh.setForeground(java.awt.Color.WHITE);
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> {
            refreshTables();
            JOptionPane.showMessageDialog(this, "Đã cập nhật sơ đồ bàn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        pTableArea.add(headerPanel, BorderLayout.PAGE_START);
    }

    // ==================== MENU MANAGEMENT (from HEAD) ====================

    public void refreshMenu() {
        filterMenuByCategory(null); // Mặc định hiển thị tất cả
    }

    // Lọc menu theo danh mục
    private void filterMenuByCategory(String category) {
        if (pMenuItems != null) {
            pMenuItems.removeAll();

            List<Product> products = productService.getAllProducts();

            for (Product product : products) {
                String status = product.getStatus();
                boolean isActive = "DangBan".equals(status) || "Đang bán".equals(status) || "1".equals(status);
                
                // Kiểm tra category (null = tất cả)
                boolean matchCategory = (category == null) || category.equalsIgnoreCase(product.getCategory());
                
                if (isActive && matchCategory) {
                    JButton btn = createMenuItemButton(
                        product.getName(),
                        String.format("%.0fđ", product.getPrice()),
                        product.getCategory(),
                        product.getImage()
                    );
                    pMenuItems.add(btn);
                }
            }

            pMenuItems.revalidate();
            pMenuItems.repaint();
        }
    }

    private JButton createMenuItemButton(String name, String price, String category, String imagePath) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(0, 5));
        btn.setPreferredSize(new Dimension(160, 150));
        btn.setMinimumSize(new Dimension(160, 150));
        btn.setMaximumSize(new Dimension(160, 150));
        btn.setFocusPainted(false);
        btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Tạo label chứa ảnh ở giữa - chiếm toàn bộ phần trên
        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setVerticalAlignment(SwingConstants.CENTER);
        
        // Load và scale ảnh - kích thước lớn hơn (100x100)
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                java.io.File imgFile = new java.io.File(imagePath);
                
                if (!imgFile.exists()) {
                    imgFile = new java.io.File("src/icon/" + imagePath);
                }
                if (!imgFile.exists()) {
                    imgFile = new java.io.File("images/" + imagePath);
                }
                if (!imgFile.exists()) {
                    imgFile = new java.io.File("src/images/" + imagePath);
                }
                
                if (imgFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(imgFile.getAbsolutePath());
                    Image scaledImg = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaledImg));
                } else {
                    lblImage.setText("📦");
                    lblImage.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
                }
            } catch (Exception e) {
                lblImage.setText("📦");
                lblImage.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
            }
        } else {
            lblImage.setText("📦");
            lblImage.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        }

        // Panel dưới cùng chứa tên (dòng 1) và giá (dòng 2) - căn giữa
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        bottomPanel.setOpaque(false);

        // Rút gọn tên nếu quá dài (max 14 ký tự để hiển thị đủ "...")
        String displayName = name;
        

        JLabel lblName = new JLabel(displayName, SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblName.setForeground(new Color(50, 50, 50));
        lblName.setToolTipText(name); // Hiển thị tên đầy đủ khi hover

        JLabel lblPrice = new JLabel(price, SwingConstants.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPrice.setForeground(new Color(231, 76, 60)); // Màu đỏ nổi bật

        bottomPanel.add(lblName);
        bottomPanel.add(lblPrice);

        // Thêm components vào button
        btn.add(lblImage, BorderLayout.CENTER);  // Ảnh chiếm phần giữa (lớn)
        btn.add(bottomPanel, BorderLayout.SOUTH); // Tên + Giá ở dưới

        Color bgColor = new Color(255, 255, 255);
        btn.setBackground(bgColor);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(240, 248, 255));
                btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                    javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
                btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
        });

        btn.addActionListener(e -> addItemToBill(name, price));

        return btn;
    }


    // ==================== BILL MANAGEMENT (merged) ====================

    private void addItemToBill(String itemName, String priceStr) {
        String priceNumeric = priceStr.replace(",", "").replace("đ", "").trim();
        int price = Integer.parseInt(priceNumeric);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String existingItem = (String) model.getValueAt(i, 0);
            if (existingItem.equals(itemName)) {
                int currentQty = (Integer) model.getValueAt(i, 1);
                int newQty = currentQty + 1;
                int total = newQty * price;

                model.setValueAt(newQty, i, 1);
                model.setValueAt(formatCurrency(total), i, 3);
                found = true;
                break;
            }
        }

        if (!found) {
            model.addRow(new Object[]{
                itemName,
                1,
                formatCurrency(price),
                formatCurrency(price)
            });
        }

        updateTotalAmount();
    }

    private void updateTotalAmount() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int subtotal = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String amountStr = (String) model.getValueAt(i, 3);
            String numericStr = amountStr.replace(",", "").replace("đ", "").trim();
            subtotal += Integer.parseInt(numericStr);
        }

        lblSubtotalValue.setText(formatCurrency(subtotal));

        int discountPercent = 0;
        try {
            discountPercent = Integer.parseInt(txtDiscountPercent.getText().trim());
            if (discountPercent < 0) discountPercent = 0;
            if (discountPercent > 100) discountPercent = 100;
        } catch (NumberFormatException e) {
            discountPercent = 0;
            txtDiscountPercent.setText("0");
        }

        int discountAmount = (subtotal * discountPercent) / 100;
        int total = subtotal - discountAmount;

        lblTotalValue.setText(formatCurrency(total));
    }

    private String formatCurrency(int amount) {
        return String.format("%,dđ", amount);
    }

    private void clearBill() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        txtDiscountPercent.setText("0");
        lblSubtotalValue.setText("0đ");
        lblTotalValue.setText("0đ");
    }

    // ==================== CHECKOUT (from HEAD) ====================

    private void handleCheckout() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong hóa đơn!");
            return;
        }

        if (selectedTable == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước khi thanh toán!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận thanh toán cho " + jLabel1.getText() + "?",
            "Xác nhận thanh toán",
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Order order = new Order();
        order.setTotalAmount(parseCurrency(lblTotalValue.getText()));
        order.setCreatedBy(UserSession.getCurrentUser().getUserName());

        List<OrderDetail> details = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String productName = model.getValueAt(i, 0).toString();
            int quantity = Integer.parseInt(model.getValueAt(i, 1).toString());
            double unitPrice = parseCurrency(model.getValueAt(i, 2).toString());
            double totalPrice = parseCurrency(model.getValueAt(i, 3).toString());

            int productId = productService.getProductIdByName(productName);

            if (productId == -1) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy sản phẩm: " + productName,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            OrderDetail detail = new OrderDetail();
            detail.setProductId(productId);
            detail.setProductName(productName);
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setTotalPrice(totalPrice);

            details.add(detail);
        }
        order.setDetails(details);

        int orderId = orderService.createOrder(order);

        if (orderId > 0) {
            printInvoice(orderId, order);

            // Update table status to available
            if (selectedTable != null) {
                selectedTable.setStatus("Trong");
                try {
                    tableService.changeStatus(selectedTable.getId(), "Trong");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                refreshAllTableColors();
            }

            clearBill();

            JOptionPane.showMessageDialog(this,
                "Thanh toán thành công!\nMã hóa đơn: " + orderId);
        } else {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi lưu hóa đơn!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseCurrency(String currencyStr) {
        return Double.parseDouble(currencyStr.replace("đ", "").replace(",", "").trim());
    }

    private void printInvoice(int orderId, Order order) {
        JDialog invoiceDialog = new JDialog();
        invoiceDialog.setTitle("Hóa đơn #" + orderId);
        invoiceDialog.setSize(400, 600);
        invoiceDialog.setLocationRelativeTo(this);

        StringBuilder invoice = new StringBuilder();
        invoice.append("===========================================\n");
        invoice.append("QUÁN CAFE JAVA\n");
        invoice.append("123 Đường Nguyễn Trãi\n");
        invoice.append("ĐT: 0123456789\n");
        invoice.append("===========================================\n\n");
        invoice.append("Hóa đơn số: ").append(orderId).append("\n");
        invoice.append("Ngày: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(order.getCreatedDate())).append("\n");
        invoice.append(jLabel1.getText()).append(" - ").append(jLabel2.getText()).append("\n");
        invoice.append("Nhân viên: ").append(UserSession.getCurrentUser().getFullName()).append("\n");
        invoice.append("-------------------------------------------\n\n");

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String name = model.getValueAt(i, 0).toString();
            String qty = model.getValueAt(i, 1).toString();
            String total = model.getValueAt(i, 3).toString();
            invoice.append(String.format("%-20s x%2s  %10s\n", name, qty, total));
        }

        invoice.append("\n-------------------------------------------\n");
        invoice.append(String.format("%-20s %15s\n", lblSubtotalLabel.getText(), lblSubtotalValue.getText()));
        invoice.append(String.format("%-20s %15s\n", lblDiscountLabel.getText(), txtDiscountPercent.getText() + "%"));
        invoice.append(String.format("%-20s %15s\n", lblTotalLabel.getText(), lblTotalValue.getText()));
        invoice.append("-------------------------------------------\n\n");
        invoice.append("     Cảm ơn quý khách! Hẹn gặp lại!\n");
        invoice.append("===========================================\n");

        JTextPane textPane = new JTextPane();
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textPane.setEditable(false);
        textPane.setText(invoice.toString());

        javax.swing.text.StyledDocument doc = textPane.getStyledDocument();
        javax.swing.text.SimpleAttributeSet center = new javax.swing.text.SimpleAttributeSet();
        javax.swing.text.StyleConstants.setAlignment(center, javax.swing.text.StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        invoiceDialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnPrint = new JButton("In");
        btnPrint.addActionListener(e -> {
            try {
                textPane.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(invoiceDialog, "Lỗi khi in: " + ex.getMessage());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnPrint);
        invoiceDialog.add(buttonPanel, BorderLayout.SOUTH);

        invoiceDialog.setVisible(true);
    }

    // ==================== GENERATED CODE (NetBeans) ====================

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        pMenuArea = new javax.swing.JPanel();
        pFilterBar = new javax.swing.JPanel();
        btnAll = new javax.swing.JButton();
        btnCoffee = new javax.swing.JButton();
        btnTea = new javax.swing.JButton();
        btnJuice = new javax.swing.JButton();
        btnCake = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pBillArea = new javax.swing.JPanel();
        pBillHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pBillBottom = new javax.swing.JPanel();
        pSummaryPanel = new javax.swing.JPanel();
        lblSubtotalLabel = new javax.swing.JLabel();
        lblSubtotalValue = new javax.swing.JLabel();
        lblDiscountLabel = new javax.swing.JLabel();
        txtDiscountPercent = new javax.swing.JTextField();
        lblTotalLabel = new javax.swing.JLabel();
        lblTotalValue = new javax.swing.JLabel();
        pSouth = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnCheckout = new javax.swing.JButton();
        pTableArea = new javax.swing.JPanel();
        lblTablesTitle = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pTablesGrid = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE));

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setResizeWeight(0.25);

        jSplitPane2.setDividerLocation(650);
        jSplitPane2.setResizeWeight(0.65);

        pMenuArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        pMenuArea.setLayout(new java.awt.BorderLayout());

        pFilterBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 8));

        btnAll.setText("jButton9");
        pFilterBar.add(btnAll);

        btnCoffee.setText("jButton10");
        pFilterBar.add(btnCoffee);

        btnTea.setText("jButton11");
        pFilterBar.add(btnTea);

        btnJuice.setText("jButton12");
        pFilterBar.add(btnJuice);

        btnCake.setText("jButton13");
        pFilterBar.add(btnCake);

        pMenuArea.add(pFilterBar, java.awt.BorderLayout.PAGE_START);
        pMenuArea.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane2.setLeftComponent(pMenuArea);

        pBillArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        pBillArea.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Bàn 01");

        jLabel2.setText("Dùng tại bàn ");

        javax.swing.GroupLayout pBillHeaderLayout = new javax.swing.GroupLayout(pBillHeader);
        pBillHeader.setLayout(pBillHeaderLayout);
        pBillHeaderLayout.setHorizontalGroup(
                pBillHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pBillHeaderLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(44, 44, 44)));
        pBillHeaderLayout.setVerticalGroup(
                pBillHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pBillHeaderLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(pBillHeaderLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                .addContainerGap(45, Short.MAX_VALUE)));

        pBillArea.add(pBillHeader, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        jScrollPane2.setViewportView(jTable1);

        pBillArea.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pBillBottom.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pBillBottom.setLayout(new java.awt.BorderLayout(0, 10));

        pSummaryPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.anchor = java.awt.GridBagConstraints.WEST;

        lblSubtotalLabel.setText("Tạm tính:");
        lblSubtotalLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        pSummaryPanel.add(lblSubtotalLabel, gbc);

        lblSubtotalValue.setText("0đ");
        lblSubtotalValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblSubtotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        pSummaryPanel.add(lblSubtotalValue, gbc);

        lblDiscountLabel.setText("Giảm giá (%):");
        lblDiscountLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        pSummaryPanel.add(lblDiscountLabel, gbc);

        txtDiscountPercent.setText("0");
        txtDiscountPercent.setPreferredSize(new java.awt.Dimension(80, 25));
        txtDiscountPercent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        pSummaryPanel.add(txtDiscountPercent, gbc);

        lblTotalLabel.setText("Tổng cộng:");
        lblTotalLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        pSummaryPanel.add(lblTotalLabel, gbc);

        lblTotalValue.setText("0đ");
        lblTotalValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTotalValue.setForeground(new java.awt.Color(52, 152, 219));
        lblTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        pSummaryPanel.add(lblTotalValue, gbc);

        pBillBottom.add(pSummaryPanel, java.awt.BorderLayout.CENTER);

        pSouth.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));

        btnCancel.setText("HỦY");
        btnCancel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnCancel.setPreferredSize(new java.awt.Dimension(120, 40));
        btnCancel.setBackground(new java.awt.Color(231, 76, 60));
        btnCancel.setForeground(java.awt.Color.WHITE);
        btnCancel.setFocusPainted(false);
        pSouth.add(btnCancel);

        btnCheckout.setText("THANH TOÁN");
        btnCheckout.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnCheckout.setPreferredSize(new java.awt.Dimension(150, 40));
        btnCheckout.setBackground(new java.awt.Color(46, 204, 113));
        btnCheckout.setForeground(java.awt.Color.WHITE);
        btnCheckout.setFocusPainted(false);
        pSouth.add(btnCheckout);

        pBillBottom.add(pSouth, java.awt.BorderLayout.SOUTH);

        pBillArea.add(pBillBottom, java.awt.BorderLayout.PAGE_END);

        jSplitPane2.setRightComponent(pBillArea);

        jSplitPane1.setRightComponent(jSplitPane2);

        pTableArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        pTableArea.setLayout(new java.awt.BorderLayout());

        lblTablesTitle.setText("SƠ ĐỒ BÀN ");
        pTableArea.add(lblTablesTitle, java.awt.BorderLayout.PAGE_START);

        // Dynamic grid layout (0 rows = auto)
        pTablesGrid.setLayout(new java.awt.GridLayout(0, 2, 16, 16));

        jButton1.setText("jButton1");
        pTablesGrid.add(jButton1);

        jButton3.setText("jButton3");
        pTablesGrid.add(jButton3);

        jButton2.setText("jButton2");
        pTablesGrid.add(jButton2);

        jButton4.setText("jButton4");
        pTablesGrid.add(jButton4);

        jButton5.setText("jButton5");
        pTablesGrid.add(jButton5);

        jButton6.setText("jButton6");
        pTablesGrid.add(jButton6);

        jButton7.setText("jButton7");
        pTablesGrid.add(jButton7);

        jButton8.setText("jButton8");
        pTablesGrid.add(jButton8);

        // Wrap pTablesGrid in ScrollPane
        jScrollPane3.setViewportView(pTablesGrid);
        jScrollPane3.setBorder(null);
        pTableArea.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        // Create Legend Panel
        pLegend = new javax.swing.JPanel();
        pLegend.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 5));
        pLegend.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        pTableArea.add(pLegend, java.awt.BorderLayout.PAGE_END);

        jSplitPane1.setLeftComponent(pTableArea);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnCake;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnCoffee;
    private javax.swing.JButton btnJuice;
    private javax.swing.JButton btnTea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel pLegend;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblDiscountLabel;
    private javax.swing.JLabel lblSubtotalLabel;
    private javax.swing.JLabel lblSubtotalValue;
    private javax.swing.JLabel lblTablesTitle;
    private javax.swing.JLabel lblTotalLabel;
    private javax.swing.JLabel lblTotalValue;
    private javax.swing.JPanel pBillArea;
    private javax.swing.JPanel pBillBottom;
    private javax.swing.JPanel pBillHeader;
    private javax.swing.JPanel pFilterBar;
    private javax.swing.JPanel pMenuArea;
    private javax.swing.JPanel pSouth;
    private javax.swing.JPanel pSummaryPanel;
    private javax.swing.JPanel pTableArea;
    private javax.swing.JPanel pTablesGrid;
    private javax.swing.JTextField txtDiscountPercent;
    // End of variables declaration//GEN-END:variables
}
