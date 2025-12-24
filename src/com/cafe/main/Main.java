package com.cafe.main;

import com.cafe.view.login.LoginFrame;
import com.cafe.view.sales.SalesPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Sales Demo");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new SalesPanel());
            f.setSize(1200, 700);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
        
        
    }
}
