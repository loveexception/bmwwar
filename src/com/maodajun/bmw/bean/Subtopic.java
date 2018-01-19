package com.maodajun.bmw.bean;


import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_subtopics")

public class Subtopic  {
    @Id
    private int id;
    
    @Column
    private int tid;
    @One(field="tid")
    private Topic topic;
    
    @Column
    @ColDefine(type=ColType.TEXT)
    private String context;
    
    @Column
    private int pid;
    @One(field = "pid")
    private Person person;
    
    @Column
    private Date send;
    @Column
    private Date plan;
    
    @Column
    @Comment("0立即发布，1按计划发布，9已发布，2连接失败，3服务失败，5暂停")
    private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Date getSend() {
		return send;
	}

	public void setSend(Date send) {
		this.send = send;
	}

	public Date getPlan() {
		return plan;
	}

	public void setPlan(Date plan) {
		this.plan = plan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
   
   
}
