/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

import com.mycompany.qltb.KhuVucConstructor.KhuVuc;
import com.mycompany.qltb.PhongConstructor.Phong;
import com.mycompany.qltb.TrangThaiConstructor.TrangThai;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class qlp_dao {

    // Hàm lấy kết nối CSDL (Bạn sửa lại thông tin user/pass/db name cho đúng)
    private Connection getConnection() throws SQLException {
        // Điền thông tin kết nối của bạn vào đây
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/qltb_thpt", "root", ""); 
    }

    // --- MỚI: Hàm lấy danh sách Trạng Thái ---
    public List<TrangThai> getAllTrangThai() {
        List<TrangThai> list = new ArrayList<>();
        String sql = "SELECT * FROM trangthai"; 
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Giả sử cột là 'matinhtrang' và 'tentinhtrang'
                list.add(new TrangThai(rs.getString("matinhtrang"), rs.getString("tentinhtrang")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // --- CẬP NHẬT: Lấy danh sách phòng (kèm tên Trạng thái) ---
    public List<Phong> getAllPhong() {
        List<Phong> list = new ArrayList<>();
        // JOIN thêm bảng trangthai
        String sql = "SELECT p.maphong, p.tenphong, p.makhuVuc, k.tenkhuvuc, p.trangthai, t.tentinhtrang " +
                     "FROM phong p " +
                     "INNER JOIN khuvuc k ON p.makhuvuc = k.makhuvuc " +
                     "LEFT JOIN trangthai t ON p.trangthai = t.matinhtrang"; // JOIN bảng trạng thái

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                KhuVuc kv = new KhuVuc(rs.getString("makhuvuc"), rs.getString("tenkhuvuc"));
                // Tạo đối tượng Trạng thái từ kết quả select
                TrangThai tt = new TrangThai(rs.getString("trangthai"), rs.getString("tentinhtrang"));
                
                // Constructor Phong mới (4 tham số)
                list.add(new Phong(rs.getString("maphong"), rs.getString("tenphong"), kv, tt));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // --- CẬP NHẬT: Insert thêm cột trạng thái ---
    public boolean insert(Phong p) {
        String sql = "INSERT INTO phong (maphong, tenphong, makhuvuc, trangthai) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getMaPhong());
            pstmt.setString(2, p.getTenPhong());
            pstmt.setString(3, p.getKhuVuc().getMaKhuVuc());
            pstmt.setString(4, p.getTrangThai().getMaTinhTrang()); // Insert mã tình trạng
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // --- CẬP NHẬT: Update thêm cột trạng thái ---
    public boolean update(Phong p) {
        String sql = "UPDATE phong SET tenphong = ?, makhuvuc = ?, trangthai = ? WHERE maphong = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getTenPhong());
            pstmt.setString(2, p.getKhuVuc().getMaKhuVuc());
            pstmt.setString(3, p.getTrangThai().getMaTinhTrang()); // Update mã tình trạng
            pstmt.setString(4, p.getMaPhong());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 1. Lấy danh sách Khu Vực (Dùng để đổ vào ComboBox)
    public List<KhuVuc> getAllKhuVuc() {
        List<KhuVuc> list = new ArrayList<>();
        String sql = "SELECT * FROM khuvuc"; // Giả sử bảng tên là 'khuvuc'

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maKv = rs.getString("makhuvuc");
                String tenKv = rs.getString("tenkhuvuc");
                
                // Vì KhuVuc là static inner class, ta khởi tạo bình thường
                // Nếu không static, cần: new KhuVucConstructor().new KhuVuc(...)
                KhuVuc kv = new KhuVuc(maKv, tenKv);
                list.add(kv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5. Xóa Phòng
    public boolean delete(String maphong) {
        String sql = "DELETE FROM phong WHERE maphong = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maphong);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 6. Kiểm tra mã phòng đã tồn tại chưa (Để validate khi thêm)
    public boolean isExist(String maphong) {
        String sql = "SELECT count(*) FROM phong WHERE maphong = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maphong);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean insertKhuVuc(KhuVuc kv) {
        String sql = "INSERT INTO khuvuc (maKhuVuc, tenKhuVuc) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kv.getMaKhuVuc());
            pstmt.setString(2, kv.getTenKhuVuc());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 2. Sửa Khu Vực
    public boolean updateKhuVuc(KhuVuc kv) {
        String sql = "UPDATE khuvuc SET tenKhuVuc = ? WHERE maKhuVuc = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kv.getTenKhuVuc());
            pstmt.setString(2, kv.getMaKhuVuc());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 3. Xóa Khu Vực
    public boolean deleteKhuVuc(String maKhuVuc) {
        // Lưu ý: Nếu khu vực đang có phòng thì CSDL có thể chặn xóa (Foreign Key)
        String sql = "DELETE FROM khuvuc WHERE maKhuVuc = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhuVuc);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
            // Thường lỗi do ràng buộc khóa ngoại
        }
        return false;
    }

    // 4. Kiểm tra trùng mã khu vực
    public boolean isKhuVucExist(String maKhuVuc) {
        String sql = "SELECT count(*) FROM khuvuc WHERE maKhuVuc = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhuVuc);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}