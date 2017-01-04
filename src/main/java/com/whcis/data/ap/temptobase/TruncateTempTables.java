package com.whcis.data.ap.temptobase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by miracle on 2016/12/8.
 */
@Component
public class TruncateTempTables {

    public static void truncateTables(JdbcTemplate tempJdbcTemplate) {
        System.out.println("Truncating temporary tables ... ...");
        try {
            tempJdbcTemplate.execute("truncate table tab_permisson_wuhan_month");
            tempJdbcTemplate.execute("truncate table tab_penaly_wuhan_month");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Finish truncating!");
    }
}
