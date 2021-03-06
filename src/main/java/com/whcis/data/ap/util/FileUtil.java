package com.whcis.data.ap.util;

import com.whcis.data.ap.newtemplate.LicensingNT;
import com.whcis.data.ap.newtemplate.PenaltyNT;
import com.whcis.data.ap.oldtemplate.LicensingOT;
import com.whcis.data.ap.oldtemplate.PenaltyOT;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by neo on 2017/1/4.
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void proceedFile(Workbook readWB, JdbcTemplate jdbcTemplate, int type, int offset, int source) {
        if (type == 0) {
            logger.info("<===== Start inserting licensings ...");
        } else {
            logger.info("<===== Start inserting penalties ...");
        }
        Sheet sheet = readWB.getSheet(type);
        int columns = sheet.getColumns();
        int rows = sheet.getRows();
        for (int i = offset; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = sheet.getCell(j, i);
                if (type == 0) {
                    if (offset == 1) {
                        LicensingNT.setX(j, cell.getContents());
                    } else {
                        LicensingOT.setX(j, cell.getContents());
                    }
                } else {
                    if (offset == 1) {
                        PenaltyNT.setX(j, cell.getContents());
                    } else {
                        PenaltyOT.setX(j, cell.getContents());
                    }
                }
            }

            if (type == 0) {
                if (offset == 1) {
                    insertLicensingNT(i, jdbcTemplate, source);
                } else {
                    insertLicensingOT(i, jdbcTemplate, source);
                }
            } else {
                if (offset == 1) {
                    insertPenaltyNT(i, jdbcTemplate, source);
                } else {
                    insertPenaltyOT(i, jdbcTemplate, source);
                }
            }
        }
        if (type == 0) {
            logger.info("<===== Finish inserting licensings ...");
        } else {
            logger.info("<===== Finish inserting penalties ...");
        }
    }

    private static void insertLicensingNT(int intRow, JdbcTemplate jdbcTemplate, int source) {
        int index = intRow + 1;
        try {
            if (LicensingNT.XK_WSH.contains("表格说明") || LicensingNT.isEmpty()) {
                logger.error("Line " + index + " ignored. Object name is null!");
                return;
            }
            jdbcTemplate.execute(
                    "INSERT INTO tab_permisson_wuhan_month (`XK_WSH`,`XK_XMMC`,`XK_SPLB`,`XK_NR`,`XK_XDR`,`XK_XDR_SHXYM`,`XK_XDR_ZDM`,`XK_XDR_GSDJ`,`XK_XDR_SWDJ`,`XK_XDR_SFZ`,`XK_FR`,`XK_JDRQ`,`XK_JZQ`,`XK_XZJG`,`XK_ZT`,`DFBM`,`SJC`,`BZ`,`SJMC`, `SOURCE`) VALUES "
                            + LicensingNT.toValues(source));

        } catch (Exception e) {
            logger.error("Line " + index + " Licensing insert failed: " + LicensingNT.toValues(source));
            e.printStackTrace();
        } finally {
            LicensingNT.clean();
        }
    }

    private static void insertPenaltyNT(int intRow, JdbcTemplate jdbcTemplate, int source) {
        int index = intRow + 1;
        try {
            if (PenaltyNT.CF_WSH.contains("表格说明") || PenaltyNT.isEmpty()) {
                logger.error("Line " + index + " ignored. Object name is null!");
                return;
            }
            jdbcTemplate.execute(
                    "INSERT INTO tab_penaly_wuhan_month (`CF_WSH`,`CF_CFMC`,`CF_CFLB1`,`CF_CFLB2`,`CF_SY`,`CF_YJ`,`CF_XDR_MC`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_FR`,`CF_JG`,`CF_JDRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`, `CF_AJMC`, `GSQX`, `SOURCE`) VALUES "
                            + PenaltyNT.toValues(source));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Line " + index + " Penalty insert failed: " + PenaltyNT.toValues(source));
        } finally {
            PenaltyNT.clean();
        }
    }

    private static void insertLicensingOT(int intRow, JdbcTemplate jdbcTemplate, int source) {
        int index = intRow + 1;
        try {
            if (LicensingOT.XK_XDR.contains("表格说明") || LicensingOT.isEmpty()) {
                logger.error("Line " + intRow + " ignored. Object name is null!");
                return;
            }
            jdbcTemplate.execute(
                            "INSERT INTO tab_permisson_wuhan_month (`XK_XDR`,`XK_FR`,`XK_XDR_SHXYM`,`XK_XDR_ZDM`,`XK_XDR_GSDJ`,`XK_XDR_SWDJ`,`XK_XDR_SFZ`,`XK_XMMC`,`XK_SPLB`,`XK_WSH`,`XK_NR`,`XK_JDRQ`,`XK_JZQ`,`XK_XZJG`,`XK_ZT`,`DFBM`,`SJC`,`BZ`,`QTXX`,`SJMC`, `SOURCE`) VALUES "
                                    + LicensingOT.toValues(source));
        } catch (Exception e) {
            logger.error("Line "+ index + " insert failed: " + LicensingOT.toValues(source));
        } finally {
            LicensingOT.clean();
        }
    }

    private static void insertPenaltyOT(int intRow, JdbcTemplate jdbcTemplate, int source) {
        int index = intRow + 1;
        try {
            if (PenaltyOT.CF_XDR_MC.contains("表格说明") || PenaltyOT.isEmpty()) {
                logger.error("Line " + intRow + " ignored. Object name is null!");
                return;
            }
            jdbcTemplate.execute(
                            "INSERT INTO tab_penaly_wuhan_month (`CF_XDR_MC`,`CF_FR`,`CF_XDR_SHXYM`,`CF_XDR_ZDM`,`CF_XDR_GSDJ`,`CF_XDR_SWDJ`,`CF_XDR_SFZ`,`CF_CFMC`,`CF_CFLB1`,`CF_WSH`,`CF_SY`,`CF_YJ`,`CF_JG`,`CF_JDRQ`,`CF_JZRQ`,`CF_XZJG`,`CF_ZT`,`DFBM`,`SJC`,`BZ`,`QT`,`CF_AJMC`,`GSQX`, `SOURCE`) VALUES "
                                    + PenaltyOT.toValues(source));
        } catch (Exception e) {
            logger.error("Line " + index + " insert failed: " + PenaltyOT.toValues(source));
        } finally {
            PenaltyOT.clean();
        }
    }
}
