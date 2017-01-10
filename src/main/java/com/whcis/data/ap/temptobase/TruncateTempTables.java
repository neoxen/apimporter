package com.whcis.data.ap.temptobase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by miracle on 2016/12/8.
 */
@Component
public class TruncateTempTables {

    private static final Logger logger = LoggerFactory.getLogger(TruncateTempTables.class);

    public static void truncateTempTables(JdbcTemplate tempJdbcTemplate) {
        logger.info("Truncating temporary tables ... ...");
        try {
            tempJdbcTemplate.execute("truncate table tab_permisson_wuhan_month");
            tempJdbcTemplate.execute("truncate table tab_penaly_wuhan_month");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        logger.info("Finish truncating!");
    }

    public static void truncateBaseTables(JdbcTemplate baseJdbcTemplate) {
        logger.info("Truncating base tables ... ...");
        try {
            baseJdbcTemplate.execute("truncate table ap_administrative_licensing_temp_copy");
            baseJdbcTemplate.execute("truncate table ap_administrative_penalty_temp_copy");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        logger.info("Finish truncating!");
    }
}
