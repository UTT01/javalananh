/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

import com.mycompany.qltb.KhuVucConstructor.KhuVuc;
import com.mycompany.qltb.TrangThaiConstructor.TrangThai;

/**
 *
 * @author Admin
 */
public class PhongConstructor {
// Class đại diện cho Phòng
    public static class Phong {
    private String maPhong;
    private String tenPhong;
    private KhuVuc khuVuc;
    private TrangThai trangThai;

    // Cập nhật Constructor
    public Phong(String maPhong, String tenPhong, KhuVuc khuVuc, TrangThai trangThai) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.khuVuc = khuVuc;
        this.trangThai = trangThai;
    }

    // Getter/Setter
    public String getMaPhong() { return maPhong; }
    public String getTenPhong() { return tenPhong; }
    public KhuVuc getKhuVuc() { return khuVuc; }
    public TrangThai getTrangThai() { return trangThai; }
}
}
