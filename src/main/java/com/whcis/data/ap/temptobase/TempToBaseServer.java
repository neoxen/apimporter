
package com.whcis.data.ap.temptobase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LicensingTempToBaseServer {

    private JdbcTemplate tempJdbcTemplate;

    private JdbcTemplate baseJdbcTemplate;

    static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    static HashMap<String, Integer> name = new HashMap<String, Integer>();

    static HashMap<String, Integer> abbr_name = new HashMap<String, Integer>();

    @Autowired
    public LicensingTempToBaseServer(JdbcTemplate tempJdbcTemplate, JdbcTemplate baseJdbcTemplate) {
        this.tempJdbcTemplate = tempJdbcTemplate;
        this.baseJdbcTemplate = baseJdbcTemplate;
    }

    public void stepFive() {
        System.out.println("************************************************************");
        System.out.println("* Step 5: Upload Data from Temporary Server To Base Server *");
        System.out.println("************************************************************");

        getName();

        System.out.println("Importing licensings to server ... ...");
        setLicensingData();
        System.out.println("Importing penalties to server ... ...");
        setPenaltyData();

        System.out.println("******************");
        System.out.println("* Finish Step 5! *");
        System.out.println("******************");
    }

    private void getName() {

        try {
            String query = "select id, name, abbr_name from ap_organ";

            SqlRowSet rowSet = baseJdbcTemplate.queryForRowSet(query);

            if (!rowSet.wasNull()) {
                while (rowSet.next()) {
                    name.put(rowSet.getString("name"), rowSet.getInt("id"));
                    abbr_name.put(rowSet.getString("abbr_name"), rowSet.getInt("id"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLicensingData() {
        String query = "select * from tab_permisson_wuhan_month";

        try {
            SqlRowSet rowSet = tempJdbcTemplate.queryForRowSet(query);
            while (rowSet.next()) {
                String sql = "";

                try {
                    sql = "INSERT INTO ap_administrative_licensing_temp (`object_name`,`current_state`,`legal_rep`,`credit_code`,`org_code`,`ic_code`,`tax_code`,`identity_code`,`title`,`licensing_type`,`licensing_code`,`licensing_detail`,`effective_date`,`invalid_date`,`licensing_organ`,`local_code`,`update_time`,`remark`, `import_date`) VALUES ("
                            + withNull(rowSet.getString("XK_XDR"))
                            + ","
                            + toState(withNull(rowSet.getString("XK_ZT")))
                            + ","
                            + withNull(rowSet.getString("XK_FR"))
                            + ","
                            + withNull(rowSet.getString("XK_XDR_SHXYM"))
                            + ","
                            + withNull(rowSet.getString("XK_XDR_ZDM"))
                            + ","
                            + withNull(rowSet.getString("XK_XDR_GSDJ"))
                            + ","
                            + withNull(rowSet.getString("XK_XDR_SWDJ"))
                            + ","
                            + withNull(rowSet.getString("XK_XDR_SFZ"))
                            + ","
                            + withNull(rowSet.getString("XK_XMMC"))
                            + ","
                            + toLicensingType(withNull(rowSet.getString("XK_SPLB")))
                            + ","
                            + withNull(rowSet.getString("XK_WSH"))
                            + ","
                            + withNull(rowSet.getString("XK_NR"))
                            + ","
                            + withNullDate(rowSet.getDate("XK_JDRQ"))
                            + ","
                            + withNullDate(rowSet.getDate("XK_JZQ"))
                            + ","
                            + toCode(withNull(rowSet.getString("XK_XZJG")))// 转为代码
                            + ","
                            + withNull(rowSet.getString("DFBM"))
                            + ","
                            + withNullDate(rowSet.getDate("SJC"))
                            + ","
                            + withNull(rowSet.getString("BZ"))
                            + ","
                            + importDate()
                            + ")";

                    // duplication check
                    String checkQuery = "select * from ap_administrative_licensing_temp where "
                            + toLicensingID(rowSet.getString("XK_WSH"), rowSet.getString("XK_XMMC"), rowSet.getString("XK_NR"), rowSet.getString("XK_XDR"), rowSet.getString("XK_XDR_SFZ")) + "limit 1";

                    SqlRowSet checkRowSet = baseJdbcTemplate.queryForRowSet(checkQuery);

                    if (!checkRowSet.wasNull()) {
                        System.out.println(rowSet.getInt("id") + " Record duplicated: " + sql);
                        continue;
                    }


                    baseJdbcTemplate.execute(sql);
                } catch (Exception e) {
                    System.out.println(rowSet.getInt("id") + " failed");
                    System.out.println(sql);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPenaltyData() {
        String query = "select * from tab_penaly_wuhan_month";
        try {
            SqlRowSet rowSet = tempJdbcTemplate.queryForRowSet(query);
            while (rowSet.next()) {
                String sql = "";
                try {
                    sql = "INSERT INTO ap_administrative_penalty_temp (`object_name`,`current_state`,`legal_rep`,`credit_code`,`org_code`,`ic_code`,`tax_code`,`identity_code`,`title`,`penalty_type`,`penalty_code`,`penalty_cause`,`penalty_basis`,`penalty_result`,`effective_date`,`invalid_date`,`penalty_organ`,`local_code`,`update_time`,`remark`, `import_date`) VALUES ("
                            + withNull(rowSet.getString("CF_XDR_MC"))
                            + ","
                            + toState(withNull(rowSet.getString("CF_ZT")))
                            + ","
                            + withNull(rowSet.getString("CF_FR"))
                            + ","
                            + withNull(rowSet.getString("CF_XDR_SHXYM"))
                            + ","
                            + withNull(rowSet.getString("CF_XDR_ZDM"))
                            + ","
                            + withNull(rowSet.getString("CF_XDR_GSDJ"))
                            + ","
                            + withNull(rowSet.getString("CF_XDR_SWDJ"))
                            + ","
                            + withNull(rowSet.getString("CF_XDR_SFZ"))
                            + ","
                            + withNull(rowSet.getString("CF_CFMC"))
                            + ","
                            + toPenaltyType(withNull(rowSet.getString("CF_CFLB1")))
                            + ","
                            + withNull(rowSet.getString("CF_WSH"))
                            + ","
                            + withNull(rowSet.getString("CF_SY"))
                            + ","
                            + withNull(rowSet.getString("CF_YJ"))
                            + ","
                            + withNull(rowSet.getString("CF_JG"))
                            + ","
                            + withNullDate(rowSet.getDate("CF_JDRQ"))
                            + ","
                            + withNullDate(rowSet.getDate("CF_JZRQ"))
                            + ","
                            + toCode(withNull(rowSet.getString("CF_XZJG")))// 转为代码
                            + ","
                            + withNull(rowSet.getString("DFBM"))
                            + ","
                            + withNullDate(rowSet.getDate("SJC"))
                            + ","
                            + withNull(rowSet.getString("BZ"))
                            + ","
                            + importDate()
                            + ")";

                    // duplication check
                    String checkQuery = "select * from ap_administrative_penalty_temp where "
                            + toPenaltyID(rowSet.getString("CF_WSH"), rowSet.getString("CF_CFMC"), rowSet.getString("CF_SY"), rowSet.getString("CF_XDR_MC"), rowSet.getString("CF_JG")) + "limit 1";

                    SqlRowSet checkRowSet = baseJdbcTemplate.queryForRowSet(checkQuery);

                    if (!checkRowSet.wasNull()) {
                        System.out.println(rowSet.getInt("id") + " Record duplicated: " + sql);
                        continue;
                    }

                    baseJdbcTemplate.execute(sql);
                } catch (Exception e) {
                    System.out.println(rowSet.getInt("id") + " failed");
                    System.out.println(sql);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String toCode(String s) {
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

    private static String toLicensingType(String s) {
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

    private static String toPenaltyType(String s) {
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


    public static String toLicensingID(String XK_WSH, String XK_XMMC, String XK_NR, String XK_XDR, String XK_XDR_SFZ) {
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

    private static String toPenaltyID(String CF_WSH, String CF_CFMC, String CF_SY, String CF_XDR_MC, String CF_JG) {
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

}
