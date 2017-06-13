package com.whcis.data.ap.util;

import com.whcis.data.ap.config.FilePathConfig;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

    public void highlightDuplicateEntries() {
        logger.info("============================= Highlighting duplicating entries =============================");
        highlightDuplicateEntriesCH();
        highlightDuplicateEntriesBase();
        logger.info("================================ Data Importing Accomplished ===============================");
    }

    public void highlightDuplicateEntriesCH() {
        if (!duplicateEntryCH.isEmpty()) {
            int countL = 0;
            int countP = 0;
            for (Record record : duplicateEntryCH) {
                if (record.getType().equals("licensing")){
                    countL++;
                }
                else {
                    countP++;
                }
            }
            logger.warn("Downloaded from XYChina: " + countL + " duplicating licensings and " + countP + " duplicating penalties." );
        }
    }

    public void highlightDuplicateEntriesBase() {
        if (!duplicateEntryBase.isEmpty()) {
            int countL = 0;
            int countP = 0;
            for (Record record : duplicateEntryBase) {
                if (record.getType().equals("licensing")){
                    countL++;
                }
                else {
                    countP++;
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Downloaded from XYChina: " + countLicensingCH + " licensings and " + countPenaltyCH + " penalties.");
        logger.info("Reported of New Template: " + countLicensingNew + " licensings and " + countPenaltyNew + " penalties.");
        logger.info("Reported of Old Template: " + countLicensingOld + " licensings and " + countPenaltyOld + " penalties.");
        logger.info("Total to proceeding: " + countLicensingCH+countLicensingNew+countLicensingOld + " licensings and " + countPenaltyCH+countPenaltyNew+countPenaltyOld + " penalties.");
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
