package com.tank.es.searcher.agg;

/**
 * @migrate sezina
 * migrated by chenxueyan
 * Created by zhongshu on 2016/10/3 13:45.
 */
public class HawkeyeAgg {

    private String aggName;
    private String aggField;
    private Integer size;

    public HawkeyeAgg() {
    }

    public HawkeyeAgg(String aggField, Integer size) {
        this.aggName = aggField;
        this.aggField = aggField;
        this.size = size;
    }

    public String getAggName() {
        return aggName;
    }

    public void setAggName(String aggName) {
        this.aggName = aggName;
    }

    public String getAggField() {
        return aggField;
    }

    public void setAggField(String aggField) {
        this.aggField = aggField;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
