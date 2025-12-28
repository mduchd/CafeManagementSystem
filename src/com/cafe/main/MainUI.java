package com.cafe.main;

import javax.swing.JFrame;
import com.cafe.view.product.ProductPanel; // sửa đúng package của bạn

public class MainUI extends JFrame {

    public MainUI() {
        setTitle("Quản lý quán Cafe");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new ProductPanel());
    }

    public static void main(String[] args) {
        new MainUI().setVisible(true);
    }
}
