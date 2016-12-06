
package com.whcis.data.ap.temptobase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PenaltyTempToBaseServer {

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
            rs_my = st_my.executeQuery("select * from tab_penaly_wuhan_month");
            while (rs_my.next()) {
                String sql = "";
                try {
                    sql = "INSERT INTO ap_administrative_penalty_temp (`object_name`,`current_state`,`legal_rep`,`credit_code`,`org_code`,`ic_code`,`tax_code`,`identity_code`,`title`,`penalty_type`,`penalty_code`,`penalty_cause`,`penalty_basis`,`penalty_result`,`effective_date`,`invalid_date`,`penalty_organ`,`local_code`,`update_time`,`remark`, `import_date`) VALUES ("
                            + withNull(rs_my.getString("CF_XDR_MC"))
                            + ","
                            + toState(withNull(rs_my.getString("CF_ZT")))
                            + ","
                            + withNull(rs_my.getString("CF_FR"))
                            + ","
                            + withNull(rs_my.getString("CF_XDR_SHXYM"))
                            + ","
                            + withNull(rs_my.getString("CF_XDR_ZDM"))
                            + ","
                            + withNull(rs_my.getString("CF_XDR_GSDJ"))
                            + ","
                            + withNull(rs_my.getString("CF_XDR_SWDJ"))
                            + ","
                            + withNull(rs_my.getString("CF_XDR_SFZ"))
                            + ","
                            + withNull(rs_my.getString("CF_CFMC"))
                            + ","
                            + toType(withNull(rs_my.getString("CF_CFLB1")))
                            + ","
                            + withNull(rs_my.getString("CF_WSH"))
                            + ","
                            + withNull(rs_my.getString("CF_SY"))
                            + ","
                            + withNull(rs_my.getString("CF_YJ"))
                            + ","
                            + withNull(rs_my.getString("CF_JG"))
                            + ","
                            + withNullDate(rs_my.getDate("CF_JDRQ"))
                            + ","
                            + withNullDate(rs_my.getDate("CF_JZRQ"))
                            + ","
                            + toCode(withNull(rs_my.getString("CF_XZJG")))// 转为代码
                            + ","
                            + withNull(rs_my.getString("DFBM"))
                            + ","
                            + withNullDate(rs_my.getDate("SJC"))
                            + ","
                            + withNull(rs_my.getString("BZ"))
                            + ","
                            + importDate()
                            + ")";

                    ResultSet rs = con_ser.createStatement().executeQuery(
                            "select count(*) as rowCount from ap_administrative_penalty_temp where "
                                    + toID(rs_my.getString("CF_WSH"), rs_my.getString("CF_CFMC"), rs_my.getString("CF_SY"), rs_my.getString("CF_XDR_MC"), rs_my.getString("CF_JG")));
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

    private static String toID(String CF_WSH, String CF_CFMC, String CF_SY, String CF_XDR_MC, String CF_JG) {
        String s = "";
        boolean isFrist = true;
        if (CF_WSH != null) {
            s = s + " penalty_code='" + CF_WSH + "'";
            isFrist = false;
        }
        if (CF_CFMC != null) {
            if (isFrist) {
                s = s + " title='" + CF_CFMC + "'";
                isFrist = false;
            } else {
                s = s + " AND title='" + CF_CFMC + "'";
            }
        }
        if (CF_SY != null) {
            if (isFrist) {
                s = s + " penalty_cause='" + CF_SY + "'";
                isFrist = false;
            } else {
                s = s + " AND penalty_cause='" + CF_SY + "'";
            }
        }
        if (CF_XDR_MC != null) {
            if (isFrist) {
                s = s + " object_name='" + CF_XDR_MC + "'";
                isFrist = false;
            } else {
                s = s + " AND object_name='" + CF_XDR_MC + "'";
            }
        }
        if (CF_JG != null) {
            if (isFrist) {
                s = s + " penalty_result='" + CF_JG + "'";
            } else {
                s = s + " AND penalty_result='" + CF_JG + "'";
            }
        }
        return s;
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
        if (s.equals("警告")) {
            return "1";
        } else if (s.equals("罚款")) {
            return "2";
        } else if (s.equals("没收违法所得、没收非法财物")) {
            return "3";
        } else if (s.equals("责令停产停业")) {
            return "4";
        } else if (s.equals("暂扣或者吊销许可证、暂扣或者吊销执照")) {
            return "5";
        } else if (s.equals("行政拘留")) {
            return "6";
        }
        return "9";
    }

    private static String toState(String s) {
        if (s.equals("正常") || s.equals("null") || s.equals("0")) {
            return "9";
        } else if (s.equals("撤销") || s.equals("1")) {
            return "1";
        } else if (s.equals("异议") || s.equals("2")) {
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

}
