package com.ptmind.elasticsearch.factory;

import com.ptmind.domain.elasticsearch.entity.ESQueryModel;
import com.ptmind.elasticsearch.system.Constants;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BoolQueryFactory
 * @Description:.
 * @Company: Copyright (c) Pt mind
 * @version: 2.1
 * @date: 2017/4/12
 * @author: ptmind
 */
@Service("boolQueryFactory")
@Scope("prototype")
public class BoolQueryFactory implements ElasticFactory {

	private BoolQueryBuilder boolQueryBuilder;

	public BoolQueryFactory instance() {
		boolQueryBuilder = new BoolQueryBuilder();
		return this;
	}

	/**
	 * 必须相等的字段（不分词）
	 *
	 * @date: 2017/4/13
	 * @author: zhangli
	 * @param mustTermsMap
	 * @return
	 */
	public BoolQueryFactory mustTerms(Map<String, Object> mustTermsMap) {
		if(!CollectionUtils.isEmpty(mustTermsMap)){
			for (Map.Entry entry : mustTermsMap.entrySet()) {
				boolQueryBuilder.must(QueryBuilders.termQuery((String) entry.getKey(), entry.getValue()));
			}
		}
		return this;
	}

	/**
	 * 必须分词搜索的字段
	 *
	 * @date: 2017/4/13
	 * @author: zhangli
	 * @param esQueryModel
	 * @return
	 */
	public BoolQueryFactory mustMatch(ESQueryModel esQueryModel){
		List<String> mustSearchFields = esQueryModel.getMustSearchFields();
		BoolQueryBuilder mustMatch = new BoolQueryBuilder();
		for(Map.Entry<String,String> entry : esQueryModel.getAnalyzerMapping().entrySet()){
			for(int i = 0;i < mustSearchFields.size();i++){
				mustMatch.should(QueryBuilders.matchQuery(mustSearchFields.get(i) + Constants.SPLIT_CHAR + entry.getKey(),
								QueryParser.escape(esQueryModel.getSearchKey())).analyzer(entry.getValue()).operator(Operator.OR));
			}
		}
		boolQueryBuilder.must(mustMatch);
		return this;
	}

	/**
	 * 模糊查询，搜索性能有一定下降
	 *
	 * @date: 2017/4/13
	 * @author: zhangli
	 * @param esQueryModel
	 * @return
	 */
	public BoolQueryFactory mustWildCard(ESQueryModel esQueryModel){
		List<String> mustSearchFields = esQueryModel.getMustSearchFields();
		BoolQueryBuilder mustMatch = new BoolQueryBuilder();
		for(int i = 0;i < mustSearchFields.size();i++){
			mustMatch.should(QueryBuilders.wildcardQuery(mustSearchFields.get(i) + Constants.SPLIT_CHAR + Constants.FILED_ORIGINAL ,
							".*" + QueryParser.escape(esQueryModel.getSearchKey()) + ".*"));
		}
		boolQueryBuilder.must(mustMatch);
		return this;
	}

	/**
	 * 正则搜索，搜索性能有一定下降
	 *
	 * @date: 2017/4/13
	 * @author: zhangli
	 * @param esQueryModel
	 * @return
	 */
	public BoolQueryFactory mustRegexp(ESQueryModel esQueryModel){
		List<String> mustSearchFields = esQueryModel.getMustSearchFields();
		BoolQueryBuilder mustMatch = new BoolQueryBuilder();
		for(int i = 0;i < mustSearchFields.size();i++){
			mustMatch.should(QueryBuilders.regexpQuery(mustSearchFields.get(i) + Constants.SPLIT_CHAR + Constants.FILED_ORIGINAL ,
							".*" + QueryParser.escape(esQueryModel.getSearchKey()) + ".*"));
		}
		boolQueryBuilder.must(mustMatch);
		return this;
	}

	public BoolQueryBuilder get(){
		return boolQueryBuilder;
	}

}
