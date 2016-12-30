package com.whcis.data.ap.newtemplate;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class PenaltyUploadToTempServer {

    private static String url_my = "jdbc:mysql://localhost:3306/db_credit_test?useSSL=false";

    private static String user_my = "root";

    private static String password_my = "kaifa001";

    private static Connection con_my;

//    private static String sPath = "/Users/neo/Downloads/20161202/N1128.xls";
    private static String sPath = "/Users/neo/Downloads/20161227/N1226.xls";

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
            con_my = DriverManager.getConnection(url_my, user_my, password_my);
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
            for (int i = 1; i < rsRows; i++) {
                for (int j = 0; j < rsColumns; j++) {
                    Cell cell = readsheet.getCell(j, i);
                    PenaltyWHBean.setX(j, cell.getContents());
                }
                insertINTO(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertINTO(int intRow) {
        try {
            if (PenaltyWHBean.CF_WSH.contains("表格说明") || PenaltyWHBean.isEmpty()) {
                return;
            }
            con_my.createStatement()
                    .execute(
                            "INSERT INTO tab_penaly_wuhan_month (`CF_WSH`,`CF_CFMC`,`CF_CFLB1`,`CF_CFLB2`,`CF_SY`,`CF_YJ`,`CF_XDR_MC`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_FR`,`CF_JG`,`CF_JDRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`) VALUES "
                                    + PenaltyWHBean.toValues());
        } catch (Exception e) {
            e.printStackTrace();
            System.out
                    .println( intRow + " Insert failed: " + PenaltyWHBean.toValues());
        } finally {
            PenaltyWHBean.clean();
        }
    }

}
