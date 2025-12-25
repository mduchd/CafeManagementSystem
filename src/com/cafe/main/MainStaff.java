package com.cafe.main;

import com.cafe.view.main.MainFrame;
import com.cafe.model.User;
import com.cafe.service.UserSession;
import javax.swing.SwingUtilities;

/**
 * Demo to test STAFF role - shows only SalesPanel without sidebar
 */
public class MainStaff {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Login as STAFF
            User staffUser = new User("staff", "password", "staff", "Nhân viên Demo");
            UserSession.getInstance().login(staffUser);
            
            // Create and show MainFrame
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
