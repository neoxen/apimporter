package com.whcis.data.ap.oldtemplate;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.util.FileUtil;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@EnableConfigurationProperties({FilePathConfig.class})
public class OldTemplateToTempServer {

    private static final Logger logger = LoggerFactory.getLogger(OldTemplateToTempServer.class);

    private FilePathConfig filePathConfig;

    private JdbcTemplate tempJdbcTemplate;

    @Autowired
    public OldTemplateToTempServer(FilePathConfig filePathConfig, JdbcTemplate tempJdbcTemplate) {
        this.filePathConfig = filePathConfig;
        this.tempJdbcTemplate = tempJdbcTemplate;
    }


    public void stepFour() {
        logger.info("========== Step 4: Upload Old Template To The Temporary Server ==========");

        String filePath = filePathConfig.getOldTemplate();
        writeToDatabase(filePath, 3);

        logger.info("========== Finish Step 4! ==========");
    }


    private void writeToDatabase(String filePath, int source) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 0,3, source);
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 1,3, source);
            readWB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
