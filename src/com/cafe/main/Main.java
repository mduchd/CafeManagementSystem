package com.cafe.main;

import com.cafe.view.login.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Main entry point for Cafe Management System
 * Flow: LoginFrame -> MainFrame (with role-based UI)
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
    }
}
