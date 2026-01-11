package com.cafe.view.warehouse;

import java.awt.*;
import javax.swing.*;

public class WarehousePanel extends JPanel {

    private static final Color TAB_BG = new Color(44, 62, 80);
    private static final Color TAB_ACTIVE = new Color(52, 152, 219);
    private static final Color TAB_HOVER = new Color(52, 73, 94);

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton activeButton = null;

    public WarehousePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // === TAB BAR NGANG Ở TRÊN ===
        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabBar.setBackground(TAB_BG);
        tabBar.setPreferredSize(new Dimension(0, 45));

        // Tạo các tab button
        JButton btnSupplier = createTabButton("Nhà cung cấp", "SUPPLIER");
        JButton btnImport = createTabButton("Nhập kho", "IMPORT");
        JButton btnExport = createTabButton("Xuất kho", "EXPORT");
        JButton btnStatistic = createTabButton("Thống kê", "STATISTIC");

        tabBar.add(btnSupplier);
        tabBar.add(btnImport);
        tabBar.add(btnExport);
        tabBar.add(btnStatistic);

        add(tabBar, BorderLayout.NORTH);

        // === CONTENT PANEL BÊN DƯỚI ===
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(240, 240, 240));

        // Wrap each panel in a JScrollPane for better display
        contentPanel.add(wrapInScrollPane(new SupplierPanel()), "SUPPLIER");
        contentPanel.add(wrapInScrollPane(new ImportPanel()), "IMPORT");
        contentPanel.add(wrapInScrollPane(new ExportPanel()), "EXPORT");
        contentPanel.add(wrapInScrollPane(new StatisticPanel()), "STATISTIC");

        add(contentPanel, BorderLayout.CENTER);

        // Set default active
        setActiveButton(btnSupplier);
        cardLayout.show(contentPanel, "SUPPLIER");
    }

    private JScrollPane wrapInScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JButton createTabButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(TAB_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(130, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn != activeButton) {
                    btn.setBackground(TAB_HOVER);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn != activeButton) {
                    btn.setBackground(TAB_BG);
                }
            }
        });

        // Click handler
        btn.addActionListener(e -> {
            setActiveButton(btn);
            cardLayout.show(contentPanel, cardName);
        });

        return btn;
    }

    private void setActiveButton(JButton btn) {
        if (activeButton != null) {
            activeButton.setBackground(TAB_BG);
            activeButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        }
        activeButton = btn;
        activeButton.setBackground(TAB_ACTIVE);
    }
}
