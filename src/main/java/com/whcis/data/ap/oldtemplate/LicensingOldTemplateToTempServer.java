package com.whcis.data.ap.oldtemplate;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class LicensingOldTemplateToTempServer {
    private static String url = "jdbc:mysql://localhost:3306/db_credit_test?useSSL=false";

    private static String user = "root";

    private static String password = "kaifa001";

    private static Connection con;

//    private static String sPath = "/Users/neo/Downloads/20161202/O1128.xls";
    private static String sPath = "/Users/neo/Downloads/20161227/O1226.xls";

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
            Sheet readsheet = readWB.getSheet(0);
            int rsColumns = readsheet.getColumns();
            int rsRows = readsheet.getRows();
            for (int i = 3; i < rsRows; i++) {
                for (int j = 0; j < rsColumns; j++) {
                    Cell cell = readsheet.getCell(j, i);
                    PermissonWHBean.setX(j, cell.getContents());
                }
                insertINTO(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertINTO(int intRow) {
        try {
            if (PermissonWHBean.XK_XDR.contains("表格说明") || PermissonWHBean.isEmpty()) {
                return;
            }
            con.createStatement()
                    .execute(
                            "INSERT INTO tab_permisson_wuhan_month (`XK_XDR`,`XK_FR`,`XK_XDR_SHXYM`,`XK_XDR_ZDM`,`XK_XDR_GSDJ`,`XK_XDR_SWDJ`,`XK_XDR_SFZ`,`XK_XMMC`,`XK_SPLB`,`XK_WSH`,`XK_NR`,`XK_JDRQ`,`XK_JZQ`,`XK_XZJG`,`XK_ZT`,`DFBM`,`SJC`,`BZ`,`QTXX`,`SJMC`) VALUES "
                                    + PermissonWHBean.toValues());
        } catch (Exception e) {
            System.out
                    .println(intRow + " insert failed: " + PermissonWHBean.toValues());
        } finally {
            PermissonWHBean.clean();
        }
    }
}
