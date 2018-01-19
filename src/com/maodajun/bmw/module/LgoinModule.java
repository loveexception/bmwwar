package com.maodajun.bmw.module;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.random.ArrayRandom;
import org.nutz.lang.random.ListRandom;
import org.nutz.lang.random.R;
import org.nutz.lang.random.Random;
import org.nutz.lang.random.RecurArrayRandom;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import com.maodajun.bmw.bean.Login;
import com.maodajun.bmw.bean.Person;
import com.maodajun.bmw.bean.Plan;

@IocBean
@At("/login")
@Ok("json:{ignoreNull:true}")
@Fail("http:500") // 抛出异常的话,就走500页面
@Filters(@By(type = CheckSession.class, args = { "me", "/" })) // 检查当前Session是否带me这个属性
public class LgoinModule {

	@Inject
	protected Dao dao;

	@At
	public int count() {
		return dao.count(Login.class);
	}

	@At
	public Object add(@Param("..") Login login,@Attr("me") int me) {
		NutMap re = new NutMap();
		login.setStauts("1");
		login.setUid(me);
		login = dao.insert(login);
		return re.setv("ok", true).setv("data", login);
	}
	@At
	public Object clearhistory( ) {
		NutMap re = new NutMap();
		Calendar cal = toDay();
		Date end = cal.getTime();
		dao.clear(Login.class,Cnd.where("plan","<",end).and("stauts","<>","9"));
		return re.setv("ok", true).setv("data", end);
		
	}
	@At
	public Object cleartoday() {
		NutMap re = new NutMap();
		Calendar cal = toDay();
		Date start = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date end = cal.getTime();
		dao.clear(Login.class,Cnd.where("plan",">",start).and("plan","<",end).and("stauts","<>","9"));
		return re.setv("ok", true).setv("data", start);
	}

	private Calendar toDay() {
		Calendar cal  = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal;
	}
	@At
	public Object cleartomorrow() {
		NutMap re = new NutMap();
		Calendar cal = tomorrow();
		Date start = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date end = cal.getTime();
		dao.clear(Login.class,Cnd.where("plan",">",start).and("plan","<",end).and("stauts","<>","9"));
		return re.setv("ok", true).setv("data", start);
	}

	private Calendar tomorrow() {
		Calendar cal = toDay();
		cal.add(Calendar.DATE, 1);
		return cal;
	}
	
	@At
	public Object randweek( @Attr("me") int me) {
		NutMap re = new NutMap();

		for(int i =0 ; i <7 ; i++){
			clearday(i);

			Calendar cal = nextDay(i);
			
			Date day = cal.getTime();
			re = (NutMap)rand(day,new Login(),me);
			if(!re.getBoolean("ok")){
				return re;
			}
			
		}

		return re.setv("ok", true);
		
	}

	private void clearday(int today) {
		Calendar cal = nextDay(today);
		cal.add(Calendar.DATE, 1);
		Date start = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date end = cal.getTime();
		System.out.println("mao-start"+ start);
		System.out.println("mao-end"+ end);
		
		dao.clear(Login.class,Cnd.where("plan",">",start).and("plan","<",end).and("stauts","<>","9"));
	}

	private Calendar nextDay(int i) {
		Calendar cal = toDay();
		cal.add(Calendar.DATE, i);
		return cal;
	}


	@At
	public Object rand(@Param("today") Date today,@Param("..") Login login, @Attr("me") int me) {
		List<Plan> plans = dao.query(Plan.class, null);
		
		List<Person> persons = dao.query(Person.class,null);
		Random<Person> r = new ListRandom<Person>(persons);

		List<Login> logins = new ArrayList<Login>();
		for (Plan plan : plans) {
			for(int i = 0 ; i < plan.getCount();i++ ){
				Login tmp = new Login();
				Person one = r.next();
				if(one==null){
					continue;
				}
		
				Date d = randDay(plan, today);
				
				tmp.setPid(one.getId());
				tmp.setStauts("1");
	
				tmp.setPlan(d);
				tmp.setUid(me);
				logins.add(tmp);
			}
		}
		for (Login tmp : logins) {
			dao.insert(tmp);
		}
		NutMap re = new NutMap();
		return re.setv("ok", true).setv("data", logins);
	}

	private Date randDay(Plan plan, Date today) {
		Calendar cal  = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(plan.getName()));
		cal.set(Calendar.MINUTE, R.random(0, 59));
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DATE, 1);
		Date d = cal.getTime();
		return d;
	}

	@At
	public Object update(@Param("..") Login login) {
		NutMap re = new NutMap();

		dao.updateIgnoreNull(login);
		return re.setv("ok", true);
	}

	@At
	public Object delete(@Param("id") int id, @Attr("me") int me) {
		dao.delete(Login.class, id); 
		return new NutMap().setv("ok", true);
	}

	@At
	public Object query(@Param("name") String name, @Param("..") Pager pager) {
		Cnd cnd = Strings.isBlank(name) ? null : Cnd.where("name", "like", "%" + name + "%");
		QueryResult qr = new QueryResult();
		List<Login> list = dao.query(Login.class, cnd, pager);


		qr.setList(list);
		pager.setRecordCount(dao.count(Login.class, cnd));
		qr.setPager(pager);
		return qr; // 默认分页是第1页,每页20条
	}

	@At("/")
	@Ok("jsp:jsp.login.list") 
	public void index() {
	}


}