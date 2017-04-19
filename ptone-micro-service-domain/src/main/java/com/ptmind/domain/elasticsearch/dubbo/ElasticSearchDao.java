package com.ptmind.domain.elasticsearch.dubbo;

import com.ptmind.domain.elasticsearch.entity.ESQueryModel;
import com.ptmind.domain.elasticsearch.entity.ElasticSearchRequest;
import com.ptmind.domain.elasticsearch.entity.ElasticsearchPage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by ptmind on 2015/9/9.
 */
public interface ElasticSearchDao {
	/**
	 * 创建document
	 *
	 * @param elasticSearchRequest
	 * @param map
	 */
	public void createDocument(ElasticSearchRequest elasticSearchRequest, Map map);

	public void createDocument(ElasticSearchRequest elasticSearchRequest, String json);

	/**
	 * 删除document
	 *
	 * @param elasticSearchRequest
	 * @return
	 */
	public void deleteDocument(ElasticSearchRequest elasticSearchRequest);

	/**
	 * 更新document
	 *
	 * @param elasticSearchRequest
	 * @param map
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void updateDocument(ElasticSearchRequest elasticSearchRequest, Map map) throws ExecutionException, InterruptedException;

	public void updateDocument(ElasticSearchRequest elasticSearchRequest, String json) throws ExecutionException, InterruptedException;

	public void updateDocument(ElasticSearchRequest elasticSearchRequest, String field, Object value) throws ExecutionException, InterruptedException;

	/**
	 * 根据分词查询
	 *
	 * @return
	 * @date 20170401
	 * @author zhangli
	 */
	public List<String> queryDocument(ESQueryModel esQueryModel);

	/**
	 * WildCard查询
	 *
	 * @return
	 * @date 20170401
	 * @author zhangli
	 */
	public List<String> queryDocumentWildCard(ESQueryModel esQueryModel);

	/**
	 * 正则查询
	 *
	 * @return
	 * @date 20170401
	 * @author zhangli
	 */
	public List<String> queryDocumentRegexp(ESQueryModel esQueryModel);

	/**
	 * 查询document
	 *
	 * @param request
	 * @param searchValue
	 * @param field
	 * @return
	 */
	public List<String> queryDocument(ElasticSearchRequest request, String searchValue, String field);

	public List<String> queryDocumentId(ElasticSearchRequest request, String searchValue, String field);

	public List<String> queryDocument(ElasticSearchRequest request, String searchValue);

	public List<String> queryDocumentId(ElasticSearchRequest request, String searchValue);

	public List<String> queryDocument(ElasticSearchRequest request, String searchValue, List<String> fields);

	public List<String> queryDocumentId(ElasticSearchRequest request, String searchValue, List<String> fields);

	/**
	 * 带有分页的查询
	 *
	 * @param request
	 * @param searchValue
	 * @param field
	 * @param page
	 * @return
	 */
	public List<String> queryDocumentByPage(ElasticSearchRequest request, String searchValue, String field, ElasticsearchPage page);

	public List<String> queryDocumentIdByPage(ElasticSearchRequest request, String searchValue, String field, ElasticsearchPage page);

	public List<String> queryDocumentByPage(ElasticSearchRequest request, String searchValue, ElasticsearchPage page);

	public List<String> queryDocumentIdByPage(ElasticSearchRequest request, String searchValue, ElasticsearchPage page);

	public List<String> queryDocumentByPage(ElasticSearchRequest request, String searchValue, List<String> fields, ElasticsearchPage page);

	public List<String> queryDocumentIdByPage(ElasticSearchRequest request, String searchValue, List<String> fields, ElasticsearchPage page);

	/**
	 * 判断某个id是否存在
	 *
	 * @param request
	 * @return
	 */
	public boolean isExist(ElasticSearchRequest request);

	public void saveOrUpdate(ElasticSearchRequest request, Map map) throws ExecutionException, InterruptedException;


}
