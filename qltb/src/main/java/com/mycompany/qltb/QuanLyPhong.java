package com.mycompany.qltb;

import com.mycompany.qltb.KhuVucConstructor.KhuVuc;
import com.mycompany.qltb.PhongConstructor.Phong;
import com.mycompany.qltb.TrangThaiConstructor.TrangThai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class QuanLyPhong extends JFrame {

    // Components
    private JList<KhuVuc> listKhuVuc;
    private DefaultListModel<KhuVuc> listModelKhuVuc;
    private JTable tablePhong;
    private DefaultTableModel tableModelPhong;
    
    private JComboBox<KhuVuc> cboKhuVuc;
    private JComboBox<TrangThai> cboTrangThai;
    
    private JTextField txtMaPhong, txtTenPhong;
    
    // Các nút quản lý PHÒNG
    private JButton btnThem, btnSua, btnXoa, btnLamMoi; 
    
    // Các nút quản lý KHU VỰC (MỚI)
    private JButton btnThemKv, btnSuaKv, btnXoaKv;

    public QuanLyPhong() {
        setTitle("Quản lý Phòng");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

        // Load dữ liệu
        loadDataToKhuVucList();
        loadDataToComboBox();
        loadDataToTrangThaiComboBox(); 
        loadAllPhongToTable();
    }

    private void initUI() {
        // --- PHẦN TRÁI: KHU VỰC ---
        listModelKhuVuc = new DefaultListModel<>();
        listKhuVuc = new JList<>(listModelKhuVuc);
        listKhuVuc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setPreferredSize(new Dimension(280, 0)); 
        pnlLeft.setBorder(new TitledBorder("Danh mục Khu vực"));
        pnlLeft.add(new JScrollPane(listKhuVuc), BorderLayout.CENTER);

        // Nút bấm Khu vực (MỚI)
        JPanel pnlLeftButtons = new JPanel(new FlowLayout());
        btnThemKv = new JButton("Thêm KV");
        btnSuaKv = new JButton("Sửa KV");
        btnXoaKv = new JButton("Xóa KV");
        pnlLeftButtons.add(btnThemKv);
        pnlLeftButtons.add(btnSuaKv);
        pnlLeftButtons.add(btnXoaKv);
        pnlLeft.add(pnlLeftButtons, BorderLayout.SOUTH);

        // --- PHẦN PHẢI: PHÒNG ---
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBorder(new TitledBorder("Thông tin chi tiết Phòng"));

        // Bảng Phòng
        String[] columns = {"Mã Phòng", "Tên Phòng", "Khu Vực", "Trạng Thái"};
        tableModelPhong = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablePhong = new JTable(tableModelPhong);
        tablePhong.setRowHeight(25);
        pnlRight.add(new JScrollPane(tablePhong), BorderLayout.CENTER);

        // Form Nhập liệu
        JPanel pnlFormContainer = new JPanel(new BorderLayout());
        JPanel pnlFormInput = new JPanel(new GridLayout(4, 2, 10, 10)); 
        pnlFormInput.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnlFormInput.add(new JLabel("Khu vực:"));
        cboKhuVuc = new JComboBox<>();
        pnlFormInput.add(cboKhuVuc);

        pnlFormInput.add(new JLabel("Mã phòng:"));
        txtMaPhong = new JTextField();
        pnlFormInput.add(txtMaPhong);

        pnlFormInput.add(new JLabel("Tên phòng:"));
        txtTenPhong = new JTextField();
        pnlFormInput.add(txtTenPhong);

        pnlFormInput.add(new JLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>();
        pnlFormInput.add(cboTrangThai);

        // Nút bấm Phòng
        JPanel pnlFormButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnThem = new JButton("Thêm Phòng");
        btnSua = new JButton("Sửa Phòng");
        btnXoa = new JButton("Xóa Phòng");
        btnLamMoi = new JButton("Làm mới"); 
        
        pnlFormButtons.add(btnLamMoi); pnlFormButtons.add(btnThem); 
        pnlFormButtons.add(btnSua); pnlFormButtons.add(btnXoa);

        pnlFormContainer.add(pnlFormInput, BorderLayout.CENTER);
        pnlFormContainer.add(pnlFormButtons, BorderLayout.SOUTH);
        pnlRight.add(pnlFormContainer, BorderLayout.SOUTH);

        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        addEvents();
    }

    private void addEvents() {
        // ================= XỬ LÝ SỰ KIỆN KHU VỰC (MỚI) =================

        // 1. Thêm Khu Vực (Hiện Dialog)
        btnThemKv.addActionListener(e -> showDialogKhuVuc(null));

        // 2. Sửa Khu Vực
        btnSuaKv.addActionListener(e -> {
            KhuVuc selected = listKhuVuc.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực cần sửa!");
                return;
            }
            showDialogKhuVuc(selected);
        });

        // 3. Xóa Khu Vực
        btnXoaKv.addActionListener(e -> {
            KhuVuc selected = listKhuVuc.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực cần xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Xóa khu vực \"" + selected.getTenKhuVuc() + "\"?\n(Cảnh báo: Nếu có phòng thuộc khu vực này, bạn sẽ không thể xóa)", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                qlp_dao dao = new qlp_dao();
                if (dao.deleteKhuVuc(selected.getMaKhuVuc())) {
                    JOptionPane.showMessageDialog(this, "Xóa khu vực thành công!");
                    loadDataToKhuVucList();
                    loadDataToComboBox();
                    loadAllPhongToTable(); // Refresh lại bảng phòng
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! (Có thể khu vực này đang chứa phòng)");
                }
            }
        });

        // ================= XỬ LÝ SỰ KIỆN PHÒNG (CODE CŨ) =================
        
        listKhuVuc.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                KhuVuc selectedKv = listKhuVuc.getSelectedValue();
                if (selectedKv != null) {
                    filterPhongByKhuVuc(selectedKv);
                    for (int i = 0; i < cboKhuVuc.getItemCount(); i++) {
                        if (cboKhuVuc.getItemAt(i).getMaKhuVuc().equals(selectedKv.getMaKhuVuc())) {
                            cboKhuVuc.setSelectedIndex(i); break;
                        }
                    }
                }
            }
        });

        tablePhong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablePhong.getSelectedRow();
                if (row >= 0) {
                    txtMaPhong.setText(tablePhong.getValueAt(row, 0).toString());
                    txtTenPhong.setText(tablePhong.getValueAt(row, 1).toString());
                    String tenKhuVuc = tablePhong.getValueAt(row, 2).toString();
                    String tenTrangThai = tablePhong.getValueAt(row, 3).toString();

                    txtMaPhong.setEditable(false); 

                    for (int i = 0; i < cboKhuVuc.getItemCount(); i++) {
                        if (cboKhuVuc.getItemAt(i).getTenKhuVuc().equals(tenKhuVuc)) {
                            cboKhuVuc.setSelectedIndex(i); break;
                        }
                    }
                    for (int i = 0; i < cboTrangThai.getItemCount(); i++) {
                        if (cboTrangThai.getItemAt(i).getTenTinhTrang().equals(tenTrangThai)) {
                            cboTrangThai.setSelectedIndex(i); break;
                        }
                    }
                }
            }
        });

        btnThem.addActionListener(e -> {
            String ma = txtMaPhong.getText().trim();
            String ten = txtTenPhong.getText().trim();
            KhuVuc kv = (KhuVuc) cboKhuVuc.getSelectedItem();
            TrangThai tt = (TrangThai) cboTrangThai.getSelectedItem();

            if (ma.isEmpty() || ten.isEmpty() || kv == null || tt == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            qlp_dao dao = new qlp_dao();
            if (dao.isExist(ma)) {
                JOptionPane.showMessageDialog(this, "Mã phòng đã tồn tại!");
                return;
            }
            Phong p = new Phong(ma, ten, kv, tt); 
            if (dao.insert(p)) {
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
                refreshTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaPhong.getText().isEmpty()) return;
            String ma = txtMaPhong.getText().trim();
            String ten = txtTenPhong.getText().trim();
            KhuVuc kv = (KhuVuc) cboKhuVuc.getSelectedItem();
            TrangThai tt = (TrangThai) cboTrangThai.getSelectedItem();
            Phong p = new Phong(ma, ten, kv, tt);
            qlp_dao dao = new qlp_dao();
            if (dao.update(p)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                refreshTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        });

        btnXoa.addActionListener(e -> {
            String ma = txtMaPhong.getText().trim();
            if (ma.isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa phòng " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (new qlp_dao().delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    refreshTable();
                    clearForm();
                }
            }
        });
        
        btnLamMoi.addActionListener(e -> {
            clearForm(); 
            listKhuVuc.clearSelection(); 
            loadAllPhongToTable();
        });
    }

    // --- HÀM HIỂN THỊ DIALOG THÊM/SỬA KHU VỰC ---
    private void showDialogKhuVuc(KhuVuc kvEdit) {
        JDialog dialog = new JDialog(this, kvEdit == null ? "Thêm Khu Vực Mới" : "Sửa Khu Vực", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));

        JTextField txtMaKV = new JTextField();
        JTextField txtTenKV = new JTextField();

        dialog.add(new JLabel("  Mã Khu Vực:"));
        dialog.add(txtMaKV);
        dialog.add(new JLabel("  Tên Khu Vực:"));
        dialog.add(txtTenKV);

        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        JPanel pBtn = new JPanel();
        pBtn.add(btnSave); pBtn.add(btnCancel);
        
        dialog.add(new JLabel("")); // Placeholder
        dialog.add(pBtn);

        // Nếu là chế độ Sửa, điền dữ liệu cũ vào
        if (kvEdit != null) {
            txtMaKV.setText(kvEdit.getMaKhuVuc());
            txtTenKV.setText(kvEdit.getTenKhuVuc());
            txtMaKV.setEditable(false); // Không cho sửa mã
        }

        // Sự kiện Lưu
        btnSave.addActionListener(e -> {
            String ma = txtMaKV.getText().trim();
            String ten = txtTenKV.getText().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            qlp_dao dao = new qlp_dao();
            boolean success = false;

            if (kvEdit == null) { // Thêm mới
                if (dao.isKhuVucExist(ma)) {
                    JOptionPane.showMessageDialog(dialog, "Mã khu vực đã tồn tại!");
                    return;
                }
                success = dao.insertKhuVuc(new KhuVuc(ma, ten));
            } else { // Sửa
                success = dao.updateKhuVuc(new KhuVuc(ma, ten));
            }

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Thao tác thành công!");
                dialog.dispose(); // Đóng dialog
                // Reload lại dữ liệu
                loadDataToKhuVucList();
                loadDataToComboBox();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thao tác thất bại!");
            }
        });

        // Sự kiện Hủy
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // --- CÁC HÀM HỖ TRỢ ---
    private void loadDataToTrangThaiComboBox() {
        cboTrangThai.removeAllItems();
        qlp_dao dao = new qlp_dao();
        List<TrangThai> list = dao.getAllTrangThai(); 
        for (TrangThai tt : list) cboTrangThai.addItem(tt);
    }

    private void loadDataToKhuVucList() {
        listModelKhuVuc.clear();
        for (KhuVuc kv : new qlp_dao().getAllKhuVuc()) listModelKhuVuc.addElement(kv);
    }

    private void loadDataToComboBox() {
        cboKhuVuc.removeAllItems();
        for (KhuVuc kv : new qlp_dao().getAllKhuVuc()) cboKhuVuc.addItem(kv);
    }

    private void filterPhongByKhuVuc(KhuVuc kv) {
        if (kv == null) return;
        tableModelPhong.setRowCount(0);
        List<Phong> list = new qlp_dao().getAllPhong();
        for (Phong p : list) {
            if (p.getKhuVuc() != null && p.getKhuVuc().getMaKhuVuc().equals(kv.getMaKhuVuc())) {
                addRowToTable(p);
            }
        }
    }
    
    private void loadAllPhongToTable() {
        tableModelPhong.setRowCount(0);
        List<Phong> list = new qlp_dao().getAllPhong();
        for (Phong p : list) addRowToTable(p);
    }

    private void addRowToTable(Phong p) {
        Vector<Object> row = new Vector<>();
        row.add(p.getMaPhong());
        row.add(p.getTenPhong());
        row.add(p.getKhuVuc() != null ? p.getKhuVuc().getTenKhuVuc() : "");
        row.add(p.getTrangThai() != null ? p.getTrangThai().getTenTinhTrang() : "");
        tableModelPhong.addRow(row);
    }

    private void refreshTable() {
        if (listKhuVuc.getSelectedValue() != null) refreshTableCurrentKhuVuc();
        else loadAllPhongToTable();
    }
    
    private void refreshTableCurrentKhuVuc() {
        KhuVuc selected = listKhuVuc.getSelectedValue();
        if (selected != null) filterPhongByKhuVuc(selected);
    }

    private void clearForm() {
        txtMaPhong.setText(""); txtTenPhong.setText(""); txtMaPhong.setEditable(true);
        if (cboKhuVuc.getItemCount() > 0) cboKhuVuc.setSelectedIndex(0);
        if (cboTrangThai.getItemCount() > 0) cboTrangThai.setSelectedIndex(0);
        tablePhong.clearSelection();
    }
}