
package com.whcis.data.ap.newtemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class LicensingUploadToCreditHubei {


//    private static String url_ser = "jdbc:mysql://192.168.18.110:3306/upload_to_xychina?useSSL=false";
//
//    private static String user_ser = "zxdc";
//
//    private static String password_ser = "zxdc";

    private static String url_ser = "jdbc:mysql://localhost:3306/upload_to_xychina?useSSL=false";

    private static String user_ser = "root";

    private static String password_ser = "kaifa001";

    private static Connection con_ser;

//    private static String sPath = "/Users/neo/Downloads/20161202/N2016-11-30.xls";
    private static String sPath = "/Users/neo/Downloads/20161227/N2016-12-21.xls";

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
            con_ser = DriverManager.getConnection(url_ser, user_ser, password_ser);
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
            // duplication check
            ResultSet rs = con_ser.createStatement().executeQuery("select count(*) as rowCount from licensing_tem where " + LicensingWHBean.toID());
            // ResultSet rs =
            // con_my.createStatement().executeQuery("select count(*) as rowCount from licensing_tem where "
            // + LicensingWHBean.toID());
            rs.next();
            if (rs.getInt("rowCount") > 0) {
                System.out.println(intRow + " Record duplicated: " + LicensingWHBean.toValues());
                return;
            }
            con_ser.createStatement()
                    .execute(
                            "INSERT INTO licensing_tem (`XK_WSH`,`XK_XMMC`,`XK_SPLB`,`XK_NR`,`XK_XDR`,`XK_XDR_SHXYM`,`XK_XDR_ZDM`,`XK_XDR_GSDJ`,`XK_XDR_SWDJ`,`XK_XDR_SFZ`,`XK_FR`,`XK_JDRQ`,`XK_JZQ`,`XK_XZJG`,`XK_ZT`,`DFBM`,`SJC`,`BZ`) VALUES "
                                    + LicensingWHBean.toValues());

        } catch (Exception e) {
            System.out
                    .println(intRow + " Insert failed: " + LicensingWHBean.toValues());
            e.printStackTrace();
        } finally {
            LicensingWHBean.clean();
        }
    }
}
