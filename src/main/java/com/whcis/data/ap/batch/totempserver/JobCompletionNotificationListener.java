package com.whcis.data.ap.batch.totempserver;

/**
 * Created by neo on 2017/6/29.
 */


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.whcis.data.ap.model.LicensingTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

//@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    @Qualifier("tempJdbcTemplate")
    private JdbcTemplate tempJdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate tempJdbcTemplate) {
        this.tempJdbcTemplate = tempJdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            List<LicensingTemp> results = tempJdbcTemplate.query("SELECT * FROM tab_permisson_wuhan_month", new RowMapper<LicensingTemp>() {
                @Override
                public LicensingTemp mapRow(ResultSet rs, int row) throws SQLException {
                    LicensingTemp licensing = new LicensingTemp();
                    licensing.setXkWsh(rs.getString("XK_WSH"));
                    licensing.setXkXmmc(rs.getString("XK_XMMC"));
                    return licensing;
                }
            });

            for (LicensingTemp licensing : results) {
                log.info("Found <" + licensing + "> in the database.");
            }

        }
    }
}