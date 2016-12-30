
package com.whcis.data.ap.newtemplate;

import com.whcis.data.ap.config.FilePathConfig;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@EnableConfigurationProperties({FilePathConfig.class})
public class LicensingUploadToTempServer {

    @Autowired
    private FilePathConfig filePathConfig;

    @Autowired
    @Qualifier("tempJdbcTemplate")
    private JdbcTemplate tempJdbcTemplate;

    public void stepOne() {
        System.out.println("*******************************************************************");
        System.out.println("* Step One: Extract The Source And Upload To The Temporary Server *");
        System.out.println("*******************************************************************");
        System.out.println("Start uploading ...");

        String filePath = filePathConfig.getNewTemplate();
        writeToDatabase(filePath);

        System.out.println("Finished data uploading!");
    }


    private void writeToDatabase(String filePath) {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(filePath));
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

    private void insertINTO(int intRow) {
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
