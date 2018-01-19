package com.maodajun.bmw.bean;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import com.mysql.fabric.xmlrpc.base.Array;

@Table("t_persons")

public class Person  implements Comparable{
    @Id
    private int id;
    @Name
    @Column
    private String name;
    @Column
    private String password;
    @Column
    @Comment("原服务  id")
    private String oldid;
    @Many(field = "pid")
    private List<Login> logins;  
    
    private Login today = new Login();
    private Login tomorrow = new Login();
    private List<Login> week = new ArrayList();
    
	public List<Login> getWeek() {
		return week;
	}
	public void setWeek(List<Login> week) {
		this.week = week;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getOldid() {
		return oldid;
	}
	public void setOldid(String oldid) {
		this.oldid = oldid;
	}
	public List<Login> getLogins() {
		return logins;
	}
	public void setLogins(List<Login> logins) {
		this.logins = logins;
	}
	public Login getToday() {
		return today;
	}
	public void setToday(Login today) {
		this.today = today;
	}
	public Login getTomorrow() {
		return tomorrow;
	}
	public void setTomorrow(Login tomorrow) {
		this.tomorrow = tomorrow;
	}

	@Override
	public int compareTo(Object o) {
		Person obj = (Person)o;
		return obj.id-id;
	}
	
}
