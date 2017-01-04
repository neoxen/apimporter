
package com.whcis.data.ap.newtemplate;

import com.whcis.data.ap.config.FilePathConfig;
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
public class LicensingUploadToTempServer {

    private FilePathConfig filePathConfig;

    private JdbcTemplate tempJdbcTemplate;

    @Autowired
    public LicensingUploadToTempServer(FilePathConfig filePathConfig, JdbcTemplate tempJdbcTemplate) {
        this.filePathConfig = filePathConfig;
        this.tempJdbcTemplate = tempJdbcTemplate;
    }

    public void stepOne() {
        System.out.println("*******************************************************************");
        System.out.println("* Step One: Extract The Source And Upload To The Temporary Server *");
        System.out.println("*******************************************************************");

        String filePath = filePathConfig.getNewTemplate();
        writeToDatabase(filePath);

        System.out.println("********************");
        System.out.println("* Finish Step One! *");
        System.out.println("********************");
    }


    private void writeToDatabase(String filePath) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));

            System.out.println("Start inserting licensings ...");
            Sheet licensingSheet = readWB.getSheet(0);
            int lColumns = licensingSheet.getColumns();
            int lRows = licensingSheet.getRows();
            for (int i = 1; i < lRows; i++) {
                for (int j = 0; j < lColumns; j++) {
                    Cell cell = licensingSheet.getCell(j, i);
                    LicensingWHBean.setX(j, cell.getContents());
                }
                insertLicensing(i);
            }
            System.out.println("Finish inserting licensings ...");

            System.out.println("Start inserting penalties ...");
            Sheet penaltySheet = readWB.getSheet(1);
            int pColumns = penaltySheet.getColumns();
            int pRows = penaltySheet.getRows();
            for (int i = 1; i < pRows; i++) {
                for (int j = 0; j < pColumns; j++) {
                    Cell cell = penaltySheet.getCell(j, i);
                    PenaltyWHBean.setX(j, cell.getContents());
                }
                insertPenalty(i);
            }
            System.out.println("Finish inserting penalties ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertLicensing(int intRow) {
        try {
            if (LicensingWHBean.XK_WSH.contains("表格说明") || LicensingWHBean.isEmpty()) {
                return;
            }
            tempJdbcTemplate.execute(
                            "INSERT INTO tab_permisson_wuhan_month (`XK_WSH`,`XK_XMMC`,`XK_SPLB`,`XK_NR`,`XK_XDR`,`XK_XDR_SHXYM`,`XK_XDR_ZDM`,`XK_XDR_GSDJ`,`XK_XDR_SWDJ`,`XK_XDR_SFZ`,`XK_FR`,`XK_JDRQ`,`XK_JZQ`,`XK_XZJG`,`XK_ZT`,`DFBM`,`SJC`,`BZ`) VALUES "
                                    + LicensingWHBean.toValues());

        } catch (Exception e) {
            System.out
                    .println(
                            intRow + "Licensing insert failed: " + LicensingWHBean.toValues());
            e.printStackTrace();
        } finally {
            LicensingWHBean.clean();
        }
    }

    private void insertPenalty(int intRow) {
        try {
            if (PenaltyWHBean.CF_WSH.contains("表格说明") || PenaltyWHBean.isEmpty()) {
                return;
            }
            tempJdbcTemplate.execute(
                            "INSERT INTO tab_penaly_wuhan_month (`CF_WSH`,`CF_CFMC`,`CF_CFLB1`,`CF_CFLB2`,`CF_SY`,`CF_YJ`,`CF_XDR_MC`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_FR`,`CF_JG`,`CF_JDRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`) VALUES "
                                    + PenaltyWHBean.toValues());
        } catch (Exception e) {
            e.printStackTrace();
            System.out
                    .println( intRow + "Penalty insert failed: " + PenaltyWHBean.toValues());
        } finally {
            PenaltyWHBean.clean();
        }
    }
}
