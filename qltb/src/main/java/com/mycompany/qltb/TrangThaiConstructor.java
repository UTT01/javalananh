/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

/**
 *
 * @author Admin
 */
public class TrangThaiConstructor {
    //constructor cho trạng thái
    public static class TrangThai {
        private String maTinhTrang;
        private String tenTinhTrang;

        public TrangThai(String maTinhTrang, String tenTinhTrang) {
            this.maTinhTrang = maTinhTrang;
            this.tenTinhTrang = tenTinhTrang;
        }

        public String getMaTinhTrang() { return maTinhTrang; }
        public String getTenTinhTrang() { return tenTinhTrang; }

        @Override
        public String toString() {
            return tenTinhTrang; // Để hiển thị trong ComboBox
        }
    }
}
