package com.mycompany.qltb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuMuonDao {

    // 1. Lấy danh sách thiết bị đang SẴN SÀNG
    public List<qltb_thuoctinh> getThietBiSanSang() {
        List<qltb_thuoctinh> list = new ArrayList<>();
        // Chỉ lấy những thiết bị còn số lượng tốt > 0 và trạng thái Sẵn sàng (1), loại trừ trạng thái Dừng hoạt động (8)
        String sql = "SELECT * FROM thietbi WHERE trangthai = 1 AND soluong_tot > 0 AND trangthai != 8";

        try (Connection c = new db().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                qltb_thuoctinh tb = new qltb_thuoctinh();
                tb.setMaTB(rs.getString("matbi"));
                tb.setTenTB(rs.getString("tentbi"));
                tb.setMaLoai(rs.getString("maloai"));
                tb.setSoLuongTot(rs.getInt("soluong_tot"));
                tb.setSoLuongHong(rs.getInt("soluong_hong"));
                list.add(tb);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    public boolean hoanTatTraPhieu(int maPM, List<TraPhieuDialog.ChiTietTra> listTra) {
        Connection c = new db().getConnection();
        PreparedStatement psPhieu = null;
        PreparedStatement psUpdateKho = null;
        PreparedStatement psUpdateCT = null;

        try {
            c.setAutoCommit(false);

            // 1. Đánh dấu Phiếu là Đã trả
            String sqlPhieu = "UPDATE phieumuon SET ngaytra = NOW() WHERE mapm = ?";
            psPhieu = c.prepareStatement(sqlPhieu);
            psPhieu.setInt(1, maPM);
            psPhieu.executeUpdate();

            // Chuẩn bị câu lệnh update
           // Khi trả: số lượng tốt -> cộng vào soluong_tot, số lượng hỏng -> cộng vào soluong_hong
           String sqlUpdateKho = "UPDATE thietbi SET soluong_tot = soluong_tot + ?, soluong_hong = soluong_hong + ? WHERE matbi = ?";
            
            // Update chi tiết phiếu (Ghi nhận trạng thái trả vào cột ghi chú hoặc cột riêng nếu có)
            // Ở đây mình sẽ ghép chuỗi "Tốt: x, Hỏng: y" vào cột ghichu của ctphieumuon để lưu vết
            String sqlUpdateCT = "UPDATE ctphieumuon SET trangthai_luc_tra = ?, ghichu = ? WHERE mapm = ? AND matbi = ?";

            psUpdateKho = c.prepareStatement(sqlUpdateKho);
            psUpdateCT = c.prepareStatement(sqlUpdateCT);

            for (TraPhieuDialog.ChiTietTra item : listTra) {
                // 2. CỘNG KHO (Cộng số lượng TỐT vào soluong_tot, số lượng HỎNG vào soluong_hong)
                psUpdateKho.setInt(1, item.slTot);  // Cộng vào soluong_tot
                psUpdateKho.setInt(2, item.slHong); // Cộng vào soluong_hong
                psUpdateKho.setString(3, item.maTB);
                psUpdateKho.addBatch();

                // 3. Update trạng thái chi tiết
                // 1 = Bình thường, 3 = Có hỏng hóc
                int trangThaiTongQuat = (item.slHong > 0) ? 3 : 1; 
                String ghiChuTra = "Trả: " + item.slTot + " Tốt, " + item.slHong + " Hỏng. " + item.ghiChu;
                
                // Nếu bảng ctphieumuon của bạn chưa có cột 'ghichu', bạn cần thêm vào DB hoặc bỏ dòng này
                // ALTER TABLE ctphieumuon ADD COLUMN ghichu VARCHAR(255);
                
                psUpdateCT.setInt(1, trangThaiTongQuat);
                psUpdateCT.setString(2, ghiChuTra);
                psUpdateCT.setInt(3, maPM);
                psUpdateCT.setString(4, item.maTB);
                psUpdateCT.addBatch();
            }

            psUpdateKho.executeBatch();
            psUpdateCT.executeBatch();

            c.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (c != null) c.rollback(); } catch (Exception ex) {}
            return false;
        } finally {
            try { if (c != null) c.close(); } catch (Exception e) {}
        }
    }
    // 2. TẠO PHIẾU MƯỢN
    public boolean taoPhieuMuon(PhieuMuon pm, java.util.Map<String, Integer> mapThietBi) {
    Connection c = new db().getConnection();
    PreparedStatement psPhieu = null;
    PreparedStatement psChiTiet = null;
    PreparedStatement psUpdateTB = null;

    try {
        c.setAutoCommit(false);

        // A. Insert PHIEUMUON
        String sqlPhieu = "INSERT INTO phieumuon(mahs, malop, maphong, hantra, ghichu) VALUES (?, ?, ?, ?, ?)";
        psPhieu = c.prepareStatement(sqlPhieu, Statement.RETURN_GENERATED_KEYS);
        psPhieu.setString(1, pm.getMaHS());
        psPhieu.setString(2, pm.getMaLop());
        psPhieu.setString(3, pm.getMaPhong());
        if (pm.getHanTra() != null) {
            psPhieu.setTimestamp(4, pm.getHanTra());
        } else {
            System.out.println("CẢNH BÁO: Hạn trả bị Null, đang lấy ngày hiện tại.");
            psPhieu.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
        }
        psPhieu.setString(5, pm.getGhiChu());

        if (psPhieu.executeUpdate() == 0) throw new SQLException("Tạo phiếu thất bại.");

        int maPM = 0;
        try (ResultSet generatedKeys = psPhieu.getGeneratedKeys()) {
            if (generatedKeys.next()) maPM = generatedKeys.getInt(1);
        }

        // B. Insert CTPHIEUMUON (Thêm cột soluong) & Trừ Tồn Kho
        String sqlChiTiet = "INSERT INTO ctphieumuon(mapm, matbi, soluong, trangthai_luc_muon) VALUES (?, ?, ?, 1)";
        // SQL trừ tồn kho (chỉ trừ từ soluong_tot khi mượn)
        String sqlUpdateTB = "UPDATE thietbi SET soluong_tot = soluong_tot - ? WHERE matbi = ?";

        psChiTiet = c.prepareStatement(sqlChiTiet);
        psUpdateTB = c.prepareStatement(sqlUpdateTB);

        // Duyệt qua Map (Key là Mã TB, Value là Số lượng)
        for (java.util.Map.Entry<String, Integer> entry : mapThietBi.entrySet()) {
            String maTB = entry.getKey();
            int slMuon = entry.getValue();

            // Insert Chi tiết
            psChiTiet.setInt(1, maPM);
            psChiTiet.setString(2, maTB);
            psChiTiet.setInt(3, slMuon); // Lưu số lượng mượn
            psChiTiet.addBatch();

            // Trừ kho
            psUpdateTB.setInt(1, slMuon);
            psUpdateTB.setString(2, maTB);
            psUpdateTB.addBatch();
        }

        psChiTiet.executeBatch();
        psUpdateTB.executeBatch();

        c.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        try { if (c != null) c.rollback(); } catch (SQLException ex) {}
        return false;
    } finally {
        // ... (giữ nguyên phần đóng kết nối)
        try { if (c != null) c.close(); } catch (Exception e) {}
    }
}
    public List<String> getAllMaPhong() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT maphong FROM phong";
        try (Connection c = new db().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("maphong"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // 3. TÌM KIẾM
    public List<PhieuMuon> findPhieuMuon(String keyword, int status, int month, int year) {
        List<PhieuMuon> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT pm.*, hs.tenhs FROM phieumuon pm " +
            "JOIN hocsinh hs ON pm.mahs = hs.mahs " +
            "WHERE hs.tenhs LIKE ? "
        );

        if (status == 1) sql.append(" AND pm.ngaytra IS NULL");
        else if (status == 2) sql.append(" AND pm.ngaytra IS NOT NULL");

        if (month > 0) sql.append(" AND MONTH(pm.ngaymuon) = ").append(month);
        if (year > 0) sql.append(" AND YEAR(pm.ngaymuon) = ").append(year);

        sql.append(" ORDER BY pm.ngaymuon DESC");

        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            
            ps.setString(1, "%" + keyword + "%");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuMuon pm = new PhieuMuon();
                pm.setMaPM(rs.getInt("mapm"));
                pm.setMaHS(rs.getString("mahs"));
                pm.setTenHS(rs.getString("tenhs"));
                pm.setMaLop(rs.getString("malop"));
                pm.setMaPhong(rs.getString("maphong"));
                pm.setNgayMuon(rs.getTimestamp("ngaymuon"));
                pm.setHanTra(rs.getTimestamp("hantra"));
                pm.setNgayTra(rs.getTimestamp("ngaytra"));
                pm.setGhiChu(rs.getString("ghichu"));
                list.add(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. FIND BY ID
    public PhieuMuon findById(int maPM) {
        String sql = "SELECT pm.*, hs.tenhs FROM phieumuon pm " +
                     "JOIN hocsinh hs ON pm.mahs = hs.mahs " +
                     "WHERE pm.mapm = ?";
        
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, maPM);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                PhieuMuon pm = new PhieuMuon();
                pm.setMaPM(rs.getInt("mapm"));
                pm.setMaHS(rs.getString("mahs"));
                pm.setTenHS(rs.getString("tenhs"));
                pm.setMaLop(rs.getString("malop"));
                pm.setMaPhong(rs.getString("maphong"));
                pm.setNgayMuon(rs.getTimestamp("ngaymuon"));
                pm.setHanTra(rs.getTimestamp("hantra"));
                pm.setNgayTra(rs.getTimestamp("ngaytra"));
                pm.setGhiChu(rs.getString("ghichu"));
                return pm;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5. [QUAN TRỌNG] LẤY DANH SÁCH THIẾT BỊ THEO PHIẾU
    public List<qltb_thuoctinh> getThietBiMuonByPhieu(int maPM) {
        List<qltb_thuoctinh> list = new ArrayList<>();

        // [CẬP NHẬT SQL] Lấy thêm ct.ghichu (để biết tình trạng trả) và ct.soluong
        String sql = "SELECT tb.matbi, tb.tentbi, tb.maloai, lt.tenloai, " +
                     "ct.soluong, ct.ghichu AS tinhtrang " + // Lấy ghichu đặt tên là tinhtrang
                     "FROM ctphieumuon ct " +
                     "JOIN thietbi tb ON ct.matbi = tb.matbi " +
                     "JOIN loaitb lt ON tb.maloai = lt.maloai " +
                     "WHERE ct.mapm = ?";

        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, maPM);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                qltb_thuoctinh tb = new qltb_thuoctinh();
                tb.setMaTB(rs.getString("matbi"));
                tb.setTenTB(rs.getString("tentbi"));
                tb.setMaLoai(rs.getString("tenloai"));
                tb.setSoLuongTot(rs.getInt("soluong")); // Số lượng mượn (lưu vào soLuongTot tạm thời)
                tb.setSoLuongHong(0); // Không có thông tin hỏng khi mượn

                // Tận dụng trường Ghi chú của đối tượng để lưu tình trạng trả
                // Nếu null (chưa trả) thì để trống hoặc ghi "Đang mượn"
                String tt = rs.getString("tinhtrang");
                tb.setGhiChu(tt == null ? "Đang mượn" : tt);

                list.add(tb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. UPDATE
    public boolean updatePhieuMuon(PhieuMuon pm) {
        String sql = "UPDATE phieumuon SET mahs=?, malop=?, maphong=?, hantra=?, ghichu=? WHERE mapm=?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, pm.getMaHS());
            ps.setString(2, pm.getMaLop());
            ps.setString(3, pm.getMaPhong());
            ps.setTimestamp(4, pm.getHanTra());
            ps.setString(5, pm.getGhiChu());
            ps.setInt(6, pm.getMaPM());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getMaLopByHS(String maHS) {
        String sql = "SELECT malop FROM hocsinh WHERE mahs = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maHS);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("malop");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }
    public boolean traPhieuMuon(int maPM, int tinhTrang) {
        Connection c = new db().getConnection();
        PreparedStatement psUpdatePhieu = null;
        PreparedStatement psUpdateCT = null;
        PreparedStatement psUpdateTB = null;

        try {
            c.setAutoCommit(false); // Bắt đầu giao dịch

            // 1. Cập nhật ngày trả cho PHIEUMUON (Đánh dấu đã trả)
            String sqlPhieu = "UPDATE phieumuon SET ngaytra = NOW() WHERE mapm = ?";
            psUpdatePhieu = c.prepareStatement(sqlPhieu);
            psUpdatePhieu.setInt(1, maPM);
            psUpdatePhieu.executeUpdate();

            // 2. Cập nhật trạng thái lúc trả cho CTPHIEUMUON
            String sqlCT = "UPDATE ctphieumuon SET trangthai_luc_tra = ? WHERE mapm = ?";
            psUpdateCT = c.prepareStatement(sqlCT);
            psUpdateCT.setInt(1, tinhTrang);
            psUpdateCT.setInt(2, maPM);
            psUpdateCT.executeUpdate();

            // 3. Cập nhật trạng thái cho THIETBI 
            // Nếu trả bình thường (1) -> Thiết bị thành Sẵn sàng (1)
            // Nếu trả báo hỏng (3) -> Thiết bị thành Hỏng (3)
            // Lấy danh sách thiết bị trong phiếu để update
            String sqlGetTB = "SELECT matbi FROM ctphieumuon WHERE mapm = ?";
            PreparedStatement psGetTB = c.prepareStatement(sqlGetTB);
            psGetTB.setInt(1, maPM);
            ResultSet rs = psGetTB.executeQuery();

            String sqlUpdateTB = "UPDATE thietbi SET trangthai = ? WHERE matbi = ?";
            psUpdateTB = c.prepareStatement(sqlUpdateTB);

            while (rs.next()) {
                String maTB = rs.getString("matbi");
                psUpdateTB.setInt(1, tinhTrang); // 1 hoặc 3
                psUpdateTB.setString(2, maTB);
                psUpdateTB.addBatch();
            }
            psUpdateTB.executeBatch();

            c.commit(); // Lưu tất cả thay đổi
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (c != null) c.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (c != null) c.close(); } catch (Exception e) {}
        }
    }

    // Phương thức bổ sung để lấy tên lớp và phòng
    public List<String> getAllTenLop() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT tenlop FROM lop";
        try (Connection c = new db().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("tenlop"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllTenPhong() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT tenphong FROM phong";
        try (Connection c = new db().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("tenphong"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getTenLopByMa(String maLop) {
        String sql = "SELECT tenlop FROM lop WHERE malop = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maLop);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tenlop");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTenPhongByMa(String maPhong) {
        String sql = "SELECT tenphong FROM phong WHERE maphong = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tenphong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMaLopByTen(String tenLop) {
        String sql = "SELECT malop FROM lop WHERE tenlop = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("malop");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMaPhongByTen(String tenPhong) {
        String sql = "SELECT maphong FROM phong WHERE tenphong = ?";
        try (Connection c = new db().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tenPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maphong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}