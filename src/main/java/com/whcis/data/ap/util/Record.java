package com.whcis.data.ap.util;

/**
 * Created by neo on 2017/6/9.
 */
public class Record {
    private String type;
    private Integer index;

    public Record() {
        this.type = "";
        this.index = 0;
    }

    public Record(String type, Integer index) {
        this.type = type;
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
