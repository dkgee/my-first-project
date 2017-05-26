package com.tank.es.utils;

import com.tank.es.searcher.agg.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.children.Children;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * AggWrapUtil
 *
 * @author chenxueyan
 * @since 16/11/17
 */
public class AggWrapUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AggWrapUtil.class);


    /**
     * @param aggMap
     * @param HawkeyeAggList
     * @param bools         该参数赋值分类
     *                      1）null，则聚合出来的结果各层key都不拼接 "\t" + docCount
     *                      2）不赋值，则聚合出来的结果各层key都不拼接 "\t" + docCount
     *                      3）一个true，则聚合出来的结果各层key都拼接 "\t" + docCount
     *                      4）三个布尔值，例如：true,false,false，一定是大于等于三个，否则会抛ArrayIndexOutOfBoundException
     * @return
     */
    public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> wrap3Agg(
            Map<String, Aggregation> aggMap,
            List<HawkeyeAgg> HawkeyeAggList,
            boolean... bools) {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> ret = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>();
        boolean[] tmpBools = parseBools(2, bools);
        if (tmpBools != null)
            bools = tmpBools;

        for (MultiBucketsAggregation.Bucket firstLayerBucket : getBuckets(aggMap, HawkeyeAggList, 0)) {

            String key = firstLayerBucket.getKeyAsString();
            double docCount = firstLayerBucket.getDocCount();
            LinkedHashMap<String, LinkedHashMap<String, Double>> secondMap = new LinkedHashMap<String, LinkedHashMap<String, Double>>();
            for (MultiBucketsAggregation.Bucket secondLayerBucket : getBuckets(firstLayerBucket, HawkeyeAggList, 1)) {

                String innerKey = secondLayerBucket.getKeyAsString();
                double innerDocCount = secondLayerBucket.getDocCount();
                LinkedHashMap<String, Double> thirdMap = new LinkedHashMap<String, Double>();
                for (MultiBucketsAggregation.Bucket thirdLayerBucket : getBuckets(secondLayerBucket, HawkeyeAggList, 2)) {
                    thirdMap.put(thirdLayerBucket.getKeyAsString(), (double) thirdLayerBucket.getDocCount());
                }

                String tmpInnerKey = getKey(innerKey, innerDocCount, bools[1]);
                secondMap.put(tmpInnerKey, thirdMap);
            }
            String tmpKey = getKey(key, docCount, bools[0]);
            ret.put(tmpKey, secondMap);
        }
        return ret;
    }

    /**
     * @param aggMap
     * @param HawkeyeAggList
     * @return 只有最后一层有 doc_count ,其他层都没有 记录doc_count
     */
    public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> wrap3AggWithChild(Map<String, Aggregation> aggMap, List<HawkeyeAgg> HawkeyeAggList) {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> ret = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>>();
        for (MultiBucketsAggregation.Bucket firstLayerBucket : getBuckets(aggMap, HawkeyeAggList, 0)) {
            String key = firstLayerBucket.getKeyAsString();
            LinkedHashMap<String, LinkedHashMap<String, Double>> secondMap = new LinkedHashMap<String, LinkedHashMap<String, Double>>();
            for (Aggregation aggregation : firstLayerBucket.getAggregations()) {
                Children child = (Children) aggregation;
                String innerKey = child.getName();
                LinkedHashMap<String, Double> thirdMap = new LinkedHashMap<String, Double>();
                for (Aggregation thirdAgg : child.getAggregations()) {
                    InternalFilter thirdFilter = (InternalFilter) thirdAgg;
                    String thirdName = thirdFilter.getName();
                    thirdMap.put(thirdName, (double) thirdFilter.getDocCount());
                }
                secondMap.put(innerKey, thirdMap);
            }
            ret.put(key, secondMap);
        }
        return ret;
    }


    public static Map<String, Object> wrapNAgg(Map<String, Aggregation> aggregationMap) {
        Map<String, Object> results = new HashMap<>();
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            String keyword = entry.getKey();
            Map<String, Object> singleResults = new HashMap<>();
            //单层statAgg解析
            if (entry.getValue() instanceof Sum) {
                Sum stat = (Sum) entry.getValue();
                if (aggregationMap.size() == 1) {
                    results.put(keyword, stat.getValue());
                    return results;
                }
                singleResults.put(stat.getName(), Double.valueOf(stat.getValueAsString()));
                results.put(keyword, singleResults);
                continue;
            }

            MultiBucketsAggregation multiBucketsAggregation = (MultiBucketsAggregation) entry.getValue();
            for (MultiBucketsAggregation.Bucket bucket : multiBucketsAggregation.getBuckets()) {
                //单层termAgg
                if (bucket.getAggregations().getAsMap().size() == 0) {
                    singleResults.put(bucket.getKeyAsString(), bucket.getDocCount());
                } else {
                    singleResults.put(bucket.getKeyAsString(), wrapNAgg(bucket.getAggregations().getAsMap()));
                }
            }
            results.put(keyword, singleResults);
        }
        return results;
    }

    /**
     * 解析n层agg
     *
     * @param aggMap
     * @param HawkeyeAggList
     * @param index
     * @param flag
     * @return
     */
    public static LinkedHashMap<String, Object> wrapNAgg(Map<String, Aggregation> aggMap, LinkedList<HawkeyeAgg> HawkeyeAggList, int index, boolean... flag) {
        LinkedHashMap<String, Object> ret = new LinkedHashMap<>();
        index = index + 1;

        boolean[] tmpBools = parseBools(HawkeyeAggList.size(), flag);
        if (tmpBools != null)
            flag = tmpBools;

        /**
         * 一层统计sum
         */
        if (HawkeyeAggList.size() == 1 && HawkeyeAggList.getLast() instanceof SumAgg) {
            Sum stat = (Sum) aggMap.get(HawkeyeAggList.getLast().getAggName());
            ret.put(HawkeyeAggList.getLast().getAggName(), Double.valueOf(stat.getValueAsString()));
            return ret;
        }

        if (HawkeyeAggList.size() == 1 && HawkeyeAggList.getLast() instanceof CardinalityAgg) {
            String name = HawkeyeAggList.get(0).getAggName();
            InternalCardinality internalCardinality = (InternalCardinality) aggMap.get(name);
            ret.put(name, internalCardinality.getValue());
            return ret;
        }

        /**
         * 一层agg
         * 多层
         */
        for (MultiBucketsAggregation.Bucket bucket1 : getBuckets(aggMap, HawkeyeAggList, index)) {
            String innerKey = bucket1.getKeyAsString();
            double count = bucket1.getDocCount();
            if (index == HawkeyeAggList.size() - 1) {
                ret.put(innerKey, count);
            } else {
                String key = getKey(innerKey, count, flag[index]);
                ret.put(key, wrapNAgg(bucket1, HawkeyeAggList, index, flag));
            }
        }
        return ret;
    }

    /**
     * 解析n层agg
     *
     * @param bucket
     * @param HawkeyeAggList
     * @param index
     * @param flag
     * @return
     */
    public static LinkedHashMap<String, Object> wrapNAgg(MultiBucketsAggregation.Bucket bucket, LinkedList<HawkeyeAgg> HawkeyeAggList, int index, boolean... flag) {
        LinkedHashMap<String, Object> ret = new LinkedHashMap<>();
        index = index + 1;
        for (MultiBucketsAggregation.Bucket bucket1 : getBuckets(bucket, HawkeyeAggList, index)) {
            if ((index == HawkeyeAggList.size() - 2) && (HawkeyeAggList.getLast() instanceof SumAgg)) {
                statAgg(ret, bucket1, HawkeyeAggList.getLast());
            } else {
                String innerKey = bucket1.getKeyAsString();
                double count = bucket1.getDocCount();
                String key = innerKey;
                if(flag.length<=index){
                     key = getKey(innerKey, count, flag[flag.length - 1]);
                }else {
                    key = getKey(innerKey, count, flag[index]);
                }
                if (index == HawkeyeAggList.size() - 1) {
                    ret.put(innerKey, count);
                } else {
                    ret.put(key, wrapNAgg(bucket1, HawkeyeAggList, index, flag));
                }
            }
        }
        return ret;
    }

    /**
     * statAgg 计算
     *
     * @param ret
     * @param bucket
     * @param HawkeyeAgg
     */
    private static void statAgg(LinkedHashMap<String, Object> ret, MultiBucketsAggregation.Bucket bucket, HawkeyeAgg HawkeyeAgg) {
        String key = bucket.getKeyAsString();
        Aggregation aggregation = bucket.getAggregations().get(HawkeyeAgg.getAggName());
        if (aggregation instanceof Sum) {
            Sum sum = (Sum) aggregation;
            Double sumDou = sum.getValue();
            ret.put(key, sumDou);
        } else if (aggregation instanceof Cardinality) {
            Cardinality cardinality = (Cardinality) aggregation;
            Long cardinalityValue = cardinality.getValue();
            ret.put(key, new Double(cardinalityValue));
        }
    }

    /**
     * @param aggMap
     * @param HawkeyeAggList
     * @param bools         该参数赋值分类
     *                      1）null，则聚合出来的结果各层key都不拼接 "\t" + docCount
     *                      2）不赋值，则聚合出来的结果各层key都不拼接 "\t" + docCount
     *                      3）一个true，则聚合出来的结果各层key都拼接 "\t" + docCount
     *                      4）三个布尔值，例如：true,false,false，一定是大于等于三个，否则会抛ArrayIndexOutOfBoundException
     * @return
     */
    public static LinkedHashMap<String, LinkedHashMap<String, Double>> wrap2Agg(Map<String, Aggregation> aggMap, List<HawkeyeAgg> HawkeyeAggList, boolean... bools) {
        LinkedHashMap<String, LinkedHashMap<String, Double>> ret = new LinkedHashMap<>();
        boolean[] tmpBools = parseBools(1, bools);
        if (tmpBools != null)
            bools = tmpBools;

        for (MultiBucketsAggregation.Bucket firstLayerBucket : getBuckets(aggMap, HawkeyeAggList, 0)) {
            String key = firstLayerBucket.getKeyAsString();
            double docCount = firstLayerBucket.getDocCount();
            LinkedHashMap<String, Double> secondMap = new LinkedHashMap<String, Double>();
            for (MultiBucketsAggregation.Bucket secondLayerBucket : getBuckets(firstLayerBucket, HawkeyeAggList, 1)) {
                String innerKey = secondLayerBucket.getKeyAsString();
                double innerDocCount = secondLayerBucket.getDocCount();
                secondMap.put(innerKey, innerDocCount);
            }
            String tmpKey = getKey(key, docCount, bools[0]);
            ret.put(tmpKey, secondMap);
        }
        return ret;
    }


    public static LinkedHashMap<String, LinkedHashMap<String, Double>> wrap2AggWithChild(Map<String, Aggregation> aggMap, List<HawkeyeAgg> HawkeyeAggList, boolean... bools) {
        LinkedHashMap<String, LinkedHashMap<String, Double>> ret = new LinkedHashMap<>();
        boolean[] tmpBools = parseBools(1, bools);
        if (tmpBools != null)
            bools = tmpBools;

        for (MultiBucketsAggregation.Bucket firstLayerBucket : getBuckets(aggMap, HawkeyeAggList, 0)) {
            String key = firstLayerBucket.getKeyAsString();
            double docCount = firstLayerBucket.getDocCount();
            LinkedHashMap<String, Double> secondMap = new LinkedHashMap<String, Double>();
            for (Aggregation aggregation : firstLayerBucket.getAggregations()) {
                Children child = (Children) aggregation;
                String innerKey = child.getName();
                double innerDocCount = child.getDocCount();
                secondMap.put(innerKey, innerDocCount);
            }
            String tmpKey = getKey(key, docCount, bools[0]);
            ret.put(tmpKey, secondMap);
        }
        return ret;
    }

    public static LinkedHashMap<String, Double> wrapMetricsAgg(Map<String, Aggregation> aggMap, List<HawkeyeAgg> HawkeyeAggList) {
        LinkedHashMap<String, Double> ret = new LinkedHashMap<>();

        for (MultiBucketsAggregation.Bucket firstLayerBucket : getBuckets(aggMap, HawkeyeAggList, 0)) {
            String key = firstLayerBucket.getKeyAsString();
            Aggregation aggregation = firstLayerBucket.getAggregations().get(HawkeyeAggList.get(1).getAggName());
            if (aggregation instanceof Sum) {
                Sum sum = (Sum) aggregation;
                Double sumDou = sum.getValue();
                ret.put(key, sumDou);
            } else if (aggregation instanceof Cardinality) {
                Cardinality cardinality = (Cardinality) aggregation;
                Long cardinalityValue = cardinality.getValue();
                ret.put(key, new Double(cardinalityValue));
            }
        }
        return ret;
    }

    /**
     * statAgg
     * 获取统计值
     *
     * @param aggRet
     * @param HawkeyeAgg
     * @return
     */
    public static LinkedHashMap<String, Double> wrapOneStatAgg(Map<String, Aggregation> aggRet, HawkeyeAgg HawkeyeAgg) {
        LinkedHashMap<String, Double> results = new LinkedHashMap<>();
        if (HawkeyeAgg instanceof SumAgg) {
            Sum stat = (Sum) aggRet.get(HawkeyeAgg.getAggName());
            results.put(HawkeyeAgg.getAggName(), Double.valueOf(stat.getValueAsString()));
        } else if (HawkeyeAgg instanceof AvgAgg) {

            Avg avg = (Avg) aggRet.get(HawkeyeAgg.getAggName());
            results.put(HawkeyeAgg.getAggName(), avg.getValue());

        } else if (HawkeyeAgg instanceof MaxAgg) {

            Max max = (Max) aggRet.get(HawkeyeAgg.getAggName());
            results.put(HawkeyeAgg.getAggName(), max.getValue());

        }
//        else if (HawkeyeAgg instanceof MinAgg) {
//             stat = (Min) aggRet.get(HawkeyeAgg.getAggName());
//        } else if (HawkeyeAgg instanceof MaxAgg) {
//            stat = (Max) aggRet.get(HawkeyeAgg.getAggName());
//        }
//        results.put(HawkeyeAgg.getAggName(), );
        return results;
    }


    public static LinkedHashMap<String, Double> wrapOneTermAgg(Map<String, Aggregation> aggRet, HawkeyeAgg HawkeyeAgg) {
        LinkedHashMap<String, Double> ret = new LinkedHashMap<>();
        String aggName = HawkeyeAgg.getAggName();
        MultiBucketsAggregation multiBucketsAggregation = (MultiBucketsAggregation) aggRet.get(aggName);
        for (MultiBucketsAggregation.Bucket bucket : multiBucketsAggregation.getBuckets()) {
            String key = bucket.getKeyAsString();                    // bucket key
            long docCount = bucket.getDocCount();            // Doc count
            ret.put(key, new Double(docCount));
        }
        return ret;
    }

    public static LinkedHashMap<String, Long> wrapCardinalityAgg(Map<String, Aggregation> aggMap, List<HawkeyeAgg> HawkeyeAggList) {
        LinkedHashMap<String, Long> ret = new LinkedHashMap<>();
        String name = HawkeyeAggList.get(0).getAggName();
        InternalCardinality internalCardinality = (InternalCardinality) aggMap.get(name);
        ret.put(name, internalCardinality.getValue());
        return ret;
    }

    public static List<MultiBucketsAggregation.Bucket> getBuckets(Map<String, Aggregation> aggMap, HawkeyeAgg HawkeyeAgg) {
        MultiBucketsAggregation multiBucketsAggregation = (MultiBucketsAggregation) aggMap.get(HawkeyeAgg.getAggName());
        LOG.info("multiBucketsAggregation.getBuckets size: {}", multiBucketsAggregation.getBuckets().size());
        return (List<MultiBucketsAggregation.Bucket>) multiBucketsAggregation.getBuckets();
    }

    public static List<MultiBucketsAggregation.Bucket> getBuckets(Map<String, Aggregation> aggMap, List<HawkeyeAgg> HawkeyeAggList, int index) {
        String name = HawkeyeAggList.get(index).getAggName();
        MultiBucketsAggregation multiBucketsAggregation = (MultiBucketsAggregation) aggMap.get(name);
        LOG.info("multiBucketsAggregation.getBuckets size: {}", multiBucketsAggregation.getBuckets().size());
        return (List<MultiBucketsAggregation.Bucket>) multiBucketsAggregation.getBuckets();
    }

    public static List<MultiBucketsAggregation.Bucket> getBuckets(MultiBucketsAggregation.Bucket bucket, List<HawkeyeAgg> HawkeyeAggList, int index) {
        String name = HawkeyeAggList.get(index).getAggName();
        MultiBucketsAggregation multiBucketsAggregation = bucket.getAggregations().get(name);
        return (List<MultiBucketsAggregation.Bucket>) multiBucketsAggregation.getBuckets();
    }


    public static boolean[] parseBools(int count, boolean... bools) {

        if (bools == null || bools.length == 0) {
            return new boolean[count];
        } else if (bools.length == 1 && bools[0]) {
            bools = new boolean[count];
            for (int i = 0; i < count; i++) {
                bools[i] = true;
            }
            return bools;
        }
        return null;
    }

    public static String getKey(String key, double docCount, boolean flag) {
        return flag ? key + "\t" + docCount : key;
    }
}