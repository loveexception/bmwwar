package com.maodajun.bmw.quartz;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.apache.catalina.startup.WebAnnotationSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Each;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.maodajun.bmw.bean.Login;

public class BmwChromeJobTest {
	static List<FreeProxy> proxys;
	MyProxy myproxy;
	BmwChromeJob job;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 proxys =  Ip3366Proxy.creatProxys(null);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	Dao dao = null;
	private WebDriver driver;
	@Before
	public void startUp(){
		// 创建一个数据源
		SimpleDataSource dataSource = new SimpleDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/bmw?useUnicode=true&characterEncoding=UTF-8");
		dataSource.setUsername("maodajun");
		dataSource.setPassword("maodajun");

		// 创建一个NutDao实例,在真实项目中, NutDao通常由ioc托管, 使用注入的方式获得.
		dao = new NutDao(dataSource);
		myproxy = new MyProxy();
		job = new BmwChromeJob();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAllProxy() throws Exception {
		for (FreeProxy object : proxys) {
			System.out.println(object.getIp()+":"+object.getPort());
		}
		assertEquals(proxys.size(),300);
	}
//	@Test 
//	public void testPerson(){
//		job.dao = dao ;
//		List list = new MyPerson().loginsByDate(new Date());
//		assertEquals(list.size(), 50);
//	}
//	@Test 
//	public void testRead(){
//		job.dao = dao ;
//		List 
//		list = job.topicRead();
//		assertEquals(list.size(), 5);
//
//		job.dao = dao ;
//		list = job.topicCollection();
//		assertEquals(list.size(), 2);
//		
//		list = job.topicLike();
//		assertEquals(list.size(), 1);
//	}
//	@Test
//	public void testLogin() throws Exception {
//		
//		Pager page = new Pager();
//		List<Login> logins = dao.query(Login.class, null,page );
//		
//		for (Login login : logins) {
//			login = dao.fetchLinks(login,"person");
//		}
//
//	
//		
//		myproxy.proxyLogin(logins, proxys);
//	}
//	@Test
//	public void testOpen() throws Exception{
//		System.setProperty("webdriver.chrome.driver","/Users/maodajun/Downloads/chromedriver");
//
//		WebDriver driver =new  ChromeDriver();
//		String url = "http://www.baidu.com";
//		job.openChrome(driver,url);
//		
//		
//	}

}
