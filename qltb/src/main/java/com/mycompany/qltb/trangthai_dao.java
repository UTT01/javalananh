/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LanAnh
 */
public class trangthai_dao {
    public List<trangthai> getAll() {
        List<trangthai> list = new ArrayList<>();
        String sql = "SELECT * FROM trangthai";

        try {
            db database = new db();
            Connection c = database.getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new trangthai(
                        rs.getInt("matinhtrang"),
                        rs.getString("tentinhtrang"),
                        rs.getString("mota")

                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public trangthai findByTen(String ten) {
    String sql = "SELECT * FROM trangthai WHERE tentinhtrang = ?";

    try (Connection c = new db().getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, ten);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new trangthai(
                rs.getInt("matinhtrang"),
                rs.getString("tentinhtrang"),
                rs.getString("mota")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

}
