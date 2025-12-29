/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

/**
 *
 * @author Admin
 */
public class KhuVucConstructor {
    public static class KhuVuc {
    private String maKhuVuc;
    private String tenKhuVuc;

    public KhuVuc(String maKhuVuc, String tenKhuVuc) {
        this.maKhuVuc = maKhuVuc;
        this.tenKhuVuc = tenKhuVuc;
    }

    public String getMaKhuVuc() { return maKhuVuc; }
    public String getTenKhuVuc() { return tenKhuVuc; }

    @Override
    public String toString() {
        return tenKhuVuc; // Hiển thị tên trong ComboBox/List
    }
}
}
