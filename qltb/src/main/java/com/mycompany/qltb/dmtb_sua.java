/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;


import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author LanAnh
 */

// Các import: nhập các class từ thư viện Java để sử dụng
import java.awt.BorderLayout;    // Layout sắp xếp NORTH, SOUTH, CENTER, EAST, WEST
import java.awt.Color;
import java.awt.FlowLayout;     // Layout sắp xếp các phần tử theo dòng ngang
import java.awt.Font;           // Class để tạo kiểu chữ (font)
import java.awt.GridLayout;     // Layout sắp xếp theo bảng lưới (hàng x cột)
import javax.swing.BorderFactory;   // Tạo viền (border) cho panel
import javax.swing.JButton;         // Nút bấm
import javax.swing.JFrame;          // Cửa sổ chính
import javax.swing.JLabel;          // Nhãn hiển thị chữ
import javax.swing.JPanel;          // //Khung chứa các thành phần con
import javax.swing.JTextField;      // Ô nhập văn bản
import javax.swing.JOptionPane;     // Hộp thoại thông báo (message dialog)

// Đây là khai báo class tên là dmtb_view
// Class này kế thừa từ JFrame → nghĩa là nó chính là một cửa sổ
// Mục đích: tạo form để thêm danh mục thiết bị mới
public class dmtb_sua extends JFrame {

    // Đây là trường dữ liệu (field) kiểu qltb_view, tên là parent
    // Dùng để lưu tham chiếu đến cửa sổ cha (danh sách chính) để sau khi thêm xong có thể reload dữ liệu
    private qltb_view parent;
    private dmtb_thuoctinh dm;
    public JTextField txtId = new JTextField();
    public JTextField txtHoten = new JTextField();
    public JButton btnadd = new JButton("Sửa");
    public JButton btnhuy = new JButton("Hủy");

    // Trường dữ liệu kiểu dmtb_dao (Data Access Object)
    // Dùng để gọi các phương thức thao tác với cơ sở dữ liệu (insert, update, delete...)
    public dmtb_dao dao = new dmtb_dao();

    // Đây là constructor mặc định (không tham số)
    // Gọi constructor có tham số với parent = null
 

    // Đây là constructor có tham số
    // Nhận vào cửa sổ cha (parent) để sau này có thể gọi loadData() từ cửa sổ cha
    public dmtb_sua(qltb_view parent, dmtb_thuoctinh dm) {
        this.parent = parent;  // Gán parent từ tham số vào trường dữ liệu
        this.dm=dm;
        // ===== Cấu hình Frame (cửa sổ) =====
        setTitle("Sửa danh mục");                  // Đặt tiêu đề cửa sổ
        setSize(400, 220);                          // Đặt kích thước cửa sổ: rộng 400, cao 220
        setLayout(new BorderLayout(10, 10));        // Dùng BorderLayout, khoảng cách ngang/dọc 10px
        setLocationRelativeTo(null);                // Hiển thị cửa sổ giữa màn hình
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Khi đóng cửa sổ chỉ đóng form này, không thoát chương trình

        // ===== Tiêu đề lớn ở trên cùng =====
        JLabel title = new JLabel("SỬA DANH MỤC THIẾT BỊ", JLabel.CENTER); // Tạo nhãn, căn giữa
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));                // Đặt font chữ đậm, cỡ 15
        add(title, BorderLayout.NORTH);                                    // Thêm vào phía NORTH (trên cùng)

        // ===== Phần form nhập liệu =====
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));  // Panel dùng lưới 2 hàng x 2 cột, khoảng cách 10px
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20)); // Tạo lề bên trong: trên 15, trái/phải 20, dưới 10
        form.add(new JLabel("Mã danh mục:"));      
        txtId.setText(dm.getMaloai());
        txtId.setEditable(false);  // Chỉ hiển thị, không cho sửa
        txtId.setBackground(new Color(240, 240, 240));  // Màu nền xám nhạt để rõ là readonly
        form.add(txtId);                          
        form.add(new JLabel("Tên danh mục:"));      
        form.add(txtHoten);                         
        add(form, BorderLayout.CENTER);             

        // ===== Phần nút bấm ở dưới =====
        JPanel pbtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5)); // Panel dùng FlowLayout, căn giữa, khoảng cách nút 15px
        pbtn.add(btnadd);   // Thêm nút "Thêm"
        pbtn.add(btnhuy);   // Thêm nút "Hủy"
        add(pbtn, BorderLayout.SOUTH);  // Đặt panel nút vào phía dưới

        // ===== Xử lý sự kiện khi nhấn nút Thêm =====
       btnadd.addActionListener(e -> {
            String ten = txtHoten.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            dm.setTenloai(ten);
            if (dao.update(dm)) {
                JOptionPane.showMessageDialog(this, "Sửa danh mục thành công!");
                if (parent != null) parent.loadDanhMuc();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Xử lý sự kiện nút Hủy: chỉ đóng form
        btnhuy.addActionListener(e -> dispose());

        // Hiển thị cửa sổ lên màn hình
        setVisible(true);
    }
    
}
