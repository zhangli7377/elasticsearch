package com.ptmind.elasticsearch.dubbo.impl;

import com.ptmind.domain.elasticsearch.dubbo.ElasticSearchDao;
import com.ptmind.domain.elasticsearch.entity.ESQueryModel;
import com.ptmind.domain.elasticsearch.entity.ElasticSearchRequest;
import com.ptmind.domain.elasticsearch.entity.ElasticsearchPage;
import com.ptmind.elasticsearch.core.HitSourceGet;
import com.ptmind.elasticsearch.factory.BoolQueryFactory;
import com.ptmind.elasticsearch.factory.SearchResponseFactory;
import com.ptmind.elasticsearch.util.ElasticSearchClient;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

/**
 * Created by ptmind on 2015/9/9.
 */
@Component("elasticSearchDao")
public class ElasticSearchDaoImpl implements ElasticSearchDao {

	@Autowired
	private ElasticSearchClient elasticSearchClient;

	@Autowired
	private BoolQueryFactory boolQueryFactory;

	@Autowired
	private SearchResponseFactory searchResponseFactory;

	@Autowired
	private HitSourceGet hitSourceGet;

	@Override
	public void createDocument(ElasticSearchRequest elasticSearchRequest, Map map) {

		createDocument(elasticSearchRequest, (Object) map);
	}

	@Override
	public void createDocument(ElasticSearchRequest elasticSearchRequest, String json) {
		createDocument(elasticSearchRequest, (Object) json);
	}

	@Override
	public void deleteDocument(ElasticSearchRequest elasticSearchRequest) {
		elasticSearchClient.getClient().prepareDelete(elasticSearchRequest.getIndex(), elasticSearchRequest.getType(), elasticSearchRequest.getId()).execute();
	}

	@Override
	public void updateDocument(ElasticSearchRequest elasticSearchRequest, Map map) throws ExecutionException, InterruptedException {
		updateDocument(elasticSearchRequest, (Object) map);
	}

	@Override
	public void updateDocument(ElasticSearchRequest elasticSearchRequest, String json) throws ExecutionException, InterruptedException {
		updateDocument(elasticSearchRequest, (Object) json);
	}

	@Override
	public void updateDocument(ElasticSearchRequest elasticSearchRequest, String field, Object value) throws ExecutionException, InterruptedException {
		Map map = new HashMap();
		map.put(field, value);
		updateDocument(elasticSearchRequest, map);
	}

	@Override
	public List<String> queryDocument(ElasticSearchRequest request, String searchValue) {
		return queryDocument(request, searchValue, "_all");
	}

	@Override
	public List<String> queryDocumentId(ElasticSearchRequest request, String searchValue) {
		return queryDocumentId(request, searchValue, "_all");
	}

	@Override
	public List<String> queryDocument(ElasticSearchRequest request, String searchValue, String field) {
		List<String> fieldList = new ArrayList<String>();
		fieldList.add(field);
		return queryDocument(request, searchValue, fieldList);
	}

	@Override
	public List<String> queryDocumentId(ElasticSearchRequest request, String searchValue, String field) {
		List<String> fieldList = new ArrayList<String>();
		fieldList.add(field);
		return queryDocumentId(request, searchValue, fieldList);
	}

	@Override
	public List<String> queryDocument(ElasticSearchRequest request, String searchValue, List<String> fields) {
		return queryDocumentByPage(request, searchValue, fields, null);
	}

	@Override
	public List<String> queryDocument(ESQueryModel esQueryModel) {
		BoolQueryBuilder boolQuery = boolQueryFactory.instance().mustTerms(esQueryModel.getMustTermsMap()).mustMatch(esQueryModel).get();
		SearchResponse searchResponse = searchResponseFactory.instance(esQueryModel,boolQuery);
		return hitSourceGet.listHitSource(searchResponse);
	}

	@Override
	public List<String> queryDocumentWildCard(ESQueryModel esQueryModel) {
		BoolQueryBuilder boolQuery = boolQueryFactory.instance().mustTerms(esQueryModel.getMustTermsMap()).mustWildCard(esQueryModel).get();
		SearchResponse searchResponse = searchResponseFactory.instance(esQueryModel,boolQuery);
		return hitSourceGet.listHitSource(searchResponse);
	}

	@Override
	public List<String> queryDocumentRegexp(ESQueryModel esQueryModel) {
		BoolQueryBuilder boolQuery = boolQueryFactory.instance().mustTerms(esQueryModel.getMustTermsMap()).mustRegexp(esQueryModel).get();
		SearchResponse searchResponse = searchResponseFactory.instance(esQueryModel,boolQuery);
		return hitSourceGet.listHitSource(searchResponse);
	}

	@Override
	public List<String> queryDocumentId(ElasticSearchRequest request, String searchValue, List<String> fields) {
		return queryDocumentIdByPage(request, searchValue, fields, null);
	}

	@Override
	public List<String> queryDocumentByPage(ElasticSearchRequest request, String searchValue, ElasticsearchPage page) {
		return queryDocumentByPage(request, searchValue, "_all", page);
	}

	@Override
	public List<String> queryDocumentIdByPage(ElasticSearchRequest request, String searchValue, ElasticsearchPage page) {
		return queryDocumentIdByPage(request, searchValue, "_all", page);
	}

	@Override
	public List<String> queryDocumentByPage(ElasticSearchRequest request, String searchValue, String field, ElasticsearchPage page) {
		List<String> fieldList = new ArrayList<String>();
		fieldList.add(field);
		return queryDocumentByPage(request, searchValue, fieldList, page);
	}

	@Override
	public List<String> queryDocumentIdByPage(ElasticSearchRequest request, String searchValue, String field, ElasticsearchPage page) {
		List<String> fieldList = new ArrayList<String>();
		fieldList.add(field);
		return queryDocumentIdByPage(request, searchValue, fieldList, page);
	}


	@Override
	public List<String> queryDocumentByPage(ElasticSearchRequest request, String searchValue, List<String> fields, ElasticsearchPage page) {
		List<String> resultList = new ArrayList<String>();
		QueryBuilder queryBuilder = createMultiMatchQueryBuilder(searchValue, fields);
		SearchRequestBuilder searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(request.getIndex()).setTypes(request.getType());
		searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
		searchRequestBuilder.setQuery(queryBuilder);
		if (page != null) {
			searchRequestBuilder.setFrom(page.getStart())
							.setSize(page.getSize());
		}
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return hitSourceGet.listHitSource(searchResponse);
	}

	private QueryBuilder createMultiMatchQueryBuilder(String searchValue, List<String> fields) {
		String[] fieldArray = new String[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			fieldArray[i] = fields.get(i);
		}
		QueryBuilder queryBuilder = multiMatchQuery(searchValue, fieldArray);
		return queryBuilder;
	}

	@Override
	public List<String> queryDocumentIdByPage(ElasticSearchRequest request, String searchValue, List<String> fields, ElasticsearchPage page) {
		QueryBuilder queryBuilder = createMultiMatchQueryBuilder(searchValue, fields);
		SearchRequestBuilder searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(request.getIndex()).setTypes(request.getType());
		searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
		searchRequestBuilder.setQuery(queryBuilder);
		if (page != null) {
			searchRequestBuilder.setFrom(page.getStart())
							.setSize(page.getSize());
		}
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return hitSourceGet.listHitSource(searchResponse);
	}

	@Override
	public boolean isExist(ElasticSearchRequest request) {
		GetResponse getResponse = elasticSearchClient.getClient().prepareGet(request.getIndex(), request.getType(), request.getId())
						.get();
		return getResponse.isExists();
	}

	private void createDocument(ElasticSearchRequest elasticSearchRequest, Object o) {
		IndexRequestBuilder indexRequestBuilder = elasticSearchClient.getClient().prepareIndex(elasticSearchRequest.getIndex(), elasticSearchRequest.getType(), elasticSearchRequest.getId());
		indexRequestBuilder = setSource(indexRequestBuilder, o);
		indexRequestBuilder.execute();

	}

	private void updateDocument(ElasticSearchRequest elasticSearchRequest, Object o) throws ExecutionException, InterruptedException {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(elasticSearchRequest.getIndex());
		updateRequest.type(elasticSearchRequest.getType());
		updateRequest.id(elasticSearchRequest.getId());
		updateRequest = setDoc(updateRequest, o);
		elasticSearchClient.getClient().update(updateRequest).get();

	}

	private IndexRequestBuilder setSource(IndexRequestBuilder indexRequestBuilder, Object o) {

		if (o instanceof Map) {
			indexRequestBuilder = indexRequestBuilder.setSource((Map) o);
		} else if (o instanceof String) {
			indexRequestBuilder = indexRequestBuilder.setSource((String) o);
		}

		return indexRequestBuilder;
	}

	private UpdateRequest setDoc(UpdateRequest updateRequest, Object o) {
		if (o instanceof Map) {
			updateRequest = updateRequest.doc((Map) o);
		} else if (o instanceof String) {
			updateRequest = updateRequest.doc((String) o);
		}
		return updateRequest;
	}

	@Override
	public void saveOrUpdate(ElasticSearchRequest request, Map map) throws ExecutionException, InterruptedException {
		//如果存在，则进行更新
		if (isExist(request)) {
			updateDocument(request, map);
		} else {
			//不存在则进行保存
			createDocument(request, map);
		}
	}

}
