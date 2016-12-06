
package com.whcis.data.ap.temptobase;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LicensingTempToBaseServer {
    private static String url_my = "jdbc:mysql://localhost:3306/db_credit_test?useSSL=false";

    private static String user_my = "root";

    private static String password_my = "kaifa001";

    private static Connection con_my;

    private static String url_ser = "jdbc:mysql://192.168.18.110:3306/cxwh_a?useSSL=false";

    private static String user_ser = "zxdc";

    private static String password_ser = "zxdc";

    private static Connection con_ser;

    static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    static HashMap<String, Integer> name = new HashMap<String, Integer>();

    static HashMap<String, Integer> abbr_name = new HashMap<String, Integer>();

    public static void main(String[] args) {
        System.out.println("Importing to server ... ...");
        initDataBase();
        getName();
        setData();
        System.out.println("Finish importing!");

    }

    private static void getName() {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con_ser.createStatement();
            rs = st.executeQuery("select * from ap_organ");
            while (rs.next()) {
                name.put(rs.getString("name"), rs.getInt("id"));
                abbr_name.put(rs.getString("abbr_name"), rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (st != null) {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void setData() {
        Statement st_my = null;
        ResultSet rs_my = null;

        Statement st_ser = null;

        try {
            st_my = con_my.createStatement();
            st_ser = con_ser.createStatement();
            rs_my = st_my.executeQuery("select * from tab_permisson_wuhan_month");
            while (rs_my.next()) {
                String sql = "";

                try {
                    sql = "INSERT INTO ap_administrative_licensing_temp (`object_name`,`current_state`,`legal_rep`,`credit_code`,`org_code`,`ic_code`,`tax_code`,`identity_code`,`title`,`licensing_type`,`licensing_code`,`licensing_detail`,`effective_date`,`invalid_date`,`licensing_organ`,`local_code`,`update_time`,`remark`, `import_date`) VALUES ("
                            + withNull(rs_my.getString("XK_XDR"))
                            + ","
                            + toState(withNull(rs_my.getString("XK_ZT")))
                            + ","
                            + withNull(rs_my.getString("XK_FR"))
                            + ","
                            + withNull(rs_my.getString("XK_XDR_SHXYM"))
                            + ","
                            + withNull(rs_my.getString("XK_XDR_ZDM"))
                            + ","
                            + withNull(rs_my.getString("XK_XDR_GSDJ"))
                            + ","
                            + withNull(rs_my.getString("XK_XDR_SWDJ"))
                            + ","
                            + withNull(rs_my.getString("XK_XDR_SFZ"))
                            + ","
                            + withNull(rs_my.getString("XK_XMMC"))
                            + ","
                            + toType(withNull(rs_my.getString("XK_SPLB")))
                            + ","
                            + withNull(rs_my.getString("XK_WSH"))
                            + ","
                            + withNull(rs_my.getString("XK_NR"))
                            + ","
                            + withNullDate(rs_my.getDate("XK_JDRQ"))
                            + ","
                            + withNullDate(rs_my.getDate("XK_JZQ"))
                            + ","
                            + toCode(withNull(rs_my.getString("XK_XZJG")))// 转为代码
                            + ","
                            + withNull(rs_my.getString("DFBM"))
                            + ","
                            + withNullDate(rs_my.getDate("SJC"))
                            + ","
                            + withNull(rs_my.getString("BZ"))
                            + ","
                            + importDate()
                            + ")";

                    // duplication check
                    ResultSet rs = con_ser.createStatement().executeQuery(
                            "select count(*) as rowCount from ap_administrative_licensing_temp where "
                                    + toID(rs_my.getString("XK_WSH"), rs_my.getString("XK_XMMC"), rs_my.getString("XK_NR"), rs_my.getString("XK_XDR"), rs_my.getString("XK_XDR_SFZ")));
                    rs.next();
                    if (rs.getInt("rowCount") > 0) {
                        System.out.println(rs_my.getInt("id") + " duplicated");
                        System.out.println(sql);
                        continue;
                    }

                    st_ser.executeUpdate(sql);
                } catch (Exception e) {
                    System.out.println(rs_my.getInt("id") + " failed");
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

    private static String toCode(String s) {
        Iterator iter = name.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (s.contains((String) entry.getKey())) {
                return (int) entry.getValue() + "";
            }
        }
        Iterator iter2 = abbr_name.entrySet().iterator();
        while (iter2.hasNext()) {
            Map.Entry entry = (Map.Entry) iter2.next();
            if (s.contains((String) entry.getKey())) {
                return (int) entry.getValue() + "";
            }
        }
        return "null";
    }

    private static String toType(String s) {
        if (s.equals("普通")) {
            return "1";
        } else if (s.equals("特许")) {
            return "2";
        } else if (s.equals("认可")) {
            return "3";
        } else if (s.equals("核准")) {
            return "4";
        } else if (s.equals("登记")) {
            return "5";
        }
        return "9";
    }

    private static String toState(String s) {
        if (s.equals("正常") || s.equals("null") || s.equals("0")) {
            return "9";
        } else if (s.equals("撤销") || s.equals("1")) {
            return "1";
        } else if (s.equals("异议") || s.equals("3")) {
            return "2";
        }
        return "3";
    }

    private static String withNull(String s) {
        if (s != null && !s.equalsIgnoreCase("null")) {
            return "'" + s + "'";
        }
        return "null";
    }

    private static String withNullDate(Date date) {
        if (date == null)
            return "null";
        return "'" + sdf1.format(date) + "'";
    }

    private static String importDate() {
        return "'" + sdf1.format(new java.util.Date()) + "'";
    }

    private static void initDataBase() {
        try {
            con_my = DriverManager.getConnection(url_my, user_my, password_my);
            con_ser = DriverManager.getConnection(url_ser, user_ser, password_ser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toID(String XK_WSH, String XK_XMMC, String XK_NR, String XK_XDR, String XK_XDR_SFZ) {
        String s = "";
        boolean isFrist = true;
        if (XK_WSH != null) {
            s = s + " licensing_code='" + XK_WSH + "'";
            isFrist = false;
        }
        if (XK_XMMC != null) {
            if (isFrist) {
                s = s + " title='" + XK_XMMC + "'";
                isFrist = false;
            } else {
                s = s + " AND title='" + XK_XMMC + "'";
            }
        }
        if (XK_NR != null) {
            if (isFrist) {
                s = s + " licensing_detail='" + XK_NR + "'";
                isFrist = false;
            } else {
                s = s + " AND licensing_detail='" + XK_NR + "'";
            }
        }
        if (XK_XDR != null) {
            if (isFrist) {
                s = s + " object_name='" + XK_XDR + "'";
                isFrist = false;
            } else {
                s = s + " AND object_name='" + XK_XDR + "'";
            }
        }
        if (XK_XDR_SFZ != null) {
            if (isFrist) {
                s = s + " identity_code ='" + XK_XDR_SFZ + "'";
                isFrist = false;
            } else {
                s = s + " AND identity_code='" + XK_XDR_SFZ + "'";
            }
        }
        return s;
    }

}
