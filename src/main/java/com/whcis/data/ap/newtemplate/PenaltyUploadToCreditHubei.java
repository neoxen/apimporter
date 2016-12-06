
package com.whcis.data.ap.newtemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class PenaltyUploadToCreditHubei {
    private static String url_ser = "jdbc:mysql://192.168.18.110:3306/upload_to_xychina?useSSL=false";

    private static String user_ser = "zxdc";

    private static String password_ser = "zxdc";

    private static Connection con_ser;


    private static String sPath = "/Users/neo/Downloads/20161202/NP2016-11-29.xls";

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
            // duplication check
            ResultSet rs = con_ser.createStatement().executeQuery("select count(*) as rowCount from penaly_tem where " + PenaltyWHBean.toID());
            rs.next();
            if (rs.getInt("rowCount") > 0) {
                System.out.println(intRow + " Record duplicated: " + PenaltyWHBean.toValues());
                return;
            }
            con_ser.createStatement()
                    .execute(
                            "INSERT INTO penaly_tem (`CF_WSH`,`CF_CFMC`,`CF_CFLB1`,`CF_CFLB2`,`CF_SY`,`CF_YJ`,`CF_XDR_MC`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_FR`,`CF_JG`,`CF_JDRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`) VALUES "
                                    + PenaltyWHBean.toValues());
        } catch (Exception e) {
            e.printStackTrace();
            System.out
                    .println(intRow + " Insert failed: " + PenaltyWHBean.toValues());
        } finally {
            PenaltyWHBean.clean();
        }
    }

}
