package com.maodajun.bmw.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_logins")
public class Login {
    @Id
    private int id;
   
    @Column
    private int pid;
    
    @One(field = "pid")
    public Person person;

   
    @Column
    private String ip;
    
    @Column
    private String port;
    
    @Column
    @Comment("0立即发布，1按计划发布，9已发布，2连接失败，3服务失败，5暂停，6预定登陆，")
    private String stauts;
    
    @Column
    private Date plan;
    
    @Column
    private Date send;
    
    @Column
    private int uid;
    
    @Column
    private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getStauts() {
		return stauts;
	}

	public void setStauts(String stauts) {
		this.stauts = stauts;
	}

	public Date getPlan() {
		return plan;
	}

	public void setPlan(Date plan) {
		this.plan = plan;
	}

	public Date getSend() {
		return send;
	}

	public void setSend(Date send) {
		this.send = send;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	
    
    
    

}
