package com.tank.es.searcher;

import com.tank.es.searcher.agg.HawkeyeAgg;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Description:es搜素接口
 * User： JinHuaTao
 * Date：2017/4/11
 * Time: 18:45
 */
public interface ISearcher {

    SearchHit[] search(String index, String type, QueryBuilder queryBuilder, int offset, int size, SortBuilder sortBuilder);

    SearchHit[] search(String index, String type, QueryBuilder queryBuilder, SortBuilder sortBuilder);

    long count(String index, String type, QueryBuilder queryBuilder);

    void update(String index, String type, String id, String parent, Map<String, Object> updateSource) throws ExecutionException, InterruptedException;

    long updateByQuery(String index, String type, QueryBuilder queryBuilder, Script script);

    Map<String, Object> get(String index, String type, String id, String parent);

    Map<String, Object> getChildByUnknownParent(String index, String type, String id);

    LinkedHashMap<String, Object> getNTermsAgg(String index, String type, QueryBuilder queryBuilder, LinkedList<HawkeyeAgg> hawkeyeAggList, boolean... flag) throws Exception;

    Map<String, Aggregation> aggregations(String index, String type, QueryBuilder queryBuilder, List<AbstractAggregationBuilder> aggregationBuilders) throws Exception;

    Map<String, Double> get1TermsAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception;

    LinkedHashMap<String, Double> getSumAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception;

    LinkedHashMap<String, Double> getAvgAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception;

    LinkedHashMap<String, Double> getMaxAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception;

    LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> get3TermsAggWithChild(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception;
    
}
