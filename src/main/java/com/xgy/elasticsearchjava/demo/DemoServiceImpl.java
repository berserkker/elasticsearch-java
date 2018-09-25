package com.xgy.elasticsearchjava.demo;

import com.alibaba.fastjson.JSONObject;
import com.xgy.elasticsearchjava.util.Tool;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DemoServiceImpl implements DemoService {

    private Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private Tool tool;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void demoMethodOne() {
        //日期筛选条件
        Date startDate = tool.stringToDate("2018-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endDate = tool.stringToDate("2018-12-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        //数据筛选条件，匹配多个
        Object[] demoIds = {1, 2, 3};
        //获取对应
        //封装返回数据
        JSONObject data = new JSONObject();
        //ES 设置要查询的索引名称  类型名称
        SearchRequestBuilder requestBuilder = elasticsearchTemplate.getClient().prepareSearch("demo_v1");
        requestBuilder.setTypes("demo");
        //过滤搜索条件 使用must且
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        //过滤多个id
        bqb.must().add(QueryBuilders.termsQuery("demo_id", demoIds));
        //过滤单个条件
        bqb.must().add(QueryBuilders.matchQuery("type", 1));
        //使用filter过滤时间区间，可加上偏移条件  时区相关
        bqb.filter().add(QueryBuilders.rangeQuery("create_time").gte("2018-01-01 00:00:00").lte("2018-12-01 00:00:00").timeZone("+08:00"));
        requestBuilder.setQuery(bqb);
        //不需要返回原始数据，只需要统计数据
        requestBuilder.setSize(0);
        //按照id分组group by id
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("demoIds").
                field("demo_id");

        //统计规则,按照时间分组
        DateHistogramAggregationBuilder aggregation = AggregationBuilders.dateHistogram("times");
        String format = "yyyy/MM/dd HH:mm:ss";
        aggregation.field("create_time");
        //为0的也返回
        aggregation.minDocCount(0);
        //设置时区或者偏移量
        aggregation.timeZone(DateTimeZone.forOffsetHours(8));
        aggregation.extendedBounds(new ExtendedBounds(startDate.getTime(), endDate.getTime()));
        aggregation.keyed(true);
        //设置分组间隔,5小时
        aggregation.dateHistogramInterval(DateHistogramInterval.hours(5));
        //设置时间格式
        aggregation.format(format);

        //求分组之后每个分组的result的平均值
        AvgAggregationBuilder avgAggregation = AggregationBuilders.avg("result").field("result");
        aggregation.subAggregation(avgAggregation);
        termsAggregation.subAggregation(aggregation);
        requestBuilder.addAggregation(termsAggregation);
        logger.debug("ES JSON 查询条件------");
        logger.debug(requestBuilder.toString());
        SearchResponse myResponse = requestBuilder.get();
        //获取demo_id桶
        LongTerms demos = (LongTerms) myResponse.getAggregations().asMap().get("demoIds");
        //循环每个桶
        for (LongTerms.Bucket orgBucket : demos.getBuckets()) {
            //获取各个时间的平均值桶
            InternalDateHistogram times = (InternalDateHistogram) orgBucket.getAggregations().asMap().get("times");
            Map<String, Object> map = new HashMap<>();
            for (InternalDateHistogram.Bucket timeBucket : times.getBuckets()) {
                InternalAvg internalAvg = (InternalAvg) timeBucket.getAggregations().asMap().get("result");
                //时间和分组后的时间和对应的值存入map
                map.put(timeBucket.getKeyAsString(), internalAvg.getValue());
            }
        }
    }
}
