
package com.whcis.data.ap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    private static final Logger logger = LoggerFactory.getLogger(Common.class);

    static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-ddHH:mm");
    static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
    static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.S");

    static SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy.MM.dd");
    static SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy.MM.ddHH:mm");
    static SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy.MM.ddHH:mm:ss");
    static SimpleDateFormat sdf8 = new SimpleDateFormat("yyyy.MM.ddHH:mm:ss.S");

    static SimpleDateFormat sdf9 = new SimpleDateFormat("yyyy.MM");

    static SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy年MM月dd日");

    public static String sToDate(String s) {
        s = s.trim().replace(" ", "").replace("�", "");

        s = s.replace(",", "-").replace("，", "-").replace("：", ":");

        s = s.replace("/", "-");

        if (s.equals("长期")) {
            return "'2099-12-31'";
        }

        if (s.equals("9999/12/31") || s.equals("9999-12-31") || s.equals("2099/12/31")) {
            return "'2099-12-31'";
        }

        if (s.equals("\\")) {
            return "null";
        }

        if (s.equals("/")) {
            return "null";
        }

        if (s.equals("空")) {
            return "null";
        }

        if (s.indexOf("-") == 2 || s.indexOf("/") == 2 || s.indexOf(".") == 2) {
            s = "20" + s;
        }

        if (s.contains("00:00:00")) {
            s = s.substring(0, s.indexOf("00:00:00"));
        }

        if (s.contains("00:00:00.0")) {
            s = s.substring(0, s.indexOf("00:00:00.0"));
        }

        try {
            Date date = new Date(sdf1.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf2.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf3.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf4.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf5.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf6.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf7.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf8.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf9.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        try {
            Date date = new Date(sdf10.parse(s).getTime());
            return "'" + sdf1.format(date) + "'";
        } catch (Exception e) {

        }
        logger.warn(s);
        return "null";
    }

    public static String toState(String s) {
        if (s.equals("正常") || s.equals("null") || s.equals("0")) {
            return "'0'";
        } else if (s.equals("撤销") || s.equals("1")) {
            return "'1'";
        } else if (s.equals("异议") || s.equals("2")) {
            return "'2'";
        }
        return "'3'";
    }
}
