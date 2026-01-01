
package com.cafe.view.sales;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class SalesPanel extends javax.swing.JPanel {

    private static final Color COLOR_EMPTY = new Color(46, 204, 113);     // xanh: trống
    private static final Color COLOR_BUSY = new Color(231, 76, 60);       // đỏ: có khách
    private static final Color COLOR_SELECTED = new Color(52, 152, 219);  // xanh dương: đang chọn
    private final Map<Integer, Integer> tableStatus = new HashMap<>(); // 0 trống, 1 có khách
    private int selectedTableNo = 1;
    private final com.cafe.service.ProductService productService = new com.cafe.service.ProductService();
    
    public SalesPanel() {
        initComponents();
        initLogic();
    }
    
    private void initLogic() {
    // 1) Setup table buttons with custom styling
    JButton[] tableBtns = { jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8 };

    for (int i = 0; i < tableBtns.length; i++) {
        int tableNo = i + 1;
        JButton b = tableBtns[i];

        // Set text
        b.setText("Bàn " + tableNo);
        
        // Set style
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        b.setForeground(java.awt.Color.WHITE);

        // Initialize table status (0 = empty, 1 = busy)
        tableStatus.put(tableNo, 0);
        
        // Set initial color
        setTableColor(b, 0, tableNo == selectedTableNo);

        // Add click handler
        b.addActionListener(e -> selectTable(tableNo));
    }

    // Demo: Table 2 is busy
    tableStatus.put(2, 1);
    refreshAllTableColors();
    
    // 2) Customize filter buttons
    btnAll.setText("Tất cả");
    btnCoffee.setText("Cà phê");
    btnTea.setText("Trà");
    btnJuice.setText("Nước");
    btnCake.setText("Bánh");
    
    // 3) Customize bill header labels
    jLabel1.setText("Bàn 01");
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
        model.addRow(new Object[]{
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
        if (discountPercent < 0) discountPercent = 0;
        if (discountPercent > 100) discountPercent = 100;
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

private void selectTable(int tableNo) {
    selectedTableNo = tableNo;
    jLabel1.setText(String.format("Bàn %02d", tableNo));
    refreshAllTableColors();
}

private void refreshAllTableColors() {
    JButton[] tableBtns = { jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8 };
    for (int i = 0; i < tableBtns.length; i++) {
        int tableNo = i + 1;
        int status = tableStatus.getOrDefault(tableNo, 0);
        setTableColor(tableBtns[i], status, tableNo == selectedTableNo);
    }
}

private void setTableColor(JButton btn, int status, boolean selected) {
    if (selected) btn.setBackground(COLOR_SELECTED);
    else if (status == 1) btn.setBackground(COLOR_BUSY);
    else btn.setBackground(COLOR_EMPTY);
    btn.setForeground(Color.WHITE);
}
  

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
        pActionRow = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jButton15 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        pSouth = new javax.swing.JPanel();
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(44, 44, 44))
        );
        pBillHeaderLayout.setVerticalGroup(
            pBillHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBillHeaderLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(pBillHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pBillArea.add(pBillHeader, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        pBillArea.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pBillBottom.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        pBillBottom.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        pActionRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 8));

        jLabel3.setText("Món");
        pActionRow.add(jLabel3);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pActionRow.add(jComboBox2);

        jLabel4.setText("Số lượng");
        pActionRow.add(jLabel4);
        pActionRow.add(jSpinner1);

        jButton15.setText("Thêm");
        pActionRow.add(jButton15);

        jLabel5.setText("Tổng tiền");
        pActionRow.add(jLabel5);

        pBillBottom.add(pActionRow);

        btnCheckout.setText("THANH TOÁN");

        javax.swing.GroupLayout pSouthLayout = new javax.swing.GroupLayout(pSouth);
        pSouth.setLayout(pSouthLayout);
        pSouthLayout.setHorizontalGroup(
            pSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSouthLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(btnCheckout)
                .addContainerGap(147, Short.MAX_VALUE))
        );
        pSouthLayout.setVerticalGroup(
            pSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSouthLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(btnCheckout)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pBillBottom.add(pSouth);

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

        jSplitPane1.setLeftComponent(pTableArea);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnCake;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnCoffee;
    private javax.swing.JButton btnJuice;
    private javax.swing.JButton btnTea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblTablesTitle;
    private javax.swing.JPanel pActionRow;
    private javax.swing.JPanel pBillArea;
    private javax.swing.JPanel pBillBottom;
    private javax.swing.JPanel pBillHeader;
    private javax.swing.JPanel pFilterBar;
    private javax.swing.JPanel pMenuArea;
    private javax.swing.JPanel pSouth;
    private javax.swing.JPanel pTableArea;
    private javax.swing.JPanel pTablesGrid;
    // End of variables declaration//GEN-END:variables
}
