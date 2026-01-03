package com.mycompany.qltb;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class trangchu extends JFrame {

    CardLayout cardLayout;
    JPanel panelChinh;
    JPanel panelMenuNgang;

    public trangchu() {
        // 1. Cấu hình JFrame chính
        setTitle("HỆ THỐNG QUẢN LÝ THIẾT BỊ");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 2. TẠO MENU NẰM NGANG (NORTH)
        panelMenuNgang = new JPanel();
        panelMenuNgang.setBackground(new Color(45, 52, 54)); 
        panelMenuNgang.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));


        // Tạo các nút menu
        JButton btnHome = taoNutMenu("Trang Chủ");
        JButton btnQLTB = taoNutMenu("Quản Lý Thiết Bị");
        JButton btnQLHS = taoNutMenu("Quản Lý HS Mượn");
        JButton btnPhieuMuon = taoNutMenu("Phiếu Mượn ▾");
        JButton btnPhong = taoNutMenu("Quản Lý Phòng");
        JButton btnLop = taoNutMenu("Quản Lý Lớp");

        panelMenuNgang.add(btnHome);
        panelMenuNgang.add(btnQLTB);
        panelMenuNgang.add(btnQLHS);
        panelMenuNgang.add(btnPhieuMuon);
        panelMenuNgang.add(btnPhong);
        panelMenuNgang.add(btnLop);

       


        add(panelMenuNgang, BorderLayout.NORTH);

        // 3. TẠO MENU CON CHO PHIẾU MƯỢN
        JPopupMenu menuConPhieuMuon = new JPopupMenu();
        JMenuItem itemXemDS = new JMenuItem("Xem danh sách phiếu");
        JMenuItem itemXuatPhieu = new JMenuItem("Xuất phiếu mượn");
        menuConPhieuMuon.add(itemXemDS);
        menuConPhieuMuon.add(itemXuatPhieu);

        // 4. CẤU HÌNH CARDLAYOUT
        cardLayout = new CardLayout();
        panelChinh = new JPanel(cardLayout);

        // --- Các màn hình ---
        JPanel panelHome = taoPanelTam("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG");


        // Trang QLTB
        qltb_view viewQLTB = new qltb_view();

        // Trang quản lý phiếu mượn
        QuanLyPhieuMuonPanel panelPhieuMuon = new QuanLyPhieuMuonPanel(this);
        
        // --- SỬA Ở ĐÂY: Thay thế Panel tạm bằng Class QuanLyPhong ---
       // QuanLyPhong panelPhong = new QuanLyPhong(); 
 
        // -----------------------------------------------------------
        

        // Trang QLTB của bạn
      


        // Thêm vào CardLayout với mã định danh (Key)
        panelChinh.add(panelHome, "HOME");
        panelChinh.add(viewQLTB, "QLTB");
        //panelChinh.add(panelQLHS, "QLHS");
        panelChinh.add(panelPhieuMuon, "PHIEUMUON");

        
        // Thêm panelPhong thực tế vào CardLayout
//        panelChinh.add(panelPhong, "PHONG");
//        
//
//        panelChinh.add(panelPhong, "PHONG");
//panelChinh.add(panelLop, "LOP");

        add(panelChinh, BorderLayout.CENTER);

        // 5. XỬ LÝ SỰ KIỆN
        btnHome.addActionListener(e -> cardLayout.show(panelChinh, "HOME"));
        btnQLTB.addActionListener(e -> cardLayout.show(panelChinh, "QLTB"));
        btnQLHS.addActionListener(e -> cardLayout.show(panelChinh, "QLHS"));
        
        btnPhieuMuon.addActionListener(e -> {
            menuConPhieuMuon.show(btnPhieuMuon, 0, btnPhieuMuon.getHeight());
        });
        itemXemDS.addActionListener(e -> cardLayout.show(panelChinh, "PHIEUMUON"));
        itemXuatPhieu.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đang xuất phiếu..."));


        // Sự kiện chuyển tab Quản Lý Phòng
        btnPhong.addActionListener(e -> cardLayout.show(panelChinh, "PHONG"));
        
        btnLop.addActionListener(e -> cardLayout.show(panelChinh, "LOP"));

        // Mặc định hiện trang chủ

        btnPhong.addActionListener(e -> cardLayout.show(panelChinh, "PHONG"));
        btnLop.addActionListener(e -> cardLayout.show(panelChinh, "LOP"));


        cardLayout.show(panelChinh, "HOME");
    }

    private JButton taoNutMenu(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(45, 52, 54));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(9, 132, 227));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(45, 52, 54));
            }
        });
        return btn;
    }

    private JPanel taoPanelTam(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(title, JLabel.CENTER);
        l.setFont(new Font("Arial", Font.BOLD, 20));
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new trangchu().setVisible(true));
    }
}