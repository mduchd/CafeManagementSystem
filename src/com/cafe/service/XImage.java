package com.cafe.service;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public class XImage {
    public static ImageIcon getResizedIcon(String fileName, int width, int height) {
        try {
            // Chỉ định thư mục chứa ảnh là src/icon/
            File file = new File("src/icon/" + fileName);
            
            if (!file.exists()) {
                System.out.println("Không tìm thấy file: " + file.getAbsolutePath());
                return null;
            }
            
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}