package com.mycompany.qltb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class qltb_dao {

    public boolean isEmpty(qltb_thuoctinh tb) {
        return tb.getMaTB().isEmpty() || tb.getTenTB().isEmpty() || tb.getMaLoai().isEmpty();
    }

    public boolean isExist(String maTB) {
        String sql = "SELECT matbi FROM thietbi WHERE matbi = ?";
        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maTB);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /* =========================
       THÊM THIẾT BỊ (Đã sửa thêm soluong)
       ========================= */
    public boolean insert(qltb_thuoctinh tb) {
        if (isEmpty(tb) || isExist(tb.getMaTB())) return false;

        // Thêm cột soluong vào câu SQL
        String sql = "INSERT INTO thietbi(matbi, tentbi, maloai, trangthai, ghichu, soluong) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tb.getMaTB());
            ps.setString(2, tb.getTenTB());
            ps.setString(3, tb.getMaLoai());
            ps.setInt(4, tb.getTrangThai());
            ps.setString(5, tb.getGhiChu());
            ps.setInt(6, tb.getSoLuong()); // <--- QUAN TRỌNG: Thêm dòng này

            return ps.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /* =========================
       SỬA THIẾT BỊ (Đã sửa thêm soluong)
       ========================= */
    public boolean update(qltb_thuoctinh tb) {
        if (isEmpty(tb)) return false;

        String sql = "UPDATE thietbi SET tentbi = ?, maloai = ?, trangthai = ?, ghichu = ?, soluong = ? WHERE matbi = ?";

        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tb.getTenTB());
            ps.setString(2, tb.getMaLoai());
            ps.setInt(3, tb.getTrangThai());
            ps.setString(4, tb.getGhiChu());
            ps.setInt(5, tb.getSoLuong()); 
            ps.setString(6, tb.getMaTB());

            return ps.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(String maTB) {
        String sql = "DELETE FROM thietbi WHERE matbi = ?";
        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maTB);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /* =========================
       TÌM THEO MÃ (Đã sửa lỗi cú pháp)
       ========================= */
    public qltb_thuoctinh findById(String maTB) {
        String sql = "SELECT * FROM thietbi WHERE matbi = ?";
        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maTB);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                qltb_thuoctinh tb = new qltb_thuoctinh();
                tb.setMaTB(rs.getString("matbi"));
                tb.setTenTB(rs.getString("tentbi"));
                tb.setMaLoai(rs.getString("maloai"));
                tb.setTrangThai(rs.getInt("trangthai"));
                tb.setGhiChu(rs.getString("ghichu"));
                tb.setSoLuong(rs.getInt("soluong")); // Lấy số lượng
                return tb;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /* =========================
       LẤY TẤT CẢ (Đã thêm lấy soluong)
       ========================= */
    public List<qltb_thuoctinh> getAll() {
        List<qltb_thuoctinh> list = new ArrayList<>();
        String sql = "SELECT tb.matbi, tb.tentbi, lt.tenloai, tt.tentinhtrang, tb.soluong " +
                     "FROM thietbi tb " +
                     "JOIN loaitb lt ON tb.maloai = lt.maloai " +
                     "JOIN trangthai tt ON tb.trangthai = tt.matinhtrang";

        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                qltb_thuoctinh tb = new qltb_thuoctinh();
                tb.setMaTB(rs.getString("matbi"));
                tb.setTenTB(rs.getString("tentbi"));
                tb.settenLoai(rs.getString("tenloai"));
                tb.settenTrangThai(rs.getString("tentinhtrang"));
                tb.setSoLuong(rs.getInt("soluong")); // <--- Lấy số lượng
                list.add(tb);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /* =========================
       LẤY THEO LOẠI (Đã thêm lấy soluong)
       ========================= */
    public List<qltb_thuoctinh> getByLoai(String maloai) {
        List<qltb_thuoctinh> list = new ArrayList<>();
        String sql = "SELECT tb.matbi, tb.tentbi, lt.tenloai, tt.tentinhtrang, tb.soluong " +
                     "FROM thietbi tb " +
                     "JOIN loaitb lt ON tb.maloai = lt.maloai " +
                     "JOIN trangthai tt ON tb.trangthai = tt.matinhtrang " +
                     "WHERE tb.maloai = ?";

        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maloai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qltb_thuoctinh tb = new qltb_thuoctinh();
                tb.setMaTB(rs.getString("matbi"));
                tb.setTenTB(rs.getString("tentbi"));
                tb.settenLoai(rs.getString("tenloai"));
                tb.settenTrangThai(rs.getString("tentinhtrang"));
                tb.setSoLuong(rs.getInt("soluong")); // <--- Lấy số lượng
                list.add(tb);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}