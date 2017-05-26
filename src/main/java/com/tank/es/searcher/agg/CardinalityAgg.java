package com.tank.es.searcher.agg;

/**
 * @migrate sezina
 * Created by lin on 2016/10/10.
 */
public class CardinalityAgg extends HawkeyeAgg {

    public CardinalityAgg(String aggField, Integer size) {
        super(aggField, size);
    }
}
