
package com.whcis.data.ap.newtemplate;

import com.whcis.data.ap.config.FilePathConfig;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@EnableConfigurationProperties({FilePathConfig.class})
public class UploadToCreditHubei {

    private FilePathConfig filePathConfig;

    private JdbcTemplate xychinaJdbcTemplate;

    @Autowired
    public UploadToCreditHubei(FilePathConfig filePathConfig, JdbcTemplate xychinaJdbcTemplate) {
        this.filePathConfig = filePathConfig;
        this.xychinaJdbcTemplate = xychinaJdbcTemplate;
    }

    public void stepOne() {
        System.out.println("******************************************************");
        System.out.println("* Step 1: Upload New Template To Credit Hubei Server *");
        System.out.println("******************************************************");

        String filePath = filePathConfig.getXyChina();
        writeToDatabase(filePath);

        System.out.println("******************");
        System.out.println("* Finish Step 1! *");
        System.out.println("******************");
    }

    private void writeToDatabase(String filePath) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));

            proceedFile(readWB, 0);
            proceedFile(readWB, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proceedFile (Workbook readWB, int type) {
        if (type == 0) {
            System.out.println("Start inserting licensings ...");
        } else {
            System.out.println("Start inserting penalties ...");
        }
        Sheet penaltySheet = readWB.getSheet(type);
        int pColumns = penaltySheet.getColumns();
        int pRows = penaltySheet.getRows();
        for (int i = 1; i < pRows; i++) {
            for (int j = 0; j < pColumns; j++) {
                Cell cell = penaltySheet.getCell(j, i);
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
            System.out.println("Finish inserting licensings ...");
        } else {
            System.out.println("Finish inserting penalties ...");
        }
    }

    private void insertLicensing(int intRow) {
        int index = intRow + 1;
        try {
            if (LicensingNT.XK_WSH.contains("表格说明") || LicensingNT.isEmpty()) {
                return;
            }
            // duplication check
            String query = "select * from licensing_tem where " + LicensingNT.toID() + " limit 1";

            SqlRowSet rowSet = xychinaJdbcTemplate.queryForRowSet(query);

            if (!rowSet.wasNull()) {
                System.out.println("Line " + index + " Record duplicated: " + LicensingNT.toValues());
                return;
            }
            xychinaJdbcTemplate.execute(
                            "INSERT INTO licensing_tem (`XK_WSH`,`XK_XMMC`,`XK_SPLB`,`XK_NR`,`XK_XDR`,`XK_XDR_SHXYM`,`XK_XDR_ZDM`,`XK_XDR_GSDJ`,`XK_XDR_SWDJ`,`XK_XDR_SFZ`,`XK_FR`,`XK_JDRQ`,`XK_JZQ`,`XK_XZJG`,`XK_ZT`,`DFBM`,`SJC`,`BZ`) VALUES "
                                    + LicensingNT.toValues());

        } catch (Exception e) {
            System.out
                    .println("Line " + index + " Insert failed: " + LicensingNT.toValues());
            e.printStackTrace();
        } finally {
            LicensingNT.clean();
        }
    }

    private void insertPenalty(int intRow) {
        int index = intRow + 1;
        try {
            if (PenaltyNT.CF_WSH.contains("表格说明") || PenaltyNT.isEmpty()) {
                return;
            }
            // duplication check
            String query = "select * from penaly_tem where " + PenaltyNT.toID() + " limit 1";

            SqlRowSet rowSet = xychinaJdbcTemplate.queryForRowSet(query);

            if (!rowSet.wasNull()) {
                System.out.println("Line " + index + " Record duplicated: " + PenaltyNT.toValues());
                return;
            }
            xychinaJdbcTemplate.execute(
                            "INSERT INTO penaly_tem (`CF_WSH`,`CF_CFMC`,`CF_CFLB1`,`CF_CFLB2`,`CF_SY`,`CF_YJ`,`CF_XDR_MC`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_FR`,`CF_JG`,`CF_JDRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`) VALUES "
                                    + PenaltyNT.toValues());
        } catch (Exception e) {
            e.printStackTrace();
            System.out
                    .println("Line " + index + " Insert failed: " + PenaltyNT.toValues());
        } finally {
            PenaltyNT.clean();
        }
    }
}
