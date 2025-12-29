/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

/**
 *
 * @author LanAnh
 */

import javax.swing.*;

public class menu {

    public static JMenuBar createMainMenuBar(Runnable onNhapFile, Runnable onXuatFileTB, Runnable onLapPhieuMuon, Runnable onXuatPhieuMuon,
                                             Runnable onOpenSinhVien, Runnable onOpenPhong, Runnable onOpenLopTinChi) {
        JMenuBar bar = new JMenuBar();

        // 1. Menu "Quản lý thiết bị" (Code cũ OK)
        JMenu menuQuanLyTB = new JMenu("Quản lý thiết bị");
        JMenuItem itemNhapFile = new JMenuItem("Nhập file");
        JMenuItem itemXuatFile = new JMenuItem("Xuất file");

        itemNhapFile.addActionListener(e -> onNhapFile.run());
        itemXuatFile.addActionListener(e -> onXuatFileTB.run());

        menuQuanLyTB.add(itemNhapFile);
        menuQuanLyTB.add(itemXuatFile);

        // 2. Menu "Phiếu mượn" (Code cũ OK)
        JMenu menuPhieuMuon = new JMenu("Phiếu mượn");
        JMenuItem itemLapPhieu = new JMenuItem("Lập phiếu mượn");
        JMenuItem itemXuatPhieuMuon = new JMenuItem("Xuất file");
        
        itemLapPhieu.addActionListener(e -> onLapPhieuMuon.run());
        itemXuatPhieuMuon.addActionListener(e -> onXuatPhieuMuon.run());
        
        menuPhieuMuon.add(itemLapPhieu);
        menuPhieuMuon.add(itemXuatPhieuMuon);

        // =================================================================
        // 3. Menu "Quản lý sinh viên mượn" (ĐÃ SỬA)
        // Lỗi cũ: Gán actionListener trực tiếp vào JMenu -> Không chạy
        // Sửa: Thêm JMenuItem con
        JMenu menuSinhVienMuon = new JMenu("Quản lý sinh viên");
        JMenuItem itemOpenSV = new JMenuItem("Danh sách sinh viên"); // Tạo item con
        itemOpenSV.addActionListener(e -> onOpenSinhVien.run());     // Gán sự kiện vào item con
        menuSinhVienMuon.add(itemOpenSV);                            // Thêm item vào menu cha

        // =================================================================
        // 4. Menu "Quản lý phòng" (ĐÃ SỬA - Đây là chỗ bạn cần nhất)
        JMenu menuQuanLyPhong = new JMenu("Quản lý phòng");
        JMenuItem itemOpenPhong = new JMenuItem("Danh sách phòng");  // Tạo item con
        itemOpenPhong.addActionListener(e -> onOpenPhong.run());     // Gán sự kiện vào item con
        menuQuanLyPhong.add(itemOpenPhong);                          // Thêm item vào menu cha

        // =================================================================
        // 5. Menu "Lớp tín chỉ" (ĐÃ SỬA)
        JMenu menuLopTinChi = new JMenu("Lớp tín chỉ");
        JMenuItem itemOpenLTC = new JMenuItem("Danh sách lớp");      // Tạo item con
        itemOpenLTC.addActionListener(e -> onOpenLopTinChi.run());   // Gán sự kiện vào item con
        menuLopTinChi.add(itemOpenLTC);                              // Thêm item vào menu cha

        // Thêm tất cả vào thanh menu chính
        bar.add(menuQuanLyTB);
        bar.add(menuPhieuMuon);
        bar.add(menuSinhVienMuon);
        bar.add(menuQuanLyPhong);
        bar.add(menuLopTinChi);

        return bar;
    }
}