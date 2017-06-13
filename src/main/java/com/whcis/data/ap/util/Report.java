package com.whcis.data.ap.util;

import java.util.ArrayList;

/**
 * Created by neo on 2017/6/9.
 */
public class Report {
    private ArrayList<Record> duplicateEntryCH;
    private ArrayList<Record> duplicateEntryBase;

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
        highlightDuplicateEntriesCH();
        highlightDuplicateEntriesBase();
    }

    public void highlightDuplicateEntriesCH() {

    }

    public void highlightDuplicateEntriesBase() {

    }
}
