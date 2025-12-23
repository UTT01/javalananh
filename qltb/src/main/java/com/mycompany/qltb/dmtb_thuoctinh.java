/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

import java.io.Serializable;

/**
 *
 * @author LanAnh
 */
public class dmtb_thuoctinh implements Serializable{
     private String maloai;
    private String tenloai;

    public dmtb_thuoctinh() {}

    public dmtb_thuoctinh(String maloai, String tenloai) {
        this.maloai = maloai;
        this.tenloai = tenloai;
    }

    public String getMaloai() {
        return maloai;
    }

    public void setMaloai(String maloai) {
        this.maloai = maloai;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }
    @Override
public String toString() {
    return tenloai;  // Hoặc tên field của bạn là gì (thường là ten hoặc tenloai)
}
}
