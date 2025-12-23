package com.mycompany.qltb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO - Data Access Object
 * Xử lý dữ liệu bảng loaitb
 */
public class dmtb_dao {

    // =========================
    // THÊM LOẠI THIẾT BỊ
    // =========================
    public boolean insert(dmtb_thuoctinh ltb) {
        String sql = "INSERT INTO loaitb VALUES (?, ?)";
        try {
            db database = new db();
            Connection c = database.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, ltb.getMaloai());
            ps.setString(2, ltb.getTenloai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // SỬA LOẠI THIẾT BỊ
    // =========================
    public boolean update(dmtb_thuoctinh ltb) {
        String sql = "UPDATE loaitb SET tenloai=? WHERE maloai=?";
        try {
            db database = new db();
            Connection c = database.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, ltb.getTenloai());
            ps.setString(2, ltb.getMaloai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // XÓA LOẠI THIẾT BỊ
    // =========================
    public boolean delete(String maloai) {
        String sql = "DELETE FROM loaitb WHERE maloai=?";
        try {
            db database = new db();
            Connection c = database.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, maloai);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // LẤY DANH SÁCH
    // =========================
    public List<dmtb_thuoctinh> getAll() {
        List<dmtb_thuoctinh> list = new ArrayList<>();
        String sql = "SELECT * FROM loaitb";

        try {
            db database = new db();
            Connection c = database.getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new dmtb_thuoctinh(
                        rs.getString("maloai"),
                        rs.getString("tenloai")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================
    // TÌM THEO MÃ
    // =========================
    public dmtb_thuoctinh findById(String maloai) {
        String sql = "SELECT * FROM loaitb WHERE maloai=?";
        try {
            db database = new db();
            Connection c = database.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, maloai);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new dmtb_thuoctinh(
                        rs.getString("maloai"),
                        rs.getString("tenloai")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //kiểm tra trung mã
  public boolean trungma(String maloai) {
        String sql = "SELECT * FROM loaitb WHERE maloai=?";
        try {
            db database = new db();
            Connection c = database.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, maloai);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
               return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
  public dmtb_thuoctinh findByTen(String tenLoai) {
    String sql = "SELECT * FROM loaitb WHERE tenloai = ?";

    try (Connection c = new db().getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, tenLoai);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new dmtb_thuoctinh(
                rs.getString("maloai"),
                rs.getString("tenloai")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null; // chưa tồn tại
}
public String generateMaLoai() {
    String sql = "SELECT maloai FROM loaitb ORDER BY maloai DESC LIMIT 1";

    try (
        Connection c = new db().getConnection();
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(sql)
    ) {
        if (rs.next()) {
            String lastMa = rs.getString("maloai"); // VD: DM005
            int so = Integer.parseInt(lastMa.substring(2));
            return String.format("DM%03d", so + 1);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return "DM001"; // nếu bảng đang rỗng
}


}
 

