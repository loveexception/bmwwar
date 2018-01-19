package com.maodajun.bmw.module;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.cri.Static;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Dumps;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.lang.util.NutType;
import org.nutz.lang.util.PType;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import com.maodajun.bmw.bean.Topic;


@IocBean
@At("/topic")
@Ok("json:{ignoreNull:true}")
@Fail("http:500") // 抛出异常的话,就走500页面
@Filters(@By(type = CheckSession.class, args = { "me", "/" })) // 检查当前Session是否带me这个属性
public class TopicModule {

	@Inject
	protected Dao dao;

	@At
	public int count() {
		return dao.count(Topic.class);
	}



	@At
	public Object update(@Param("..") Topic topic) {
		NutMap re = new NutMap();
		
		topic.setName(null);// 不允许更新用户名
		dao.updateIgnoreNull(topic);// 
		return re.setv("ok", true);
	}

	@At
	public Object delete(@Param("id") int id, @Attr("me") int me) {
		dao.delete(Topic.class, id); // 再严谨一些的话,需要判断是否为>0
		return new NutMap().setv("ok", true);
	}

	@At
	public Object query(@Param("name") String name,@Param("type") String type, @Param("..") Pager pager) {
		Cnd cnd = Cnd.where("1","=",1);
		if(Strings.isNotBlank(name)){
			cnd.and("name", "like", "%" + name + "%");
		}
		if(Strings.isNotBlank(type)){
			cnd.and("type","=",type);
		}else{
			cnd.and("type","<>","1");
		}
		cnd.desc("sort").desc("id");
		QueryResult qr = new QueryResult();
		List<Topic> list = dao.query(Topic.class, cnd, pager);

		qr.setList(list);
		pager.setRecordCount(dao.count(Topic.class, cnd));
		qr.setPager(pager);
		return qr; // 默认分页是第1页,每页20条
	}
	@At
	public Object get(@Param("id") String id) {
		Topic obj =dao.fetch(Topic.class,Integer.parseInt(id));
		return obj; // 默认分页是第1页,每页20条
	}
	@At
	@Filters
	public Object spider(@Param("name") String name,@Param("type") String type, @Param("..") Pager pager) throws Exception {
		String typed="";
		if(Strings.equals(type, "1")){
			typed= "&type="+type;
		}else{
			type="2";
		}
        name = name!=null?name :"";
		String host = "https://www.mybmwclub.cn/";
		String page = ""+pager.getPageNumber();
		String url = "{0}/mobcent/app/web/?r=forum/newtopiclist&keyword={1}&page={2}{3}&inajax=1";
		
		url = MessageFormat.format(url,host,name,page,typed);
		System.out.println(url);
		Response response = Http.get(url);
		String str = response.getContent();
		Map<String, Object> map = Json.fromJson(new PType<Map<String, Object>>() {
		}, str/* 其他源也可以 */);
		List<Map<String,String>> list = (List<Map<String,String>>) map.get("data");
		List<Topic> result= new ArrayList<Topic>();
		for (int i =0 ; i< list.size();i++) {
			Map<String,String> temp  = list.get(list.size()-i-1);
			Topic topic = dao.fetch(Topic.class, Cnd.where("oldid","=",temp.get("topic_id")));
			if(topic==null){
				topic = new Topic();
				topic.setName(""+temp.get("topic_title")+":"+temp.get("topic_id"));
				topic.setContent(""+temp.get("topic_content"));
				topic.setLiked(Integer.parseInt(""+temp.get("topic_zans")));
				topic.setCollected(Integer.parseInt(""+temp.get("topic_collections")));
				topic.setReaded(Integer.parseInt(""+temp.get("topic_views")));
				topic.setType(type);
				topic.setOldid(""+temp.get("topic_id"));
				topic.setReplies(""+temp.get("topic_replies"));
				topic.setSort("0");
				dao.insert(topic);
			}else{
				topic.setName(""+temp.get("topic_title")+":"+temp.get("topic_id"));
				topic.setContent(""+temp.get("topic_content"));
				topic.setLiked(Integer.parseInt(""+temp.get("topic_zans")));
				topic.setCollected(Integer.parseInt(""+temp.get("topic_collections")));
				topic.setReaded(Integer.parseInt(""+temp.get("topic_views")));
				topic.setOldid(""+temp.get("topic_id"));
				topic.setType(type);
				topic.setReplies(""+temp.get("topic_replies"));
				dao.update(topic);
				
			}
			result.add(topic);			
		}
		QueryResult qr = new QueryResult();

		//qr.setList(result);
		//pager.setRecordCount(dao.count(Topic.class, null));
		//qr.setPager(pager);
        
        return query(name,type,pager);
	}



	@At("/main")
	@Ok("jsp:jsp.topic.main") // 真实路径是 /WEB-INF/jsp/topic/list.jsp
	public void main() {
		
	}
	@At("/newone")
	@Ok("jsp:jsp.topic.newone") // 真实路径是 /WEB-INF/jsp/topic/list.jsp
	public void newone() {
	}

	
}