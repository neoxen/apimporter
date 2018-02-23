
package com.whcis.data.ap.newtemplate;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.util.FileUtil;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@EnableConfigurationProperties({FilePathConfig.class})
public class NewTemplateUploadToTempServer {
    private static final Logger logger = LoggerFactory.getLogger(NewTemplateUploadToTempServer.class);

    private FilePathConfig filePathConfig;

    private JdbcTemplate tempJdbcTemplate;

    @Autowired
    public NewTemplateUploadToTempServer(FilePathConfig filePathConfig, JdbcTemplate tempJdbcTemplate) {
        this.filePathConfig = filePathConfig;
        this.tempJdbcTemplate = tempJdbcTemplate;
    }

    public void stepTwo() {
        logger.info("========== Step 2: Upload XyChina To The Temporary Server ==========");

        String filePath = filePathConfig.getXyChina();
        writeToDatabase(filePath,1);

        logger.info("========== Finish Step 2! ==========");
    }

    public void stepThree() {
        logger.info("========== Step 3: Upload New Template To The Temporary Server ==========");

        String filePath = filePathConfig.getNewTemplate();
        writeToDatabase(filePath,3);

        logger.info("========== Finish Step 3! ==========");
    }

    public void stepWhcic() {
        logger.info("========== Step Whcic: Upload Whcic Data To The Temporary Server ==========");

        String filePath = filePathConfig.getXyChina();
        writeToDatabase(filePath,2);

        logger.info("========== Finish Step Whcic! ==========");
    }

    public void stepNormalization() {
        logger.info("========== Step Normalization: Narmalizing Organ Name of The Administrative Publicities ==========");

        int countSQL = normalize("organ-check.sql");

        logger.info("========== Proceed " + countSQL +  " normalization rules ==========");
        logger.info("========== Finish Step Normalization! ==========");
    }


    private void writeToDatabase(String filePath, int source) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 0,1,source);
            FileUtil.proceedFile(readWB,tempJdbcTemplate, 1,1,source);
            readWB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int normalize(String fileName) {
        Resource res = new ClassPathResource(fileName);

        int intReturn = 0 ;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(res.getFile()), "UTF-8"));
            String tempString = null;
            intReturn = 0;

            while ((tempString = reader.readLine()) != null) {
                tempJdbcTemplate.execute(tempString);
                intReturn++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return intReturn;
    }

}
