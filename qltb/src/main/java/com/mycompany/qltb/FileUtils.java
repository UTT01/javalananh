package com.mycompany.qltb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class FileUtils {

    public static void loadDataFromFile(qltb_view view, String filePath) {

        qltb_dao tbDao = new qltb_dao();
        dmtb_dao dmDao = new dmtb_dao();
        trangthai_dao ttDao = new trangthai_dao();

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                // format: tenDanhMuc | maTB | tenTB | tenTrangThai
                StringTokenizer st = new StringTokenizer(line, "|");
                if (st.countTokens() < 4) continue;

                String tenDanhMuc   = st.nextToken().trim();
                String maTB         = st.nextToken().trim();
                String tenTB        = st.nextToken().trim();
                String tenTrangThai = st.nextToken().trim();

                /* =========================
                   1. DANH MỤC
                   ========================= */
                dmtb_thuoctinh dm = dmDao.findByTen(tenDanhMuc);
                if (dm == null) {
                    String maLoaiMoi = dmDao.generateMaLoai(); // KHÔNG dùng timeMillis
                    dm = new dmtb_thuoctinh(maLoaiMoi, tenDanhMuc);
                    dm = dmDao.findByTen(tenDanhMuc);
                    dmDao.insert(dm);
                    
                }

                /* =========================
                   2. TRẠNG THÁI
                   ========================= */
                trangthai tt = ttDao.findByTen(tenTrangThai);
                if (tt == null) continue; // trạng thái không tồn tại → bỏ dòng

                /* =========================
                   3. TRÙNG MÃ TB
                   ========================= */
                if (tbDao.isExist(maTB)) continue;

                /* =========================
                   4. THÊM THIẾT BỊ
                   ========================= */
                qltb_thuoctinh tb = new qltb_thuoctinh();
                tb.setMaTB(maTB);
                tb.setTenTB(tenTB);
                tb.setMaLoai(dm.getMaloai());
                tb.setTrangThai(tt.getMaTinhTrang());
                tb.setGhiChu("");

                tbDao.insert(tb);
                count++;
            }

            JOptionPane.showMessageDialog(
                view,
                "Nhập file thành công!\nĐã thêm " + count + " thiết bị.",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE
            );

            // reload giao diện
            view.loadDanhMuc();
            view.loadtb();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                view,
                "Lỗi đọc file: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    public static void exportToFile(JFrame parent, JTable table) {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {

            TableModel model = table.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                String tenLoai   = model.getValueAt(i, 2).toString();
                String maTB      = model.getValueAt(i, 0).toString();
                String tenTB     = model.getValueAt(i, 1).toString();
                String trangThai = model.getValueAt(i, 3).toString();

                fw.write(tenLoai + "|" + maTB + "|" + tenTB + "|" + trangThai + "\n");
            }

            JOptionPane.showMessageDialog(parent,
                    "Xuất file thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent,
                    "Lỗi xuất file: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
