package com.tank.es.searcher.agg;

/**
 *  migrated by chenxueyan
 * Created by zhongshu on 2016/10/11 18:34.
 */
public class SumAgg extends HawkeyeAgg {

    public SumAgg(String aggField, Integer size) {
        super(aggField, size);
    }

    public SumAgg(String name){
        this.setAggName(name);
    }
}