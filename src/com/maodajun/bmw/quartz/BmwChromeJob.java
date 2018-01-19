package com.maodajun.bmw.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.nutz.castor.Castor;
import org.nutz.castor.Castors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.cri.Static;
import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Dumps;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.maodajun.bmw.bean.Login;
import com.maodajun.bmw.bean.Person;
import com.maodajun.bmw.bean.Subtopic;
import com.maodajun.bmw.bean.Topic;





@IocBean
public class BmwChromeJob implements Job {
	MyProxy proxy;
	
	@Inject  MyPerson myPerson ;
    @Inject Ip3366Proxy Ip3366Proxy ;
    private static final Log log = Logs.get();

    @Inject protected Dao dao;
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        log.info("测试 , start");
//
//        List<Login> logins=myPerson.loginsByDate(new Date());
//        Map<String,Integer> rate = loginsToRate(logins);
//        List<FreeProxy> proxys = Ip3366Proxy.creatProxys(rate);
//        logins = loginsSaveProxys(proxys,logins);
//        List<Topic> reads  = myPerson.findReadedPlan(new Date());
//        List<Topic> likes  = myPerson.findLikedPlan(new Date());
//        List<Topic> colls  = myPerson.findcolledPlan(new Date());
//        Map<Login,BoccDriver> loginsdrivers = BoccDriver.createPersonsDriversByLogin(logins);
//        
//        for (Map.Entry<Login, BoccDriver> entry : loginsdrivers.entrySet()) {
//        	BoccDriver driver = entry .getValue();
//        	Login login = entry.getKey();
//        	Person me = login.getPerson();
//        	try{
//            	login = driver.login(me);
//            	myPerson.save(login);
//            	reads = driver.readed(me,reads);
//            	likes = driver.liked(me,likes);
//            	colls = driver.colled(me,colls);        		
//        	}catch (Exception e) {
//				log.error(e);
//			}
//		}
//        
//        List<Topic> topics= myPerson.mix(reads, likes,colls);
//        myPerson.save(topics);
//        
//        List<Subtopic> subtopics = myPerson.subtopicByDate(new Date());
//        Map<Subtopic,BoccDriver> topicdrivers = BoccDriver.createPersonsDriversBySubtopic(subtopics);
//        for (Map.Entry<Subtopic, BoccDriver> entry : topicdrivers.entrySet()) {
//        	BoccDriver driver = entry .getValue();
//        	Subtopic sub = entry.getKey();
//        	Person me = sub.getPerson();
//        	Topic topic = sub.getTopic();
//        	try{
//        		driver.subtopiced(me,topic.getOldid(),sub.getContext());
//        		myPerson.save(sub);
//        	}catch (Exception e) {
//        		log.error(e);
//			}
//		}
//        
//        
//        log.info("clean Non-Active User , Done");
    }
	public Map<String, Integer> loginsToRate(List<Login> logins) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Login> loginsSaveProxys(List<FreeProxy> proxys, List<Login> logins) {
		// TODO Auto-generated method stub
		return null;
	}
 
    /**
     * 所有今天需要读的帖子
     * @return
     */
    public List<Topic> topicRead(){
    	Cnd cnd =Cnd.where(new Static("readed < readedplan"));
		List<Topic> topics = dao.query(Topic.class, cnd,null );
 		return topics;
    }
    /**
     * 所有今天需要收藏的帖子
     * @return
     */
    public List<Topic> topicCollection(){
    	Cnd cnd =Cnd.where(new Static("collected < collectedplan"));
		List<Topic> topics = dao.query(Topic.class, cnd,null );
 		return topics;
    }
   
    /**
     * 所有今天需要收藏的帖子
     * @return
     */
    public List<Topic> topicLike(){
    	Cnd cnd =Cnd.where(new Static("liked < likedplan"));
		List<Topic> topics = dao.query(Topic.class, cnd,null );
 		return topics;
    }
     public void openChrome(WebDriver driver, String url){
			driver.get(url);
     }
 
 

    
    
    
}