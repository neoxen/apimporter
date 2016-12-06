
package com.whcis.data.ap.temptobase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Permissom {

    static Connection oracle_conn = null;

    static Statement oracle_stmt = null;

    static Statement oracle_stmt_write = null;

    static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");

    static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy.MM.dd");

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // 打开orcale数据库连接
        openOrcale();
        // 数据转换
        transformData();
        // 关闭数据库连接
        closeOrcale();
    }

    private static void closeOrcale() {
        // TODO Auto-generated method stub
        if (oracle_conn != null) {
            try {
                oracle_conn.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (oracle_stmt != null) {
            try {
                oracle_stmt.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (oracle_stmt_write != null) {
            try {
                oracle_stmt_write.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void transformData() {
        // TODO Auto-generated method stub
        try {
            ResultSet rs = oracle_stmt.executeQuery("select * from TAB_PERMISSION_WUHAN");
            while (rs.next()) {
                try {
                    oracle_stmt_write.executeUpdate("insert into TAB_PERMISSON_WUHAN_TEMPORARY values(" + rs.getInt("ID") + ",'" + withNull(rs.getString("XK_WSH")) + "','"
                            + withNull(rs.getString("XK_XMMC")) + "','" + withNull(rs.getString("XK_SPLB")) + "','" + toShort(withNull(rs.getString("XK_NR"))) + "','"
                            + withNull(rs.getString("XK_XDR")) + "','" + withNull(rs.getString("XK_XDR_SHXYM")) + "','" + withNull(rs.getString("XK_XDR_ZDM")) + "','"
                            + withNull(rs.getString("XK_XDR_GSDJ")) + "','" + withNull(rs.getString("XK_XDR_SWDJ")) + "','" + withNull(rs.getString("XK_XDR_SFZ")) + "','"
                            + withNull(rs.getString("XK_FR")) + "'," + toDate(withNullDate(rs.getDate("XK_JDRQ"))) + "," + toDate(withNull(rs.getString("XK_JZQ"))) + ",'"
                            + withNull(rs.getString("XK_XZJG")) + "','','" + getNum(withNull(rs.getString("XK_ZT"))) + "','" + toDFBM(withNull(rs.getString("DFBM"))) + "',"
                            + toDate(withNullDate(rs.getDate("SJC"))) + ",'" + withNull(rs.getString("BZ")) + "','" + withNull(rs.getString("SJMC")) + "'" + ")");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("insert into TAB_PERMISSON_WUHAN_TEMPORARY values(" + rs.getInt("ID") + ",'" + withNull(rs.getString("XK_WSH")) + "','" + withNull(rs.getString("XK_XMMC"))
                            + "','" + withNull(rs.getString("XK_SPLB")) + "','" + toShort(withNull(rs.getString("XK_NR"))) + "','" + withNull(rs.getString("XK_XDR")) + "','"
                            + withNull(rs.getString("XK_XDR_SHXYM")) + "','" + withNull(rs.getString("XK_XDR_ZDM")) + "','" + withNull(rs.getString("XK_XDR_GSDJ")) + "','"
                            + withNull(rs.getString("XK_XDR_SWDJ")) + "','" + withNull(rs.getString("XK_XDR_SFZ")) + "','" + withNull(rs.getString("XK_FR")) + "',"
                            + toDate(withNullDate(rs.getDate("XK_JDRQ"))) + "," + toDate(withNull(rs.getString("XK_JZQ"))) + ",'" + withNull(rs.getString("XK_XZJG")) + "','','"
                            + getNum(withNull(rs.getString("XK_ZT"))) + "','" + toDFBM(withNull(rs.getString("DFBM"))) + "'," + toDate(withNullDate(rs.getDate("SJC"))) + ",'"
                            + withNull(rs.getString("BZ")) + "','" + withNull(rs.getString("SJMC")) + "'" + ")");
                    e.printStackTrace();
                }
            }
            rs.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String toShort(String s) {
        // TODO Auto-generated method stub
        if (!s.equals("")) {
            return s.substring(0, s.length() > 4000 ? 4000 : s.length());
        }
        return "";
    }

    private static String toDFBM(String s) {
        // TODO Auto-generated method stub
        if (!s.equals("")) {
            return s.substring(0, s.length() > 6 ? 6 : s.length());
        }
        return "";
    }

    private static String getNum(String s) {
        // TODO Auto-generated method stub
        if (s.equals("正常")) {
            return "0";
        } else if (s.equals("撤销")) {
            return "1";
        } else if (s.equals("异议")) {
            return "2";
        }
        return "3";
    }

    private static String toDate(String s) {
        // TODO Auto-generated method stub
        if (s.equals("")) {
            return "''";
        }
        if (s.equals("长期")) {
            return "to_date('2099-12-31','yyyy-mm-dd hh24:mi:ss')";
        }
        try {
            Date date = new Date(sdf1.parse(s).getTime());
            return "to_date('" + date.toString() + "','yyyy-mm-dd hh24:mi:ss')";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf2.parse(s).getTime());
            return "to_date('" + date.toString() + "','yyyy-mm-dd hh24:mi:ss')";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf3.parse(s).getTime());
            return "to_date('" + date.toString() + "','yyyy-mm-dd hh24:mi:ss')";
        } catch (Exception e) {

        }
//        System.out.println(s);
        return "''";
    }

    private static String withNullDate(Date date) {
        // TODO Auto-generated method stub
        if (date == null)
            return "";
        return date.toString();
    }

    private static String withNull(String s) {
        // TODO Auto-generated method stub
        if (s != null && !s.equalsIgnoreCase("null")) {
            return s;
        }
        return "";
    }

    private static void openOrcale() {
        // TODO Auto-generated method stub
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            oracle_conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.18.110:1521:xe", "DB_CREDIT_TEST", "whcic");
            oracle_stmt = oracle_conn.createStatement();
            oracle_stmt_write = oracle_conn.createStatement();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
