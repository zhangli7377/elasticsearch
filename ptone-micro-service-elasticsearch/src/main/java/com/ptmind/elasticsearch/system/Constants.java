package com.ptmind.elasticsearch.system;

import org.springframework.stereotype.Component;

/**
 * @ClassName: Constants
 * @Description:.
 * @Company: Copyright (c) Pt mind
 * @version: 2.1
 * @date: 2017/4/13
 * @author: zhangli
 */
@Component
public class Constants {

	public static String SPLIT_CHAR = ".";
	public static final String FILED_ORIGINAL = "original"; //字段原始数据不分词

	/*public String i18nFields[] = {"cn","en","jp","pinyin_cn"};

	public String[] i18nFieldBuilder(List<String> searchFields){
		List<String> fields = new ArrayList<String>();
		for(int i = 0;i < searchFields.size();i++){
			for(int k = 0;k < i18nFields.length;k++){
				fields.add(searchFields.get(i) + SPLIT_CHAR + i18nFields[k]);
			}
		}
		fields.addAll(searchFields);
		return (String[])fields.toArray(new String[fields.size()]);
	}*/

}
