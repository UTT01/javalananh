package com.mycompany.qltb;

import java.sql.Timestamp;

public class PhieuMuon {
    private int maPM;
    private String tenhs;
    private String maHS;
    private String maLop;
    private String maPhong;
    private Timestamp ngayMuon;
    private Timestamp hanTra;
    private String ghiChu;
    private Timestamp ngayTra;
    private int soLuong;

    public PhieuMuon() {}

    public PhieuMuon(int maPM, String maHS,String tenhs, String maLop, String maPhong, Timestamp ngayMuon, Timestamp hanTra, String ghiChu,Timestamp ngayTra,int soLuong) {
        this.maPM = maPM;
        this.maHS = maHS;
        this.tenhs = tenhs;
        this.maLop = maLop;
        this.maPhong = maPhong;
        this.ngayMuon = ngayMuon;
        this.hanTra = hanTra;
        this.ghiChu = ghiChu;
        this.ngayTra = ngayTra;
        this.soLuong = soLuong;
    }   

    public String getTenHS() {
        return tenhs;
    }

    public void setTenHS(String tenhs) {
        this.tenhs = tenhs;
    }

    public Timestamp getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(Timestamp ngayTra) {
        this.ngayTra = ngayTra;
    }
    // Getters and Setters
    public int getSoLuong() { return soLuong; }

    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public int getMaPM() { return maPM; }
    public void setMaPM(int maPM) { this.maPM = maPM; }
    public String getMaHS() { return maHS; }
    public void setMaHS(String maHS) { this.maHS = maHS; }
    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public Timestamp getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(Timestamp ngayMuon) { this.ngayMuon = ngayMuon; }
    public Timestamp getHanTra() { return hanTra; }
    public void setHanTra(Timestamp hanTra) { this.hanTra = hanTra; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}