
package com.whcis.data.ap.newtemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public class LicensingUploadToTempServer {

    @Autowired
    @Qualifier("tempJdbcTemplate")
    private static JdbcTemplate tempJdbcTemplate;

    private static String sPath = "/Users/neo/Downloads/20161202/N1128.xls";

    public static void main(String[] args) {
        // write data
        writeToDatabase();

        System.out.println("OK!");
    }

    private static void writeToDatabase() {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(sPath));
            Sheet readsheet = readWB.getSheet(0);
            int rsColumns = readsheet.getColumns();
            int rsRows = readsheet.getRows();
            for (int i = 1; i < rsRows; i++) {
                for (int j = 0; j < rsColumns; j++) {
                    Cell cell = readsheet.getCell(j, i);
                    LicensingWHBean.setX(j, cell.getContents());
                }
                insertINTO(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertINTO(int intRow) {
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
                            intRow + " Insert failed: " + LicensingWHBean.toValues());
            e.printStackTrace();
        } finally {
            LicensingWHBean.clean();
        }
    }
}
