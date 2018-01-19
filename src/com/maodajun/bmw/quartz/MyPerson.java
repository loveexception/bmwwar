package com.maodajun.bmw.quartz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.maodajun.bmw.bean.Login;
import com.maodajun.bmw.bean.Subtopic;
import com.maodajun.bmw.bean.Topic;
@IocBean
public class MyPerson {
	@Inject protected Dao dao;
	
	/**
	 * 当时需要登陆的人
	 * @param date
	 * @return
	 */
	public List<Login> loginsByDate(Date time) {
		
    	Cnd cnd =Cnd.where("stauts", "=", "1");
    	if(time==null){
    		time = new Date();
    	}
    	cnd = cnd.and("plan", "<=",time);
    	Pager page = new Pager();
    	page.setPageSize(50);
		List<Login> logins = dao.query(Login.class, cnd,page );
 		return logins;		
	}

	public List<Subtopic> subtopicByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Topic> findReadedPlan(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Topic> findLikedPlan(Date date) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Topic> findcolledPlan(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(List<Topic> topics) {
		// TODO Auto-generated method stub
		
	}

	public List<Topic> mix(List<Topic> reads, List<Topic> likes, List<Topic> colls) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(Login login) {
		// TODO Auto-generated method stub
		
	}

	public void save(Subtopic sub) {
		// TODO Auto-generated method stub
		
	}

	

}
