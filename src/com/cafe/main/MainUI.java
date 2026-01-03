package com.cafe.main;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import com.cafe.view.product.ProductPanel;

public class MainUI extends JFrame {

    public MainUI() {
        setTitle("Quản lý quán Cafe");
        setSize(900, 2400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());          
        add(new ProductPanel(), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainUI().setVisible(true);
        });
    }
}
