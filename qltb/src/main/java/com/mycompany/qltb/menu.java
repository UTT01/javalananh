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
import java.awt.event.ActionListener;

public class menu {

    public static JMenuBar createMainMenuBar(Runnable onNhapFile, Runnable onXuatFileTB, Runnable onXuatPhieuMuon,
                                             Runnable onOpenSinhVien, Runnable onOpenPhong, Runnable onOpenLopTinChi) {
        JMenuBar bar = new JMenuBar();

        // Menu "Quản lý thiết bị"
        JMenu menuQuanLyTB = new JMenu("Quản lý thiết bị");
        JMenuItem itemNhapFile = new JMenuItem("Nhập file");
        JMenuItem itemXuatFile = new JMenuItem("Xuất file");

        itemNhapFile.addActionListener(e -> onNhapFile.run());
        itemXuatFile.addActionListener(e -> onXuatFileTB.run());

        menuQuanLyTB.add(itemNhapFile);
        menuQuanLyTB.add(itemXuatFile);

        // Menu "Phiếu mượn"
        JMenu menuPhieuMuon = new JMenu("Phiếu mượn");
        JMenuItem itemXuatPhieuMuon = new JMenuItem("Xuất file");
        itemXuatPhieuMuon.addActionListener(e -> onXuatPhieuMuon.run());
        menuPhieuMuon.add(itemXuatPhieuMuon);

        // Menu "Quản lý sinh viên mượn"
        JMenu menuSinhVienMuon = new JMenu("Quản lý sinh viên mượn");
        menuSinhVienMuon.addActionListener(e -> onOpenSinhVien.run());

        // Menu "Quản lý phòng"
        JMenu menuQuanLyPhong = new JMenu("Quản lý phòng");
        menuQuanLyPhong.addActionListener(e -> onOpenPhong.run());

        // Menu "Lớp tín chỉ"
        JMenu menuLopTinChi = new JMenu("Lớp tín chỉ");
        menuLopTinChi.addActionListener(e -> onOpenLopTinChi.run());

        // Thêm tất cả vào thanh menu
        bar.add(menuQuanLyTB);
        bar.add(menuPhieuMuon);
        bar.add(menuSinhVienMuon);
        bar.add(menuQuanLyPhong);
        bar.add(menuLopTinChi);

        return bar;
    }
}

