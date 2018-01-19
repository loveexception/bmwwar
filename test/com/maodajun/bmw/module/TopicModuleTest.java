package com.maodajun.bmw.module;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.util.PType;

import com.maodajun.bmw.bean.Topic;

public class TopicModuleTest {
	Dao dao = null;
	@Before
	public void startUp(){
		// 创建一个数据源
		SimpleDataSource dataSource = new SimpleDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/bmw?useUnicode=true&characterEncoding=UTF-8");
		dataSource.setUsername("maodajun");
		dataSource.setPassword("maodajun");

		// 创建一个NutDao实例,在真实项目中, NutDao通常由ioc托管, 使用注入的方式获得.
		dao = new NutDao(dataSource);

	}

	
	@Test
	public void testMain() {
		
		String host = "http://139.217.15.159/newbocc";
		String name = "";
		String page = "1";
		String url = "{0}/mobcent/app/web/?r=forum/newtopiclist&keyword={1}&page={2}&pageSize=100&inajax=1";
		url = MessageFormat.format(url,host,name,page);
		assertEquals(url, "http://139.217.15.159/newbocc/mobcent/app/web/?r=forum/newtopiclist&keyword=&page=1&pageSize=100&inajax=1");
		Response response = Http.get(url);
		String str = response.getContent();
		Map<String, Object> map = Json.fromJson(new PType<Map<String, Object>>() {
		}, str/* 其他源也可以 */);
		List<Map<String,String>> list = (List<Map<String,String>>) map.get("data");
		List<Topic> result= new ArrayList<Topic>();
		for (Map<String,String> temp : list) {

			
			Topic topic = dao.fetch(Topic.class, Cnd.where("oldid","=",temp.get("topic_id")));
			if(topic==null){
				topic = new Topic();
				topic.setName(""+temp.get("topic_title"));
				topic.setContent(""+temp.get("topic_content"));
				topic.setLiked(Integer.parseInt(""+temp.get("topic_zans")));
				topic.setCollected(Integer.parseInt(""+temp.get("topic_collections")));
				topic.setReaded(Integer.parseInt(""+temp.get("topic_views")));
				topic.setOldid(""+temp.get("topic_id"));
				dao.insert(topic);
			}else{
				topic.setName(""+temp.get("topic_title"));
				topic.setContent(""+temp.get("topic_content"));
				topic.setLiked(Integer.parseInt(""+temp.get("topic_zans")));
				topic.setCollected(Integer.parseInt(""+temp.get("topic_collections")));
				topic.setReaded(Integer.parseInt(""+temp.get("topic_views")));
				topic.setOldid(""+temp.get("topic_id"));
				dao.update(topic);
				
			}
			result.add(topic);			
		}
		
		
		System.out.println(result);
	}

}
