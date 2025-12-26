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
    
        // Chỉ insert vào soluong_tot và soluong_hong
        String sql = "INSERT INTO thietbi(matbi, tentbi, maloai, trangthai, ghichu, soluong_tot, soluong_hong) VALUES (?, ?, ?, ?, ?, ?, 0)";
        
        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tb.getMaTB());
            ps.setString(2, tb.getTenTB());
            ps.setString(3, tb.getMaLoai());
            ps.setInt(4, tb.getTrangThai());
            ps.setString(5, tb.getGhiChu());
            ps.setInt(6, tb.getSoLuongTot()); // Lấy số lượng nhập vào làm số lượng tốt
    
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /* =========================
       SỬA THIẾT BỊ (Đã sửa thêm soluong_tot và soluong_hong)
       ========================= */
    public boolean update(qltb_thuoctinh tb) {
        if (isEmpty(tb)) return false;

        String sql = "UPDATE thietbi SET tentbi = ?, maloai = ?, trangthai = ?, ghichu = ?, soluong_tot = ?, soluong_hong = ? WHERE matbi = ?";

        try (Connection c = new db().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tb.getTenTB());
            ps.setString(2, tb.getMaLoai());
            ps.setInt(3, tb.getTrangThai());
            ps.setString(4, tb.getGhiChu());
            ps.setInt(5, tb.getSoLuongTot());
            ps.setInt(6, tb.getSoLuongHong());
            ps.setString(7, tb.getMaTB());

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
       TÌM THEO MÃ (Đã sửa để lấy soluong_tot và soluong_hong)
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
                tb.setSoLuongTot(rs.getInt("soluong_tot")); // Lấy số lượng tốt
                tb.setSoLuongHong(rs.getInt("soluong_hong")); // Lấy số lượng hỏng
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
        // Không cần select tong_soluong từ DB
        String sql = "SELECT tb.matbi, tb.tentbi, lt.tenloai, tt.tentinhtrang, tb.soluong_tot, tb.soluong_hong " +
                     "FROM thietbi tb " +
                     "JOIN loaitb lt ON tb.maloai = lt.maloai " +
                     "JOIN trangthai tt ON tb.trangthai = tt.matinhtrang";
    
        try (Connection c = new db().getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                qltb_thuoctinh tb = new qltb_thuoctinh();
                tb.setMaTB(rs.getString("matbi"));
                tb.setTenTB(rs.getString("tentbi"));
                tb.settenLoai(rs.getString("tenloai"));
                tb.settenTrangThai(rs.getString("tentinhtrang"));
                
                // Chỉ cần set 2 giá trị này
                tb.setSoLuongTot(rs.getInt("soluong_tot"));
                tb.setSoLuongHong(rs.getInt("soluong_hong"));
                
                // Khi gọi tb.getTongSoLuong() ở view, nó sẽ tự trả về (Tot + Hong)
                
                list.add(tb);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /* =========================
       LẤY THEO LOẠI (Đã sửa để lấy soluong_tot và soluong_hong)
       ========================= */
    public List<qltb_thuoctinh> getByLoai(String maloai) {
        List<qltb_thuoctinh> list = new ArrayList<>();
        String sql = "SELECT tb.matbi, tb.tentbi, lt.tenloai, tt.tentinhtrang, tb.soluong_tot, tb.soluong_hong " +
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
                tb.setSoLuongTot(rs.getInt("soluong_tot")); // Lấy số lượng tốt
                tb.setSoLuongHong(rs.getInt("soluong_hong")); // Lấy số lượng hỏng
                list.add(tb);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}