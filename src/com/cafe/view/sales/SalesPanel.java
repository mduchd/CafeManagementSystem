
package com.cafe.view.sales;

import com.cafe.model.CafeTable;
import com.cafe.service.CafeTableService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SalesPanel extends javax.swing.JPanel {

    // Color constants for 3 table statuses
    private static final Color COLOR_AVAILABLE = new Color(46, 204, 113); // green: Trong (available)
    private static final Color COLOR_IN_USE = new Color(231, 76, 60); // red: DangSuDung (in use)
    private static final Color COLOR_RESERVED = new Color(241, 196, 15); // yellow: DaDat (reserved)
    private static final Color COLOR_SELECTED = new Color(52, 152, 219); // blue: currently selected

    // Services
    private final CafeTableService tableService = new CafeTableService();
    private final com.cafe.service.ProductService productService = new com.cafe.service.ProductService();

    // Table data from database
    private List<CafeTable> tableList = new ArrayList<>();
    private CafeTable selectedTable = null;
    private List<JButton> tableButtons = new ArrayList<>();

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

        // 3) Customize bill header labels
        jLabel1.setText("Chưa chọn bàn");
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

        jLabel2.setText("Dùng tại bàn");
        jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        // 4) Setup bill table model
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] { "Món", "SL", "Đơn giá", "Thành tiền" }));

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
        lblTotalValue.setForeground(new java.awt.Color(52, 152, 219)); // Blue
        lblTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        // 6) Customize action buttons
        btnCancel.setText("HỦY");
        btnCancel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnCancel.setBackground(new java.awt.Color(231, 76, 60)); // Red
        btnCancel.setForeground(java.awt.Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setPreferredSize(new java.awt.Dimension(120, 40));

        btnCheckout.setText("THANH TOÁN");
        btnCheckout.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnCheckout.setBackground(new java.awt.Color(46, 204, 113)); // Green
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
    }

    /**
     * Load tables from database and create dynamic table buttons
     */
    private void loadTablesFromDatabase() {
        // Clear existing buttons
        pTablesGrid.removeAll();
        tableButtons.clear();

        // Load tables from database
        tableList = tableService.getAll();

        // Calculate grid layout (max 2 columns)
        int rows = Math.max(1, (int) Math.ceil(tableList.size() / 2.0));
        pTablesGrid.setLayout(new java.awt.GridLayout(rows, 2, 16, 16));

        // Create button for each table
        for (CafeTable table : tableList) {
            JButton btn = createTableButton(table);
            tableButtons.add(btn);
            pTablesGrid.add(btn);
        }

        // If no tables, show message
        if (tableList.isEmpty()) {
            JLabel lblNoTables = new JLabel("Chưa có bàn nào", SwingConstants.CENTER);
            lblNoTables.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 14));
            pTablesGrid.add(lblNoTables);
        } else {
            // Auto-select first table
            selectTable(tableList.get(0));
        }

        // Refresh UI
        pTablesGrid.revalidate();
        pTablesGrid.repaint();
    }

    /**
     * Create a styled button for a table
     */
    private JButton createTableButton(CafeTable table) {
        JButton btn = new JButton(table.getName());
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btn.setForeground(java.awt.Color.WHITE);
        btn.setPreferredSize(new java.awt.Dimension(120, 60));

        // Set color based on status
        updateTableButtonColor(btn, table);

        // Click handler
        btn.addActionListener(e -> selectTable(table));

        return btn;
    }

    /**
     * Update button color based on table status
     */
    private void updateTableButtonColor(JButton btn, CafeTable table) {
        boolean isSelected = (selectedTable != null && selectedTable.getId() == table.getId());

        if (isSelected) {
            btn.setBackground(COLOR_SELECTED);
        } else {
            // Map status to color
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

    /**
     * Refresh all table button colors
     */
    private void refreshAllTableColors() {
        for (int i = 0; i < tableButtons.size() && i < tableList.size(); i++) {
            updateTableButtonColor(tableButtons.get(i), tableList.get(i));
        }
    }

    /**
     * Select a table and update UI
     */
    private void selectTable(CafeTable table) {
        selectedTable = table;
        jLabel1.setText(table.getName());
        refreshAllTableColors();
    }

    /**
     * Public method to refresh tables from database
     * Can be called from other panels when table data changes
     */
    public void refreshTables() {
        loadTablesFromDatabase();
    }

    /**
     * Setup refresh button in table diagram header
     */
    private void setupRefreshButton() {
        // Create header panel with title and refresh button
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);

        // Title label
        lblTablesTitle.setText("SƠ ĐỒ BÀN");
        lblTablesTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        headerPanel.add(lblTablesTitle, BorderLayout.WEST);

        // Refresh button
        JButton btnRefresh = new JButton("🔄 Làm mới");
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

        // Replace the title in pTableArea
        pTableArea.add(headerPanel, BorderLayout.PAGE_START);
    }

    private JButton createMenuItemButton(String name, String price, String category) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(5, 5));
        btn.setPreferredSize(new Dimension(120, 80));
        btn.setFocusPainted(false);

        // Label tên món
        JLabel lblName = new JLabel(name, SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Label giá
        JLabel lblPrice = new JLabel(price, SwingConstants.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPrice.setForeground(new Color(52, 152, 219));

        // Panel chứa text
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(lblName);
        textPanel.add(lblPrice);

        btn.add(textPanel, BorderLayout.CENTER);

        // Màu nền theo category
        Color bgColor = switch (category) {
            case "COFFEE" -> new Color(255, 255, 255);
            case "TEA" -> new Color(255, 255, 255);
            case "JUICE" -> new Color(255, 255, 255);
            case "CAKE" -> new Color(255, 255, 255);
            default -> new Color(255, 255, 255);
        };
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        // Click handler - thêm món vào bill
        btn.addActionListener(e -> addItemToBill(name, price));

        return btn;
    }

    private void addItemToBill(String itemName, String priceStr) {
        // Parse giá (loại bỏ dấu phẩy và 'đ')
        String priceNumeric = priceStr.replace(",", "").replace("đ", "").trim();
        int price = Integer.parseInt(priceNumeric);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        // Kiểm tra món đã có trong bill chưa
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String existingItem = (String) model.getValueAt(i, 0);
            if (existingItem.equals(itemName)) {
                // Tăng số lượng
                int currentQty = (Integer) model.getValueAt(i, 1);
                int newQty = currentQty + 1;
                int total = newQty * price;

                model.setValueAt(newQty, i, 1);
                model.setValueAt(formatCurrency(total), i, 3);
                found = true;
                break;
            }
        }

        // Nếu chưa có, thêm dòng mới
        if (!found) {
            model.addRow(new Object[] {
                    itemName,
                    1,
                    formatCurrency(price),
                    formatCurrency(price)
            });
        }

        // Cập nhật tổng tiền
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int subtotal = 0;

        // Calculate subtotal
        for (int i = 0; i < model.getRowCount(); i++) {
            String amountStr = (String) model.getValueAt(i, 3);
            String numericStr = amountStr.replace(",", "").replace("đ", "").trim();
            subtotal += Integer.parseInt(numericStr);
        }

        // Update subtotal label
        lblSubtotalValue.setText(formatCurrency(subtotal));

        // Calculate discount
        int discountPercent = 0;
        try {
            discountPercent = Integer.parseInt(txtDiscountPercent.getText().trim());
            if (discountPercent < 0)
                discountPercent = 0;
            if (discountPercent > 100)
                discountPercent = 100;
        } catch (NumberFormatException e) {
            discountPercent = 0;
            txtDiscountPercent.setText("0");
        }

        // Calculate total with discount
        int discountAmount = (subtotal * discountPercent) / 100;
        int total = subtotal - discountAmount;

        // Update total label
        lblTotalValue.setText(formatCurrency(total));
    }

    private String formatCurrency(int amount) {
        return String.format("%,dđ", amount);
    }

    private void clearBill() {
        // Clear table
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        // Reset discount
        txtDiscountPercent.setText("0");

        // Reset summary values
        lblSubtotalValue.setText("0đ");
        lblTotalValue.setText("0đ");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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
        pTablesGrid = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

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

        // Summary panel with GridBagLayout for better control
        pSummaryPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.anchor = java.awt.GridBagConstraints.WEST;

        // Tạm tính
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

        // Giảm giá
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

        // Tổng cộng
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

        // Buttons panel
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

        pTablesGrid.setLayout(new java.awt.GridLayout(4, 2, 16, 16));

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

        pTableArea.add(pTablesGrid, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 344, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE));

        pTableArea.add(jPanel1, java.awt.BorderLayout.PAGE_END);

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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
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
