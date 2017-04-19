package com.ptmind.elasticsearch.factory;

import com.ptmind.domain.elasticsearch.entity.ESQueryModel;
import com.ptmind.elasticsearch.util.ElasticSearchClient;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SearchResponseFactory
 * @Description:.
 * @Company: Copyright (c) Pt mind
 * @version: 2.1
 * @date: 2017/4/13
 * @author: ptmind
 */
@Service("searchResponseFactory")
public class SearchResponseFactory implements ElasticFactory {

	@Autowired
	private ElasticSearchClient elasticSearchClient;

	public SearchResponse instance(ESQueryModel esQueryModel,BoolQueryBuilder boolQuery){
		SearchResponse searchResponse = elasticSearchClient.getClient().prepareSearch(esQueryModel.getIndex())
						.setTypes(esQueryModel.getType())
						.setSearchType(SearchType.QUERY_THEN_FETCH)
						.setQuery(boolQuery)
						.setSize(esQueryModel.getLimit())
						.execute()
						.actionGet();
		return searchResponse;
	}

}
