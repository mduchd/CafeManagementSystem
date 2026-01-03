package com.cafe.view.employee;

import com.cafe.model.Employee;
import com.cafe.service.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// Employee Management Panel - CRUD operations for employee records
public class EmployeePanel extends JPanel {

    // Service for database operations
    private EmployeeService employeeService = new EmployeeService();

    // Table components
    private JTable tblEmployees;
    private DefaultTableModel tableModel;

    // Form input fields
    private JTextField txtFullName;
    private JTextField txtDateOfBirth;
    private JComboBox<String> cboGender;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtStartedDate;
    private JTextField txtSalary;
    private JComboBox<String> cboPosition;
    private JTextField txtSearch;

    // Currently selected employee ID (-1 = none)
    private int selectedId = -1;

    // Date format: dd/MM/yyyy
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor - initialize UI and load data
    public EmployeePanel() {
        initComponents();
        loadData();
    }

    // Initialize all UI components
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Quản lý Nhân viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table area
        JPanel pCenter = new JPanel(new BorderLayout(10, 10));
        pCenter.setBackground(Color.WHITE);

        // Search panel
        JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSearch.setBackground(Color.WHITE);
        pSearch.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        pSearch.add(txtSearch);
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> search());
        pSearch.add(btnSearch);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });
        pSearch.add(btnRefresh);
        pCenter.add(pSearch, BorderLayout.NORTH);

        // Data table setup
        String[] columns = { "Mã NV", "Họ tên", "Ngày sinh", "Giới tính", "SĐT", "Email", "Chức vụ", "Lương" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        tblEmployees = new JTable(tableModel);
        tblEmployees.setRowHeight(28);
        tblEmployees.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblEmployees.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Row selection listener
        tblEmployees.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblEmployees);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        add(pCenter, BorderLayout.CENTER);

        // Right - Form panel
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBackground(new Color(245, 245, 245));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pRight.setPreferredSize(new Dimension(280, 0));

        // Form title - centered
        JPanel pTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pTitle.setBackground(new Color(245, 245, 245));
        pTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblForm = new JLabel("Thông tin nhân viên");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pTitle.add(lblForm);
        pRight.add(pTitle);
        pRight.add(Box.createVerticalStrut(15));

        // Full Name field
        pRight.add(createLabel("Họ tên *"));
        txtFullName = new JTextField();
        txtFullName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtFullName.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtFullName);
        pRight.add(Box.createVerticalStrut(8));

        // Date of Birth field
        pRight.add(createLabel("Ngày sinh (dd/MM/yyyy)"));
        txtDateOfBirth = new JTextField();
        txtDateOfBirth.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtDateOfBirth.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtDateOfBirth);
        pRight.add(Box.createVerticalStrut(8));

        // Gender field
        pRight.add(createLabel("Giới tính"));
        cboGender = new JComboBox<>(new String[] { "Nam", "Nữ" });
        cboGender.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboGender.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(cboGender);
        pRight.add(Box.createVerticalStrut(8));

        // Phone field
        pRight.add(createLabel("Số điện thoại"));
        txtPhone = new JTextField();
        txtPhone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPhone.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtPhone);
        pRight.add(Box.createVerticalStrut(8));

        // Email field
        pRight.add(createLabel("Email"));
        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtEmail);
        pRight.add(Box.createVerticalStrut(8));

        // Address field
        pRight.add(createLabel("Địa chỉ"));
        txtAddress = new JTextField();
        txtAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtAddress);
        pRight.add(Box.createVerticalStrut(8));

        // Started Date field
        pRight.add(createLabel("Ngày vào làm (dd/MM/yyyy)"));
        txtStartedDate = new JTextField();
        txtStartedDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtStartedDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtStartedDate);
        pRight.add(Box.createVerticalStrut(8));

        // Salary field
        pRight.add(createLabel("Lương"));
        txtSalary = new JTextField();
        txtSalary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtSalary.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(txtSalary);
        pRight.add(Box.createVerticalStrut(8));

        // Position field
        pRight.add(createLabel("Chức vụ"));
        cboPosition = new JComboBox<>(new String[] { "Quản lý", "Thu ngân", "Phục vụ", "Pha chế" });
        cboPosition.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboPosition.setAlignmentX(Component.CENTER_ALIGNMENT);
        pRight.add(cboPosition);
        pRight.add(Box.createVerticalStrut(20));

        // Action buttons
        JPanel pButtons = new JPanel(new GridLayout(2, 2, 5, 5));
        pButtons.setBackground(new Color(245, 245, 245));
        pButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        pButtons.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add button
        JButton btnAdd = new JButton("Thêm");
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> add());
        pButtons.add(btnAdd);

        // Update button
        JButton btnUpdate = new JButton("Sửa");
        btnUpdate.setBackground(new Color(33, 150, 243));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> update());
        pButtons.add(btnUpdate);

        // Delete button
        JButton btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> delete());
        pButtons.add(btnDelete);

        // Clear form button
        JButton btnClear = new JButton("Xóa form");
        btnClear.addActionListener(e -> clearForm());
        pButtons.add(btnClear);

        pRight.add(pButtons);

        add(pRight, BorderLayout.EAST);
    }

    // Helper method to create form labels
    private JPanel createLabel(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(label);
        return panel;
    }

    // Load all employees from database to table
    private void loadData() {
        tableModel.setRowCount(0);
        List<Employee> employees = employeeService.getAll();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[] {
                    emp.getId(),
                    emp.getFullName(),
                    formatDate(emp.getDateOfBirth()),
                    emp.getGender(),
                    emp.getPhoneNumber(),
                    emp.getEmail(),
                    emp.getPosition(),
                    formatSalary(emp.getSalary())
            });
        }
    }

    // Format salary with Vietnamese currency
    private String formatSalary(double salary) {
        return String.format("%,.0fđ", salary);
    }

    // Format LocalDate to string (dd/MM/yyyy)
    private String formatDate(LocalDate date) {
        if (date == null)
            return "";
        return date.format(DATE_FORMAT);
    }

    // Parse string to LocalDate, returns null if invalid
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty())
            return null;
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Handle table row selection - populate form with selected data
    private void selectRow() {
        int row = tblEmployees.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);

            // Get full employee data from service
            Employee emp = employeeService.getById(selectedId);
            if (emp != null) {
                txtFullName.setText(emp.getFullName());
                txtDateOfBirth.setText(formatDate(emp.getDateOfBirth()));
                cboGender.setSelectedItem(emp.getGender());
                txtPhone.setText(emp.getPhoneNumber());
                txtEmail.setText(emp.getEmail());
                txtAddress.setText(emp.getAddress());
                txtStartedDate.setText(formatDate(emp.getStartedDate()));
                txtSalary.setText(String.valueOf((long) emp.getSalary()));
                cboPosition.setSelectedItem(emp.getPosition());
            }
        }
    }

    // Clear all form fields and reset selection
    private void clearForm() {
        selectedId = -1;
        txtFullName.setText("");
        txtDateOfBirth.setText("");
        cboGender.setSelectedIndex(0);
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtStartedDate.setText("");
        txtSalary.setText("");
        cboPosition.setSelectedIndex(0);
        tblEmployees.clearSelection();
    }

    // Get Employee object from form fields with validation
    private Employee getFormData() throws Exception {
        Employee emp = new Employee();
        emp.setId(selectedId);
        emp.setFullName(txtFullName.getText().trim());

        // Validate date of birth
        String dobStr = txtDateOfBirth.getText().trim();
        if (!dobStr.isEmpty()) {
            LocalDate dob = parseDate(dobStr);
            if (dob == null) {
                throw new Exception("Ngày sinh không hợp lệ! Định dạng: dd/MM/yyyy");
            }
            emp.setDateOfBirth(dob);
        }

        emp.setGender((String) cboGender.getSelectedItem());
        emp.setPhoneNumber(txtPhone.getText().trim());
        emp.setEmail(txtEmail.getText().trim());
        emp.setAddress(txtAddress.getText().trim());

        // Validate start date
        String startStr = txtStartedDate.getText().trim();
        if (!startStr.isEmpty()) {
            LocalDate startDate = parseDate(startStr);
            if (startDate == null) {
                throw new Exception("Ngày vào làm không hợp lệ! Định dạng: dd/MM/yyyy");
            }
            emp.setStartedDate(startDate);
        }

        // Validate salary
        String salaryStr = txtSalary.getText().trim();
        if (!salaryStr.isEmpty()) {
            try {
                emp.setSalary(Double.parseDouble(salaryStr.replace(",", "")));
            } catch (NumberFormatException e) {
                throw new Exception("Lương không hợp lệ!");
            }
        }

        emp.setPosition((String) cboPosition.getSelectedItem());
        return emp;
    }

    // Add new employee to database
    private void add() {
        try {
            Employee emp = getFormData();
            employeeService.add(emp);
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update existing employee in database
    private void update() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Employee emp = getFormData();
            employeeService.update(emp);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected employee from database
    private void delete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                employeeService.delete(selectedId);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Search employees by keyword
    private void search() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Employee> employees = employeeService.search(keyword);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[] {
                    emp.getId(),
                    emp.getFullName(),
                    formatDate(emp.getDateOfBirth()),
                    emp.getGender(),
                    emp.getPhoneNumber(),
                    emp.getEmail(),
                    emp.getPosition(),
                    formatSalary(emp.getSalary())
            });
        }
    }
}
