package com.mycompany.qltb;

import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class QuanLyPhieuMuon extends JFrame {

    // --- Component Bên Trái ---
    private JTextField txtSearch;
    private JComboBox<String> cboStatus;
    private JComboBox<String> cboMonth;
    private JComboBox<String> cboYear;
    private JTable tblPhieu;
    private DefaultTableModel modelPhieu;

    // --- Component Bên Phải ---
    private JButton btnTaoMoi;
    private JButton btnTraPhieu; // [MỚI] Nút Trả Phiếu

    private JTextField txtMaPM, txtMaHS, txtTenHS, txtNgayMuon, txtHanTra, txtGhiChu;
    private JComboBox<String> cboTenLop; // ComboBox chọn tên lớp
    private JComboBox<String> cboTenPhong; // ComboBox chọn tên phòng
    private JTable tblChiTietTB;
    private DefaultTableModel modelChiTietTB;
    private JButton btnUpdate; 

    private PhieuMuonDao dao = new PhieuMuonDao();
    private qltb_view parent; 

    public QuanLyPhieuMuon(qltb_view parent) {
        this.parent = parent;
        initComponents();
        loadComboBoxLop();
        loadComboBoxPhong();
        loadData();
    }

    private void initComponents() {
        setTitle("QUẢN LÝ PHIẾU MƯỢN");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ==========================================
        // 1. PANEL BÊN TRÁI 
        // ==========================================
        JPanel pLeft = new JPanel(new BorderLayout(5, 5));
        pLeft.setPreferredSize(new Dimension(500, 0));
        pLeft.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu mượn"));

        // ... (Giữ nguyên phần tìm kiếm/lọc pFilter và tblPhieu như cũ) ...
        JPanel pFilter = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel pRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(15);
        txtSearch.addActionListener(e -> loadData()); 
        cboStatus = new JComboBox<>(new String[]{"Tất cả", "Đang mượn", "Đã trả"});
        cboStatus.addActionListener(e -> loadData());
        pRow1.add(new JLabel("Tìm tên HS:")); pRow1.add(txtSearch); pRow1.add(cboStatus);

        JPanel pRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cboMonth = new JComboBox<>();
        cboMonth.addItem("Tất cả tháng");
        for (int i = 1; i <= 12; i++) cboMonth.addItem("Tháng " + i);
        cboYear = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        for (int i = currentYear + 1; i >= 2020; i--) cboYear.addItem(String.valueOf(i));
        cboMonth.setSelectedIndex(currentMonth); 
        cboYear.setSelectedItem(String.valueOf(currentYear));
        cboMonth.addActionListener(e -> loadData());
        cboYear.addActionListener(e -> loadData());
        pRow2.add(new JLabel("Thời gian:")); pRow2.add(cboMonth); pRow2.add(cboYear);

        pFilter.add(pRow1); pFilter.add(pRow2);
        pLeft.add(pFilter, BorderLayout.NORTH);

        modelPhieu = new DefaultTableModel(new String[]{"Mã PM", "Tên HS", "Ngày mượn", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblPhieu = new JTable(modelPhieu);
        tblPhieu.setRowHeight(25);
        tblPhieu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showDetail();
        });
        pLeft.add(new JScrollPane(tblPhieu), BorderLayout.CENTER);

        // ==========================================
        // 2. PANEL BÊN PHẢI 
        // ==========================================
        JPanel pRight = new JPanel(new BorderLayout());
        pRight.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu mượn"));

        // --- Nút Chức Năng (Góc trên) ---
        JPanel pRightTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        // Nút Tạo Mới
        btnTaoMoi = new JButton("+ TẠO PHIẾU MỚI");
        btnTaoMoi.setBackground(new Color(0, 153, 76)); // Xanh lá
        btnTaoMoi.setForeground(Color.WHITE);
        btnTaoMoi.setFont(new Font("Arial", Font.BOLD, 12));
        btnTaoMoi.addActionListener(e -> new TaoPhieuMuonFrm(this).setVisible(true));
        
        // [MỚI] Nút Trả Phiếu
        btnTraPhieu = new JButton("TRẢ PHIẾU / HOÀN TẤT");
        btnTraPhieu.setBackground(new Color(0, 102, 204)); // Xanh dương
        btnTraPhieu.setForeground(Color.WHITE);
        btnTraPhieu.setFont(new Font("Arial", Font.BOLD, 12));
        btnTraPhieu.addActionListener(e -> xuLyTraPhieu()); // Gọi hàm xử lý

        pRightTop.add(btnTaoMoi);
        pRightTop.add(btnTraPhieu);
        pRight.add(pRightTop, BorderLayout.NORTH);

        // --- Khu vực Form + Bảng ---
        JPanel pCenterRight = new JPanel(new BorderLayout(5, 5));
        
        JPanel pForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtMaPM = new JTextField(15); txtMaPM.setEditable(false); txtMaPM.setBackground(new Color(240,240,240));
        txtMaHS = new JTextField(15);
        txtTenHS = new JTextField(15); txtTenHS.setEditable(false);
        cboTenLop = new JComboBox<>();
        cboTenPhong = new JComboBox<>();
        txtNgayMuon = new JTextField(15); txtNgayMuon.setEditable(false);
        txtHanTra = new JTextField(15);
        txtGhiChu = new JTextField(15);

        // Layout Form (như cũ)
        gbc.gridx=0; gbc.gridy=0; pForm.add(new JLabel("Mã Phiếu:"), gbc);
        gbc.gridx=1; pForm.add(txtMaPM, gbc);
        gbc.gridx=2; pForm.add(new JLabel("Mã HS:"), gbc);
        gbc.gridx=3; pForm.add(txtMaHS, gbc);
        gbc.gridx=0; gbc.gridy=1; pForm.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx=1; pForm.add(txtTenHS, gbc);
        gbc.gridx=2; pForm.add(new JLabel("Lớp:"), gbc);
        gbc.gridx=3; pForm.add(cboTenLop, gbc);
        gbc.gridx=0; gbc.gridy=2; pForm.add(new JLabel("Phòng:"), gbc);
        gbc.gridx=1; pForm.add(cboTenPhong, gbc);
        gbc.gridx=2; pForm.add(new JLabel("Ngày mượn:"), gbc);
        gbc.gridx=3; pForm.add(txtNgayMuon, gbc);
        gbc.gridx=0; gbc.gridy=3; pForm.add(new JLabel("Hạn trả:"), gbc);
        gbc.gridx=1; pForm.add(txtHanTra, gbc);
        gbc.gridx=2; pForm.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx=3; pForm.add(txtGhiChu, gbc);

        pCenterRight.add(pForm, BorderLayout.NORTH);

        // Bảng thiết bị
        JPanel pTableDevice = new JPanel(new BorderLayout());
        pTableDevice.setBorder(BorderFactory.createTitledBorder("Các thiết bị trong phiếu này"));
        modelChiTietTB = new DefaultTableModel(new String[]{"Mã TB", "Tên TB", "Loại", "Số lượng", "Tình trạng"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblChiTietTB = new JTable(modelChiTietTB);
        tblChiTietTB.setRowHeight(22);
        tblChiTietTB.getColumnModel().getColumn(4).setPreferredWidth(200);
        pTableDevice.add(new JScrollPane(tblChiTietTB), BorderLayout.CENTER);

        pCenterRight.add(pTableDevice, BorderLayout.CENTER);
        pRight.add(pCenterRight, BorderLayout.CENTER);

        // Nút cập nhật thông tin chung (như cũ)
        JPanel pRightBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnUpdate = new JButton("CẬP NHẬT THÔNG TIN");
        btnUpdate.setPreferredSize(new Dimension(180, 35));
        btnUpdate.addActionListener(e -> updatePhieuMuon());
        pRightBottom.add(btnUpdate);
        pRight.add(pRightBottom, BorderLayout.SOUTH);

        // SplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pLeft, pRight);
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);
    }

    private void loadComboBoxLop() {
        cboTenLop.removeAllItems();
        List<String> listLop = dao.getAllTenLop();
        for (String tenLop : listLop) {
            cboTenLop.addItem(tenLop);
        }
    }

    private void loadComboBoxPhong() {
        cboTenPhong.removeAllItems();
        List<String> listPhong = dao.getAllTenPhong();
        for (String tenPhong : listPhong) {
            cboTenPhong.addItem(tenPhong);
        }
    }

    // --- LOGIC LOAD DỮ LIỆU ---
    public void loadData() {
        modelPhieu.setRowCount(0);
        String keyword = txtSearch.getText().trim();
        int status = cboStatus.getSelectedIndex(); 
        int month = cboMonth.getSelectedIndex() > 0 ? cboMonth.getSelectedIndex() : 0;
        int year = Integer.parseInt(cboYear.getSelectedItem().toString());

        List<PhieuMuon> list = dao.findPhieuMuon(keyword, status, month, year);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (PhieuMuon pm : list) {
            String trangThaiText = (pm.getNgayTra() == null) ? "Đang mượn" : "Đã trả";
            modelPhieu.addRow(new Object[]{ pm.getMaPM(), pm.getTenHS(), sdf.format(pm.getNgayMuon()), trangThaiText });
        }
    }

    // --- LOGIC HIỂN THỊ CHI TIẾT ---
    private void showDetail() {
        int row = tblPhieu.getSelectedRow();
        if (row == -1) return;

        int maPM = Integer.parseInt(tblPhieu.getValueAt(row, 0).toString());
        PhieuMuon pm = dao.findById(maPM);
        if (pm != null) {
            txtMaPM.setText(String.valueOf(pm.getMaPM()));
            txtMaHS.setText(pm.getMaHS());
            txtTenHS.setText(pm.getTenHS());
            // Hiển thị tên lớp thay vì mã lớp
            String tenLop = dao.getTenLopByMa(pm.getMaLop());
            if (tenLop != null) {
                cboTenLop.setSelectedItem(tenLop);
            }
            // Hiển thị tên phòng thay vì mã phòng
            String tenPhong = dao.getTenPhongByMa(pm.getMaPhong());
            if (tenPhong != null) {
                cboTenPhong.setSelectedItem(tenPhong);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            txtNgayMuon.setText(pm.getNgayMuon() != null ? sdf.format(pm.getNgayMuon()) : "");
            txtHanTra.setText(pm.getHanTra() != null ? sdf.format(pm.getHanTra()) : "");
            txtGhiChu.setText(pm.getGhiChu());
            
            // Nếu đã trả thì khóa nút trả
            btnTraPhieu.setEnabled(pm.getNgayTra() == null);
        }

        modelChiTietTB.setRowCount(0);
    List<qltb_thuoctinh> listTB = dao.getThietBiMuonByPhieu(maPM);
    
    for (qltb_thuoctinh tb : listTB) {
        modelChiTietTB.addRow(new Object[]{
            tb.getMaTB(),
            tb.getTenTB(),
            tb.getMaLoai(),
            tb.getTongSoLuong(), // Hiển thị tổng số lượng
            tb.getGhiChu()   // Hiển thị tình trạng (VD: Trả: 5 Tốt, 0 Hỏng...)
        });
    }
    }

    // --- [MỚI] LOGIC XỬ LÝ TRẢ PHIẾU ---
    private void xuLyTraPhieu() {
        if (txtMaPM.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa chọn phiếu nào!");
            return;
        }

        // Kiểm tra xem phiếu này đã trả chưa
        int row = tblPhieu.getSelectedRow();
        if (row != -1) {
            String status = tblPhieu.getValueAt(row, 3).toString();
            if (status.equals("Đã trả")) {
                JOptionPane.showMessageDialog(this, "Phiếu này đã trả rồi!");
                return;
            }
        }

        int maPM = Integer.parseInt(txtMaPM.getText());

        // --- GỌI DIALOG TRẢ PHIẾU ---
        // Đảm bảo bạn đã tạo file TraPhieuDialog.java mà tôi gửi ở câu trả lời trước
        TraPhieuDialog dialog = new TraPhieuDialog(this, maPM);
        dialog.setVisible(true);
    }

    private void updatePhieuMuon() {
        // ... (Code cũ giữ nguyên) ...
        try {
             PhieuMuon pm = new PhieuMuon();
             pm.setMaPM(Integer.parseInt(txtMaPM.getText()));
             pm.setMaHS(txtMaHS.getText());
             // Lấy mã lớp từ tên lớp được chọn
             String tenLop = (String) cboTenLop.getSelectedItem();
             String maLop = dao.getMaLopByTen(tenLop);
             pm.setMaLop(maLop);
             // Lấy mã phòng từ tên phòng được chọn
             String tenPhong = (String) cboTenPhong.getSelectedItem();
             String maPhong = dao.getMaPhongByTen(tenPhong);
             pm.setMaPhong(maPhong);
             pm.setGhiChu(txtGhiChu.getText());
             
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             Date d = sdf.parse(txtHanTra.getText());
             pm.setHanTra(new Timestamp(d.getTime()));

             if (dao.updatePhieuMuon(pm)) {
                 JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                 loadData(); 
             } else {
                 JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
             }
        } catch (Exception e) { e.printStackTrace(); }
    }
}