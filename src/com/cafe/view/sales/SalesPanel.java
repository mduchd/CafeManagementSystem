/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.cafe.view.sales;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
/**
 *
 * @author Owner
 */
public class SalesPanel extends javax.swing.JPanel {

    private static final Color COLOR_EMPTY = new Color(46, 204, 113);     // xanh: trống
    private static final Color COLOR_BUSY = new Color(231, 76, 60);       // đỏ: có khách
    private static final Color COLOR_SELECTED = new Color(52, 152, 219);  // xanh dương: đang chọn
    private final Map<Integer, Integer> tableStatus = new HashMap<>(); // 0 trống, 1 có khách
    private int selectedTableNo = 1;
    
    private void initLogic() {
    // 1) Setup nút bàn (8 bàn theo UI bạn đang có)
    JButton[] tableBtns = { jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8 };

    for (int i = 0; i < tableBtns.length; i++) {
        int tableNo = i + 1;
        JButton b = tableBtns[i];

        b.setText("Bàn " + tableNo);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);

        tableStatus.put(tableNo, 0);              // mặc định trống
        setTableColor(b, 0, tableNo == selectedTableNo);

        b.addActionListener(e -> selectTable(tableNo));
    }

    // demo: bàn 2 có khách
    tableStatus.put(2, 1);
    refreshAllTableColors();

    // 2) Filter buttons (đổi text cho đẹp)
    btnAll.setText("Tất cả");
    btnCoffee.setText("Cà phê");
    btnTea.setText("Trà");
    btnJuice.setText("Nước");
    btnCake.setText("Bánh");

    // 3) Bill header label (đang là jLabel1/jLabel2 trong form)
    jLabel1.setText("Bàn 01");
    jLabel2.setText("Dùng tại bàn");

    // 4) Bill table model
    jTable1.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][]{},
        new String[]{"Món", "SL", "Đơn giá", "Thành tiền"}
    ));
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
        jPanel1 = new javax.swing.JPanel();

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        pTableArea.add(jPanel1, java.awt.BorderLayout.PAGE_END);

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
    private javax.swing.JPanel jPanel1;
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
