package com.ptmind.domain.elasticsearch.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ESQueryModel
 * @Description:.
 * @Company: Copyright (c) Pt mind
 * @version: 2.1
 * @date: 2017/4/1
 * @author: zhangli
 */
public class ESQueryModel implements Serializable {

	private static final long serialVersionUID = -6686242192610365094L;

	private String index;
	private String type;
	private String id;
	private int limit;//返回前多少条记录
	private String searchKey;//搜索的关键字
	private List<String> mustSearchFields;//搜索的字段
	private Map<String,Object> mustTermsMap;//字段必须完全匹配的值
	private Map<String,String> analyzerMapping;//搜索的后缀及对应的分析器
	private String minimumShouldMatch;//匹配度

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<String> getMustSearchFields() {
		return mustSearchFields;
	}

	public void setMustSearchFields(List<String> mustSearchFields) {
		this.mustSearchFields = mustSearchFields;
	}

	public Map<String, Object> getMustTermsMap() {
		return mustTermsMap;
	}

	public void setMustTermsMap(Map<String, Object> mustTermsMap) {
		this.mustTermsMap = mustTermsMap;
	}

	public Map<String, String> getAnalyzerMapping() {
		return analyzerMapping;
	}

	public void setAnalyzerMapping(Map<String, String> analyzerMapping) {
		this.analyzerMapping = analyzerMapping;
	}

	public String getMinimumShouldMatch() {
		return minimumShouldMatch;
	}

	public void setMinimumShouldMatch(String minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
	}
}
