package com.ptmind.elasticsearch.test;

import com.alibaba.dubbo.common.json.JSONObject;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by ptmind on 2015/9/8.
 */
public class ClientTest {

	private static Client client = null;

	@Before
	public void instance() throws UnknownHostException {
		Settings settings = Settings.builder()
//		 .put("client.transport.sniff", true).build();
						.put("cluster.name", "datadeck")
//						.put("xpack.security.user", "ptone:ptone2008")
						.build();

//		String token = basicAuthHeaderValue("test_user", new SecuredString("changeme".toCharArray()));
//
//		client.filterWithHeader(Collections.singletonMap("Authorization", token))
//						.prepareSearch().get();

		/*TransportClient client1 = new PreBuiltTransportClient(settings)
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));*/
		client = new PreBuiltTransportClient(settings)
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.2.33"), 9300));


		//get(client);

		//match(client);
		//update(client);

//		get(client);

		//multiMatch(client);

		//delete(client);
//		multiMatch(client);
		//multiMatch1(client);

		//filter(client);
	}

	public static void indexThreadTest(final Client client) {
		ExecutorService indexTreadPool = Executors.newFixedThreadPool(1000);
		final int threadNum = 201;
		for (int i = 0; i < threadNum; i++) {
			final int index = i;
			indexTreadPool.execute(new Runnable() {
				@Override
				public void run() {
					Map<String, Object> json = new HashMap<String, Object>();
					json.put("user", "kimchy" + index);
					json.put("postDate", new Date());
					json.put("message", "trying out Elasticsearch" + index);
					System.out.println("start run " + index + " thread");
					long startTime = System.currentTimeMillis();
					if (index == threadNum - 1) {
						System.out.println("start run last thread ==============================");
					}
					IndexResponse indexResponse = client.prepareIndex("twitter", "tweet", index + "")
									.setSource(json).get();
					if (index == threadNum - 1) {
						long endTime = System.currentTimeMillis();
						System.out.println("end last index ================================" + indexResponse.getIndex() + "|" + indexResponse.getType() + "|" + indexResponse.getId() + " | execute time :" + (endTime - startTime));
						search(client);
					}
					System.out.println("end " + index + " thread");
				}
			});
		}
		indexTreadPool.shutdown();
	}

	public static void search(Client client) {
//		QueryBuilder queryBuilder = multiMatchQuery("trying out Elasticsearch209", new String[]{"message"});
		QueryBuilder queryBuilder = termQuery("user", "kimchy200");
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("twitter").setTypes("tweet");
		searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
		searchRequestBuilder.setQuery(queryBuilder);
		SearchResponse searchResponse = searchRequestBuilder.get();
		SearchHit[] hits = searchResponse.getHits().getHits();
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getSourceAsString());
		}
	}

	public static void indexTest(Client client) {

		System.out.println("start index");
		Map<String, Object> json = new HashMap<String, Object>();
		for (int i = 0; i < 1000; i++) {

			json = new HashMap<String, Object>();
			json.put("user", "kimchy" + i);
			json.put("postDate", new Date());
			json.put("message", "trying out Elasticsearch" + i);

			client.prepareIndex("twitter", "tweet", i + "")
							.setSource(json).get();
		}
		System.out.println("end index");

		System.out.println("start get id 999 index");

		IndexResponse indexResponse = client.prepareIndex("twitter", "tweet", "999")
						.setSource(json).get();

		String index = indexResponse.getIndex();
		String type = indexResponse.getType();
		String id = indexResponse.getId();
		long version = indexResponse.getVersion();
//		boolean created = indexResponse.isCreated();

		System.out.println(index + "|" + type + "|" + id);
	}

	public static void index(Client client) {

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("user", "kimchy");
		json.put("postDate", new Date());
		json.put("message", "trying out Elasticsearch");

		IndexResponse indexResponse = client.prepareIndex("twitter", "tweet", "1")
						.setSource(json)
						.execute()
						.actionGet();

		String index = indexResponse.getIndex();
		String type = indexResponse.getType();
		String id = indexResponse.getId();
		long version = indexResponse.getVersion();
//		boolean created = indexResponse.isCreated();

		System.out.println();
	}

	public static void get(Client client) {
		GetResponse response = client.prepareGet("ptone", "widget", "AU-vxjqTSph6A_atkMrD").get();
		System.out.println(response.getSourceAsString());

	}

	public static void match(Client client) {

		QueryBuilder queryBuilder = matchQuery("Widget_Title", "民族");

		SearchResponse searchResponse = client.prepareSearch("ptone")
						.setTypes("widget")
						.setSearchType(SearchType.QUERY_THEN_FETCH)
						.setQuery(queryBuilder)
						.setFrom(0)
						.setSize(5)
						.execute()
						.actionGet();

		SearchHit[] hits = searchResponse.getHits().getHits();
		for (SearchHit searchHit : hits) {
			String sourceStr = searchHit.getSourceAsString();
			System.out.println(sourceStr);
		}


	}

	public static void multiMatch(Client client) {
		//在多个字段中进行搜索(_all 代表在所有的字段中进行查询)
		QueryBuilder queryBuilder = multiMatchQuery("shiqu", "panel_title.pinyin_cn");

		SearchResponse searchResponse = client.prepareSearch("datadeck")
						.setTypes("ptone_panel_info")
						.setSearchType(SearchType.QUERY_THEN_FETCH)
						.setQuery(queryBuilder)
						.execute()
						.actionGet();

		SearchHit[] hits = searchResponse.getHits().getHits();
		for (SearchHit searchHit : hits) {
			String sourceStr = searchHit.getSourceAsString();
			System.out.println(sourceStr);
		}

	}

	@Test
	public void multiMatch1() {
		String searchKey = "mianban";
		//在多个字段中进行搜索(_all 代表在所有的字段中进行查询)
		//QueryBuilder commonQueryBuilder = multiMatchQuery(searchKey, new String[]{"panel_title.en","panel_title.cn","panel_title.jp"});
		//QueryBuilder pinyinQueryBuilder = multiMatchQuery(searchKey, new String[]{ "panel_title.pinyin_cn"});

		QueryBuilder commonQB = boolQuery()
						.must(QueryBuilders.multiMatchQuery(searchKey, new String[]{"panel_title.en", "panel_title.cn", "panel_title.jp","panel_title.pinyin_cn"}).minimumShouldMatch("90%"))
						//.should(QueryBuilders.matchQuery("panel_title.pinyin_cn", searchKey).minimumShouldMatch("90%"))
						.must(QueryBuilders.termQuery("space_id","5c463ee8-7a56-438c-857f-4b220d41d763"))
						.must(QueryBuilders.termQuery("status","1"));
		//QueryBuilder pinyinQB = boolQuery().should(QueryBuilders.multiMatchQuery(searchKey, "panel_title.pinyin_cn")).minimumShouldMatch(90);

//			SearchRequestBuilder srb1 = client.prepareSearch().setIndices("datadeck").setTypes("ptone_panel_info").setQuery(commonQB);
//			SearchRequestBuilder srb2 = client.prepareSearch().setQuery(pinyinQB);

		SearchResponse searchResponse = client.prepareSearch("datadeck")
						.setTypes("ptone_panel_info")
						.setSearchType(SearchType.QUERY_THEN_FETCH)
						.setQuery(commonQB)
						.setSize(20)
						.execute()
						.actionGet();

		SearchHit[] hits = searchResponse.getHits().getHits();
		System.out.println("total:" + hits.length);
		for (SearchHit searchHit : hits) {
			String sourceStr = searchHit.getSourceAsString();
			System.out.println(sourceStr);
		}

//			MultiSearchResponse searchResponse = client.prepareMultiSearch()
//							.add(srb1)
//							.add(srb2)
//							.get();
//
//			for (MultiSearchResponse.Item item : searchResponse.getResponses()) {
//				SearchResponse response = item.getResponse();
//				SearchHit[] hits = response.getHits().getHits();
//				for (SearchHit searchHit : hits) {
//					String sourceStr = searchHit.getSourceAsString();
//					System.out.println(sourceStr);
//				}
//			}
	}

	public static void update(Client client) throws Exception {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("ptone");
		updateRequest.type("widget");
		updateRequest.id("AU-vxjqTSph6A_atkMrD");

        /*XContentBuilder builder=jsonBuilder().startObject()
										 .field("Widget_Title","{\\\"zh_CN\\\":\\\"标题\\\",\\\"en_US\\\":\\\"英文标题\\\",\\\"ja_JP\\\":\\\"日文标题\\\"}")
                     .endObject();

        updateRequest.doc(builder);*/

		Map<String, String> map = new HashMap<String, String>();
		map.put("Widget_Title", "{\\\"zh_CN\\\":\\\"中华人民共和国\\\",\\\"en_US\\\":\\\"EN_WidgetTitle\\\",\\\"ja_JP\\\":\\\"JP_WidgetTitle\\\"}");

		updateRequest.doc(map);


		UpdateResponse response = client.update(updateRequest).get();


	}


	public static void delete(Client client) {
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "AU-sfQqkSph6A_atkMon")
						.execute()
						.actionGet();

	}

	public static void count(Client client) {
				/*QueryBuilder queryBuilder=multiMatchQuery("users","_all");
        CountResponse countResponse= client.prepareCount("ptone")
              .setTypes("widget")
              .setQuery(queryBuilder)
              .execute()
              .actionGet();
        System.out.println(countResponse.getCount());
*/

//        CountResponse countResponse= client.prepareCount("ptone")
//                .setTypes("widget")
//                .execute()
//                .actionGet();
//        System.out.println(countResponse.getCount());

	}

//    public static void filter(Client client)
//    {
//
//        //在多个字段中进行搜索(_all 代表在所有的字段中进行查询)
//        QueryBuilder queryBuilder=multiMatchQuery("users","_all");
//
//        Map<String,Object> filterMap=new HashMap<String, Object>();
//
//        filterMap.put("Date_Period","day");
//        filterMap.put("Date_Key","last_week");
//
//        FilterBuilder[] filterBuilderArray=new FilterBuilder[filterMap.keySet().size()];
//
//        int i=0;
//        for(Map.Entry<String,Object> entry:filterMap.entrySet())
//        {
//            FilterBuilder filterBuilder=termFilter(entry.getKey(),entry.getValue());
//            filterBuilderArray[i]=filterBuilder;
//            i++;
//        }
//
//       FilterBuilder filterBuilder=andFilter(filterBuilderArray);
//
//
//        // 组合 QueryBuilder 和 FilterBuilder
//        FilteredQueryBuilder builder=filteredQuery(queryBuilder,filterBuilder);
//
//
//        SearchResponse searchResponse=client.prepareSearch("ptone")
//                .setTypes("widget")
//                .setSearchType(SearchType.QUERY_THEN_FETCH)
//                .setQuery(builder)
//                .execute()
//                .actionGet();
//
//        SearchHit[] hits=searchResponse.getHits().getHits();
//        for(SearchHit searchHit:hits)
//        {
//            String sourceStr=searchHit.getSourceAsString();
//            System.out.println(sourceStr);
//        }
//
//    }


}
