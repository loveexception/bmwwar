package com.maodajun.bmw.module;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
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

import com.maodajun.bmw.bean.Plan;

@IocBean
@At("/plan")
@Ok("json:{locked:'password|salt',ignoreNull:true}")
@Fail("http:500") // 抛出异常的话,就走500页面
@Filters(@By(type=CheckSession.class, args={"me", "/"})) // 检查当前Session是否带me这个属性
public class PlanModule {

    @Inject
    protected Dao dao;

    @At
    public int count() {
        return dao.count(Plan.class);
    }

    @At
    @Ok(">>:/")
    public void logout(HttpSession session) {
        session.invalidate();
    }
    @At
    public Object add(@Param("..")Plan plan) {
        NutMap re = new NutMap();

        plan = dao.insert(plan);
        return re.setv("ok", true).setv("data", plan);
    }
    @At
    public Object update(@Param("..")Plan plan) {
        NutMap re = new NutMap();

        plan.setName(null);// 不允许更新用户名

        dao.updateIgnoreNull(plan);// 真正更新的其实只有password和salt
        return re.setv("ok", true);
    }
    @At
    public Object delete(@Param("id")int id, @Attr("me")int me) {

        dao.delete(Plan.class, id); // 再严谨一些的话,需要判断是否为>0
        return new NutMap().setv("ok", true);
    }
    @At
    public Object kill(@Attr("me")int me) {
    	dao.clear(Plan.class);
        return new NutMap().setv("ok", true);
    }
    @At
    public Object query(@Param("name")String name, @Param("..")Pager pager) {
        Cnd cnd = Strings.isBlank(name)? null : Cnd.where("name", "like", "%"+name+"%");
        QueryResult qr = new QueryResult();
        qr.setList(dao.query(Plan.class, cnd, null));

        return qr; //默认分页是第1页,每页20条
    }

    @At("/main")
    @Ok("jsp:jsp.plan.main") // 真实路径是 /WEB-INF/jsp/user/list.jsp
    public void main() {
    }
 
}