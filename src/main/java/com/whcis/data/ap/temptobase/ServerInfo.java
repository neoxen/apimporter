package com.whcis.data.ap.temptobase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by neo on 2017/1/9.
 */
public class ServerInfo {
    private static final Logger logger = LoggerFactory.getLogger(ServerInfo.class);

    static String intLicensing = "";
    static String intPenalty = "";


    public static void printMaxRecordID(JdbcTemplate baseJdbcTemplate) {

        try {
            intLicensing =  baseJdbcTemplate.queryForObject("SELECT MAX(id) from ap_administrative_licensing_temp", String.class);
            intPenalty = baseJdbcTemplate.queryForObject("SELECT MAX(id) from ap_administrative_penalty_temp",String.class);

            logger.info("***************************************************************");
            logger.info("* The base server have " + intLicensing + " licensings and " + intPenalty + " penalties. *");
            logger.info("***************************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyNewRecords(JdbcTemplate baseJdbcTemplate) {
        try {
            String sqlLicensing = "insert into ap_administrative_licensing_temp_copy " +
                    "select * from ap_administrative_licensing_temp where id > ";
            String sqlPenalty = "insert into ap_administrative_penalty_temp_copy " +
                    "select * from ap_administrative_penalty_temp where id > ";

            baseJdbcTemplate.execute(sqlLicensing + intLicensing);
            baseJdbcTemplate.execute(sqlPenalty + intPenalty);

            String countLicensing =  baseJdbcTemplate.queryForObject("SELECT COUNT(id) from ap_administrative_licensing_temp_copy", String.class);
            String countPenalty = baseJdbcTemplate.queryForObject("SELECT COUNT(id) from ap_administrative_penalty_temp_copy",String.class);

            logger.info("**********************************************************************");
            logger.info("* " + countLicensing + " licensings and " + countPenalty + " penalties copied to temporary tables. *");
            logger.info("**********************************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
