package com.cafe.view.warehouse;

import com.cafe.database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Warehouse Statistics Panel - View inventory statistics
 * Style đồng bộ với TablePanel và EmployeePanel
 */
public class StatisticPanel extends JPanel {

    // Table component
    private JTable tblStatistic;
    private DefaultTableModel tableModel;

    // Summary labels
    private JLabel lblTotalIngredients;
    private JLabel lblTotalImport;
    private JLabel lblTotalExport;
    private JLabel lblTotalValue;

    // Constructor
    public StatisticPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Thống kê tồn kho");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Summary cards at top
        JPanel pSummary = new JPanel(new GridLayout(1, 4, 15, 0));
        pSummary.setBackground(Color.WHITE);
        pSummary.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        lblTotalIngredients = new JLabel("0", SwingConstants.CENTER);
        lblTotalImport = new JLabel("0", SwingConstants.CENTER);
        lblTotalExport = new JLabel("0", SwingConstants.CENTER);
        lblTotalValue = new JLabel("0đ", SwingConstants.CENTER);

        pSummary.add(createCard("Loại nguyên liệu", lblTotalIngredients, new Color(52, 152, 219)));
        pSummary.add(createCard("Tổng nhập", lblTotalImport, new Color(76, 175, 80)));
        pSummary.add(createCard("Tổng xuất", lblTotalExport, new Color(255, 152, 0)));
        pSummary.add(createCard("Giá trị tồn kho", lblTotalValue, new Color(156, 39, 176)));

        // Center panel contains summary + table
        JPanel pCenter = new JPanel(new BorderLayout(10, 10));
        pCenter.setBackground(Color.WHITE);
        pCenter.add(pSummary, BorderLayout.NORTH);

        // Data table setup
        String[] columns = { "Nguyên liệu", "Tổng nhập", "Tổng xuất", "Tồn kho", "Giá trị (VNĐ)" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblStatistic = new JTable(tableModel);
        tblStatistic.setRowHeight(32);
        tblStatistic.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblStatistic.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblStatistic.getTableHeader().setBackground(new Color(44, 62, 80));
        tblStatistic.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblStatistic);
        pCenter.add(scrollPane, BorderLayout.CENTER);

        // Refresh button at bottom
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBottom.setBackground(Color.WHITE);
        JButton btnRefresh = new JButton("Làm mới thống kê");
        btnRefresh.setBackground(new Color(33, 150, 243));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadData());
        pBottom.add(btnRefresh);
        pCenter.add(pBottom, BorderLayout.SOUTH);

        add(pCenter, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(lblTitle, BorderLayout.NORTH);

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadData() {
        tableModel.setRowCount(0);

        String sql = """
                    SELECT
                        n.ingredient_name,
                        IFNULL(SUM(n.quantity), 0) AS tong_nhap,
                        IFNULL(x.tong_xuat, 0) AS tong_xuat,
                        (IFNULL(SUM(n.quantity), 0) - IFNULL(x.tong_xuat, 0)) AS ton_kho,
                        IFNULL(AVG(n.price), 0) AS avg_price
                    FROM tbl_import n
                    LEFT JOIN (
                        SELECT ingredient_name, SUM(quantity) AS tong_xuat
                        FROM tbl_export
                        GROUP BY ingredient_name
                    ) x ON n.ingredient_name = x.ingredient_name
                    GROUP BY n.ingredient_name, x.tong_xuat
                    ORDER BY n.ingredient_name
                """;

        int totalIngredients = 0;
        int totalImport = 0;
        int totalExport = 0;
        double totalValue = 0;

        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String ingredient = rs.getString("ingredient_name");
                int nhap = rs.getInt("tong_nhap");
                int xuat = rs.getInt("tong_xuat");
                int ton = rs.getInt("ton_kho");
                double avgPrice = rs.getDouble("avg_price");
                double value = ton * avgPrice;

                tableModel.addRow(new Object[] {
                        ingredient,
                        nhap,
                        xuat,
                        ton,
                        String.format("%,.0fđ", value)
                });

                totalIngredients++;
                totalImport += nhap;
                totalExport += xuat;
                totalValue += value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update summary cards
        lblTotalIngredients.setText(String.valueOf(totalIngredients));
        lblTotalImport.setText(String.valueOf(totalImport));
        lblTotalExport.setText(String.valueOf(totalExport));
        lblTotalValue.setText(String.format("%,.0fđ", totalValue));
    }
}
