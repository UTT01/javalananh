package com.mycompany.qltb;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TraPhieuDialog extends JDialog {

    private JTable tblChiTiet;
    private DefaultTableModel model;
    private JButton btnLuu, btnHuy;
    private PhieuMuonDao dao = new PhieuMuonDao();
    private int maPM;
    private QuanLyPhieuMuon parent;

    public TraPhieuDialog(QuanLyPhieuMuon parent, int maPM) {
        super(parent, "CHI TIẾT TRẢ THIẾT BỊ - Phiếu " + maPM, true);
        this.parent = parent;
        this.maPM = maPM;
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 1. Tạo bảng nhập liệu
        // Cột 0,1,2: Ko sửa. Cột 3 (SL Tốt): Sửa được. Cột 4 (SL Hỏng): Tự tính
        model = new DefaultTableModel(new String[]{"Mã TB", "Tên TB", "SL Mượn", "SL Trả Tốt (Nhập)", "SL Hỏng (Tự tính)", "Ghi chú"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 3 || col == 5; // Chỉ cho sửa cột SL Tốt và Ghi chú
            }
        };

        tblChiTiet = new JTable(model);
        tblChiTiet.setRowHeight(30);
        
        // Sự kiện: Khi nhập SL Tốt -> Tự tính SL Hỏng
        tblChiTiet.addPropertyChangeListener(e -> {
            if ("tableCellEditor".equals(e.getPropertyName())) {
                if (!tblChiTiet.isEditing()) { // Kết thúc sửa
                    updateSlHong();
                }
            }
        });

        add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        // 2. Nút bấm
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("HOÀN TẤT TRẢ");
        btnLuu.setBackground(new Color(0, 153, 76));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnHuy = new JButton("Hủy");

        pBottom.add(btnLuu);
        pBottom.add(btnHuy);
        add(pBottom, BorderLayout.SOUTH);

        // 3. Load dữ liệu
        loadData();

        // 4. Sự kiện
        btnLuu.addActionListener(e -> xuLyTra());
        btnHuy.addActionListener(e -> dispose());
    }

    private void loadData() {
        // Lấy danh sách thiết bị trong phiếu
        List<qltb_thuoctinh> list = dao.getThietBiMuonByPhieu(maPM);
        for (qltb_thuoctinh tb : list) {
            model.addRow(new Object[]{
                tb.getMaTB(),
                tb.getTenTB(),
                tb.getSoLuong(), // Đây là số lượng ĐÃ MƯỢN lấy từ ctphieumuon
                tb.getSoLuong(), // Mặc định SL Tốt = SL Mượn
                0,               // Mặc định SL Hỏng = 0
                ""
            });
        }
    }

    private void updateSlHong() {
        int row = tblChiTiet.getSelectedRow();
        if (row != -1) {
            try {
                int slMuon = Integer.parseInt(model.getValueAt(row, 2).toString());
                int slTot = Integer.parseInt(model.getValueAt(row, 3).toString());

                if (slTot < 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng không được âm!");
                    model.setValueAt(slMuon, row, 3); // Reset
                    return;
                }
                if (slTot > slMuon) {
                    JOptionPane.showMessageDialog(this, "SL Trả Tốt không được lớn hơn SL Mượn!");
                    model.setValueAt(slMuon, row, 3); // Reset
                    return;
                }

                // Tính SL Hỏng
                model.setValueAt(slMuon - slTot, row, 4);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên!");
            }
        }
    }

    private void xuLyTra() {
        // Gom dữ liệu từ bảng
        List<ChiTietTra> listTra = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String maTB = model.getValueAt(i, 0).toString();
            int slTot = Integer.parseInt(model.getValueAt(i, 3).toString());
            int slHong = Integer.parseInt(model.getValueAt(i, 4).toString());
            String ghiChu = model.getValueAt(i, 5) != null ? model.getValueAt(i, 5).toString() : "";
            
            listTra.add(new ChiTietTra(maTB, slTot, slHong, ghiChu));
        }

        // Gọi DAO update
        if (dao.hoanTatTraPhieu(maPM, listTra)) {
            JOptionPane.showMessageDialog(this, "Trả phiếu thành công!\nKho đã được cập nhật.");
            parent.loadData(); // Refresh màn hình cha
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi trả phiếu!");
        }
    }
    
    // Class phụ để lưu dữ liệu trả
    public static class ChiTietTra {
        public String maTB;
        public int slTot;
        public int slHong;
        public String ghiChu;

        public ChiTietTra(String maTB, int slTot, int slHong, String ghiChu) {
            this.maTB = maTB;
            this.slTot = slTot;
            this.slHong = slHong;
            this.ghiChu = ghiChu;
        }
    }
}