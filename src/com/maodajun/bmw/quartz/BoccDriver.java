package com.maodajun.bmw.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.maodajun.bmw.bean.Login;
import com.maodajun.bmw.bean.Person;
import com.maodajun.bmw.bean.Subtopic;
import com.maodajun.bmw.bean.Topic;

public class BoccDriver {
	WebDriver driver;
	

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public static Map<Login, BoccDriver> createPersonsDriversByLogin(List<Login> logins) {
		Map<Login,BoccDriver> result =new HashMap<Login, BoccDriver>();
		for (Login login : logins) {
			BoccDriver bocc =new BoccDriver();
			WebDriver driver = new ChromeDriver();
			bocc.setDriver(driver);
			result.put(login,bocc);
		}
		return result;
	}

	public static Map<Subtopic, BoccDriver> createPersonsDriversBySubtopic(List<Subtopic> subtopics) {
		Map<Subtopic,BoccDriver> result =new HashMap<Subtopic, BoccDriver>();
		for (Subtopic login : subtopics) {
			BoccDriver bocc =new BoccDriver();
			WebDriver driver = new ChromeDriver();
			bocc.setDriver(driver);
			result.put(login,bocc);
		}
		return result;
	}



	public Login login(Person me) {
		// TODO Auto-generated method stub
		return null;
	}



	public List<Topic> readed(Person me, List<Topic> reads) {
		// TODO Auto-generated method stub
		return null;
	}



	public List<Topic> liked(Person me, List<Topic> likes) {
		// TODO Auto-generated method stub
		return null;
	}



	public List<Topic> colled(Person me, List<Topic> colls) {
		// TODO Auto-generated method stub
		return null;
	}

	public void subtopiced(Person me, String oldid, String context) {
		// TODO Auto-generated method stub
		
	}







}
