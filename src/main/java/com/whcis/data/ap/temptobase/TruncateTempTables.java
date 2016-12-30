package com.whcis.data.ap.temptobase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by miracle on 2016/12/8.
 */
public class TruncateTempTables {

    private static String url_my = "jdbc:mysql://localhost:3306/db_credit_test?useSSL=false";

    private static String user_my = "root";

    private static String password_my = "kaifa001";

    private static Connection con_my;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Truncating temporary tables ... ...");
        initDataBase();
        truncateTables();
        System.out.println("Finish truncating!");

    }

    private static void initDataBase() {
        try {
            con_my = DriverManager.getConnection(url_my, user_my, password_my);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void truncateTables() {
        Statement st_my = null;

        try {
            st_my = con_my.createStatement();
            st_my.execute("truncate table tab_permisson_wuhan_month");
            st_my.execute("truncate table tab_penaly_wuhan_month");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (st_my != null) {
            try {
                st_my.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
