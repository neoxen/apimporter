
package com.whcis.data.ap.newtemplate;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.util.FileUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@EnableConfigurationProperties({FilePathConfig.class})
public class NewTemplateUploadToTempServer {

    private FilePathConfig filePathConfig;

    private JdbcTemplate tempJdbcTemplate;

    @Autowired
    public NewTemplateUploadToTempServer(FilePathConfig filePathConfig, JdbcTemplate tempJdbcTemplate) {
        this.filePathConfig = filePathConfig;
        this.tempJdbcTemplate = tempJdbcTemplate;
    }

    public void stepTwo() {
        System.out.println("**************************************************");
        System.out.println("* Step 2: Upload XyChina To The Temporary Server *");
        System.out.println("**************************************************");

        String filePath = filePathConfig.getXyChina();
        writeToDatabase(filePath);

        System.out.println("******************");
        System.out.println("* Finish Step 2! *");
        System.out.println("******************");
    }

    public void stepThree() {
        System.out.println("*******************************************************");
        System.out.println("* Step 3: Upload New Template To The Temporary Server *");
        System.out.println("*******************************************************");

        String filePath = filePathConfig.getNewTemplate();
        writeToDatabase(filePath);

        System.out.println("******************");
        System.out.println("* Finish Step 3! *");
        System.out.println("******************");
    }


    private void writeToDatabase(String filePath) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 0,1);
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 1,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
