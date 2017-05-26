package com.tank.es.searcher.agg;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.List;

/**
 *  migrated by chenxueyan
 * Created by zhongshu on 2016/10/3 13:50.
 */
public class TermsAgg extends HawkeyeAgg {

    private List<String> includeList;
    private List<String> excludeList;
    private String includeRegexp;
    private String excludeRegexp;
    private Terms.Order order;

    public TermsAgg(String aggField, int size) {
        super(aggField, size);
    }

    public List<String> getIncludeList() {
        return includeList;
    }

    public void setIncludeList(List<String> includeList) {
        this.includeList = includeList;
    }

    public List<String> getExcludeList() {
        return excludeList;
    }

    public void setExcludeList(List<String> excludeList) {
        this.excludeList = excludeList;
    }

    public String getIncludeRegexp() {
        return includeRegexp;
    }

    public void setIncludeRegexp(String includeRegexp) {
        this.includeRegexp = includeRegexp;
    }

    public String getExcludeRegexp() {
        return excludeRegexp;
    }

    public void setExcludeRegexp(String excludeRegexp) {
        this.excludeRegexp = excludeRegexp;
    }

    public Terms.Order getOrder() {
        return order;
    }

    public void setOrder(Terms.Order order) {
        this.order = order;
    }
}