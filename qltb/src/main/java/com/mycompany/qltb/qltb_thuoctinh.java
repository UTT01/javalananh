/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

/**
 *
 * @author LanAnh
 */

   public class qltb_thuoctinh {
    private String maTB;
    private String tenTB;
    private String maLoai;
    private int trangThai;
    private String ghiChu;
    private String tenLoai;
    private String tenTrangThai;
    private int soLuongTot;  
    private int soLuongHong;
    
    public qltb_thuoctinh() {}

    public qltb_thuoctinh(String maTB, String tenTB, String tenLoai, String tenTrangThai,int soLuongTot , int soLuongHong) {
        this.maTB = maTB;
        this.tenTB = tenTB;
        this.tenLoai = tenLoai;
        this.tenTrangThai = tenTrangThai;
        this.soLuongTot = soLuongTot;
        this.soLuongHong = soLuongHong;
    }

    public int getTongSoLuong() { 
        return this.soLuongTot + this.soLuongHong; 
    }
    

    public int getSoLuongTot() { return soLuongTot; }
    public void setSoLuongTot(int soLuongTot) { this.soLuongTot = soLuongTot; }

    public int getSoLuongHong() { return soLuongHong; }
    public void setSoLuongHong(int soLuongHong) { this.soLuongHong = soLuongHong; }
    public String getMaTB() { return maTB; }
    public void setMaTB(String maTB) { this.maTB = maTB; }

    public String getTenTB() { return tenTB; }
    public void setTenTB(String tenTB) { this.tenTB = tenTB; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String gettenTrangThai() { return tenTrangThai; }
     public String gettenLoai() { return tenLoai; }
    public void settenTrangThai(String tenTrangThai) {
    this.tenTrangThai = tenTrangThai;
}

public void settenLoai(String tenLoai) {
    this.tenLoai = tenLoai;
}

}


