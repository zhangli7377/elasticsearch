package com.ptmind.elasticsearch.core;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: HitSourceGet
 * @Description:.
 * @Company: Copyright (c) Pt mind
 * @version: 2.1
 * @date: 2017/4/13
 * @author: ptmind
 */
@Service("hitSourceGet")
public class HitSourceGet {

	public List<String> listHitSource(SearchResponse searchResponse){
		List<String> list = new ArrayList<String>();
		SearchHit[] hits = searchResponse.getHits().getHits();
		for (SearchHit searchHit : hits) {
			String sourceStr = searchHit.getSourceAsString();
			list.add(sourceStr);
		}
		return list;
	}

}
