package com.tank.es.searcher.agg;

import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.joda.time.DateTimeZone;

import java.util.LinkedList;
import java.util.List;

/**
 * migrated by chenxueyan
 * Created by zhongshu on 2016/10/3 13:39.
 * todo 还没有做child_aggregation聚合的解析，这个用得比较少，用到时再算
 */
public class HawkeyeAggParser {

    public static List<AbstractAggregationBuilder> parse(List<HawkeyeAgg> hawkeyeAggList) {
        List<AbstractAggregationBuilder> target = new LinkedList<>();
        if (hawkeyeAggList == null || hawkeyeAggList.size() <= 0)
            return target;

        HawkeyeAgg lastAgg = hawkeyeAggList.get((hawkeyeAggList.size() - 1));
        AbstractAggregationBuilder tmp = convert(lastAgg);

        for (int i = hawkeyeAggList.size() - 2; i >= 0; i--) {
            HawkeyeAgg hawkeyeAgg = hawkeyeAggList.get(i);
            AggregationBuilder aggBuilder = (AggregationBuilder) convert(hawkeyeAgg);
            if (aggBuilder == null)
                continue;
            tmp = (AbstractAggregationBuilder) aggBuilder.subAggregation(tmp);
        }

        target.add(tmp);
        return target;
    }

    private static AbstractAggregationBuilder convert(HawkeyeAgg hawkeyeAgg) {
        AbstractAggregationBuilder abstractAggregationBuilder = null;

        if (hawkeyeAgg instanceof TermsAgg) {
            abstractAggregationBuilder = (AbstractAggregationBuilder) createTermsAgg(hawkeyeAgg);
        } else if (hawkeyeAgg instanceof DateAgg) {
            abstractAggregationBuilder = (AbstractAggregationBuilder) createDateAgg(hawkeyeAgg);
        } else if (hawkeyeAgg instanceof CardinalityAgg) {
            abstractAggregationBuilder = createCardinalityAgg(hawkeyeAgg);
        } else if (hawkeyeAgg instanceof SumAgg) {
            abstractAggregationBuilder = createSumAgg(hawkeyeAgg);
        } else if (hawkeyeAgg instanceof  ChildAgg) {
            abstractAggregationBuilder = createChildAgg(hawkeyeAgg);
        } else if (hawkeyeAgg instanceof FilterAgg) {
            abstractAggregationBuilder = createFilterAgg(hawkeyeAgg);
        } else if (hawkeyeAgg instanceof AvgAgg){
            abstractAggregationBuilder = createAvgAgg(hawkeyeAgg);
        } else if (hawkeyeAgg instanceof MaxAgg) {
            abstractAggregationBuilder = createMaxAgg(hawkeyeAgg);
        }

        return abstractAggregationBuilder;
    }

    private static AbstractAggregationBuilder createMaxAgg(HawkeyeAgg hawkeyeAgg) {
        MaxAgg maxAgg = (MaxAgg) hawkeyeAgg;
        return AggregationBuilders.max(maxAgg.getAggName()).field(maxAgg.getAggName());
    }

    private static AbstractAggregationBuilder createAvgAgg(HawkeyeAgg hawkeyeAgg) {
        AvgAgg avgAgg = (AvgAgg) hawkeyeAgg;
        return AggregationBuilders.avg(avgAgg.getAggName()).field(avgAgg.getAggName()).missing(avgAgg.getMissing());
    }

    private static CardinalityAggregationBuilder createCardinalityAgg(HawkeyeAgg hawkeyeAgg) {
        CardinalityAgg cardinalityAgg = (CardinalityAgg) hawkeyeAgg;
        return AggregationBuilders.cardinality(hawkeyeAgg.getAggName()).field(hawkeyeAgg.getAggField()).precisionThreshold(1000L);
    }

    private static AggregationBuilder createTermsAgg(HawkeyeAgg hawkeyeAgg) {
        TermsAgg termsAgg = (TermsAgg) hawkeyeAgg;
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms(termsAgg.getAggName())
                .field(termsAgg.getAggField())
                .size(termsAgg.getSize());
        termsBuilder.order(termsAgg.getOrder());
        return termsBuilder;
    }

    private static AggregationBuilder createDateAgg(HawkeyeAgg hawkeyeAgg) {
        DateAgg dateAgg = (DateAgg) hawkeyeAgg;
        return AggregationBuilders.dateHistogram(dateAgg.getAggName())
                .field(dateAgg.getAggField())
                .dateHistogramInterval(new DateHistogramInterval(dateAgg.getInterval()))
                .timeZone(DateTimeZone.forID(dateAgg.getTimeZone()))
                .format(dateAgg.getFormat());

    }

    private static AbstractAggregationBuilder createSumAgg(HawkeyeAgg hawkeyeAgg) {
        SumAgg sumAgg = (SumAgg) hawkeyeAgg;
        return AggregationBuilders.sum(sumAgg.getAggName()).field(sumAgg.getAggName());
    }

    private static AbstractAggregationBuilder createChildAgg(HawkeyeAgg hawkeyeAgg) {
        ChildAgg childAgg = (ChildAgg) hawkeyeAgg;
        return AggregationBuilders.children(childAgg.getAggName(), childAgg.getAggField());
    }

    private static AbstractAggregationBuilder createFilterAgg(HawkeyeAgg hawkeyeAgg){
        FilterAgg filterAgg = (FilterAgg) hawkeyeAgg;
        return AggregationBuilders.filter(hawkeyeAgg.getAggName(), filterAgg.getQueryBuilder());
    }
}