/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;




import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class qltb_dao {

    /* =========================
       KIỂM TRA RỖNG
       ========================= */
    public boolean isEmpty(qltb_thuoctinh tb) {
        return tb.getMaTB().isEmpty()
            || tb.getTenTB().isEmpty()
            || tb.getMaLoai().isEmpty();
    }

    /* =========================
       KIỂM TRA TRÙNG MÃ
       ========================= */
    public boolean isExist(String maTB) {
        String sql = "SELECT matbi FROM thietbi WHERE matbi = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maTB);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =========================
       THÊM THIẾT BỊ
       ========================= */
    public boolean insert(qltb_thuoctinh tb) {
        if (isEmpty(tb) || isExist(tb.getMaTB())) return false;

        String sql = "INSERT INTO thietbi VALUES (?, ?, ?, ?, ?)";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tb.getMaTB());
            ps.setString(2, tb.getTenTB());
            ps.setString(3, tb.getMaLoai());
            ps.setInt(4, tb.getTrangThai());
            ps.setString(5, tb.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =========================
       SỬA THIẾT BỊ
       ========================= */
    public boolean update(qltb_thuoctinh tb) {
        if (isEmpty(tb)) return false;

        String sql = """
            UPDATE thietbi 
            SET tentbi = ?, maloai = ?, trangthai = ?, ghichu = ?
            WHERE matbi = ?
        """;

        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tb.getTenTB());
            ps.setString(2, tb.getMaLoai());
            ps.setInt(3, tb.getTrangThai());
            ps.setString(4, tb.getGhiChu());
            ps.setString(5, tb.getMaTB());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =========================
       XÓA THIẾT BỊ
       ========================= */
    public boolean delete(String maTB) {
        String sql = "DELETE FROM thietbi WHERE matbi = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maTB);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =========================
       TÌM THEO MÃ
       ========================= */
    public qltb_thuoctinh findById(String maTB) {
        String sql = "SELECT * FROM thietbi WHERE matbi = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maTB);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new qltb_thuoctinh(
                        rs.getString("matbi"),
                        rs.getString("tentbi"),
                        rs.getString("maloai"),
                        rs.getString("trangthai")
                        
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* =========================
       LẤY TẤT CẢ THIẾT BỊ
       ========================= */
    public List<qltb_thuoctinh> getAll() {
    List<qltb_thuoctinh> list = new ArrayList<>();
    String sql = """
        SELECT tb.matbi, tb.tentbi, lt.tenloai, tt.tentinhtrang
        FROM thietbi tb
        JOIN loaitb lt ON tb.maloai = lt.maloai
        JOIN trangthai tt ON tb.trangthai = tt.matinhtrang
    """;

    try (
        Connection c = new db().getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {
         while (rs.next()) {
            qltb_thuoctinh tb = new qltb_thuoctinh();
            tb.setMaTB(rs.getString("matbi"));
            tb.setTenTB(rs.getString("tentbi"));
            tb.settenLoai(rs.getString("tenloai"));
            tb.settenTrangThai(rs.getString("tentinhtrang"));
            list.add(tb);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    public List<qltb_thuoctinh> getByLoai(String maloai) {
    List<qltb_thuoctinh> list = new ArrayList<>();

    String sql = """
        SELECT tb.matbi, tb.tentbi, lt.tenloai, tt.tentinhtrang
        FROM thietbi tb
        JOIN loaitb lt ON tb.maloai = lt.maloai
        JOIN trangthai tt ON tb.trangthai = tt.matinhtrang
        WHERE tb.maloai = ?
    """;

    try (Connection c = new db().getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, maloai);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            qltb_thuoctinh tb = new qltb_thuoctinh(
                rs.getString("matbi"),
                rs.getString("tentbi"),
                rs.getString("tenloai"),
                rs.getString("tentinhtrang")
            );
            list.add(tb);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


}



