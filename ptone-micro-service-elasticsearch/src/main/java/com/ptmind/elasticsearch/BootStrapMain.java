package com.ptmind.elasticsearch;

import com.ptmind.domain.elasticsearch.dubbo.ElasticSearchDao;
import com.ptmind.domain.elasticsearch.entity.ElasticSearchRequest;
import com.ptmind.domain.elasticsearch.entity.ElasticsearchPage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by ptmind on 2015/9/9.
 */

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class BootStrapMain {

	public static Logger logger = LoggerFactory.getLogger(BootStrapMain.class);

	public static void main(String[] args) {
		logger.info("ElasticSearch Client Service init start ......");
		ApplicationContext applicationContext = SpringApplication.run(BootStrapMain.class, args);
		logger.info("ElasticSearch Client Service init end ......");

//		test(applicationContext);
	}

	private static void test(ApplicationContext applicationContext) {
		ElasticSearchDao elasticSearchDao = (ElasticSearchDao) applicationContext.getBean("elasticSearchDao");
		testCreateDocument(elasticSearchDao);
		//testDeleteDocument(elasticSearchDao);
		//testUpdateDocument(elasticSearchDao);
		//testQueryDocument(elasticSearchDao);
//		testCount(elasticSearchDao);
	}

	@Test
	private static void testCreateDocument(ElasticSearchDao elasticSearchDao) {
		ElasticSearchRequest elasticSearchRequest = new ElasticSearchRequest("test_index", "test_type", "1001");
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "1001");
		map.put("name", "zhangsan");

		elasticSearchDao.createDocument(elasticSearchRequest, map);
	}

	private static void testDeleteDocument(ElasticSearchDao elasticSearchDao) {
		ElasticSearchRequest elasticSearchRequest = new ElasticSearchRequest("test_index", "test_type", "1001");
		elasticSearchDao.deleteDocument(elasticSearchRequest);
	}

	private static void testUpdateDocument(ElasticSearchDao elasticSearchDao) throws ExecutionException, InterruptedException {
		ElasticSearchRequest elasticSearchRequest = new ElasticSearchRequest("test_index", "test_type", "1001");
		Map<String, String> map = new HashMap<String, String>();
		elasticSearchDao.updateDocument(elasticSearchRequest, map);
	}

	private static void testQueryDocument(ElasticSearchDao elasticSearchDao) {
		ElasticSearchRequest elasticSearchRequest = new ElasticSearchRequest("ptone", "widget");
		List<String> list1 = elasticSearchDao.queryDocument(elasticSearchRequest, "line", "Graph_Name");

		printList(list1);

		List<String> list2 = elasticSearchDao.queryDocument(elasticSearchRequest, "areaspline");

		printList(list2);

		ElasticsearchPage page = new ElasticsearchPage();
		page.setStart(0);
		page.setSize(3);

		List<String> list3 = elasticSearchDao.queryDocumentByPage(elasticSearchRequest, "user", page);
		printList(list3);

	}

	private static void printList(List<String> list) {
		System.out.println("start============================");
		for (String str : list) {
			System.out.println(str);
		}
		System.out.println("end============================");
	}

}
