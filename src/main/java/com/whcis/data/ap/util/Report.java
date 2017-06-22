package com.whcis.data.ap.util;

import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import com.whcis.data.ap.config.FilePathConfig;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by neo on 2017/6/9.
 */
public class Report {
    private static final Logger logger = LoggerFactory.getLogger(Report.class);
    private ArrayList<Record> duplicateEntryCH;
    private ArrayList<Record> duplicateEntryBase;

    private int countLicensingCH = 0;
    private int countPenaltyCH = 0;
    private int countLicensingNew = 0;
    private int countPenaltyNew = 0;
    private int countLicensingOld = 0;
    private int countPenaltyOld = 0;

    public Report() {
        this.duplicateEntryCH = new ArrayList<>();
        this.duplicateEntryBase = new ArrayList<>();
    }

    public Report(ArrayList<Record> duplicateEntryCH, ArrayList<Record> duplicateEntryBase) {
        this.duplicateEntryCH = duplicateEntryCH;
        this.duplicateEntryBase = duplicateEntryBase;
    }

    public ArrayList<Record> getDuplicateEntryCH() {
        return duplicateEntryCH;
    }

    public ArrayList<Record> getDuplicateEntryBase() {
        return duplicateEntryBase;
    }

    public void setDuplicateEntryCH(ArrayList<Record> duplicateEntryCH) {
        this.duplicateEntryCH = duplicateEntryCH;
    }

    public void setDuplicateEntryBase(ArrayList<Record> duplicateEntryBase) {
        this.duplicateEntryBase = duplicateEntryBase;
    }

    public void highlightDuplicateEntries(FilePathConfig filePathConfig) {
        logger.info("============================= Highlighting duplicating entries =============================");
        highlightDuplicateEntriesCH(filePathConfig);
        highlightDuplicateEntriesBase(filePathConfig);
        logger.info("================================ Data Importing Accomplished ===============================");
    }

    public void highlightDuplicateEntriesCH(FilePathConfig filePathConfig) {
        if (!duplicateEntryCH.isEmpty()) {
            int countL = 0;
            int countP = 0;

            String filePathConfigXyChina = filePathConfig.getXyChina();
            String newFilePathXyChina = filePathConfigXyChina.substring(0, filePathConfigXyChina.indexOf(".")).concat("-信用中国重复.xls");

            try {
                Workbook wbXyChina = Workbook.getWorkbook(new File(filePathConfigXyChina));
                WritableWorkbook wwbXyChina = Workbook.createWorkbook(new File(newFilePathXyChina), wbXyChina);

                WritableSheet sheetLCH= wwbXyChina.getSheet(0);
                WritableSheet sheetPCH = wwbXyChina.getSheet(1);

                int intColumnLCH = sheetLCH.getColumns();
                int intColumnPCH = sheetPCH.getColumns();

                WritableFont wf = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);
                WritableCellFormat wcf = new WritableCellFormat(wf);
                wcf.setBackground(Colour.LIGHT_GREEN);
                wcf.setBorder(Border.ALL, BorderLineStyle.DOTTED, Colour.BROWN);

                for (Record record : duplicateEntryCH) {
                    if (record.getType().equals("licensing")){
                        countL++;
                        for (int i =0; i<intColumnLCH;i++) {
                            sheetLCH.getWritableCell(i, record.getIndex()).setCellFormat(wcf);
                        }
                    }
                    else {
                        countP++;
                        for (int i =0; i<intColumnPCH;i++) {
                            sheetPCH.getWritableCell(i, record.getIndex()).setCellFormat(wcf);
                        }
                    }
                }

                wwbXyChina.write();
                wwbXyChina.close();
                wbXyChina.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.warn("Downloaded from CreditHubei: " + countL + " duplicating licensings and " + countP + " duplicating penalties." );
        }
    }

    public void highlightDuplicateEntriesBase(FilePathConfig filePathConfig) {
        if (!duplicateEntryBase.isEmpty()) {
            int countL = 0;
            int countP = 0;

            String filePathXyChina = filePathConfig.getXyChina();
            String filePathNewTemp = filePathConfig.getNewTemplate();
            String filePathOldTemp = filePathConfig.getOldTemplate();

            String newFilePathXyChina = filePathXyChina.substring(0, filePathXyChina.indexOf(".")).concat("-双公示库重复.xls");
            String newFilePathNewTemp = filePathNewTemp.substring(0, filePathNewTemp.indexOf(".")).concat("-双公示库重复.xls");
            String newFilePathOldTemp = filePathOldTemp.substring(0, filePathOldTemp.indexOf(".")).concat("-双公示库重复.xls");


            try {
                Workbook wbXyChina = Workbook.getWorkbook(new File(filePathXyChina));
                WritableWorkbook wwbXyChina = Workbook.createWorkbook(new File(newFilePathXyChina), wbXyChina);

                Workbook wbNewTemp = Workbook.getWorkbook(new File(filePathNewTemp));
                WritableWorkbook wwbNewTemp = Workbook.createWorkbook(new File(newFilePathNewTemp), wbNewTemp);

                Workbook wbOldTemp = Workbook.getWorkbook(new File(filePathOldTemp));
                WritableWorkbook wwbOldTemp = Workbook.createWorkbook(new File(newFilePathOldTemp), wbOldTemp);

                WritableSheet sheetLCH = wwbXyChina.getSheet(0);
                WritableSheet sheetPCH = wwbXyChina.getSheet(1);

                WritableSheet sheetLNT = wwbNewTemp.getSheet(0);
                WritableSheet sheetPNT = wwbNewTemp.getSheet(1);

                WritableSheet sheetLOT = wwbOldTemp.getSheet(0);
                WritableSheet sheetPOT = wwbOldTemp.getSheet(1);

                int intColumnLCH = sheetLCH.getColumns();
                int intColumnPCH = sheetPCH.getColumns();

                int intColumnLNT = sheetLNT.getColumns();
                int intColumnPNT = sheetPNT.getColumns();

                int intColumnLOT = sheetLOT.getColumns();
                int intColumnPOT = sheetPOT.getColumns();

                WritableFont wf = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);
                WritableCellFormat greencf = new WritableCellFormat(wf);
                greencf.setBackground(Colour.LIGHT_GREEN);
                greencf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BROWN);

                WritableCellFormat redcf = new WritableCellFormat(wf);
                redcf.setBackground(Colour.RED);
                redcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BROWN);

                WritableCellFormat bluecf = new WritableCellFormat(wf);
                bluecf.setBackground(Colour.AQUA);
                bluecf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BROWN);

                for (Record record : duplicateEntryBase) {
                    if (record.getType().equals("licensing")){
                        countL++;

                        if (record.getIndex() <= countLicensingCH) {
                            for (int i = 0; i < intColumnLCH; i++) {
                                sheetLCH.getWritableCell(i, record.getIndex()).setCellFormat(bluecf);
                            }
                        }
                        else if (countLicensingCH < record.getIndex() && record.getIndex() <= (countLicensingCH+countLicensingNew)) {
                            for (int i = 0; i < intColumnLNT; i++) {
                                sheetLNT.getWritableCell(i, record.getIndex()-countLicensingCH).setCellFormat(bluecf);
                            }
                        } else {
                            for (int i = 0; i < intColumnLOT; i++) {
                                sheetLOT.getWritableCell(i, record.getIndex()-countLicensingCH-countLicensingNew+2).setCellFormat(bluecf);
                            }
                        }
                    }
                    else {
                        countP++;

                        if (record.getIndex() <= countPenaltyCH) {
                            for (int i = 0; i < intColumnPCH; i++) {
                                sheetPCH.getWritableCell(i, record.getIndex()).setCellFormat(bluecf);
                            }
                        }
                        else if (countPenaltyCH < record.getIndex() && record.getIndex() <= (countPenaltyCH+countPenaltyNew)) {
                            for (int i = 0; i < intColumnPNT; i++) {
                                sheetPNT.getWritableCell(i, record.getIndex()-countPenaltyCH).setCellFormat(bluecf);
                            }
                        } else {
                            for (int i = 0; i < intColumnPOT; i++) {
                                sheetPOT.getWritableCell(i, record.getIndex()-countPenaltyCH-countPenaltyNew+2).setCellFormat(bluecf);
                            }
                        }
                    }
                }

                wwbXyChina.write();
                wwbXyChina.close();
                wbXyChina.close();

                wwbNewTemp.write();
                wwbNewTemp.close();
                wbNewTemp.close();

                wwbOldTemp.write();
                wwbOldTemp.close();
                wbOldTemp.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.warn("Importing into Base Server: " + countL + " duplicating licensings and " + countP + " duplicating penalties." );

        }
    }

    public void countEntries(FilePathConfig filePathConfig) {
        logger.info("============================= Summary =============================");
        String filePathConfigXyChina = filePathConfig.getXyChina();
        String filePathNewTemp = filePathConfig.getNewTemplate();
        String filePathOldTemp = filePathConfig.getOldTemplate();
        try {
            Workbook wbXyChina = Workbook.getWorkbook(new File(filePathConfigXyChina));
            Sheet sheetLCH= wbXyChina.getSheet(0);
            countLicensingCH = sheetLCH.getRows() - 1;
            Sheet sheetPCH = wbXyChina.getSheet(1);
            countPenaltyCH = sheetPCH.getRows() - 1;

            Workbook wbNewTemp = Workbook.getWorkbook(new File(filePathNewTemp));
            Sheet sheetLNP= wbNewTemp.getSheet(0);
            countLicensingNew = sheetLNP.getRows() - 1;
            Sheet sheetPNP = wbNewTemp.getSheet(1);
            countPenaltyNew = sheetPNP.getRows() - 1;

            Workbook wbOldTemp = Workbook.getWorkbook(new File(filePathOldTemp));
            Sheet sheetLOP= wbOldTemp.getSheet(0);
            countLicensingOld = sheetLOP.getRows() - 3;
            Sheet sheetPOP = wbOldTemp.getSheet(1);
            countPenaltyOld = sheetPOP.getRows() - 3;

            wbXyChina.close();
            wbNewTemp.close();
            wbOldTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int sumLicensing = countLicensingCH + countLicensingNew + countLicensingOld;
        int sumPenalty = countPenaltyCH + countPenaltyNew + countPenaltyOld;
        logger.info("Downloaded from CreditHubei: " + countLicensingCH + " licensings and " + countPenaltyCH + " penalties.");
        logger.info("Reported in New Template: " + countLicensingNew + " licensings and " + countPenaltyNew + " penalties.");
        logger.info("Reported in Old Template: " + countLicensingOld + " licensings and " + countPenaltyOld + " penalties.");
        logger.info("Total to proceeding: " + sumLicensing + " licensings and " + sumPenalty + " penalties.");
    }



    public int getCountLicensingCH() {
        return countLicensingCH;
    }

    public void setCountLicensingCH(int countLicensingCH) {
        this.countLicensingCH = countLicensingCH;
    }

    public int getCountPenaltyCH() {
        return countPenaltyCH;
    }

    public void setCountPenaltyCH(int countPenaltyCH) {
        this.countPenaltyCH = countPenaltyCH;
    }

    public int getCountLicensingNew() {
        return countLicensingNew;
    }

    public void setCountLicensingNew(int countLicensingNew) {
        this.countLicensingNew = countLicensingNew;
    }

    public int getCountPenaltyNew() {
        return countPenaltyNew;
    }

    public void setCountPenaltyNew(int countPenaltyNew) {
        this.countPenaltyNew = countPenaltyNew;
    }

    public int getCountLicensingOld() {
        return countLicensingOld;
    }

    public void setCountLicensingOld(int countLicensingOld) {
        this.countLicensingOld = countLicensingOld;
    }

    public int getCountPenaltyOld() {
        return countPenaltyOld;
    }

    public void setCountPenaltyOld(int countPenaltyOld) {
        this.countPenaltyOld = countPenaltyOld;
    }
}
