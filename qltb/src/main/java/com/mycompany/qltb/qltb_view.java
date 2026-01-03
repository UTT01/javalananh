
package com.mycompany.qltb;

// Các import: nhập các class cần thiết từ thư viện Java
import java.awt.BorderLayout;          // Layout chia vùng NORTH, SOUTH, EAST, WEST, CENTER
import java.awt.Dimension;            // Kích thước (width, height)
import java.awt.GridLayout;           // Layout dạng lưới (hàng x cột)
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;     // Tạo viền cho panel
import javax.swing.DefaultListModel;  // Mô hình dữ liệu cho JList
import javax.swing.JButton;           // Nút bấm
import javax.swing.JComboBox;        // Hộp chọn (combo box)
import javax.swing.JFileChooser;
import javax.swing.JFrame;           // Cửa sổ chính
import javax.swing.JLabel;           // Nhãn chữ
import javax.swing.JList;            // Danh sách dọc
import javax.swing.JMenu;            // Menu (File, Edit...)
import javax.swing.JMenuBar;         // Thanh menu trên cùng
import javax.swing.JMenuItem;        // Mục menu con (Save, Open...)
import javax.swing.JOptionPane;
import javax.swing.JPanel;           // Khung chứa các thành phần con
import javax.swing.JScrollPane;      // Thanh cuộn (dùng cho JList, JTable)
import javax.swing.JTable;           // Bảng dữ liệu
import javax.swing.JTextField;      // Ô nhập văn bản một dòng
import static javax.swing.WindowConstants.EXIT_ON_CLOSE; // Import tĩnh để dùng ngắn gọn
import javax.swing.table.DefaultTableModel; // Mô hình dữ liệu cho JTable

// Khai báo class chính tên là qltb_view
// Kế thừa từ JFrame → đây là cửa sổ chính của chương trình quản lý thiết bị
public class qltb_view extends JFrame {

    // Trường dữ liệu (field) public kiểu JMenuItem
    // Các mục menu: Save, Open, Exit
    public JMenuItem menuSave, menuOpen, menuExit;

    // Trường dữ liệu public kiểu JList
    // Danh sách hiển thị các danh mục (dạng dọc)
    // <dmtb_thuoctinh> nghĩa là mỗi phần tử là một đối tượng danh mục
    public JList<dmtb_thuoctinh> listDanhMuc;
    public DefaultListModel<dmtb_thuoctinh> listModelDm = new DefaultListModel<>();
    // Các nút bấm quản lý danh mục
    public JButton btnNewDm, btnUpdateDm, btnRemoveDm;

    // Bảng hiển thị danh sách sản phẩm/thiết bị
    public JTable tableSp;

    // Mô hình dữ liệu cho JTable
    // Quản lý cột và dòng của bảng
    public DefaultTableModel tableModelSp;

    // ComboBox chọn danh mục khi thêm/sửa sản phẩm
    public JComboBox<dmtb_thuoctinh> cboCategory;

    // ComboBox chọn trạng thái thiết bị (còn/mượn/hỏng...)
    public JComboBox<trangthai> cbotrangthai;

    // Các ô nhập liệu cho sản phẩm
    public JTextField txtId, txtName, txtSoLuongTot, txtSoLuongHong;


    // Các nút bấm quản lý sản phẩm
    public JButton btnthem, btnsua, btnxoa;

    // Constructor chính của cửa sổ
    public qltb_view() {
        // Cấu hình cửa sổ chính
        setTitle("Quản lý thiết bị!");                    // Tiêu đề cửa sổ
        setSize(1000, 600);                               // Kích thước: rộng 1000, cao 600
        setDefaultCloseOperation(EXIT_ON_CLOSE);          // Đóng cửa sổ → thoát chương trình
        setLocationRelativeTo(null);                      // Hiển thị giữa màn hình
        setLayout(new BorderLayout());                    // Dùng BorderLayout cho toàn bộ cửa sổ
       // gọi menu
setJMenuBar(menu.createMainMenuBar(
    () -> { // Nhập file
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            FileUtils.loadDataFromFile(this, fc.getSelectedFile().getPath());
        }
    },
    () -> FileUtils.exportToFile(this, tableSp),
    () -> new QuanLyPhieuMuon(this).setVisible(true),
    () -> JOptionPane.showMessageDialog(this, "Xuất phiếu mượn..."),
    () -> JOptionPane.showMessageDialog(this, "Mở form sinh viên mượn..."),

    () -> new QuanLyPhong().setVisible(true),
    // --------------------

    () -> JOptionPane.showMessageDialog(this, "Mở form lớp tín chỉ...")
));


        // ===== Panel bên trái: Quản lý danh mục =====
        JPanel left = new JPanel(new BorderLayout());                 // Panel trái dùng BorderLayout
        left.setPreferredSize(new Dimension(250, 0));                 // Rộng cố định 250px
        left.setBorder(BorderFactory.createTitledBorder("Danh mục thiết bị")); // Viền có tiêu đề

        listDanhMuc = new JList<>(listModelDm);                       // Tạo JList dùng model listModelDm
        listDanhMuc.addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        dmtb_thuoctinh dm = listDanhMuc.getSelectedValue();
        if (dm != null) {
            loadtbByLoai(dm.getMaloai());
        }
    }
});

        left.add(new JScrollPane(listDanhMuc), BorderLayout.CENTER);  // Thêm JList có thanh cuộn vào giữa

        JPanel pbtn = new JPanel();                                   // Panel chứa các nút danh mục

        btnNewDm = new JButton("New");                                // Nút thêm danh mục mới
        btnNewDm.addActionListener(e -> {                             // Sự kiện khi nhấn nút New
            // Mở form thêm danh mục mới, truyền 'this' (cửa sổ hiện tại) làm parent
            new dmtb_view(this).setVisible(true);
        });
        btnUpdateDm = new JButton("Update");                          // Nút sửa (chưa code sự kiện)
       btnUpdateDm.addActionListener(e -> {                             // Sự kiện khi nhấn nút New
            dmtb_thuoctinh selected = listDanhMuc.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new dmtb_sua(this, selected);
        });
        btnRemoveDm = new JButton("Remove");                          // Nút xóa (chưa code sự kiện)
        btnRemoveDm.addActionListener(e -> {
            dmtb_thuoctinh selected = listDanhMuc.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa danh mục \"" + selected.getTenloai() + "\" không?\n(Các thiết bị thuộc danh mục này có thể bị ảnh hưởng)",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dmtb_dao dao = new dmtb_dao();
                if (dao.delete(selected.getMaloai())) {  // hoặc selected.getId() tùy field của bạn
                    JOptionPane.showMessageDialog(this, "Xóa danh mục thành công!");
                    loadDanhMuc();  // Reload lại JList và ComboBox
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! (Có thể danh mục đang được sử dụng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
       });

        pbtn.add(btnNewDm); pbtn.add(btnUpdateDm); pbtn.add(btnRemoveDm); // Thêm 3 nút vào panel
        left.add(pbtn, BorderLayout.SOUTH);                           // Đặt panel nút ở dưới cùng panel trái
        add(left, BorderLayout.WEST);                                 // Thêm toàn bộ panel trái vào phía WEST

        // ===== Panel bên phải: Thông tin sản phẩm =====
        JPanel right = new JPanel(new BorderLayout());                 // Panel phải dùng BorderLayout
        right.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết")); // Viền có tiêu đề

        // Tạo mô hình bảng với 7 cột, không cho chỉnh sửa ô trực tiếp
        tableModelSp = new DefaultTableModel(new String[]{"Mã thiết bị","Tên thiết bị ","Danh mục","Trạng thái","SL Tốt","SL Hỏng","Tổng SL"}, 0) {
            @Override 
            public boolean isCellEditable(int row, int col) { 
                return false;                                         // Không cho edit ô trong bảng
            }
        };

        tableSp = new JTable(tableModelSp);                           // Tạo bảng dùng model trên
        tableSp.setRowHeight(24);                                     // Chiều cao mỗi dòng 24px
        tableSp.getTableHeader().setReorderingAllowed(false);   
        // Không cho kéo cột đổi chỗ
         tableSp.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        int row = tableSp.getSelectedRow();
        if (row >= 0) {
            showDataFromTable(row);
        }
    }
     txtId.setEditable(false);
});

        JScrollPane spTable = new JScrollPane(tableSp);               // Thêm thanh cuộn cho bảng
        spTable.setPreferredSize(new Dimension(0, 300));              // Chiều cao bảng khoảng 250px
        right.add(spTable, BorderLayout.NORTH);                       // Đặt bảng ở phía trên panel phải

        // Form nhập liệu sản phẩm (lưới 6 hàng x 2 cột)
        JPanel form = new JPanel(new GridLayout(6, 2, 2, 10));
        cboCategory = new JComboBox<>();                              // ComboBox chọn danh mục

        txtId = new JTextField();                                     // Ô nhập mã thiết bị
        txtName = new JTextField();                                   // Ô nhập tên thiết bị
        txtSoLuongTot = new JTextField();                             // Ô nhập số lượng tốt
        txtSoLuongHong = new JTextField();                            // Ô nhập số lượng hỏng
        cbotrangthai = new JComboBox<>();                               // ComboBox chọn trạng thái

        // Thêm các nhãn và ô nhập vào form
        form.add(new JLabel("Danh mục thiết bị:")); form.add(cboCategory);
        form.add(new JLabel("Mã thiết bị :"));     form.add(txtId);
        form.add(new JLabel("Tên thiết :"));       form.add(txtName);
        form.add(new JLabel("SL Tốt :"));          form.add(txtSoLuongTot);
        form.add(new JLabel("SL Hỏng :"));         form.add(txtSoLuongHong);
        form.add(new JLabel("Trạng thái :"));      form.add(cbotrangthai);

        right.add(form, BorderLayout.CENTER);                         // Đặt form vào giữa panel phải
        loadDanhMuc();
        loadtrangthai();
        loadtb();
    setVisible(true);
        // Panel nút bấm cho sản phẩm
        JPanel pbtn2 = new JPanel();
        btnthem = new JButton("Thêm");                                // Nút làm mới form sản phẩm     
        btnthem.addActionListener(e -> {
    String maTB = txtId.getText().trim();
    String tenTB = txtName.getText().trim();
    String soLuongTotStr = txtSoLuongTot.getText().trim();
    String soLuongHongStr = txtSoLuongHong.getText().trim();
    dmtb_thuoctinh loai = (dmtb_thuoctinh) cboCategory.getSelectedItem();
    trangthai tt = (trangthai) cbotrangthai.getSelectedItem();

    // 1. Kiểm tra rỗng
    if (maTB.isEmpty() || tenTB.isEmpty() || soLuongTotStr.isEmpty() || soLuongHongStr.isEmpty() || loai == null || tt == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    // 2. Kiểm tra số lượng hợp lệ
    int soLuongTot = 0, soLuongHong = 0;
    try {
        soLuongTot = Integer.parseInt(soLuongTotStr);
        soLuongHong = Integer.parseInt(soLuongHongStr);
        if (soLuongTot < 0 || soLuongHong < 0) {
            JOptionPane.showMessageDialog(this, "Số lượng không được âm!");
            return;
        }
        if (soLuongTot + soLuongHong == 0) {
            JOptionPane.showMessageDialog(this, "Tổng số lượng phải lớn hơn 0!");
            return;
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên hợp lệ!");
        return;
    }

    qltb_dao dao = new qltb_dao();

    // 3. Kiểm tra trùng mã
    if (dao.isExist(maTB)) {
        JOptionPane.showMessageDialog(this, "Mã thiết bị đã tồn tại!");
        return;
    }

    // 4. Tạo đối tượng thiết bị
    qltb_thuoctinh tb = new qltb_thuoctinh();
    tb.setMaTB(maTB);
    tb.setTenTB(tenTB);
    tb.setMaLoai(loai.getMaloai());
    tb.setTrangThai(tt.getMaTinhTrang());
    tb.setSoLuongTot(soLuongTot);
    tb.setSoLuongHong(soLuongHong);
    tb.setGhiChu("");

    // 5. Thêm vào CSDL
    if (dao.insert(tb)) {
        JOptionPane.showMessageDialog(this, "Thêm thiết bị thành công!");
        loadtb(); // load lại JTable
    } else {
        JOptionPane.showMessageDialog(this, "Thêm thất bại!");
    }
});

        btnsua = new JButton("Sửa");                              
        btnsua.addActionListener(e -> {

    int row = tableSp.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị cần sửa!");
        return;
    }

    String maTB = txtId.getText().trim();
    String tenTB = txtName.getText().trim();
    String soLuongTotStr = txtSoLuongTot.getText().trim();
    String soLuongHongStr = txtSoLuongHong.getText().trim();
    dmtb_thuoctinh loai = (dmtb_thuoctinh) cboCategory.getSelectedItem();
    trangthai tt = (trangthai) cbotrangthai.getSelectedItem();

    // Kiểm tra rỗng
    if (tenTB.isEmpty() || soLuongTotStr.isEmpty() || soLuongHongStr.isEmpty() || loai == null || tt == null) {
        JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        return;
    }

    // Kiểm tra số lượng hợp lệ
    int soLuongTot = 0, soLuongHong = 0;
    try {
        soLuongTot = Integer.parseInt(soLuongTotStr);
        soLuongHong = Integer.parseInt(soLuongHongStr);
        if (soLuongTot < 0 || soLuongHong < 0) {
            JOptionPane.showMessageDialog(this, "Số lượng không được âm!");
            return;
        }
        if (soLuongTot + soLuongHong == 0) {
            JOptionPane.showMessageDialog(this, "Tổng số lượng phải lớn hơn 0!");
            return;
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên hợp lệ!");
        return;
    }

    qltb_thuoctinh tb = new qltb_thuoctinh();
    tb.setMaTB(maTB);
    tb.setTenTB(tenTB);
    tb.setMaLoai(loai.getMaloai());
    tb.setTrangThai(tt.getMaTinhTrang());
    tb.setSoLuongTot(soLuongTot);
    tb.setSoLuongHong(soLuongHong);
    tb.setGhiChu("");

    qltb_dao dao = new qltb_dao();

    if (dao.update(tb)) {
        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        loadtb();
        txtId.setEditable(true);
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
    }
});

        
        btnxoa = new JButton("Xóa");                          // Nút xóa sản phẩm
  
       btnxoa.addActionListener(e -> {
    int row = tableSp.getSelectedRow();

    if (row < 0) {
        JOptionPane.showMessageDialog(this,
                "Vui lòng chọn thiết bị cần xóa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    String maTB = tableSp.getValueAt(row, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa thiết bị mã: " + maTB + " ?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        qltb_dao dao = new qltb_dao();
        if (dao.delete(maTB)) {
            JOptionPane.showMessageDialog(this, "Xóa thiết bị thành công!");
            loadtb(); // Reload lại JTable
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa thất bại! Thiết bị đang được sử dụng.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
});

 
        pbtn2.add(btnthem); pbtn2.add(btnsua); pbtn2.add(btnxoa);
        right.add(pbtn2, BorderLayout.SOUTH);                         // Đặt panel nút ở dưới

        add(right, BorderLayout.CENTER);                              // Thêm panel phải vào giữa cửa sổ
    }



    public void loadDanhMuc() {
    listModelDm.clear();
    cboCategory.removeAllItems();  
    dmtb_dao dao = new dmtb_dao();
  List<dmtb_thuoctinh> dsDanhMuc = dao.getAll();

    for (dmtb_thuoctinh dm : dsDanhMuc) {
        listModelDm.addElement(dm);
        
        // Thêm cùng đối tượng đó vào JComboBox
        // JComboBox cũng sẽ hiển thị tên nhờ override toString()
        cboCategory.addItem(dm);
    } 
}
     public void loadtrangthai() {
    cbotrangthai.removeAllItems();
    trangthai_dao dao = new trangthai_dao();
    // Chỉ lấy trạng thái Sẵn sàng (1) và Dừng hoạt động (8)
    List<trangthai> dsDanhMuc = dao.getTrangThaiQuanLy();

    for (trangthai dm : dsDanhMuc) {
        cbotrangthai.addItem(dm);
    }
}
     public void loadtb() {
    tableModelSp.setRowCount(0); //Xóa toàn bộ dữ liệu cũ trong bảng
     // Gọi DAO lấy danh sách thiết bị
    qltb_dao dao = new qltb_dao();
    List<qltb_thuoctinh> dsTB = dao.getAll();

    // Đổ dữ liệu vào JTable
    for (qltb_thuoctinh tb : dsTB) {
        tableModelSp.addRow(new Object[]{
            tb.getMaTB(),
            tb.getTenTB(),
            tb.gettenLoai(),        // hoặc tb.getMaLoai()
            tb.gettenTrangThai(),   // hoặc tb.getTrangThai()
            tb.getSoLuongTot(),     // Số lượng tốt
            tb.getSoLuongHong(),    // Số lượng hỏng
            tb.getTongSoLuong()     // Tổng số lượng
        });
    } 
}
     private void showDataFromTable(int row) {
    // Lấy dữ liệu từ JTable
    String maTB = tableSp.getValueAt(row, 0).toString();
    String tenTB = tableSp.getValueAt(row, 1).toString();
    String tenLoai = tableSp.getValueAt(row, 2).toString();
    String tenTrangThai = tableSp.getValueAt(row, 3).toString();
    String soLuongTot = tableSp.getValueAt(row, 4).toString();
    String soLuongHong = tableSp.getValueAt(row, 5).toString();

    // Đẩy lên TextField
    txtId.setText(maTB);
    txtName.setText(tenTB);
    txtSoLuongTot.setText(soLuongTot);
    txtSoLuongHong.setText(soLuongHong);

    // Set lại ComboBox danh mục
    for (int i = 0; i < cboCategory.getItemCount(); i++) {
        if (cboCategory.getItemAt(i).getTenloai().equals(tenLoai)) {
            cboCategory.setSelectedIndex(i);
            break;
        }
    }

    // Set lại ComboBox trạng thái
    for (int i = 0; i < cbotrangthai.getItemCount(); i++) {
        if (cbotrangthai.getItemAt(i).getTenTinhTrang().equals(tenTrangThai)) {
            cbotrangthai.setSelectedIndex(i);
            break;
        }
    }
}
private void clearForm() {
    txtId.setText("");
    txtName.setText("");
    txtSoLuongTot.setText("");
    txtSoLuongHong.setText("");
    cboCategory.setSelectedIndex(-1);
    cbotrangthai.setSelectedIndex(-1);
}

    public void loadtbByLoai(String maloai) {
    tableModelSp.setRowCount(0);

    qltb_dao dao = new qltb_dao();
    List<qltb_thuoctinh> ds = dao.getByLoai(maloai);

    for (qltb_thuoctinh tb : ds) {
        tableModelSp.addRow(new Object[]{
            tb.getMaTB(),
            tb.getTenTB(),
            tb.gettenLoai(),
            tb.gettenTrangThai(),
            tb.getSoLuongTot(),
            tb.getSoLuongHong(),
            tb.getTongSoLuong()
        });
    }
}
 
 
  

    
    public static void main(String[] args) {
        new qltb_view().setVisible(true);// Tạo và hiển thị cửa sổ chính
        
    }

   }