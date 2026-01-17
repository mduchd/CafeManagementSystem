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
import java.text.MessageFormat;
import javax.swing.JTable;

public class StatsPanel extends javax.swing.JPanel {

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

        // 2. Tạo cửa sổ xem trước
        JDialog previewDialog = new JDialog();
        previewDialog.setTitle("Xem trước báo cáo");
        previewDialog.setModal(true);
        previewDialog.setSize(650, 750);
        previewDialog.setLocationRelativeTo(this);
        previewDialog.setLayout(new BorderLayout());

        // 3. Panel nội dung báo cáo - dùng BorderLayout thay vì BoxLayout
        JPanel reportPanel = new JPanel(new BorderLayout(0, 15));
        reportPanel.setBackground(Color.WHITE);
        reportPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        //  HEADER SECTION 
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblHeader = new JLabel("BÁO CÁO DOANH THU");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeader.setForeground(new Color(44, 62, 80));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblHeader);
        headerPanel.add(Box.createVerticalStrut(5));

        JLabel lblSubHeader = new JLabel("Java Coffee Management System");
        lblSubHeader.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblSubHeader.setForeground(Color.GRAY);
        lblSubHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblSubHeader);
        headerPanel.add(Box.createVerticalStrut(8));

        JLabel lblTime = new JLabel("Thời gian: " + txtFromDate.getText() + " đến " + txtToDate.getText());
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTime);

        reportPanel.add(headerPanel, BorderLayout.NORTH);

        // === CENTER SECTION ===
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Summary Panel với border đẹp
        JPanel summaryWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        summaryWrapper.setBackground(Color.WHITE);
        
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 20, 8));
        summaryPanel.setBackground(new Color(248, 249, 250));
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 25, 15, 25)
        ));
        
        summaryPanel.add(createSummaryLabel("Tổng doanh thu:"));
        summaryPanel.add(createSummaryValue(lblTotalRevenueValue.getText()));
        summaryPanel.add(createSummaryLabel("Tổng đơn hàng:"));
        summaryPanel.add(createSummaryValue(lblTotalOrdersValue.getText()));
        summaryPanel.add(createSummaryLabel("Món bán chạy nhất:"));
        summaryPanel.add(createSummaryValue(lblBestSellerValue.getText()));
        
        summaryWrapper.add(summaryPanel);
        centerPanel.add(summaryWrapper);
        centerPanel.add(Box.createVerticalStrut(20));

        // Chi tiết đơn hàng label
        JPanel detailLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        detailLabelPanel.setBackground(Color.WHITE);
        JLabel lblDetail = new JLabel("Chi tiết đơn hàng:");
        lblDetail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailLabelPanel.add(lblDetail);
        centerPanel.add(detailLabelPanel);
        centerPanel.add(Box.createVerticalStrut(8));

        // Bảng dữ liệu
        DefaultTableModel previewModel = new DefaultTableModel();
        for (int i = 0; i < table.getColumnCount(); i++) {
            previewModel.addColumn(table.getColumnName(i));
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            Object[] row = new Object[table.getColumnCount()];
            for (int j = 0; j < table.getColumnCount(); j++) {
                row[j] = table.getValueAt(i, j);
            }
            previewModel.addRow(row);
        }
        JTable previewTable = new JTable(previewModel);
        previewTable.setRowHeight(28);
        previewTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        previewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        previewTable.getTableHeader().setBackground(new Color(44, 62, 80));
        previewTable.getTableHeader().setForeground(Color.WHITE);
        previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane tableScroll = new JScrollPane(previewTable);
        tableScroll.setPreferredSize(new Dimension(550, 280));
        centerPanel.add(tableScroll);
        centerPanel.add(Box.createVerticalStrut(15));

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);
        JLabel lblFooter = new JLabel("In ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooter.setForeground(Color.GRAY);
        footerPanel.add(lblFooter);
        centerPanel.add(footerPanel);

        reportPanel.add(centerPanel, BorderLayout.CENTER);

        // Scroll cho toàn bộ báo cáo
        JScrollPane scrollPane = new JScrollPane(reportPanel);
        scrollPane.setBorder(null);
        previewDialog.add(scrollPane, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton btnPrint = new JButton("In báo cáo");
        btnPrint.setBackground(new Color(52, 152, 219));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFocusPainted(false);
        btnPrint.setPreferredSize(new Dimension(120, 35));
        btnPrint.addActionListener(e -> {
            try {
                MessageFormat header = new MessageFormat("Báo cáo Doanh thu - Java Coffee");
                MessageFormat footer = new MessageFormat("Trang {0,number,integer}");
                boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
                if (complete) {
                    JOptionPane.showMessageDialog(previewDialog, "Đã in thành công!");
                    previewDialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(previewDialog, "Lỗi khi in: " + ex.getMessage());
            }
        });
        buttonPanel.add(btnPrint);

        JButton btnCancel = new JButton("Đóng");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.addActionListener(e -> previewDialog.dispose());
        buttonPanel.add(btnCancel);

        previewDialog.add(buttonPanel, BorderLayout.SOUTH);
        previewDialog.setVisible(true);
    }

    private JLabel createSummaryLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    private JLabel createSummaryValue(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(52, 152, 219));
        return lbl;
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
