package com.tank.es.searcher.agg;

/**
 *
 * @migrate sezina
 * @author zhongshu
 * @since 2017/1/20 11:26
 */
public class MaxAgg extends HawkeyeAgg {

    public MaxAgg(String name) {
        this.setAggName(name);
        this.setAggField(name);
    }
}