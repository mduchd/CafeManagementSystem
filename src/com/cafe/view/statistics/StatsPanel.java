/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.cafe.view.statistics;

import com.cafe.dao.StatisticsDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.MessageFormat; // Để tạo tiêu đề khi in
import javax.swing.JTable;      // Để dùng tính năng in của bảng
/**
 *
 * @author Owner
 */
public class StatsPanel extends javax.swing.JPanel {

    /**
     * Creates new form StatsPanel
     */
    //Màu sắc
    private final Color COLOR_BACKGROUND = new Color(240, 240, 240);
    private final Color COLOR_TEXT = new Color(50, 50, 50);
    private final Color COLOR_MAIN_BLUE = new Color(52, 152, 219);   
    private final Color COLOR_WHITE_CARD = Color.WHITE;
    private final Color COLOR_TABLE_HEADER = new Color(44, 62, 80);
    private final Color COLOR_INPUT_BG = Color.WHITE;
    
    //xử lý logic
    private JTextField txtFromDate;
    private JTextField txtToDate;
    private JLabel lblTotalRevenueValue;
    private JLabel lblTotalOrdersValue;
    private JLabel lblBestSellerValue;
    private JTable table;
    
    //xử lý dữ liệu
    private StatisticsDAO statsDAO;
    
    public StatsPanel() {
        statsDAO = new StatisticsDAO();
        initComponent();
    }
    private void initComponent(){
        setLayout(new BorderLayout());
        setBackground(COLOR_BACKGROUND);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // --- HEADER ---
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(COLOR_BACKGROUND);

        JLabel lblTitle = new JLabel("Báo cáo và thống kê doanh thu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- BỘ LỌC NGÀY ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        filterPanel.setBackground(COLOR_BACKGROUND);

        // Lấy ngày hiện tại làm mặc định
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String today = sdf.format(new Date());

        filterPanel.add(createLabel("Từ ngày (dd/mm/yyyy)"));
        txtFromDate = createTextField(today);
        filterPanel.add(txtFromDate);

        filterPanel.add(createLabel("Đến ngày"));
        txtToDate = createTextField(today);
        filterPanel.add(txtToDate);

        JButton btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThongKe.setBackground(COLOR_TABLE_HEADER);
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFocusPainted(false);
        btnThongKe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // ==> SỰ KIỆN BẤM NÚT <==
        btnThongKe.addActionListener((ActionEvent e) -> {
            loadStatisticsData(); // Gọi hàm xử lý logic
        });
        filterPanel.add(btnThongKe);
        
        JButton btnPrint = new JButton("In báo cáo");
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPrint.setBackground(new Color(46, 204, 113)); // Màu xanh lá cây cho nổi bật
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFocusPainted(false);
        btnPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Sự kiện khi bấm nút In
        btnPrint.addActionListener((ActionEvent e) -> {
            printReport(); // Gọi hàm in (chúng ta sẽ viết hàm này ngay bên dưới)
        });
        
        filterPanel.add(btnPrint); // Thêm nút In vào panel

        // --- CÁC THẺ THỐNG KÊ (CARDS) ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        cardsPanel.setBackground(COLOR_BACKGROUND);
        cardsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        cardsPanel.setPreferredSize(new Dimension(800, 180));

        // Khởi tạo label giá trị rỗng ban đầu
        lblTotalRevenueValue = createValueLabel("0 đ", Color.WHITE);
        lblTotalOrdersValue = createValueLabel("0 Đơn", COLOR_TEXT);
        lblBestSellerValue = createValueLabel("...", COLOR_TEXT);

        cardsPanel.add(createCardPanel("Tổng thu nhập", lblTotalRevenueValue, COLOR_MAIN_BLUE, Color.WHITE));
        cardsPanel.add(createCardPanel("Tổng đơn", lblTotalOrdersValue, COLOR_WHITE_CARD, COLOR_TEXT));
        cardsPanel.add(createCardPanel("Món bán chạy", lblBestSellerValue, COLOR_WHITE_CARD, COLOR_TEXT));

        topContainer.add(lblTitle);
        topContainer.add(filterPanel);
        topContainer.add(cardsPanel);
        mainPanel.add(topContainer, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        table = new JTable(); // Tạo bảng rỗng
        styleTable(table);    // Áp dụng style đẹp
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    // --- HÀM XỬ LÝ IN ẤN ---
    private void printReport() {
        // 1. Kiểm tra xem bảng có dữ liệu chưa
        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu để in! Vui lòng bấm Thống kê trước.");
            return;
        }

        try {
            // 2. Tạo tiêu đề và chân trang cho bản in
            MessageFormat header = new MessageFormat("Báo cáo Doanh thu - Java Coffee");
            MessageFormat footer = new MessageFormat("Trang {0,number,integer}");

            // 3. Gọi lệnh in mặc định của Java Swing
            // FIT_WIDTH: Tự động co bảng lại cho vừa khổ giấy A4
            boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, header, footer);

            if (complete) {
                JOptionPane.showMessageDialog(this, "Đã in thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Đã hủy lệnh in.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void loadStatisticsData() {
        String sFrom = txtFromDate.getText().trim();
        String sTo = txtToDate.getText().trim();

        try {
            // 1. Chuyển chuỗi ngày nhập vào thành Timestamp của SQL
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date dateFrom = sdf.parse(sFrom + " 00:00:00"); // Bắt đầu ngày
            Date dateTo = sdf.parse(sTo + " 23:59:59");     // Kết thúc ngày

            Timestamp tsFrom = new Timestamp(dateFrom.getTime());
            Timestamp tsTo = new Timestamp(dateTo.getTime());

            // 2. Gọi DAO lấy dữ liệu
            double revenue = statsDAO.getTotalRevenue(tsFrom, tsTo);
            int orders = statsDAO.getTotalOrders(tsFrom, tsTo);
            String bestSeller = statsDAO.getBestSellingProduct(tsFrom, tsTo);
            DefaultTableModel model = statsDAO.getOrdersModel(tsFrom, tsTo);

            // 3. Hiển thị lên giao diện
            lblTotalRevenueValue.setText(String.format("%,.0f đ", revenue));
            lblTotalOrdersValue.setText(orders + " Đơn");
            lblBestSellerValue.setText(bestSeller);
            
            // Cập nhật bảng
            table.setModel(model);
            styleTable(table); // Phải style lại sau khi set model mới

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Vui lòng nhập đúng định dạng: dd/MM/yyyy (Ví dụ: 25/10/2023)");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 676, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private JPanel createCardPanel(String title, JLabel valueLabel, Color bgColor, Color textColor) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(bgColor);
        if (bgColor == Color.WHITE) {
             card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        }
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(textColor);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(lblTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(valueLabel, gbc);
        return card;
    }

    private JLabel createValueLabel(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(color);
        return lbl;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setBackground(Color.WHITE);
        table.setForeground(COLOR_TEXT);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setBackground(COLOR_TABLE_HEADER);
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setPreferredSize(new Dimension(0, 40));
        }
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(COLOR_TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JTextField createTextField(String text) {
        JTextField tf = new JTextField(text, 10);
        tf.setBackground(COLOR_INPUT_BG);
        tf.setForeground(Color.BLACK);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)), 
                new EmptyBorder(5, 8, 5, 8))
        );
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return tf;
    }

    // Main test
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Thống Kê");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.add(new StatsPanel());
            frame.setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
