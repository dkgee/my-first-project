package com.tank.es.searcher.agg;

/**
 * @migrate sezina
 * Created by Administrator on 2016/10/22.
 */
public class ChildAgg extends HawkeyeAgg {

    public ChildAgg(String aggName, String aggField, Integer size) {
        super(aggField, size);
        setAggName(aggName);
    }

}
