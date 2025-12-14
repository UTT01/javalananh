/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qltb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author LanAnh
 */





/**
 *
 * @author LanAnh
 */
public class db {
     private String host;
    private String port;
    private String dbName;
    private String url;
    private String userName;
    private String password;

    public db() {
        host = "localhost";
        port = "3306";
        dbName = "qltb";
        url = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                + "?useSSL=false&serverTimezone=UTC";
        userName = "root";
        password = "";
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Statement getStatement() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                return conn.createStatement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

