package com.tank.es.searcher.agg;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * FilterAgg
 *
 * @migrate sezina
 * @author chenxueyan
 * @since 16/11/5
 */
public class FilterAgg extends HawkeyeAgg {
    private QueryBuilder queryBuilder;

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public FilterAgg(String aggName , QueryBuilder queryBuilder) {
        super(null, 0); // 没有作用了
        setAggName(aggName);
        this.queryBuilder = queryBuilder;
    }
}
