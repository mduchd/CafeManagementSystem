package com.cafe.main;

import com.cafe.view.main.MainFrame;
import com.cafe.model.User;
import com.cafe.service.UserSession;
import javax.swing.SwingUtilities;

/**
 * Demo to test MANAGER role - shows full sidebar with all menus
 */
public class MainManager {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Login as MANAGER
            User managerUser = new User("manager", "password", "manager", "Quản lý Demo");
            UserSession.setCurrentUser(managerUser);
            
            // Create and show MainFrame
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
