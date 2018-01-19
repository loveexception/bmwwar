package com.maodajun.bmw.module;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.nutz.castor.Castors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.cri.Static;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Dumps;
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

import com.maodajun.bmw.bean.Login;
import com.maodajun.bmw.bean.Person;
import com.maodajun.bmw.bean.User;

@IocBean
@At("/person")
@Ok("json:{ignoreNull:true}")
@Fail("http:500") // 抛出异常的话,就走500页面
@Filters(@By(type = CheckSession.class, args = { "me", "/" })) // 检查当前Session是否带me这个属性
public class PersonModule {

	@Inject
	protected Dao dao;

	@At
	public int count() {
		return dao.count(Person.class);
	}
	
    @At
    public Object add(@Param("..")Person person) {
        NutMap re = new NutMap();
        String msg = checkUser(person, true);
        if (msg != null){
            return re.setv("ok", false).setv("msg", msg);
        }

        person = dao.insert(person);
        return re.setv("ok", true).setv("data", person);
    }


	private String checkUser(Person person, boolean b) {
		if(Strings.isBlank(person.getPassword())){
			return "密码不能为空";
		}
		return null;
	}
	@At
	public Object update(@Param("..") Person user) {
		NutMap re = new NutMap();

		user.setName(null);// 不允许更新用户名

		dao.updateIgnoreNull(user);// 真正更新的其实只有password和salt
		return re.setv("ok", true);
	}

	@At
	public Object delete(@Param("id") int id, @Attr("me") int me) {
		dao.delete(Person.class, id); // 再严谨一些的话,需要判断是否为>0
		return new NutMap().setv("ok", true);
	}

	@At
	public Object query(@Param("name") String name,@Param("oldid") String oldid,@Param("..") Pager pager) {
		Cnd cnd = Strings.isBlank(name) ? Cnd.where("1","=","1") : Cnd.where("name", "like", "%" + name + "%");
		if(Strings.isNotBlank(oldid)){
			cnd.and("oldid","=",oldid);
		}
		QueryResult qr = new QueryResult();
		List<Person> list = dao.query(Person.class, cnd, pager);
		List<Integer> persons = new ArrayList();
		for (Person person : list) {
			persons.add(person.getId());

		}
		Cnd tomorrowCnd = Cnd.where("pid", "in", persons).and(new Static("TO_DAYS( plan) - TO_DAYS( NOW( ) ) < 8"));
		Cnd todayCnd = Cnd.where("pid", "in", persons).and(new Static("TO_DAYS( plan) = TO_DAYS( NOW( ) ) "));
		List<Login> todays = dao.query(Login.class, todayCnd);
		List<Login> tomorrows = dao.query(Login.class, tomorrowCnd);
		System.out.println(Dumps.obj(tomorrows));
		for (Person person : list) {
			for(Login login : todays){
				if(login.getPid() == person.getId()){
					person.setToday(login);
					break;
				}
			}
			for(Login login : tomorrows){
				if(login.getPid() == person.getId()){
					person.setTomorrow(login);
					person.getWeek().add(login);
					//break;
				}
			}
		}
		qr.setList(list);
		pager.setRecordCount(dao.count(Person.class, cnd));
		qr.setPager(pager);
		return qr; // 默认分页是第1页,每页20条
	}


	@At("/main")
	@Ok("jsp:jsp.person.main") // 真实路径是 /WEB-INF/jsp/person/list.jsp
	public void main() {
	}


}