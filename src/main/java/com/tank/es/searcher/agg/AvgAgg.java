package com.tank.es.searcher.agg;

/**
 *
 * @migrate sezina
 * @author zhongshu
 * @since 2017/1/19 17:32
 */
public class AvgAgg extends HawkeyeAgg {

    private long missing;

    public AvgAgg(String name, long missing) {
        this.setAggField(name);
        this.setAggName(name);
        this.missing = missing;
    }

    public long getMissing() {
        return missing;
    }

    public void setMissing(long missing) {
        this.missing = missing;
    }
}
