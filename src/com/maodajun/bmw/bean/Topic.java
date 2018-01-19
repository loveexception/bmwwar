package com.maodajun.bmw.bean;


import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("t_topics")

public class Topic  {
    @Id
    private int id;
    @Name
    @Column
    private String name;
    
    @Column
    private Integer readed;

    @Column
    private Integer readedplan;
    @Column
    private Integer liked;
    @Column
    private Integer likedplan;
    @Column
    private Integer collected;
    @Column
    private Integer collectedplan;
    @Column
    @Comment("原服务  id")
    private String oldid;
    @Column
    private String replies;
    
    @Column
    @Comment("是否新手贴")
    private String type;
    
    @Column
    private String sort;


	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Column
    @ColDefine(type=ColType.TEXT)
    private String content;
    
    @Many(field = "tid")
    private List<Subtopic> subtopics;

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

	public Integer getReaded() {
		return readed;
	}

	public void setReaded(Integer readed) {
		this.readed = readed;
	}

	public Integer getReadedplan() {
		return readedplan;
	}

	public void setReadedplan(Integer readedplan) {
		this.readedplan = readedplan;
	}

	public Integer getLiked() {
		return liked;
	}

	public void setLiked(Integer liked) {
		this.liked = liked;
	}

	public Integer getLikedplan() {
		return likedplan;
	}

	public void setLikedplan(Integer likedplan) {
		this.likedplan = likedplan;
	}

	public Integer getCollected() {
		return collected;
	}

	public void setCollected(Integer collected) {
		this.collected = collected;
	}

	
	public Integer getCollectedplan() {
		return collectedplan;
	}

	public void setCollectedplan(Integer collectedplan) {
		this.collectedplan = collectedplan;
	}

	public String getOldid() {
		return oldid;
	}

	public void setOldid(String oldid) {
		this.oldid = oldid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Subtopic> getSubtopics() {
		return subtopics;
	}

	public void setSubtopics(List<Subtopic> subtopics) {
		this.subtopics = subtopics;
	}  
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}


}
