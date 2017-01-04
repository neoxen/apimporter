package com.whcis.data.ap.oldtemplate;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.util.FileUtil;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@EnableConfigurationProperties({FilePathConfig.class})
public class OldTemplateToTempServer {

    private FilePathConfig filePathConfig;

    private JdbcTemplate tempJdbcTemplate;

    @Autowired
    public OldTemplateToTempServer(FilePathConfig filePathConfig, JdbcTemplate tempJdbcTemplate) {
        this.filePathConfig = filePathConfig;
        this.tempJdbcTemplate = tempJdbcTemplate;
    }


    public void StepThree() {
        System.out.println("***********************************************************");
        System.out.println("* Step Three: Upload Old Template To The Temporary Server *");
        System.out.println("***********************************************************");

        String filePath = filePathConfig.getOldTemplate();
        writeToDatabase(filePath);

        System.out.println("**********************");
        System.out.println("* Finish Step Three! *");
        System.out.println("**********************");
    }


    private void writeToDatabase(String filePath) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 0,3);
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 1,3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
