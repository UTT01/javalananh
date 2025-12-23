/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

/**
 *
 * @author LanAnh
 */
public class trangthai {
   
    private int maTinhTrang;
    private String tenTinhTrang;
    private String moTa;

    public trangthai() {}

    public trangthai(int maTinhTrang, String tenTinhTrang, String moTa) {
        this.maTinhTrang = maTinhTrang;
        this.tenTinhTrang = tenTinhTrang;
        this.moTa = moTa;
    }

    public int getMaTinhTrang() { return maTinhTrang; }
    public void setMaTinhTrang(int maTinhTrang) { this.maTinhTrang = maTinhTrang; }

    public String getTenTinhTrang() { return tenTinhTrang; }
    public void setTenTinhTrang(String tenTinhTrang) { this.tenTinhTrang = tenTinhTrang; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    @Override
public String toString() {
    return tenTinhTrang;
}

}


