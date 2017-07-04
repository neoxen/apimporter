
package com.whcis.data.ap.temptobase;

import com.whcis.data.ap.util.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class TempToBaseServer {

    private static final Logger logger = LoggerFactory.getLogger(TempToBaseServer.class);

    private JdbcTemplate tempJdbcTemplate;

    private JdbcTemplate baseJdbcTemplate;

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static Hashtable<String, Integer> name = new Hashtable<String, Integer>();

    static Hashtable<String, Integer> abbr_name = new Hashtable<String, Integer>();

    private ArrayList<Record> duplicateEntryBase;

    private Set<String> organNotFound = new HashSet<>();
    private Map<Integer, Date> dateNotProper = new HashMap<Integer, Date>();

    @Autowired
    public TempToBaseServer(JdbcTemplate tempJdbcTemplate, JdbcTemplate baseJdbcTemplate) {
        this.tempJdbcTemplate = tempJdbcTemplate;
        this.baseJdbcTemplate = baseJdbcTemplate;
        this.duplicateEntryBase = new ArrayList<>();
    }

    public void stepFive() {
        logger.info("========== Step 5: Upload Data from Temporary Server To Base Server ==========");

        getName();

        logger.info("<===== Importing licensings to server ... ...");
        setLicensingData();
        logger.info("<===== Importing penalties to server ... ...");
        setPenaltyData();

        logger.info("========== Finish Step 5! ==========");
    }

    public void checkOrgans() {
        logger.info("========== Checking organ names before importing into Base Server ==========");

        getName();

        logger.info("<===== Checking licensings ... ...");
        checkingLicensingOrgans();
        for (String organName : organNotFound) {
            logger.error("Organ " + organName + " not found.");
        }
        logger.info("<===== Checking penalties ... ...");
        organNotFound.clear();
        checkingPenaltyOrgans();

        for (String organName : organNotFound) {
            logger.error("Organ " + organName + " not found.");
        }

        logger.info("========== Finished Name Checking! ==========");
    }

    public void checkDate() {
        logger.info("========== Checking Date Parsing before importing into Base Server ==========");

        logger.info("<===== Checking licensings dates ... ...");
        checkingLicensingDates();
        for (Map.Entry<Integer, Date> entry : dateNotProper.entrySet()){
            logger.error("Date parsing not proper: record #" + entry.getKey() + " with date " + entry.getValue());
        }
        logger.info("<===== Checking penalties dates ... ...");

        dateNotProper.clear();
        checkingPenaltyDates();

        for (Map.Entry<Integer, Date> entry : dateNotProper.entrySet()){
            logger.error("Date parsing not proper: record #" + entry.getKey() + " with date " + entry.getValue());
        }

        logger.info("========== Finished Date Parsing Checking! ==========");
    }

    private void getName() {

        try {
            String query = "select id, name, abbr_name from ap_organ WHERE pid != '1' order by id DESC ";

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
                String insertSQL = "INSERT INTO ap_administrative_licensing_temp (`object_name`,`current_state`,`legal_rep`,`credit_code`,`org_code`,`ic_code`,`tax_code`,`identity_code`,`title`,`licensing_type`,`licensing_code`,`licensing_detail`,`effective_date`,`invalid_date`,`licensing_organ`,`local_code`,`update_time`,`remark`, `import_date`,`reserve1`, `reserve3`) VALUES ";

                try {
                    sql = "(" + withNull(rowSet.getString("XK_XDR"))
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
                            + ","
                            + withNull(rowSet.getString("SJMC"))
                            + ","
                            + withNull(rowSet.getString("XK_XZJG"))
                            + ")";

                    // duplication check
                    String checkQuery = "select * from ap_administrative_licensing_temp where "
                            + toLicensingID(rowSet.getString("XK_WSH"), rowSet.getString("XK_XMMC"), rowSet.getString("XK_NR"), rowSet.getString("XK_XDR"), rowSet.getString("XK_XDR_SFZ"),rowSet.getDate("XK_JDRQ")) + "limit 1";

                    SqlRowSet checkRowSet = baseJdbcTemplate.queryForRowSet(checkQuery);

                    if (checkRowSet.next()) {
                        logger.warn(rowSet.getInt("id") + " Record duplicated: " + sql);
                        Record r = new Record("licensing", rowSet.getInt("id"));
                        duplicateEntryBase.add(r);
                        continue;
                    }


                    baseJdbcTemplate.execute(insertSQL+sql);
                } catch (Exception e) {
                    logger.error(rowSet.getInt("id") + " failed: " + sql);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkingLicensingOrgans() {
        String query = "select * from tab_permisson_wuhan_month";

        try {
            SqlRowSet rowSet = tempJdbcTemplate.queryForRowSet(query);
            while (rowSet.next()) {
                toCode(withNull(rowSet.getString("XK_XZJG")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkingLicensingDates() {
        String jdrqQuery = "select id, xk_jdrq from tab_permisson_wuhan_month WHERE xk_jdrq > now()";
        String sjcQuery = "select id, sjc from tab_permisson_wuhan_month WHERE sjc > now()";

        try {
            SqlRowSet rowSet = tempJdbcTemplate.queryForRowSet(jdrqQuery);
            while (rowSet.next()) {
                dateNotProper.put(rowSet.getInt("id"), rowSet.getDate("xk_jdrq"));
            }
            rowSet = tempJdbcTemplate.queryForRowSet(sjcQuery);
            while (rowSet.next()) {
                dateNotProper.put(rowSet.getInt("id"), rowSet.getDate("sjc"));
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
                String insertSQL = "INSERT INTO ap_administrative_penalty_temp (`object_name`,`current_state`,`legal_rep`,`credit_code`,`org_code`,`ic_code`,`tax_code`,`identity_code`,`title`,`penalty_type`,`penalty_code`,`penalty_cause`,`penalty_basis`,`penalty_result`,`effective_date`,`invalid_date`,`penalty_organ`,`local_code`,`update_time`,`remark`, `import_date`, `reserve1`, `reserve3`) VALUES ";
                try {
                    sql = "(" + withNull(rowSet.getString("CF_XDR_MC"))
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
                            + ","
                            + withNull(rowSet.getString("CF_AJMC"))
                            + ","
                            + withNull(rowSet.getString("CF_XZJG"))
                            + ")";

                    // duplication check
                    String checkQuery = "select * from ap_administrative_penalty_temp where "
                            + toPenaltyID(rowSet.getString("CF_WSH"), rowSet.getString("CF_CFMC"), rowSet.getString("CF_SY"), rowSet.getString("CF_XDR_MC"), rowSet.getString("CF_JG"), rowSet.getDate("CF_JDRQ")) + "limit 1";

                    SqlRowSet checkRowSet = baseJdbcTemplate.queryForRowSet(checkQuery);

                    if (checkRowSet.next()) {
                        logger.warn(rowSet.getInt("id") + " Record duplicated: " + sql);
                        Record r = new Record("penalty", rowSet.getInt("id"));
                        duplicateEntryBase.add(r);
                        continue;
                    }

                    baseJdbcTemplate.execute(insertSQL + sql);
                } catch (Exception e) {
                    logger.error(rowSet.getInt("id") + " failed: " + sql);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkingPenaltyOrgans() {
        String query = "select * from tab_penaly_wuhan_month";
        try {
            SqlRowSet rowSet = tempJdbcTemplate.queryForRowSet(query);
            while (rowSet.next()) {
                toCode(withNull(rowSet.getString("CF_XZJG")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkingPenaltyDates() {
        String jdrqQuery = "select id, cf_jdrq from tab_penaly_wuhan_month WHERE cf_jdrq > now()";
        String sjcQuery = "select id, sjc from tab_penaly_wuhan_month WHERE sjc > now()";

        try {
            SqlRowSet rowSet = tempJdbcTemplate.queryForRowSet(jdrqQuery);
            while (rowSet.next()) {
                dateNotProper.put(rowSet.getInt("id"), rowSet.getDate("cf_jdrq"));
            }
            rowSet = tempJdbcTemplate.queryForRowSet(sjcQuery);
            while (rowSet.next()) {
                dateNotProper.put(rowSet.getInt("id"), rowSet.getDate("sjc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toCode(String s) {
        Iterator iter = name.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (s.contains((String)entry.getKey())) {
                return (int) entry.getValue() + "";
            }
        }
//        Iterator iter2 = abbr_name.entrySet().iterator();
//        while (iter2.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter2.next();
//            if (s.equals((String) entry.getKey())) {
//                return (int) entry.getValue() + "";
//            }
//        }
//        int l = s.lastIndexOf("'");
//        s = s.substring(1,l);
//        if (name.containsKey(s.trim())){
//            return (int) name.get(s.trim()) + "";
//        }
//        logger.error("Organ " + s + " not found.");
        organNotFound.add(s);
        return "null";
    }

    private static String toLicensingType(String s) {
        if (s.equals("'普通'")) {
            return "1";
        } else if (s.equals("'特许'")) {
            return "2";
        } else if (s.equals("'认可'")) {
            return "3";
        } else if (s.equals("'核准'")) {
            return "4";
        } else if (s.equals("'登记'")) {
            return "5";
        }
        return "9";
    }

    private static String toPenaltyType(String s) {
        if (s.equals("'警告'")) {
            return "1";
        } else if (s.equals("'罚款'")) {
            return "2";
        } else if (s.equals("'没收违法所得、没收非法财物'")) {
            return "3";
        } else if (s.equals("'责令停产停业'")) {
            return "4";
        } else if (s.equals("'暂扣或者吊销许可证、暂扣或者吊销执照'")) {
            return "5";
        } else if (s.equals("'行政拘留'")) {
            return "6";
        }
        return "9";
    }

    private static String toState(String s) {
        if (s.equals("'正常'") || s.equals("null") || s.equals("'0'")) {
            return "9";
        } else if (s.equals("'撤销'") || s.equals("'1'")) {
            return "1";
        } else if (s.equals("'异常'") || s.equals("'2'")) {
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
        return "'" + sdf.format(date) + "'";
    }

    private static String importDate() {
        return "'" + sdf.format(new java.util.Date()) + "'";
    }


    public static String toLicensingID(String XK_WSH, String XK_XMMC, String XK_NR, String XK_XDR, String XK_XDR_SFZ, Date XK_JDRQ) {
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
        if (XK_JDRQ != null) {
            if (isFrist) {
                s = s + " effective_date='" + XK_JDRQ + "'";
            } else {
                s = s + " AND effective_date='" + XK_JDRQ + "'";
            }
        }
        return s;
    }

    private static String toPenaltyID(String CF_WSH, String CF_CFMC, String CF_SY, String CF_XDR_MC, String CF_JG, Date CF_JDRQ) {
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
        if (CF_JDRQ != null) {
            if (isFrist) {
                s = s + " effective_date='" + CF_JDRQ + "'";
            } else {
                s = s + " AND effective_date='" + CF_JDRQ + "'";
            }
        }
        return s;
    }

    public ArrayList<Record> getDuplicateEntryBase() {
        return duplicateEntryBase;
    }
}
