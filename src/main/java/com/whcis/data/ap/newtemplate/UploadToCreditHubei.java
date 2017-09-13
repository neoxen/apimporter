
package com.whcis.data.ap.newtemplate;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.util.Record;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

@Component
@EnableConfigurationProperties({FilePathConfig.class})
public class UploadToCreditHubei {

    private static final Logger logger = LoggerFactory.getLogger(UploadToCreditHubei.class);

    private FilePathConfig filePathConfig;

    private JdbcTemplate xychinaJdbcTemplate;

    private ArrayList<Record> duplicateEntryCH;

    @Autowired
    public UploadToCreditHubei(FilePathConfig filePathConfig, JdbcTemplate xychinaJdbcTemplate) {
        this.filePathConfig = filePathConfig;
        this.xychinaJdbcTemplate = xychinaJdbcTemplate;
        this.duplicateEntryCH = new ArrayList<>();
    }

    public void stepOne() {
        logger.info("========== Step 1: Upload New Template To Credit Hubei Server ==========");

        String filePath = filePathConfig.getXyChina();
        writeToDatabase(filePath);

        logger.info("========== Finish Step 1! ==========");
    }

    private void writeToDatabase(String filePath) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));

            proceedFile(readWB, 0);
            proceedFile(readWB, 1);

            readWB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proceedFile (Workbook readWB, int type) {
        if (type == 0) {
            logger.info("<===== Start inserting licensings ...");
        } else {
            logger.info("<===== Start inserting penalties ...");
        }
        Sheet sheet = readWB.getSheet(type);
        int intColumns = sheet.getColumns();
        int intRows = sheet.getRows();
        for (int i = 1; i < intRows; i++) {
            for (int j = 0; j < intColumns; j++) {
                Cell cell = sheet.getCell(j, i);
                if (type == 0) {
                    LicensingNT.setX(j, cell.getContents());
                } else {
                    PenaltyNT.setX(j, cell.getContents());
                }
            }
            if (type == 0) {
                insertLicensing(i);
            } else {
                insertPenalty(i);
            }
        }
        if (type == 0) {
            logger.info("<===== Finish inserting licensings ...");
        } else {
            logger.info("<===== Finish inserting penalties ...");
        }
    }

    private boolean duplicateChecking(int type) {
        String query;
        if (type == 0) {
            query = "select * from licensing_tem where " + LicensingNT.toID() + " limit 1";
        } else {
            query = "select * from penaly_tem where " + PenaltyNT.toID() + " limit 1";
        }

        SqlRowSet rowSet = xychinaJdbcTemplate.queryForRowSet(query);

        return rowSet.next();

    }

    private void insertLicensing(int intRow) {
        int index = intRow + 1;
        try {
            if (LicensingNT.XK_WSH.contains("表格说明") || LicensingNT.isEmpty()) {
                logger.error("Line " + index + " ignored. Object name is null!");
                return;
            }
            // duplication check

            if (duplicateChecking(0)) {
                logger.warn("Line " + index + " Record duplicated: " + LicensingNT.toValues(1));
                Record r = new Record("licensing", intRow);
                duplicateEntryCH.add(r);
                return;
            }
            xychinaJdbcTemplate.execute(
                            "INSERT INTO licensing_tem (`XK_WSH`,`XK_XMMC`,`XK_SPLB`,`XK_NR`,`XK_XDR`,`XK_XDR_SHXYM`,`XK_XDR_ZDM`,`XK_XDR_GSDJ`,`XK_XDR_SWDJ`,`XK_XDR_SFZ`,`XK_FR`,`XK_JDRQ`,`XK_JZQ`,`XK_XZJG`,`XK_ZT`,`DFBM`,`SJC`,`BZ`, `SJMC`, `SOURCE`) VALUES "
                                    + LicensingNT.toValues(1));

        } catch (Exception e) {
            logger.error("Line " + index + " Insert failed: " + LicensingNT.toValues(1));
            e.printStackTrace();
        } finally {
            LicensingNT.clean();
        }
    }

    private void insertPenalty(int intRow) {
        int index = intRow + 1;
        try {
            if (PenaltyNT.CF_WSH.contains("表格说明") || PenaltyNT.isEmpty()) {
                logger.error("Line " + index + " ignored. Object name is null!");
                return;
            }
            // duplication check
            if (duplicateChecking(1)) {
                logger.warn("Line " + index + " Record duplicated: " + PenaltyNT.toValues(1));
                Record r = new Record("penalty", intRow);
                duplicateEntryCH.add(r);
                return;
            }
            xychinaJdbcTemplate.execute(
                            "INSERT INTO penaly_tem (`CF_WSH`,`CF_CFMC`,`CF_CFLB1`,`CF_CFLB2`,`CF_SY`,`CF_YJ`,`CF_XDR_MC`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_FR`,`CF_JG`,`CF_JDRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`,`CF_AJMC`, `GSQX`, `SOURCE`) VALUES "
                                    + PenaltyNT.toValues(1));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Line " + index + " Insert failed: " + PenaltyNT.toValues(1));
        } finally {
            PenaltyNT.clean();
        }
    }

    public ArrayList<Record> getDuplicateEntryCH() {
        return duplicateEntryCH;
    }
}
