package com.mycompany.qltb;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TaoPhieuMuonFrm extends JFrame {

    private JTextField txtMaHS, txtHanTra, txtGhiChu;
    private JComboBox<String> cboTenPhong; // Chọn tên phòng
    
    private JTable tblKho, tblChon;
    private DefaultTableModel modelKho, modelChon;
    private JButton btnAdd, btnRemove, btnSave, btnCancel;
    
    private PhieuMuonDao dao = new PhieuMuonDao();
    private QuanLyPhieuMuon parentScreen; 

    public TaoPhieuMuonFrm(QuanLyPhieuMuon parent) {
        this.parentScreen = parent;
        initComponents();
        loadComboboxPhong();
        loadKhoThietBi();
    }
    
    private void loadComboboxPhong() {
        cboTenPhong.removeAllItems();
        List<String> listPhong = dao.getAllTenPhong();
        for (String p : listPhong) {
            cboTenPhong.addItem(p);
        }
    }

    private void initComponents() {
        setTitle("LẬP PHIẾU MƯỢN THIẾT BỊ");
        setSize(1000, 600); // Tăng chiều rộng một chút
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // --- TOP: Form Thông tin ---
        JPanel pTop = new JPanel(new GridLayout(2, 4, 10, 10)); 
        pTop.setBorder(BorderFactory.createTitledBorder("Thông tin mượn"));
        
        txtMaHS = new JTextField();
        cboTenPhong = new JComboBox<>();
        txtHanTra = new JTextField();
        txtHanTra.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 86400000)));
        txtGhiChu = new JTextField();

        pTop.add(new JLabel("Mã Học Sinh:")); pTop.add(txtMaHS);
        pTop.add(new JLabel("Phòng:"));       pTop.add(cboTenPhong);
        pTop.add(new JLabel("Hạn Trả:"));     pTop.add(txtHanTra);
        pTop.add(new JLabel("Ghi Chú:"));     pTop.add(txtGhiChu);
        
        add(pTop, BorderLayout.NORTH);

        // --- CENTER: Hai bảng ---
        JPanel pCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // --- Bảng Trái: Kho thiết bị ---
        JPanel pLeft = new JPanel(new BorderLayout());
        pLeft.setBorder(BorderFactory.createTitledBorder("Danh sách thiết bị SẴN SÀNG"));
        
        // [CẬP NHẬT] Thêm cột "Tồn kho"
        modelKho = new DefaultTableModel(new String[]{"Mã TB", "Tên TB", "Tồn kho"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblKho = new JTable(modelKho);
        pLeft.add(new JScrollPane(tblKho), BorderLayout.CENTER);
        btnAdd = new JButton("Chọn >>");
        pLeft.add(btnAdd, BorderLayout.SOUTH);

        // --- Bảng Phải: Thiết bị đã chọn ---
        JPanel pRight = new JPanel(new BorderLayout());
        pRight.setBorder(BorderFactory.createTitledBorder("Thiết bị ĐƯỢC MƯỢN"));
        
        // [CẬP NHẬT] Thêm cột "SL Mượn"
        modelChon = new DefaultTableModel(new String[]{"Mã TB", "Tên TB", "SL Mượn"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblChon = new JTable(modelChon);
        pRight.add(new JScrollPane(tblChon), BorderLayout.CENTER);
        btnRemove = new JButton("<< Bỏ chọn");
        pRight.add(btnRemove, BorderLayout.SOUTH);

        pCenter.add(pLeft);
        pCenter.add(pRight);
        add(pCenter, BorderLayout.CENTER);

        // --- BOTTOM: Nút Lưu ---
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("LƯU PHIẾU MƯỢN");
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.setBackground(new Color(50, 205, 50));
        btnSave.setForeground(Color.WHITE);
        btnCancel = new JButton("Hủy bỏ");

        pBottom.add(btnSave);
        pBottom.add(btnCancel);
        add(pBottom, BorderLayout.SOUTH);

        // ==========================================
        // XỬ LÝ SỰ KIỆN LOGIC SỐ LƯỢNG
        // ==========================================

        // 1. Nút Chọn (>>)
        btnAdd.addActionListener(e -> {
            int row = tblKho.getSelectedRow();
            if (row != -1) {
                String ma = modelKho.getValueAt(row, 0).toString();
                String ten = modelKho.getValueAt(row, 1).toString();
                // Lấy số lượng tồn kho hiện tại
                int tonKho = Integer.parseInt(modelKho.getValueAt(row, 2).toString());

                // Nếu hết hàng thì báo lỗi
                if (tonKho <= 0) {
                    JOptionPane.showMessageDialog(this, "Sản phẩm này đã hết hàng!");
                    return;
                }

                // Hiện hộp thoại nhập số lượng
                String slStr = JOptionPane.showInputDialog(this, "Nhập số lượng muốn mượn (Tồn: " + tonKho + "):");
                
                if (slStr != null && !slStr.isEmpty()) {
                    try {
                        int slMuon = Integer.parseInt(slStr);
                        
                        if (slMuon <= 0) {
                            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                        } else if (slMuon > tonKho) {
                            JOptionPane.showMessageDialog(this, "Không đủ hàng trong kho! Chỉ còn " + tonKho);
                        } else {
                            // Kiểm tra xem mã này đã có bên phải chưa để cộng dồn
                            boolean daCo = false;
                            for (int i = 0; i < modelChon.getRowCount(); i++) {
                                if (modelChon.getValueAt(i, 0).toString().equals(ma)) {
                                    int slCu = Integer.parseInt(modelChon.getValueAt(i, 2).toString());
                                    modelChon.setValueAt(slCu + slMuon, i, 2); // Cộng dồn SL mượn
                                    daCo = true;
                                    break;
                                }
                            }

                            if (!daCo) {
                                // Thêm dòng mới bên phải
                                modelChon.addRow(new Object[]{ma, ten, slMuon});
                            }
                            
                            // Trừ số lượng bên trái (Visual update)
                            modelKho.setValueAt(tonKho - slMuon, row, 2);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị từ danh sách bên trái!");
            }
        });

        // 2. Nút Bỏ chọn (<<)
        btnRemove.addActionListener(e -> {
            int row = tblChon.getSelectedRow();
            if (row != -1) {
                String ma = modelChon.getValueAt(row, 0).toString();
                int slTraLai = Integer.parseInt(modelChon.getValueAt(row, 2).toString());
                
                // Xóa khỏi bảng phải
                modelChon.removeRow(row);
                
                // Tìm dòng tương ứng bên trái để cộng lại số lượng
                for (int i = 0; i < modelKho.getRowCount(); i++) {
                    if (modelKho.getValueAt(i, 0).toString().equals(ma)) {
                        int hienTai = Integer.parseInt(modelKho.getValueAt(i, 2).toString());
                        modelKho.setValueAt(hienTai + slTraLai, i, 2);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị cần bỏ chọn!");
            }
        });

        btnSave.addActionListener((ActionEvent e) -> savePhieuMuon());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadKhoThietBi() {
        modelKho.setRowCount(0);
        List<qltb_thuoctinh> list = dao.getThietBiSanSang();
        for (qltb_thuoctinh tb : list) {
            // Hiển thị cả số lượng tồn kho (chỉ hiển thị SL tốt có thể mượn)
            modelKho.addRow(new Object[]{tb.getMaTB(), tb.getTenTB(), tb.getSoLuongTot()});
        }
    }

    private void savePhieuMuon() {
    // 1. Kiểm tra thông tin cơ bản
        String maHS = txtMaHS.getText().trim();
        if (maHS.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Học Sinh!");
            return;
        }

        // Kiểm tra phòng
        if (cboTenPhong.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Phòng!");
            return;
        }

        // Kiểm tra danh sách thiết bị
        if (modelChon.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn thiết bị nào!");
            return;
        }

        // 2. Lấy Mã Lớp
        String maLop = dao.getMaLopByHS(maHS);
        if (maLop == null) {
            JOptionPane.showMessageDialog(this, "Mã học sinh không tồn tại!");
            return;
        }

        try {
            // Khởi tạo đối tượng
            PhieuMuon pm = new PhieuMuon();
            pm.setMaHS(maHS);
            pm.setMaLop(maLop);
            // Lấy mã phòng từ tên phòng được chọn
            String tenPhong = cboTenPhong.getSelectedItem().toString();
            String maPhong = dao.getMaPhongByTen(tenPhong);
            pm.setMaPhong(maPhong);
            pm.setGhiChu(txtGhiChu.getText().trim());

            // --- [QUAN TRỌNG] XỬ LÝ NGÀY THÁNG (yyyy-MM-dd) ---
            String dateStr = txtHanTra.getText().trim();
            System.out.println("Ngày nhập vào: " + dateStr); // In ra để debug

            if (dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hạn trả không được để trống!");
                return;
            }

            try {
                // Định dạng đúng theo yêu cầu của bạn: yyyy-MM-dd
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false); // Bắt buộc nhập đúng ngày chuẩn (không chấp nhận ngày 32/13...)

                java.util.Date date = sdf.parse(dateStr); // Chuyển từ String sang Date

                // Chuyển sang Timestamp để lưu vào DB (Cực kỳ quan trọng)
                pm.setHanTra(new java.sql.Timestamp(date.getTime())); 

            } catch (Exception ex) {
                ex.printStackTrace(); // In lỗi ra console để xem
                JOptionPane.showMessageDialog(this, "Ngày sai định dạng! Vui lòng nhập: yyyy-MM-dd (VD: 2025-12-25)");
                return; // <--- DỪNG NGAY LẬP TỨC nếu ngày sai
            }
            // ----------------------------------------------------

            // 3. Gom danh sách thiết bị + số lượng
            java.util.Map<String, Integer> mapThietBi = new java.util.HashMap<>();
            for (int i = 0; i < modelChon.getRowCount(); i++) {
                String maTB = modelChon.getValueAt(i, 0).toString();
                int sl = Integer.parseInt(modelChon.getValueAt(i, 2).toString());
                mapThietBi.put(maTB, sl);
            }

            // 4. Gọi DAO để lưu
            if (dao.taoPhieuMuon(pm, mapThietBi)) {
                JOptionPane.showMessageDialog(this, "Tạo phiếu thành công!");
                if (parentScreen != null) parentScreen.loadData();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi Database! Không thể lưu phiếu.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage());
        }
    }
}