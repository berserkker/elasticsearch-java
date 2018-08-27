package com.xgy.elasticsearchjava.demo;

import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	//清空
	@DeleteMapping("/clear")
	public ResponseEntity<?> deleteIndex() {
		elasticsearchTemplate.deleteIndex(Skill.class);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	//映射
	@PutMapping("/mapping")
	public ResponseEntity<?> mapping() {
		elasticsearchTemplate.putMapping(Skill.class);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 增加
	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody Skill skill) {
		elasticsearchTemplate.index(new IndexQueryBuilder().withObject(skill).build());	
		elasticsearchTemplate.putMapping(Skill.class);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 删除
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		boolean exists = elasticsearchTemplate.indexExists(Skill.class);
		if(exists)
		{		
			elasticsearchTemplate.delete(Skill.class, id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 单查询
	@GetMapping("/query/{id}")
	public ResponseEntity<?> queryOne(@PathVariable String id) {
		boolean exists = elasticsearchTemplate.indexExists(Skill.class);
		if(exists)
		{		
			GetQuery query = new GetQuery();
		    query.setId(id);
			Skill skill = elasticsearchTemplate.queryForObject(query, Skill.class);
			return new ResponseEntity<>(skill, HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 列表分页查询
	@GetMapping("/queryPage/{page}")
	public ResponseEntity<?> queryPage(@PathVariable int page) {	
		boolean exists = elasticsearchTemplate.indexExists(Skill.class);
		if(exists)
		{	
			// 分页
			Sort pageSort = new Sort(Sort.Direction.DESC, "id");
			Pageable pageable = PageRequest.of(page, 10, pageSort);
			// 查询全部
			QueryBuilder all = QueryBuilders.matchAllQuery();
			QueryBuilder range = QueryBuilders.rangeQuery("employee.age").gte(19);
			SearchQuery query = new NativeSearchQueryBuilder()
					 .withQuery(range)
					 .withPageable(pageable)	
					 .withIndices("pepole")
					 .withTypes("skill")
					 .build();
			
			Page<Skill> skillList = elasticsearchTemplate.queryForPage(query, Skill.class);
	
			return new ResponseEntity<>(skillList.getContent(), HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 列表总数
	@GetMapping("/queryCount")
	public ResponseEntity<?> queryCount() {
		boolean exists = elasticsearchTemplate.indexExists(Skill.class);
		if(exists)
		{	
			QueryBuilder all = QueryBuilders.matchAllQuery();	
			SearchQuery query = new NativeSearchQueryBuilder()
					 .withQuery(all)
					 .withIndices("pepole")
					 .withTypes("skill")
					 .build();
			long count = elasticsearchTemplate.count(query);
			
			return new ResponseEntity<>(count, HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 条件查 age 相等的数据
	@GetMapping("/queryByAge/{age}/{page}")
	public ResponseEntity<?> queryByAge(@PathVariable int age, @PathVariable int page) {
		

		// 范围查询
		QueryBuilder range = QueryBuilders.rangeQuery("employee.age").gte(21);

		QueryBuilder in = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("firstName", "name1"))
				           .should(QueryBuilders.matchQuery("age", age));

		// 匹配查询
		QueryBuilder equals = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("age", age))
							.must(QueryBuilders.matchQuery("firstName", "name1"));

		//多匹配
		QueryBuilder doubleC = QueryBuilders.boolQuery().should(equals).should(QueryBuilders.matchQuery("age", 2));
		
		
		// 模糊
		QueryBuilder like = QueryBuilders.fuzzyQuery("firstName", "name");

		// 不等于
		QueryBuilder not = QueryBuilders.boolQuery().mustNot(QueryBuilders.matchQuery("age", age));

		QueryBuilder all = QueryBuilders.matchAllQuery();	
		//====================聚合查询===========================================
		
		//求和
		SumAggregationBuilder sumBuilder = AggregationBuilders.sum("sum_age").field("employee.age");
				
		//求平均数
		AvgAggregationBuilder avg= AggregationBuilders.avg("avg_age").field("num");
		
		// 分组
		FilterAggregationBuilder filter = AggregationBuilders.filter("recent",QueryBuilders.rangeQuery("age").gte(21));
		
		//字符串Text型分组 需要添加keyword
		TermsAggregationBuilder tb = AggregationBuilders.terms("group_age").field("employee.firstName.keyword");
		TermsAggregationBuilder tb1 = AggregationBuilders.terms("group_1").field("sname.keyword");
		tb.subAggregation(tb1);
		tb.subAggregation(avg);
		tb.subAggregation(sumBuilder);
		filter.subAggregation(tb);
		
		//嵌套分组
		NestedAggregationBuilder nb = AggregationBuilders.nested("negsted", "skill");
		TermsAggregationBuilder gg = AggregationBuilders.terms("group_age").field("skill.id");		
		
		gg.subAggregation(AggregationBuilders.avg("avg_age").field("skill.num"));
		gg.subAggregation(AggregationBuilders.sum("sum_age").field("skill.num"));
		nb.subAggregation(gg);
		//聚合结果
		TopHitsAggregationBuilder hits = AggregationBuilders.topHits("hits");
		
		
		// 统计查询
		SearchQuery query = new NativeSearchQueryBuilder()
				.withQuery(all)
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("pepole")
				.withTypes("skill")
				.addAggregation(tb)
				.build();
		
		
		List<?> list = elasticsearchTemplate.queryForList(query, Skill.class);
		
		Aggregations aggregations = elasticsearchTemplate.query(query, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
//========================整体聚合=====================================
/**		InternalAvg internalAvg = (InternalAvg)aggregations.asMap().get("avg_age");
		System.out.println(internalAvg.getValue());
**/
//========================过滤聚合=====================================
/**			
		InternalFilter internalFilter = (InternalFilter)aggregations.asMap().get("recent");
		
		InternalAggregations aggs = internalFilter.getAggregations();
		LongTerms stringTerms = (LongTerms)aggs.asMap().get("group_age");

		List<LongTerms.Bucket> buckets = stringTerms.getBuckets();
		
		for (Bucket bucket : buckets) {
			
			InternalAvg internalAvg = (InternalAvg)bucket.getAggregations().asMap().get("avg_age");
			InternalSum internalSum = (InternalSum)bucket.getAggregations().asMap().get("sum_age");
			System.out.println(bucket.getDocCount() + "========age:" 
			+ bucket.getKeyAsString()+"===avg:"+internalAvg.getValue()+"=====sum:"+internalSum.getValue());
		}
**/
//========================嵌套聚合==================================================================================
/**		Nested nested = aggregations.get("negsted");
		
		LongTerms stringTerms = nested.getAggregations().get("group_age");
		
		List<LongTerms.Bucket> buckets = stringTerms.getBuckets();
		
		for (Bucket bucket : buckets) {
			
			InternalAvg internalAvg = (InternalAvg)bucket.getAggregations().asMap().get("avg_age");
			InternalSum internalSum = (InternalSum)bucket.getAggregations().asMap().get("sum_age");
			System.out.println(bucket.getDocCount() + "========age:" 
			+ bucket.getKeyAsString()+"===avg:"+internalAvg.getValue()+"=====sum:"+internalSum.getValue());
		}
**/	
//============================分组聚合============================================
		StringTerms  stringTerms = (StringTerms) aggregations.asMap().get("group_age");

		List<Bucket> buckets = stringTerms.getBuckets();
		for (Bucket bucket : buckets) {
			
			InternalAvg internalAvg = (InternalAvg)bucket.getAggregations().asMap().get("avg_age");
			InternalSum internalSum = (InternalSum)bucket.getAggregations().asMap().get("sum_age");
			StringTerms  stringTerms1 = (StringTerms)bucket.getAggregations().asMap().get("group_1");
			Iterator<Bucket> bucket1 = stringTerms1.getBuckets().iterator();
			while(bucket1.hasNext())
			{
				Bucket bucketo = bucket1.next();
				System.out.println(bucket.getDocCount() + "========:" 
						+ bucket.getKeyAsString()+"====="+bucketo.getKeyAsString()+"===avg:"+internalAvg.getValue()+"=====sum:"+internalSum.getValue());
			}
			
		}

		return new ResponseEntity<>(list,HttpStatus.OK);
	}

}
