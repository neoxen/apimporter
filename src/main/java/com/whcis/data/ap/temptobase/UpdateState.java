
package com.whcis.data.ap.temptobase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class UpdateState {
    private static String url_my = "jdbc:mysql://localhost:3306/db_credit_test?useSSL=false";

    private static String user_my = "root";

    private static String password_my = "ShanShi@1989";

    private static Connection con_my;

    private static String url_ser = "jdbc:mysql://192.168.18.110:3306/cxwh?useSSL=false";

    private static String user_ser = "zxdc";

    private static String password_ser = "zxdc";

    private static Connection con_ser;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        initDataBase();
        setData();

    }

    private static void setData() {
        Statement st_my = null;
        ResultSet rs_my = null;

        Statement st_ser = null;

        try {
            st_my = con_my.createStatement();
            st_ser = con_ser.createStatement();
            rs_my = st_my.executeQuery("SELECT * FROM db_credit_test.tab_permission_wuhan where XK_ZT = '3';");
            while (rs_my.next()) {
                String sql = "";
                try {
                    sql = "update cxwh.ap_administrative_licensing_temp set current_state=3 where id=" + (rs_my.getInt("id") + 258);
//                    System.out.println(sql);
                    st_ser.executeUpdate(sql);
                } catch (Exception e) {
                    System.out.println(sql);
                    e.printStackTrace();
                }
            }
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
        if (st_ser != null) {
            try {
                st_ser.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (rs_my != null) {
            try {
                rs_my.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void initDataBase() {
        try {
            con_my = DriverManager.getConnection(url_my, user_my, password_my);
            con_ser = DriverManager.getConnection(url_ser, user_ser, password_ser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
