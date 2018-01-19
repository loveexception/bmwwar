package com.maodajun.bmw.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.cri.Static;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Dumps;
import org.nutz.lang.Each;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import com.maodajun.bmw.bean.Person;
import com.maodajun.bmw.bean.Subtopic;
import com.maodajun.bmw.bean.Topic;
import com.maodajun.bmw.bean.User;

//import com.maodajun.bmw.bean.Login;

@IocBean
@At("/subtopic")
@Ok("json:{ignoreNull:true}")
@Fail("http:500") // 抛出异常的话,就走500页面
@Filters(@By(type = CheckSession.class, args = { "me", "/" })) // 检查当前Session是否带me这个属性
public class SubtopicModule {

	@Inject
	protected Dao dao;

	@At
	public int count() {
		return dao.count(Subtopic.class);
	}



	@At
	public Object update(@Param("..") Subtopic sub) {
		NutMap re = new NutMap();

		dao.updateIgnoreNull(sub);// 真正更新的其实只有password和salt
		return re.setv("ok", true);
	}

	@At
	public Object delete(@Param("id") int id, @Attr("me") int me) {
		dao.delete(Subtopic.class, id); // 再严谨一些的话,需要判断是否为>0
		return new NutMap().setv("ok", true);
	}

    @At
    public Object query(@Param("tid")String tid, @Param("..")Pager pager) {
        QueryResult qr = new QueryResult();
        List<Subtopic> list =dao.query(Subtopic.class, Cnd.where("tid","=",tid), null);
        List<Subtopic> result = new ArrayList<Subtopic>();
        for (Subtopic subtopic : list) {
			subtopic = dao.fetchLinks(subtopic,"person");
			result.add(subtopic);
			System.out.println("mao"+subtopic.getPerson());
		}
        qr.setList(result);
        return qr; //默认分页是第1页,每页20条
    }
    @At
    public Object add(@Param("name")String name,@Param("type")String type,@Param("..")Subtopic sub) {
    	name = Strings.trim(name);
        NutMap re = new NutMap();
        sub.setStatus("1");
        Person person = null;
        if(Strings.isNotBlank(name)){
        	Cnd cnd = Cnd.where("name","like","%"+name+"%");
        	if(Strings.equals(type,"2")){
            	cnd.and("oldid","=",type);
        	}
        	person = dao.fetch(Person.class, cnd);
        }
        
        if(person==null){
        	Cnd cnd =Cnd.where("1","=","1");
        	if(Strings.equals(type, "2")){
        		cnd.and("oldid","=",type);
        	}
        	person =dao.fetch(Person.class,cnd.asc("rand()"));
        }
    	sub.setPid(person.getId());

    	
        
        String msg = checkTopic(sub, true);
        if (msg != null){
            return re.setv("ok", false).setv("msg", msg);
        }
       
        sub = dao.insert(sub);
        return re.setv("ok", true).setv("data", sub);
    }

	private String checkTopic(Subtopic sub, boolean b) {
		if(sub.getPlan()==null){
			return "必须指定发贴时间";
		}
		return null;
	}



	@At("/main")
	@Ok("jsp:jsp.subtopic.main") // 真实路径是 /WEB-INF/jsp/person/list.jsp
	public String main(@Param("tid")String tid) {
		return tid;
	}


}