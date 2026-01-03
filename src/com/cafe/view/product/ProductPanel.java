/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.cafe.view.product;

import com.cafe.service.ProductService;
import com.cafe.model.Product;
import com.cafe.service.XImage;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class ProductPanel extends javax.swing.JPanel {

    private ProductService productService = new ProductService();
    private DefaultTableModel tableModel;

   public ProductPanel() {
    initComponents();
    tableModel = (DefaultTableModel) tblProduct.getModel();
    loadTable();
    
    try {
        btnAdd.setIcon(com.cafe.service.XImage.getResizedIcon("add.png", 20, 20));
        btnUpdate.setIcon(com.cafe.service.XImage.getResizedIcon("edit.png", 20, 20));
        btnDelete.setIcon(com.cafe.service.XImage.getResizedIcon("delete.png", 20, 20));
        btnSearch.setIcon(com.cafe.service.XImage.getResizedIcon("search.png", 20, 20));
    } catch (Exception e) {
        System.err.println("Lỗi load icon nút bấm: " + e.getMessage());
    }
}

    // --- CÁC HÀM SỬA LỖI ---

    public void loadTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblProduct.getModel();
            model.setRowCount(0);
            List<Product> list = productService.getAllProducts();
            for (Product p : list) {
                // Đổ đủ 6 cột vào Model
                model.addRow(new Object[]{
                    p.getId(), p.getName(), p.getCategory(), 
                    p.getPrice(), p.getStatus(), p.getImage()
                });
            }
            // Ẩn cột thứ 6 (index 5)
            if (tblProduct.getColumnCount() >= 6) {
                tblProduct.getColumnModel().getColumn(5).setMinWidth(0);
                tblProduct.getColumnModel().getColumn(5).setMaxWidth(0);
                tblProduct.getColumnModel().getColumn(5).setWidth(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi nạp bảng: " + e.getMessage());
        }
    } // Đã thêm dấu đóng ngoặc ở đây

    public void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            File dest = new File("src/icon/" + file.getName());
            try {
                if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
                Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                lblHinhAnh.setToolTipText(file.getName()); 
                displayImage(file.getName()); // Gọi hàm hiển thị
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi lưu ảnh: " + ex.getMessage());
            }
        }
    }

    private void displayImage(String fileName) {
    try {
        if (fileName == null || fileName.isEmpty()) {
            lblHinhAnh.setIcon(null);
            lblHinhAnh.setText("No Image");
            return;
        }

        // Tạo đường dẫn đến thư mục src/icon/
        File file = new File("src/icon/" + fileName);
        if (file.exists()) {
            // Đọc ảnh và resize cho vừa khít với JLabel lblHinhAnh
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(lblHinhAnh.getWidth(), 
                            lblHinhAnh.getHeight(), Image.SCALE_SMOOTH);
            lblHinhAnh.setIcon(new ImageIcon(img));
            lblHinhAnh.setText("");
        } else {
            lblHinhAnh.setIcon(null);
            lblHinhAnh.setText("Ảnh không tồn tại");
        }
    } catch (Exception e) {
        lblHinhAnh.setIcon(null);
    }
}

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        cbCategory.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        lblHinhAnh.setIcon(null);
        lblHinhAnh.setToolTipText(null);
        tblProduct.clearSelection();
    }    
    private void searchProduct() {
    // 1. Lấy dữ liệu từ các ô nhập liệu (Sửa đúng tên biến của bạn: txtId, txtName)
    String maSP = txtId.getText().trim().toLowerCase();
    String tenSP = txtName.getText().trim().toLowerCase();
    
    // 2. Lấy loại sản phẩm từ ComboBox (Giả sử tên biến là cboLoaiSanPham)
    String loaiSP = "";
    if (cbCategory.getSelectedIndex() > 0) { 
        // Lấy giá trị được chọn và chuyển về chữ thường để so sánh
        loaiSP = cbCategory.getSelectedItem().toString().toLowerCase();
    }
    // 3. Nếu tất cả các ô tìm kiếm đều trống, tải lại toàn bộ bảng và thoát hàm
    if (maSP.isEmpty() && tenSP.isEmpty() && loaiSP.isEmpty()) {
        loadTable();
        return;
    }

    // 4. Lấy danh sách sản phẩm mới nhất từ Service
    List<Product> list = productService.getAllProducts();
    tableModel.setRowCount(0); // Xóa trắng bảng để chuẩn bị đổ dữ liệu đã lọc

    // 5. Vòng lặp lọc dữ liệu
    for (Product p : list) {
        // Chuyển ID (int) sang String để dùng hàm .contains()
        String currentId = String.valueOf(p.getId()).toLowerCase();
        
        // Xử lý an toàn tránh lỗi Null cho Name và Category
        String currentName = (p.getName() == null) ? "" : p.getName().toLowerCase();
        String currentCate = (p.getCategory() == null) ? "" : p.getCategory().toLowerCase();

        // Kiểm tra điều kiện tìm kiếm (Logic: Nếu ô tìm kiếm trống THÌ coi như khớp)
        boolean matchMa = maSP.isEmpty() || currentId.contains(maSP);
        boolean matchTen = tenSP.isEmpty() || currentName.contains(tenSP);
        boolean matchLoai = loaiSP.isEmpty() || currentCate.equals(loaiSP);

        // Chỉ thêm vào bảng nếu thỏa mãn TẤT CẢ các điều kiện (Tìm kiếm kết hợp)
        if (matchMa && matchTen && matchLoai) {
            tableModel.addRow(new Object[]{
                p.getId(), 
                p.getName(), 
                p.getCategory(), 
                p.getPrice(), 
                p.getStatus()
            });
        }
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblCategory = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        cbCategory = new javax.swing.JComboBox<>();
        cbStatus = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        lblHinhAnh = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();
        btnShowAll = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("QUẢN LÝ SẢN PHẨM ");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel2.setBackground(new java.awt.Color(153, 255, 153));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("THÔNG TIN SẢN PHẨM ");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblId.setText("Mã sản phẩm:");

        lblName.setText("Tên sản phẩm:");

        lblCategory.setText("Loại sản phẩm:");

        lblPrice.setText("Giá bán:");

        lblStatus.setText("Trạng thái:");

        txtId.setActionCommand("null");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });

        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cà phê", "Trà", "Nước ngọt", "Bánh" }));

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang bán", "Ngừng bán" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSearch.setToolTipText("Nhập từ khóa để tìm kiếm sản phẩm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        lblHinhAnh.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblHinhAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhAnhMouseClicked(evt);
            }
        });

        jLabel3.setText("Hình ảnh");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(75, 75, 75))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCategory)
                            .addComponent(lblStatus))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(lblName)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblId)
                                    .addComponent(lblPrice))
                                .addGap(20, 20, 20)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtId)
                            .addComponent(txtName)
                            .addComponent(txtPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85)
                        .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblId)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblName)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPrice)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblCategory)
                                            .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(lblStatus))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("DANH SÁCH SẢN PHẨM");
        jLabel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "Tên sản phẩm", "Loại SP", "Giá bán", " Trạng thái", "Hình ảnh"
            }
        ));
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduct);

        btnShowAll.setText("Hiển thị tất cả danh sách");
        btnShowAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShowAll)
                .addGap(28, 28, 28))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnShowAll)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(136, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStatusActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
    try {
        Product p = new Product();
        p.setName(txtName.getText().trim());
        p.setCategory(cbCategory.getSelectedItem().toString());
        p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
        p.setStatus(cbStatus.getSelectedItem().toString());
        
        String fileName = lblHinhAnh.getToolTipText();
        p.setImage(fileName != null ? fileName : "default.png");

        if (productService.insertProduct(p)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Thêm thất bại! Kiểm tra lại dữ liệu.");
        }
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Giá bán phải là số!");
    }
    // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
    try {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }
        
        Product p = new Product();
        p.setId(Integer.parseInt(txtId.getText()));
        p.setName(txtName.getText().trim());
        p.setCategory(cbCategory.getSelectedItem().toString());
        p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
        p.setStatus(cbStatus.getSelectedItem().toString());
        p.setImage(lblHinhAnh.getToolTipText());

        if (productService.updateProduct(p)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    if (txtId.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        int id = Integer.parseInt(txtId.getText());
        if (productService.deleteProduct(id)) {
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại");
        }
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductMouseClicked
    int row = tblProduct.getSelectedRow();
    if (row < 0) return;

    // Đổ dữ liệu lên các ô text/combobox
    txtId.setText(tblProduct.getValueAt(row, 0).toString());
    txtName.setText(tblProduct.getValueAt(row, 1).toString());
    cbCategory.setSelectedItem(tblProduct.getValueAt(row, 2).toString());
    txtPrice.setText(tblProduct.getValueAt(row, 3).toString());
    cbStatus.setSelectedItem(tblProduct.getValueAt(row, 4).toString());

    Object cellValue = tblProduct.getValueAt(row, 5); 
    if (cellValue != null) {
        String fileName = cellValue.toString();
        // Lưu tên file vào ToolTip để khi bấm "Sửa" có thể lấy lại
        lblHinhAnh.setToolTipText(fileName); 
        
        // Gọi XImage để resize ảnh vừa khít với khung JLabel
        ImageIcon photo = com.cafe.service.XImage.getResizedIcon(fileName, 
                          lblHinhAnh.getWidth(), lblHinhAnh.getHeight());
        lblHinhAnh.setIcon(photo);
        lblHinhAnh.setText(photo == null ? "Không có ảnh" : "");
    }
// TODO add your handling code here:
    }//GEN-LAST:event_tblProductMouseClicked

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
searchProduct();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void lblHinhAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhAnhMouseClicked
    JFileChooser fileChooser = new JFileChooser("src/icon/"); // Trỏ vào thư mục icon của dự án
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        // Lưu chỉ tên file vào ToolTipText để lưu vào Database
        lblHinhAnh.setToolTipText(file.getName());
        displayImage(file.getName());
    }

    }//GEN-LAST:event_lblHinhAnhMouseClicked

    private void btnShowAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAllActionPerformed
    loadTable();
    clearForm();
    System.out.println("Đã cập nhật lại toàn bộ danh sách sản phẩm.");
  // TODO add your handling code here:
    }//GEN-LAST:event_btnShowAllActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblProduct;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables

}
