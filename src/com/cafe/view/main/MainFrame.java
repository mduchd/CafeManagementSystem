
package com.cafe.view.main;

import com.cafe.view.sales.SalesPanel;
import com.cafe.model.User;
import com.cafe.service.UserSession;
import javax.swing.*;
/**
 *
 * @author Owner
 */
public class MainFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());
    
    // Color scheme
    private static final java.awt.Color SIDEBAR_BG = new java.awt.Color(30, 58, 95);      // Dark blue
    private static final java.awt.Color SIDEBAR_HOVER = new java.awt.Color(41, 82, 130);  // Lighter blue
    private static final java.awt.Color SIDEBAR_ACTIVE = new java.awt.Color(52, 152, 219); // Bright blue
    
    private javax.swing.JButton activeButton = null;
    private java.awt.CardLayout cardLayout;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        super("Hệ thống quản lý Cafe");
        initComponents();
        initCustomLogic();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
    }
    
    private void initCustomLogic() {
        // Setup CardLayout
        cardLayout = (java.awt.CardLayout) pContent.getLayout();
        
        // Add panels to content area
        pContent.add(new SalesPanel(), "SALES");
        pContent.add(createPlaceholderPanel("Quản lý Bàn"), "TABLES");
        pContent.add(createPlaceholderPanel("Quản lý Sản phẩm"), "PRODUCTS");
        pContent.add(createPlaceholderPanel("Quản lý Kho"), "WAREHOUSE");
        pContent.add(createPlaceholderPanel("Thống kê"), "STATS");
        pContent.add(createPlaceholderPanel("Quản lý Nhân viên"), "EMPLOYEES");
        
        // Style sidebar
        pSidebar.setBackground(SIDEBAR_BG);
        pLogo.setBackground(SIDEBAR_BG);
        pMenu.setBackground(SIDEBAR_BG);
        
        // Style logo
        jLabel1.setForeground(java.awt.Color.WHITE);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        
        // Setup menu buttons
        setupMenuButton(btnSales, "Bán hàng", "SALES");
        setupMenuButton(btnTables, "Bàn", "TABLES");
        setupMenuButton(btnProduct, "Sản phẩm", "PRODUCTS");
        setupMenuButton(btnWarehouse, "Kho", "WAREHOUSE");
        setupMenuButton(btnStats, "Thống kê", "STATS");
        setupMenuButton(btnEmployee, "Nhân viên", "EMPLOYEES");
        
        // Setup role indicator panel at bottom of sidebar
        setupRoleIndicatorPanel();
        
        // Apply role-based permissions
        // Note: User should be logged in via UserSession before creating MainFrame
        applyRolePermissions();
        
        // Set initial active button (only if manager)
        if (UserSession.getInstance().isManager()) {
            setActiveButton(btnSales);
        }
        cardLayout.show(pContent, "SALES");
    }
    
    /**
     * Setup role indicator panel at bottom of sidebar
     */
    private void setupRoleIndicatorPanel() {
        javax.swing.JPanel pRoleIndicator = new javax.swing.JPanel();
        pRoleIndicator.setBackground(SIDEBAR_BG);
        pRoleIndicator.setLayout(new java.awt.BorderLayout());
        pRoleIndicator.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Role label
        javax.swing.JLabel lblRole = new javax.swing.JLabel();
        lblRole.setForeground(java.awt.Color.WHITE);
        lblRole.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        lblRole.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        // Update role label based on current user
        UserSession session = UserSession.getInstance();
        String roleText = session.getCurrentRole().getDisplayName();
        String userName = session.getCurrentUserName();
        lblRole.setText("<html><center>" + userName + "<br><small>" + roleText + "</small></center></html>");
        
        pRoleIndicator.add(lblRole, java.awt.BorderLayout.CENTER);
        
        // Logout button
        javax.swing.JButton btnLogout = new javax.swing.JButton("Đăng xuất");
        btnLogout.setForeground(java.awt.Color.WHITE);
        btnLogout.setBackground(new java.awt.Color(231, 76, 60));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        btnLogout.addActionListener(e -> {
            UserSession.getInstance().logout();
            dispose();
            // TODO: Show login screen
        });
        
        pRoleIndicator.add(btnLogout, java.awt.BorderLayout.SOUTH);
        
        // Add to sidebar
        pSidebar.add(pRoleIndicator, java.awt.BorderLayout.PAGE_END);
    }
    
    /**
     * Apply role-based permissions to menu items
     */
    private void applyRolePermissions() {
        UserSession session = UserSession.getInstance();
        
        if (session.isStaff()) {
            // STAFF: Hide sidebar completely, show only SalesPanel
            pSidebar.setVisible(false);
            
            // Show only Sales panel
            cardLayout.show(pContent, "SALES");
            
            // Add logout button for STAFF in top-right corner
            addStaffLogoutButton();
        } else if (session.isManager()) {
            // MANAGER: Show sidebar with all menus
            pSidebar.setVisible(true);
            
            // Show all menu buttons
            btnSales.setVisible(true);
            btnTables.setVisible(true);
            btnProduct.setVisible(true);
            btnWarehouse.setVisible(true);
            btnStats.setVisible(true);
            btnEmployee.setVisible(true);
        }
    }
    
    /**
     * Add logout button for STAFF users in top-right corner
     */
    private void addStaffLogoutButton() {
        // Create logout panel
        javax.swing.JPanel pStaffLogout = new javax.swing.JPanel();
        pStaffLogout.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));
        pStaffLogout.setOpaque(false);
        
        // User info label
        javax.swing.JLabel lblUserInfo = new javax.swing.JLabel();
        lblUserInfo.setText(UserSession.getInstance().getCurrentUserName() + " (Nhân viên)");
        lblUserInfo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        pStaffLogout.add(lblUserInfo);
        
        // Logout button
        javax.swing.JButton btnStaffLogout = new javax.swing.JButton("Đăng xuất");
        btnStaffLogout.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        btnStaffLogout.setBackground(new java.awt.Color(231, 76, 60));
        btnStaffLogout.setForeground(java.awt.Color.WHITE);
        btnStaffLogout.setFocusPainted(false);
        btnStaffLogout.setBorderPainted(false);
        btnStaffLogout.setPreferredSize(new java.awt.Dimension(90, 30));
        btnStaffLogout.addActionListener(e -> {
            UserSession.getInstance().logout();
            dispose();
            // TODO: Show login screen
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Đã đăng xuất. Vui lòng đăng nhập lại.", 
                "Đăng xuất", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
        pStaffLogout.add(btnStaffLogout);
        
        // Add to top of content area
        pContent.add(pStaffLogout, java.awt.BorderLayout.PAGE_START);
    }
    
    private void setupMenuButton(javax.swing.JButton btn, String text, String cardName) {
        btn.setText(text);
        btn.setForeground(java.awt.Color.WHITE);
        btn.setBackground(SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new java.awt.Dimension(200, 50));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn != activeButton) {
                    btn.setBackground(SIDEBAR_HOVER);
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn != activeButton) {
                    btn.setBackground(SIDEBAR_BG);
                }
            }
        });
        
        // Click handler
        btn.addActionListener(e -> {
            setActiveButton(btn);
            cardLayout.show(pContent, cardName);
        });
    }
    
    private void setActiveButton(javax.swing.JButton btn) {
        // Reset previous active button
        if (activeButton != null) {
            activeButton.setBackground(SIDEBAR_BG);
        }
        
        // Set new active button
        activeButton = btn;
        activeButton.setBackground(SIDEBAR_ACTIVE);
    }
    
    private javax.swing.JPanel createPlaceholderPanel(String title) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JLabel label = new javax.swing.JLabel(title, javax.swing.SwingConstants.CENTER);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        label.setForeground(new java.awt.Color(100, 100, 100));
        panel.add(label, java.awt.BorderLayout.CENTER);
        return panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pSidebar = new javax.swing.JPanel();
        pLogo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pMenu = new javax.swing.JPanel();
        btnSales = new javax.swing.JButton();
        btnTables = new javax.swing.JButton();
        btnProduct = new javax.swing.JButton();
        btnWarehouse = new javax.swing.JButton();
        btnStats = new javax.swing.JButton();
        btnEmployee = new javax.swing.JButton();
        pContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hệ thống quản lý Cafe");

        pSidebar.setBackground(new java.awt.Color(51, 0, 102));
        pSidebar.setPreferredSize(new java.awt.Dimension(200, 0));
        pSidebar.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Java Coffee");

        javax.swing.GroupLayout pLogoLayout = new javax.swing.GroupLayout(pLogo);
        pLogo.setLayout(pLogoLayout);
        pLogoLayout.setHorizontalGroup(
            pLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLogoLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel1)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        pLogoLayout.setVerticalGroup(
            pLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pLogoLayout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(37, 37, 37))
        );

        pSidebar.add(pLogo, java.awt.BorderLayout.PAGE_START);

        pMenu.setLayout(new javax.swing.BoxLayout(pMenu, javax.swing.BoxLayout.Y_AXIS));

        btnSales.setText("Bán hàng");
        pMenu.add(btnSales);

        btnTables.setText("Bàn");
        pMenu.add(btnTables);

        btnProduct.setText("Menu");
        pMenu.add(btnProduct);

        btnWarehouse.setText("Kho");
        pMenu.add(btnWarehouse);

        btnStats.setText("Thống kê");
        pMenu.add(btnStats);

        btnEmployee.setText("Nhân viên");
        pMenu.add(btnEmployee);

        pSidebar.add(pMenu, java.awt.BorderLayout.CENTER);

        getContentPane().add(pSidebar, java.awt.BorderLayout.LINE_START);

        pContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        pContent.setLayout(new java.awt.CardLayout());
        getContentPane().add(pContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEmployee;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnStats;
    private javax.swing.JButton btnTables;
    private javax.swing.JButton btnWarehouse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel pContent;
    private javax.swing.JPanel pLogo;
    private javax.swing.JPanel pMenu;
    private javax.swing.JPanel pSidebar;
    // End of variables declaration//GEN-END:variables
}
