
package com.whcis.data.ap.oldtemplate;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class PenaltyOldTemplateToTempServer {

    private static String url = "jdbc:mysql://localhost:3306/db_credit_test?useSSL=false";

    private static String user = "root";

    private static String password = "kaifa001";

    private static Connection con;

    private static String sPath = "/Users/neo/Downloads/20161202/O1128.xls";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(String[] args) {
        // open connection
        openDatabase();
        // write data
        writeToDatabase();

        System.out.println("OK!");
    }

    private static void openDatabase() {
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeToDatabase() {
        try {
            Workbook readWB = Workbook.getWorkbook(new File(sPath));
            Sheet readsheet = readWB.getSheet(1);
            int rsColumns = readsheet.getColumns();
            int rsRows = readsheet.getRows();
            for (int i = 3; i < rsRows; i++) {
                for (int j = 0; j < rsColumns; j++) {
                    Cell cell = readsheet.getCell(j, i);
                    PenalyWHBean.setX(j, cell.getContents());
                }
                insertINTO(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertINTO(int intRow) {
        try {
            if (PenalyWHBean.CF_XDR_MC.contains("表格说明") || PenalyWHBean.isEmpty()) {
                return;
            }
            con.createStatement()
                    .execute(
                            "INSERT INTO tab_penaly_wuhan_month (`CF_XDR_MC`,`CF_FR`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_AJMC`,`CF_CFLB1`,`CF_WSH`,`CF_SY`,`CF_YJ`,`CF_JG`,`CF_JDRQ`,`CF_JZRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`,`QT`,`CF_CFMC`) VALUES "
                                    + PenalyWHBean.toValues());
        } catch (Exception e) {
            System.out
                    .println(intRow + " insert failed: " + PenalyWHBean.toValues());
        } finally {
            PenalyWHBean.clean();
        }
    }
}
