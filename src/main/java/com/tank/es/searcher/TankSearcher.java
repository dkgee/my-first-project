package com.tank.es.searcher;

import com.tank.es.base.TankWebConfig;
import com.tank.es.base.Wechat;
import com.tank.es.searcher.agg.HawkeyeAgg;
import com.tank.es.searcher.agg.HawkeyeAggParser;
import com.tank.es.utils.AggWrapUtil;
import com.tank.es.utils.SearchWrapUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkIndexByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 * User： JinHuaTao
 * Date：2017/4/11
 * Time: 16:39
 */

public class TankSearcher implements ISearcher{
    private static final Logger logger = LoggerFactory.getLogger(TankSearcher.class);

    private TransportClient client;

    private static TankSearcher tankSearcher;

    public static TankSearcher getInstance(){
        if(tankSearcher == null){
            synchronized (TankSearcher.class){
                if(tankSearcher == null)
                    tankSearcher = new TankSearcher();
            }
        }
        return tankSearcher;
    }

    public TankSearcher(){
        TankWebConfig config = TankWebConfig.getInstance();
        String[] esHosts = config.getHosts("es.hosts");
        String clusterName = config.getProperty("es.cluster.name");
        Settings settings = Settings.builder().put("cluster.name", clusterName)
                .put("client.transport.ping_timeout", "60s")
                .build();
        logger.info("[es hosts]:", Arrays.asList(esHosts));

        InetSocketTransportAddress[] transportAddresses = new InetSocketTransportAddress[esHosts.length];

        for (int i = 0; i < esHosts.length; i++){
            String[] parts = esHosts[i].split(":");
            try {
                InetAddress inetAddress = InetAddress.getByName(parts[0]);
                transportAddresses[i] = new InetSocketTransportAddress(inetAddress, Integer.parseInt(parts[1]));
            } catch (UnknownHostException e) {
                logger.error("unknown host exception,{}", e);
            }
        }

        try {
            client = new PreBuiltTransportClient(settings).addTransportAddresses(transportAddresses);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * search data from es by offset,size, sort
     *
     * @param index
     * @param type
     * @param queryBuilder
     * @param offset
     * @param size
     * @param sortBuilder
     * @return
     */
    public SearchHit[] search(String index, String type, QueryBuilder queryBuilder, int offset, int size, SortBuilder sortBuilder){
        SearchRequestBuilder requestBuilder = client.prepareSearch()
                .setIndices(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(offset)
                .setSize(size);

        if(sortBuilder != null)
            requestBuilder.addSort(sortBuilder);

        logger.info("search - " + requestBuilder.toString());
        SearchResponse response = requestBuilder.execute().actionGet();
        return response.getHits().getHits();
    }

    /**
     *
     * search data from es
     *
     * @param index
     * @param type
     * @param queryBuilder
     * @param sortBuilder
     * @return
     */
    public SearchHit[] search(String index, String type, QueryBuilder queryBuilder, SortBuilder sortBuilder) {
        SearchRequestBuilder requestBuilder = this.client.prepareSearch().
                setIndices(index).
                setTypes(type).
                setSearchType(SearchType.QUERY_THEN_FETCH).
                setQuery(queryBuilder);

        if (sortBuilder != null)
            requestBuilder.addSort(sortBuilder);

        logger.info("search - " + requestBuilder.toString());
        SearchResponse response = requestBuilder.execute().actionGet();
        return response.getHits().getHits();
    }

    /**
     * Count docs
     *
     * @param index
     * @param type
     * @param queryBuilder
     * @return
     */
    public long count(String index, String type, QueryBuilder queryBuilder) {
        SearchRequestBuilder requestBuilder = this.client.prepareSearch().
                setIndices(index).
                setTypes(type).
                setSearchType(SearchType.QUERY_THEN_FETCH).
                setQuery(queryBuilder);
        logger.info("\ncount - " + requestBuilder.toString());
        SearchResponse response = requestBuilder.execute().actionGet();
        return response.getHits().getTotalHits();
    }

    /**
     *
     * @param index
     * @param type
     * @param id
     * @param parent
     * @param updateSource
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void update(String index, String type, String id, String parent, Map<String, Object> updateSource) throws ExecutionException, InterruptedException {
        Map<String, Object> source = getChildByUnknownParent(index, type, id);
        source.putAll(updateSource);
        UpdateRequestBuilder requestBuilder = this.client.prepareUpdate()
                .setIndex(index)
                .setType(type)
                .setId(id)
                .setDoc(source);

        if (StringUtils.isNotEmpty(parent))
            requestBuilder.setParent(parent);

        logger.info("\nupdate - " + requestBuilder.toString());
        requestBuilder.execute().actionGet();
    }

    /**
     * update by query
     *
     * @param index
     * @param type
     * @param queryBuilder
     * @param script
     * @return 成功update的文档数
     */
    public long updateByQuery(String index, String type, QueryBuilder queryBuilder, Script script) {
        UpdateByQueryRequestBuilder requestBuilder = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        requestBuilder.source(index).source().setTypes(type).setQuery(queryBuilder);
        BulkIndexByScrollResponse response = requestBuilder.script(script).execute().actionGet();
        return response.getUpdated();
    }

    /**
     *
     * @param index
     * @param type
     * @param id
     * @param parent set null if get a source map from a parent type
     * @return
     */
    public Map<String, Object> get(String index, String type, String id, String parent) {
        GetRequestBuilder requestBuilder = this.client.prepareGet(index, type, id);
        if (StringUtils.isNotEmpty(parent))
            requestBuilder.setParent(parent);

        logger.info("\nget - " + requestBuilder.toString());
        GetResponse response = requestBuilder.execute().actionGet();
        return response.getSource();
    }

    /**
     * Get a doc from child type without knowing its parent id
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    public Map<String, Object> getChildByUnknownParent(String index, String type, String id) {
        SearchHit[] searchHits = search(index, type, QueryBuilders.termQuery("id", id), 0, 1, null);
        if (searchHits.length == 0) return null;
        return searchHits[0].getSource();
    }

    /**
     * 计算n层agg
     *最后一层可以是SumAgg
     * @param index
     * @param type
     * @param queryBuilder
     * @param hawkeyeAggList
     * @return
     * @throws Exception
     */
    public LinkedHashMap<String, Object> getNTermsAgg(String index, String type, QueryBuilder queryBuilder, LinkedList<HawkeyeAgg> hawkeyeAggList, boolean... flag) throws Exception {
        List<AbstractAggregationBuilder> aggregationBuilderList = HawkeyeAggParser.parse(hawkeyeAggList);
        Map<String, Aggregation> aggMap = aggregations(
                index,
                type,
                queryBuilder,
                aggregationBuilderList);
        return AggWrapUtil.wrapNAgg(aggMap, hawkeyeAggList, -1, flag);
    }

    /**
     * 原生通用agg
     *
     * @param index
     * @param type
     * @param queryBuilder
     * @param aggregationBuilders
     * @return
     * @throws Exception
     */
    public Map<String, Aggregation> aggregations(String index, String type, QueryBuilder queryBuilder, List<AbstractAggregationBuilder> aggregationBuilders) throws Exception {
        SearchRequestBuilder requestBuilder = this.client.prepareSearch()
                .setIndices(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_THEN_FETCH);
        if (queryBuilder != null)
            requestBuilder.setQuery(queryBuilder);
        for (AbstractAggregationBuilder aggregationBuilder : aggregationBuilders) {
            requestBuilder.addAggregation(aggregationBuilder);
        }
        System.out.println(requestBuilder.toString());
        logger.info("\n" + requestBuilder.toString());
        SearchResponse response = requestBuilder.execute().actionGet();
        Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
        return aggregationMap;
    }

    /**
     * 单层agg
     *
     * @param index
     * @param type
     * @param queryBuilder
     * @param hawkeyeAggList
     * @return
     * @throws Exception
     */
    public Map<String, Double> get1TermsAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception {
        List<AbstractAggregationBuilder> aggregationBuilderList = HawkeyeAggParser.parse(hawkeyeAggList);
        Map<String, Aggregation> aggMap = aggregations(
                index,
                type,
                queryBuilder,
                aggregationBuilderList
        );
        if (hawkeyeAggList.size() > 1) {
            return AggWrapUtil.wrapMetricsAgg(aggMap, hawkeyeAggList);
        } else {
            HawkeyeAgg HawkeyeAgg = hawkeyeAggList.get(0);
            return AggWrapUtil.wrapOneTermAgg(aggMap, HawkeyeAgg);
        }
    }

    public LinkedHashMap<String, Double> getSumAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception {
        List<AbstractAggregationBuilder> aggregationBuilderList = HawkeyeAggParser.parse(hawkeyeAggList);
        Map<String, Aggregation> aggMap = aggregations(
                index,
                type,
                queryBuilder,
                aggregationBuilderList
        );
        if (hawkeyeAggList.size() > 1) {
            return AggWrapUtil.wrapMetricsAgg(aggMap, hawkeyeAggList);
        } else {
            return AggWrapUtil.wrapOneStatAgg(aggMap, hawkeyeAggList.get(0));
        }
    }

    public LinkedHashMap<String, Double> getAvgAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception {
        List<AbstractAggregationBuilder> aggregationBuilderList = HawkeyeAggParser.parse(hawkeyeAggList);
        Map<String, Aggregation> aggMap = aggregations(
                index,
                type,
                queryBuilder,
                aggregationBuilderList
        );
        if (hawkeyeAggList.size() > 1) {
            return AggWrapUtil.wrapMetricsAgg(aggMap, hawkeyeAggList);
        } else {
            return AggWrapUtil.wrapOneStatAgg(aggMap, hawkeyeAggList.get(0));
        }
    }

    public LinkedHashMap<String, Double> getMaxAgg(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception {
        List<AbstractAggregationBuilder> aggregationBuilderList = HawkeyeAggParser.parse(hawkeyeAggList);
        Map<String, Aggregation> aggMap = aggregations(
                index,
                type,
                queryBuilder,
                aggregationBuilderList
        );
        if (hawkeyeAggList.size() > 1) {
            return AggWrapUtil.wrapMetricsAgg(aggMap, hawkeyeAggList);
        } else {
            return AggWrapUtil.wrapOneStatAgg(aggMap, hawkeyeAggList.get(0));
        }
    }

    public LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> get3TermsAggWithChild(String index, String type, QueryBuilder queryBuilder, List<HawkeyeAgg> hawkeyeAggList) throws Exception {
        List<AbstractAggregationBuilder> aggregationBuilderList = HawkeyeAggParser.parse(hawkeyeAggList);
        Map<String, Aggregation> aggMap = aggregations(
                index,
                type,
                queryBuilder,
                aggregationBuilderList
        );
        return AggWrapUtil.wrap3AggWithChild(aggMap, hawkeyeAggList);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
        TankSearcher tankSearcher = TankSearcher.getInstance();

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchHit[] hits  = tankSearcher.search("hawkeye-app-web-v3", "wechat", queryBuilder, 0, 10, null);
        System.out.println(hits.length);
        System.out.println(hits[0].getSource());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit hit: hits){
            list.add(hit.getSource());
        }

        List wechatList = new ArrayList();

        for (Map<String, Object> map: list){
            try {
                wechatList.add(SearchWrapUtil.fromSourceMap(map, Wechat.class));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

}
